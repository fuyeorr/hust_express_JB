import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StationReportPanel extends BasePanel {
    private JTable reportTable;
    private DefaultTableModel tableModel;
    private StationReportService reportService;
    private JTextField stationField;
    private JLabel storageLabel;
    private JLabel signLabel;
    private JLabel exceptionLabel;

    public StationReportPanel() {
        this.reportService = new StationReportService();
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 日期选择面板
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        datePanel.setBackground(UIConstants.BACKGROUND_COLOR);

        JLabel stationLabel = new JLabel("站点ID：");
        stationField = new JTextField(6);
        JButton queryButton = new JButton("查询");
        queryButton.addActionListener(e -> handleQuery());

        datePanel.add(stationLabel);
        datePanel.add(stationField);
        datePanel.add(queryButton);
        add(datePanel, BorderLayout.NORTH);

        // 统计信息面板
        JPanel statsPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        statsPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        statsPanel.setBorder(BorderFactory.createTitledBorder("统计概览"));

        storageLabel = createStatLabel(statsPanel, "入库总数");
        signLabel = createStatLabel(statsPanel, "签收总数");
        exceptionLabel = createStatLabel(statsPanel, "异常数量");
        createStatLabel(statsPanel, "平均配送时间");
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

    private JLabel createStatLabel(JPanel container, String name) {
        JPanel stat = new JPanel();
        stat.setBackground(UIConstants.BACKGROUND_COLOR);
        JLabel label = new JLabel(name + ": 0");
        stat.add(label);
        container.add(stat);
        return label;
    }

    private void loadData() {
        stationField.setText("1");
        handleQuery();
    }

    private void handleQuery() {
        String stationText = stationField.getText().trim();
        if (stationText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入站点ID");
            return;
        }

        try {
            int stationId = Integer.parseInt(stationText);
            StationReportService.StationReportResult result = reportService.stationDailyReport(stationId);

            if (result == null) {
                JOptionPane.showMessageDialog(this, "暂无数据");
                return;
            }

            storageLabel.setText("入库总数: " + result.getStorageCount());
            signLabel.setText("签收总数: " + result.getSignCount());
            exceptionLabel.setText("异常数量: " + result.getExceptionCount());

            tableModel.setRowCount(0);
            for (StationReportService.StationReportResult.DeliveryStat stat : result.getDeliveryStats()) {
                tableModel.addRow(new Object[]{
                        stat.getDeliveryId(),
                        stat.getDeliveryCount(),
                        "",
                        "",
                        "N/A"
                });
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "站点ID必须是数字");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "查询失败: " + ex.getMessage());
        }
    }

    private void handleExport() {
        JOptionPane.showMessageDialog(this, "导出Excel功能开发中");
    }
}
