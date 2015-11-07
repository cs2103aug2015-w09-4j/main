package TGLogic;
import java.lang.String;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.Stack;

import TGExceptions.AbnormalScheduleTimeException;
import TGExceptions.TaskDateExistenceException;
import TGParser.Parser;
import TGStorage.TGStorageManager;
import TGUtils.Command;
import TGUtils.Constants;
import TGUtils.Event;
import TGUtils.Logger;

public class Logic {

	private static String fileName;
	private TGStorageManager storage;
	private Config config;
	private HashMap<String,Integer> TGIDMap;
	private Stack<Command> reversedCommandStack;
	private Logger logger;
	private String lastSearchKey;
	private boolean showDoneEvent;
	/**
	 * Initialization of TGStorageManager, TGIDMap, and reversedCommandStack
	 */
	public Logic() {
		//get file pathe from Config class
		config = new Config();
		fileName = config.getFileName();
		try {
			logger = new Logger("Tangguo.log");
		} catch (IOException e) {
			System.out.println("Failed to initiate log");
		}
		storage = new TGStorageManager(config.getFilePath(), fileName);
		TGIDMap = new HashMap<String,Integer>();
		showDoneEvent = false;
		reversedCommandStack = new Stack<Command>();
	}

	/**
	 * @return all events that matches the previous search key stored within TangGuo
	 */
	public ArrayList<ArrayList<Event>> updateSearchDisplay(){
		if (lastSearchKey==null){
			return null; //if there is no previous search key
		}else{
			return updateSearchResult(lastSearchKey);
		}
	}
	/**
	 * @return all today's events stored within TangGuo
	 */
	public ArrayList<ArrayList<Event>> updateTodayDisplay(){
		ArrayList<ArrayList<Event>> displayEvent = new ArrayList<ArrayList<Event>>();
		TGIDMap.clear();
		displayEvent.add(getTodayCache("Tasks", storage.getTaskCache(),"t"));
		displayEvent.add(getTodayCache("Deadlines", storage.getDeadlineCache(),"d"));
		displayEvent.add(getTodayCache("Schedules", storage.getScheduleCache(),"s"));
		return displayEvent;
	}
	/**
	 * @return all events stored within TangGuo
	 */
	public ArrayList<ArrayList<Event>> updateDisplay(){
		ArrayList<ArrayList<Event>> displayEvent = new ArrayList<ArrayList<Event>>();
		TGIDMap.clear();
		displayEvent.add(getCache("Tasks", storage.getTaskCache(),"t"));
		displayEvent.add(getCache("Deadlines", storage.getDeadlineCache(),"d"));
		displayEvent.add(getCache("Schedules", storage.getScheduleCache(),"s"));
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

	/**
	 * Parses input String and returns the result
	 * @param input
	 * @return result indicating success/failure of command
	 */
	public Command executeInputs(String input) {
		Command currentCommand;
		try {
			currentCommand = Parser.parseCommand(input);
		} catch (ParseException |TaskDateExistenceException e) {
			logger.writeException(e.toString());
			return getErrorCommand(Constants.TANGGUO_DATE_OUT_OF_BOUNDS);
		} catch (NumberFormatException e) {
			logger.writeException(e.toString());
			return getErrorCommand(Constants.TANGGUO_INVALID_DATE);
		} catch (IndexOutOfBoundsException e){
			logger.writeException(e.toString());
			return getErrorCommand(Constants.TANGGUO_INVALID_COMMAND);
		} catch (AbnormalScheduleTimeException e) {
			logger.writeException(e.toString());
			return getErrorCommand(Constants.TANGGUO_INVALID_SCHEDULE);
		}
		Command returnedCommand = executeProcessedCommand(currentCommand);
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
			case UPDATE_NAME:
				return updateName(command);
			case UPDATE_START:
				return updateStart(command);
			case UPDATE_END:
				return updateEnd(command);
			case UPDATE_PRIORITY:
				return updatePriority(command);
			case UPDATE_CATEGORY:
				return updateCategory(command);
			case DONE:
				return markAsDone(command);
			case DELETE:
				return deleteEvent(command);
			case TOGGLE:
				return toggleDoneDisplay();
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
			case PATH:
				return setPath(command);
			case IMPORT:
				return importData(command);
			case EXIT:
				showToUser(Constants.TANGGUO_EXIT);
				System.exit(0);
			case INVALID:
				return getErrorCommand(Constants.TANGGUO_INVALID_COMMAND);
			default:
				return getErrorCommand(Constants.TANGGUO_INVALID_COMMAND);

		}
	}

