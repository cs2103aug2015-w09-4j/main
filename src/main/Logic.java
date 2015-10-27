package main;
import java.lang.String;
import java.text.ParseException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Date;
import java.util.Stack;

import javax.swing.Spring;

import jdk.nashorn.internal.runtime.regexp.joni.SearchAlgorithm;


public class Logic {
	
	private static String fileName;
	private Scanner scanner = new Scanner(System.in);
	private TGStorageManager storage;
	private HashMap<String,Integer> TGIDMap;
	private Stack<Command> reversedCommandStack;
	private Logger logger;
	
	/**
	 * Initialization of TGStorageManager, TGIDMap, and reversedCommandStack
	 * @param file
	 */
	public Logic(String file) {
		fileName = file;
		try {
			logger = new Logger("Tangguo.log");
		} catch (IOException e) {
			System.out.println("failed to initiate log");
		}
		storage = new TGStorageManager(fileName);
		TGIDMap = new HashMap<String,Integer>();
		reversedCommandStack = new Stack<Command>();
	}
	
	public static void main(String[] args) throws IOException, ParseException {
		Logic tg = new Logic(args[0]);
		showToUser(String.format(Constants.TANGGUO_START, fileName));
		showToUser(tg.executeInputs("display"));
		
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
		String output = executeInputs(input);
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
	public String executeInputs(String input) {
		Command currentCommand;
		try {
			currentCommand = Parser.parseCommand(input);
		} catch (ParseException e) {
			logger.writeException(e.toString());
			return Constants.TANGGUO_DATE_OUT_OF_BOUNDS;
		} catch (NumberFormatException e) {
			logger.writeException(e.toString());
			return Constants.TANGGUO_INVALID_DATE;
		} catch (IndexOutOfBoundsException e){
			logger.writeException(e.toString());
			return Constants.TANGGUO_INVALID_COMMAND;
		} catch (AbnormalScheduleTimeException e) {
			logger.writeException(e.toString());
			return Constants.TANGGUO_INVALID_SCHEDULE;
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
			case UPDATE_START:
				return updateStart(command);	
			case UPDATE_END:
				return updateEnd(command); 		
			case UPDATE_PRIORITY:
				return updatePriority(command); ///not done yet///
			case UPDATE_CATEGORY:
				return updateCategory(command);
		//	case DONE:
		//		return markAsDone(command);		///not done yet///
			case DELETE:
				return deleteEvent(command);
			case UNDO:
				return undo();
			case SORT_NAME:
				return sortName();
			case SORT_START:
				return sortStart();
			case SORT_END:
				return sortEnd();
			case SORT_PRIORITY:
				return sortPriority();
			case SEARCH:
				return search(command);
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
	private String addTask(Command command){
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
	private String displayTangGuo(){
		String printOut = "";
		
		if (allCachesEmpty()){
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
		for (int i = 0; i < cache.size(); i++){
			TGIDMap.put(header+(i+1), cache.get(i).getID());
			printOut = printOut + (i+1) + ". ";
			
			printOut += cache.get(i).toString();
		}
		return printOut;
	}
	
	/**
	 * Updates the name of an existing event to the new name input by user
	 * @param command
	 * @return
	 */
	private String updateName(Command command){
		String newName = command.getEventName();
		String displayedIndex = command.getDisplayedIndex();
		int taskID = -1;
		
		if (TGIDMap.containsKey(displayedIndex)){
			taskID = TGIDMap.get(displayedIndex);
		}else{
			return Constants.TANGGUO_INVALID_INDEX;
		}
		
		String oldName = storage.getEventByID(taskID).getName();
		
		if (command.isUserCommand()){
			reversedCommandStack.push(reverseUpdateName(taskID, oldName, displayedIndex));
		}
		storage.updateNameByID(taskID, newName);
		
		return String.format(Constants.TANGGUO_UPDATE_NAME_SUCCESS, oldName,
				newName) + Constants.NEW_LINE + displayTangGuo();	
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
	
	private String updateStart(Command command) {
		Date startDate = command.getEventStart();
		String displayedIndex = command.getDisplayedIndex();
		int taskID = -1;
		
		if (TGIDMap.containsKey(displayedIndex)){
			taskID = TGIDMap.get(displayedIndex);
		}else{
			return Constants.TANGGUO_INVALID_INDEX;
		}
		
		Date oldStart = storage.getEventByID(taskID).getStart();
		
		if (storage.updateStartByID(taskID, startDate)) {
			if (command.isUserCommand()){
				reversedCommandStack.push(reverseUpdateStart(taskID, oldStart, displayedIndex));
			}
			return String.format(Constants.TANGGUO_UPDATE_START_SUCCESS, storage.getEventByID(taskID).getName(),
					startDate.toString()) + Constants.NEW_LINE + displayTangGuo();
		} else {
			return Constants.TANGGUO_UPDATE_START_FAIL;
		}
	}
	
	/**
	 * Reverts back the start date of an event
	 * @param id
	 * @param startDate
	 * @return a Command object that updates the existing start date of the Event
	 * object of "@param id" to "@param startDate"
	 */
	private Command reverseUpdateStart(int id, Date startDate, String displayedIndex){
		Command temp = new Command();
		temp.setIsUserCommand(false);
		temp.setType(Constants.COMMAND_TYPE.UPDATE_START);
		temp.setEventStart(startDate);
		temp.setEventID(id);
		temp.setDisplayedIndex(displayedIndex);
		return temp;
	}
	
	private String updateEnd(Command command) {
		Date endDate = command.getEventEnd();
		String displayedIndex = command.getDisplayedIndex();
		int taskID = -1;
		
		if (TGIDMap.containsKey(displayedIndex)){
			taskID = TGIDMap.get(displayedIndex);
		}else{
			return Constants.TANGGUO_INVALID_INDEX;
		}
		
		Date oldEnd = storage.getEventByID(taskID).getEnd();
		
		if (storage.updateEndByID(taskID, endDate)) {
			if (command.isUserCommand()){
				reversedCommandStack.push(reverseUpdateEnd(taskID, oldEnd, displayedIndex));
			}
			return String.format(Constants.TANGGUO_UPDATE_END_SUCCESS, storage.getEventByID(taskID).getName(),
					endDate.toString()) + Constants.NEW_LINE + displayTangGuo();
		} else {
			return Constants.TANGGUO_UPDATE_END_FAIL;
		}
	}
	
	/**
	 * Reverts back the end date of an event
	 * @param id
	 * @param endDate
	 * @return a Command object that updates the existing end date of the Event
	 * object of "@param id" to "@param endDate"
	 */
	private Command reverseUpdateEnd(int id, Date endDate, String displayedIndex){
		Command temp = new Command();
		temp.setIsUserCommand(false);
		temp.setType(Constants.COMMAND_TYPE.UPDATE_END);
		temp.setEventEnd(endDate);
		temp.setEventID(id);
		temp.setDisplayedIndex(displayedIndex);
		return temp;
	}
	
	
	private String updatePriority(Command command) {
		int priority = command.getEventPriority();
		String displayedIndex = command.getDisplayedIndex();
		int taskID = -1;
		
		if (TGIDMap.containsKey(displayedIndex)){
			taskID = TGIDMap.get(displayedIndex);
		}else{
			return Constants.TANGGUO_INVALID_INDEX;
		}
		
		int oldPriority = storage.getEventByID(taskID).getPriority();
		
		if (command.isUserCommand()){
			reversedCommandStack.push(reverseUpdatePriority(taskID, oldPriority, displayedIndex));
		}
		storage.updatePriorityByID(taskID, priority);
		
		return String.format(Constants.TANGGUO_UPDATE_PRIORITY_SUCCESS, storage.getEventByID(taskID).getName(),
				priority) + Constants.NEW_LINE + displayTangGuo();
	}

	/**
	 * Reverts back the priority of an event
	 * @param id
	 * @param priority
	 * @return a Command object that updates the existing name of the Event
	 * object of "@param id" to "@param priority"
	 */
	private Command reverseUpdatePriority(int id, int priority, String displayedIndex){
		Command temp = new Command();
		temp.setIsUserCommand(false);
		temp.setType(Constants.COMMAND_TYPE.UPDATE_NAME);
		temp.setEventPriority(priority);
		temp.setEventID(id);
		temp.setDisplayedIndex(displayedIndex);
		return temp;
	}
	
	private String updateCategory(Command command) {
		String newCategory = command.getEventCategory();
		String displayedIndex = command.getDisplayedIndex();
		int taskID = -1;
		
		if (TGIDMap.containsKey(displayedIndex)){
			taskID = TGIDMap.get(displayedIndex);
		}else{
			return Constants.TANGGUO_INVALID_INDEX;
		}
		
		String oldCategory = storage.getEventByID(taskID).getCategory();
		
		if (command.isUserCommand()){
			reversedCommandStack.push(reverseUpdateCategory(taskID, oldCategory, displayedIndex));
		}
		storage.updateCategoryByID(taskID, newCategory);
		
		return String.format(Constants.TANGGUO_UPDATE_CATEGORY_SUCCESS, storage.getEventByID(taskID).getName(),
				newCategory) + Constants.NEW_LINE + displayTangGuo();	
	}
	
	private Command reverseUpdateCategory(int id, String category, String displayedIndex) {
		Command temp = new Command();
		temp.setIsUserCommand(false);
		temp.setType(Constants.COMMAND_TYPE.UPDATE_CATEGORY);
		temp.setEventCategory(category);
		temp.setEventID(id);
		temp.setDisplayedIndex(displayedIndex);
		return temp;
	}
	/*
	private String markAsDone(Command command){
		String displayedIndex = command.getDisplayedIndex();
		int taskID = -1;
		
		if (TGIDMap.containsKey(displayedIndex)){
			taskID = TGIDMap.get(displayedIndex);
		}else{
			return Constants.TANGGUO_INVALID_INDEX;
		}
		
		if (command.isUserCommand()){
			reversedCommandStack.push(reverseMarkAsDone(taskID));
			storage.updateIsDoneByID(taskID, true);
		} else {
			storage.updateIsDoneByID(taskID, false);
		}	
		
		return String.format(Constants.TANGGUO_UPDATE_DONE_SUCCESS, storage.getEventByID(taskID).getName())
				+ Constants.NEW_LINE + displayTangGuo();
	}
	
	
	private Command reverseMarkAsDone(int id){
		Command temp = new Command();
		temp.setIsUserCommand(false);
		temp.setType(Constants.COMMAND_TYPE.DONE);
		temp.setEventID(id);
		return temp;
	}
	*/
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
			if (TGIDMap.containsKey(command.getDisplayedIndex())){
				IDToDelete = TGIDMap.get(command.getDisplayedIndex());
			} else {
				return Constants.TANGGUO_INVALID_INDEX;
			}
			reversedCommandStack.push(reverseDeleteEvent(IDToDelete));
		}else{
			IDToDelete = command.getEventID();
		}
		Event deletedEvent = storage.deleteEventByID(IDToDelete);

		return String.format(Constants.TANGGUO_DELETE_SUCCESS, fileName, deletedEvent.getName()) +
				Constants.NEW_LINE + displayTangGuo();
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
				assert false:"unexpected type number";
				break;
		}
		temp.setEvent(event);
		return temp;
	}
	
	private String sortName() {
		storage.sortName();
		return String.format(Constants.TANGGUO_SORT_SUCCESS, "NAME") + Constants.NEW_LINE + displayTangGuo();
	}
	
	private String sortStart() {
		storage.sortStart();
		return String.format(Constants.TANGGUO_SORT_SUCCESS, "START DATE") + Constants.NEW_LINE + displayTangGuo();
	}
	
	private String sortEnd() {
		storage.sortEnd();
		return String.format(Constants.TANGGUO_SORT_SUCCESS, "END DATE") + Constants.NEW_LINE + displayTangGuo();
	}
	
	private String sortPriority() {
		storage.sortPriority();
		return String.format(Constants.TANGGUO_SORT_SUCCESS, "PRIORITY") + Constants.NEW_LINE + displayTangGuo();
	}
	
	private String search(Command command) {
		String printOut = "";
		
		ArrayList<Event> task = storage.searchTask(command.getSearchKey());
		ArrayList<Event> deadline = storage.searchDeadline(command.getSearchKey());
		ArrayList<Event> schedule = storage.searchSchedule(command.getSearchKey());
		
		if (task.isEmpty() && deadline.isEmpty() && schedule.isEmpty()) {
			return String.format(Constants.TANGGUO_SEARCH_FAIL, command.getSearchKey());
		}
		TGIDMap.clear();
		printOut += String.format(Constants.TANGGUO_SEARCH_SUCCESS, command.getSearchKey()) + Constants.NEW_LINE;
		printOut += displayCache("Tasks", task,"t");
		printOut += displayCache("Deadlines", deadline,"d");
		printOut += displayCache("Schedules", schedule,"s");
		
		return printOut;
	}
}