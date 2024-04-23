/* Name: Jack Sweeney
Course: CNT 4714 – Spring 2024 – Project Four
Assignment title: A Three-Tier Distributed Web-Based Application
Date: April 23, 2024
*/
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import javax.naming.Context;
import javax.naming.InitialContext;

@SuppressWarnings("serial")

public class LogoutServlet extends HttpServlet {
	@Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response )
    throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        session.invalidate();
        response.sendRedirect("/project4/authentication.jsp");
      }
}
