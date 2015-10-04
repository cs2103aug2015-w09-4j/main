import java.util.Date;

public class Event {
	private int type;
	private int ID;
	private String name;
	private String category;
	private Date startDate;
	private Date endDate;
	
	public Event(int ID, String name, Date startDate, Date endDate){
		this.type = Constants.SCHEDULE_TYPE_NUMBER;
		this.ID = ID;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public Event(int ID, String name, Date endDate){
		this.type = Constants.DEADLINE_TYPE_NUMBER;
		this.ID = ID;
		this.name = name;
		this.endDate = endDate;
	}
	  
	public Event(int ID, String name){
		this.type = Constants.TASK_TYPE_NUMBER;
		this.ID = ID;
		this.name = name;
	}
	
	public int getID() {
		return ID;
	}
	
	public String getName() {
		return name;
	}
	
	public String getCategory() {
		return category;
	}
	
	public Date getStart() {
		return startDate;
	}
	
	public Date getEnd() {
		return endDate;
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
}