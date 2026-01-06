import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StationDAO extends BaseDAO<Station, Integer> {
    private static final String TABLE = "`Station`";

    @Override
    protected String getFindAllSQL() {
        return "SELECT stationID, stationName, location, stationCode, stationPhone FROM " + TABLE;
    }

    @Override
    protected String getFindByIDSQL() {
        return "SELECT stationID, stationName, location, stationCode, stationPhone FROM " + TABLE + " WHERE stationID = ?";
    }

    @Override
    protected void SetParameter(PreparedStatement pstmt, int pos, Integer id) throws SQLException {
        pstmt.setInt(pos, id);
    }

    @Override
    protected Station mapResultSetToEntity(ResultSet rs) throws SQLException {
        Station station = new Station();
        station.setStationID(rs.getInt("stationID"));
        station.setStationName(rs.getString("stationName"));
        station.setLocation(rs.getString("location"));
        station.setStationCode(rs.getString("stationCode"));
        station.setStationPhone(rs.getString("stationPhone"));
        return station;
    }

    @Override
    protected void setUpdateParameters(PreparedStatement pstmt, Station entity) {
        try {
            pstmt.setString(1, entity.getStationName());
            pstmt.setString(2, entity.getLocation());
            pstmt.setString(3, entity.getStationCode());
            pstmt.setString(4, entity.getStationPhone());
            pstmt.setInt(5, entity.getStationID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getUpdateSQL() {
        return "UPDATE " + TABLE + " SET stationName = ?, location = ?, stationCode = ?, stationPhone = ? WHERE stationID = ?";
    }

    @Override
    protected String getDeleteSQL() {
        return "DELETE FROM " + TABLE + " WHERE stationID = ?";
    }

    @Override
    protected void setIdParameter(PreparedStatement pstmt, int i, Integer id) {
        try {
            pstmt.setInt(i, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setGeneratedId(Station entity, ResultSet rs) throws SQLException {
        entity.setStationID(rs.getInt(1));
    }

    @Override
    protected void SetInsertParameter(PreparedStatement pstmt, Station entity) {
        try {
            pstmt.setString(1, entity.getStationName());
            pstmt.setString(2, entity.getLocation());
            pstmt.setString(3, entity.getStationCode());
            pstmt.setString(4, entity.getStationPhone());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO " + TABLE + " (stationName, location, stationCode, stationPhone) VALUES (?, ?, ?, ?)";
    }
}

*** Add File: /Users/amiriox/Public/hust_express_JB/Express/src/StaffDAO.java
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StaffDAO extends BaseDAO<Staff, Integer> {
    private static final String TABLE = "`Staff`";

    @Override
    protected String getFindAllSQL() {
        return "SELECT staffID, stationID, staffName, staffSex, staffPhone, staffRole FROM " + TABLE;
    }

    @Override
    protected String getFindByIDSQL() {
        return "SELECT staffID, stationID, staffName, staffSex, staffPhone, staffRole FROM " + TABLE + " WHERE staffID = ?";
    }

    @Override
    protected void SetParameter(PreparedStatement pstmt, int pos, Integer id) throws SQLException {
        pstmt.setInt(pos, id);
    }

    @Override
    protected Staff mapResultSetToEntity(ResultSet rs) throws SQLException {
        Staff staff = new Staff();
        staff.setStaffID(rs.getInt("staffID"));
        staff.setStationID(rs.getInt("stationID"));
        staff.setStaffName(rs.getString("staffName"));
        staff.setStaffSex(rs.getString("staffSex"));
        staff.setStaffPhone(rs.getString("staffPhone"));
        staff.setStaffRole(rs.getString("staffRole"));
        return staff;
    }

    @Override
    protected void setUpdateParameters(PreparedStatement pstmt, Staff entity) {
        try {
            pstmt.setInt(1, entity.getStationID());
            pstmt.setString(2, entity.getStaffName());
            pstmt.setString(3, entity.getStaffSex());
            pstmt.setString(4, entity.getStaffPhone());
            pstmt.setString(5, entity.getStaffRole());
            pstmt.setInt(6, entity.getStaffID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getUpdateSQL() {
        return "UPDATE " + TABLE + " SET stationID = ?, staffName = ?, staffSex = ?, staffPhone = ?, staffRole = ? WHERE staffID = ?";
    }

    @Override
    protected String getDeleteSQL() {
        return "DELETE FROM " + TABLE + " WHERE staffID = ?";
    }

    @Override
    protected void setIdParameter(PreparedStatement pstmt, int i, Integer id) {
        try {
            pstmt.setInt(i, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setGeneratedId(Staff entity, ResultSet rs) throws SQLException {
        entity.setStaffID(rs.getInt(1));
    }

    @Override
    protected void SetInsertParameter(PreparedStatement pstmt, Staff entity) {
        try {
            pstmt.setInt(1, entity.getStationID());
            pstmt.setString(2, entity.getStaffName());
            pstmt.setString(3, entity.getStaffSex());
            pstmt.setString(4, entity.getStaffPhone());
            pstmt.setString(5, entity.getStaffRole());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO " + TABLE + " (stationID, staffName, staffSex, staffPhone, staffRole) VALUES (?, ?, ?, ?, ?)";
    }
}

*** Add File: /Users/amiriox/Public/hust_express_JB/Express/src/ExceptionRecordDAO.java
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExceptionRecordDAO extends BaseDAO<ExceptionRecord, Integer> {
    private static final String TABLE = "`Exception`";

    @Override
    protected String getFindAllSQL() {
        return "SELECT exceptionID, packageID, exceptionType, exceptionName, description FROM " + TABLE;
    }

    @Override
    protected String getFindByIDSQL() {
        return "SELECT exceptionID, packageID, exceptionType, exceptionName, description FROM " + TABLE + " WHERE exceptionID = ?";
    }

    @Override
    protected void SetParameter(PreparedStatement pstmt, int pos, Integer id) throws SQLException {
        pstmt.setInt(pos, id);
    }

    @Override
    protected ExceptionRecord mapResultSetToEntity(ResultSet rs) throws SQLException {
        ExceptionRecord exceptionRecord = new ExceptionRecord();
        exceptionRecord.setExceptionID(rs.getInt("exceptionID"));
        exceptionRecord.setPackageID(rs.getInt("packageID"));
        exceptionRecord.setExceptionType(rs.getString("exceptionType"));
        exceptionRecord.setExceptionName(rs.getString("exceptionName"));
        exceptionRecord.setDescription(rs.getString("description"));
        return exceptionRecord;
    }

    @Override
    protected void setUpdateParameters(PreparedStatement pstmt, ExceptionRecord entity) {
        try {
            pstmt.setInt(1, entity.getPackageID());
            pstmt.setString(2, entity.getExceptionType());
            pstmt.setString(3, entity.getExceptionName());
            pstmt.setString(4, entity.getDescription());
            pstmt.setInt(5, entity.getExceptionID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getUpdateSQL() {
        return "UPDATE " + TABLE + " SET packageID = ?, exceptionType = ?, exceptionName = ?, description = ? WHERE exceptionID = ?";
    }

    @Override
    protected String getDeleteSQL() {
        return "DELETE FROM " + TABLE + " WHERE exceptionID = ?";
    }

    @Override
    protected void setIdParameter(PreparedStatement pstmt, int i, Integer id) {
        try {
            pstmt.setInt(i, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setGeneratedId(ExceptionRecord entity, ResultSet rs) throws SQLException {
        entity.setExceptionID(rs.getInt(1));
    }

    @Override
    protected void SetInsertParameter(PreparedStatement pstmt, ExceptionRecord entity) {
        try {
            pstmt.setInt(1, entity.getPackageID());
            pstmt.setString(2, entity.getExceptionType());
            pstmt.setString(3, entity.getExceptionName());
            pstmt.setString(4, entity.getDescription());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO " + TABLE + " (packageID, exceptionType, exceptionName, description) VALUES (?, ?, ?, ?)";
    }
}

*** Add File: /Users/amiriox/Public/hust_express_JB/Express/src/StorageDAO.java
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class StorageDAO extends BaseDAO<Storage, StorageKey> {
    private static final String TABLE = "`Storage`";

    @Override
    protected String getFindAllSQL() {
        return "SELECT packageID, stationID, storageTime, storageCode, storageStatus FROM " + TABLE;
    }

    @Override
    protected String getFindByIDSQL() {
        return "SELECT packageID, stationID, storageTime, storageCode, storageStatus FROM " + TABLE + " WHERE packageID = ? AND stationID = ?";
    }

    @Override
    protected void SetParameter(PreparedStatement pstmt, int pos, StorageKey id) throws SQLException {
        pstmt.setInt(pos, id.getPackageID());
        pstmt.setInt(pos + 1, id.getStationID());
    }

    @Override
    protected Storage mapResultSetToEntity(ResultSet rs) throws SQLException {
        Storage storage = new Storage();
        storage.setPackageID(rs.getInt("packageID"));
        storage.setStationID(rs.getInt("stationID"));
        storage.setStorageTime(rs.getTimestamp("storageTime"));
        storage.setStorageCode(rs.getString("storageCode"));
        storage.setStorageStatus(rs.getString("storageStatus"));
        return storage;
    }

    @Override
    protected void setUpdateParameters(PreparedStatement pstmt, Storage entity) {
        try {
            if (entity.getStorageTime() != null) {
                pstmt.setTimestamp(1, entity.getStorageTime());
            } else {
                pstmt.setNull(1, Types.TIMESTAMP);
            }
            pstmt.setString(2, entity.getStorageCode());
            pstmt.setString(3, entity.getStorageStatus());
            pstmt.setInt(4, entity.getPackageID());
            pstmt.setInt(5, entity.getStationID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getUpdateSQL() {
        return "UPDATE " + TABLE + " SET storageTime = ?, storageCode = ?, storageStatus = ? WHERE packageID = ? AND stationID = ?";
    }

    @Override
    protected String getDeleteSQL() {
        return "DELETE FROM " + TABLE + " WHERE packageID = ? AND stationID = ?";
    }

    @Override
    protected void setIdParameter(PreparedStatement pstmt, int i, StorageKey id) {
        try {
            pstmt.setInt(i, id.getPackageID());
            pstmt.setInt(i + 1, id.getStationID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setGeneratedId(Storage entity, ResultSet rs) {
        // Storage uses composite key; nothing to set
    }

    @Override
    protected void SetInsertParameter(PreparedStatement pstmt, Storage entity) {
        try {
            if (entity.getStorageTime() != null) {
                pstmt.setTimestamp(1, entity.getStorageTime());
            } else {
                pstmt.setNull(1, Types.TIMESTAMP);
            }
            pstmt.setString(2, entity.getStorageCode());
            pstmt.setString(3, entity.getStorageStatus());
            pstmt.setInt(4, entity.getPackageID());
            pstmt.setInt(5, entity.getStationID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO " + TABLE + " (storageTime, storageCode, storageStatus, packageID, stationID) VALUES (?, ?, ?, ?, ?)";
    }
}

*** Add File: /Users/amiriox/Public/hust_express_JB/Express/src/SignRecordDAO.java
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class SignRecordDAO extends BaseDAO<SignRecord, Integer> {
    private static final String TABLE = "`Sign`";

    @Override
    protected String getFindAllSQL() {
        return "SELECT signID, packageID, stationID, receiverID, signType, signTime FROM " + TABLE;
    }

    @Override
    protected String getFindByIDSQL() {
        return "SELECT signID, packageID, stationID, receiverID, signType, signTime FROM " + TABLE + " WHERE signID = ?";
    }

    @Override
    protected void SetParameter(PreparedStatement pstmt, int pos, Integer id) throws SQLException {
        pstmt.setInt(pos, id);
    }

    @Override
    protected SignRecord mapResultSetToEntity(ResultSet rs) throws SQLException {
        SignRecord signRecord = new SignRecord();
        signRecord.setSignID(rs.getInt("signID"));
        signRecord.setPackageID(rs.getInt("packageID"));
        signRecord.setStationID(rs.getInt("stationID"));
        signRecord.setReceiverID(rs.getInt("receiverID"));
        signRecord.setSignType(rs.getString("signType"));
        signRecord.setSignTime(rs.getTimestamp("signTime"));
        return signRecord;
    }

    @Override
    protected void setUpdateParameters(PreparedStatement pstmt, SignRecord entity) {
        try {
            pstmt.setInt(1, entity.getPackageID());
            pstmt.setInt(2, entity.getStationID());
            pstmt.setInt(3, entity.getReceiverID());
            pstmt.setString(4, entity.getSignType());
            if (entity.getSignTime() != null) {
                pstmt.setTimestamp(5, entity.getSignTime());
            } else {
                pstmt.setNull(5, Types.TIMESTAMP);
            }
            pstmt.setInt(6, entity.getSignID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getUpdateSQL() {
        return "UPDATE " + TABLE + " SET packageID = ?, stationID = ?, receiverID = ?, signType = ?, signTime = ? WHERE signID = ?";
    }

    @Override
    protected String getDeleteSQL() {
        return "DELETE FROM " + TABLE + " WHERE signID = ?";
    }

    @Override
    protected void setIdParameter(PreparedStatement pstmt, int i, Integer id) {
        try {
            pstmt.setInt(i, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setGeneratedId(SignRecord entity, ResultSet rs) throws SQLException {
        entity.setSignID(rs.getInt(1));
    }

    @Override
    protected void SetInsertParameter(PreparedStatement pstmt, SignRecord entity) {
        try {
            pstmt.setInt(1, entity.getPackageID());
            pstmt.setInt(2, entity.getStationID());
            pstmt.setInt(3, entity.getReceiverID());
            pstmt.setString(4, entity.getSignType());
            if (entity.getSignTime() != null) {
                pstmt.setTimestamp(5, entity.getSignTime());
            } else {
                pstmt.setNull(5, Types.TIMESTAMP);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO " + TABLE + " (packageID, stationID, receiverID, signType, signTime) VALUES (?, ?, ?, ?, ?)";
    }
}
