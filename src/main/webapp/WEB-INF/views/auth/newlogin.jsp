<%@ page contentType="text/html;charset=UTF-8" %>
<html>
  <head>
    <title>Login</title>
    <!-- Bootstrap and custom CSS -->
    <link href="<%= request.getContextPath() %>/style/style.css" rel="stylesheet">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">

    <!-- Load jQuery before Bootstrap -->
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>

    <!-- Link to the external JS file -->
    <script>
      // Set the context path dynamically for the JS file to use
      const contextPath = "<%= request.getContextPath() %>";
    </script>
    <script src="<%= request.getContextPath() %>/js/newlogin.js" defer></script>
  </head>

  <body>
    <div class="container d-flex justify-content-center align-items-center" style="min-height: 100vh;">
      <div class="card p-4 shadow-sm" style="width: 100%; max-width: 400px;">
        <h2 class="text-center">Login</h2>
        <p id="error-message" class="text-danger text-center"></p>

        <!-- Toggle Login Method -->
        <div class="form-group text-center">
          <label for="login-method">Choose Login Method:</label>
          <select id="login-method" class="form-control">
            <option value="ajax">AJAX Login</option>
            <option value="traditional">Traditional Login</option>
          </select>
        </div>

        <!-- Login Form -->
        <form id="login-form" class="form-group">
          <!-- Remove the action and method here for AJAX flexibility -->
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
