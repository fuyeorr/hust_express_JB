package ui;

import javax.swing.*;
import java.awt.*;
import java.util.prefs.Preferences;

public class MainWindow extends JFrame{
    private ConnectionPanel connectionPanel;
    private QueryPanel queryPanel;
    private ResultsPanel resultsPanel;
    
    public MainWindow() {
        // Window settings
        setTitle("We are Shappy!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocation(0, 0);
        setResizable(true);

        // Init panels
        connectionPanel = new ConnectionPanel();
        queryPanel = new QueryPanel();
        resultsPanel = new ResultsPanel();

        //Layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(connectionPanel, BorderLayout.NORTH);
        mainPanel.add(queryPanel, BorderLayout.CENTER);
        mainPanel.add(resultsPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }
}
