import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StaffManagementPanel extends BasePanel {
    private JTable staffTable;
    private DefaultTableModel tableModel;
    private StaffDAO staffDAO;
    private StationDAO stationDAO;
    private List<Staff> staffList;
    
    public StaffManagementPanel() {
        try {
            staffDAO = new StaffDAO();
            stationDAO = new StationDAO();
            initUI();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "初始化失败: " + e.getMessage());
        }
    }
    
    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // 表格
        String[] columnNames = {"员工ID", "姓名", "性别", "电话", "所属驿站", "角色"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        staffTable = new JTable(tableModel);
        staffTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        staffTable.setRowHeight(30);
        staffTable.setFont(UIConstants.LABEL_FONT);
        staffTable.getTableHeader().setFont(UIConstants.TITLE_FONT);
        
        JScrollPane scrollPane = new JScrollPane(staffTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(scrollPane, BorderLayout.CENTER);
        
        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        
        JButton addButton = new JButton("新增");
        JButton editButton = new JButton("编辑");
        JButton deleteButton = new JButton("删除");
        JButton refreshButton = new JButton("刷新");
        
        addButton.setFont(UIConstants.BUTTON_FONT);
        editButton.setFont(UIConstants.BUTTON_FONT);
        deleteButton.setFont(UIConstants.BUTTON_FONT);
        refreshButton.setFont(UIConstants.BUTTON_FONT);
        
        addButton.addActionListener(e -> handleAdd());
        editButton.addActionListener(e -> handleEdit());
        deleteButton.addActionListener(e -> handleDelete());
        refreshButton.addActionListener(e -> refreshData());
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);
        
        loadData();
    }
    
    private void loadData() {
        try {
            tableModel.setRowCount(0);
            staffList = staffDAO.findAll();
            
            for (Staff staff : staffList) {
                String stationName = getStationName(staff.getStationID());
                
                Object[] rowData = {
                    staff.getStaffID(),
                    staff.getStaffName(),
                    staff.getStaffSex(),
                    staff.getStaffPhone(),
                    stationName,
                    staff.getStaffRole()
                };
                tableModel.addRow(rowData);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "加载数据失败: " + e.getMessage(), 
                "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private String getStationName(Integer stationID) {
        if (stationID == null) return "未分配";
        
        try {
            List<Station> stations = stationDAO.findAll();
            for (Station station : stations) {
                if (station.getStationID().equals(stationID)) {
                    return station.getStationName();
                }
            }
            return "驿站ID: " + stationID;
        } catch (Exception e) {
            return "驿站ID: " + stationID;
        }
    }
    
    private void handleAdd() {
        try {
            // 获取驿站列表
            List<Station> stations = stationDAO.findAll();
            if (stations.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请先添加驿站信息", 
                    "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            StaffDialog dialog = new StaffDialog((JFrame) SwingUtilities.getWindowAncestor(this), 
                "添加员工", null, stations);
            dialog.setVisible(true);
            
            if (dialog.isConfirmed()) {
                Staff staff = dialog.getStaff();
                if (staff != null) {
                    // 使用insert方法而不是save方法
                    boolean success = staffDAO.insert(staff);
                    if (success) {
                        refreshData();
                        JOptionPane.showMessageDialog(this, "员工添加成功！");
                    } else {
                        JOptionPane.showMessageDialog(this, "添加失败", 
                            "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "添加失败: " + e.getMessage(), 
                "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleEdit() {
        int selectedRow = staffTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择要编辑的员工", 
                "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int staffID = (int) tableModel.getValueAt(selectedRow, 0);
            Staff staff = null;
            
            // 从列表中查找员工
            for (Staff s : staffList) {
                if (s.getStaffID() != null && s.getStaffID().equals(staffID)) {
                    staff = s;
                    break;
                }
            }
            
            if (staff == null) {
                JOptionPane.showMessageDialog(this, "未找到员工信息");
                return;
            }
            
            // 获取驿站列表
            List<Station> stations = stationDAO.findAll();
            
            StaffDialog dialog = new StaffDialog((JFrame) SwingUtilities.getWindowAncestor(this), 
                "编辑员工", staff, stations);
            dialog.setVisible(true);
            
            if (dialog.isConfirmed()) {
                Staff updatedStaff = dialog.getStaff();
                if (updatedStaff != null) {
                    updatedStaff.setStaffID(staffID);
                    
                    // 使用update方法
                    boolean success = staffDAO.update(updatedStaff);
                    if (success) {
                        refreshData();
                        JOptionPane.showMessageDialog(this, "员工信息更新成功！");
                    } else {
                        JOptionPane.showMessageDialog(this, "更新失败", 
                            "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "编辑失败: " + e.getMessage(), 
                "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleDelete() {
        int selectedRow = staffTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择要删除的员工");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "确定要删除选中的员工吗？", "确认删除", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int staffID = (int) tableModel.getValueAt(selectedRow, 0);
                
                boolean success = staffDAO.delete(staffID);
                if (success) {
                    tableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(this, "员工删除成功！");
                } else {
                    JOptionPane.showMessageDialog(this, "删除失败", 
                        "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "删除失败: " + e.getMessage(), 
                    "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void refreshData() {
        loadData();
    }
    
    private class StaffDialog extends JDialog {
        private boolean confirmed = false;
        private Staff staff;
        
        private JTextField nameField;
        private JComboBox<String> sexComboBox;
        private JTextField phoneField;
        private JComboBox<String> stationComboBox;
        private JTextField roleField;
        private JButton okButton;
        private JButton cancelButton;
        
        private List<Station> stations;
        
        public StaffDialog(JFrame parent, String title, Staff staff, List<Station> stations) {
            super(parent, title, true);
            this.staff = staff != null ? staff : new Staff();
            this.stations = stations;
            initDialog();
        }
        
        private void initDialog() {
            setLayout(new BorderLayout(10, 10));
            setSize(400, 300);
            setLocationRelativeTo(getParent());
            
            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 5, 5, 5);
            
            gbc.gridx = 0; gbc.gridy = 0;
            formPanel.add(new JLabel("姓名:"), gbc);
            gbc.gridx = 1; gbc.weightx = 1.0;
            nameField = new JTextField(15);
            formPanel.add(nameField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
            formPanel.add(new JLabel("性别:"), gbc);
            gbc.gridx = 1; gbc.weightx = 1.0;
            sexComboBox = new JComboBox<>(new String[]{"男", "女"});
            formPanel.add(sexComboBox, gbc);
            
            gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
            formPanel.add(new JLabel("电话:"), gbc);
            gbc.gridx = 1; gbc.weightx = 1.0;
            phoneField = new JTextField(15);
            formPanel.add(phoneField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
            formPanel.add(new JLabel("所属驿站:"), gbc);
            gbc.gridx = 1; gbc.weightx = 1.0;
            stationComboBox = new JComboBox<>();
            for (Station station : stations) {
                stationComboBox.addItem(station.getStationName() + " (ID: " + station.getStationID() + ")");
            }
            formPanel.add(stationComboBox, gbc);
            
            gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
            formPanel.add(new JLabel("角色:"), gbc);
            gbc.gridx = 1; gbc.weightx = 1.0;
            roleField = new JTextField(15);
            formPanel.add(roleField, gbc);
            
            add(formPanel, BorderLayout.CENTER);
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            okButton = new JButton("确定");
            cancelButton = new JButton("取消");
            
            okButton.addActionListener(e -> {
                if (validateInput()) {
                    confirmed = true;
                    setStaffFromForm();
                    dispose();
                }
            });
            
            cancelButton.addActionListener(e -> {
                confirmed = false;
                dispose();
            });
            
            buttonPanel.add(okButton);
            buttonPanel.add(cancelButton);
            add(buttonPanel, BorderLayout.SOUTH);
            
            if (staff.getStaffName() != null) {
                fillFormFromStaff();
            }
            
            getRootPane().setDefaultButton(okButton);
        }
        
        private boolean validateInput() {
            if (nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入员工姓名");
                nameField.requestFocus();
                return false;
            }
            
            if (phoneField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入联系电话");
                phoneField.requestFocus();
                return false;
            }
            
            if (roleField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入员工角色");
                roleField.requestFocus();
                return false;
            }
            
            return true;
        }
        
        private void fillFormFromStaff() {
            nameField.setText(staff.getStaffName());
            if (staff.getStaffSex() != null) {
                sexComboBox.setSelectedItem(staff.getStaffSex());
            }
            phoneField.setText(staff.getStaffPhone());
            roleField.setText(staff.getStaffRole());
            
            // 设置驿站选择
            if (staff.getStationID() != null) {
                for (int i = 0; i < stations.size(); i++) {
                    if (stations.get(i).getStationID().equals(staff.getStationID())) {
                        stationComboBox.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }
        
        private void setStaffFromForm() {
            staff.setStaffName(nameField.getText().trim());
            staff.setStaffSex((String) sexComboBox.getSelectedItem());
            staff.setStaffPhone(phoneField.getText().trim());
            staff.setStaffRole(roleField.getText().trim());
            
            // 从选择中提取驿站ID
            if (stationComboBox.getSelectedIndex() >= 0 && !stations.isEmpty()) {
                int selectedIndex = stationComboBox.getSelectedIndex();
                Station selectedStation = stations.get(selectedIndex);
                staff.setStationID(selectedStation.getStationID());
            }
        }
        
        public boolean isConfirmed() {
            return confirmed;
        }
        
        public Staff getStaff() {
            return staff;
        }
    }
}
