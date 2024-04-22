<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.ResultSet" %>

<%
    boolean valid = session.getAttribute("logged_in") != null && session.getAttribute("user_type") != null;
    boolean good_login = false;
    String error = "User not logged in";
    if (valid) {
        boolean logged_in = session.getAttribute("logged_in") == "true" ? true : false;
        String user_type = (String) session.getAttribute("user_type");
        if (logged_in && user_type.equals("accountant")) {
            good_login = true;
        } else if (!session.getAttribute("user_type").equals("accountant")) {
            error = "User not accountant level";
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
    <title>Accountant Home</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
    <link rel="stylesheet" type="text/css" href="table.css">
    <script src="resetTools.js"></script>
</head>
<body>
    <header>
        <h4>Spring 2024 Project 4 Enterprise System</h4>
        <small>A Servlet/JSP-based Multi-tiered Enterprise Application Using A Tomcat Container</small>
    </header>
    <div>
        <div class="container">
            <p>CNT 4714 Spring 2024 - Enterprise System</p>
            <p class="info">
                You are connected to the Project 4 Enterprise System database as an accountant-level user.
            </p>
            <h3 class="level">Accountant Level</h3>
        </div>
        <div class="container">
            <h4>Available Operations: </h4>
            <div class="operations">
                <form action="accountantservlet" method="post">
                    <div class="operation">
                        <input type="radio" name="operation" value="max_status" id="max_status">
                        <label for="max_status">Get The Maximum Status Value Of All Suppliers</label>
                    </div>
                    <div class="operation">
                        <input type="radio" name="operation" value="total_weight" id="total_weight">
                        <label for="total_weight">Get The Total Weight Of All Parts</label>
                    </div>
                    <div class="operation">
                        <input type="radio" name="operation" value="total_shipments" id="total_shipments">
                        <label for="total_shipments">Get The Total Number of Shipments</label>
                    </div>
                    <div class="operation">
                        <input type="radio" name="operation" value="most_workers" id="most_workers">
                        <label for="most_workers">Get The Name And Number Of Workers Of The Job With The Most Workers</label>
                    </div>
                    <div class="operation">
                        <input type="radio" name="operation" value="supplier_names" id="supplier_names">
                        <label for="supplier_names">List The Name And Status Of Every Supplier</label>
                    </div>
                    <input type="submit" value="Execute Command">
                    <input type="button" value="Reset Results" onclick="resetResults()">
                </form>
            </div>
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
                            <p class="result_update">Statement executed successfully. <br><%= updatedRows %> row(s) affected.</p>
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
