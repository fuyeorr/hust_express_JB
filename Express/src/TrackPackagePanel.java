import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TrackPackagePanel extends BasePanel {
    private JTable trackTable;
    private DefaultTableModel tableModel;
    private JTextArea detailsArea;
    private QueryService queryService;

    public TrackPackagePanel() {
        this.queryService = new QueryService();
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 搜索面板
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(UIConstants.BACKGROUND_COLOR);

        JLabel searchLabel = new JLabel("包裹ID或运单号：");
        JTextField searchField = new JTextField(15);
        JButton searchButton = new JButton("查询");
        searchButton.addActionListener(e -> handleSearch(searchField.getText()));

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        // 轨迹表格
        String[] columnNames = {"时间", "位置", "状态", "说明"};
        tableModel = new DefaultTableModel(columnNames, 0);
        trackTable = new JTable(tableModel);
        trackTable.setFont(UIConstants.LABEL_FONT);

        // 详情区域
        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(UIConstants.LABEL_FONT);
        detailsArea.setBorder(BorderFactory.createTitledBorder("异常记录"));

        // 分割面板
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(new JScrollPane(trackTable));
        splitPane.setRightComponent(new JScrollPane(detailsArea));
        splitPane.setDividerLocation(400);

        add(splitPane, BorderLayout.CENTER);
    }

    private void handleSearch(String trackId) {
        if (trackId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入包裹ID或运单号");
            return;
        }

        try {
            int packageId = Integer.parseInt(trackId);
            
            // 查询包裹信息
            PackageEntity packageEntity = queryService.getPackageInfo(packageId);
            
            if (packageEntity == null) {
                JOptionPane.showMessageDialog(this, "未找到该包裹");
                return;
            }

            // 查询运输轨迹
            List<Track> tracks = queryService.getPackageTracks(packageId);

            // 清空旧数据
            tableModel.setRowCount(0);

            // 填充轨迹数据
            if (tracks != null && !tracks.isEmpty()) {
                for (Track track : tracks) {
                    tableModel.addRow(new Object[]{
                        track.getTrackTime(),
                        track.getCurrentLocation(),
                        track.getTrackInfo(),
                        track.getTrackInfo()
                    });
                }
            } else {
                JOptionPane.showMessageDialog(this, "暂无轨迹信息");
            }

            // 显示异常记录
            List<ExceptionRecord> exceptions = queryService.getPackageExceptions(packageId);
            if (exceptions == null || exceptions.isEmpty()) {
                detailsArea.setText("无异常记录");
            } else {
                StringBuilder sb = new StringBuilder();
                for (ExceptionRecord ex : exceptions) {
                    sb.append(ex.getExceptionType() != null ? ex.getExceptionType() : "异常")
                            .append(": ")
                            .append(ex.getDescription() != null ? ex.getDescription() : "")
                            .append("\n");
                }
                detailsArea.setText(sb.toString());
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "包裹ID必须是数字");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "查询失败: " + ex.getMessage());
        }
    }
}
