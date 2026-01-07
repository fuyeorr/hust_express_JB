import javax.swing.*;
import java.awt.*;

public class LoginPanel extends BasePanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private MainWindow mainWindow;
    private UserDAO userDAO;

    public LoginPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.userDAO = new UserDAO();
        initUI();
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // 标题
        JLabel titleLabel = new JLabel("快递驿站管理系统");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // 用户名
        JLabel usernameLabel = new JLabel("用户名：");
        usernameLabel.setFont(UIConstants.LABEL_FONT);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(usernameLabel, gbc);

        usernameField = new JTextField(15);
        usernameField.setFont(UIConstants.LABEL_FONT);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(usernameField, gbc);

        // 密码
        JLabel passwordLabel = new JLabel("密码：");
        passwordLabel.setFont(UIConstants.LABEL_FONT);
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(passwordLabel, gbc);

        passwordField = new JPasswordField(15);
        passwordField.setFont(UIConstants.LABEL_FONT);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(passwordField, gbc);

        // 登录按钮
        JButton loginButton = new JButton("登录");
        loginButton.setFont(UIConstants.BUTTON_FONT);
        loginButton.setPreferredSize(new Dimension(UIConstants.BUTTON_WIDTH, UIConstants.BUTTON_HEIGHT));
        loginButton.addActionListener(e -> handleLogin());
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(loginButton, gbc);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "用户名和密码不能为空");
            return;
        }

        try {
            // 从数据库查询用户
            User user = userDAO.findByID(Integer.parseInt(username));

            if (user != null && user.getPassword().equals(password)) {
                // 登录成功，保存用户信息到 MainWindow
                mainWindow.setCurrentUser(user);
                mainWindow.showRoleSelectionPanel();
            } else {
                JOptionPane.showMessageDialog(this, "用户名或密码错误");
                passwordField.setText("");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "用户名必须是数字");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "登录异常: " + ex.getMessage());
        }
    }
}