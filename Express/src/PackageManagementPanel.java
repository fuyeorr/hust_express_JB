import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PackageManagementPanel extends BasePanel {
    private JTable packageTable;
    private DefaultTableModel tableModel;
    private PackageDAO packageDAO;
    private List<PackageEntity> packageList;
    
    public PackageManagementPanel() {
        try {
            this.packageDAO = new PackageDAO();
            initUI();
            loadData();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "初始化失败: " + e.getMessage());
        }
    }
    
    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // 搜索和过滤面板
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        
        JLabel statusLabel = new JLabel("状态：");
        statusLabel.setFont(UIConstants.LABEL_FONT);
        
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"全部", "待发出", "运输中", "已入库", "已签收", "派送中", "已退回"});
        statusCombo.setFont(UIConstants.LABEL_FONT);
        
        JLabel senderLabel = new JLabel("发件人：");
        senderLabel.setFont(UIConstants.LABEL_FONT);
        JTextField senderField = new JTextField(12);
        senderField.setFont(UIConstants.LABEL_FONT);
        
        JButton filterButton = new JButton("筛选");
        filterButton.setFont(UIConstants.BUTTON_FONT);
        filterButton.addActionListener(e -> {
            String status = (String) statusCombo.getSelectedItem();
            String sender = senderField.getText().trim();
            handleFilter(status, sender);
        });
        
        JButton resetButton = new JButton("重置");
        resetButton.setFont(UIConstants.BUTTON_FONT);
        resetButton.addActionListener(e -> {
            statusCombo.setSelectedIndex(0);
            senderField.setText("");
            loadData();
        });
        
        searchPanel.add(statusLabel);
        searchPanel.add(statusCombo);
        searchPanel.add(senderLabel);
        searchPanel.add(senderField);
        searchPanel.add(filterButton);
        searchPanel.add(resetButton);
        add(searchPanel, BorderLayout.NORTH);
        
        // 表格
        String[] columnNames = {"包裹ID", "发件人", "电话", "收件人", "电话", "地址", "重量(kg)", "状态", "快递公司"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 6) return Double.class; // 重量列显示为数字
                return String.class;
            }
        };
        
        packageTable = new JTable(tableModel);
        packageTable.setFont(UIConstants.LABEL_FONT);
        packageTable.setRowHeight(30);
        packageTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        packageTable.getTableHeader().setFont(UIConstants.TITLE_FONT);
        
        JScrollPane scrollPane = new JScrollPane(packageTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(scrollPane, BorderLayout.CENTER);
        
        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        
        JButton viewButton = new JButton("查看详情");
        JButton editButton = new JButton("编辑");
        JButton deleteButton = new JButton("删除");
        JButton refreshButton = new JButton("刷新");
        
        viewButton.setFont(UIConstants.BUTTON_FONT);
        editButton.setFont(UIConstants.BUTTON_FONT);
        deleteButton.setFont(UIConstants.BUTTON_FONT);
        refreshButton.setFont(UIConstants.BUTTON_FONT);
        
        viewButton.addActionListener(e -> handleView());
        editButton.addActionListener(e -> handleEdit());
        deleteButton.addActionListener(e -> handleDelete());
        refreshButton.addActionListener(e -> loadData());
        
        buttonPanel.add(viewButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void loadData() {
        try {
            tableModel.setRowCount(0);
            packageList = packageDAO.findAll();
            
            for (PackageEntity pkg : packageList) {
                addRowToTable(pkg);
            }
            
            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "没有找到包裹数据", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "加载数据失败: " + ex.getMessage(), 
                "错误", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void addRowToTable(PackageEntity pkg) {
        Object[] row = {
            pkg.getPackageID(),
            pkg.getSenderName() != null ? pkg.getSenderName() : "未知",
            pkg.getSenderPhone() != null ? pkg.getSenderPhone() : "未知",
            pkg.getReceiverName() != null ? pkg.getReceiverName() : "未知",
            pkg.getReceiverPhone() != null ? pkg.getReceiverPhone() : "未知",
            pkg.getType() != null ? pkg.getType() : "普通",
            pkg.getWeight() != null ? pkg.getWeight() : 0.0,
            pkg.getCurrentStatus() != null ? pkg.getCurrentStatus() : "未知",
            pkg.getCompanyName() != null ? pkg.getCompanyName() : "未分配"
        };
        tableModel.addRow(row);
    }
    
    private void handleFilter(String status, String sender) {
        try {
            tableModel.setRowCount(0);
            
            for (PackageEntity pkg : packageList) {
                boolean matchStatus = "全部".equals(status) || 
                                     (pkg.getCurrentStatus() != null && 
                                      pkg.getCurrentStatus().equals(status));
                
                boolean matchSender = sender.isEmpty() || 
                                     (pkg.getSenderName() != null && 
                                      pkg.getSenderName().toLowerCase().contains(sender.toLowerCase()));
                
                if (matchStatus && matchSender) {
                    addRowToTable(pkg);
                }
            }
            
            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "没有找到匹配的包裹", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "筛选失败: " + ex.getMessage(), 
                "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleView() {
        int selectedRow = packageTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择一个包裹", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int packageId = (Integer) tableModel.getValueAt(selectedRow, 0);
            
            for (PackageEntity pkg : packageList) {
                if (pkg.getPackageID().equals(packageId)) {
                    showPackageDetails(pkg);
                    return;
                }
            }
            
            // 如果缓存中没有，从数据库获取
            PackageEntity pkg = packageDAO.findByID(packageId);
            if (pkg != null) {
                showPackageDetails(pkg);
            } else {
                JOptionPane.showMessageDialog(this, "包裹不存在或已被删除");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "获取详情失败: " + ex.getMessage(), 
                "错误", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void showPackageDetails(PackageEntity pkg) {
        String details = String.format(
            "<html>" +
            "<h3>包裹详情</h3>" +
            "<table border='0' cellspacing='5'>" +
            "<tr><td><b>包裹ID:</b></td><td>%d</td></tr>" +
            "<tr><td><b>订单ID:</b></td><td>%d</td></tr>" +
            "<tr><td><b>发件人:</b></td><td>%s</td></tr>" +
            "<tr><td><b>发件人电话:</b></td><td>%s</td></tr>" +
            "<tr><td><b>收件人:</b></td><td>%s</td></tr>" +
            "<tr><td><b>收件人电话:</b></td><td>%s</td></tr>" +
            "<tr><td><b>地址:</b></td><td>%s</td></tr>" +
            "<tr><td><b>重量:</b></td><td>%.2f kg</td></tr>" +
            "<tr><td><b>体积:</b></td><td>%.2f m³</td></tr>" +
            "<tr><td><b>包裹状态:</b></td><td>%s</td></tr>" +
            "<tr><td><b>当前状态:</b></td><td>%s</td></tr>" +
            "<tr><td><b>快递公司:</b></td><td>%s</td></tr>" +
            "<tr><td><b>描述:</b></td><td>%s</td></tr>" +
            "</table>" +
            "</html>",
            pkg.getPackageID(),
            pkg.getOrderID() != null ? pkg.getOrderID() : 0,
            pkg.getSenderName() != null ? pkg.getSenderName() : "未知",
            pkg.getSenderPhone() != null ? pkg.getSenderPhone() : "未知",
            pkg.getReceiverName() != null ? pkg.getReceiverName() : "未知",
            pkg.getReceiverPhone() != null ? pkg.getReceiverPhone() : "未知",
            pkg.getType() != null ? pkg.getType() : "普通",
            pkg.getWeight() != null ? pkg.getWeight() : 0.0,
            pkg.getVolume() != null ? pkg.getVolume() : 0.0,
            pkg.getPackageStatus() != null ? pkg.getPackageStatus() : "未知",
            pkg.getCurrentStatus() != null ? pkg.getCurrentStatus() : "未知",
            pkg.getCompanyName() != null ? pkg.getCompanyName() : "未分配",
            pkg.getDescription() != null ? pkg.getDescription() : "无"
        );
        
        JOptionPane.showMessageDialog(this, details, "包裹详情", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void handleEdit() {
        int selectedRow = packageTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择一个包裹", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int packageId = (Integer) tableModel.getValueAt(selectedRow, 0);
            PackageEntity pkg = null;
            
            // 从缓存中查找
            for (PackageEntity packageEntity : packageList) {
                if (packageEntity.getPackageID().equals(packageId)) {
                    pkg = packageEntity;
                    break;
                }
            }
            
            if (pkg == null) {
                pkg = packageDAO.findByID(packageId);
            }
            
            if (pkg == null) {
                JOptionPane.showMessageDialog(this, "包裹不存在或已被删除");
                return;
            }
            
            // 编辑对话框
            String[] options = {"待发出", "运输中", "已入库", "已签收", "派送中", "已退回"};
            
            String newStatus = (String) JOptionPane.showInputDialog(
                this, 
                "选择新的包裹状态：", 
                "编辑包裹状态",
                JOptionPane.QUESTION_MESSAGE, 
                null, 
                options, 
                pkg.getCurrentStatus()
            );
            
            if (newStatus != null && !newStatus.equals(pkg.getCurrentStatus())) {
                pkg.setCurrentStatus(newStatus);
                boolean success = packageDAO.update(pkg);
                
                if (success) {
                    // 更新表格
                    tableModel.setValueAt(newStatus, selectedRow, 7);
                    
                    // 更新缓存
                    for (int i = 0; i < packageList.size(); i++) {
                        if (packageList.get(i).getPackageID().equals(packageId)) {
                            packageList.get(i).setCurrentStatus(newStatus);
                            break;
                        }
                    }
                    
                    JOptionPane.showMessageDialog(this, "包裹状态更新成功！");
                } else {
                    JOptionPane.showMessageDialog(this, "更新失败", 
                        "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "编辑失败: " + ex.getMessage(), 
                "错误", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void handleDelete() {
        int selectedRow = packageTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择一个包裹", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "确定要删除这个包裹吗？此操作不可恢复！", 
            "确认删除",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int packageId = (Integer) tableModel.getValueAt(selectedRow, 0);
                
                boolean success = packageDAO.delete(packageId);
                if (success) {
                    tableModel.removeRow(selectedRow);
                    
                    // 从缓存中删除
                    for (int i = 0; i < packageList.size(); i++) {
                        if (packageList.get(i).getPackageID().equals(packageId)) {
                            packageList.remove(i);
                            break;
                        }
                    }
                    
                    JOptionPane.showMessageDialog(this, "包裹删除成功！");
                } else {
                    JOptionPane.showMessageDialog(this, "删除失败", 
                        "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "删除失败: " + ex.getMessage(), 
                    "错误", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}
