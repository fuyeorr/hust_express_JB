import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CompanyDAO extends BaseDAO<Company, Integer> {
    private static final String TABLE = "`Company`";

    @Override
    protected String getFindAllSQL() {
        return "SELECT companyID, companyName, companyCode, companyPhone FROM " + TABLE;
    }

    @Override
    protected String getFindByIDSQL() {
        return "SELECT companyID, companyName, companyCode, companyPhone FROM " + TABLE + " WHERE companyID = ?";
    }

    @Override
    protected void SetParameter(PreparedStatement pstmt, int pos, Integer id) throws SQLException {
        pstmt.setInt(pos, id);
    }

    @Override
    protected Company mapResultSetToEntity(ResultSet rs) throws SQLException {
        Company company = new Company();
        company.setCompanyID(rs.getInt("companyID"));
        company.setCompanyName(rs.getString("companyName"));
        company.setCompanyCode(rs.getString("companyCode"));
        company.setCompanyPhone(rs.getString("companyPhone"));
        return company;
    }

    @Override
    protected void setUpdateParameters(PreparedStatement pstmt, Company entity) {
        try {
            pstmt.setString(1, entity.getCompanyName());
            pstmt.setString(2, entity.getCompanyCode());
            pstmt.setString(3, entity.getCompanyPhone());
            pstmt.setInt(4, entity.getCompanyID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getUpdateSQL() {
        return "UPDATE " + TABLE + " SET companyName = ?, companyCode = ?, companyPhone = ? WHERE companyID = ?";
    }

    @Override
    protected String getDeleteSQL() {
        return "DELETE FROM " + TABLE + " WHERE companyID = ?";
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
    protected void setGeneratedId(Company entity, ResultSet rs) throws SQLException {
        entity.setCompanyID(rs.getInt(1));
    }

    @Override
    protected void SetInsertParameter(PreparedStatement pstmt, Company entity) {
        try {
            pstmt.setString(1, entity.getCompanyName());
            pstmt.setString(2, entity.getCompanyCode());
            pstmt.setString(3, entity.getCompanyPhone());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO " + TABLE + " (companyName, companyCode, companyPhone) VALUES (?, ?, ?)";
    }
}
