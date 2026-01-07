import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class PackageDAO extends BaseDAO<PackageEntity, Integer> {
    private static final String TABLE = "package";

    @Override
    protected String getFindAllSQL() {
        return "SELECT " +
               "p.packageID, " +
               "p.orderID, " +
               "p.weight, " +
               "p.volume, " +
               "p.packageStatus, " +
               "p.currentStatus, " +
               "sender.name AS senderName, " +
               "sender.phone AS senderPhone, " +
               "receiver.name AS receiverName, " +
               "receiver.phone AS receiverPhone, " +
               "way.origin, " +
               "way.destination, " +
               "c.companyName " +
               "FROM " + TABLE + " p " +
               "LEFT JOIN orders o ON p.orderID = o.orderID " +
               "LEFT JOIN user sender ON o.senderID = sender.userID " +
               "LEFT JOIN user receiver ON o.receiverID = receiver.userID " +
               "LEFT JOIN waybill way ON p.packageID = way.packageID " +
               "LEFT JOIN company c ON way.companyID = c.companyID";
    }

    @Override
    protected String getFindByIDSQL() {
        return "SELECT " +
               "p.packageID, " +
               "p.orderID, " +
               "p.weight, " +
               "p.volume, " +
               "p.packageStatus, " +
               "p.currentStatus, " +
               "sender.name AS senderName, " +
               "sender.phone AS senderPhone, " +
               "receiver.name AS receiverName, " +
               "receiver.phone AS receiverPhone, " +
               "way.origin, " +
               "way.destination, " +
               "c.companyName " +
               "FROM " + TABLE + " p " +
               "LEFT JOIN orders o ON p.orderID = o.orderID " +
               "LEFT JOIN user sender ON o.senderID = sender.userID " +
               "LEFT JOIN user receiver ON o.receiverID = receiver.userID " +
               "LEFT JOIN waybill way ON p.packageID = way.packageID " +
               "LEFT JOIN company c ON way.companyID = c.companyID " +
               "WHERE p.packageID = ?";
    }

    @Override
    protected void SetParameter(PreparedStatement pstmt, int pos, Integer id) throws SQLException {
        pstmt.setInt(pos, id);
    }

    @Override
    protected PackageEntity mapResultSetToEntity(ResultSet rs) throws SQLException {
        PackageEntity pkg = new PackageEntity();
        pkg.setPackageID(rs.getInt("packageID"));
        pkg.setOrderID(rs.getInt("orderID"));
        pkg.setWeight(rs.getDouble("weight"));
        pkg.setVolume(rs.getDouble("volume"));
        pkg.setPackageStatus(rs.getString("packageStatus"));
        pkg.setCurrentStatus(rs.getString("currentStatus"));
        
        // 设置UI字段
        pkg.setSenderName(rs.getString("senderName"));
        pkg.setSenderPhone(rs.getString("senderPhone"));
        pkg.setReceiverName(rs.getString("receiverName"));
        pkg.setReceiverPhone(rs.getString("receiverPhone"));
        
        // 使用起点和终点作为类型和描述（如果需要）
        String origin = rs.getString("origin");
        String destination = rs.getString("destination");
        pkg.setType(origin + " → " + destination);
        pkg.setDescription("从 " + origin + " 到 " + destination);
        
        pkg.setCompanyName(rs.getString("companyName"));
        
        return pkg;
    }

    @Override
    protected void setUpdateParameters(PreparedStatement pstmt, PackageEntity entity) {
        try {
            if (entity.getOrderID() != null) {
                pstmt.setInt(1, entity.getOrderID());
            } else {
                pstmt.setNull(1, Types.INTEGER);
            }
            if (entity.getWeight() != null) {
                pstmt.setDouble(2, entity.getWeight());
            } else {
                pstmt.setNull(2, Types.DOUBLE);
            }
            if (entity.getVolume() != null) {
                pstmt.setDouble(3, entity.getVolume());
            } else {
                pstmt.setNull(3, Types.DOUBLE);
            }
            pstmt.setString(4, entity.getPackageStatus());
            pstmt.setString(5, entity.getCurrentStatus());
            pstmt.setInt(6, entity.getPackageID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getUpdateSQL() {
        return "UPDATE " + TABLE + " SET orderID = ?, weight = ?, volume = ?, packageStatus = ?, currentStatus = ? WHERE packageID = ?";
    }

    @Override
    protected String getDeleteSQL() {
        return "DELETE FROM " + TABLE + " WHERE packageID = ?";
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
    protected void setGeneratedId(PackageEntity entity, ResultSet rs) throws SQLException {
        entity.setPackageID(rs.getInt(1));
    }

    @Override
    protected void SetInsertParameter(PreparedStatement pstmt, PackageEntity entity) {
        try {
            if (entity.getOrderID() != null) {
                pstmt.setInt(1, entity.getOrderID());
            } else {
                pstmt.setNull(1, Types.INTEGER);
            }
            if (entity.getWeight() != null) {
                pstmt.setDouble(2, entity.getWeight());
            } else {
                pstmt.setNull(2, Types.DOUBLE);
            }
            if (entity.getVolume() != null) {
                pstmt.setDouble(3, entity.getVolume());
            } else {
                pstmt.setNull(3, Types.DOUBLE);
            }
            pstmt.setString(4, entity.getPackageStatus());
            pstmt.setString(5, entity.getCurrentStatus());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO " + TABLE + " (orderID, weight, volume, packageStatus, currentStatus) VALUES (?, ?, ?, ?, ?)";
    }
}
