import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StationReportPanel extends BasePanel {
    private JTable reportTable;
    private DefaultTableModel tableModel;

    public StationReportPanel() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 日期选择面板
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        datePanel.setBackground(UIConstants.BACKGROUND_COLOR);

        JLabel startLabel = new JLabel("开始日期：");
        JTextField startDateField = new JTextField(10);
        JLabel endLabel = new JLabel("结束日期：");
        JTextField endDateField = new JTextField(10);
        JButton queryButton = new JButton("查询");
        queryButton.addActionListener(e -> handleQuery());

        datePanel.add(startLabel);
        datePanel.add(startDateField);
        datePanel.add(endLabel);
        datePanel.add(endDateField);
        datePanel.add(queryButton);
        add(datePanel, BorderLayout.NORTH);

        // 统计信息面板
        JPanel statsPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        statsPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        statsPanel.setBorder(BorderFactory.createTitledBorder("统计概览"));

        String[] statLabels = {"入库总数", "签收总数", "异常数量", "平均配送时间"};
        for (String label : statLabels) {
            JPanel stat = new JPanel();
            stat.setBackground(UIConstants.BACKGROUND_COLOR);
            stat.add(new JLabel(label + ": 0"));
            statsPanel.add(stat);
        }
        add(statsPanel, BorderLayout.NORTH);

        // 表格
        String[] columnNames = {"快递员", "配送数", "签收数", "异常数", "平均用时"};
        tableModel = new DefaultTableModel(columnNames, 0);
        reportTable = new JTable(tableModel);
        reportTable.setFont(UIConstants.LABEL_FONT);
        add(new JScrollPane(reportTable), BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(UIConstants.BACKGROUND_COLOR);

        JButton exportButton = new JButton("导出Excel");
        exportButton.addActionListener(e -> handleExport());

        buttonPanel.add(exportButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadData();
    }

    private void loadData() {
        // TODO: 从数据库加载报表数据
        tableModel.addRow(new Object[]{"张三", 50, 48, 2, "2小时"});
        tableModel.addRow(new Object[]{"李四", 45, 43, 2, "2.5小时"});
    }

    private void handleQuery() {
        // TODO: 根据日期范围查询
        loadData();
    }

    private void handleExport() {
        JOptionPane.showMessageDialog(this, "导出Excel功能开发中");
    }
}