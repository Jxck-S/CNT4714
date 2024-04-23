
/* Name: Jack Sweeney
Course: CNT 4714 – Spring 2024 – Project Four
Assignment title: A Three-Tier Distributed Web-Based Application
Date: April 23, 2024
*/
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.Statement;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.PreparedStatement;

@SuppressWarnings("serial")

public class AccountantServlet extends HttpServlet {
	@Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response )
    throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        try {
          Properties authdb_props = new Properties();
          String file = getServletContext().getRealPath("/WEB-INF/lib/accountant.properties");
          authdb_props.load(new FileInputStream(file));
          String db_url = authdb_props.getProperty("URL");

          Connection auth_db_connection = DriverManager.getConnection(db_url, authdb_props.getProperty("username"), authdb_props.getProperty("password"));
          String operation = request.getParameter("operation");
          String commands = "";

          switch (operation) {
            case "max_status":
              //Get The Maximum Status Value Of All Suppliers
              commands = "CALL Get_The_Maximum_Status_Of_All_Suppliers()";
              break;
            case "total_weight":
              //Get The Total Weight Of All Parts
              commands = "CALL Get_The_Sum_Of_All_Parts_Weights()";
              break;
            case "total_shipments":
              //Get The Total Number of Shipments
              commands = "CALL Get_The_Total_Number_Of_Shipments()";
              break;
            case "most_workers":
              //Get The Name And Number Of Workers Of The Job With The Most Workers
              commands = "CALL Get_The_Name_Of_The_Job_With_The_Most_Workers()";
              break;
            case "supplier_names":
              //List The Name And Status Of Every Supplier
              commands = "CALL List_The_Name_And_Status_Of_All_Suppliers()";
              break;
            default:
              // Handle invalid operation
              throw new Exception("Invalid operation");
          }

          Statement statement = auth_db_connection.createStatement();
          boolean isResultSet = statement.execute(commands);
          if (isResultSet) {
            ResultSet resultSet = statement.getResultSet();
            session.setAttribute("result_type", "resultset");
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
              columnNames[i - 1] = metaData.getColumnName(i);
            }
            session.setAttribute("column_names", columnNames);
            session.setAttribute("result", resultSet);
          } else {
              // The command was an update or other
              int updateCount = statement.getUpdateCount();

              if (updateCount == -1) {
                  session.setAttribute("result_type", "update_count");
                  session.setAttribute("result", updateCount);
              } else {
                session.setAttribute("result_type", "update_count");
                session.setAttribute("result", updateCount);
              }
          }

        } catch (Exception e) {
          session.setAttribute("result_type", "error");
          session.setAttribute("result", e.getMessage());
        }
        response.sendRedirect("accountantHome.jsp");
      }
}
