<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="common/head.jsp" %>


<% Todo todo = (Todo)request.getAttribute("todo"); %>

<h2>編集</h2>
<form action="EditServlet" method="post">
	<input type="hidden" name="id" value="<%= todo.getId() %>">
	<input type="text" name="task" value="<%= todo.getTask() %>">
	<input type="date" name="date" value="<%= todo.getDate() %>">
	<input type="submit" value="編集">
</form>


<%@ include file="common/footer.jsp" %>