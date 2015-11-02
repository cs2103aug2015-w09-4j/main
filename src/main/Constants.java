package main;
public class Constants {
	public static final String XML_TASK_EXPRESSION = "/calendar/task"; 
	public static final String XML_DEADLINE_EXPRESSION = "/calendar/deadline";
	public static final String XML_SCHEDULE_EXPRESSION = "/calendar/schedule";
	public static final String XML_CALENDAR_EXPRESSION = "calendar";
	
	/*
	 * Event Constants
	 */
	public static final int TASK_TYPE_NUMBER = 1; 
	public static final int DEADLINE_TYPE_NUMBER = 2; 
	public static final int SCHEDULE_TYPE_NUMBER = 3; 
	public static final String DEFAULT_CATEGORY = "DEFAULT";
	public static final int DEFAULT_PRIORITY = 1;
	
	
	/*
	 * Parser Constants
	 */
	public static final String DEADLINE_SPLIT = " by ";
	public static final String SCHEDULE_FIRST_SPLIT = " from ";
	public static final String SCHEDULE_SECOND_SPLIT = " to ";
	
	/*
	 * Tanguo Constants
	 */
	public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy HH:mm";
	public static final int DATE_LENGTH = 17;
	public static final String NEW_LINE = "\n";
	
	public static final String TANGGUO_START = "Welcome to TangGuo. %s is ready for use";
	public static final String TANGGUO_ADD_SUCCESS = "Added to %1$s: %2$s";
	public static final String TANGGUO_DELETE_SUCCESS = "Deleted from %1$s: %2$s";
	public static final String TANGGUO_UPDATE_NAME_SUCCESS = "Name of %1$s updated to %2$s";
	public static final String TANGGUO_UPDATE_START_SUCCESS = "Start date of %1$s updated to %2$s";
	public static final String TANGGUO_UPDATE_START_FAIL = "Unable to update Start date, try again";
	public static final String TANGGUO_UPDATE_END_SUCCESS = "End date of %1$s updated to %2$s";
	public static final String TANGGUO_UPDATE_END_FAIL = "Unable to update End date, try again";
	public static final String TANGGUO_UPDATE_PRIORITY_SUCCESS = "Priority of %1$s updated to %2$s";	//print int?
	public static final String TANGGUO_UPDATE_CATEGORY_SUCCESS = "Category of %1$s updated to %2$s";
	public static final String TANGGUO_UPDATE_DONE_SUCCESS = "%1$s completed";
	public static final String TANGGUO_SORT_SUCCESS = "Sorted by %1$s!";
	
	public static final String TANGGUO_UNDO_SUCCESS = "Undo successful!";
	public static final String TANGGUO_EXIT = "See you again!";
	
	public static final String TANGGUO_SCHEDULE_CLASH = "Cannot add : %1$s because that time slot is already taken!";
	public static final String TANGGUO_UNDO_NO_COMMAND ="No command to be undone!";
	public static final String TANGGUO_EMPTY_FILE = "%s is empty!";

	public static final String TANGGUO_SEARCH_SUCCESS = "Search results for %1$s: ";
	public static final String TANGGUO_SEARCH_FAIL = "The keyword: %1$s is not found!";
	
	public static final String TANGGUO_IO_EXCEPTION = "An unexpected error has occurred";
	public static final String TANGGUO_OUT_BOUNDS = "The index you entered is invalid, try again";
	public static final String TANGGUO_INVALID_INDEX = "The task index you entered seems to be invalid, try again";
	public static final String TANGGUO_INVALID_DATE = "The date format you entered seems to be invalid, try again";
	public static final String TANGGUO_INVALID_SCHEDULE = "The start time you entered seem to be later than your end time, try again";
	public static final String TANGGUO_INVALID_COMMAND = "The command you entered seems to be invalid, try another command";
	public static final String TANGGUO_DATE_OUT_OF_BOUNDS = "The date or time you have entered is in the wrong format, try again";
	public static final String TANGGUO_INVALID_PRIORITY = "The priority level you entered seems to be invalid, try again";
	
	public enum COMMAND_TYPE {
		ADD, ADD_DEADLINE, ADD_SCHEDULE, ADD_TASK, DISPLAY, DELETE, 
		UPDATE_NAME, UPDATE_START, UPDATE_END, UPDATE_PRIORITY, UPDATE_CATEGORY, 
		UNDO, DONE, SORT_NAME, SORT_START, SORT_END, SORT_PRIORITY, SEARCH, EXCEPTION, INVALID, EXIT
	};

}