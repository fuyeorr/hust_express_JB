import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeliveryManDAO extends BaseDAO<DeliveryMan, Integer> {
    private static final String TABLE = "`DeliveryMan`";

    @Override
    protected String getFindAllSQL() {
        return "SELECT deliveryID, companyID, delName, delPhone, delSex, delType FROM " + TABLE;
    }

    @Override
    protected String getFindByIDSQL() {
        return "SELECT deliveryID, companyID, delName, delPhone, delSex, delType FROM " + TABLE + " WHERE deliveryID = ?";
    }

    @Override
    protected void SetParameter(PreparedStatement pstmt, int pos, Integer id) throws SQLException {
        pstmt.setInt(pos, id);
    }

    @Override
    protected DeliveryMan mapResultSetToEntity(ResultSet rs) throws SQLException {
        DeliveryMan deliveryMan = new DeliveryMan();
        deliveryMan.setDeliveryID(rs.getInt("deliveryID"));
        deliveryMan.setCompanyID(rs.getInt("companyID"));
        deliveryMan.setDelName(rs.getString("delName"));
        deliveryMan.setDelPhone(rs.getString("delPhone"));
        deliveryMan.setDelSex(rs.getString("delSex"));
        deliveryMan.setDelType(rs.getString("delType"));
        return deliveryMan;
    }

    @Override
    protected void setUpdateParameters(PreparedStatement pstmt, DeliveryMan entity) {
        try {
            pstmt.setInt(1, entity.getCompanyID());
            pstmt.setString(2, entity.getDelName());
            pstmt.setString(3, entity.getDelPhone());
            pstmt.setString(4, entity.getDelSex());
            pstmt.setString(5, entity.getDelType());
            pstmt.setInt(6, entity.getDeliveryID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getUpdateSQL() {
        return "UPDATE " + TABLE + " SET companyID = ?, delName = ?, delPhone = ?, delSex = ?, delType = ? WHERE deliveryID = ?";
    }

    @Override
    protected String getDeleteSQL() {
        return "DELETE FROM " + TABLE + " WHERE deliveryID = ?";
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
    protected void setGeneratedId(DeliveryMan entity, ResultSet rs) throws SQLException {
        entity.setDeliveryID(rs.getInt(1));
    }

    @Override
    protected void SetInsertParameter(PreparedStatement pstmt, DeliveryMan entity) {
        try {
            pstmt.setInt(1, entity.getCompanyID());
            pstmt.setString(2, entity.getDelName());
            pstmt.setString(3, entity.getDelPhone());
            pstmt.setString(4, entity.getDelSex());
            pstmt.setString(5, entity.getDelType());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO " + TABLE + " (companyID, delName, delPhone, delSex, delType) VALUES (?, ?, ?, ?, ?)";
    }
}
