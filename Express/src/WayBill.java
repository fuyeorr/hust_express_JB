import java.sql.Timestamp;

public class WayBill {
    private Integer wayID;
    private Integer packageID;
    private Integer companyID;
    private String origin;
    private String destination;
    private Timestamp sendTime;
    private String wayStatus;

    public WayBill() {
    }

    public Integer getWayID() {
        return wayID;
    }

    public void setWayID(Integer wayID) {
        this.wayID = wayID;
    }

    public Integer getPackageID() {
        return packageID;
    }

    public void setPackageID(Integer packageID) {
        this.packageID = packageID;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Timestamp getSendTime() {
        return sendTime;
    }

    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
    }

    public String getWayStatus() {
        return wayStatus;
    }

    public void setWayStatus(String wayStatus) {
        this.wayStatus = wayStatus;
    }
}
