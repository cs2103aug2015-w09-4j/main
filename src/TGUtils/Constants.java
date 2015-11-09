package TGUtils;

import java.awt.Color;
import java.awt.Dimension;

public class Constants {
	/*
	 * GUI Tabs
	 */
	public static final int TODAY_TAB_NUMBER = 0;
	public static final int TASK_TAB_NUMBER = 1;
	public static final int DEADLINE_TAB_NUMBER = 2;
	public static final int SCHEDULE_TAB_NUMBER = 3;
	public static final int SEARCH_TAB_NUMBER = 4;
	public static final int HELP_TAB_NUMBER = 5;

	public static final int EVENT_LIST_TASK = 0;
	public static final int EVENT_LIST_DEADLINE = 1;
	public static final int EVENT_LIST_SCHEDULE = 2;
	
	public static final String XML_TASK_EXPRESSION = "/calendar/task";
	public static final String XML_DEADLINE_EXPRESSION = "/calendar/deadline";
	public static final String XML_SCHEDULE_EXPRESSION = "/calendar/schedule";
	public static final String XML_CALENDAR_EXPRESSION = "calendar";
	
	public final static Color GUI_COLOR_CLASH = new Color(255, 160, 0);
	public final static Color GUI_COLOR_HIGH = new Color(246, 150, 121);
	public final static Color GUI_COLOR_MID = new Color(255, 247, 153);
	public final static Color GUI_COLOR_LOW = new Color(130, 202, 156);
	public final static Color GUI_COLOR_EVEN_ROW = new Color(216, 216, 216);
	
	public final static int TABLE_WIDTH = 1280;
	public final static int TABLE_HEIGHT = 112;
	public final static Dimension TABLE_DIMENSION = new Dimension(TABLE_WIDTH, TABLE_HEIGHT);
	
	public final static int COLUMN_ID_SIZE = 25;
	public final static int COLUMN_CATEGORY_SIZE = 100;
	public final static int COLUMN_PRIORITY_SIZE = 100;
	public final static int COLUMN_FIXED_TOTAL = COLUMN_ID_SIZE + COLUMN_CATEGORY_SIZE + COLUMN_PRIORITY_SIZE;
	
	public final static float CELL_TIME_PERCENTAGE = 0.20f;
	public final static float CELL_NAME_TASK_PERCENTAGE = 1.00f;
	public final static float CELL_NAME_DEADLINE_PERCENTAGE = 0.80f;
	public final static float CELL_NAME_SCHEDULE_PERCENTAGE = 0.60f;

	/*
	 * Event Constants
	 */
	public static final int TASK_TYPE_NUMBER = 1;
	public static final int DEADLINE_TYPE_NUMBER = 2;
	public static final int SCHEDULE_TYPE_NUMBER = 3;
	public static final String DEFAULT_CATEGORY = "DEFAULT";
	public static final int DEFAULT_PRIORITY = 1;

	/*
	 * StorageManager Constants
	 */
	public static final String LOG_FILE = "Tangguo.log";
	public static final String LOG_ADD_TASK = "add task: ";
	public static final String LOG_ADD_DEADLINE = "add deadline: ";
	public static final String LOG_ADD_SCHEDULE = "add schedule: ";
	public static final String LOG_DELETE_TASK = "delete task: ";
	public static final String LOG_DELETE_DEADLINE = "delete deadline: ";
	public static final String LOG_DELETE_SCHEDULE = "delete schedule: ";
		
	public static final String LOG_FAILED_COMPILATION_XPATH = "Failed to compile xPath";
	public static final String LOG_FAILED_PARSE_DATE_FROM_FILE = "Failed to parse date when reading from file";
	public static final String LOG_FAILED_PARSE_FILE = "Failed to parse storage file";
	public static final String LOG_FAILED_WRITE_TO_FILE = "Failed to write into file";
	public static final String LOG_FAILED_CREATE_FILE = "Failed to create storage file";
	public static final String LOG_FAILED_CLOSE_STRINGWRITER = "Failed to close StringWriter";
	public static final String LOG_FAILED_TRANSFORM_XMLSTRING = "Failed to transfrom XMLString";
	
	public static final String FAILED_TO_INITIALIZE_LOGGER = "Failed to initialize log file";
	public static final String ASSERT_NO_MATCHED_ID = "no matched ID found";
	
