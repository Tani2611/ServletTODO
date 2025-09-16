package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dto.Search;
import dto.Sort;
import dto.Todo;

public class TodoDao {
	String DRIVER = "org.postgresql.Driver";
	String URL = "jdbc:postgresql://localhost:5432/servlet_todo";
	String ID = "mizutani";
	String PS = "mizutani";
	List<Todo> todoList = new ArrayList<>(); //new TodoDao のたびに呼ばれる。

	public void insert(Todo todo) throws Exception {
		try (Connection con = DriverManager.getConnection(URL, ID, PS); // DB接続
				PreparedStatement st = con.prepareStatement("INSERT INTO todo(task, date) VALUES(?, ?)");) { // 指定DBにSQL文を投げる

			st.setString(1, todo.getTask());
			st.setDate(2, java.sql.Date.valueOf(todo.getDate())); //Date ↔ LocalDate
			st.executeUpdate();
		}
	}

	public void update(Todo todo) throws Exception {
		try (Connection con = DriverManager.getConnection(URL, ID, PS);
				PreparedStatement st = con.prepareStatement("UPDATE todo SET task = ?, date = ? WHERE todo.id = ? ");) {

			st.setString(1, todo.getTask());
			st.setDate(2, java.sql.Date.valueOf(todo.getDate())); //Date ↔ LocalDate
			st.setInt(3, todo.getId());
			st.executeUpdate();
		}
	}

	public void updateStatus(Todo todo) throws Exception {
		try (Connection con = DriverManager.getConnection(URL, ID, PS);
				PreparedStatement st = con.prepareStatement("UPDATE todo SET status = ? WHERE id = ? ")) {
			st.setBoolean(1, todo.isStatus());
			st.setInt(2, todo.getId()); //Date ↔ LocalDate
			st.executeUpdate(); //insert update delte用
		}
	}

	// TODO: DAOはDB操作のみにする　〇
	public int delete(Todo todo) throws Exception {
		try (Connection con = DriverManager.getConnection(URL, ID, PS);
				PreparedStatement st = con.prepareStatement("DELETE FROM todo WHERE id = ?");) {

			st.setInt(1, todo.getId());
			int count = st.executeUpdate(); //insert update delte用

			System.out.println("削除件数：" + count);
			return count;

		}
	}

	public Todo getTodo(Todo todo) throws Exception {
		try (Connection con = DriverManager.getConnection(URL, ID, PS);
				PreparedStatement st = con.prepareStatement("SELECT * FROM todo WHERE id = ?");) {

			st.setInt(1, todo.getId());

			try (ResultSet rs = st.executeQuery()) {
				return createTodo(rs);
			}
		}
	}

	public List<Todo> getTodoList(Sort sort) throws Exception {
		Class.forName(DRIVER);
		String safeSort = sortValidation(sort.sort());
		String safeOrder = orderValidation(sort.order());
		try (Connection con = DriverManager.getConnection(URL, ID, PS);
				PreparedStatement st = con.prepareStatement("SELECT * FROM todo ORDER BY" + " " + safeSort + " " + safeOrder + "," + "id" + " " + "DESC");
				ResultSet rs = st.executeQuery();) {

			return createTodoList(rs);
		}

	}

