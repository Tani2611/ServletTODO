<%@ page contentType="text/html; charset=UTF-8"%>

<% Search searchTodo = (Search)request.getAttribute("searchTodo"); %>

<h2>検索</h2>
<label for="searchTask">タスク：</label>
<input id="searchTask" type="text" name="searchTask" value="${empty searchTodo ? '' : searchTodo.task()}" ><br>
<label for="searchStartDate">期限：</label>
<input id="searchStartDate" type="date" name="searchStartDate" value="${empty searchTodo ? '' : searchTodo.startDate()}" > ～
<label for="searchEndDate"></label>
<input id="searchEndDate" type="date" name="searchEndDate" value="${empty searchTodo ? '' : searchTodo.endDate()}" ><br>
<input id="searchTrueStatus"  type="radio" name="searchStatus" value="trueStatus" ${not empty searchTodo and searchTodo.status() == 'trueStatus' ? 'checked' : ''}><label for="searchTrueStatus">完了済み</label>
<input id="searchFalseStatus" type="radio" name="searchStatus" value="falseStatus" ${not empty searchTodo and searchTodo.status() == 'falseStatus' ? 'checked' : ''}><label for="searchFalseStatus">未完了</label>
<input id="searchNullStatus" type="radio" name="searchStatus" value="nullStatus" ${not empty searchTodo and searchTodo.status() == 'nullStatus' or  searchTodo.status() == null ? 'checked' : ''}><label for="searchNullStatus">どちらも</label><br>
<button type="reset" >リセット</button>　
<button type="submit" >検索</button>