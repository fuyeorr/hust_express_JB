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
