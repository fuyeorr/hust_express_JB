import java.sql.*;
import java.io.InputStream;
import java.util.Properties;

public class DBUtil {
    private static String USERNAME;
    private static String PASSWORD;
    private static String URL;
    private static String DRIVER;

    static {
        try (InputStream config_in = DBUtil.class.getClassLoader().getResourceAsStream("resources/config.properties")) {
            Properties prop = new Properties();
            if (config_in == null) {
                System.out.println("DBUtil failed to load.");
                throw new RuntimeException("config.properties 文件缺失");
            }
            prop.load(config_in);
            USERNAME = prop.getProperty("db.username");
            PASSWORD = prop.getProperty("db.password");
            URL = prop.getProperty("db.url");
            DRIVER = prop.getProperty("db.driver");
            System.out.println("username: " + USERNAME);
            System.out.println("password: " + PASSWORD);
            // Class.forName(DRIVER);
        } catch (Exception e) {
            // Whatever
        } finally {
            // Whatever
        }
    }

    public static void /*Connection*/ getConnection() throws SQLException {
        System.out.println("username: " + USERNAME);
        System.out.println("password: " + PASSWORD);
        // return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
