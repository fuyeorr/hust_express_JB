import java.sql.Timestamp;

public class Storage {
    private Integer packageID;
    private Integer stationID;
    private Timestamp storageTime;
    private String storageCode;
    private String storageStatus;

    public Storage() {
    }

    public Integer getPackageID() {
        return packageID;
    }

    public void setPackageID(Integer packageID) {
        this.packageID = packageID;
    }

    public Integer getStationID() {
        return stationID;
    }

    public void setStationID(Integer stationID) {
        this.stationID = stationID;
    }

    public Timestamp getStorageTime() {
        return storageTime;
    }

    public void setStorageTime(Timestamp storageTime) {
        this.storageTime = storageTime;
    }

    public String getStorageCode() {
        return storageCode;
    }

    public void setStorageCode(String storageCode) {
        this.storageCode = storageCode;
    }

    public String getStorageStatus() {
        return storageStatus;
    }

    public void setStorageStatus(String storageStatus) {
        this.storageStatus = storageStatus;
    }
}
