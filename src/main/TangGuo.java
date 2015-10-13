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
	
	/**
	 * Initialization of TGStorageManager, TGIDMap, and reversedCommandStack
	 * @param file
	 */
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
	
	/**
	 * requests for user's inputs and executes the commands given, showing the 
	 * result back to the user
	 */
	private void runUserInput(){
		requestInput();
		String input = scanner.nextLine();
		String output = executeinputs(input);
		showToUser(output);
	}
	
	//display to user
	private static void showToUser(String display) {
		System.out.println(display);
	}
	
	//request input from the user
	private static void requestInput() {
		System.out.print("input: ");
	}
	
	/**
	 * Parses input String and returns the result
	 * @param input
	 * @return result indicating success/failure of command
	 */
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
	
	/**
	 * Decides which method to be executed based on parsed command
	 * @param command
	 * @return result indicating success/failure of command
	 */
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
			case UPDATE_NAME:
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
				assert false:"Unhandled command type:"+command.getType();
				return Constants.TANGGUO_IO_EXCEPTION;
		} 
	}
	
	/**
	 * adds a Deadline event
	 * @param command
	 * @return
	 */
	private String addDeadline(Command command){
		
		if (command.isUserCommand()){						//user command
			int newID = storage.addDeadline(command.getEventName(), command.getEventEnd());
			reversedCommandStack.push(reverseAdd(newID));
		}else{												//undo
			storage.addDeadline(command.getEvent());
		}
		return String.format(Constants.TANGGUO_ADD_SUCCESS, fileName, command.getEventName());
	}
	
	/**
	 * adds a Schedule event
	 * @param command
	 * @return
	 */
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
	
	/**
	 * adds a Floating Task event
	 * @param command
	 * @return
	 */
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
	
	/**
	 * Deletes a prior Event that was added to TangGuo 
	 * @param id	(should be a Command object of ADD_ type)
	 * @return a Command object that deletes the Command object of the input ID
	 */
	private Command reverseAdd(int id){
		Command temp = new Command();
		temp.setIsUserCommand(false);
		temp.setType(Constants.COMMAND_TYPE.DELETE);
		temp.setEventID(id);
		return temp;
	}
	
	/**
	 * Undoes previous command by user
	 * @return String indicating success/failure of undoing previous command
	 */
	private String undo(){
		if (reversedCommandStack.isEmpty()){
			return Constants.TANGGUO_UNDO_NO_COMMAND;
		}
		executeProcessedCommand(reversedCommandStack.pop());
		return Constants.TANGGUO_UNDO_SUCCESS;
	}
	
	/**
	 * Displays all events stored within TangGuo
	 * @return
	 */
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
	
	//checks if TangGuo has no events stored
	private boolean allCachesEmpty(){
		return(storage.getDeadlineCache().isEmpty() && storage.getTaskCache().isEmpty()
				&& storage.getScheduleCache().isEmpty());			
	}
	
	//Displays all events of a particular type: deadline/schedule/floating task
	private String displayCache(String cacheName, ArrayList<Event> cache, String header){
		String printOut = cacheName + ":\n";
		for (int i = 0; i < cache.size(); i++) {
			TGIDMap.put(header+(i+1), cache.get(i).getID());
			printOut = printOut + (i+1) + ". " + cache.get(i).getName() + "\n";
		}
		return printOut;
	}
	
	/**
	 * Updates the name of an existing event to the new name input by user
	 * @param command
	 * @return
	 */
	private String updateName(Command command) {
		String newName = command.getEventName();
		String displayedIndex = command.getDisplayedIndex();
		int taskID = -1;
		
		try {
			taskID = TGIDMap.get(displayedIndex);
		} catch (NullPointerException e) {
			return Constants.TANGGUO_INVALID_INDEX;
		}
		
		String oldName = storage.getEventByID(taskID).getName();
		if (command.isUserCommand()){
			reversedCommandStack.push(reverseUpdateName(taskID, oldName, displayedIndex));
		}
		storage.updateNameByID(taskID, newName);
		
		return String.format(Constants.TANGGUO_UPDATE_NAME, oldName,
				newName) + "\n" + displayTangGuo();	
	}
	
	/**
	 * Reverts back the name of an event
	 * @param id
	 * @param name
	 * @return a Command object that updates the existing name of the Event
	 * object of "@param id" to "@param name"
	 */
	private Command reverseUpdateName(int id, String name, String displayedIndex){
		Command temp = new Command();
		temp.setIsUserCommand(false);
		temp.setType(Constants.COMMAND_TYPE.UPDATE_NAME);
		temp.setEventName(name);
		temp.setEventID(id);
		temp.setDisplayedIndex(displayedIndex);
		return temp;
	}
	
	/**
	 * deletes an Event
	 * @param toBeDeleted : [letter][number] 
	 * letter refers to the type of event = {t: task, s: schedule, d: deadline};
	 * number refers to index displayed
	 * @return
	 */
	private String deleteEvent(Command command) {
		int IDToDelete;
		if (command.isUserCommand()){
			IDToDelete = TGIDMap.get(command.getDisplayedIndex());
			reversedCommandStack.push(reverseDeleteEvent(IDToDelete));
		} else {
			IDToDelete = command.getEventID();
		}
		Event deletedEvent = storage.deleteEventByID(IDToDelete);

		System.out.println(String.format(Constants.TANGGUO_DELETE_SUCCESS, fileName, deletedEvent.getName()));
		return displayTangGuo();
	}
	
	/**
	 * Add back an event that was deleted previously
	 * @param id
	 * @return a Command Object that adds the object of "@param id" to TangGuo
	 */
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