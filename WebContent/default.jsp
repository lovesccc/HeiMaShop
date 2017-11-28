<%--
  Created by IntelliJ IDEA.
  User: 78455
  Date: 2017/11/27
  Time: 23:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%
    response.sendRedirect(request.getContextPath()+"/indexServlet");
%>
</body>
</html>
