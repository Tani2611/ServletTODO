<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.*, dao.TodoDao, dto.Todo, dto.Search" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.time.LocalDate, java.time.LocalTime, java.time.LocalDateTime" %>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>TODO</title>
		<link rel="stylesheet" href="<%= request.getContextPath() %>/common/styles.css">
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" integrity="sha512-..." crossorigin="anonymous" />
	</head>
	<body>
		<header>
			<ul>
				<li><a href="<%= request.getContextPath() %>/TodoServlet">トップ</a></li>
				<li><a href="<%= request.getContextPath() %>/add.jsp">追加</a></li>
				<li><a href="<%= request.getContextPath() %>/index2.jsp">非同期</a></li>
			</ul>
			<% String msg = (String)request.getAttribute("msg"); %>
			<% if(msg != null) { %>
				<p class="msg"><%= msg %></p>
			<% } %>
			<p>sortSession：${sortSession}　　orderSession：${orderSession}</p>
		</header>
		<main>