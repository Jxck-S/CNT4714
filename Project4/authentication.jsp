<!DOCTYPE html>
<html>
<head>
  <title>Login Page</title>
  <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
  <div>
    <header>
      <h4>Spring 2024 Project 4 Enterprise System</h4>
      <small>A Servlet/JSP-based Multi-tiered Enterprise Application Using A Tomcat Container</small>
    </header>
    <div class="label">User Authentication</div>
    <div class="container">
      <form action="/project4/authcheck" method="post">
        <h3>Login</h3>
        <label for="username">Username:</label>
        <input type="text" id="username" name="username"><br>
        <label for="password">Password:</label>
        <input type="password" id="password" name="password"><br>
        <input type="submit" value="Submit">
      </form>
    </div>
  </div>
</body>
</html>