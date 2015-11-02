package main;
import java.util.Date;

public class Command {
	private Constants.COMMAND_TYPE type;
	private int eventType;
	private int eventID;
	private String eventName;
	private Date eventStart;
	private Date eventEnd;
	private String eventCategory = Constants.DEFAULT_CATEGORY;
	private int eventPriority = Constants.DEFAULT_PRIORITY;
	private String searchKey;
	private String path;
	private String displayedIndex;
	private boolean isUserCommand;
	private Event event;
	
	public Event getEvent() {
		return event;
	}
	
	public Constants.COMMAND_TYPE getType() {
		return type;
	}
	
	public int getEventType() {
		return eventType;
	}
	
	public int getEventID() {
		return eventID;
	}
	
	public String getEventName() {
		return eventName;
	}
	
	public Date getEventStart() {
		return eventStart;
	}
	
	public Date getEventEnd() {
		return eventEnd;
	}
	
	public String getEventCategory() {
		return eventCategory;
	}
	
	public int getEventPriority() {
		return eventPriority;
	}
	
	public String getSearchKey() {
		return searchKey;
	}

	public String getPath() {
		return path;
	}
	
	public String getDisplayedIndex() {
		return displayedIndex;
	}
	
	public boolean isUserCommand(){
		return isUserCommand;
	}
	
	public void setEvent(Event event) {
		this.event = event;
	}
	
	public void setType(Constants.COMMAND_TYPE type) {
		this.type = type;
	}
	
	public void setEventType(int eventType) {
		this.eventType = eventType;
	}
	
	public void setEventID(int eventID) {
		this.eventID = eventID;
	}
	
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	
	public void setEventStart(Date eventStartDate) {
		this.eventStart = eventStartDate;
	}
	
	public void setEventEnd(Date eventEndDate) {
		this.eventEnd = eventEndDate;
	}
	
	public void setEventCategory(String eventCategory) {
		this.eventCategory = eventCategory;
	}
	
	public void setEventPriority(int eventPriority) {
		this.eventPriority = eventPriority;
	}
	
	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public void setDisplayedIndex(String displayedIndex) {
		this.displayedIndex = displayedIndex;
	}
	
	public void setIsUserCommand(boolean b){
		isUserCommand = b;
	}
}