	//toggle the boolean showDoneEvent, which indicates whether completed event is hidden/displayed
	private Command toggleDoneDisplay() {
		Command returnedCommand = new Command();
		showDoneEvent = !showDoneEvent;
		if (showDoneEvent){
			returnedCommand.setDisplayMessage(Constants.TANGGUO_SHOW_DONE);
		}else{
			returnedCommand.setDisplayMessage(Constants.TANGGUO_HIDE_DONE);
		}
		returnedCommand.setDisplayedEventList(updateDisplay());
		return returnedCommand;
	}
	/**
	 * adds a Deadline event
	 */
	private Command addDeadline(Command command){
		Command returnedCommand = new Command();
		returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_ADD_SUCCESS, fileName, command.getEventName()));
		if (command.isUserCommand()){
			int newID = storage.addDeadline(command.getEventName(), command.getEventEnd(), command.getEventCategory(), command.getEventPriority());
			reversedCommandStack.push(reverseAdd(newID));
		}else{
			storage.addDeadlineToStorage(command.getEvent());
		}

		returnedCommand.setDisplayedEventList(updateDisplay());
		return returnedCommand;
	}

	/**
	 * adds a Schedule event
	 */
	private Command addSchedule(Command command){
		Command returnedCommand = new Command();
		returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_ADD_SUCCESS, fileName, command.getEventName()));
		if (command.isUserCommand()){
			int newID = storage.addSchedule(command.getEventName(), command.getEventStart(), command.getEventEnd(), command.getEventCategory(), command.getEventPriority());
			reversedCommandStack.push(reverseAdd(newID));
		}else{
			storage.addScheduleToStorage(command.getEvent());
		}
		returnedCommand.setDisplayedEventList(updateDisplay());
		return returnedCommand;
	}

	/**
	 * adds a Floating Task event
	 */
	private Command addTask(Command command){
		Command returnedCommand = new Command();
		returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_ADD_SUCCESS, fileName, command.getEventName()));
		if (command.isUserCommand()){
			int newID = storage.addTask(command.getEventName(), command.getEventCategory(), command.getEventPriority());
			reversedCommandStack.push(reverseAdd(newID));
		}else{
			storage.addTaskToStorage(command.getEvent());
		}
		returnedCommand.setDisplayedEventList(updateDisplay());
		return returnedCommand;
	}

	/**
	 * Deletes a prior Event that was added to TangGuo
	 * @param id
	 * @return a Command object that deletes the Command object of the input ID
	 */
	private Command reverseAdd(int id){
		Command temp = new Command();
		temp.setIsUserCommand(false); //
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

	//Displays all events of a particular type: deadline/schedule/floating task
	private ArrayList<Event> getCache(String cacheName, ArrayList<Event> cache, String header){
		ArrayList<Event> temp = new ArrayList<Event>();
		int counter = 1;
		for (int i = 0; i < cache.size(); i++){
			if (cache.get(i).isDone() && !showDoneEvent) continue;
			TGIDMap.put(header+(counter++), cache.get(i).getID());
			temp.add(cache.get(i));
		}
		return temp;
	}
	//return a list of event from the given cache
	private ArrayList<Event> getTodayCache(String cacheName, ArrayList<Event> cache, String header){
		ArrayList<Event> temp = new ArrayList<Event>();
		int counter = 1;
		for (int i = 0; i < cache.size(); i++){
			if (cache.get(i).isDone() && !showDoneEvent) continue;
			if (isTodayEvent(cache.get(i))){
				TGIDMap.put(header+(counter++), cache.get(i).getID()); //<type><number> is mapped to ID
				temp.add(cache.get(i));
			}
		}
		return temp;
	}
	// check whether an event belongs to today
	private boolean isTodayEvent(Event event){
		Date today = new Date();
		if (event.getType()==Constants.TASK_TYPE_NUMBER){ //tasks are always today's event
			return true;
		}else if (event.getType()==Constants.DEADLINE_TYPE_NUMBER){ //deadline is today's if end date is on today
			return isSameDay(today,event.getEnd());
		}else if (event.getType()==Constants.SCHEDULE_TYPE_NUMBER){
			//deadline is today's if end date or start date is on today
			return isSameDay(today,event.getEnd()) || isSameDay(today,event.getStart());
		}else{
			return false;
		}
	}
	//return true if the 2 dates are on the same day, false otherwise
	private boolean isSameDay(Date date1, Date date2){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(date1).equals(sdf.format(date2));
	}

	/**
	 * Updates the name of an existing event to the new name input by user
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
		returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_UPDATE_NAME_SUCCESS, oldName,newName));
		returnedCommand.setDisplayedEventList(updateDisplay());
		return returnedCommand;
	}
	//return an ERROR command which tells the GUI the command is invalid
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
		if (priority == -1) {
			return getErrorCommand(Constants.TANGGUO_INVALID_PRIORITY);
		}
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

	//return a command that is the reverse of the UPDATE command
	//the returned command is a UPDATE command with
	private Command reverseUpdateCategory(int id, String category, String displayedIndex) {
		Command temp = new Command();
		temp.setIsUserCommand(false);
		temp.setType(Constants.COMMAND_TYPE.UPDATE_CATEGORY);
		temp.setEventCategory(category);
		temp.setEventID(id);
		temp.setDisplayedIndex(displayedIndex);
		return temp;
	}

	private Command markAsDone(Command command){
		Command returnedCommand = new Command();
		int taskID;
		if (command.isUserCommand()){//if it is a user's command, set the event as "done"
			String displayedIndex = command.getDisplayedIndex();
			taskID = -1;
			if (TGIDMap.containsKey(displayedIndex)){
				taskID = TGIDMap.get(displayedIndex);
			}else{
				return getErrorCommand(Constants.TANGGUO_INVALID_INDEX);
			}
			reversedCommandStack.push(reverseMarkAsDone(taskID));
			storage.updateIsDoneByID(taskID, true);
		} else { //if it is an "undo" command, set the event as "not done"
			taskID = command.getEventID();
			storage.updateIsDoneByID(taskID, false);
		}
		returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_UPDATE_DONE_SUCCESS, storage.getEventByID(taskID).getName()));
		returnedCommand.setDisplayedEventList(updateDisplay());
		return returnedCommand;
	}

	//return the reverse of the DONE command, which is the same DONE command
	//but need to set IsUserCommand as false
	private Command reverseMarkAsDone(int id){
		Command temp = new Command();
		temp.setIsUserCommand(false);
		temp.setType(Constants.COMMAND_TYPE.DONE);
		temp.setEventID(id);
		return temp;
	}

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
	 * @return the reverse of a DELETE command, which is an ADD command
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
	//sort storage by name (ascending order)
	private Command sortName() {
		storage.sortName();
		Command returnedCommand = new Command();
		returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_SORT_SUCCESS, "NAME"));
		returnedCommand.setDisplayedEventList(updateDisplay());
		return returnedCommand;
	}
	//sort storage by start date (ascending order)
	private Command sortStart() {
		storage.sortStart();
		Command returnedCommand = new Command();
		returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_SORT_SUCCESS, "START DATE"));
		returnedCommand.setDisplayedEventList(updateDisplay());
		return returnedCommand;
	}
	//sort storage by end date (ascending order)
	private Command sortEnd() {
		storage.sortEnd();
		Command returnedCommand = new Command();
		returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_SORT_SUCCESS, "END DATE"));
		returnedCommand.setDisplayedEventList(updateDisplay());
		return returnedCommand;
	}
	//sort storage by priority (ascending order)
	private Command sortPriority() {
		storage.sortPriority();
		Command returnedCommand = new Command();
		returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_SORT_SUCCESS, "PRIORITY"));
		returnedCommand.setDisplayedEventList(updateDisplay());
		return returnedCommand;
	}
	//search through the storage for event containing the keyword
	private Command search(Command command) {
		lastSearchKey = command.getSearchKey(); //record down as the most recent keyword
		ArrayList<ArrayList<Event>> displayedEvent = updateSearchResult(lastSearchKey);
		if (displayedEvent==null) { //no search result
			return getErrorCommand(String.format(Constants.TANGGUO_SEARCH_FAIL, command.getSearchKey()));
		}
		TGIDMap.clear();
		Command returnedCommand = new Command();
		returnedCommand.setDisplayedTab(Constants.SEARCH_TAB_NUMBER); //GUI needs to switch to SEARCH tab
		returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_SEARCH_SUCCESS, command.getSearchKey()));
		returnedCommand.setDisplayedEventList(displayedEvent);
		return returnedCommand;
	}
	//return the list of events that match the searchKey
	private ArrayList<ArrayList<Event>> updateSearchResult(String searchKey){
		ArrayList<Event> task = storage.searchTask(searchKey);
		ArrayList<Event> deadline = storage.searchDeadline(searchKey);
		ArrayList<Event> schedule = storage.searchSchedule(searchKey);
		if (task.isEmpty() && deadline.isEmpty() && schedule.isEmpty()) { //no result
			return null;
		}
		TGIDMap.clear();
		ArrayList<ArrayList<Event>> displayEvent = new ArrayList<ArrayList<Event>>();
		TGIDMap.clear();
		displayEvent.add(getCache("Tasks", task,"t"));
		displayEvent.add(getCache("Deadlines", deadline,"d"));
		displayEvent.add(getCache("Schedules", schedule,"s"));
		return displayEvent;
	}

	private Command setPath(Command command) {
		config.setFilePath(command.getPath());
		storage.setFilePath(command.getPath());
		config.writeConfig();
		Command returnedCommand = new Command();
		returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_PATH_SET, fileName, command.getPath()));
		return returnedCommand;
	}

	private Command importData(Command command) {
		int div = command.getPath().lastIndexOf("/");
		String filePath = command.getPath().substring(0, div + 1);
		fileName = command.getPath().substring(div + 1);
		config.setFilePath(filePath);
		config.setFileName(fileName);
		config.writeConfig();
		storage = new TGStorageManager(filePath, fileName);
		filePath = (filePath.equals(""))? "default" : filePath;
		Command returnedCommand = new Command();
		returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_IMPORT_SUCCESS, filePath));
		returnedCommand.setDisplayedEventList(updateDisplay());
		return returnedCommand;
	}
}