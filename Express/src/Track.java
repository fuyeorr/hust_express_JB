import java.sql.Timestamp;

public class Track {
    private Integer trackID;
    private Integer wayID;
    private Integer deliveryID;
    private Timestamp trackTime;
    private String currentLocation;
    private String trackInfo;

    public Track() {
    }

    public Integer getTrackID() {
        return trackID;
    }

    public void setTrackID(Integer trackID) {
        this.trackID = trackID;
    }

    public Integer getWayID() {
        return wayID;
    }

    public void setWayID(Integer wayID) {
        this.wayID = wayID;
    }

    public Integer getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(Integer deliveryID) {
        this.deliveryID = deliveryID;
    }

    public Timestamp getTrackTime() {
        return trackTime;
    }

    public void setTrackTime(Timestamp trackTime) {
        this.trackTime = trackTime;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getTrackInfo() {
        return trackInfo;
    }

    public void setTrackInfo(String trackInfo) {
        this.trackInfo = trackInfo;
    }

    // UI
    public java.sql.Timestamp getUpdateTime() {
        return getTrackTime();
    }

    public String getLocation() {
        return getCurrentLocation();
    }

    public String getStatus() {
        // no dedicated status field; reuse trackInfo as status/description
        return getTrackInfo();
    }

    public String getDescription() {
        return getTrackInfo();
    }
}
