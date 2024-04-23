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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("serial")
class Shipment {
	private String snum;
	private String pnum;
	private String jnum;

	public Shipment(String snum, String pnum, String jnum) {
		this.snum = snum;
		this.pnum = pnum;
		this.jnum = jnum;
	}

	public String getSnum() {
		return snum;
	}

	public String getPnum() {
		return pnum;
	}

	public String getJnum() {
		return jnum;
	}
}

public class RootServlet extends HttpServlet {
	@Override
	protected void doPost( HttpServletRequest request, HttpServletResponse response )
		throws ServletException, IOException
	{
		HttpSession session = request.getSession();
		session.setAttribute("current_command", request.getParameter("commands"));
		try {
			Properties authdb_props = new Properties();
			String file = getServletContext().getRealPath("/WEB-INF/lib/root.properties");
			authdb_props.load(new FileInputStream(file));
			String db_url = authdb_props.getProperty("URL");
			Connection root_db_conn = DriverManager.getConnection(db_url, authdb_props.getProperty("username"), authdb_props.getProperty("password"));
			String commands = request.getParameter("commands");
			Statement statement = root_db_conn.createStatement();

			// Get existing snums, to know what shippments already exist
			Statement existing_shipments = root_db_conn.createStatement();
			existing_shipments.execute("SELECT snum, pnum, jnum FROM shipments WHERE quantity >= 100");
			ResultSet rs = existing_shipments.getResultSet();

			// ...

			List<Shipment> snumsList_above_100 = new ArrayList<>();
			while (rs.next()) {
				String snum = rs.getString("snum");
				String pnum = rs.getString("pnum");
				String jnum = rs.getString("jnum");
				Shipment shipment = new Shipment(snum, pnum, jnum);
				snumsList_above_100.add(shipment);
			}


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


			StringBuilder existing_100_String = new StringBuilder("(");

			// Append the values from the list as tuples
			for (int i = 0; i < snumsList_above_100.size(); i++) {
				Shipment obj = snumsList_above_100.get(i);
				existing_100_String.append("(\"")
						.append(obj.getSnum())
						.append("\", \"")
						.append(obj.getPnum())
						.append("\", \"")
						.append(obj.getJnum())
						.append("\")");
				if (i < snumsList_above_100.size() - 1) {
					existing_100_String.append(", ");
				}
			}
			existing_100_String.append(")");

			String sql = "UPDATE suppliers SET status = status + 5 WHERE snum IN (" +
			"    SELECT snum FROM shipments " +
			"    WHERE quantity >= 100 AND (snum, pnum, jnum) NOT IN" + existing_100_String + ")";


			PreparedStatement update_sup_Statement = root_db_conn.prepareStatement(sql);
			update_sup_Statement.executeUpdate();
			int update_sup_count = update_sup_Statement.getUpdateCount();
			session.setAttribute("update_sup_count", update_sup_count);


			// Close the connection
		} catch (Exception e) {
			session.setAttribute("result_type", "error");
			session.setAttribute("result", e.getMessage());
		}
		// Forward the request and response objects to the current page
		response.sendRedirect("rootHome.jsp");
	}
}