	public static final String ATTRIBUTE_CURRENT_INDEX = "current";
	public static final String ATTRIBUTE_ID = "id";
	public static final String ATTRIBUTE_INDENT_NUMBER = "indent-number";
	public static final int ATTRIBUTE_INDENT_NUMBER_VALUE = 2;
	public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_CATEGORY = "category";
	public static final String PROPERTY_PRIORITY = "priority";
	public static final String PROPERTY_IS_DONE = "isDone";
	public static final String PROPERTY_HAS_CLASH = "hasClash";
	public static final String PROPERTY_START = "startDate";
	public static final String PROPERTY_END = "endDate";
	public static final String PROPERTY_YES = "yes";
	
	public static final String CALENDAR = "calendar";
	public static final String INITIALIZE_CURRENT_INDEX = "0";
	public static final String TASK_TYPE = "task";
	public static final String SCHEDULE_TYPE = "schedule";
	public static final String DEADLINE_TYPE = "deadline";

	
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
	public static final char DEADLINE_CHAR = 'd';
	public static final char SCHEDULE_CHAR = 's';

	public static final String LOW = "LOW";
	public static final String MID = "MID";
	public static final String HIGH = "HIGH";


	/*
	 * Logic Constants
	 */
	public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy HH:mm";
	public static final String DAY_FORMAT = "yyyyMMdd";
	public static final int DATE_LENGTH = 17;
	public static final String NEW_LINE = "\n";
	public static final String SLASH = "/";
	public static final int INVALID_INDICATOR = -1;
	
	public static final String TASK_IDENTITY = "t";
	public static final String DEADLINE_IDENTITY = "d";
	public static final String SCHEDULE_IDENTITY = "s";
	public static final String DEFAULT_STRING = "default";
	
	public static final String DISPLAY_NAME = "NAME";
	public static final String DISPLAY_START = "START DATE";
	public static final String DISPLAY_END = "END DATE";
	public static final String DISPLAY_PRIORITY	= "PRIORITY";

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
	public static final String TANGGUO_SEARCH_SUCCESS = "Search results for %1$s ";
	public static final String TANGGUO_IMPORT_SUCCESS = "TangGuo data successfully imported from \"%1$s\"!";
	public static final String TANGGUO_PATH_SET = "%1$s data saved to \"%2$s\"!";
	public static final String TANGGUO_SHOW_DONE = "The completed events are now shown!";
	public static final String TANGGUO_HIDE_DONE = "The completed events are now hidden!";
	public static final String TANGGUO_EXIT = "See you again!";

	public static final String TANGGUO_SCHEDULE_CLASH = "Cannot add : %1$s because that time slot is already taken!";
	public static final String TANGGUO_UNDO_NO_COMMAND ="No command to be undone!";
	public static final String TANGGUO_EMPTY_FILE = "%s is empty!";
	public static final String TANGGUO_SEARCH_FAIL = "The keyword: %1$s is not found!";
	public static final String TANGGUO_IO_EXCEPTION = "An unexpected error has occurred";
	public static final String TANGGUO_OUT_BOUNDS = "The index you entered is invalid, try again";
	public static final String TANGGUO_INVALID_INDEX = "The task index you entered seems to be invalid, try again";
	public static final String TANGGUO_INVALID_DATE = "The date format you entered seems to be invalid, try again";
	public static final String TANGGUO_INVALID_SCHEDULE = "The start time you entered seem to be later than your end time, try again";
	public static final String TANGGUO_INVALID_COMMAND = "The command you entered seems to be invalid, try another command";
	public static final String TANGGUO_DATE_OUT_OF_BOUNDS = "The date or time you have entered is in the wrong format, try again";
	public static final String TANGGUO_INVALID_PRIORITY = "The priority level you entered seems to be invalid, try again";

	public static final String ASSERT_UNEXPECTED_TYPE_NUM = "unexpected type number";


	public enum COMMAND_TYPE {
		ADD, ADD_DEADLINE, ADD_SCHEDULE, ADD_TASK, DISPLAY, DELETE, 
		UPDATE_NAME, UPDATE_START, UPDATE_END, UPDATE_PRIORITY, UPDATE_CATEGORY, UNDO, DONE, 
		SORT_NAME, SORT_START, SORT_END, SORT_PRIORITY, SEARCH, TOGGLE, PATH, IMPORT, EXCEPTION, INVALID, EXIT
	};

}