package main;
import java.util.Date;

public class Command {
	private Constants.COMMAND_TYPE type;
	private int eventType;
	private int eventID;
	private String eventName;
	private Date eventStart;
	private Date eventEnd;
	private String eventCategory;
	private int eventPriority;
	private String searchKey;
	private String path;
	private String displayedIndex;
	private boolean isUserCommand;
	private Event event;
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
	public void setIsUserCommand(boolean b){
		isUserCommand = b;
	}
	public boolean isUserCommand(){
		return isUserCommand;
	}
	public String getDisplayedIndex() {
		return displayedIndex;
	}
	public void setDisplayedIndex(String displayedIndex) {
		this.displayedIndex = displayedIndex;
	}
	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}
	public String getSearchKey() {
		return searchKey;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getPath() {
		return path;
	}
	public int getEventType() {
		return eventType;
	}
	public void setEventType(int eventType) {
		this.eventType = eventType;
	}
	public int getEventID() {
		return eventID;
	}
	public void setEventID(int eventID) {
		this.eventID = eventID;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public Date getEventStart() {
		return eventStart;
	}
	public void setEventStart(Date eventStartDate) {
		this.eventStart = eventStartDate;
	}
	public Date getEventEnd() {
		return eventEnd;
	}
	public void setEventEnd(Date eventEndDate) {
		this.eventEnd = eventEndDate;
	}
	public String getEventCategory() {
		return eventCategory;
	}
	public void setEventCategory(String eventCategory) {
		this.eventCategory = eventCategory;
	}
	public int getEventPriority() {
		return eventPriority;
	}
	public void setEventPriority(int eventPriority) {
		this.eventPriority = eventPriority;
	}
	public Constants.COMMAND_TYPE getType() {
		return type;
	}
	public void setType(Constants.COMMAND_TYPE type) {
		this.type = type;
	}
}