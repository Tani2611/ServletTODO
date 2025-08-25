package todo;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.CommonProcess;

@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CommonProcess.getDefaultCode(request, response);
		HttpSession session = request.getSession();
		String sortSession = (String) session.getAttribute("sortSession");
		String orderSession = (String) session.getAttribute("orderSession");

		Search searchTodo = new Search(
				request.getParameter("searchTask"),
				request.getParameter("searchStartDate"),
				request.getParameter("searchSingleDate"),
				request.getParameter("searchEndDate"),
				request.getParameter("searchRadioStatus"));
		
		
		

		if (!searchTodo.startDate().isEmpty() && !searchTodo.singleDate().isEmpty() && !searchTodo.endDate().isEmpty()) {
			if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
				response.setContentType("application/json; charset=UTF-8");
				response.getWriter().write("{\"error\": \"入力に間違いがあります\"}");
				return;
			}else {
				request.setAttribute("searchTodo", searchTodo);
				request.setAttribute("msg", "入力に間違いがあります");
				request.getRequestDispatcher("TodoServlet").forward(request, response);
				return;
			}
		}

		List<Todo> todoList = null;
		try {
			todoList = new TodoDao().search(searchTodo, sortSession, orderSession);
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
