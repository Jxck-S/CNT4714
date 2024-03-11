/*
Name: Jack Sweeney
Course: CNT 4714 Spring 2024
Assignment title: Project 3 – A Specialized Accountant Application
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

        appGUI.connectButton.addActionListener(new AppListeners.ConnectListener(appGUI, stored_info));
        appGUI.clearResultsButton.addActionListener(new AppListeners.ClearResultListener(appGUI));
        appGUI.clearSQLButton.addActionListener(new AppListeners.ClearSQListener(appGUI));
        appGUI.disconnectButton.addActionListener(new AppListeners.DisconnectListener(appGUI, stored_info));


        appGUI.executeButton.addActionListener(new AppListeners.ExecuteQuery(appGUI, stored_info));


        appGUI.frame.setVisible(true);



    }
}
