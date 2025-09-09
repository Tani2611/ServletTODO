package dto;

import java.time.LocalDate;

public class Todo {
	private Integer id;
	private String task;
	private LocalDate date;
	//	private transient LocalDate date;
	private boolean status = false;

	public Todo(Integer id, String task, LocalDate date, boolean status) {
		super();
		this.id = id;
		this.task = task;
		this.date = date;
		this.status = status;
	}

	public Todo(Integer id, String task, LocalDate date) {
		super();
		this.id = id;
		this.task = task;
		this.date = date;
	}

	public Todo(String task, LocalDate date) {
		super();
		this.task = task;
		this.date = date;
	}

	public Todo(int id) {
		super();
		this.id = id;
	}

	public Todo() {

	}

	public String getDateStr() {
		return (date != null) ? date.toString() : "";
	}

	public int getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return id + "｜" + task + "｜" + date + "｜" + status;
	}

}
