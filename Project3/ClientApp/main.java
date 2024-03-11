/*
Name: Jack Sweeney
Course: CNT 4714 Spring 2024
Assignment title: Project 3 – A Two-tier Client-Server Application
Date: March 10, 2024
Class: main.java
*/
import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class main {
    public static void main(String[] args) {
        AppGUI appGUI = new AppGUI(1000, 700);
        Stored stored_info = new Stored();
        Properties log_db_properties = new Properties();
        Exception exception = null;
        try {
            log_db_properties.load(new FileInputStream("opplog.properties"));
            String db_url = log_db_properties.getProperty("URL");
            String JBDC_driver = log_db_properties.getProperty("Dname");
            // Begin connection to the database, using the url, password, and username, and JBDC driver
            Class.forName(JBDC_driver);
            stored_info.opp_log_dbConnection = DriverManager.getConnection(db_url, log_db_properties.getProperty("username"), log_db_properties.getProperty("password"));
        } catch (SQLException ex) {
            exception = ex;
        }
        catch (ClassNotFoundException ex) {
            exception = ex;
        }
        catch (IOException ex) {
            exception = ex;
        }
        finally {
            if (stored_info.opp_log_dbConnection != null) {
                System.out.println("Connected to opplog");
                appGUI.connectButton.addActionListener(new AppListeners.ConnectListener(appGUI, stored_info));
                appGUI.clearResultsButton.addActionListener(new AppListeners.ClearResultListener(appGUI));
                appGUI.clearSQLButton.addActionListener(new AppListeners.ClearSQListener(appGUI));
                appGUI.disconnectButton.addActionListener(new AppListeners.DisconnectListener(appGUI, stored_info));


                appGUI.executeButton.addActionListener(new AppListeners.ExecuteQuery(appGUI, stored_info));


                appGUI.frame.setVisible(true);
            }
            else {
                System.out.println("Not connected to opplog");
                System.out.println("Error: " + exception);
                JOptionPane.showMessageDialog(null, "Couldn't connect to opperations log DB" + exception.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);

            }
        }


    }
}
