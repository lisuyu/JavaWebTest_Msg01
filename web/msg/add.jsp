<%@ page import="test.msg.dao.DAOFactory" %>
<%@ page import="test.msg.dao.IMessageDao" %>
<%@ page import="test.msg.model.Message" %>
<%@ page import="java.io.IOException" %><%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/4/13 0013
  Time: 11:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    try {
        IMessageDao msgDao = DAOFactory.getMessageDao();
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        int userId = Integer.parseInt(request.getParameter("userId"));
        Message msg = new Message();
        msg.setTitle(title);
        msg.setContent(content);
        msgDao.add(msg,userId);
        response.sendRedirect(request.getContextPath()+"/msg/list.jsp");
    } catch (NumberFormatException e) {
%>
    <h2 style="color:rebeccapurple"><%=e.getMessage()%></h2>
<%
        e.printStackTrace();
    } catch (IOException e) {
%>
<h2 style="color:rebeccapurple"><%=e.getMessage()%></h2>
<%
        e.printStackTrace();
    }
%>
