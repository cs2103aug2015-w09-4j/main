import java.util.Date;


public class Event {
	public String name;
	public int type;
	public Date startDate;
	public Date endDate;
	public int ID;
	
	public Event(int ID, String name, Date startDate, Date endDate){
		this.ID = ID;
		this.name = name;
		this.type = Constants.SCHEDULE_TYPE_NUMBER;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	public Event(int ID, String name, Date endDate){
		this.ID = ID;
		this.name = name;
		this.type = Constants.DEADLINE_TYPE_NUMBER;
		this.endDate = endDate;
		
	}
	  
	public Event(int ID, String name){
		this.ID = ID;
		this.name = name;
		this.type = Constants.TASK_TYPE_NUMBER;
	}
}
