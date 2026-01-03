import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 * 数据访问方法
 * T: 实体类型 (Model)
 * PKT: 主键类型
*/
public abstract class BaseDAO<T, PKT> {
    protected abstract String getFindAllSQL();
    protected abstract String getFindByIDSQL();
    protected abstract void SetParameter(PreparedStatement pstmt, int pos, PKT id) throws SQLException;
    protected abstract T mapResultSetToEntity(ResultSet rs) throws SQLException;
    public List<T> findAll() {
        List<T> entities = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(getFindAllSQL());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                entities.add(mapResultSetToEntity(rs));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return entities;
    }
    public T findByID(PKT id) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(getFindByIDSQL());
            SetParameter(pstmt, 1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if(rs.next()) {
                    return mapResultSetToEntity(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean insert(T entity) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(getInsertSQL(), Statement.RETURN_GENERATED_KEYS);

            SetInsertParameter(pstmt, entity);
            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if(rs.next()) {
                        setGeneratedId(entity, rs);
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean update(T entity) {
        String sql = getUpdateSQL();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setUpdateParameters(pstmt, entity);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    protected abstract void setUpdateParameters(PreparedStatement pstmt, T entity);

    protected abstract String getUpdateSQL();

    public boolean delete(PKT id) {
        String sql = getDeleteSQL();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setIdParameter(pstmt, 1, id);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    protected abstract void setIdParameter(PreparedStatement pstmt, int i, PKT id);

    protected abstract String getDeleteSQL();

    protected abstract void setGeneratedId(T entity, ResultSet rs);

    protected abstract void SetInsertParameter(PreparedStatement pstmt, T entity);

    protected abstract String getInsertSQL();
}

/*
 * 其他 DAO 类需要和 Model 结合，extends BaseDAO<T, PKT>
 * 实现抽象方法, 其中 `get*SQL` 返回对应语义的 SQL 语句
 * 而 `Set*Parameter` 和 `Map*` 则是将 Model 对象和 SQL 语句互相转换
 * DAO 只能处理单表相关，即实体模型和SQL语句的转换，
 * 但是涉及到 JOIN 需要再写一个 QueryProvider，把扁平的关系表转换为嵌套的对象结构
 * 但需要 Model 先成型
*/