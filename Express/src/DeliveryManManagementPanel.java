import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DeliveryManManagementPanel extends BasePanel {
    private JTable deliveryManTable;
    private DefaultTableModel tableModel;
    private DeliveryManDAO deliveryManDAO;
    private CompanyDAO companyDAO;
    private List<DeliveryMan> deliveryManList;
    private JTextField searchField;
    
    public DeliveryManManagementPanel() {
        try {
            deliveryManDAO = new DeliveryManDAO();
            companyDAO = new CompanyDAO();
            initUI();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "初始化失败: " + e.getMessage());
        }
    }
    
    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // 搜索面板
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        
        JLabel searchLabel = new JLabel("快递员名称：");
        searchLabel.setFont(UIConstants.LABEL_FONT);
        searchField = new JTextField(15);
        searchField.setFont(UIConstants.LABEL_FONT);
        JButton searchButton = new JButton("查询");
        searchButton.setFont(UIConstants.BUTTON_FONT);
        JButton resetButton = new JButton("重置");
        resetButton.setFont(UIConstants.BUTTON_FONT);
        
        searchButton.addActionListener(e -> handleSearch());
        resetButton.addActionListener(e -> {
            searchField.setText("");
            refreshData();
        });
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(resetButton);
        add(searchPanel, BorderLayout.NORTH);
        
        // 表格
        String[] columnNames = {"快递员ID", "姓名", "性别", "电话", "所属公司", "配送类型"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        deliveryManTable = new JTable(tableModel);
        deliveryManTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        deliveryManTable.setRowHeight(30);
        deliveryManTable.setFont(UIConstants.LABEL_FONT);
        deliveryManTable.getTableHeader().setFont(UIConstants.TITLE_FONT);
        
        JScrollPane scrollPane = new JScrollPane(deliveryManTable);
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
            deliveryManList = deliveryManDAO.findAll();
            
            for (DeliveryMan deliveryMan : deliveryManList) {
                String companyName = getCompanyName(deliveryMan.getCompanyID());
                
                Object[] rowData = {
                    deliveryMan.getDeliveryID(),
                    deliveryMan.getDelName(),
                    deliveryMan.getDelSex(),
                    deliveryMan.getDelPhone(),
                    companyName,
                    deliveryMan.getDelType()
                };
                tableModel.addRow(rowData);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "加载数据失败: " + e.getMessage(), 
                "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private String getCompanyName(Integer companyID) {
        if (companyID == null) return "未分配";
        
        try {
            List<Company> companies = companyDAO.findAll();
            for (Company company : companies) {
                if (company.getCompanyID().equals(companyID)) {
                    return company.getCompanyName();
                }
            }
            return "公司ID: " + companyID;
        } catch (Exception e) {
            return "公司ID: " + companyID;
        }
    }
    
    private void handleSearch() {
        String name = searchField.getText().trim();
        if (name.isEmpty()) {
            refreshData();
            return;
        }
        
        try {
            tableModel.setRowCount(0);
            for (DeliveryMan deliveryMan : deliveryManList) {
                if (deliveryMan.getDelName() != null && 
                    deliveryMan.getDelName().toLowerCase().contains(name.toLowerCase())) {
                    
                    String companyName = getCompanyName(deliveryMan.getCompanyID());
                    
                    Object[] rowData = {
                        deliveryMan.getDeliveryID(),
                        deliveryMan.getDelName(),
                        deliveryMan.getDelSex(),
                        deliveryMan.getDelPhone(),
                        companyName,
                        deliveryMan.getDelType()
                    };
                    tableModel.addRow(rowData);
                }
            }
            
            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "未找到匹配的快递员", 
                    "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "查询失败: " + e.getMessage(), 
                "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleAdd() {
        try {
            // 获取公司列表
            List<Company> companies = companyDAO.findAll();
            if (companies.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请先添加快递公司信息", 
                    "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            DeliveryManDialog dialog = new DeliveryManDialog(
                (JFrame) SwingUtilities.getWindowAncestor(this), 
                "添加快递员", null, companies);
            dialog.setVisible(true);
            
            if (dialog.isConfirmed()) {
                DeliveryMan deliveryMan = dialog.getDeliveryMan();
                if (deliveryMan != null) {
                    boolean success = deliveryManDAO.insert(deliveryMan);
                    if (success) {
                        refreshData();
                        JOptionPane.showMessageDialog(this, "快递员添加成功！");
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
        int selectedRow = deliveryManTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择要编辑的快递员", 
                "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int deliveryID = (int) tableModel.getValueAt(selectedRow, 0);
            DeliveryMan deliveryMan = null;
            
            for (DeliveryMan dm : deliveryManList) {
                if (dm.getDeliveryID() != null && dm.getDeliveryID().equals(deliveryID)) {
                    deliveryMan = dm;
                    break;
                }
            }
            
            if (deliveryMan == null) {
                JOptionPane.showMessageDialog(this, "未找到快递员信息");
                return;
            }
            
            // 获取公司列表
            List<Company> companies = companyDAO.findAll();
            
            DeliveryManDialog dialog = new DeliveryManDialog(
                (JFrame) SwingUtilities.getWindowAncestor(this), 
                "编辑快递员", deliveryMan, companies);
            dialog.setVisible(true);
            
            if (dialog.isConfirmed()) {
                DeliveryMan updatedDeliveryMan = dialog.getDeliveryMan();
                if (updatedDeliveryMan != null) {
                    updatedDeliveryMan.setDeliveryID(deliveryID);
                    
                    boolean success = deliveryManDAO.update(updatedDeliveryMan);
                    if (success) {
                        refreshData();
                        JOptionPane.showMessageDialog(this, "快递员信息更新成功！");
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
        int selectedRow = deliveryManTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择要删除的快递员");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "确定要删除选中的快递员吗？", "确认删除", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int deliveryID = (int) tableModel.getValueAt(selectedRow, 0);
                
                boolean success = deliveryManDAO.delete(deliveryID);
                if (success) {
                    tableModel.removeRow(selectedRow);
                    // 从列表中移除
                    for (int i = 0; i < deliveryManList.size(); i++) {
                        if (deliveryManList.get(i).getDeliveryID().equals(deliveryID)) {
                            deliveryManList.remove(i);
                            break;
                        }
                    }
                    JOptionPane.showMessageDialog(this, "快递员删除成功！");
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
    
    private class DeliveryManDialog extends JDialog {
        private boolean confirmed = false;
        private DeliveryMan deliveryMan;
        
        private JTextField nameField;
        private JComboBox<String> sexComboBox;
        private JTextField phoneField;
        private JComboBox<String> companyComboBox;
        private JComboBox<String> typeComboBox;
        private JButton okButton;
        private JButton cancelButton;
        
        private List<Company> companies;
        
        public DeliveryManDialog(JFrame parent, String title, DeliveryMan deliveryMan, List<Company> companies) {
            super(parent, title, true);
            this.deliveryMan = deliveryMan != null ? deliveryMan : new DeliveryMan();
            this.companies = companies;
            initDialog();
        }
        
        private void initDialog() {
            setLayout(new BorderLayout(10, 10));
            setSize(400, 350);
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
            formPanel.add(new JLabel("所属公司:"), gbc);
            gbc.gridx = 1; gbc.weightx = 1.0;
            companyComboBox = new JComboBox<>();
            for (Company company : companies) {
                companyComboBox.addItem(company.getCompanyName() + " (ID: " + company.getCompanyID() + ")");
            }
            formPanel.add(companyComboBox, gbc);
            
            gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
            formPanel.add(new JLabel("配送类型:"), gbc);
            gbc.gridx = 1; gbc.weightx = 1.0;
            typeComboBox = new JComboBox<>(new String[]{"普通配送", "加急配送", "特殊配送", "夜间配送"});
            formPanel.add(typeComboBox, gbc);
            
            add(formPanel, BorderLayout.CENTER);
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            okButton = new JButton("确定");
            cancelButton = new JButton("取消");
            
            okButton.addActionListener(e -> {
                if (validateInput()) {
                    confirmed = true;
                    setDeliveryManFromForm();
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
            
            if (deliveryMan.getDelName() != null) {
                fillFormFromDeliveryMan();
            }
            
            getRootPane().setDefaultButton(okButton);
        }
        
        private boolean validateInput() {
            if (nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入快递员姓名");
                nameField.requestFocus();
                return false;
            }
            
            if (phoneField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入联系电话");
                phoneField.requestFocus();
                return false;
            }
            
            // 简单的电话格式验证
            String phone = phoneField.getText().trim();
            if (!phone.matches("^1[3-9]\\d{9}$")) {
                JOptionPane.showMessageDialog(this, "请输入正确的手机号码");
                phoneField.requestFocus();
                return false;
            }
            
            return true;
        }
        
        private void fillFormFromDeliveryMan() {
            nameField.setText(deliveryMan.getDelName());
            if (deliveryMan.getDelSex() != null) {
                sexComboBox.setSelectedItem(deliveryMan.getDelSex());
            }
            phoneField.setText(deliveryMan.getDelPhone());
            
            // 设置公司选择
            if (deliveryMan.getCompanyID() != null) {
                for (int i = 0; i < companies.size(); i++) {
                    if (companies.get(i).getCompanyID().equals(deliveryMan.getCompanyID())) {
                        companyComboBox.setSelectedIndex(i);
                        break;
                    }
                }
            }
            
            // 设置配送类型选择
            if (deliveryMan.getDelType() != null) {
                for (int i = 0; i < typeComboBox.getItemCount(); i++) {
                    if (typeComboBox.getItemAt(i).equals(deliveryMan.getDelType())) {
                        typeComboBox.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }
        
        private void setDeliveryManFromForm() {
            deliveryMan.setDelName(nameField.getText().trim());
            deliveryMan.setDelSex((String) sexComboBox.getSelectedItem());
            deliveryMan.setDelPhone(phoneField.getText().trim());
            deliveryMan.setDelType((String) typeComboBox.getSelectedItem());
            
            // 从选择中提取公司ID
            if (companyComboBox.getSelectedIndex() >= 0 && !companies.isEmpty()) {
                int selectedIndex = companyComboBox.getSelectedIndex();
                Company selectedCompany = companies.get(selectedIndex);
                deliveryMan.setCompanyID(selectedCompany.getCompanyID());
            }
        }
        
        public boolean isConfirmed() {
            return confirmed;
        }
        
        public DeliveryMan getDeliveryMan() {
            return deliveryMan;
        }
    }
}
