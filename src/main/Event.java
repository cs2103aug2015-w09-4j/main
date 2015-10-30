package main;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Event {
	private int type;
	private int ID;
	private String name;
	private Date startDate;
	private Date endDate;
	private String category;
	private int priority;
	private boolean isDone;

	public Event(int ID, String name, Date startDate, Date endDate){
		this.type = Constants.SCHEDULE_TYPE_NUMBER;
		this.ID = ID;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.category = Constants.DEFAULT_CATEGORY;
		this.priority = Constants.DEFAULT_PRIORITY;
		this.isDone = false;
	}

	public Event(int ID, String name, Date endDate){
		this.type = Constants.DEADLINE_TYPE_NUMBER;
		this.ID = ID;
		this.name = name;
		this.endDate = endDate;
		this.category = Constants.DEFAULT_CATEGORY;
		this.priority = Constants.DEFAULT_PRIORITY;
		this.isDone = false;
	}

	public Event(int ID, String name){
		this.type = Constants.TASK_TYPE_NUMBER;
		this.ID = ID;
		this.name = name;
		this.category = Constants.DEFAULT_CATEGORY;
		this.priority = Constants.DEFAULT_PRIORITY;
		this.isDone = false;
	}

	public boolean isDone(){
		return this.isDone;
	}

	public void setIsDone(boolean b){
		this.isDone = b;
	}
	public int getType() {
		return type;
	}

	public int getID() {
		return ID;
	}

	public String getName() {
		return name;
	}

	public Date getStart() {
		return startDate;
	}

	public Date getEnd() {
		return endDate;
	}

	public String getCategory() {
		return category;
	}

	public int getPriority() {
		return priority;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStart(Date startDate) {
		this.startDate = startDate;
	}

	public void setEnd(Date endDate) {
		this.endDate = endDate;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public boolean contains(String input) {
		if (getName().indexOf(input) > -1) {
			return true;
		} else if (getCategory().indexOf(input) > -1) {
			return true;
		} else if (getStart() != null) {
			if (formatDate(getStart()).toLowerCase().indexOf(input) > -1 || formatDate(getEnd()).toLowerCase().indexOf(input) > -1) {
				return true;
			} else {
				return false;
			}
		} else if (getEnd() != null) {
			if (formatDate(getEnd()).toLowerCase().indexOf(input) > -1) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * returns for
	 * schedule: "{start date, end date} eventName"
	 * deadline: "{end date} eventName"
	 *     task: "eventName"
	 */
	public String toString(){
		String string = "";
		if (getStart() != null){
			string += "{"+formatDate(getStart())+", "+formatDate(getEnd())+"} ";
		} else if (getEnd() != null){
			string += "{"+formatDate(getEnd())+"} ";
		}
		string += getName() + "\n";
		return string;
	}

	/**
	 * returns date in the form of "DAY MONTH DATE HOUR:MINUTE" in a 24-hour format
	 * is returned in the form of "DAY YEAR MONTH DATE HOUR:MINUTE" if year is not
	 * current year
	 * @param date
	 * @return
	 */
	private String formatDate(Date date){
		Calendar now = Calendar.getInstance();   // Gets the current date and time
		int currentYear = now.get(Calendar.YEAR);
		int dateYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(date));

		if (dateYear <= currentYear){
			return new SimpleDateFormat("E MMM dd HH:mm").format(date);
		} else {
			return new SimpleDateFormat("E yyyy MMM dd HH:mm").format(date);
		}
	}
}