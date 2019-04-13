<%@ page import="test.msg.dao.IMessageDao" %>
<%@ page import="test.msg.dao.DAOFactory" %>
<%@ page import="test.msg.model.Message" %>
<%@ page import="test.msg.model.Pager" %>
<%@ page import="test.msg.dao.IUserDao" %>
<%@ page import="test.msg.dao.ICommentDao" %><%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/4/13 0013
  Time: 9:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <%
        IMessageDao messageDao = DAOFactory.getMessageDao();
        IUserDao userDao = DAOFactory.getUserDao();
        ICommentDao commentDao = DAOFactory.getCommentDao();
        Pager<Message> pages = messageDao.list();
        System.out.println("@@@###"+pages+"###@@@");
        int totalRecord = pages.getTotalPages();
    %>
</head>
<body>
<jsp:include page="inc.jsp">
    <jsp:param name="op" value="列表"/>
</jsp:include>
<table align="center" width="900" border="1">
    <tr>
        <td>标题</td><td>发布时间</td><td>发布人</td><td>评论数量</td>
    </tr>
    <%
        for (Message message:pages.getDates()){
    %>
    <tr>
        <td><%=message.getTitle()%></td>
        <td><%=message.getPostDate()%></td>
        <td><%=userDao.load(message.getUserId()).getUsername()%></td>
        <td><%=commentDao.list(message.getId())%>></td>
    </tr>
    <%
        }
    %>
    <tr>
        <td colspan="4">
            <jsp:include page="../inc/pager.jsp">
                <jsp:param value="<%=totalRecord%>" name="items"/>
            </jsp:include>
        </td>
    </tr>
</table>
</body>
</html>
