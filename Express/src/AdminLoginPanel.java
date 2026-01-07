import javax.swing.*;
import java.awt.*;

public class AdminLoginPanel extends BasePanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private MainWindow mainWindow;
    private UserDAO userDAO;

    public AdminLoginPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.userDAO = new UserDAO();
        initUI();
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("管理员登录");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("账号："), gbc);
        usernameField = new JTextField(16);
        gbc.gridx = 1;
        add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("密码："), gbc);
        passwordField = new JPasswordField(16);
        gbc.gridx = 1;
        add(passwordField, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton loginBtn = new JButton("登录");
        JButton backBtn = new JButton("返回");
        loginBtn.addActionListener(e -> handleLogin());
        backBtn.addActionListener(e -> mainWindow.showRoleSelectionPanel());
        btnPanel.add(loginBtn);
        btnPanel.add(backBtn);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(btnPanel, gbc);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "账号和密码不能为空");
            return;
        }

        try {
            // local fallback admin credential
            if ("root".equals(username) && "114514".equals(password)) {
                User admin = new User();
                admin.setName("root");
                admin.setPassword("114514");
                admin.setUserType("admin");
                mainWindow.setCurrentUser(admin);
                mainWindow.showAdminPanel();
                return;
            }
            // find admin user by name + password + userType == admin
            java.util.List<User> users = userDAO.findAll();
            for (User u : users) {
                if (u.getName() != null && u.getName().equals(username)
                        && u.getPassword() != null && u.getPassword().equals(password)
                        && "admin".equalsIgnoreCase(u.getUserType())) {
                    mainWindow.setCurrentUser(u);
                    mainWindow.showAdminPanel();
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "管理员账号或密码错误");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "登录异常: " + ex.getMessage());
        }
    }
}
