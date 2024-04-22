<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.ResultSet" %>
<%
    boolean valid = session.getAttribute("logged_in") != null && session.getAttribute("user_type") != null;
    boolean good_login = false;
    String error = "User not logged in";
    if (valid) {
        boolean logged_in = session.getAttribute("logged_in") == "true" ? true : false;
        String user_type = (String) session.getAttribute("user_type");
        if (logged_in && user_type.equals("root")) {
            good_login = true;
        } else if (!session.getAttribute("user_type").equals("root")) {
            error = "User not root-level";
        }
    }
    if (!good_login) {
        session.setAttribute("error", error);
        response.sendRedirect("errorpage.jsp");
    }

%>
<!DOCTYPE html>
<html>
<head>
<title>Root Home</title>
<link rel="stylesheet" href="styles.css">
<link rel="stylesheet" href="table.css">
<script src="resetTools.js"></script>
</head>
<body>
    <header>
        <h4>Spring 2024 Project 4 Enterprise System</h4>
        <small>A Servlet/JSP-based Multi-tiered Enterprise Application Using A Tomcat Container</small>
    </header>
<div>
    <div class="container">
        <p>Logged in as a root-level user.</p>
        <h3 class="level">Root Level</h3>
    </div>
    <div class="container">
        <p>Please enter any SQL query or update command in the box below.</p>

        <form method="post" action="rootservlet">
            <% String currentCommand = (String) session.getAttribute("current_command");
            if (currentCommand == null) {
                currentCommand = "";
            }
            %>
            <textarea id="commands" name="commands" rows="20" cols="100" ><%= currentCommand %></textarea>
            <br>
            <input type="submit" value="Execute Command">
            <input type="button" value="Reset Form" onclick="resetForm()">
            <input type="button" value="Reset Results" onclick="resetResults()">
        </form>
    </div>


    <div class="container">
        <p>** Results Window**</p>
        <div id="results_container">
            <% String resultType = (String) session.getAttribute("result_type");
            if (resultType != null) { %>
                <%-- DEBUG --%>
                <%-- <div>
                    Result Type: <%= resultType %>
                    Results: <%= session.getAttribute("result") %>
                </div>
                <% --%>
                <%

                    if (resultType.equals("resultset")) {
                        ResultSet resultSet = (ResultSet) session.getAttribute("result");
                        String[] columnNames = (String[]) session.getAttribute("column_names");
                %>
                        <table>
                            <tr>
                                <% for (String columnName : columnNames) { %>
                                    <th><%= columnName %></th>
                                <% } %>
                            </tr>
                            <% while (resultSet.next()) { %>
                                <tr>
                                    <% for (String columnName : columnNames) { %>
                                        <td><%= resultSet.getString(columnName) %></td>
                                    <% } %>
                                </tr>
                            <% } %>
                        </table>
                <%
                    } else if (resultType.equals("update_count")) {
                        int updatedRows = (int) session.getAttribute("result");
                %>
                        <div class="result_update">
                        <p>Statement executed successfully. <br><%= updatedRows %> row(s) affected.</p>

                <%
                        if ((int)session.getAttribute("update_sup_count") > 0) {
                            %>
                                <div>Business Logic Detected! - Updating Supplier Status</div>
                                <div>Logic updated <%= session.getAttribute("update_sup_count") %> supplier mark(s).</div>

                            <%
                        }
                        else {
                            %>
                                <div>Business Logic Not Triggered</div>
                            <%

                        }
                    %></div>
                <%
                    }
                    else if (resultType.equals("error")) {
                        String errorMessage = (String) session.getAttribute("result");
                    %>
                        <div class="result_error">Error executing the SQL statement:<br> <div class="code_style"><%= errorMessage %></div></div>
                    <%
                    }
                %>
            <% } %>

        </div>
    </div>
</div>

</body>
</html>