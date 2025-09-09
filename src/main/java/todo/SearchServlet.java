package todo;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.CommonProcess;
import dao.TodoDao;
import dto.Search;
import dto.Sort;
import dto.Todo;

@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CommonProcess.getDefaultCode(request, response);
		Sort sort = CommonProcess.getSort(request);

		Search searchTodo = new Search(
				request.getParameter("searchTask"),
				request.getParameter("searchStartDate"),
				request.getParameter("searchEndDate"),
				request.getParameter("searchStatus"));

		List<Todo> todoList = null;
		try {
			todoList = new TodoDao().search(searchTodo, sort);
		} catch (Exception e) {
			throw new ServletException(e);
		}
		if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
			System.out.println("SearchServlet 非同期");
			CommonProcess.getJsonList(response, todoList);
		} else {
			System.out.println("SearchServlet 同期");
			request.setAttribute("todoList", todoList);
			request.setAttribute("searchTodo", searchTodo);
			request.getRequestDispatcher("index.jsp").forward(request, response);
		}

	}
}
