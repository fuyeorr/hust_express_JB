import javax.swing.*;
import java.awt.*;

public class SystemSettingsPanel extends BasePanel {
    public SystemSettingsPanel() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 选项卡面板
        JTabbedPane tabbedPane = new JTabbedPane();

        // 数据库设置
        tabbedPane.addTab("数据库", createDatabasePanel());

        // 邮件设置
        tabbedPane.addTab("邮件通知", createEmailPanel());

        // 系统参数
        tabbedPane.addTab("系统参数", createSystemPanel());

        add(tabbedPane, BorderLayout.CENTER);

        // 保存按钮
        JButton saveButton = new JButton("保存设置");
        saveButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "设置已保存"));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createDatabasePanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBackground(UIConstants.BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel("数据库地址："));
        panel.add(new JTextField("localhost:3306"));
        panel.add(new JLabel("数据库名："));
        panel.add(new JTextField("express_db"));
        panel.add(new JLabel("用户名："));
        panel.add(new JTextField("root"));
        panel.add(new JLabel("密码："));
        panel.add(new JPasswordField());

        return panel;
    }

    private JPanel createEmailPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBackground(UIConstants.BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel("SMTP服务器："));
        panel.add(new JTextField("smtp.qq.com"));
        panel.add(new JLabel("端口："));
        panel.add(new JTextField("587"));
        panel.add(new JLabel("发送者邮箱："));
        panel.add(new JTextField(""));
        panel.add(new JLabel("发送者密码："));
        panel.add(new JPasswordField());

        return panel;
    }

    private JPanel createSystemPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBackground(UIConstants.BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel("最大入库数量："));
        panel.add(new JTextField("1000"));
        panel.add(new JLabel("签收超时时间(天)："));
        panel.add(new JTextField("7"));

        return panel;
    }
}