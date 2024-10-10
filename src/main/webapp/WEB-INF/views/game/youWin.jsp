<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>You Win</title>
        <link href="<%= request.getContextPath() %>/style/style.css" rel="stylesheet">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    </head>
    <body>
        <div class="container d-flex justify-content-center align-items-center" style="min-height: 100vh;">
            <div class="text-center card p-4 shadow-sm" style="width: 100%; max-width: 400px;">
                <h1 class="mb-4">Congratulations! You Won</h1>
                <p><a href="${pageContext.request.contextPath}/game/index" class="btn btn-primary btn-block mb-3">Play Again</a></p>
                <p><a href="${pageContext.request.contextPath}/home" class="btn btn-secondary btn-block">Home</a></p>
            </div>
        </div>
    </body>
</html>