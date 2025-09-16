package todo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.CommonProcess;
import common.CommonRepository;
import dto.Search;
import dto.Sort;
import dto.Todo;

@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
	private final CommonRepository repo = new CommonRepository();
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CommonProcess.getDefaultCode(request, response);
		Sort sort = CommonProcess.getSort(request);
		boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

		Search searchTodo = new Search(
				request.getParameter("searchTask"),
				request.getParameter("searchStartDate"),
				request.getParameter("searchEndDate"),
				request.getParameter("searchStatus"));

		List<Todo> todoList = new ArrayList<>();
		todoList = repo.search(searchTodo, sort);
		
		if (isAjax) {
			CommonProcess.getJsonList(response, todoList);
		} else {
			request.setAttribute("searchTodo", searchTodo);
			request.setAttribute("todoList", todoList);
			request.getRequestDispatcher("index.jsp").forward(request, response);
		}

	}
}
