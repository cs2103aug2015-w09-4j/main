package main;
import java.lang.String;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.util.Scanner;

import java.util.ArrayList;
import java.util.Date;

public class TangGuo {
	
	private static String fileName;
	private Scanner scanner = new Scanner(System.in);
	private TGStorageManager storage;
	private ArrayList<Integer> taskIDCache;
	private ArrayList<Integer> scheduleIDCache;
	private ArrayList<Integer> deadlineIDCache;
	
	public TangGuo(String file) {
		fileName = file;
		storage = new TGStorageManager(fileName);
		initialiseIDCaches();
	}
	
	public static void main(String[] args) throws IOException, ParseException {
		TangGuo tg = new TangGuo(args[0]);
		showToUser(String.format(Constants.TANGGUO_START, fileName));
		
		while (true) {
			tg.runUserInput();
		}
	}
	
	private void runUserInput(){
		requestInput();
		String input = scanner.nextLine();
		String output = executeinputs(input);
		showToUser(output);
	}
	
	private static void showToUser(String display) {
		System.out.println(display);
	}
	
	private static void requestInput() {
		System.out.print("input: ");
	}
	
	public String executeinputs(String input) {
		
		String command = getFirstWord(input);
		Constants.COMMAND_TYPE commandType = findCommandType(command);
		String arguments = removeFirstWord(input);
		
		switch (commandType) {
			case ADD_DEADLINE:
				return addDeadline(arguments);
			case ADD_SCHEDULE:
				return addSchedule(arguments);
			case ADD_TASK:
				return addTask(arguments);
			case DISPLAY:
				return displayTangGuo();
			case UPDATE:
				return updateName(arguments);
			case DELETE:
				return deleteEvent(arguments);
			case EXIT:
				showToUser(Constants.TANGGUO_EXIT);
				System.exit(0);
			case INVALID:
				return Constants.TANGGUO_INVALID_COMMAND;
			default:
				return Constants.TANGGUO_IO_EXCEPTION;
		} 
	}
	
	private String addDeadline(String event){
		
		// add into storage
		String[] array = event.split("by ");
		
		try {
			Date endDate = dateConverter(array[array.length-1]);
			deadlineIDCache.add(storage.getCurrentIndex());
			storage.addDeadline(event, endDate);			
		} catch (ParseException e){
			return Constants.TANGGUO_INVALID_DATE;
		}
		return String.format(Constants.TANGGUO_ADD_SUCCESS, fileName, event);
	}
	
	private String addSchedule(String event){
		
		// add into storage
		String[] array1 = event.split("from ");
		String[] array2 = array1[array1.length - 1].split("to ");

		try {
			Date endDate = dateConverter(array2[1]);
			Date startDate = dateConverter(array2[0]);
			scheduleIDCache.add(storage.getCurrentIndex());
			storage.addSchedule(event, startDate, endDate);
		} catch (ParseException e) {
			return Constants.TANGGUO_INVALID_DATE;
		}
		return String.format(Constants.TANGGUO_ADD_SUCCESS, fileName, event);
	}
	
	private String addTask(String event) {
		// add into storage
		taskIDCache.add(storage.getCurrentIndex());
		storage.addTask(event);
		
		return String.format(Constants.TANGGUO_ADD_SUCCESS, fileName, event);
	}
	
	private String displayTangGuo() {
		String printOut = "";
		
		if (allCachesEmpty()) {
			return String.format(Constants.TANGGUO_EMPTY_FILE, fileName);
		}
		
		printOut += displayCache("Tasks", storage.getTaskCache());
		printOut += displayCache("Deadlines", storage.getDeadlineCache());
		printOut += displayCache("Schedules", storage.getScheduleCache());
		
		return printOut;
	}
	
	private boolean allCachesEmpty(){
		return(storage.getDeadlineCache().isEmpty() && storage.getTaskCache().isEmpty()
				&& storage.getScheduleCache().isEmpty());			
	}
	
	private String displayCache(String cacheName, ArrayList<Event> cache){
		String printOut = cacheName + ":\n";
		for (int i = 0; i < cache.size(); i++) {
			printOut = printOut + (i+1) + ". " + cache.get(i).getName() + "\n";
		}
		return printOut;
	}
	
