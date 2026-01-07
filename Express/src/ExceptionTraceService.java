import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ExceptionTraceService {

    private final ExceptionRecordDAO exceptionDAO = new ExceptionRecordDAO();
    private final PackageDAO packageDAO = new PackageDAO();
    private final WayBillDAO wayBillDAO = new WayBillDAO();
    private final TrackDAO trackDAO = new TrackDAO();
    private final DeliveryManDAO deliveryManDAO = new DeliveryManDAO();
    private final StorageDAO storageDAO = new StorageDAO();
    private final StationDAO stationDAO = new StationDAO();
    private final SignRecordDAO signDAO = new SignRecordDAO();

    /**
     * 追踪异常详情
     */
    public ExceptionTrace traceException(int exceptionID) {
        ExceptionRecord ex = exceptionDAO.findByID(exceptionID);
        if (ex == null) {
            return null;
        }

        PackageEntity pkg = packageDAO.findByID(ex.getPackageID());
        if (pkg == null) {
            return null;
        }

        WayBill wayBill = wayBillDAO.findByPackageID(pkg.getPackageID());

        List<Track> tracks = Collections.emptyList();
        List<DeliveryMan> deliveryMen = Collections.emptyList();
        if (wayBill != null) {
            tracks = trackDAO.findByWayID(wayBill.getWayID());
            // 获取所有相关的快递员
            deliveryMen = tracks.stream()
                    .map(t -> deliveryManDAO.findByID(t.getDeliveryID()))
                    .filter(d -> d != null)
                    .distinct()
                    .collect(Collectors.toList());
        }

        // 获取包裹经过的所有站点
        List<Storage> storages = storageDAO.findByPackageID(pkg.getPackageID());
        List<Station> stations = storages.stream()
                .map(s -> stationDAO.findByID(s.getStationID()))
                .filter(st -> st != null)
                .distinct()
                .collect(Collectors.toList());

        // 获取签收记录
        List<SignRecord> signs = signDAO.findByPackageID(pkg.getPackageID());

        return new ExceptionTrace(ex, pkg, wayBill, tracks, deliveryMen, stations, signs);
    }

    /**
     * 异常追踪结果类
     */
    public static class ExceptionTrace {
        private final ExceptionRecord exceptionRecord;
        private final PackageEntity pkg;
        private final WayBill wayBill;
        private final List<Track> tracks;
        private final List<DeliveryMan> deliveryMen;
        private final List<Station> stations;
        private final List<SignRecord> signRecords;

        public ExceptionTrace(ExceptionRecord exceptionRecord, PackageEntity pkg, WayBill wayBill,
                              List<Track> tracks, List<DeliveryMan> deliveryMen,
                              List<Station> stations, List<SignRecord> signRecords) {
            this.exceptionRecord = exceptionRecord;
            this.pkg = pkg;
            this.wayBill = wayBill;
            this.tracks = tracks;
            this.deliveryMen = deliveryMen;
            this.stations = stations;
            this.signRecords = signRecords;
        }

        public ExceptionRecord getExceptionRecord() {
            return exceptionRecord;
        }

        public PackageEntity getPkg() {
            return pkg;
        }

        public WayBill getWayBill() {
            return wayBill;
        }

        public List<Track> getTracks() {
            return tracks;
        }

        public List<DeliveryMan> getDeliveryMen() {
            return deliveryMen;
        }

        public List<Station> getStations() {
            return stations;
        }

        public List<SignRecord> getSignRecords() {
            return signRecords;
        }
    }
}
