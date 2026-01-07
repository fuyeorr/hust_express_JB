
import java.sql.Timestamp;
import java.util.List;

public class OrderService {

    private UserDAO userDAO = new UserDAO();
    private OrderDAO orderDAO = new OrderDAO();
    private PackageDAO packageDAO = new PackageDAO();
    private CompanyDAO companyDAO = new CompanyDAO();
    private WayBillDAO wayBillDAO = new WayBillDAO();
    private DeliveryManDAO deliveryManDAO = new DeliveryManDAO();
    private TrackDAO trackDAO = new TrackDAO();
    private StorageDAO storageDAO = new StorageDAO();
    private SignRecordDAO signDAO = new SignRecordDAO();
    private ExceptionRecordDAO exceptionDAO = new ExceptionRecordDAO();

    /** 1. 用户下单 */
    public void createOrder(int senderID, int receiverID, PackageEntity pkg) {
        if (userDAO.findByID(senderID) == null ||
                userDAO.findByID(receiverID) == null) {
            throw new RuntimeException("用户不存在");
        }

        OrderRecord order = new OrderRecord();
        order.setSenderID(senderID);
        order.setReceiverID(receiverID);
        order.setOrderStatus("正常");
        order.setStartTime(new Timestamp(System.currentTimeMillis()));
        orderDAO.insert(order);

        pkg.setOrderID(order.getOrderID());
        pkg.setCurrentStatus("已下单");
        packageDAO.insert(pkg);
    }

    /** 2. 分配快递公司和快递员 */
    public void assignDelivery(int packageID) {
        List<Company> companies = companyDAO.findAll();
        Company company = companies.get(0);

        List<DeliveryMan> men = deliveryManDAO.findAll();
        DeliveryMan man = men.get(0);

        WayBill wayBill = new WayBill();
        wayBill.setPackageID(packageID);
        wayBill.setCompanyID(company.getCompanyID());
        wayBill.setWayStatus("已揽收");
        wayBillDAO.insert(wayBill);

        PackageEntity pkg = packageDAO.findByID(packageID);
        pkg.setCurrentStatus("运输中");
        packageDAO.update(pkg);
    }

    /** 3. 更新运输轨迹 */
    public void updateTrack(int wayID, int deliveryID, String location, String info) {
        Track track = new Track();
        track.setWayID(wayID);
        track.setDeliveryID(deliveryID);
        track.setTrackTime(new Timestamp(System.currentTimeMillis()));
        track.setCurrentLocation(location);
        track.setTrackInfo(info);
        trackDAO.insert(track);
    }

    /** 4. 入库 */
    public void storageIn(int packageID, int stationID) {
        Storage storage = new Storage();
        storage.setPackageID(packageID);
        storage.setStationID(stationID);
        storage.setStorageStatus("正常");
        storage.setStorageTime(new Timestamp(System.currentTimeMillis()));
        storageDAO.insert(storage);

        PackageEntity pkg = packageDAO.findByID(packageID);
        pkg.setCurrentStatus("已入库");
        packageDAO.update(pkg);
    }

    /** 5. 签收 */
    public void sign(int packageID, int receiverID, String signType) {
        SignRecord sign = new SignRecord();
        sign.setPackageID(packageID);
        sign.setReceiverID(receiverID);
        sign.setSignType(signType);
        sign.setSignTime(new Timestamp(System.currentTimeMillis()));
        signDAO.insert(sign);

        PackageEntity pkg = packageDAO.findByID(packageID);
        pkg.setCurrentStatus("已签收");
        packageDAO.update(pkg);
    }
}