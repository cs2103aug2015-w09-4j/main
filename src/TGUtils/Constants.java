package TGUtils;
public class Constants {
	/*
	 * GUI Tabs
	 */
	public static final int TODAY_TAB_NUMBER = 0;
	public static final int TASK_TAB_NUMBER = 1;
	public static final int DEADLINE_TAB_NUMBER = 2;
	public static final int SCHEDULE_TAB_NUMBER = 3;
	public static final int SEARCH_TAB_NUMBER = 4;

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
	
	public static final String DEADLINE = "deadline";
	public static final String SCHEDULE = "schedule";
	public static final String SCHEDULE_END = "scheduleEnd";
	public static final String SCHEDULE_START = "scheduleStart";
	public static final String SORT = "sort";
	public static final String UPDATE = "update";
	public static final String WORD_SPLIT = "\\s+";
	public static final String TOGGLE = "toggle";
	public static final String IMPORT = "import";
	public static final String PATH = "path";
	public static final String SEARCH = "search";
	public static final String SORT_PRIORITY = "sort priority";
	public static final String SORT_END = "sort end";
	public static final String SORT_START = "sort start";
	public static final String SORT_NAME = "sort name";
	public static final String DONE = "done";
	public static final String UPDATE_CATEGORY = "update category";
	public static final String UPDATE_PRIORITY = "update priority";
	public static final String UPDATE_START = "update start";
	public static final String UPDATE_END = "update end";
	public static final String UPDATE_NAME = "update name";
	public static final String UNDO = "undo";
	public static final String EXIT = "exit";
	public static final String DELETE = "delete";
	public static final String DISPLAY = "display";
	public static final String ADD = "add";
	
	public static final String DEFAULT_SCHEDULE_TIME = "00:00";
	public static final String DEFAULT_DEADLINE_TIME = "23:59";
	public static final String HOUR_MINUTE_SPLIT = ":";
	public static final String DAY_MONTH_YEAR_SPLIT = "/";
	public static final String NULL = "";
	public static final String TIME_DETECTION = "[0-9]:[0-9]";
	public static final String DATE_DETECTION = "[0-9]/[0-9]";
	public static final String SPACE = " ";

	public static final String LOW = "LOW";
	public static final String MID = "MID";
	public static final String HIGH = "HIGH";

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

	public static final String TANGGUO_SEARCH_SUCCESS = "Search results for %1$s ";
	public static final String TANGGUO_SEARCH_FAIL = "The keyword: %1$s is not found!";
	public static final String TANGGUO_PATH_SET = "%1$s data saved to \"%2$s\"!";
	public static final String TANGGUO_IMPORT_SUCCESS = "TangGuo data successfully imported from \"%1$s\"!";
	
	public static final String TANGGUO_IO_EXCEPTION = "An unexpected error has occurred";
	public static final String TANGGUO_OUT_BOUNDS = "The index you entered is invalid, try again";
	public static final String TANGGUO_INVALID_INDEX = "The task index you entered seems to be invalid, try again";
	public static final String TANGGUO_INVALID_DATE = "The date format you entered seems to be invalid, try again";
	public static final String TANGGUO_INVALID_SCHEDULE = "The start time you entered seem to be later than your end time, try again";
	public static final String TANGGUO_INVALID_COMMAND = "The command you entered seems to be invalid, try another command";
	public static final String TANGGUO_DATE_OUT_OF_BOUNDS = "The date or time you have entered is in the wrong format, try again";

	public static final String TANGGUO_INVALID_PRIORITY = "The priority level you entered seems to be invalid, try again";

	public static final String TANGGUO_SHOW_DONE = "The completed events are now shown!";
	public static final String TANGGUO_HIDE_DONE = "The completed events are now hidden!";


	public enum COMMAND_TYPE {
		ADD, ADD_DEADLINE, ADD_SCHEDULE, ADD_TASK, DISPLAY, DELETE, 
		UPDATE_NAME, UPDATE_START, UPDATE_END, UPDATE_PRIORITY, UPDATE_CATEGORY, UNDO, DONE, 
		SORT_NAME, SORT_START, SORT_END, SORT_PRIORITY, SEARCH, TOGGLE, PATH, IMPORT, EXCEPTION, INVALID, EXIT
	};

}