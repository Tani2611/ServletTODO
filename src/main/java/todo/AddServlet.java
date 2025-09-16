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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CommonProcess.getDefaultCode(request, response);
		Todo todo = CommonProcess.getParameter(request);
		boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

		// TODO: エラーの時はreturnする分岐処理忘れている　〇
		if(!CommonProcess.taskInputValidation(request, response, todo, isAjax, "add.jsp")) {
			return;
		}

		repo.insert(todo);

		if (isAjax) {
			CommonProcess.async(request, response, true);
		} else {
			CommonProcess.sync(request, response, "追加しました");
		}
	}


}