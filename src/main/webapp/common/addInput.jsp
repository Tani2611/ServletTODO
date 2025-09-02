<%@ page contentType="text/html; charset=UTF-8" %>

<%-- Todo todo = (Todo)request.getAttribute("todo"); --%>

<h2>追加</h2>
<label for="task">タスク：</label>
<input id="task" type="text" name="task" value="${empty todo.getTask() ? '' : todo.getTask()}">　
<label for="date">期限：</label>
<input id="date" type="date" name="date" value="${empty todo.getDate() ? '' : todo.getDate()}">　
<button type="submit" >追加</button>