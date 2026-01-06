import java.sql.Timestamp;

public class SignRecord {
    private Integer signID;
    private Integer packageID;
    private Integer stationID;
    private Integer receiverID;
    private String signType;
    private Timestamp signTime;

    public SignRecord() {
    }

    public Integer getSignID() {
        return signID;
    }

    public void setSignID(Integer signID) {
        this.signID = signID;
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

    public Integer getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(Integer receiverID) {
        this.receiverID = receiverID;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public Timestamp getSignTime() {
        return signTime;
    }

    public void setSignTime(Timestamp signTime) {
        this.signTime = signTime;
    }
}
