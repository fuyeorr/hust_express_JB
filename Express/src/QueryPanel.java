import javax.swing.*;
import java.awt.*;

public class QueryPanel extends BasePanel {
    private JTextField queryField;
    private ResultsPanel resultsPanel;
    private QueryService queryService;

    public QueryPanel() {
        this.queryService = new QueryService();
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(UIConstants.BACKGROUND_COLOR);
        top.add(new JLabel("包裹ID 或 用户ID："));
        queryField = new JTextField(20);
        top.add(queryField);
        JButton searchBtn = new JButton("查询");
        searchBtn.addActionListener(e -> handleSearch());
        top.add(searchBtn);
        add(top, BorderLayout.NORTH);

        resultsPanel = new ResultsPanel();
        add(resultsPanel, BorderLayout.CENTER);
    }

    private void handleSearch() {
        String text = queryField.getText().trim();
        if (text.isEmpty()) {
            // load all
            try {
                resultsPanel.showPackages(queryService.getAllPackages());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "查询失败: " + ex.getMessage());
            }
            return;
        }

        try {
            int id = Integer.parseInt(text);
            Object pkg = queryService.getPackageInfo(id);
            if (pkg != null) {
                resultsPanel.showSingle(pkg);
            } else {
                JOptionPane.showMessageDialog(this, "未找到包裹");
                resultsPanel.showPackages(null);
            }
        } catch (NumberFormatException nf) {
            JOptionPane.showMessageDialog(this, "请输入数字包裹ID或留空查看全部");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "查询失败: " + ex.getMessage());
        }
    }
}
