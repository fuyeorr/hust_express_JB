import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReceivePackagePanel extends BasePanel {
    private JTable packageTable;
    private DefaultTableModel tableModel;
    private QueryService queryService;
    private OrderService orderService;

    public ReceivePackagePanel() {
        this.queryService = new QueryService();
        this.orderService = new OrderService();
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 标题
        JLabel titleLabel = new JLabel("取件");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        add(titleLabel, BorderLayout.NORTH);

        // 搜索面板
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(UIConstants.BACKGROUND_COLOR);

        JLabel searchLabel = new JLabel("包裹ID：");
        JTextField searchField = new JTextField(15);
        JButton searchButton = new JButton("查询");
        searchButton.addActionListener(e -> handleSearch(searchField.getText()));
        JButton refreshButton = new JButton("刷新列表");
        refreshButton.addActionListener(e -> loadData());

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(refreshButton);

        // 表格
        String[] columnNames = {"包裹ID", "订单ID", "重量(kg)", "类型", "当前状态"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        packageTable = new JTable(tableModel);
        packageTable.setFont(UIConstants.LABEL_FONT);
        packageTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(packageTable), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(UIConstants.BACKGROUND_COLOR);

        JButton receiveButton = new JButton("确认取件");
        receiveButton.setPreferredSize(new Dimension(UIConstants.BUTTON_WIDTH, UIConstants.BUTTON_HEIGHT));
        receiveButton.setFont(UIConstants.BUTTON_FONT);
        receiveButton.addActionListener(e -> handleReceive());

        buttonPanel.add(receiveButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        try {
            // 查询所有已入库的包裹
            List<PackageEntity> packages = queryService.getAllPackages();

            for (PackageEntity pkg : packages) {
                if ("已入库".equals(pkg.getCurrentStatus())) {
                    tableModel.addRow(new Object[]{
                        pkg.getPackageID(),
                        pkg.getOrderID(),
                        pkg.getWeight(),
                        pkg.getPackageStatus(),
                        pkg.getCurrentStatus()
                    });
                }
            }

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "当前没有待取件的包裹", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "加载数据失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void handleSearch(String packageId) {
        if (packageId.isEmpty()) {
            loadData();
            return;
        }

        try {
            int id = Integer.parseInt(packageId);
            PackageEntity pkg = queryService.getPackageInfo(id);

            if (pkg != null && "已入库".equals(pkg.getCurrentStatus())) {
                tableModel.setRowCount(0);
                tableModel.addRow(new Object[]{
                    pkg.getPackageID(),
                    pkg.getOrderID(),
                    pkg.getWeight(),
                    pkg.getPackageStatus(),
                    pkg.getCurrentStatus()
                });
            } else if (pkg == null) {
                JOptionPane.showMessageDialog(this, "未找到该包裹", "提示", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "包裹状态为: " + pkg.getCurrentStatus() + "\n只能取件状态为'已入库'的包裹",
                    "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "包裹ID必须是数字", "错误", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "查询失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void handleReceive() {
        int selectedRow = packageTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择要取件的包裹", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int packageId = (Integer) tableModel.getValueAt(selectedRow, 0);

            // 获取包裹详细信息
            QueryService.PackageDetail detail = queryService.queryPackageDetail(packageId);
            if (detail == null || detail.getOrder() == null) {
                JOptionPane.showMessageDialog(this, "无法获取包裹详细信息", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 获取收件人ID
            int receiverID = detail.getOrder().getReceiverID();

            // 确认对话框
            int confirm = JOptionPane.showConfirmDialog(this,
                "确认取件？\n\n包裹ID: " + packageId + "\n订单ID: " + detail.getOrder().getOrderID(),
                "确认取件",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            // 调用签收方法
            orderService.sign(packageId, receiverID, "驿站自取");

            JOptionPane.showMessageDialog(this,
                "取件成功！\n\n包裹ID: " + packageId + "\n已完成签收",
                "成功",
                JOptionPane.INFORMATION_MESSAGE);
            loadData();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "取件失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
