package main;
import java.lang.String;
import java.text.ParseException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Stack;


public class TangGuo {
	
	private static String fileName;
	private Scanner scanner = new Scanner(System.in);
	private TGStorageManager storage;
	private HashMap<String,Integer> TGIDMap;
	private Stack<Command> reversedCommandStack;
	
	public TangGuo(String file) {
		fileName = file;
		storage = new TGStorageManager(fileName);
		TGIDMap = new HashMap<String,Integer>();
		reversedCommandStack = new Stack<Command>();
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
		return executeProcessedCommand(currentCommand);
	}
	
	private String executeProcessedCommand(Command command){
		switch (command.getType()) {
			case ADD_DEADLINE:
				
				return addDeadline(command);
			case ADD_SCHEDULE:
				return addSchedule(command);
			case ADD_TASK:
				return addTask(command);
			case DISPLAY:
				return displayTangGuo();
			case UPDATE:
				return updateName(command);
			case DELETE:
				return deleteEvent(command);
			case UNDO:
				return undo();
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
		
		if (command.isUserCommand()){
			int newID = storage.addDeadline(command.getEventName(), command.getEventEnd());
			reversedCommandStack.push(reverseAdd(newID));
		}else{
			storage.addDeadline(command.getEvent());
		}
		return String.format(Constants.TANGGUO_ADD_SUCCESS, fileName, command.getEventName());
	}
	
	private String addSchedule(Command command){
		// add into storage
		
		if (command.isUserCommand()){
			int newID = storage.addSchedule(command.getEventName(), command.getEventStart(), command.getEventEnd());
			reversedCommandStack.push(reverseAdd(newID));
		}else{
			storage.addSchedule(command.getEvent());
		}
		return String.format(Constants.TANGGUO_ADD_SUCCESS, fileName, command.getEventName());
	}
	
	private String addTask(Command command) {
		// add into storage
		if (command.isUserCommand()){
			int newID = storage.addTask(command.getEventName());
			reversedCommandStack.push(reverseAdd(newID));
		}else{
			storage.addTask(command.getEvent());
		}
		return String.format(Constants.TANGGUO_ADD_SUCCESS, fileName, command.getEventName());
	}
	private Command reverseAdd(int id){
		Command temp = new Command();
		temp.setIsUserCommand(false);
		temp.setType(Constants.COMMAND_TYPE.DELETE);
		temp.setEventID(id);
		return temp;
	}
	
	private String undo(){
		if (reversedCommandStack.isEmpty()){
			return Constants.TANGGUO_UNDO_NO_COMMAND;
		}
		executeProcessedCommand(reversedCommandStack.pop());
		return Constants.TANGGUO_UNDO_SUCCESS;
	}
	private String displayTangGuo() {
		String printOut = "";
		
		if (allCachesEmpty()) {
			return String.format(Constants.TANGGUO_EMPTY_FILE, fileName);
		}
		TGIDMap.clear();
		printOut += displayCache("Tasks", storage.getTaskCache(),"t");
		printOut += displayCache("Deadlines", storage.getDeadlineCache(),"d");
		printOut += displayCache("Schedules", storage.getScheduleCache(),"s");
		
		return printOut;
	}
	
	private boolean allCachesEmpty(){
		return(storage.getDeadlineCache().isEmpty() && storage.getTaskCache().isEmpty()
				&& storage.getScheduleCache().isEmpty());			
	}
	
	private String displayCache(String cacheName, ArrayList<Event> cache, String header){
		String printOut = cacheName + ":\n";
		for (int i = 0; i < cache.size(); i++) {
			TGIDMap.put(header+(i+1), cache.get(i).getID());
			printOut = printOut + (i+1) + ". " + cache.get(i).getName() + "\n";
		}
		return printOut;
	}
	
	private String updateName(Command command) {
		//reversedCommandStack.push(reverseupdateName(command));
		String newName = command.getEventName();
		
		int taskID = TGIDMap.get(command.getDisplayedIndex());
		String oldVersion = storage.getEventByID(taskID).getName();
		if (command.isUserCommand()){
			reversedCommandStack.push(reverseUpdateName(taskID,oldVersion));
		}
		storage.updateNameByID(taskID, newName);
		
		return String.format(Constants.TANGGUO_UPDATE_NAME, oldVersion,
				newName) + "\n" + displayTangGuo();
		
	}
	
	private Command reverseUpdateName(int id, String name){
		Command temp = new Command();
		temp.setIsUserCommand(false);
		temp.setType(Constants.COMMAND_TYPE.UPDATE);
		temp.setEventName(name);
		return temp;
	}
	/**
	 * deletes an Event
	 * @param toBeDeleted : [letter][number] 
	 * letter refers to the type of event = {t, s, d}; number refers to index displayed
	 * @return
	 */
	private String deleteEvent(Command command) {
		int IDToDelete;
		if (command.isUserCommand()){
			IDToDelete = TGIDMap.get(command.getDisplayedIndex());
			reversedCommandStack.push(reverseDeleteEvent(IDToDelete));
		}else{
			IDToDelete = command.getEventID();
		}
		Event deletedEvent = storage.deleteEventByID(IDToDelete);

		System.out.println(String.format(Constants.TANGGUO_DELETE_SUCCESS, fileName, deletedEvent.getName()));
		return displayTangGuo();
	}
	
	private Command reverseDeleteEvent(int id){
		Command temp = new Command();
		temp.setIsUserCommand(false);
		Event event = storage.getEventByID(id);
		switch(event.getType()){
			case Constants.TASK_TYPE_NUMBER:
				temp.setType(Constants.COMMAND_TYPE.ADD_TASK);
				break;
			case Constants.SCHEDULE_TYPE_NUMBER:
				temp.setType(Constants.COMMAND_TYPE.ADD_SCHEDULE);
				break;
			case Constants.DEADLINE_TYPE_NUMBER:
				temp.setType(Constants.COMMAND_TYPE.ADD_DEADLINE);
				break;
			default:
				break;
		}
		temp.setEvent(event);
		return temp;
		
	}
	
	

}