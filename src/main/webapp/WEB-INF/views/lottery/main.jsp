<%@ page import="java.util.LinkedList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Lottery Main</title>
        <link href="<%= request.getContextPath() %>/style/style.css" rel="stylesheet">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    </head>
    <body>
        <div class="container d-flex justify-content-center align-items-center" style="min-height: 100vh;">
            <div class="text-center card p-4 shadow-sm" style="width: 100%; max-width: 400px;">
                <header>
                    <h1 class="mb-4">Generate Your Winning Numbers!</h1>
                </header>
                <% LinkedList<String> errors = (LinkedList<String>) request.getAttribute("errors"); %>
                <% if (errors != null) { %>
                <div class="alert alert-danger">
                    <ul>
                        <% for (String error : errors) { %>
                        <li><%= error %></li>
                        <% } %>
                    </ul>
                </div>
                <% } %>
                <main>
                    <form action="${pageContext.request.contextPath}/lottery/generate" method="post" class="form-group">
                        <div class="form-group">
                            <label for="groups">Number of Groups:</label>
                            <input type="text" id="groups" name="groups" class="form-control" value="${param.groups}" required>
                        </div>
                        <div class="form-group">
                            <label for="excludes">Excludes:</label>
                            <input type="text" id="excludes" name="excludes" class="form-control" value="${param.excludes}">
                        </div>
                        <input type="submit" value="Submit" class="btn btn-primary btn-block">
                    </form>
                    <p class="mt-3"><a href="${pageContext.request.contextPath}/home" class="btn btn-secondary btn-block">Home</a></p>
                </main>
            </div>
        </div>
    </body>
</html>