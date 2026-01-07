import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class WayBillDAO extends BaseDAO<WayBill, Integer> {
    private static final String TABLE = "`WayBill`";

    @Override
    protected String getFindAllSQL() {
        return "SELECT wayID, packageID, companyID, origin, destination, sendTime, wayStatus FROM " + TABLE;
    }

    @Override
    protected String getFindByIDSQL() {
        return "SELECT wayID, packageID, companyID, origin, destination, sendTime, wayStatus FROM " + TABLE + " WHERE wayID = ?";
    }

    @Override
    protected void SetParameter(PreparedStatement pstmt, int pos, Integer id) throws SQLException {
        pstmt.setInt(pos, id);
    }

    @Override
    protected WayBill mapResultSetToEntity(ResultSet rs) throws SQLException {
        WayBill wayBill = new WayBill();
        wayBill.setWayID(rs.getInt("wayID"));
        wayBill.setPackageID(rs.getInt("packageID"));
        wayBill.setCompanyID(rs.getInt("companyID"));
        wayBill.setOrigin(rs.getString("origin"));
        wayBill.setDestination(rs.getString("destination"));
        wayBill.setSendTime(rs.getTimestamp("sendTime"));
        wayBill.setWayStatus(rs.getString("wayStatus"));
        return wayBill;
    }

    @Override
    protected void setUpdateParameters(PreparedStatement pstmt, WayBill entity) {
        try {
            pstmt.setInt(1, entity.getPackageID());
            pstmt.setInt(2, entity.getCompanyID());
            pstmt.setString(3, entity.getOrigin());
            pstmt.setString(4, entity.getDestination());
            if (entity.getSendTime() != null) {
                pstmt.setTimestamp(5, entity.getSendTime());
            } else {
                pstmt.setNull(5, Types.TIMESTAMP);
            }
            pstmt.setString(6, entity.getWayStatus());
            pstmt.setInt(7, entity.getWayID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getUpdateSQL() {
        return "UPDATE " + TABLE + " SET packageID = ?, companyID = ?, origin = ?, destination = ?, sendTime = ?, wayStatus = ? WHERE wayID = ?";
    }

    @Override
    protected String getDeleteSQL() {
        return "DELETE FROM " + TABLE + " WHERE wayID = ?";
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
    protected void setGeneratedId(WayBill entity, ResultSet rs) throws SQLException {
        entity.setWayID(rs.getInt(1));
    }

    @Override
    protected void SetInsertParameter(PreparedStatement pstmt, WayBill entity) {
        try {
            pstmt.setInt(1, entity.getPackageID());
            pstmt.setInt(2, entity.getCompanyID());
            pstmt.setString(3, entity.getOrigin());
            pstmt.setString(4, entity.getDestination());
            if (entity.getSendTime() != null) {
                pstmt.setTimestamp(5, entity.getSendTime());
            } else {
                pstmt.setNull(5, Types.TIMESTAMP);
            }
            pstmt.setString(6, entity.getWayStatus());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO " + TABLE + " (packageID, companyID, origin, destination, sendTime, wayStatus) VALUES (?, ?, ?, ?, ?, ?)";
    }

    public WayBill findByPackageID(int packageID) {
        String sql = "SELECT wayID, packageID, companyID, origin, destination, sendTime, wayStatus FROM " + TABLE + " WHERE packageID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, packageID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
