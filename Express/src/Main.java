import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        System.out.printf("Hello and welcome，shappy!\n");

        Connection con = DBUtil.getConnection();
        DBUtil.close(con, null, null);
    }
}