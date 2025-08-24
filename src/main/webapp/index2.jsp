<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="common/head.jsp"%>


<form id="addForm">
	<%@ include file="common/addInput.jsp"%>
</form>

<form id="searchForm">
	<%@ include file="common/searchInput.jsp"%>
</form>

<form id="sortForm">
	<%@ include file="common/sortInput.jsp"%>
</form>

<table border="1" cellspacing="0">
	<%@ include file="common/thead.jsp"%>
	<tbody>
		<%-- 非同期todoList --%>
	</tbody>
</table>


<%@ include file="common/footer.jsp"%>