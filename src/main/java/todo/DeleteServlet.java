package todo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.CommonProcess;
import common.CommonRepository;
import dto.Todo;

@WebServlet("/DeleteServlet")
public class DeleteServlet extends HttpServlet {
	private final CommonRepository repo = new CommonRepository();
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CommonProcess.getDefaultCode(request, response);
		Todo todo = CommonProcess.getParameter(request);

		boolean isDelete = repo.delete(todo);

		if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
			System.out.println("DeleteServlet 非同期");
			response.setContentType("application/json; charset=UTF-8");
			if(!isDelete) {
				response.getWriter().write("{\"error\": \"そのタスクは見つかりません\"}");				
				return;
			}
			response.getWriter().write("{\"true\": \"削除完了\"}");				
		} else {
			System.out.println("DeleteServlet 同期");
			request.setAttribute("msg", "削除しました");
			request.getRequestDispatcher("TodoServlet").forward(request, response);
		}

	}

}
