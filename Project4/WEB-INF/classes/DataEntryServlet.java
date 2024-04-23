
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
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.PreparedStatement;

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

public class DataEntryServlet extends HttpServlet {
	@Override
	protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		HttpSession session = request.getSession();
		try {
			Properties data_entry_props = new Properties();
			String file = getServletContext().getRealPath("/WEB-INF/lib/dataentry.properties");
			data_entry_props.load(new FileInputStream(file));
			String db_url = data_entry_props.getProperty("URL");
			Connection data_entry_conn = DriverManager.getConnection(db_url, data_entry_props.getProperty("username"), data_entry_props.getProperty("password"));
			String table = request.getParameter("t");
			session.setAttribute("table", table);
			// Storing all request attributes in session for reload to keep them in the forms
			PreparedStatement statement = null;
			String values = "";
			switch (table) {
				case "suppliers":
					statement = data_entry_conn.prepareStatement("INSERT INTO suppliers (snum, sname, status, city) VALUES (?, ?, ?, ?)");
					statement.setString(1, request.getParameter("sup_snum"));
					statement.setString(2, request.getParameter("sup_sname"));
					statement.setInt(3, Integer.parseInt(request.getParameter("sup_status")));
					statement.setString(4, request.getParameter("sup_city"));
					values = "(" + request.getParameter("sup_snum") + ", " + request.getParameter("sup_sname") + ", " + request.getParameter("sup_status") + ", " + request.getParameter("sup_city") + ")";
					break;
				case "parts":
					statement = data_entry_conn.prepareStatement("INSERT INTO parts (pnum, pname, color, weight, city) VALUES (?, ?, ?, ?, ?)");
					statement.setString(1, request.getParameter("part_pnum"));
					statement.setString(2, request.getParameter("part_pname"));
					statement.setString(3, request.getParameter("part_color"));
					statement.setInt(4, Integer.parseInt(request.getParameter("part_weight")));
					statement.setString(5, request.getParameter("part_city"));
					values = "(" + request.getParameter("part_pnum") + ", " + request.getParameter("part_pname") + ", " + request.getParameter("part_color") + ", " + request.getParameter("part_weight") + ", " + request.getParameter("part_city") + ")";
					break;
				case "jobs":
					statement = data_entry_conn.prepareStatement("INSERT INTO jobs (jnum, jname, numworkers, city) VALUES (?, ?, ?, ?)");
					statement.setString(1, request.getParameter("job_jnum"));
					statement.setString(2, request.getParameter("job_jname"));
					statement.setInt(3, Integer.parseInt(request.getParameter("job_numworkers")));
					statement.setString(4, request.getParameter("job_city"));
					values = "(" + request.getParameter("job_jnum") + ", " + request.getParameter("job_jname") + ", " + request.getParameter("job_numworkers") + ", " + request.getParameter("job_city") + ")";
					break;
				case "shipments":
					statement = data_entry_conn.prepareStatement("INSERT INTO shipments (snum, pnum, jnum, quantity) VALUES (?, ?, ?, ?)");
					statement.setString(1, request.getParameter("ship_snum"));
					statement.setString(2, request.getParameter("ship_pnum"));
					statement.setString(3, request.getParameter("ship_jnum"));
					statement.setInt(4, Integer.parseInt(request.getParameter("ship_quantity")));
					values = "(" + request.getParameter("ship_snum") + ", " + request.getParameter("ship_pnum") + ", " + request.getParameter("ship_jnum") + ", " + request.getParameter("ship_quantity") + ")";
					break;
				default:
					session.setAttribute("error", "no insert table specified");
					response.sendRedirect("/project4/errorpage.jsp");
					return;
			}
			List<Shipment> snumsList_above_100 = null;
			if (table == "shipments"){
				// Get existing snums, to know what shippments already exist
				Statement existing_shipments = data_entry_conn.createStatement();
				existing_shipments.execute("SELECT snum, pnum, jnum FROM shipments WHERE quantity >= 100");
				ResultSet rs = existing_shipments.getResultSet();
				snumsList_above_100 = new ArrayList<>();
				while (rs.next()) {
					String snum = rs.getString("snum");
					String pnum = rs.getString("pnum");
					String jnum = rs.getString("jnum");
					Shipment shipment = new Shipment(snum, pnum, jnum);
					snumsList_above_100.add(shipment);
				}
			}



			if (statement != null) {
				session.setAttribute("current_command", statement.toString());
				boolean isResultSet = statement.execute();
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
						session.setAttribute("inserted_values", values);
						if (updateCount == -1) {
								session.setAttribute("result_type", "update_count");
								session.setAttribute("result", updateCount);
						} else {
							session.setAttribute("result_type", "update_count");
							session.setAttribute("result", updateCount);
						}
				}

				if (table == "shipments" && snumsList_above_100 != null){
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


					PreparedStatement update_sup_Statement = data_entry_conn.prepareStatement(sql);
					update_sup_Statement.executeUpdate();
					int update_sup_count = update_sup_Statement.getUpdateCount();
					session.setAttribute("update_sup_count", update_sup_count);
				}
			}


		} catch (Exception e) {
			session.setAttribute("result_type", "error");
			session.setAttribute("result", e.getMessage());
			response.sendRedirect("/project4/dataEntryHome.jsp");
			return;
		}

        response.sendRedirect("/project4/dataEntryHome.jsp");
		return;


	}
}
