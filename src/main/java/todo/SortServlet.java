package todo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.CommonProcess;

@WebServlet("/SortServlet")
public class SortServlet extends HttpServlet {
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CommonProcess.getDefaultCode(request, response);
		HttpSession session = request.getSession();
		String searchSort = request.getParameter("searchSort");
		String searchOrder = request.getParameter("searchOrder");

		if (searchSort == null || searchSort.isEmpty()) {
			searchSort = "id";
		}
		if (searchOrder == null || searchOrder.isEmpty()) {
			searchOrder = "DESC";
		}

		session.setAttribute("sortSession", searchSort);
		session.setAttribute("orderSession", searchOrder);

		List<Todo> todoList = new ArrayList<>();
		try {
			todoList = new TodoDao().sort(searchSort, searchOrder);
		} catch (Exception e) {
			throw new ServletException(e);
		}

		if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
			System.out.println("SortServlet 非同期");
			CommonProcess.getJsonList(response, todoList);
		} else {
			System.out.println("SortServlet 同期");
			request.setAttribute("todoList", todoList);
			request.getRequestDispatcher("index.jsp").forward(request, response);
		}

	}
}
