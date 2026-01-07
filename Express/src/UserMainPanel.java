import javax.swing.*;
import java.awt.*;

public class UserMainPanel extends BasePanel {
    private MainWindow mainWindow;
    private JTabbedPane tabbedPane;

    public UserMainPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // 创建标签页
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("寄件", new SendPackagePanel());
        tabbedPane.addTab("取件", new ReceivePackagePanel());
        tabbedPane.addTab("追踪", new TrackPackagePanel());

        add(tabbedPane, BorderLayout.CENTER);

        // 退出按钮
        JButton logoutButton = new JButton("退出");
        logoutButton.addActionListener(e -> mainWindow.logout());
        add(logoutButton, BorderLayout.SOUTH);
    }
}