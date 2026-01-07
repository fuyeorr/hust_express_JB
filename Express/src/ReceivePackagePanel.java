import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.sql.Timestamp;

public class ReceivePackagePanel extends BasePanel {
    private JTable packageTable;
    private DefaultTableModel tableModel;
    private PackageDAO packageDAO;
    private SignRecordDAO signRecordDAO;

    public ReceivePackagePanel() {
        this.packageDAO = new PackageDAO();
        this.signRecordDAO = new SignRecordDAO();
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 搜索面板
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(UIConstants.BACKGROUND_COLOR);

        JLabel searchLabel = new JLabel("包裹ID：");
        JTextField searchField = new JTextField(15);
        JButton searchButton = new JButton("查询");
        searchButton.addActionListener(e -> handleSearch(searchField.getText()));

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        // 表格
        String[] columnNames = {"包裹ID", "发送人", "发送人电话", "类型", "重量", "状态"};
        tableModel = new DefaultTableModel(columnNames, 0);
        packageTable = new JTable(tableModel);
        packageTable.setFont(UIConstants.LABEL_FONT);
        add(new JScrollPane(packageTable), BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(UIConstants.BACKGROUND_COLOR);

        JButton receiveButton = new JButton("确认取件");
        receiveButton.addActionListener(e -> handleReceive());

        buttonPanel.add(receiveButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        try {
            // 查询所有已入库的包裹
            List<PackageEntity> packages = packageDAO.findAll();
            
            for (PackageEntity pkg : packages) {
                if ("已入库".equals(pkg.getCurrentStatus())) {
                    tableModel.addRow(new Object[]{
                        pkg.getPackageID(),
                        pkg.getSenderName(),
                        pkg.getSenderPhone(),
                        pkg.getType(),
                        pkg.getWeight(),
                        pkg.getCurrentStatus()
                    });
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "加载数据失败: " + ex.getMessage());
        }
    }

    private void handleSearch(String packageId) {
        if (packageId.isEmpty()) {
            loadData();
            return;
        }

        try {
            int id = Integer.parseInt(packageId);
            PackageEntity pkg = packageDAO.findByID(id);
            
            if (pkg != null && "已入库".equals(pkg.getCurrentStatus())) {
                tableModel.setRowCount(0);
                tableModel.addRow(new Object[]{
                    pkg.getPackageID(),
                    pkg.getSenderName(),
                    pkg.getSenderPhone(),
                    pkg.getType(),
                    pkg.getWeight(),
                    pkg.getCurrentStatus()
                });
            } else {
                JOptionPane.showMessageDialog(this, "未找到待取件的包裹");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "包裹ID必须是数字");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "查询失败: " + ex.getMessage());
        }
    }

    private void handleReceive() {
        int selectedRow = packageTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择要取件的包裹");
            return;
        }

        try {
            int packageId = (Integer) tableModel.getValueAt(selectedRow, 0);
            
            // 生成签收记录
            SignRecord signRecord = new SignRecord();
            signRecord.setPackageID(packageId);
            signRecord.setSignTime(new Timestamp(System.currentTimeMillis()));
            signRecord.setSignType("驿站自取");
            
            // 保存签收记录
            if (signRecordDAO.insert(signRecord)) {
                // 更新包裹状态
                PackageEntity pkg = packageDAO.findByID(packageId);
                pkg.setCurrentStatus("已签收");
                packageDAO.update(pkg);
                
                JOptionPane.showMessageDialog(this, "取件成功");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "取件失败，请重试");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "取件异常: " + ex.getMessage());
        }
    }
}