<%@ page import="test.msg.model.User" %><%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/4/13 0013
  Time: 10:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>留言添加</title>
    <%
        User loginUser = (User)session.getAttribute("loginUser");
    %>
</head>

<body>
<jsp:include page="inc.jsp">
    <jsp:param name="op" value="添加"/>
</jsp:include>
<form action="add.jsp" method="post">
    <input type="hidden" name="userId" value="<%=loginUser.getId()%>">
    <table width="800" align="center" border="1">
        <tr>
            <td>标题</td><td><input type="text" name="title" size="100"></td>
        </tr>
        <tr>
            <td colspan="2">内容</td></td>
        </tr>
        <tr>
            <td colspan="2">
                <textarea rows="20" cols="120" name="content"></textarea>
            </td>
        </tr>
        <tr>
            <td colspan="2" align="center">
                <input type="submit" name="添加留言"/>
            </td>
        </tr>
    </table>
</form>
</body>
</html>
