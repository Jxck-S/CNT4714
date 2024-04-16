<!DOCTYPE html>
<html>
<head>
  <title>Login Page</title>
  <link rel="stylesheet" href="styles.css">
</head>
<body>
  <div class="container">
    <h1>CNT 4714 Spring 2024 - Enterprise System</h1>
    <p class="error">Authentication Error</p>
    <p>"Username and/or Password Not Recognized"</p>
    <p>Access To System Is Denied</p>
    <p>Please Try Again Later</p>
    <%
      String error = (String) session.getAttribute("error");
      if (error != null) {
    %>
        <p class="error"><%= error %></p>
    <% } %>
    <a href="authentication.jsp">Return to Login Page</a>
  </div>
</body>
</html>