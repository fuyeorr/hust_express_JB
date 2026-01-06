import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class OrderDAO extends BaseDAO<OrderRecord, Integer> {

    private static final String TABLE = "`Order`";

    @Override
    protected String getFindAllSQL() {
        return "SELECT orderID, senderID, receiverID, startTime, orderStatus, cost FROM " + TABLE;
    }

    @Override
    protected String getFindByIDSQL() {
        return "SELECT orderID, senderID, receiverID, startTime, orderStatus, cost FROM " + TABLE + " WHERE orderID = ?";
    }

    @Override
    protected void SetParameter(PreparedStatement pstmt, int pos, Integer id) throws SQLException {
        pstmt.setInt(pos, id);
    }

    @Override
    protected OrderRecord mapResultSetToEntity(ResultSet rs) throws SQLException {
        OrderRecord order = new OrderRecord();
        order.setOrderID(rs.getInt("orderID"));
        order.setSenderID(rs.getInt("senderID"));
        order.setReceiverID(rs.getInt("receiverID"));
        order.setStartTime(rs.getTimestamp("startTime"));
        order.setOrderStatus(rs.getString("orderStatus"));
        order.setCost(rs.getDouble("cost"));
        return order;
    }

    @Override
    protected void setUpdateParameters(PreparedStatement pstmt, OrderRecord entity) {
        try {
            pstmt.setInt(1, entity.getSenderID());
            pstmt.setInt(2, entity.getReceiverID());
            if (entity.getStartTime() != null) {
                pstmt.setTimestamp(3, entity.getStartTime());
            } else {
                pstmt.setNull(3, Types.TIMESTAMP);
            }
            pstmt.setString(4, entity.getOrderStatus());
            if (entity.getCost() != null) {
                pstmt.setDouble(5, entity.getCost());
            } else {
                pstmt.setNull(5, Types.DOUBLE);
            }
            pstmt.setInt(6, entity.getOrderID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getUpdateSQL() {
        return "UPDATE " + TABLE + " SET senderID = ?, receiverID = ?, startTime = ?, orderStatus = ?, cost = ? WHERE orderID = ?";
    }

    @Override
    protected String getDeleteSQL() {
        return "DELETE FROM " + TABLE + " WHERE orderID = ?";
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
    protected void setGeneratedId(OrderRecord entity, ResultSet rs) throws SQLException {
        entity.setOrderID(rs.getInt(1));
    }

    @Override
    protected void SetInsertParameter(PreparedStatement pstmt, OrderRecord entity) {
        try {
            pstmt.setInt(1, entity.getSenderID());
            pstmt.setInt(2, entity.getReceiverID());
            if (entity.getStartTime() != null) {
                pstmt.setTimestamp(3, entity.getStartTime());
            } else {
                pstmt.setNull(3, Types.TIMESTAMP);
            }
            pstmt.setString(4, entity.getOrderStatus());
            if (entity.getCost() != null) {
                pstmt.setDouble(5, entity.getCost());
            } else {
                pstmt.setNull(5, Types.DOUBLE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO " + TABLE + " (senderID, receiverID, startTime, orderStatus, cost) VALUES (?, ?, ?, ?, ?)";
    }
}
