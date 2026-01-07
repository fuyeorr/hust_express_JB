import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionPanel extends BasePanel {
    private JTextField hostField;
    private JTextField portField;
    private JTextField dbField;
    private JTextField userField;
    private JPasswordField passwordField;

    public ConnectionPanel() {
        initUI();
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel title = new JLabel("数据库连接配置");
        title.setFont(UIConstants.TITLE_FONT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(title, gbc);

        gbc.gridwidth = 1;

        gbc.gridy++;
        add(new JLabel("主机："), gbc);
        hostField = new JTextField(20);
        gbc.gridx = 1;
        add(hostField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("端口："), gbc);
        portField = new JTextField(6);
        gbc.gridx = 1;
        add(portField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("数据库名："), gbc);
        dbField = new JTextField(15);
        gbc.gridx = 1;
        add(dbField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("用户名："), gbc);
        userField = new JTextField(15);
        gbc.gridx = 1;
        add(userField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("密码："), gbc);
        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        add(passwordField, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        JButton testBtn = new JButton("测试连接");
        JButton saveBtn = new JButton("保存配置");
        testBtn.addActionListener(this::handleTest);
        saveBtn.addActionListener(this::handleSave);
        btnPanel.add(testBtn);
        btnPanel.add(saveBtn);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        add(btnPanel, gbc);
    }

    private void handleTest(ActionEvent e) {
        String host = hostField.getText().trim();
        String port = portField.getText().trim();
        String db = dbField.getText().trim();
        String user = userField.getText().trim();
        String pass = new String(passwordField.getPassword());

        if (host.isEmpty() || port.isEmpty() || db.isEmpty() || user.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请填写主机、端口、数据库名和用户名");
            return;
        }

        String url = String.format("jdbc:mariadb://%s:%s/%s", host, port, db);
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            try (Connection c = DriverManager.getConnection(url, user, pass)) {
                JOptionPane.showMessageDialog(this, "连接成功");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "连接失败: " + ex.getMessage());
        }
    }

    private void handleSave(ActionEvent e) {
        String host = hostField.getText().trim();
        String port = portField.getText().trim();
        String db = dbField.getText().trim();
        String user = userField.getText().trim();
        String pass = new String(passwordField.getPassword());

        if (host.isEmpty() || port.isEmpty() || db.isEmpty() || user.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请填写主机、端口、数据库名和用户名");
            return;
        }

        String url = String.format("jdbc:mariadb://%s:%s/%s", host, port, db);

        Properties prop = new Properties();
        prop.setProperty("db.url", url);
        prop.setProperty("db.username", user);
        prop.setProperty("db.password", pass);

        // 保存到项目中的 resources/config.properties，供 DBUtil 读取
        try (OutputStream out = new FileOutputStream("src/resources/config.properties")) {
            prop.store(out, "database config saved by UI");
            JOptionPane.showMessageDialog(this, "配置已保存 (src/resources/config.properties)");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "保存失败: " + ex.getMessage());
        }
    }
}
