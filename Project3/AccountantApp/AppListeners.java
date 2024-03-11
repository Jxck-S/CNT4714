import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSetMetaData;

import java.awt.Color;
public class AppListeners {
    static public class ExitListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.out.println("Exiting");
            System.exit(0);
        }
    }

    static public class ConnectListener implements ActionListener {
        private AppGUI appGUI;
        private Stored stored_info;
        public ConnectListener(AppGUI appGUI, Stored stored_info) {
            this.appGUI = appGUI;
            this.stored_info = stored_info;
        }
        public void actionPerformed(ActionEvent e) {
            System.out.println("Trying Connect");
            String selected_db = this.appGUI.db_properties_list.getSelectedValue();
            System.out.println("Selected DB Prop File: " + selected_db);
            String selected_user = this.appGUI.user_properties_list.getSelectedValue();
            System.out.println("Selected User File: " + selected_user);
            String username = this.appGUI.username.getText();
            String password = new String(this.appGUI.password.getPassword());
            System.out.println("Username: " + username);
            System.out.println("Password: " + password);
            Properties user_properties = new Properties();
            boolean verified_credentials = false;
            try {
                user_properties.load(new FileInputStream("users/" + selected_user));
                String properties_username = user_properties.getProperty("username");
                String properties_password = user_properties.getProperty("password");
                // compare the username and password to the user and pass from the file to see if they match
                if (username.equals(properties_username) && password.equals(properties_password)) {
                    System.out.println("Credentials Verified");
                    verified_credentials = true;
                } else {
                    String not_verififedString = "NOT CONNECTED - User Credentials Do Not Match Properties File!";
                    System.out.println(not_verififedString);
                    this.appGUI.connectionStatus.setText(not_verififedString);
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex);
            }

            if (verified_credentials){
                //Connect to database using the selected db properties file
                Properties db_properties = new Properties();
                try {
                    db_properties.load(new FileInputStream("dbs/" + selected_db));
                    String db_url = db_properties.getProperty("URL");
                    String JBDC_driver = db_properties.getProperty("Dname");
                    // Begin connection to the database, using the url, password, and username, and JBDC driver
                    Class.forName(JBDC_driver);
                    // Create the connection
                    this.stored_info.dbConnection = DriverManager.getConnection(db_url, username, password);
                    String connectedString = "CONNECTED TO " + db_url;
                    this.appGUI.connectionStatus.setText(connectedString);
                    this.appGUI.connectionStatus.setForeground(Color.YELLOW);
                    System.out.println(connectedString);

                } catch (IOException ex) {
                    System.out.println("Error: " + ex);
                }
                catch (ClassNotFoundException ex) {
                    System.out.println(ex);
                }
                catch (SQLException ex) {
                    System.out.println("Error: " + ex);
                }
                finally {
                    this.appGUI.connectButton.setEnabled(false);
                    this.appGUI.disconnectButton.setEnabled(true);
                    this.appGUI.executeButton.setEnabled(true);
                }
            }

        }
    }
    static public class DisconnectListener implements ActionListener {
        private AppGUI appGUI;
        private Stored stored_info;
        public DisconnectListener(AppGUI appGUI, Stored stored_info) {
            this.appGUI = appGUI;
            this.stored_info = stored_info;
        }
        public void actionPerformed(ActionEvent e) {
            System.out.println("Disconnecting from DB");
            try {
                if (this.stored_info.dbConnection != null && !this.stored_info.dbConnection.isClosed()) {
                    this.stored_info.dbConnection.close();
                }
                this.appGUI.connectionStatus.setText("NO CONNECTION ESTABLISHED");
                this.appGUI.connectionStatus.setForeground(Color.RED);
            } catch (SQLException ex) {
                System.out.println("Error: " + ex);
            }
            finally {
                this.appGUI.connectButton.setEnabled(true);
                this.appGUI.disconnectButton.setEnabled(false);
                this.appGUI.executeButton.setEnabled(false);
            }
        }
    }
    static public class ClearResultListener implements ActionListener {
        private AppGUI appGUI;
        public ClearResultListener(AppGUI appGUI) {
            this.appGUI = appGUI;
        }
        public void actionPerformed(ActionEvent e) {
            System.out.println("Clearing Result Window");
            this.appGUI.clear_results();
        }
    }


    static public class ClearSQListener implements ActionListener {
        private AppGUI appGUI;
        public ClearSQListener(AppGUI appGUI) {
            this.appGUI = appGUI;
        }
        public void actionPerformed(ActionEvent e) {
            System.out.println("Clearing SQL Window");
            this.appGUI.clear_commands();
        }
    }


    static public class ExecuteQuery implements ActionListener {
        private AppGUI appGUI;
        private Stored stored_info;
        public ExecuteQuery(AppGUI appGUI, Stored stored_info) {
            this.appGUI = appGUI;
            this.stored_info = stored_info;
        }
        public void actionPerformed(ActionEvent e) {
            System.out.println("Attempting to execute SQL Query");
            String sql_command = this.appGUI.sqlCommands.getText();
            try {
                Statement statement = this.stored_info.dbConnection.createStatement();
                boolean isResultSet = statement.execute(sql_command);
                if (isResultSet) {
                    ResultSet resultSet = statement.getResultSet();
                    ResultSetMetaData metaData = resultSet.getMetaData();

                        DefaultTableModel model = new DefaultTableModel();
                        this.appGUI.resultsJTable.setModel(model);

                        // Get column names
                        int columnCount = metaData.getColumnCount();
                        String[] columnNames = new String[columnCount];
                        for (int i = 1; i <= columnCount; i++) {
                            columnNames[i - 1] = metaData.getColumnName(i);
                        }
                        model.setColumnIdentifiers(columnNames);

                        // Add rows
                        while (resultSet.next()) {
                            Object[] rowData = new Object[columnCount];
                            for (int i = 1; i <= columnCount; i++) {
                                rowData[i - 1] = resultSet.getObject(i);
                            }
                            model.addRow(rowData);
                        }
                } else {
                    // The command was an update or other
                    int updateCount = statement.getUpdateCount();

                    if (updateCount == -1) {
                        System.out.println("No results returned.");
                    } else {
                        String msg = "Successful Update: " + updateCount + " rows updated.";
                        System.out.println(msg);
                        JOptionPane.showMessageDialog(null, msg, "Successful Update", JOptionPane.INFORMATION_MESSAGE);
                    }
                }

            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                System.out.println("Error: " + ex);
            }


        }
    }
}
