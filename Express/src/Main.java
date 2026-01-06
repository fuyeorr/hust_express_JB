import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.SwingUtilities;
import src.ui.MainWindow;

public class Main {
    public static void main(String[] args) throws SQLException {
        System.out.printf("Hello and welcome，shappy!\n");

        Connection con = DBUtil.getConnection();
        DBUtil.close(con, null, null);

        // Launch GUI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainWindow();
            }
        });
    }
}