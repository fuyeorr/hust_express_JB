import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        System.out.printf("Hello and welcome，shappy!\n");

        try {
            Connection con = DBUtil.getConnection();
            DBUtil.close(con, null, null);
        } catch (Exception ex) {
            System.err.println("DB connection check failed: " + ex.getMessage());
        }

        // Ensure default admin account exists (name=root, pwd=114514)
        try {
            UserDAO userDAO = new UserDAO();
            boolean found = false;
            for (User u : userDAO.findAll()) {
                if (u.getName() != null && u.getName().equals("root") && "admin".equalsIgnoreCase(u.getUserType())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                User admin = new User();
                admin.setName("root");
                admin.setPassword("114514");
                admin.setUserType("admin");
                boolean ok = userDAO.insert(admin);
                System.out.println("Default admin created: " + ok);
            }
        } catch (Exception ex) {
            System.err.println("Failed to ensure default admin: " + ex.getMessage());
        }

        // Launch GUI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainWindow();
            }
        });
    }
}