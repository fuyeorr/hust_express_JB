import javax.swing.*;
import java.awt.*;

public class RoleSelectionPanel extends BasePanel {
    private MainWindow mainWindow;

    public RoleSelectionPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        initUI();
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        // 标题
        JLabel titleLabel = new JLabel("请选择身份");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(titleLabel, gbc);

        // 用户按钮
        JButton userButton = new JButton("普通用户");
        userButton.setFont(UIConstants.BUTTON_FONT);
        userButton.setPreferredSize(new Dimension(150, 50));
        userButton.addActionListener(e -> mainWindow.showUserPanel());
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(userButton, gbc);

        // 管理员按钮
        JButton adminButton = new JButton("管理员");
        adminButton.setFont(UIConstants.BUTTON_FONT);
        adminButton.setPreferredSize(new Dimension(150, 50));
        adminButton.addActionListener(e -> mainWindow.showAdminLoginPanel());
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(adminButton, gbc);

    }
}