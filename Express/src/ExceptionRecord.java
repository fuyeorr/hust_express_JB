public class ExceptionRecord {
    private Integer exceptionID;
    private Integer packageID;
    private String exceptionType;
    private String exceptionName;
    private String description;

    public ExceptionRecord() {
    }

    public Integer getExceptionID() {
        return exceptionID;
    }

    public void setExceptionID(Integer exceptionID) {
        this.exceptionID = exceptionID;
    }

    public Integer getPackageID() {
        return packageID;
    }

    public void setPackageID(Integer packageID) {
        this.packageID = packageID;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    public String getExceptionName() {
        return exceptionName;
    }

    public void setExceptionName(String exceptionName) {
        this.exceptionName = exceptionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
