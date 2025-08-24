<%@ page contentType="text/html; charset=UTF-8"%>

<% Search searchTodo = (Search)request.getAttribute("searchTodo"); %>

<h2>検索</h2>

<input type="text" name="searchTask" value="${empty searchTodo ? '' : searchTodo.task()}" ><br>
<input id="searchStartDate" type="date" name="searchStartDate" value="${empty searchTodo ? '' : searchTodo.startDate()}" ><span id="leftArrow">　↔　</span>
<input id="searchSingleDate" type="date" name="searchSingleDate" value="${empty searchTodo ? '' : searchTodo.singleDate()}" ><span id="rightArrow">　↔　</span>
<input id="searchEndDate" type="date" name="searchEndDate" value="${empty searchTodo ? '' : searchTodo.endDate()}" ><br>
<input id="searchTrueStatus"  type="radio" name="searchRadioStatus" value="trueStatus" ${not empty searchTodo and searchTodo.status() == 'trueStatus' ? 'checked' : ''}><label for="searchTrueStatus">完了済み</label>
<input id="searchFalseStatus" type="radio" name="searchRadioStatus" value="falseStatus" ${not empty searchTodo and searchTodo.status() == 'falseStatus' ? 'checked' : ''}><label for="searchFalseStatus">未完了</label><br>
<button type="submit" >検索</button>