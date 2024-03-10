import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;


public class AppGUI {

    JFrame frame = new JFrame();
    JList<String> db_properties_list;
    JList<String> user_properties_list;
    JScrollPane db_properties_scroller;
    JScrollPane user_properties_scroller;
    JButton connectButton = new JButton();
    JButton clearSQLButton = new JButton("Clear SQL Command");
    JButton clearResultsButton = new JButton();
    JTextArea sqlCommands = new JTextArea();
    JTextArea sqlResults = new JTextArea();
    JLabel connectionStatus = new JLabel();
    JTextField username = new JTextField();
    JPasswordField password = new JPasswordField();
    JButton disconnectButton = new JButton("Disconnect");
    JButton executeButton = new JButton("Execute SQL Command");
    JTable resultsJTable = new JTable();



    public AppGUI(int width, int height) {
        frame.setTitle("Accountant Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        Font sectionLabelFont = new Font("Arial", Font.BOLD, 16);
        LineBorder innerBorders = new LineBorder(Color.BLACK, 2);
        Color brownish = Color.decode("#594131");
        Color yellow_white = Color.decode("#bfb458");
        // DB Connection Area
        JPanel dbConnectionAreaInnerPanel = new JPanel(new GridLayout(5, 2, 5, 15));
        java.util.List<String> filenames = FileListUtil.getFileNamesFromDirectory("dbs");
        db_properties_list = FileListUtil.convertToJList(filenames);

        dbConnectionAreaInnerPanel.add(new JLabel("DB URL Properties:", SwingConstants.RIGHT));
        this.db_properties_scroller = new JScrollPane(db_properties_list);
        dbConnectionAreaInnerPanel.add(this.db_properties_scroller);

        java.util.List<String> user_filenames = FileListUtil.getFileNamesFromDirectory("users");
        user_properties_list = FileListUtil.convertToJList(user_filenames);


        dbConnectionAreaInnerPanel.add(new JLabel("User Properties:", SwingConstants.RIGHT));
        this.user_properties_scroller = new JScrollPane(user_properties_list);
        dbConnectionAreaInnerPanel.add(this.user_properties_scroller);

        dbConnectionAreaInnerPanel.add(new JLabel("Username:", SwingConstants.RIGHT));
        dbConnectionAreaInnerPanel.add(username);
        dbConnectionAreaInnerPanel.add(new JLabel("Password:", SwingConstants.RIGHT));
        dbConnectionAreaInnerPanel.add(password);
        connectButton.setText("Connect");
        disconnectButton.setEnabled(false);
        executeButton.setEnabled(false);
        dbConnectionAreaInnerPanel.add(connectButton);
        dbConnectionAreaInnerPanel.add(disconnectButton);
        dbConnectionAreaInnerPanel.setBorder(innerBorders);
        JPanel dbConnectionAreaOutterPanel = new JPanel(new BorderLayout());
        JLabel connectionDetailsLabel = new JLabel("Connection Details", SwingConstants.CENTER);
        connectionDetailsLabel.setFont(sectionLabelFont);
        dbConnectionAreaOutterPanel.add(connectionDetailsLabel, BorderLayout.NORTH);
        dbConnectionAreaOutterPanel.add(dbConnectionAreaInnerPanel, BorderLayout.CENTER);

        JPanel outterSQLCommandPanel = new JPanel();
        outterSQLCommandPanel.setLayout(new BorderLayout()); // Set BorderLayout for outer panel

        // Add label to the NORTH region
        JLabel sqlCommandsLabel = new JLabel("Enter SQL Command", SwingConstants.CENTER);
        sqlCommandsLabel.setFont(sectionLabelFont);
        outterSQLCommandPanel.add(sqlCommandsLabel, BorderLayout.NORTH);

        JPanel innerSQLCommandPanel = new JPanel();
        innerSQLCommandPanel.setLayout(new BorderLayout()); // Set BorderLayout for inner panel
        innerSQLCommandPanel.setBorder(innerBorders);
        // Add text area to the CENTER region (it will take up most of the space)
        innerSQLCommandPanel.add(new JScrollPane(sqlCommands), BorderLayout.CENTER);

        // Add buttons to the SOUTH region
        JPanel commandButtons = new JPanel();
        commandButtons.add(clearSQLButton);
        commandButtons.add(executeButton);
        innerSQLCommandPanel.add(commandButtons, BorderLayout.SOUTH);

        // Add the inner panel to the outer panel's CENTER region
        outterSQLCommandPanel.add(innerSQLCommandPanel, BorderLayout.CENTER);

        // SQL Execution Result Window

        JScrollPane resultScrollPane = new JScrollPane(resultsJTable);
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.add(new JLabel("SQL Execution Results Window"), BorderLayout.NORTH);
        resultPanel.add(resultScrollPane, BorderLayout.CENTER);
        clearResultsButton.setText("Clear Results Window");
        resultPanel.add(clearResultsButton, BorderLayout.SOUTH);

        // Status of Connection Window
        connectionStatus.setText("NO CONNECTION ESTABLISHED");

        connectionStatus.setForeground(Color.RED);
        JPanel statusPanel = new JPanel();
        statusPanel.setBackground(Color.BLACK);
        statusPanel.add(connectionStatus);

        // Add all panels to the frame
        Border border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        JPanel panels = new JPanel();
        panels.setBorder(border);
        panels.setLayout(new GridLayout(2, 1));

        JPanel topPanel = new JPanel(new GridLayout(1, 2, 10, 5));
        topPanel.setBorder(border);
        topPanel.add(dbConnectionAreaOutterPanel);
        topPanel.add(outterSQLCommandPanel);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(border);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.add(statusPanel);
        bottomPanel.add(resultPanel);

        panels.add(topPanel);
        panels.add(bottomPanel);

        panels.setBackground(brownish);
        JPanel outterMostPanel = new JPanel();
        outterMostPanel.setLayout(new BorderLayout());
        JLabel title = new JLabel("Accountant Application", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(brownish);
        outterMostPanel.add(title, BorderLayout.NORTH);
        outterMostPanel.add(panels, BorderLayout.CENTER);
        outterMostPanel.setBackground(yellow_white);
        frame.add(outterMostPanel);

    }
    public void clear_results(){
        DefaultTableModel model = (DefaultTableModel) this.resultsJTable.getModel();
        model.setRowCount(0);
        model.setColumnCount(0);
        this.resultsJTable.revalidate();

    }
    public void clear_commands(){
        this.sqlCommands.setText(null);

    }
}


