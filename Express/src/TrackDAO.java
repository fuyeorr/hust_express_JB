import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrackDAO extends BaseDAO<Track, Integer> {
    private static final String TABLE = "`Track`";

    @Override
    protected String getFindAllSQL() {
        return "SELECT trackID, wayID, deliveryID, trackTime, currentLocation, trackInfo FROM " + TABLE;
    }

    @Override
    protected String getFindByIDSQL() {
        return "SELECT trackID, wayID, deliveryID, trackTime, currentLocation, trackInfo FROM " + TABLE + " WHERE trackID = ?";
    }

    @Override
    protected void SetParameter(PreparedStatement pstmt, int pos, Integer id) throws SQLException {
        pstmt.setInt(pos, id);
    }

    @Override
    protected Track mapResultSetToEntity(ResultSet rs) throws SQLException {
        Track track = new Track();
        track.setTrackID(rs.getInt("trackID"));
        track.setWayID(rs.getInt("wayID"));
        track.setDeliveryID(rs.getInt("deliveryID"));
        track.setTrackTime(rs.getTimestamp("trackTime"));
        track.setCurrentLocation(rs.getString("currentLocation"));
        track.setTrackInfo(rs.getString("trackInfo"));
        return track;
    }

    @Override
    protected void setUpdateParameters(PreparedStatement pstmt, Track entity) {
        try {
            pstmt.setInt(1, entity.getWayID());
            pstmt.setInt(2, entity.getDeliveryID());
            if (entity.getTrackTime() != null) {
                pstmt.setTimestamp(3, entity.getTrackTime());
            } else {
                pstmt.setNull(3, Types.TIMESTAMP);
            }
            pstmt.setString(4, entity.getCurrentLocation());
            pstmt.setString(5, entity.getTrackInfo());
            pstmt.setInt(6, entity.getTrackID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getUpdateSQL() {
        return "UPDATE " + TABLE + " SET wayID = ?, deliveryID = ?, trackTime = ?, currentLocation = ?, trackInfo = ? WHERE trackID = ?";
    }

    @Override
    protected String getDeleteSQL() {
        return "DELETE FROM " + TABLE + " WHERE trackID = ?";
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
    protected void setGeneratedId(Track entity, ResultSet rs) throws SQLException {
        entity.setTrackID(rs.getInt(1));
    }

    @Override
    protected void SetInsertParameter(PreparedStatement pstmt, Track entity) {
        try {
            pstmt.setInt(1, entity.getWayID());
            pstmt.setInt(2, entity.getDeliveryID());
            if (entity.getTrackTime() != null) {
                pstmt.setTimestamp(3, entity.getTrackTime());
            } else {
                pstmt.setNull(3, Types.TIMESTAMP);
            }
            pstmt.setString(4, entity.getCurrentLocation());
            pstmt.setString(5, entity.getTrackInfo());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO " + TABLE + " (wayID, deliveryID, trackTime, currentLocation, trackInfo) VALUES (?, ?, ?, ?, ?)";
    }

    public List<Track> findByWayID(int wayID) {
        List<Track> tracks = new ArrayList<>();
        String sql = "SELECT trackID, wayID, deliveryID, trackTime, currentLocation, trackInfo FROM " + TABLE + " WHERE wayID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, wayID);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    tracks.add(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tracks;
    }

    public Map<Integer, Long> countByDeliveryID() {
        Map<Integer, Long> result = new HashMap<>();
        String sql = "SELECT deliveryID, COUNT(*) as count FROM " + TABLE + " GROUP BY deliveryID";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                result.put(rs.getInt("deliveryID"), rs.getLong("count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}

