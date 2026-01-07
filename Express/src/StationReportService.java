import java.util.List;

public class StationReportService {

    private StorageDAO storageDAO = new StorageDAO();
    private SignRecordDAO signDAO = new SignRecordDAO();
    private ExceptionRecordDAO exceptionDAO = new ExceptionRecordDAO();
    private TrackDAO trackDAO = new TrackDAO();

    public void stationDailyReport(int stationID) {
        List<Storage> storages = storageDAO.findAll();
        List<SignRecord> signs = signDAO.findAll();
        List<ExceptionRecord> exceptions = exceptionDAO.findAll();
        List<Track> tracks = trackDAO.findAll();

        long storageCount = storages.stream()
                .filter(s -> s.getStationID() == stationID)
                .count();

        long signCount = signs.stream().count();
        long exceptionCount = exceptions.size();

        System.out.println("入库数量：" + storageCount);
        System.out.println("签收数量：" + signCount);
        System.out.println("异常数量：" + exceptionCount);

        tracks.forEach(t ->
                System.out.println("快递员 " + t.getDeliveryID() + " 完成一次配送")
        );
    }
}