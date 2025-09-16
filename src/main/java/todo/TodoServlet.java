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
import dto.Sort;
import dto.Todo;

@WebServlet("/TodoServlet")
public class TodoServlet extends HttpServlet {
	private final CommonRepository repo = new CommonRepository();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CommonProcess.getDefaultCode(request, response);	
		Sort sort = CommonProcess.getSort(request);
		String msg = (String) request.getAttribute("msg");
		boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		
		List<Todo> todoList = new ArrayList<>();
		todoList = repo.getTodoList(sort);

		if (isAjax) {
			CommonProcess.getJsonList(response, todoList);
		} else {
			request.setAttribute("msg", msg);
			request.setAttribute("todoList", todoList);
			request.getRequestDispatcher("index.jsp").forward(request, response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}