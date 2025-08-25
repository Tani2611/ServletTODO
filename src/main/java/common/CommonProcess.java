package common;

import java.io.IOException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import todo.Todo;
import todo.TodoDao;

public class CommonProcess {

	public static void getDefaultCode(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8"); //リクエストの型を指定
		response.setContentType("text/html; charset=UTF-8"); //レスポンスの型を指定
	}

	public static Todo getParameter(HttpServletRequest request) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		String id = request.getParameter("id");
		String task = request.getParameter("task");
		String date = request.getParameter("date");
		String status = request.getParameter("status");

		Todo todo = new Todo();

		if (id != null && !id.isEmpty()) {
			todo.setId(Integer.parseInt(request.getParameter("id")));
		} else {
			todo.setId(null);
		}

		if (task != null && !task.isEmpty()) {
			todo.setTask(request.getParameter("task"));
		} else {
			todo.setTask("");
		}

		if (date != null && !date.isEmpty()) {
			todo.setDate(LocalDate.parse(request.getParameter("date")));
		} else {
			todo.setDate(null);
		}

		if (status != null && status.equals("true")) {
			todo.setStatus(true);
		} else {
			todo.setStatus(false);
		}

		return todo;
	}

	public static void getJsonList(HttpServletResponse response, List<Todo> todoList) throws ServletException, IOException {
		Gson gson = new GsonBuilder()// LocalDateを整形
				.registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, ctx) -> new JsonPrimitive(src.toString()))
				.create();

		String json = gson.toJson(todoList); //todoListをjsonにする処理。日付が出てきたら、gsonの形式に
		response.getWriter().write(json);//レスポンスにjsonの文字列を書いて格納
	}

	//DAOの共通。微妙？ーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーー
	public static void getDaoMethod(String methodName, Object param) throws ServletException {
		try {
			TodoDao dao = new TodoDao();
			Method method = dao.getClass().getMethod(methodName, param.getClass());
			method.invoke(dao, param);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	//まだダメ
	//	public static List<Todo> getDaoTodoList(String methodName, String sort, String order) throws ServletException {
	//		try {
	//			TodoDao dao = new TodoDao();
	//			Method method = dao.getClass().getMethod(methodName, String.class, String.class);
	//
	//			@SuppressWarnings("unchecked")
	//			List<Todo> todoList = (List<Todo>) method.invoke(dao, new Object[] { sort, order });
	//			return todoList;
	//		} catch (Exception e) {
	//			throw new ServletException(e);
	//		}
	//	}

}
