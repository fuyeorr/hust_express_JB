import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PackageManagementPanel extends BasePanel {
    private JTable packageTable;
    private DefaultTableModel tableModel;
    private PackageDAO packageDAO;

    public PackageManagementPanel() {
        this.packageDAO = new PackageDAO();
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 搜索和过滤面板
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(UIConstants.BACKGROUND_COLOR);

        JLabel statusLabel = new JLabel("状态：");
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"全部", "待发出", "运输中", "已入库", "已签收"});
        JButton filterButton = new JButton("筛选");
        filterButton.addActionListener(e -> handleFilter((String) statusCombo.getSelectedItem()));

        searchPanel.add(statusLabel);
        searchPanel.add(statusCombo);
        searchPanel.add(filterButton);
        add(searchPanel, BorderLayout.NORTH);

        // 表格
        String[] columnNames = {"包裹ID", "发送人", "收件人", "类型", "重量", "当前状态", "快递公司"};
        tableModel = new DefaultTableModel(columnNames, 0);

        packageTable = new JTable(tableModel);
        packageTable.setFont(UIConstants.LABEL_FONT);
        packageTable.setRowHeight(30);
        add(new JScrollPane(packageTable), BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(UIConstants.BACKGROUND_COLOR);

        JButton viewButton = new JButton("查看详情");
        JButton editButton = new JButton("编辑");
        JButton deleteButton = new JButton("删除");

        viewButton.addActionListener(e -> handleView());
        editButton.addActionListener(e -> handleEdit());
        deleteButton.addActionListener(e -> handleDelete());

        buttonPanel.add(viewButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        try {
            List<PackageEntity> packages = packageDAO.findAll();
            for (PackageEntity pkg : packages) {
                tableModel.addRow(new Object[]{
                    pkg.getPackageID(),
                    pkg.getSenderName(),
                    pkg.getReceiverName(),
                    pkg.getType(),
                    pkg.getWeight(),
                    pkg.getCurrentStatus(),
                    pkg.getCompanyName()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "加载数据失败: " + ex.getMessage());
        }
    }

    private void handleFilter(String status) {
        tableModel.setRowCount(0);
        try {
            List<PackageEntity> packages = packageDAO.findAll();
            
            for (PackageEntity pkg : packages) {
                if ("全部".equals(status) || status.equals(pkg.getCurrentStatus())) {
                    tableModel.addRow(new Object[]{
                        pkg.getPackageID(),
                        pkg.getSenderName(),
                        pkg.getReceiverName(),
                        pkg.getType(),
                        pkg.getWeight(),
                        pkg.getCurrentStatus(),
                        pkg.getCompanyName()
                    });
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "筛选失败: " + ex.getMessage());
        }
    }

    private void handleView() {
        int selectedRow = packageTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择包裹");
            return;
        }

        int packageId = (Integer) tableModel.getValueAt(selectedRow, 0);
        try {
            PackageEntity pkg = packageDAO.findByID(packageId);
            String details = String.format(
                "包裹ID: %d\n发送人: %s\n收件人: %s\n类型: %s\n重量: %.2f kg\n状态: %s\n描述: %s",
                pkg.getPackageID(), pkg.getSenderName(), pkg.getReceiverName(),
                pkg.getType(), pkg.getWeight(), pkg.getCurrentStatus(), pkg.getDescription()
            );
            JOptionPane.showMessageDialog(this, details, "包裹详情", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "获取详情失败: " + ex.getMessage());
        }
    }

    private void handleEdit() {
        int selectedRow = packageTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择包裹");
            return;
        }

        int packageId = (Integer) tableModel.getValueAt(selectedRow, 0);
        try {
            PackageEntity pkg = packageDAO.findByID(packageId);
            
            String newStatus = (String) JOptionPane.showInputDialog(
                this, "选择新状态：", "编辑包裹状态",
                JOptionPane.QUESTION_MESSAGE, null,
                new String[]{"待发出", "运输中", "已入库", "已签收"}, pkg.getCurrentStatus()
            );
            
            if (newStatus != null) {
                pkg.setCurrentStatus(newStatus);
                if (packageDAO.update(pkg)) {
                    JOptionPane.showMessageDialog(this, "更新成功");
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "更新失败");
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "编辑失败: " + ex.getMessage());
        }
    }

    private void handleDelete() {
        int selectedRow = packageTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择包裹");
            return;
        }

        int packageId = (Integer) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "确定删除此包裹吗？");
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (packageDAO.delete(packageId)) {
                    JOptionPane.showMessageDialog(this, "删除成功");
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "删除失败");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "删除失败: " + ex.getMessage());
            }
        }
    }
}