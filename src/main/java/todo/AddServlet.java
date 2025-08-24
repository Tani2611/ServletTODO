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

@WebServlet("/AddServlet")
public class AddServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CommonProcess.getDefaultCode(request, response);

		HttpSession session = request.getSession();
		String sortSession = (String) session.getAttribute("sortSession");
		String orderSession = (String) session.getAttribute("orderSession");

		Todo todo = CommonProcess.getParameter(request);

		if (request.getParameter("task").isEmpty() || request.getParameter("date").isEmpty()) {
			request.setAttribute("msg", "入力に間違いがあります");
			request.setAttribute("todo", todo);
			request.getRequestDispatcher("add.jsp").forward(request, response);
			return;
		}

		//		CommonProcess.getDaoMethod("insert", todo); うまく動く
		//		List<Todo> todoList = CommonProcess.getDaoTodoList("getTodoList", "sortSession", "orderSession"); 動かない

		List<Todo> todoList = new ArrayList<>();
		try {
			new TodoDao().insert(todo);
			todoList = new TodoDao().getTodoList(sortSession, orderSession);
		} catch (Exception e) {
			throw new ServletException(e);
		}

		if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
			System.out.println("AddServlet 非同期");
			CommonProcess.getJsonList(response, todoList);
		} else {
			System.out.println("AddServlet 同期");
			request.setAttribute("msg", "追加しました");
			request.getRequestDispatcher("TodoServlet").forward(request, response);
		}
	}

}
