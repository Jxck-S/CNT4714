<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="java.util.Arrays" %>
<%
	boolean valid = session.getAttribute("logged_in") != null && session.getAttribute("user_type") != null;
	boolean good_login = false;
	String error = "User not logged in";
	if (valid) {
		boolean logged_in = session.getAttribute("logged_in") == "true" ? true : false;
		String user_type = (String) session.getAttribute("user_type");
		if (logged_in && user_type.equals("dataentryuser")) {
			good_login = true;
		} else if (!session.getAttribute("user_type").equals("dataentryuser")) {
			error = "User not data entry-level";
		}
	}
	if (!good_login) {
		session.setAttribute("error", error);
		response.sendRedirect("errorpage.jsp");
	}

%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Data Entry</title>
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
			<h3 class="level">Data Entry Application</h3>
	</div>

	<div class="grid-box">
		<div class="container no_padding">
			<form action="dataentryservlet/?t=suppliers" method="post" id="suppliers">
				<h2>Suppliers Record Insert</h2>
				<table>
					<tr>
						<th>snum</th>
						<th>sname</th>
						<th>status</th>
						<th>city</th>
					</tr>
					<tr>
					<td><input type="text" id="sup_snum" name="sup_snum" required></td>
					<td><input type="text" id="sup_sname" name="sup_sname" required></td>
					<td><input type="text" id="sup_status" name="sup_status" required></td>
					<td><input type="text" id="sup_city" name="sup_city" required></td>
					</tr>
				</table>

				<input type="submit" value="Enter Supplier Record Into Database">
				<input type="button" value="Clear Data and Results" onclick="resetFormById('suppliers')">

			</form>
		</div>

		<div class="container no_padding">
			<form action="dataentryservlet/?t=parts" method="post" id="parts">
				<h2>Parts Record Insert</h2>
				<table>
					<tr>
						<th>pnum</th>
						<th>pname</th>
						<th>color</th>
						<th>weight</th>
						<th>city</th>
					</tr>
					<tr>
					<td><input type="text" id="part_pnum" name="part_pnum" required></td>
					<td><input type="text" id="part_pname" name="part_pname" required></td>
					<td><input type="text" id="part_color" name="part_color" required></td>
					<td><input type="text" id="part_weight" name="part_weight" required></td>
					<td><input type="text" id="part_city" name="part_city" required></td>
					</tr>
				</table>

				<input type="submit" value="Enter Parts Record Into Database">
				<input type="button" value="Clear Data and Results" onclick="resetFormById('parts')">

			</form>
		</div>

		<div class="container no_padding">
			<form action="dataentryservlet/?t=jobs" method="post" id="jobs">
				<h2>Jobs Record Insert</h2>
				<table>
					<tr>
						<th>jnum</th>
						<th>jname</th>
						<th>numworkers</th>
						<th>city</th>
					</tr>
					<tr>
					<td><input type="text" id="job_jnum" name="job_jnum" required></td>
					<td><input type="text" id="job_jname" name="job_jname" required></td>
					<td><input type="text" id="job_numworkers" name="job_numworkers" required></td>
					<td><input type="text" id="job_city" name="job_city" required></td>
					</tr>
				</table>

				<input type="submit" value="Enter Jobs Record Into Database">
				<input type="button" value="Clear Data and Results" onclick="resetFormById('jobs')">

			</form>
		</div>

		<div class="container no_padding">
			<form action="dataentryservlet/?t=shipments" method="post" id="shipments">
				<h2>Shipments Record Insert</h2>
				<table>
					<tr>
						<th>snum</th>
						<th>pnum</th>
						<th>jnum</th>
						<th>quantity</th>
					</tr>
					<tr>
					<td><input type="text" id="ship_snum" name="ship_snum" required></td>
					<td><input type="text" id="ship_pnum" name="ship_pnum" required></td>
					<td><input type="text" id="ship_jnum" name="ship_jnum" required></td>
					<td><input type="text" id="ship_quantity" name="ship_quantity" required></td>
					</tr>
				</table>

				<input type="submit" value="Enter Shipments Record Into Database">
				<input type="button" value="Clear Data and Results" onclick="resetFormById('shipments')">
			</form>
		</div>
	</div>
	<div class="container">
		<p>** Results Window**</p>

		<div id="results_container">
			<% String resultType = (String) session.getAttribute("result_type");
			if (resultType != null) { %>

				<%-- <div>
					Result Type: <%= resultType %>
					Results: <%= session.getAttribute("result") %>
					Current Command: <%= session.getAttribute("current_command") %>
				</div> --%>

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
						<div>New <%= session.getAttribute("table") %> record: <%= session.getAttribute("inserted_values") %> - successfully inserted into database.</div>
				<%
						if (session.getAttribute("updated_sup_count") != null && (int)session.getAttribute("update_sup_count") > 0) {
                            %>
                                <div>Business Logic Triggered.</div>
                            <%
                        }
						%></div><%
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
