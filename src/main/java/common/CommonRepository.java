package common;

import java.util.List;

import javax.servlet.ServletException;

import dao.TodoDao;
import dto.Search;
import dto.Sort;
import dto.Todo;

public class CommonRepository {
	private TodoDao dao;

	public CommonRepository() {
		this.dao = new TodoDao();
	}

	public void insert(Object obj) throws ServletException {
		if (obj instanceof Todo todo) {
			try {
				dao.insert(todo);
			} catch (Exception e) {
				throw new ServletException(e);
			}
		}
	}
	
	public void update(Object obj) throws ServletException {
		if (obj instanceof Todo todo) {
			try {
				dao.update(todo);
			} catch (Exception e) {
				throw new ServletException(e);
			}
		}
	}
	
	public void updateStatus(Object obj) throws ServletException {
		if (obj instanceof Todo todo) {
			try {
				dao.updateStatus(todo);
			} catch (Exception e) {
				throw new ServletException(e);
			}
		}
	}

	// TODO: Repositoryで分岐処理　〇
	public boolean delete(Object obj) throws ServletException {
		int deleteCount = 0;
		if (obj instanceof Todo todo) {
			try {
				deleteCount = dao.delete(todo);
			} catch (Exception e) {
				throw new ServletException(e);
			}
		}
		return deleteCount == 1 ? true : false;
	}
	
	public Todo getTodo(Object obj) throws ServletException {
		if (obj instanceof Todo todo) {
			try {
				return dao.getTodo(todo);
			} catch (Exception e) {
				throw new ServletException(e);
			}
		}
		return null;
	}
	
	public List<Todo> getTodoList(Object obj) throws ServletException {
		if (obj instanceof Sort sort) {
			try {
				return dao.getTodoList(sort);
			} catch (Exception e) {
				throw new ServletException(e);
			}
		}
		return null;
	}
	
	public List<Todo> search(Search searchTodo, Sort sort) throws ServletException {
		try {
			return dao.search(searchTodo, sort);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
	
	public List<Todo> sort(Object obj) throws ServletException {
		if (obj instanceof Sort sort) {
			try {
				return dao.sort(sort);
			} catch (Exception e) {
				throw new ServletException(e);
			}
		}
		return null;
	}
	
	
}
