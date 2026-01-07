import javax.swing.*;
import java.awt.*;

public class SendPackagePanel extends BasePanel {
    private JTextField senderNameField;
    private JTextField senderPhoneField;
    private JTextField receiverNameField;
    private JTextField receiverPhoneField;
    private JTextField weightField;
    private JComboBox<String> packageTypeCombo;
    private JTextArea descriptionArea;
    private OrderService orderService;

    public SendPackagePanel() {
        this.orderService = new OrderService();
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 标题
        JLabel titleLabel = new JLabel("寄件");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        add(titleLabel, BorderLayout.NORTH);

        // 表单面板
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // 寄件人信息
        JLabel senderLabel = new JLabel("寄件人信息");
        senderLabel.setFont(UIConstants.LABEL_FONT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(senderLabel, gbc);

        JLabel senderNameLabel = new JLabel("姓名：");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(senderNameLabel, gbc);

        senderNameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(senderNameField, gbc);

        JLabel senderPhoneLabel = new JLabel("电话：");
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(senderPhoneLabel, gbc);

        senderPhoneField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(senderPhoneField, gbc);

        // 收件人信息
        JLabel receiverLabel = new JLabel("收件人信息");
        receiverLabel.setFont(UIConstants.LABEL_FONT);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(receiverLabel, gbc);

        JLabel receiverNameLabel = new JLabel("姓名：");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        formPanel.add(receiverNameLabel, gbc);

        receiverNameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 4;
        formPanel.add(receiverNameField, gbc);

        JLabel receiverPhoneLabel = new JLabel("电话：");
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(receiverPhoneLabel, gbc);

        receiverPhoneField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 5;
        formPanel.add(receiverPhoneField, gbc);

        // 包裹信息
        JLabel packageLabel = new JLabel("包裹信息");
        packageLabel.setFont(UIConstants.LABEL_FONT);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        formPanel.add(packageLabel, gbc);

        JLabel typeLabel = new JLabel("包裹类型：");
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        formPanel.add(typeLabel, gbc);

        packageTypeCombo = new JComboBox<>(new String[]{"文件", "衣物", "食品", "其他"});
        gbc.gridx = 1;
        gbc.gridy = 7;
        formPanel.add(packageTypeCombo, gbc);

        JLabel weightLabel = new JLabel("重量(kg)：");
        gbc.gridx = 0;
        gbc.gridy = 8;
        formPanel.add(weightLabel, gbc);

        weightField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 8;
        formPanel.add(weightField, gbc);

        JLabel descLabel = new JLabel("描述：");
        gbc.gridx = 0;
        gbc.gridy = 9;
        formPanel.add(descLabel, gbc);

        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        gbc.gridx = 1;
        gbc.gridy = 9;
        formPanel.add(new JScrollPane(descriptionArea), gbc);

        add(new JScrollPane(formPanel), BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(UIConstants.BACKGROUND_COLOR);

        JButton submitButton = new JButton("提交");
        submitButton.setFont(UIConstants.BUTTON_FONT);
        submitButton.setPreferredSize(new Dimension(UIConstants.BUTTON_WIDTH, UIConstants.BUTTON_HEIGHT));
        submitButton.addActionListener(e -> handleSubmit());

        JButton resetButton = new JButton("重置");
        resetButton.setFont(UIConstants.BUTTON_FONT);
        resetButton.setPreferredSize(new Dimension(UIConstants.BUTTON_WIDTH, UIConstants.BUTTON_HEIGHT));
        resetButton.addActionListener(e -> resetForm());

        buttonPanel.add(submitButton);
        buttonPanel.add(resetButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleSubmit() {
        String senderName = senderNameField.getText();
        String senderPhone = senderPhoneField.getText();
        String receiverName = receiverNameField.getText();
        String receiverPhone = receiverPhoneField.getText();
        String weight = weightField.getText();
        String packageType = (String) packageTypeCombo.getSelectedItem();
        String description = descriptionArea.getText();

        if (senderName.isEmpty() || receiverName.isEmpty() || weight.isEmpty() || senderPhone.isEmpty() || receiverPhone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请填写必要信息（含电话）");
            return;
        }

        try {
            double weightValue = Double.parseDouble(weight);

            PackageEntity packageEntity = new PackageEntity();
            packageEntity.setType(packageType);
            packageEntity.setWeight(weightValue);
            packageEntity.setCurrentStatus("待发出");
            packageEntity.setDescription(description);
            packageEntity.setSenderName(senderName);
            packageEntity.setSenderPhone(senderPhone);
            packageEntity.setReceiverName(receiverName);
            packageEntity.setReceiverPhone(receiverPhone);

            try {
                orderService.createOrderWithUsers(senderName, senderPhone, receiverName, receiverPhone, packageEntity);
                String message = String.format(
                    "寄件单提交成功！\n\n包裹ID: %d\n订单ID: %d\n\n已自动分配快递员\n请使用包裹ID进行查询和取件",
                    packageEntity.getPackageID(),
                    packageEntity.getOrderID()
                );
                JOptionPane.showMessageDialog(this, message, "提交成功", JOptionPane.INFORMATION_MESSAGE);
                resetForm();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "提交失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "重量必须是数字");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "提交失败: " + ex.getMessage());
        }
    }

    private void resetForm() {
        senderNameField.setText("");
        senderPhoneField.setText("");
        receiverNameField.setText("");
        receiverPhoneField.setText("");
        weightField.setText("");
        descriptionArea.setText("");
    }
}
