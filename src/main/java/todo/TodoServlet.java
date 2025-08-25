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

@WebServlet("/TodoServlet")
public class TodoServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CommonProcess.getDefaultCode(request, response);

		HttpSession session = request.getSession();
		String sortSession = (String) session.getAttribute("sortSession");
		String orderSession = (String) session.getAttribute("orderSession");

		if (sortSession == null || sortSession.isEmpty()) {
			sortSession = "id";
		}
		if (orderSession == null || orderSession.isEmpty()) {
			orderSession = "DESC";
		}
		
		session.setAttribute("sortSession", sortSession);
		session.setAttribute("orderSession", orderSession);

		String msg = (String) request.getAttribute("msg");

		List<Todo> todoList = new ArrayList<>();
		try {
			todoList = new TodoDao().getTodoList(sortSession, orderSession);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("TodoServlet !!!!!!!!!!!");

		if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
			System.out.println("TodoServlet 非同期");
			CommonProcess.getJsonList(response, todoList);
		} else {
			System.out.println("TodoServlet 同期");
			request.setAttribute("todoList", todoList);
			request.setAttribute("msg", msg);
			request.getRequestDispatcher("index.jsp").forward(request, response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}