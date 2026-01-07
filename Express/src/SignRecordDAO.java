import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

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

    public List<SignRecord> findByPackageID(int packageID) {
        List<SignRecord> signs = new ArrayList<>();
        String sql = "SELECT signID, packageID, stationID, receiverID, signType, signTime FROM " + TABLE + " WHERE packageID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, packageID);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    signs.add(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return signs;
    }

    public long countByStationID(int stationID) {
        String sql = "SELECT COUNT(*) as count FROM " + TABLE + " WHERE stationID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, stationID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("count");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
