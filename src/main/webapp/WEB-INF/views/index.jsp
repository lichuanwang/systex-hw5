<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Home Page</title>
        <!-- Bootstrap and custom CSS -->
        <link href="<%= request.getContextPath() %>/style/style.css" rel="stylesheet">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
    </head>
    <body>
        <div class="container d-flex justify-content-center align-items-center" style="min-height: 100vh;">
            <div class="text-center card p-4 shadow-sm" style="width: 100%; max-width: 400px;">
                <h1 class="mb-4">Welcome to the Lottery Game</h1>
                <p><a href="${pageContext.request.contextPath}/lottery/index" class="btn btn-primary btn-block mb-3">Play Lottery</a></p>
                <p><a href="${pageContext.request.contextPath}/game/index" class="btn btn-primary btn-block mb-3">Play Guess Game</a></p>
                <!-- Logout Button -->
                <% if (session.getAttribute("user") != null) { %>
                <p><a href="<%= request.getContextPath() %>/auth/logout" class="btn btn-danger btn-block">Logout</a></p>
                <% } %>
            </div>
        </div>
    </body>
</html>