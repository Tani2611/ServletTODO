package todo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.CommonProcess;

@WebServlet("/DeleteServlet")
public class DeleteServlet extends HttpServlet {
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CommonProcess.getDefaultCode(request, response);

		Todo todo = CommonProcess.getParameter(request);

		try {
			new TodoDao().delete(todo);
		} catch (Exception e) {
			throw new ServletException(e);
		}

		if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
			System.out.println("DeleteServlet 非同期");
		} else {
			System.out.println("DeleteServlet 同期");
			request.setAttribute("msg", "削除しました");
			request.getRequestDispatcher("TodoServlet").forward(request, response);
		}

	}

}
