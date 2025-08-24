<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="common/head.jsp"%>

<form action="SearchServlet" method="post">
	<%@ include file="common/searchInput.jsp"%>
</form>

<form action="SortServlet" method="post">
	<%@ include file="common/sortInput.jsp"%>
</form>


<% List<Todo> todoList = (List<Todo>) request.getAttribute("todoList"); %>
<table border="1" cellspacing="0">
	<%@ include file="common/thead.jsp"%>
	
	<% for (Todo t : todoList) { %>
	<tbody>
			<tr>
				<td>
					<form action="StatusServlet" method="post">
							<input type="hidden" name="id" value="<%=t.getId()%>">
							<input type="checkbox" name="status" value="true" <%=t.isStatus() ? "checked" : ""%> onchange="this.form.submit()">
					</form>
				</td>
				<td><%= t.getId() %></td>
				<td><a href="<%=request.getContextPath()%>/EditServlet?id=<%=t.getId()%>"><%=t.getTask()%></a></td>
				<td class="date"><%=t.getDate()%></td>
				<td>
					<form action="DeleteServlet" method="post">
						<input type="hidden" name="id" value="<%=t.getId()%>">
					    <button onclick="return confirm('削除しますか？');"><i class="far fa-trash-alt fa-1x icon"></i></button>
					</form>
				</td>
			</tr>
	</tbody>
	<%}%>
</table>



<%@ include file="common/footer.jsp"%>

