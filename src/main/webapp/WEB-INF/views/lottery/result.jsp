<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title>Lottery Result</title>
        <link rel="stylesheet" href="<%= request.getContextPath() %>/style/style.css">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    </head>
    <body>
        <div class="container d-flex justify-content-center align-items-center" style="min-height: 100vh;">
            <div class="text-center card p-4 shadow-sm" style="width: 100%; max-width: 500px;">
                <header>
                    <h1 class="mb-4">Your Winning Lottery Numbers!</h1>
                </header>
                <main>
                    <table class="table table-bordered table-striped">
                        <tbody>
                            <%
                                ArrayList<String> lotterySets = (ArrayList<String>) request.getAttribute("lotteryNumber");
                                int count = 0;
                                for (String lotterySet : lotterySets) {
                                    count++;
                            %>
                            <tr>
                                <td>Lottery Set <%= count %>:</td>
                                <td><%= lotterySet %></td>
                            </tr>
                            <%}%>
                        </tbody>
                    </table>
                    <p><a href="${pageContext.request.contextPath}/lottery/index" class="btn btn-success btn-block mb-3">Generate More!</a></p>
                    <p><a href="${pageContext.request.contextPath}/home" class="btn btn-secondary btn-block">Home</a></p>
                </main>
            </div>
        </div>
    </body>
</html>