import javax.swing.*;
import java.awt.*;

public class AdminMainPanel extends BasePanel {
    private MainWindow mainWindow;
    private JTabbedPane tabbedPane;

    public AdminMainPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // 创建标签页
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("包裹管理", new PackageManagementPanel());
        tabbedPane.addTab("配送员管理", new DeliveryManManagementPanel());
        tabbedPane.addTab("员工管理", new StaffManagementPanel());
        tabbedPane.addTab("站点报表", new StationReportPanel());

        add(tabbedPane, BorderLayout.CENTER);

        // 退出按钮
        JButton logoutButton = new JButton("退出");
        logoutButton.addActionListener(e -> mainWindow.logout());
        add(logoutButton, BorderLayout.SOUTH);
    }
}