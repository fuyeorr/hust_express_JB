import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StationDAO extends BaseDAO<Station, Integer> {
    private static final String TABLE = "`Station`";

    @Override
    protected String getFindAllSQL() {
        return "SELECT stationID, stationName, location, stationCode, stationPhone FROM " + TABLE;
    }

    @Override
    protected String getFindByIDSQL() {
        return "SELECT stationID, stationName, location, stationCode, stationPhone FROM " + TABLE + " WHERE stationID = ?";
    }

    @Override
    protected void SetParameter(PreparedStatement pstmt, int pos, Integer id) throws SQLException {
        pstmt.setInt(pos, id);
    }

    @Override
    protected Station mapResultSetToEntity(ResultSet rs) throws SQLException {
        Station station = new Station();
        station.setStationID(rs.getInt("stationID"));
        station.setStationName(rs.getString("stationName"));
        station.setLocation(rs.getString("location"));
        station.setStationCode(rs.getString("stationCode"));
        station.setStationPhone(rs.getString("stationPhone"));
        return station;
    }

    @Override
    protected void setUpdateParameters(PreparedStatement pstmt, Station entity) {
        try {
            pstmt.setString(1, entity.getStationName());
            pstmt.setString(2, entity.getLocation());
            pstmt.setString(3, entity.getStationCode());
            pstmt.setString(4, entity.getStationPhone());
            pstmt.setInt(5, entity.getStationID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getUpdateSQL() {
        return "UPDATE " + TABLE + " SET stationName = ?, location = ?, stationCode = ?, stationPhone = ? WHERE stationID = ?";
    }

    @Override
    protected String getDeleteSQL() {
        return "DELETE FROM " + TABLE + " WHERE stationID = ?";
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
    protected void setGeneratedId(Station entity, ResultSet rs) throws SQLException {
        entity.setStationID(rs.getInt(1));
    }

    @Override
    protected void SetInsertParameter(PreparedStatement pstmt, Station entity) {
        try {
            pstmt.setString(1, entity.getStationName());
            pstmt.setString(2, entity.getLocation());
            pstmt.setString(3, entity.getStationCode());
            pstmt.setString(4, entity.getStationPhone());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO " + TABLE + " (stationName, location, stationCode, stationPhone) VALUES (?, ?, ?, ?)";
    }
}

