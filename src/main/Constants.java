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
	public static final int DEFAULT_PRIORITY = -1;
	
	
	/*
	 * Tanguo Constants
	 */
	public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy HH:mm";
	public static final int DATE_LENGTH = 17;
	public static final String TANGGUO_IO_EXCEPTION = "An unexpected error has occurred";
	public static final String TANGGUO_START = "Welcome to TangGuo. %s is ready for use";
	public static final String TANGGUO_ADD_SUCCESS = "Added to %1$s: %2$s";
	public static final String TANGGUO_WRONG_DELETE = "The index you entered is not a number, try again";
	public static final String TANGGUO_OUT_BOUNDS = "The index you entered is invalid, try again";
	public static final String TANGGUO_DELETE_SUCCESS = "Deleted from %1$s: %2$s";
	public static final String TANGGUO_EMPTY_FILE = "%s is empty!";
	public static final String TANGGUO_CLEAR = "All contents deleted from %s";
	public static final String TANGGUO_UPDATE_NAME = "%1$s updated to %2$s";
	public static final String TANGGUO_INVALID_DATE = "The date format you entered seems to be invalid, try again";
	public static final String TANGGUO_INVALID_COMMAND = "The command you entered seems to be invalid, try another command";
	public static final String TANGGUO_EXIT = "See you again!";
	public static final String TANGGUO_UNDO_NO_COMMAND ="No command to be undone!";
	public static final String TANGGUO_UNDO_SUCCESS = "Undo successful!";
	public enum COMMAND_TYPE {
		ADD_DEADLINE, ADD_SCHEDULE, ADD_TASK, DISPLAY, DELETE,UNDO, EXIT, INVALID, UPDATE_NAME, EXCEPTION
	};

}

