import java.util.Date;


public class Event {
	public String name;
	public int type;
	public Date startDate;
	public Date endDate;
	
	public Event(String name, Date startDate, Date endDate){
		this.name = name;
		this.type = 3;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	public Event(String name, Date startDate){
		this.name = name;
		this.type = 2;
		this.startDate = startDate;
		
	}
	public Event(String name){
		this.name = name;
		this.type = Constants.TASK_TYPE_NUMBER;
	}
}
