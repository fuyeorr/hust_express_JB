import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO extends BaseDAO<User, Integer> {

    private static final String TABLE = "`User`";

    @Override
    protected String getFindAllSQL() {
        return "SELECT userID, name, phone, address, sex, userType FROM " + TABLE;
    }

    @Override
    protected String getFindByIDSQL() {
        return "SELECT userID, name, phone, address, sex, userType FROM " + TABLE + " WHERE userID = ?";
    }

    @Override
    protected void SetParameter(PreparedStatement pstmt, int pos, Integer id) throws SQLException {
        pstmt.setInt(pos, id);
    }

    @Override
    protected User mapResultSetToEntity(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserID(rs.getInt("userID"));
        user.setName(rs.getString("name"));
        user.setPhone(rs.getString("phone"));
        user.setAddress(rs.getString("address"));
        user.setSex(rs.getString("sex"));
        user.setUserType(rs.getString("userType"));
        return user;
    }

    @Override
    protected void setUpdateParameters(PreparedStatement pstmt, User entity) {
        try {
            pstmt.setString(1, entity.getName());
            pstmt.setString(2, entity.getPhone());
            pstmt.setString(3, entity.getAddress());
            pstmt.setString(4, entity.getSex());
            pstmt.setString(5, entity.getUserType());
            pstmt.setInt(6, entity.getUserID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getUpdateSQL() {
        return "UPDATE " + TABLE + " SET name = ?, phone = ?, address = ?, sex = ?, userType = ? WHERE userID = ?";
    }

    @Override
    protected String getDeleteSQL() {
        return "DELETE FROM " + TABLE + " WHERE userID = ?";
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
    protected void setGeneratedId(User entity, ResultSet rs) throws SQLException {
        entity.setUserID(rs.getInt(1));
    }

    @Override
    protected void SetInsertParameter(PreparedStatement pstmt, User entity) {
        try {
            pstmt.setString(1, entity.getName());
            pstmt.setString(2, entity.getPhone());
            pstmt.setString(3, entity.getAddress());
            pstmt.setString(4, entity.getSex());
            pstmt.setString(5, entity.getUserType());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO " + TABLE + " (name, phone, address, sex, userType) VALUES (?, ?, ?, ?, ?)";
    }
}
