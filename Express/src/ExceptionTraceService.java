import java.util.List;

public class ExceptionTraceService {

    private ExceptionRecordDAO exceptionDAO = new ExceptionRecordDAO();
    private PackageDAO packageDAO = new PackageDAO();
    private WayBillDAO wayBillDAO = new WayBillDAO();
    private TrackDAO trackDAO = new TrackDAO();
    private DeliveryManDAO deliveryManDAO = new DeliveryManDAO();
    private StorageDAO storageDAO = new StorageDAO();
    private StationDAO stationDAO = new StationDAO();
    private StaffDAO staffDAO = new StaffDAO();
    private SignRecordDAO signDAO = new SignRecordDAO();

    public void traceException(int exceptionID) {
        ExceptionRecord ex = exceptionDAO.findByID(exceptionID);
        PackageEntity pkg = packageDAO.findByID(ex.getPackageID());
        WayBill wayBill = wayBillDAO.findByID(pkg.getPackageID());

        System.out.println("异常类型：" + ex.getExceptionType());
        System.out.println("异常描述：" + ex.getDescription());

        List<Track> tracks = trackDAO.findAll();
        tracks.stream()
                .filter(t -> t.getWayID() == wayBill.getWayID())
                .forEach(t -> {
                    DeliveryMan man = deliveryManDAO.findByID(t.getDeliveryID());
                    System.out.println("责任快递员：" + man.getDelName());
                });

        List<Storage> storages = storageDAO.findAll();
        storages.stream()
                .filter(s -> s.getPackageID() == pkg.getPackageID())
                .forEach(s -> {
                    Station station = stationDAO.findByID(s.getStationID());
                    System.out.println("入库驿站：" + station.getStationName());
                });

        signDAO.findAll().stream()
                .filter(s -> s.getPackageID() == pkg.getPackageID())
                .forEach(s -> System.out.println("最终已签收"));
    }
}