<%@ page contentType="text/html;charset=UTF-8" %>
<html>
  <head>
    <title>Login</title>
    <!-- Bootstrap and custom CSS -->
    <link href="<%= request.getContextPath() %>/style/style.css" rel="stylesheet">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">

    <!-- Link to the external JS file -->
    <script src="<%= request.getContextPath() %>/js/newlogin.js" defer></script>
  </head>
  <body>
    <div class="container d-flex justify-content-center align-items-center" style="min-height: 100vh;">
      <div class="card p-4 shadow-sm" style="width: 100%; max-width: 400px;">
        <h2 class="text-center">Login</h2>
        <p id="error-message" class="text-danger text-center"></p>

        <form id="login-form" class="form-group">
          <div class="form-group">
            <label for="username">Username:</label>
            <input type="text" id="username" name="username" class="form-control" required>
          </div>
          <div class="form-group">
            <label for="password">Password:</label>
            <input type="password" id="password" name="password" class="form-control" required>
          </div>
          <button type="submit" class="btn btn-primary btn-block">Login</button>
        </form>

        <p class="text-center mt-3">
          Don't have an account? <a href="<%= request.getContextPath() %>/auth/register">Register here</a>
        </p>
        <p class="text-center"><a href="<%= request.getContextPath() %>/home">Home</a></p>
      </div>
    </div>
  </body>
</html>