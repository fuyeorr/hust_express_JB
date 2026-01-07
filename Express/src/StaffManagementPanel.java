import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StaffManagementPanel extends BasePanel {
    private JTable staffTable;
    private DefaultTableModel tableModel;

    public StaffManagementPanel() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 表格
        String[] columnNames = {"员工ID", "姓名", "电话", "所属驿站", "角色", "状态"};
        tableModel = new DefaultTableModel(columnNames, 0);
        staffTable = new JTable(tableModel);
        staffTable.setFont(UIConstants.LABEL_FONT);
        add(new JScrollPane(staffTable), BorderLayout.CENTER);

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
        // TODO: 从 StaffDAO 加载数据
        tableModel.addRow(new Object[]{"S001", "李四", "13900139000", "北京驿站", "管理员", "在线"});
    }

    private void handleAdd() {
        JOptionPane.showMessageDialog(this, "添加员工");
    }

    private void handleEdit() {
        int selectedRow = staffTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择员工");
            return;
        }
        JOptionPane.showMessageDialog(this, "编辑员工信息");
    }

    private void handleDelete() {
        int selectedRow = staffTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请选择员工");
            return;
        }
        tableModel.removeRow(selectedRow);
    }
}