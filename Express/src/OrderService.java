import java.sql.Timestamp;
import java.util.List;

public class OrderService {

    private final UserDAO userDAO = new UserDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final PackageDAO packageDAO = new PackageDAO();
    private final CompanyDAO companyDAO = new CompanyDAO();
    private final WayBillDAO wayBillDAO = new WayBillDAO();
    private final DeliveryManDAO deliveryManDAO = new DeliveryManDAO();
    private final TrackDAO trackDAO = new TrackDAO();
    private final StorageDAO storageDAO = new StorageDAO();
    private final StationDAO stationDAO = new StationDAO();
    private final SignRecordDAO signDAO = new SignRecordDAO();

    /**
     * 根据用户信息创建订单并自动分配运力
     */
    public PackageEntity createOrderWithUsers(String senderName, String senderPhone,
                                              String receiverName, String receiverPhone,
                                              PackageEntity pkg) {
        User sender = getOrCreateUser(senderName, senderPhone, "寄件");
        User receiver = getOrCreateUser(receiverName, receiverPhone, "收件");

        // 1. 创建订单和包裹
        createOrder(sender.getUserID(), receiver.getUserID(), pkg);

        // 2. 分配快递公司和快递员
        assignDelivery(pkg.getPackageID());

        // 3. 模拟运输到站点并入库（使用第一个站点）
        List<Station> stations = stationDAO.findAll();
        if (stations != null && !stations.isEmpty()) {
            int stationID = stations.get(0).getStationID();
            storageIn(pkg.getPackageID(), stationID);
        }

        return pkg;
    }

    /**
     * 1. 用户下单
     */
    public void createOrder(int senderID, int receiverID, PackageEntity pkg) {
        // 验证用户存在
        User sender = userDAO.findByID(senderID);
        User receiver = userDAO.findByID(receiverID);
        if (sender == null || receiver == null) {
            throw new RuntimeException("用户不存在");
        }

        // 创建订单
        OrderRecord order = new OrderRecord();
        order.setSenderID(senderID);
        order.setReceiverID(receiverID);
        order.setOrderStatus("正常");
        order.setStartTime(new Timestamp(System.currentTimeMillis()));

        if (!orderDAO.insert(order)) {
            throw new RuntimeException("订单创建失败");
        }

        if (order.getOrderID() == null) {
            throw new RuntimeException("订单ID未生成");
        }

        // 创建包裹
        pkg.setOrderID(order.getOrderID());
        pkg.setCurrentStatus("已下单");

        if (!packageDAO.insert(pkg)) {
            throw new RuntimeException("包裹创建失败");
        }

        if (pkg.getPackageID() == null) {
            throw new RuntimeException("包裹ID未生成");
        }
    }

    /**
     * 2. 分配快递公司和快递员
     */
    public void assignDelivery(int packageID) {
        PackageEntity pkg = packageDAO.findByID(packageID);
        if (pkg == null) {
            throw new RuntimeException("包裹不存在");
        }

        // 选择快递公司（简单选择第一个）
        List<Company> companies = companyDAO.findAll();
        if (companies == null || companies.isEmpty()) {
            throw new RuntimeException("没有可用的快递公司");
        }
        Company company = companies.get(0);

        // 选择快递员（简单选择第一个）
        List<DeliveryMan> deliveryMen = deliveryManDAO.findAll();
        if (deliveryMen == null || deliveryMen.isEmpty()) {
            throw new RuntimeException("没有可用的快递员");
        }
        DeliveryMan deliveryMan = deliveryMen.get(0);

        // 创建运单
        WayBill wayBill = new WayBill();
        wayBill.setPackageID(packageID);
        wayBill.setCompanyID(company.getCompanyID());
        wayBill.setWayStatus("已揽收");
        wayBill.setSendTime(new Timestamp(System.currentTimeMillis()));

        if (!wayBillDAO.insert(wayBill)) {
            throw new RuntimeException("运单创建失败");
        }

        if (wayBill.getWayID() == null) {
            throw new RuntimeException("运单ID未生成");
        }

        // 创建初始轨迹记录
        Track track = new Track();
        track.setWayID(wayBill.getWayID());
        track.setDeliveryID(deliveryMan.getDeliveryID());
        track.setTrackTime(new Timestamp(System.currentTimeMillis()));
        track.setCurrentLocation("揽收站点");
        track.setTrackInfo("包裹已揽收，准备运输");

        if (!trackDAO.insert(track)) {
            throw new RuntimeException("轨迹创建失败");
        }

        // 更新包裹状态
        pkg.setCurrentStatus("运输中");
        if (!packageDAO.update(pkg)) {
            throw new RuntimeException("包裹状态更新失败");
        }
    }

