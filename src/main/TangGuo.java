package main;
import java.lang.String;

import java.text.ParseException;

import java.io.IOException;
import java.util.Scanner;

import java.util.ArrayList;


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
		Command currentCommand;
		try {
			currentCommand = Parser.parseCommand(input);
		} catch (ParseException e) {
			return Constants.TANGGUO_INVALID_DATE;
		} catch (IndexOutOfBoundsException e){
			return Constants.TANGGUO_INVALID_COMMAND;
		}
		switch (currentCommand.getType()) {
			case ADD_DEADLINE:
				return addDeadline(currentCommand);
			case ADD_SCHEDULE:
				return addSchedule(currentCommand);
			case ADD_TASK:
				return addTask(currentCommand);
			case DISPLAY:
				return displayTangGuo();
			case UPDATE:
				return updateName(currentCommand);
			case DELETE:
				return deleteEvent(currentCommand);
			case EXIT:
				showToUser(Constants.TANGGUO_EXIT);
				System.exit(0);
			case INVALID:
				return Constants.TANGGUO_INVALID_COMMAND;
			default:
				return Constants.TANGGUO_IO_EXCEPTION;
		} 
	}
	
	private String addDeadline(Command command){
		deadlineIDCache.add(storage.getCurrentIndex());
		storage.addDeadline(command.getEventName(), command.getEventEnd());			
		
		return String.format(Constants.TANGGUO_ADD_SUCCESS, fileName, command.getEventName());
	}
	
	private String addSchedule(Command command){
		scheduleIDCache.add(storage.getCurrentIndex());
		// add into storage
		storage.addSchedule(command.getEventName(), command.getEventStart(), command.getEventEnd());
		return String.format(Constants.TANGGUO_ADD_SUCCESS, fileName, command.getEventName());
	}
	
	private String addTask(Command command) {
		taskIDCache.add(storage.getCurrentIndex());
		// add into storage
		storage.addTask(command.getEventName());
		
		return String.format(Constants.TANGGUO_ADD_SUCCESS, fileName, command.getEventName());
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
	
	private String updateName(Command command) {
		String taskToEdit = command.getDisplayedIndex();
		String newName = command.getEventName();
		
		String taskType = taskToEdit.substring(0, 1);
		int index, taskID;
		try {
			index = Integer.parseInt(taskToEdit.substring(1));
			index--;	//assuming cache[0] is non-null
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
	private String deleteEvent(Command command) {
		String taskType = command.getDisplayedIndex().substring(0, 1);
		int index, IDToDelete;
		try {
			index = Integer.parseInt(command.getDisplayedIndex().substring(1));
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

}