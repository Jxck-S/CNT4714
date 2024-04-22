<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
    // Check if user is logged in

    boolean valid = session.getAttribute("logged_in") != null && session.getAttribute("user_type") != null;
    boolean good_login = false;
    if (valid) {
        boolean logged_in = session.getAttribute("logged_in") == "true" ? true : false;
        String user_type = (String) session.getAttribute("user_type");
        if (logged_in) {
            good_login = true;
        }
    }
    if (!good_login) {
        session.setAttribute("error", "User not logged in");
        response.sendRedirect("errorpage.jsp");
    }

%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="styles.css">
    <title>Project 4 Home</title>
</head>
<body>
    <header>
        <h4>Spring 2024 Project 4 Enterprise System</h4>
        <small>A Servlet/JSP-based Multi-tiered Enterprise Application Using A Tomcat Container</small>
    </header>
    <%
        String go_to = null;
        String user_type = (String) session.getAttribute("user_type");
        if (user_type != null) {
            switch (user_type) {
                case "client":
                    go_to = "clientHome.jsp";
                    break;
                case "root":
                    go_to = "rootHome.jsp";
                    break;
                case "dataentryuser":
                    go_to = "dataEntryHome.jsp";
                    break;
                case "accountant":
                    go_to = "accountantHome.jsp";
                    break;
                default:
                    // Handle unknown user types
                    session.setAttribute("error", "Unknown user type");
                    go_to = "errorpage.jsp";
                    break;
            }
        }



        %>
            <div class="container">
                <h1 class="label">Project 4 Home</h1>
                <h4 class="user_type">Logged in as: <%= user_type %> user type</h4>
                <a href="<%= go_to %>">Go to <%= user_type %> home</a>
                <br>
                <a href="/project4/logout">Logout</a>
            </div>

        <%


    %>
</body>
</html>