	private String updateName(String event) {
		String taskToEdit = getFirstWord(event);
		String newName;
		
		String taskType = taskToEdit.substring(0, 1);
		int index, taskID;
		try {
			index = Integer.parseInt(taskToEdit.substring(1));
			index--;	//assuming cache[0] is non-null
		} catch (Exception e) {
			return Constants.TANGGUO_INVALID_COMMAND;
		}
		
		try {
			newName = event.split("\"")[1];
		} catch (Exception e) {
			return Constants.TANGGUO_INVALID_COMMAND;
		}
		
		String oldVersion;
		
		try{
			if (taskType.equals("t")){
				taskID = taskIDCache.get(index);
				oldVersion = storage.getTaskCache().get(index).getName();
			} else if (taskType.equals("d")){
				taskID = deadlineIDCache.get(index);
				oldVersion = storage.getDeadlineCache().get(index).getName();
			} else if (taskType.equals("s")){
				taskID = scheduleIDCache.get(index);
				oldVersion = storage.getScheduleCache().get(index).getName();
			} else {
				return Constants.TANGGUO_INVALID_COMMAND;
			}
		} catch (IndexOutOfBoundsException e){
			return Constants.TANGGUO_OUT_BOUNDS;
		}
		
		storage.updateNameByID(taskID, newName);
		
		return String.format(Constants.TANGGUO_UPDATE_NAME, oldVersion,
				newName) + "\n" + displayTangGuo();
		
	}
	
	/**
	 * deletes an Event
	 * @param toBeDeleted : [letter][number] 
	 * letter refers to the type of event = {t, s, d}; number refers to index displayed
	 * @return
	 */
	private String deleteEvent(String toBeDeleted) {
		String taskType = toBeDeleted.substring(0, 1);
		int index, IDToDelete;
		try {
			index = Integer.parseInt(toBeDeleted.substring(1));
		} catch (Exception e) {
			return Constants.TANGGUO_INVALID_COMMAND;
		}
		
		//assuming cache[0] is non-null
		index--;
		
		try{
			if (taskType.equals("t")){
				IDToDelete = taskIDCache.remove(index);
			} else if (taskType.equals("d")){
				IDToDelete = deadlineIDCache.remove(index);
			} else if (taskType.equals("s")){
				IDToDelete = scheduleIDCache.remove(index);
			} else {
				return Constants.TANGGUO_INVALID_COMMAND;
			}
		} catch (IndexOutOfBoundsException e){
			return Constants.TANGGUO_OUT_BOUNDS;
		}
				
		Event deletedEvent = storage.deleteEventByID(IDToDelete);

		System.out.println(String.format(Constants.TANGGUO_DELETE_SUCCESS, fileName, deletedEvent.getName()));
		return displayTangGuo();
	}
	
	private Date dateConverter(String dateString) throws ParseException {
		DateFormat format = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
		Date date = format.parse(dateString);
		return date;
	}
	
	private void initialiseIDCaches() {
		
		ArrayList<Event> tasks = storage.getTaskCache();
		ArrayList<Event> deadlines = storage.getDeadlineCache();
		ArrayList<Event> schedules = storage.getScheduleCache();
		
		taskIDCache = initializeCache(tasks);
		deadlineIDCache = initializeCache(deadlines);
		scheduleIDCache = initializeCache(schedules);
			
	}
	
	private ArrayList<Integer> initializeCache(ArrayList<Event> cache) {
		ArrayList<Integer> IDCache = new ArrayList<>();
		if(!cache.isEmpty()) {
			for(int i = 0; i < cache.size(); i++) {
				IDCache.add(cache.get(i).getID());
			}
		}
		return IDCache;
	}
	
	private Constants.COMMAND_TYPE findCommandType(String commandTypeString) {
		if (commandTypeString.equalsIgnoreCase("add schedule")) {
			return Constants.COMMAND_TYPE.ADD_SCHEDULE;
		} else if (commandTypeString.equalsIgnoreCase("add task")) {
			return Constants.COMMAND_TYPE.ADD_TASK;
		} else if (commandTypeString.equalsIgnoreCase("add deadline")) {
			return Constants.COMMAND_TYPE.ADD_DEADLINE;
		} else if (commandTypeString.equalsIgnoreCase("display")) {
			return Constants.COMMAND_TYPE.DISPLAY;
		} else if (commandTypeString.equalsIgnoreCase("delete")) {
			return Constants.COMMAND_TYPE.DELETE;
		} else if (commandTypeString.equalsIgnoreCase("exit")) {
			return Constants.COMMAND_TYPE.EXIT;
		} else if (commandTypeString.equalsIgnoreCase("update name")) {
			return Constants.COMMAND_TYPE.UPDATE;
		} else {
			return Constants.COMMAND_TYPE.INVALID;
		}
	}
	
	private String getFirstWord(String input) {
		String inputString = input.trim().split("\\s+")[0];
		
		if(inputString.equals("add") || inputString.equals("update")) {
			inputString += " " + input.trim().split("\\s+")[1];
		}	
		return inputString;
	}
	
	private String removeFirstWord(String input) {
		return input.replace(getFirstWord(input), "").trim();
	}
}