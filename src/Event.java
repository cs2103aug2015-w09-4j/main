import java.util.Date;

public class Event {
	private int type;
	private int ID;
	private String name;
	private Date startDate;
	private Date endDate;
	private String category;
	private int priority;
	
	public Event(int ID, String name, Date startDate, Date endDate){
		this.type = Constants.SCHEDULE_TYPE_NUMBER;
		this.ID = ID;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.category = Constants.DEFAULT_CATEGORY;
		this.priority = Constants.DEFAULT_PRIORITY;
	}
	
	public Event(int ID, String name, Date endDate){
		this.type = Constants.DEADLINE_TYPE_NUMBER;
		this.ID = ID;
		this.name = name;
		this.endDate = endDate;
		this.category = Constants.DEFAULT_CATEGORY;
		this.priority = Constants.DEFAULT_PRIORITY;
	}
	  
	public Event(int ID, String name){
		this.type = Constants.TASK_TYPE_NUMBER;
		this.ID = ID;
		this.name = name;
		this.category = Constants.DEFAULT_CATEGORY;
		this.priority = Constants.DEFAULT_PRIORITY;
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
	
}