import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ResultsPanel extends BasePanel {
    private JTable table;
    private DefaultTableModel model;

    public ResultsPanel() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        String[] cols = {"包裹ID", "订单ID", "重量", "体积", "包裹状态", "当前状态"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setFont(UIConstants.LABEL_FONT);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void showPackages(List packages) {
        model.setRowCount(0);
        if (packages == null) return;
        for (Object o : packages) {
            try {
                // PackageEntity has getters used below
                Object id = o.getClass().getMethod("getPackageID").invoke(o);
                Object orderId = o.getClass().getMethod("getOrderID").invoke(o);
                Object weight = o.getClass().getMethod("getWeight").invoke(o);
                Object volume = o.getClass().getMethod("getVolume").invoke(o);
                Object status = o.getClass().getMethod("getPackageStatus").invoke(o);
                Object cur = o.getClass().getMethod("getCurrentStatus").invoke(o);
                model.addRow(new Object[]{id, orderId, weight, volume, status, cur});
            } catch (Exception ex) {
                // ignore individual mapping errors
            }
        }
    }

    public void showSingle(Object pkg) {
        model.setRowCount(0);
        if (pkg == null) return;
        try {
            Object id = pkg.getClass().getMethod("getPackageID").invoke(pkg);
            Object orderId = pkg.getClass().getMethod("getOrderID").invoke(pkg);
            Object weight = pkg.getClass().getMethod("getWeight").invoke(pkg);
            Object volume = pkg.getClass().getMethod("getVolume").invoke(pkg);
            Object status = pkg.getClass().getMethod("getPackageStatus").invoke(pkg);
            Object cur = pkg.getClass().getMethod("getCurrentStatus").invoke(pkg);
            model.addRow(new Object[]{id, orderId, weight, volume, status, cur});
        } catch (Exception ex) {
            // ignore
        }
    }
}
 
