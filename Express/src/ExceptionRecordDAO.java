import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public List<ExceptionRecord> findByPackageID(int packageID) {
        List<ExceptionRecord> exceptions = new ArrayList<>();
        String sql = "SELECT exceptionID, packageID, exceptionType, exceptionName, description FROM " + TABLE + " WHERE packageID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, packageID);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    exceptions.add(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exceptions;
    }

    public long countByStationID(int stationID) {
        String sql = "SELECT COUNT(DISTINCT e.exceptionID) as count FROM " + TABLE + " e " +
                     "INNER JOIN `Storage` s ON e.packageID = s.packageID " +
                     "WHERE s.stationID = ?";
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
