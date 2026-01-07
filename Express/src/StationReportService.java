import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StationReportService {

    private final StorageDAO storageDAO = new StorageDAO();
    private final SignRecordDAO signDAO = new SignRecordDAO();
    private final ExceptionRecordDAO exceptionDAO = new ExceptionRecordDAO();
    private final TrackDAO trackDAO = new TrackDAO();

    /**
     * 生成站点日报
     * 统计站点的入库、签收、异常和快递员配送情况
     */
    public StationReportResult stationDailyReport(int stationID) {
        // 使用 DAO 的统计方法直接获取数量
        long storageCount = storageDAO.countByStationID(stationID);
        long signCount = signDAO.countByStationID(stationID);
        long exceptionCount = exceptionDAO.countByStationID(stationID);

        // 获取快递员配送统计
        Map<Integer, Long> deliveryCounts = trackDAO.countByDeliveryID();
        List<StationReportResult.DeliveryStat> deliveryStats = deliveryCounts.entrySet().stream()
                .map(e -> new StationReportResult.DeliveryStat(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        return new StationReportResult(storageCount, signCount, exceptionCount, deliveryStats);
    }

    /**
     * 站点报表结果类
     */
    public static class StationReportResult {
        private final long storageCount;
        private final long signCount;
        private final long exceptionCount;
        private final List<DeliveryStat> deliveryStats;

        public StationReportResult(long storageCount, long signCount, long exceptionCount, List<DeliveryStat> deliveryStats) {
            this.storageCount = storageCount;
            this.signCount = signCount;
            this.exceptionCount = exceptionCount;
            this.deliveryStats = deliveryStats;
        }

        public long getStorageCount() {
            return storageCount;
        }

        public long getSignCount() {
            return signCount;
        }

        public long getExceptionCount() {
            return exceptionCount;
        }

        public List<DeliveryStat> getDeliveryStats() {
            return deliveryStats;
        }

        /**
         * 快递员配送统计
         */
        public static class DeliveryStat {
            private final Integer deliveryId;
            private final long deliveryCount;

            public DeliveryStat(Integer deliveryId, long deliveryCount) {
                this.deliveryId = deliveryId;
                this.deliveryCount = deliveryCount;
            }

            public Integer getDeliveryId() {
                return deliveryId;
            }

            public long getDeliveryCount() {
                return deliveryCount;
            }
        }
    }
}
