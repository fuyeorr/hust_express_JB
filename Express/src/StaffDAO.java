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
