public class StorageKey {
    private Integer packageID;
    private Integer stationID;

    public StorageKey() {
    }

    public StorageKey(Integer packageID, Integer stationID) {
        this.packageID = packageID;
        this.stationID = stationID;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StorageKey)) return false;
        StorageKey that = (StorageKey) o;
        return packageID != null && packageID.equals(that.packageID)
                && stationID != null && stationID.equals(that.stationID);
    }

    @Override
    public int hashCode() {
        int result = packageID != null ? packageID.hashCode() : 0;
        result = 31 * result + (stationID != null ? stationID.hashCode() : 0);
        return result;
    }
}
