package common;

import javax.servlet.ServletException;

import dao.TodoDao;
import dto.Todo;

public class CommonRepository {
	private TodoDao dao;
	
	public CommonRepository() {
		this.dao = new TodoDao();
	}
	
	public void insert(Object obj) throws ServletException {
		if(obj instanceof Todo todo) {
			try {
				dao.insert(todo);
			} catch (Exception e) {
				throw new ServletException(e);
			}
		}
	}
	
	public boolean delete(Object obj) throws ServletException {
		boolean isDelete = false;
		if(obj instanceof Todo todo) {
			try {
				isDelete = dao.delete(todo);
			} catch (Exception e) {
				throw new ServletException(e);
			}
		}
		return isDelete;
	}
}
