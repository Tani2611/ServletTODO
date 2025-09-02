package todo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.CommonProcess;

@WebServlet("/StatusServlet")
public class StatusServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CommonProcess.getDefaultCode(request, response);
		Todo todo = CommonProcess.getParameter(request);

		try {
			new TodoDao().updateStatus(todo);
		} catch (Exception e) {
			throw new ServletException(e);
		}

		if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
			System.out.println("StatusServlet 非同期");
		} else {
			System.out.println("StatusServlet 同期");
			response.sendRedirect(request.getContextPath() + "/TodoServlet");
		}

	}
}
