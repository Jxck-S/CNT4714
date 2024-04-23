
/* Name: Jack Sweeney
Course: CNT 4714 – Spring 2024 – Project Four
Assignment title: A Three-Tier Distributed Web-Based Application
Date: April 23, 2024
*/
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.PreparedStatement;

@SuppressWarnings("serial")

public class AuthCheck extends HttpServlet {
	@Override
	protected void doPost( HttpServletRequest request,
				HttpServletResponse response ) throws ServletException, IOException
	{

		HttpSession session = request.getSession();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		try {
			Properties authdb_props = new Properties();
			String file = getServletContext().getRealPath("/WEB-INF/lib/authdb.properties");
			authdb_props.load(new FileInputStream(file));
			String db_url = authdb_props.getProperty("URL");
			Class.forName(authdb_props.getProperty("Dname"));
			Connection auth_db_connection = DriverManager.getConnection(db_url, authdb_props.getProperty("username"), authdb_props.getProperty("password"));
			PreparedStatement statement = auth_db_connection.prepareStatement("SELECT * FROM usercredentials WHERE login_username = ? AND login_password = ?");
			statement.setString(1, username);
			statement.setString(2, password);
			statement.executeQuery();
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {

			session.setAttribute("username",	username);;
			session.setAttribute("logged_in", "true");
			if (username.equals("client")) {
				session.setAttribute("user_type", "client");
				response.sendRedirect("clientHome.jsp");
			}
			else if (username.equals("root")) {
				session.setAttribute("user_type", "root");
				response.sendRedirect("rootHome.jsp");
			} else if (username.equals("dataentryuser")) {
				session.setAttribute("user_type", "dataentryuser");
				response.sendRedirect("dataEntryHome.jsp");
			} else if (username.equals("theaccountant")) {
				session.setAttribute("user_type", "accountant");
				response.sendRedirect("accountantHome.jsp");
			}

			}
			else {
			throw new Exception("Invalid username or password");

			}
		} catch (Exception e) {
			StackTraceElement[] stackTrace = e.getStackTrace();
			String error = "";
			for (StackTraceElement element : stackTrace) {
			error += element.toString() + "<br>";
			}
			session.setAttribute("error", e.getMessage()+" "+error);
			response.sendRedirect("errorpage.jsp");
		return;
		}
	}
}

