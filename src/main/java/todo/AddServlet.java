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

@WebServlet("/AddServlet")
public class AddServlet extends HttpServlet {
	private final CommonRepository repo = new CommonRepository();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		CommonProcess.getDefaultCode(request, response);
		Todo todo = CommonProcess.getParameter(request);
		boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		
		validationMethod(request, response, todo, isAjax);
		
		repo.insert(todo);
		
		responseMethod(request, response, isAjax);
	}
	
	
	
	public void validationMethod(HttpServletRequest request, HttpServletResponse response, Todo todo, boolean isAjax) throws IOException, ServletException {
		if (isAjax) {
			response.setContentType("application/json; charset=UTF-8");
			if( todo.getTask().isEmpty() && todo.getDate() == null) {
				response.getWriter().write("{\"nullTaskDate\": \"タスクと日付が未入力です\"}");
				return;
			} 
			if( todo.getTask().isEmpty()) {
				response.getWriter().write("{\"nullTask\": \"タスクが未入力です\"}");
				return;
			}
			if( todo.getDate() == null) {
				response.getWriter().write("{\"nullDate\": \"日付が未入力です\"}");
				return;
			}
		} else {
			request.setAttribute("msg", "入力に間違いがあります");
			request.setAttribute("todo", todo);
			request.getRequestDispatcher("add.jsp").forward(request, response);
			return;
		}
	}
	
	public void responseMethod(HttpServletRequest request, HttpServletResponse response, boolean isAjax) throws IOException, ServletException {
		if (isAjax) {
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