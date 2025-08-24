<%@ page contentType="text/html; charset=UTF-8"%>

<h2>並び替え</h2>
<select name="searchSort">
	 <option value="id" ${sortSession == 'id' || sortSession == null ? 'selected' : ''}>追加日</option>
	 <option value="task" ${sortSession == 'task' ? 'selected' : ''}>タスク</option>
	 <option value="date" ${sortSession == 'date' ? 'selected' : ''}>日付</option>
	 <option value="status" ${sortSession == 'status' ? 'selected' : ''}>状態</option>
</select>
<input id="asc"  type="radio" name="searchOrder" value="asc"  ${orderSession == 'ASC' or orderSession == 'asc' ? 'checked' : ''}><label for="asc">昇順</label>
<input id="desc" type="radio" name="searchOrder" value="desc" ${(orderSession == 'DESC' or orderSession == 'desc' or empty orderSession) ? 'checked' : ''}><label for="desc">降順</label><br>

<button type="submit">並び替え</button>