	public List<Todo> search(Search searchTodo, Sort sort) throws Exception {
		String task = null;
		Date startDate = null;
		Date endDate = null;
		String status = null;
		String safeSort = sortValidation(sort.sort());
		String safeOrder = orderValidation(sort.order());

		if (searchTodo.task() != null) {
			task = searchTodo.task();
		} else {
			task = "";
		}

		if (searchTodo.startDate() != null && !searchTodo.startDate().isEmpty()) {
			startDate = java.sql.Date.valueOf(searchTodo.startDate());
		}

		if (searchTodo.endDate() != null && !searchTodo.endDate().isEmpty()) {
			endDate = java.sql.Date.valueOf(searchTodo.endDate());
		}

		if (searchTodo.status() != null && !searchTodo.status().isEmpty()) {
			status = searchTodo.status();
		} else {
			status = "";
		}

		boolean betweenDay = startDate != null && endDate != null;
		boolean leftDay = startDate != null && endDate == null;
		boolean rightDay = startDate == null && endDate != null;

		boolean trueStatus = status.equals("trueStatus");
		boolean falseStatus = status.equals("falseStatus");
		boolean nullStatus = status.equals("nullStatus");

		StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM todo");
		List<String> sqlList = new ArrayList<>();
		List<Object> params = new ArrayList<>();

		if (!task.isEmpty()) {
			sqlList.add(" task LIKE ?");
			params.add("%" + task + "%");
		}

		if (leftDay) {
			sqlList.add(" date >= ?");
			params.add(startDate);
		} else if (rightDay) {
			sqlList.add(" date <= ?");
			params.add(endDate);
		} else if (betweenDay) {
			sqlList.add(" date >= ? AND date <= ?");
			params.add(startDate);
			params.add(endDate);
		}

		if (!nullStatus && !status.isEmpty()) {
			sqlList.add(" \"status\" = ?");
			if (trueStatus) {
				params.add(true);
			} else if (falseStatus) {
				params.add(false);
			}
		}

		if (!sqlList.isEmpty()) {
			sqlBuilder.append(" WHERE");
			sqlBuilder.append(String.join(" AND", sqlList));
		}

		sqlBuilder.append(" ORDER BY").append(" " + safeSort).append(" " + safeOrder);

		System.out.println("SQL = " + sqlBuilder);
		System.out.println("値 = " + params);

		try (Connection con = DriverManager.getConnection(URL, ID, PS);
				PreparedStatement st = con.prepareStatement(sqlBuilder.toString());) {

			for (int i = 0; i < params.size(); i++) {
				st.setObject(i + 1, params.get(i));
			}

			try (ResultSet rs = st.executeQuery()) {
				return createTodoList(rs);
			}

		}
	}

	public List<Todo> sort(Sort sort) throws Exception {
		String safeSort = sortValidation(sort.sort());
		String safeOrder = orderValidation(sort.order());
		try (Connection con = DriverManager.getConnection(URL, ID, PS);
				PreparedStatement st = con.prepareStatement("SELECT * FROM todo ORDER BY" + " " + safeSort + " " + safeOrder + "," + "id DESC");
				ResultSet rs = st.executeQuery();) {

				return createTodoList(rs);
			
		}
	}

	public Todo createTodo(ResultSet rs) throws SQLException {
		Todo todo = new Todo();
		while (rs.next()) {
			int id = rs.getInt("id");
			String task = rs.getString("task");
			LocalDate date = rs.getDate("date").toLocalDate(); // Date ↔ LocalDate
			boolean status = rs.getBoolean("status");
			todo = new Todo(id, task, date, status);
		}
		return todo;
	}

	public List<Todo> createTodoList(ResultSet rs) throws SQLException {
		List<Todo> todoList = new ArrayList<>(); 
		// ↑ これしないとダメな理由
		// → 各サーブレットでrepoインスタンスを作ると、2回目以降でDAOがnewされず、Listがリセットされないため、一覧の後ろに足していく形になってしまうのを回避するため。
		while (rs.next()) {
			int id = rs.getInt("id");
			String task = rs.getString("task");
			LocalDate date = rs.getDate("date").toLocalDate(); // Date ↔ LocalDate
			boolean status = rs.getBoolean("status");
			todoList.add(new Todo(id, task, date, status));
		}
		return todoList;
	}
	
	public String sortValidation(String sort) {
		return switch (sort) {
			case "task" -> "task";
			case "date" -> "date";
			case "status" -> "status";
			default -> "id";
		};
	}
	
	public String orderValidation(String order) {
		return order.equals("DESC") ? "DESC" : "ASC";
	}
}
