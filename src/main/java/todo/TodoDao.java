
package todo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TodoDao {
	String DRIVER = "org.postgresql.Driver";
	String URL = "jdbc:postgresql://localhost:5432/servlet_todo";
	String ID = "mizutani";
	String PS = "mizutani";

	List<Todo> todoList = new ArrayList<>();

	public void insert(Todo todo) throws Exception {
		Class.forName(DRIVER); // JDBCの初期化処理
		try (Connection con = DriverManager.getConnection(URL, ID, PS); // DB接続
				PreparedStatement st = con.prepareStatement("INSERT INTO todo(task, date) VALUES(?, ?)");) { // 指定DBにSQL文を投げる

			st.setString(1, todo.getTask());
			st.setDate(2, java.sql.Date.valueOf(todo.getDate())); //Date ↔ LocalDate
			st.executeUpdate();
		}
	}

	public void update(Todo todo) throws Exception {
		Class.forName(DRIVER);
		try (Connection con = DriverManager.getConnection(URL, ID, PS);
				PreparedStatement st = con.prepareStatement("UPDATE todo SET task = ?, date = ? WHERE todo.id = ? ");) {

			st.setString(1, todo.getTask());
			st.setDate(2, java.sql.Date.valueOf(todo.getDate())); //Date ↔ LocalDate
			st.setInt(3, todo.getId());
			st.executeUpdate();
		}
	}

	public void updateStatus(Todo todo) throws Exception {
		Class.forName(DRIVER);

		try (Connection con = DriverManager.getConnection(URL, ID, PS);
				PreparedStatement st = con.prepareStatement("UPDATE todo SET status = ? WHERE id = ? ")) {
			st.setBoolean(1, todo.isStatus());
			st.setInt(2, todo.getId()); //Date ↔ LocalDate
			st.executeUpdate(); //insert update delte用
		}

	}

	public void delete(Todo todo) throws Exception {
		Class.forName(DRIVER);

		try (Connection con = DriverManager.getConnection(URL, ID, PS);
				PreparedStatement st = con.prepareStatement("DELETE FROM todo WHERE id = ?");) {

			st.setInt(1, todo.getId());
			st.executeUpdate(); //insert update delte用
		}
	}

	public Todo getTodo(Todo todo) throws Exception {
		Class.forName(DRIVER);
		try (Connection con = DriverManager.getConnection(URL, ID, PS);
				PreparedStatement st = con.prepareStatement("SELECT * FROM todo WHERE id = ?");) {

			st.setInt(1, todo.getId());

			try (ResultSet rs = st.executeQuery()) { //SELECT用
				while (rs.next()) {
					int id = rs.getInt("id");
					String task = rs.getString("task");
					LocalDate date = rs.getDate("date").toLocalDate(); // Date ↔ LocalDate
					boolean status = rs.getBoolean("status");

					todo = new Todo(id, task, date, status);
				}
			}

		}
		return todo;

	}

	public List<Todo> getTodoList(String sort, String order) throws Exception {
		Class.forName(DRIVER);
		String sql = "SELECT * FROM todo ORDER BY" + " " + sort + " " + order;
		try (Connection con = DriverManager.getConnection(URL, ID, PS);
				PreparedStatement st = con.prepareStatement(sql);
				ResultSet rs = st.executeQuery();) {

			while (rs.next()) {
				int id = rs.getInt("id");
				String task = rs.getString("task");
				LocalDate date = rs.getDate("date").toLocalDate(); // Date ↔ LocalDate
				boolean status = rs.getBoolean("status");

				todoList.add(new Todo(id, task, date, status));
			}
		}
		return todoList;
	}

	public List<Todo> search(Search searchTodo, String sort, String order)
			throws Exception {
		Class.forName(DRIVER);

		String task = null;
		Date startDate = null;
		Date singleDate = null;
		Date endDate = null;
		String status = null;

		if (searchTodo.task() != null) {
			task = searchTodo.task();
		} else {
			task = "";
		}

		if (searchTodo.startDate() != null && !searchTodo.startDate().isEmpty()) {
			startDate = java.sql.Date.valueOf(searchTodo.startDate());
		}

		if (searchTodo.singleDate() != null && !searchTodo.singleDate().isEmpty()) {
			singleDate = java.sql.Date.valueOf(searchTodo.singleDate());
		}

		if (searchTodo.endDate() != null && !searchTodo.endDate().isEmpty()) {
			endDate = java.sql.Date.valueOf(searchTodo.endDate());
		}

		if (searchTodo.status() != null && !searchTodo.status().isEmpty()) {
			status = searchTodo.status();
		} else {
			status = "";
		}

		boolean isEmptyDay = startDate == null && singleDate == null && endDate == null;
		boolean oneDay = startDate == null && singleDate != null && endDate == null;
		boolean betweenDay = startDate != null && singleDate == null && endDate != null;
		boolean leftDay = startDate != null && singleDate == null && endDate == null;
		boolean rightDay = startDate == null && singleDate == null && endDate != null;

		boolean trueStatus = status.equals("trueStatus");
		boolean falseStatus = status.equals("falseStatus");

		StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM todo");
		List<String> sqlList = new ArrayList<>();
		List<Object> params = new ArrayList<>();

		if (!task.isEmpty()) {
			sqlList.add(" task LIKE ?");
			params.add("%" + task + "%");
		}

		if (oneDay) {
			sqlList.add(" date = ?");
			params.add(singleDate);
		} else if (leftDay) {
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

		if (!status.isEmpty()) {
			sqlList.add(" \"status\" = ?");
			if (trueStatus) {
				params.add(true);
			} else {
				params.add(false);
			}
		}

		if (!sqlList.isEmpty()) {
			sqlBuilder.append(" WHERE");
			sqlBuilder.append(String.join(" AND", sqlList));
		}

		sqlBuilder.append(" ORDER BY").append(" " + sort).append(" " + order);

		System.out.println("SQL = " + sqlBuilder);
		System.out.println("値 = " + params);

		try (Connection con = DriverManager.getConnection(URL, ID, PS);
				PreparedStatement st = con.prepareStatement(sqlBuilder.toString());) {

			for (int i = 0; i < params.size(); i++) {
				st.setObject(i + 1, params.get(i));
			}

			try (ResultSet rs = st.executeQuery()) {
				while (rs.next()) {
					int id = rs.getInt("id");
					String task1 = rs.getString("task");
					LocalDate date = rs.getDate("date").toLocalDate(); // Date ↔ LocalDate
					boolean status1 = rs.getBoolean("status");
					todoList.add(new Todo(id, task1, date, status1));
				}
			}
		}
		return todoList;
	}

	public List<Todo> sort(String sort, String order) throws Exception {
		Class.forName(DRIVER);
		String sql = "SELECT * FROM todo ORDER BY" + " " + sort + " " + order; //ここ！SQLインジェクションの回避

		try (Connection con = DriverManager.getConnection(URL, ID, PS);
				PreparedStatement st = con.prepareStatement(sql);
				ResultSet rs = st.executeQuery();) {

			while (rs.next()) {
				int id = rs.getInt("id");
				String task = rs.getString("task");
				LocalDate date = rs.getDate("date").toLocalDate(); // Date ↔ LocalDate
				boolean status = rs.getBoolean("status");
				todoList.add(new Todo(id, task, date, status));
			}
		}
		return todoList;
	}
}
