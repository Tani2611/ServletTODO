package todo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.CommonProcess;

@WebServlet("/AddServlet")
public class AddServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CommonProcess.getDefaultCode(request, response);
		Todo todo = CommonProcess.getParameter(request);

		if (request.getParameter("task").isEmpty() || request.getParameter("date").isEmpty()) {

			if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
				response.setContentType("application/json; charset=UTF-8");
				response.getWriter().write("{\"error\": \"入力に間違いがあります\"}");
				return;

			} else {

				request.setAttribute("msg", "入力に間違いがあります");
				request.setAttribute("todo", todo);
				request.getRequestDispatcher("add.jsp").forward(request, response);
				return;

			}
		}

		try {
			new TodoDao().insert(todo);
		} catch (Exception e) {
			throw new ServletException(e);
		}

		if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
			System.out.println("AddServlet 非同期");
			response.setContentType("application/json; charset=UTF-8");
			response.getWriter().write("{\"ok\": true}");
		} else {
			System.out.println("AddServlet 同期");
			request.setAttribute("msg", "追加しました");
			request.getRequestDispatcher("TodoServlet").forward(request, response);
		}
	}

}
