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

@WebServlet("/EditServlet")
public class EditServlet extends HttpServlet {
	private final CommonRepository repo = new CommonRepository();
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CommonProcess.getDefaultCode(request, response);
		Todo todo = CommonProcess.getParameter(request);

		todo = repo.getTodo(todo);
		
		request.setAttribute("todo", todo);
		request.getRequestDispatcher("/edit.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CommonProcess.getDefaultCode(request, response);
		Todo todo = CommonProcess.getParameter(request);
		boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		
		if(!CommonProcess.taskInputValidation(request, response, todo, isAjax, "edit.jsp")) {
			return;
		}

		repo.update(todo);

		if (isAjax) {
			CommonProcess.async(request, response, true);
		} else {
			CommonProcess.sync(request, response, "編集しました");
		}
	}
}

//理想ーーーーーーーーーーーーーーーーーーーーーーーーーーーーーー
//ーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーー

//ResponseHandler responseHandler = ResponseHandlerFactory.create(request);
//
//class AjaxResponseHandler implements ResponseHandler
//class SyncResponseHanlder
//
//try {
//	validationMethod(request, response, todo, isAjax);
//	
//	repo.update(todo);
//	
//	responseHandler.handle(request, response);
//} catch(XxxException e) {
//	
//} catch(Exception e) {
//	
//}



//ーーーーーーーーーーーーーーーーーーーーーーーーーーーーーー
//ーーーーーーーーーーーーーーーーーーーーーーーーーーーーーー