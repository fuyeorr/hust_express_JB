import java.util.List;

public class QueryService {

    private PackageDAO packageDAO = new PackageDAO();
    private OrderDAO orderDAO = new OrderDAO();
    private WayBillDAO wayBillDAO = new WayBillDAO();
    private TrackDAO trackDAO = new TrackDAO();
    private StorageDAO storageDAO = new StorageDAO();
    private ExceptionRecordDAO exceptionDAO = new ExceptionRecordDAO();
    private SignRecordDAO signDAO = new SignRecordDAO();

    public void queryPackageDetail(int packageID) {
        PackageEntity pkg = packageDAO.findByID(packageID);
        if (pkg == null) {
            System.out.println("包裹不存在");
            return;
        }

        OrderRecord order = orderDAO.findByID(pkg.getOrderID());
        WayBill wayBill = wayBillDAO.findByID(pkg.getPackageID());

        List<Track> tracks = trackDAO.findAll();
        List<Storage> storages = storageDAO.findAll();
        List<ExceptionRecord> exceptions = exceptionDAO.findAll();
        List<SignRecord> signs = signDAO.findAll();

        System.out.println("包裹状态：" + pkg.getCurrentStatus());
        System.out.println("订单号：" + order.getOrderID());

        tracks.stream()
                .filter(t -> t.getWayID() == wayBill.getWayID())
                .forEach(System.out::println);

        storages.stream()
                .filter(s -> s.getPackageID() == packageID)
                .forEach(System.out::println);

        exceptions.stream()
                .filter(e -> e.getPackageID() == packageID)
                .forEach(System.out::println);

        signs.stream()
                .filter(s -> s.getPackageID() == packageID)
                .forEach(System.out::println);
    }
}