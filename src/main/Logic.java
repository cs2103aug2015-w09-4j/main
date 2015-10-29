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
	/**
	 * Displays all events stored within TangGuo
	 * @return
	 */
	public ArrayList<ArrayList<Event>> updateDisplay(){
		ArrayList<ArrayList<Event>> displayEvent = new ArrayList<ArrayList<Event>>();
		/*
		if (allCachesEmpty()){
			return String.format(Constants.TANGGUO_EMPTY_FILE, fileName);
		}*/
		TGIDMap.clear();
		displayEvent.add(displayCache("Tasks", storage.getTaskCache(),"t"));
		displayEvent.add(displayCache("Deadlines", storage.getDeadlineCache(),"d"));
		displayEvent.add(displayCache("Schedules", storage.getScheduleCache(),"s"));

		return displayEvent;
	}
	/**
	 * requests for user's inputs and executes the commands given, showing the
	 * result back to the user
	 */

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
	public Command executeInputs(String input) {
		Command currentCommand;
		Command returnedCommand = new Command();
		try {
			currentCommand = Parser.parseCommand(input);
		} catch (ParseException e) {
			logger.writeException(e.toString());
			returnedCommand.setDisplayMessage(Constants.TANGGUO_DATE_OUT_OF_BOUNDS);
			return returnedCommand;
		} catch (NumberFormatException e) {
			logger.writeException(e.toString());
			returnedCommand.setDisplayMessage(Constants.TANGGUO_INVALID_DATE);
			return returnedCommand;
		} catch (IndexOutOfBoundsException e){
			logger.writeException(e.toString());
			returnedCommand.setDisplayMessage(Constants.TANGGUO_INVALID_COMMAND);
			return returnedCommand;
		} catch (AbnormalScheduleTimeException e) {
			logger.writeException(e.toString());
			returnedCommand.setDisplayMessage(Constants.TANGGUO_INVALID_SCHEDULE);
			return returnedCommand;
		}
		returnedCommand = executeProcessedCommand(currentCommand);
		return returnedCommand;
	}

	/**
	 * Decides which method to be executed based on parsed command
	 * @param command
	 * @return result indicating success/failure of command
	 */
	private Command executeProcessedCommand(Command command){
		switch (command.getType()) {
			case ADD_DEADLINE:
				return addDeadline(command);
			case ADD_SCHEDULE:
				return addSchedule(command);
			case ADD_TASK:
				return addTask(command);
			case DISPLAY:
				//return displayTangGuo();
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
			case EXIT:
				showToUser(Constants.TANGGUO_EXIT);
				System.exit(0);
			case INVALID:
				return getErrorCommand(Constants.TANGGUO_INVALID_COMMAND);
			default:
				return getErrorCommand(Constants.TANGGUO_INVALID_COMMAND);

		}
	}

	/**
	 * adds a Deadline event
	 * @param command
	 * @return
	 */
	private Command addDeadline(Command command){
		Command returnedCommand = new Command();
		returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_ADD_SUCCESS, fileName, command.getEventName()));
		if (command.isUserCommand()){						//user command
			int newID = storage.addDeadline(command.getEventName(), command.getEventEnd());
			reversedCommandStack.push(reverseAdd(newID));
		}else{												//undo
			storage.addDeadline(command.getEvent());
		}
		returnedCommand.setDisplayedEventList(updateDisplay());
		return returnedCommand;
	}

	/**
	 * adds a Schedule event
	 * @param command
	 * @return
	 */
	private Command addSchedule(Command command){
		// add into storage
		Command returnedCommand = new Command();
		returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_ADD_SUCCESS, fileName, command.getEventName()));
		if (command.isUserCommand()){
			int newID = storage.addSchedule(command.getEventName(), command.getEventStart(), command.getEventEnd());
			reversedCommandStack.push(reverseAdd(newID));
		}else{
			storage.addSchedule(command.getEvent());
		}
		returnedCommand.setDisplayedEventList(updateDisplay());
		return returnedCommand;
	}

	/**
	 * adds a Floating Task event
	 * @param command
	 * @return
	 */
	private Command addTask(Command command){
		// add into storage
		Command returnedCommand = new Command();
		returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_ADD_SUCCESS, fileName, command.getEventName()));
		if (command.isUserCommand()){
			int newID = storage.addTask(command.getEventName());
			reversedCommandStack.push(reverseAdd(newID));
		}else{
			storage.addTask(command.getEvent());
		}
		returnedCommand.setDisplayedEventList(updateDisplay());
		return returnedCommand;
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
	private Command undo(){
		if (reversedCommandStack.isEmpty()){
			return getErrorCommand(Constants.TANGGUO_UNDO_NO_COMMAND);
		}
		executeProcessedCommand(reversedCommandStack.pop());
		Command returnedCommand = new Command();
		returnedCommand.setDisplayedEventList(updateDisplay());
		returnedCommand.setDisplayMessage(Constants.TANGGUO_UNDO_SUCCESS);
		return returnedCommand;
	}



	//checks if TangGuo has no events stored
	private boolean allCachesEmpty(){
		return(storage.getDeadlineCache().isEmpty() && storage.getTaskCache().isEmpty()
				&& storage.getScheduleCache().isEmpty());
	}

	//Displays all events of a particular type: deadline/schedule/floating task
	private ArrayList<Event> displayCache(String cacheName, ArrayList<Event> cache, String header){
		ArrayList<Event> temp = new ArrayList<Event>();
		for (int i = 0; i < cache.size(); i++){
			TGIDMap.put(header+(i+1), cache.get(i).getID());

			temp.add(cache.get(i));
		}
		return temp;
	}

	/**
	 * Updates the name of an existing event to the new name input by user
	 * @param command
	 * @return
	 */
	private Command updateName(Command command){
		Command returnedCommand = new Command();

		String newName = command.getEventName();
		String displayedIndex = command.getDisplayedIndex();
		int taskID = -1;

		if (TGIDMap.containsKey(displayedIndex)){
			taskID = TGIDMap.get(displayedIndex);
		}else{
			return getErrorCommand(Constants.TANGGUO_INVALID_INDEX);
		}

		String oldName = storage.getEventByID(taskID).getName();

		if (command.isUserCommand()){
			reversedCommandStack.push(reverseUpdateName(taskID, oldName, displayedIndex));
		}
		storage.updateNameByID(taskID, newName);
		returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_UPDATE_NAME_SUCCESS, oldName,
				newName));
		returnedCommand.setDisplayedEventList(updateDisplay());
		return returnedCommand;


	}

	private Command getErrorCommand(String ErrorMessage){
		Command c = new Command();
		c.setDisplayMessage(ErrorMessage);
		return c;
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

	private Command updateStart(Command command) {
		Command returnedCommand = new Command();
		Date startDate = command.getEventStart();
		String displayedIndex = command.getDisplayedIndex();
		int taskID = -1;

		if (TGIDMap.containsKey(displayedIndex)){
			taskID = TGIDMap.get(displayedIndex);
		}else{
			return getErrorCommand(Constants.TANGGUO_INVALID_INDEX);
		}

		Date oldStart = storage.getEventByID(taskID).getStart();

		if (storage.updateStartByID(taskID, startDate)) {
			if (command.isUserCommand()){
				reversedCommandStack.push(reverseUpdateStart(taskID, oldStart, displayedIndex));
			}
			returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_UPDATE_START_SUCCESS, storage.getEventByID(taskID).getName(),
					startDate.toString()));
			returnedCommand.setDisplayedEventList(updateDisplay());
			return returnedCommand;
		} else {
			return getErrorCommand(Constants.TANGGUO_UPDATE_START_FAIL);
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

	private Command updateEnd(Command command) {
		Date endDate = command.getEventEnd();
		Command returnedCommand = new Command();
		String displayedIndex = command.getDisplayedIndex();
		int taskID = -1;

		if (TGIDMap.containsKey(displayedIndex)){
			taskID = TGIDMap.get(displayedIndex);
		}else{
			return getErrorCommand(Constants.TANGGUO_INVALID_INDEX);
		}

		Date oldEnd = storage.getEventByID(taskID).getEnd();

		System.out.println(endDate.toString());

		if (storage.updateEndByID(taskID, endDate)) {
			if (command.isUserCommand()){
				reversedCommandStack.push(reverseUpdateEnd(taskID, oldEnd, displayedIndex));
			}
			returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_UPDATE_END_SUCCESS, storage.getEventByID(taskID).getName(),
					endDate.toString()));
			returnedCommand.setDisplayedEventList(updateDisplay());
			return returnedCommand;
		} else {
			return getErrorCommand(Constants.TANGGUO_UPDATE_END_FAIL);
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


	private Command updatePriority(Command command) {
		int priority = command.getEventPriority();
		String displayedIndex = command.getDisplayedIndex();
		Command returnedCommand = new Command();
		int taskID = -1;

		if (TGIDMap.containsKey(displayedIndex)){
			taskID = TGIDMap.get(displayedIndex);
		}else{
			return getErrorCommand(Constants.TANGGUO_INVALID_INDEX);
		}

		int oldPriority = storage.getEventByID(taskID).getPriority();

		if (command.isUserCommand()){
			reversedCommandStack.push(reverseUpdatePriority(taskID, oldPriority, displayedIndex));
		}
		storage.updatePriorityByID(taskID, priority);
		returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_UPDATE_PRIORITY_SUCCESS, storage.getEventByID(taskID).getName(),
				priority));
		returnedCommand.setDisplayedEventList(updateDisplay());
		return returnedCommand;
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

	private Command updateCategory(Command command) {
		String newCategory = command.getEventCategory();
		String displayedIndex = command.getDisplayedIndex();
		int taskID = -1;
		Command returnedCommand = new Command();

		if (TGIDMap.containsKey(displayedIndex)){
			taskID = TGIDMap.get(displayedIndex);
		}else{
			return getErrorCommand(Constants.TANGGUO_INVALID_INDEX);
		}

		String oldCategory = storage.getEventByID(taskID).getCategory();

		if (command.isUserCommand()){
			reversedCommandStack.push(reverseUpdateCategory(taskID, oldCategory, displayedIndex));
		}
		storage.updateCategoryByID(taskID, newCategory);
		returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_UPDATE_CATEGORY_SUCCESS, storage.getEventByID(taskID).getName(),
				newCategory));
		returnedCommand.setDisplayedEventList(updateDisplay());
		return returnedCommand;
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
	private Command deleteEvent(Command command) {
		int IDToDelete;
		Command returnedCommand = new Command();
		if (command.isUserCommand()){
			if (TGIDMap.containsKey(command.getDisplayedIndex())){
				IDToDelete = TGIDMap.get(command.getDisplayedIndex());
			} else {
				return getErrorCommand(Constants.TANGGUO_INVALID_INDEX);
			}
			reversedCommandStack.push(reverseDeleteEvent(IDToDelete));
		}else{
			IDToDelete = command.getEventID();
		}
		Event deletedEvent = storage.deleteEventByID(IDToDelete);
		returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_DELETE_SUCCESS, fileName, deletedEvent.getName()));
		returnedCommand.setDisplayedEventList(updateDisplay());
		return returnedCommand;
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

	private Command sortName() {
		storage.sortName();
		Command returnedCommand = new Command();
		returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_SORT_SUCCESS, "NAME"));
		returnedCommand.setDisplayedEventList(updateDisplay());
		return returnedCommand;
	}

	private Command sortStart() {
		storage.sortStart();
		Command returnedCommand = new Command();
		returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_SORT_SUCCESS, "START DATE"));
		returnedCommand.setDisplayedEventList(updateDisplay());
		return returnedCommand;
	}

	private Command sortEnd() {
		storage.sortEnd();
		Command returnedCommand = new Command();
		returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_SORT_SUCCESS, "END DATE"));
		returnedCommand.setDisplayedEventList(updateDisplay());
		return returnedCommand;
	}

	private Command sortPriority() {
		storage.sortPriority();
		Command returnedCommand = new Command();
		returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_SORT_SUCCESS, "PRIORITY"));
		returnedCommand.setDisplayedEventList(updateDisplay());
		return returnedCommand;
	}
}