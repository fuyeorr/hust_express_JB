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
        // Storage has composite primary key; nothing to set
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
