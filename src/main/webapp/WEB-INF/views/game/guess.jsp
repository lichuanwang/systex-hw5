<%@ page import="java.util.LinkedList" %>
<html>
    <head>
        <title>Guess Game</title>
        <link href="<%= request.getContextPath() %>/style/style.css" rel="stylesheet">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    </head>
    <body>
        <div class="container d-flex justify-content-center align-items-center" style="min-height: 100vh;">
            <div class="text-center card p-4 shadow-sm" style="width: 100%; max-width: 400px;">
                <header>
                    <h1 class="mb-4">Start Guessing</h1>
                </header>
                <% LinkedList<String> errors = (LinkedList<String>) request.getAttribute("errors"); %>
                <% if (errors != null) { %>
                <div>
                    <ul>
                        <% for (String error : errors) { %>
                        <li><%= error %></li>
                        <% } %>
                    </ul>
                </div>
                <% } %>
                <main>
                    <p>${message}</p>
                    <form action="${pageContext.request.contextPath}/game/play" method="post" class="form-group">
                        <label for="guess">Guess a number between 1 and 10:</label>
                        <input type="text" id="guess" name="guess" class="form-control" value="${param.guess}" required>
                        <input type="submit" value="Submit" class="btn btn-primary btn-block mt-3">
                    </form>
                    <p class="mt-3"><a href="${pageContext.request.contextPath}/home" class="btn btn-secondary btn-block">Home</a></p>
                </main>
            </div>
        </div>
    </body>
</html>