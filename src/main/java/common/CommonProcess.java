package common;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import dto.Sort;
import dto.Todo;

public class CommonProcess {

	public static void getDefaultCode(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8"); //リクエストの型を指定
		response.setContentType("text/html; charset=UTF-8"); //レスポンスの型を指定
	}

	public static Todo getParameter(HttpServletRequest request) throws ServletException, IOException {
		//		request.setCharacterEncoding("UTF-8");

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

	public static Sort getSort(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String sortSession = request.getParameter("searchSort") != null
				? request.getParameter("searchSort")
				: (String) session.getAttribute("sortSession");
		String orderSession = request.getParameter("searchOrder") != null
				? request.getParameter("searchOrder")
				: (String) session.getAttribute("orderSession");

		if (sortSession == null || sortSession.isEmpty()) {
			sortSession = "id";
		}
		if (orderSession == null || orderSession.isEmpty()) {
			orderSession = "DESC";
		}

		Sort sort = new Sort(sortSession, orderSession);

		session.setAttribute("sortSession", sort.sort());
		session.setAttribute("orderSession", sort.order());
		return sort;
	}

	public static void getJsonList(HttpServletResponse response, List<Todo> todoList)
			throws ServletException, IOException {
		Gson gson = new GsonBuilder()// LocalDateを整形
				.registerTypeAdapter(LocalDate.class,
						(JsonSerializer<LocalDate>) (src, typeOfSrc, ctx) -> new JsonPrimitive(src.toString()))
				.create();

		String json = gson.toJson(todoList); //todoListをjsonにする処理。日付が出てきたら、gsonの形式に
		response.getWriter().write(json);//レスポンスにjsonの文字列を書いて格納
	}

}
