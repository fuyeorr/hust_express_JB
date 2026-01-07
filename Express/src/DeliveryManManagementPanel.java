import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DeliveryManManagementPanel extends BasePanel {
    private JTable deliveryManTable;
    private DefaultTableModel tableModel;

    public DeliveryManManagementPanel() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 搜索面板
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(UIConstants.BACKGROUND_COLOR);

        JLabel searchLabel = new JLabel("快递员名称：");
        JTextField searchField = new JTextField(15);
        JButton searchButton = new JButton("查询");
        searchButton.addActionListener(e -> handleSearch(searchField.getText()));

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        // 表格
        String[] columnNames = {"快递员ID", "姓名", "电话", "所属公司", "配送单数", "状态"};
        tableModel = new DefaultTableModel(columnNames, 0);
        deliveryManTable = new JTable(tableModel);
        deliveryManTable.setFont(UIConstants.LABEL_FONT);
        add(new JScrollPane(deliveryManTable), BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(UIConstants.BACKGROUND_COLOR);

        JButton addButton = new JButton("新增");
        JButton editButton = new JButton("编辑");
        JButton deleteButton = new JButton("删除");

        addButton.addActionListener(e -> handleAdd());
        editButton.addActionListener(e -> handleEdit());
        deleteButton.addActionListener(e -> handleDelete());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadData();
    }

    private void loadData() {
        // TODO: 从 DeliveryManDAO 加载数据
        tableModel.addRow(new Object[]{"D001", "张三", "13800138000", "顺丰", 125, "在线"});
    }

    private void handleSearch(String name) {
        // TODO: 查询快递员
    }

    private void handleAdd() {
        JOptionPane.showMessageDialog(this, "添加快递员");
    }

    private void handleEdit() {
        int selectedRow = deliveryManTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择快递员");
            return;
        }
        JOptionPane.showMessageDialog(this, "编辑快递员信息");
    }

    private void handleDelete() {
        int selectedRow = deliveryManTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择快递员");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "确定删除吗？");
        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(selectedRow);
        }
    }
}