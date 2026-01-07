import javax.swing.*;
import java.awt.*;
import java.util.prefs.Preferences;
 

public class MainWindow extends JFrame {
    private JPanel currentPanel;
    private Preferences prefs;
    private User currentUser;
    
    public MainWindow() {
        setTitle("快递驿站管理系统");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        
        prefs = Preferences.userNodeForPackage(MainWindow.class);
        
        int x = prefs.getInt("window.x", 0);
        int y = prefs.getInt("window.y", 0);
        setLocation(x, y);
        setResizable(true);
        
        showRoleSelectionPanel();
        setVisible(true);
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                prefs.putInt("window.x", getX());
                prefs.putInt("window.y", getY());
            }
        });
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    
    public User getCurrentUser() {
        return this.currentUser;
    }
    
    public void showLoginPanel() {
        getContentPane().removeAll();
        currentPanel = new LoginPanel(this);
        getContentPane().add(currentPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    
    public void showRoleSelectionPanel() {
        getContentPane().removeAll();
        currentPanel = new RoleSelectionPanel(this);
        getContentPane().add(currentPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    
    public void showUserPanel() {
        getContentPane().removeAll();
        currentPanel = new UserMainPanel(this);
        getContentPane().add(currentPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    
    public void showAdminPanel() {
        getContentPane().removeAll();
        currentPanel = new AdminMainPanel(this);
        getContentPane().add(currentPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void showAdminLoginPanel() {
        getContentPane().removeAll();
        currentPanel = new AdminLoginPanel(this);
        getContentPane().add(currentPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    
    public void logout() {
        currentUser = null;
        showRoleSelectionPanel();
    }
}
