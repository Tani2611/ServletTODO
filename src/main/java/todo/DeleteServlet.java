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
		boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

		boolean isDelete = repo.delete(todo);
		// TODO: レスポンスを共通化　△
		if (isAjax) {
			CommonProcess.async(request, response, isDelete);
		} else {
			CommonProcess.sync(request, response," 削除しました");
		}

	}

}