    /**
     * 3. 更新运输轨迹
     */
    public void updateTrack(int wayID, int deliveryID, String location, String info) {
        WayBill wayBill = wayBillDAO.findByID(wayID);
        if (wayBill == null) {
            throw new RuntimeException("运单不存在");
        }

        Track track = new Track();
        track.setWayID(wayID);
        track.setDeliveryID(deliveryID);
        track.setTrackTime(new Timestamp(System.currentTimeMillis()));
        track.setCurrentLocation(location);
        track.setTrackInfo(info);

        if (!trackDAO.insert(track)) {
            throw new RuntimeException("轨迹创建失败");
        }
    }

    /**
     * 4. 入库
     */
    public void storageIn(int packageID, int stationID) {
        PackageEntity pkg = packageDAO.findByID(packageID);
        if (pkg == null) {
            throw new RuntimeException("包裹不存在");
        }

        // 添加运输轨迹（到达站点）
        WayBill wayBill = wayBillDAO.findByPackageID(packageID);
        if (wayBill != null) {
            List<DeliveryMan> deliveryMen = deliveryManDAO.findAll();
            if (deliveryMen != null && !deliveryMen.isEmpty()) {
                Track track = new Track();
                track.setWayID(wayBill.getWayID());
                track.setDeliveryID(deliveryMen.get(0).getDeliveryID());
                track.setTrackTime(new Timestamp(System.currentTimeMillis()));
                track.setCurrentLocation("目的地站点");
                track.setTrackInfo("包裹已到达站点，准备入库");
                trackDAO.insert(track);
            }
        }

        Storage storage = new Storage();
        storage.setPackageID(packageID);
        storage.setStationID(stationID);
        storage.setStorageStatus("正常");
        storage.setStorageTime(new Timestamp(System.currentTimeMillis()));

        if (!storageDAO.insert(storage)) {
            throw new RuntimeException("入库失败");
        }

        // 更新包裹状态
        pkg.setCurrentStatus("已入库");
        if (!packageDAO.update(pkg)) {
            throw new RuntimeException("包裹状态更新失败");
        }
    }

    /**
     * 5. 签收
     */
    public void sign(int packageID, int receiverID, String signType) {
        PackageEntity pkg = packageDAO.findByID(packageID);
        if (pkg == null) {
            throw new RuntimeException("包裹不存在");
        }

        User receiver = userDAO.findByID(receiverID);
        if (receiver == null) {
            throw new RuntimeException("收件人不存在");
        }

        // 从入库记录中获取最新的站点ID
        List<Storage> storages = storageDAO.findByPackageID(packageID);
        if (storages == null || storages.isEmpty()) {
            throw new RuntimeException("包裹尚未入库，无法签收");
        }
        // 获取最新的入库记录（最后一个）
        Storage latestStorage = storages.get(storages.size() - 1);

        SignRecord sign = new SignRecord();
        sign.setPackageID(packageID);
        sign.setStationID(latestStorage.getStationID());
        sign.setReceiverID(receiverID);
        sign.setSignType(signType);
        sign.setSignTime(new Timestamp(System.currentTimeMillis()));

        if (!signDAO.insert(sign)) {
            throw new RuntimeException("签收失败");
        }

        // 更新包裹状态
        pkg.setCurrentStatus("已签收");
        if (!packageDAO.update(pkg)) {
            throw new RuntimeException("包裹状态更新失败");
        }
    }

    /**
     * 获取或创建用户
     */
    private User getOrCreateUser(String name, String phone, String userType) {
        // 使用 DAO 的 findByPhone 方法查找
        User existing = userDAO.findByPhone(phone);
        if (existing != null) {
            return existing;
        }

        // 创建新用户
        User user = new User();
        user.setName(name);
        user.setPhone(phone);
        user.setUserType(userType);

        if (!userDAO.insert(user)) {
            throw new RuntimeException("用户创建失败");
        }

        if (user.getUserID() == null) {
            throw new RuntimeException("用户ID未生成");
        }

        return user;
    }
}
