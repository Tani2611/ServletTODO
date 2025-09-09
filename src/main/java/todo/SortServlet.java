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
import dao.TodoDao;
import dto.Sort;
import dto.Todo;

@WebServlet("/SortServlet")
public class SortServlet extends HttpServlet {
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CommonProcess.getDefaultCode(request, response);
		Sort sort = CommonProcess.getSort(request);

		List<Todo> todoList = new ArrayList<>();
		try {
			todoList = new TodoDao().sort(sort);
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
