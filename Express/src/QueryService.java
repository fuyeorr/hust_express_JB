import java.util.Collections;
import java.util.List;

public class QueryService {

    private final PackageDAO packageDAO = new PackageDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final WayBillDAO wayBillDAO = new WayBillDAO();
    private final TrackDAO trackDAO = new TrackDAO();
    private final StorageDAO storageDAO = new StorageDAO();
    private final ExceptionRecordDAO exceptionDAO = new ExceptionRecordDAO();
    private final SignRecordDAO signDAO = new SignRecordDAO();

    /**
     * 查询包裹详细信息
     */
    public PackageDetail queryPackageDetail(int packageID) {
        PackageEntity pkg = packageDAO.findByID(packageID);
        if (pkg == null) {
            return null;
        }

        OrderRecord order = orderDAO.findByID(pkg.getOrderID());
        WayBill wayBill = wayBillDAO.findByPackageID(packageID);

        List<Track> tracks = Collections.emptyList();
        if (wayBill != null) {
            tracks = trackDAO.findByWayID(wayBill.getWayID());
        }

        List<Storage> storages = storageDAO.findByPackageID(packageID);
        List<ExceptionRecord> exceptions = exceptionDAO.findByPackageID(packageID);
        List<SignRecord> signs = signDAO.findByPackageID(packageID);

        return new PackageDetail(pkg, order, wayBill, tracks, storages, exceptions, signs);
    }

    /**
     * 获取包裹信息
     */
    public PackageEntity getPackageInfo(int packageID) {
        return packageDAO.findByID(packageID);
    }

    /**
     * 获取所有包裹
     */
    public List<PackageEntity> getAllPackages() {
        List<PackageEntity> list = packageDAO.findAll();
        return list == null ? Collections.emptyList() : list;
    }

    /**
     * 获取包裹轨迹
     */
    public List<Track> getPackageTracks(int packageID) {
        WayBill wayBill = wayBillDAO.findByPackageID(packageID);
        if (wayBill == null) {
            return Collections.emptyList();
        }
        return trackDAO.findByWayID(wayBill.getWayID());
    }

    /**
     * 获取包裹异常记录
     */
    public List<ExceptionRecord> getPackageExceptions(int packageID) {
        return exceptionDAO.findByPackageID(packageID);
    }

    /**
     * 包裹详情结果类
     */
    public static class PackageDetail {
        private final PackageEntity pkg;
        private final OrderRecord order;
        private final WayBill wayBill;
        private final List<Track> tracks;
        private final List<Storage> storages;
        private final List<ExceptionRecord> exceptions;
        private final List<SignRecord> signs;

        public PackageDetail(PackageEntity pkg, OrderRecord order, WayBill wayBill,
                             List<Track> tracks, List<Storage> storages,
                             List<ExceptionRecord> exceptions, List<SignRecord> signs) {
            this.pkg = pkg;
            this.order = order;
            this.wayBill = wayBill;
            this.tracks = tracks;
            this.storages = storages;
            this.exceptions = exceptions;
            this.signs = signs;
        }

        public PackageEntity getPkg() {
            return pkg;
        }

        public OrderRecord getOrder() {
            return order;
        }

        public WayBill getWayBill() {
            return wayBill;
        }

        public List<Track> getTracks() {
            return tracks;
        }

        public List<Storage> getStorages() {
            return storages;
        }

        public List<ExceptionRecord> getExceptions() {
            return exceptions;
        }

        public List<SignRecord> getSigns() {
            return signs;
        }
    }
}
