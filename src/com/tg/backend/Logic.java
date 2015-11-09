package com.tg.backend;

import java.lang.String;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.Stack;

import com.tg.parser.Parser;

import TGUtils.Command;
import TGUtils.Constants;
import TGUtils.Event;
import TGUtils.Logger;

public class Logic {

	private static String fileName;
	private TGStorageManager storage;
	private Config config;
	private HashMap<String, Integer> TGIDMap;
	private Stack<Command> reversedCommandStack;
	private Logger logger;
	private String lastSearchKey;
	private boolean showDoneEvents;

	/**
	 * Initialization of TGStorageManager, TGIDMap, and reversedCommandStack
	 */
	public Logic() {
		// get file path from Config class
		config = new Config();
		fileName = config.getFileName();
		logger = new Logger(Constants.LOG_FILE);
		storage = new TGStorageManager(config.getFilePath(), fileName);
		TGIDMap = new HashMap<String, Integer>();
		showDoneEvents = false;
		reversedCommandStack = new Stack<Command>();
	}

	/**
	 * @return an ArrayList of all events that matches the previous search
	 *         key stored within TangGuo
	 */
	public ArrayList<ArrayList<Event>> updateSearchDisplay() {
		if (lastSearchKey == null) {
			return null; // if there is no previous search key
		} else {
			return updateSearchResult(lastSearchKey);
		}
	}

	/**
	 * @return an ArrayList of all today's events stored within TangGuo
	 */
	public ArrayList<ArrayList<Event>> updateTodayDisplay() {
		ArrayList<ArrayList<Event>> displayEvent = new ArrayList<ArrayList<Event>>();
		TGIDMap.clear();
		displayEvent.add(getTodayCache(storage.getTaskCache(), Constants.TASK_IDENTITY));
		displayEvent.add(getTodayCache(storage.getDeadlineCache(), Constants.DEADLINE_IDENTITY));
		displayEvent.add(getTodayCache(storage.getScheduleCache(), Constants.SCHEDULE_IDENTITY));
		return displayEvent;
	}

	/**
	 * @return an ArrayList of all events stored within TangGuo
	 */
	public ArrayList<ArrayList<Event>> updateDisplay() {
		ArrayList<ArrayList<Event>> displayEvent = new ArrayList<ArrayList<Event>>();
		TGIDMap.clear();
		displayEvent.add(getCache(storage.getTaskCache(), Constants.TASK_IDENTITY));
		displayEvent.add(getCache(storage.getDeadlineCache(), Constants.DEADLINE_IDENTITY));
		displayEvent.add(getCache(storage.getScheduleCache(), Constants.SCHEDULE_IDENTITY));
		return displayEvent;
	}

	/**
	 * Parses the input string into a Command object and executes the Command accordingly
	 * @param input
	 * @return a Command object to GUI specifying what to display
	 */
	public Command executeInputs(String input) {
		Command currentCommand;
		try {
			currentCommand = Parser.parseCommand(input);
		} catch (ParseException e) {
			logger.writeException(e.toString());
			return getErrorCommand(Constants.TANGGUO_DATE_OUT_OF_BOUNDS);
		} catch (NumberFormatException e) {
			logger.writeException(e.toString());
			return getErrorCommand(Constants.TANGGUO_INVALID_DATE);
		} catch (IndexOutOfBoundsException e) {
			logger.writeException(e.toString());
			return getErrorCommand(Constants.TANGGUO_INVALID_COMMAND);
		}
		Command returnedCommand = executeProcessedCommand(currentCommand);
		return returnedCommand;
	}

	/**
	 * Decides which method to be executed based on parsed command
	 * @param command
	 * @return result indicating success/failure of command
	 */
	private Command executeProcessedCommand(Command command) {
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
			System.exit(0);
		case INVALID:
			return getErrorCommand(Constants.TANGGUO_INVALID_COMMAND);
		default:
			return getErrorCommand(Constants.TANGGUO_INVALID_COMMAND);
		}
	}

	/**
	 * Toggles the boolean value showDoneEvents, which indicates whether completed events are
	 * to be hidden/displayed. This toggles the display view to display/hide completed events.
	 */
	private Command toggleDoneDisplay() {
		Command returnedCommand = new Command();
		showDoneEvents = !showDoneEvents;
		if (showDoneEvents) {
			returnedCommand.setDisplayMessage(Constants.TANGGUO_SHOW_DONE);
		} else {
			returnedCommand.setDisplayMessage(Constants.TANGGUO_HIDE_DONE);
		}
		returnedCommand.setDisplayedEventList(updateDisplay());
		return returnedCommand;
	}

	/**
	 * adds a Deadline event
	 */
	private Command addDeadline(Command command) {
		Command returnedCommand = createAddReturnCommand(command);
		if (command.isUserCommand()) {
			int newID = storage.addDeadline(command.getEventName(), command.getEventEnd(),
					command.getEventCategory(), command.getEventPriority());
			reversedCommandStack.push(reverseAdd(newID));
		} else {
			storage.addDeadlineToStorage(command.getEvent());
		}
		returnedCommand.setDisplayedEventList(updateDisplay());
		return returnedCommand;
	}

	/**
	 * adds a Schedule event
	 */
	private Command addSchedule(Command command){
		if(command.getEventStart().compareTo(command.getEventEnd()) >= 0) {
			return getErrorCommand(Constants.TANGGUO_INVALID_SCHEDULE);
		}
		Command returnedCommand = new Command();
		returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_ADD_SUCCESS, fileName, command.getEventName()));
		if (command.isUserCommand()){
			int newID = storage.addSchedule(command.getEventName(), command.getEventStart(), command.getEventEnd(), command.getEventCategory(), command.getEventPriority());
			reversedCommandStack.push(reverseAdd(newID));
		} else {
			storage.addScheduleToStorage(command.getEvent());
		}
		returnedCommand.setDisplayedEventList(updateDisplay());
		return returnedCommand;
	}

	/**
	 * adds a Task event
	 */
	private Command addTask(Command command) {
		Command returnedCommand = createAddReturnCommand(command);
		if (command.isUserCommand()) {
			int newID = storage.addTask(command.getEventName(), command.getEventCategory(),
					command.getEventPriority());
			reversedCommandStack.push(reverseAdd(newID));
		} else {
			storage.addTaskToStorage(command.getEvent());
		}
		returnedCommand.setDisplayedEventList(updateDisplay());
		return returnedCommand;
	}

	/**
	 * Creates a return Command object for the Add functions, setting the display message
	 * @param command
	 * @return Command object with an ADD_SUCCESS display message
	 */
	private Command createAddReturnCommand(Command command) {
		Command returnedCommand = new Command();
		returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_ADD_SUCCESS, fileName,
				command.getEventName()));
		return returnedCommand;
	}
	/**
	 * Deletes a prior event that was added to TangGuo
	 * @param id
	 * @return a Command object that deletes the Event object with the ID @param id
	 */
	private Command reverseAdd(int id) {
		Command temp = new Command();
		temp.setIsUserCommand(false); //
		temp.setType(Constants.COMMAND_TYPE.DELETE);
		temp.setEventID(id);
		return temp;
	}

	/**
	 * Undoes the previous command entered by the user
	 * @return a Command object with display message indicating success of the undo
	 */
	private Command undo() {
		if (reversedCommandStack.isEmpty()) {
			return getErrorCommand(Constants.TANGGUO_UNDO_NO_COMMAND);
		}
		executeProcessedCommand(reversedCommandStack.pop());
		Command returnedCommand = new Command();
		returnedCommand.setDisplayedEventList(updateDisplay());
		returnedCommand.setDisplayMessage(Constants.TANGGUO_UNDO_SUCCESS);
		return returnedCommand;
	}

	/**
	 * @param cache
	 * @param header
	 * @return ArrayList of all Event objects in @param cache, that is to be displayed in TangGuo
	 * 		   according to the toggle setting
	 */
	private ArrayList<Event> getCache(ArrayList<Event> cache, String header) {
		ArrayList<Event> temp = new ArrayList<Event>();
		int counter = 1;
		for (int i = 0; i < cache.size(); i++) {
			if (cache.get(i).isDone() && !showDoneEvents)
				continue;
			TGIDMap.put(header + (counter++), cache.get(i).getID());
			temp.add(cache.get(i));
		}
		return temp;
	}

	/**
	 * @param cache
	 * @param header
	 * @return ArrayList of all Event objects in @param cache that are occurring on the current date,
	 * 		   that is to be displayed in TangGuo according the the toggle setting
	 *
	 */	private ArrayList<Event> getTodayCache(ArrayList<Event> cache, String header) {
		ArrayList<Event> temp = new ArrayList<Event>();
		int counter = 1;
		for (int i = 0; i < cache.size(); i++) {
			if (cache.get(i).isDone() && !showDoneEvents)
				continue;
			if (isTodayEvent(cache.get(i))) {
				TGIDMap.put(header + (counter++), cache.get(i).getID());
				temp.add(cache.get(i));
			}
		}
		return temp;
	}

	/**
	 * All tasks are considered as today's events
	 * Deadlines whereby the end date falls on the current date are considered
	 * today's events
	 * Schedules which time intervals include the current date are considered
	 * today's events
	 * @param event
	 * @return boolean value of whether an event occurs on the current date
	 */
	private boolean isTodayEvent(Event event) {
		Date today = new Date();
		if (event.getType() == Constants.TASK_TYPE_NUMBER) {
			return true;
		} else if (event.getType() == Constants.DEADLINE_TYPE_NUMBER) {
			return isSameDay(today, event.getEnd());
		} else if (event.getType() == Constants.SCHEDULE_TYPE_NUMBER) {
			return isWithinInterval(today, event.getStart(), event.getEnd())
					|| isOnInterval(today, event.getStart(), event.getEnd());
		} else {
			return false;
		}
	}

	// check whether target date is within the interval (start, end)
	private boolean isWithinInterval(Date target, Date start, Date end) {
		return start.before(target) && end.after(target);
	}

	// check whether target date is ON the startDate/endDate
	private boolean isOnInterval(Date target, Date start, Date end) {
		return isSameDay(target, start) || isSameDay(target, end);
	}

	// return true if the 2 dates are on the same day, false otherwise
	private boolean isSameDay(Date date1, Date date2) {
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DAY_FORMAT);
		return sdf.format(date1).equals(sdf.format(date2));
	}

	/**
	 * Updates the name of an existing event to the new name entered by the user
	 * @param command
	 * @return a Command object with the updated display
	 */
	private Command updateName(Command command) {
		Command returnedCommand = new Command();
		String newName = command.getEventName();
		String displayedIndex = command.getDisplayedIndex();
		int taskID = Constants.INVALID_INDICATOR;
		if (TGIDMap.containsKey(displayedIndex)) {
			taskID = TGIDMap.get(displayedIndex);
		} else {
			return getErrorCommand(Constants.TANGGUO_INVALID_INDEX);
		}
		String oldName = storage.getEventByID(taskID).getName();
		if (command.isUserCommand()) {
			reversedCommandStack.push(reverseUpdateName(taskID, oldName, displayedIndex));
		}
		storage.updateNameByID(taskID, newName);
		returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_UPDATE_NAME_SUCCESS, oldName, newName));
		returnedCommand.setDisplayedEventList(updateDisplay());
		return returnedCommand;
	}

	// return an ERROR command which tells the GUI that the command given is invalid
	private Command getErrorCommand(String ErrorMessage) {
		Command c = new Command();
		c.setDisplayMessage(ErrorMessage);
		return c;
	}

	/**
	 * Reverts back the name of an event
	 * @param id
	 * @param name
	 * @return a Command object that updates the existing name of the Event
	 *         object of "@param id" to "@param name"
	 */
	private Command reverseUpdateName(int id, String name, String displayedIndex) {
		Command temp = new Command();
		temp.setIsUserCommand(false);
		temp.setType(Constants.COMMAND_TYPE.UPDATE_NAME);
		temp.setEventName(name);
		temp.setEventID(id);
		temp.setDisplayedIndex(displayedIndex);
		return temp;
	}

	/**
	 * Updates the start time of an existing Event to the new start time entered by user
	 * @param command
	 * @return a Command object with the updated display
	 */
	private Command updateStart(Command command) {
		Command returnedCommand = new Command();
		Date startDate = command.getEventStart();
		String displayedIndex = command.getDisplayedIndex();
		int taskID = Constants.INVALID_INDICATOR;
		if (TGIDMap.containsKey(displayedIndex)) {
			taskID = TGIDMap.get(displayedIndex);
		} else {
			return getErrorCommand(Constants.TANGGUO_INVALID_INDEX);
		}
		Event element = storage.getEventByID(taskID);
		if (startDate.after(element.getEnd())) {
			return getErrorCommand(Constants.TANGGUO_INVALID_START);
		}
		Date oldStart = element.getStart();
		if (command.isUserCommand()){
			reversedCommandStack.push(reverseUpdateStart(taskID, oldStart, displayedIndex));
		}
		storage.updateStartByID(taskID, startDate);
		returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_UPDATE_START_SUCCESS, storage.getEventByID(taskID).getName(),
				startDate.toString()));
		returnedCommand.setDisplayedEventList(updateDisplay());
		return returnedCommand;
	}

	/**
	 * Reverts back the start date of an event
	 * @param id
	 * @param startDate
	 * @return a Command object that updates the existing start date of the
	 *         Event object of @param id to @param startDate
	 */
	private Command reverseUpdateStart(int id, Date startDate, String displayedIndex) {
		Command temp = new Command();
		temp.setIsUserCommand(false);
		temp.setType(Constants.COMMAND_TYPE.UPDATE_START);
		temp.setEventStart(startDate);
		temp.setEventID(id);
		temp.setDisplayedIndex(displayedIndex);
		return temp;
	}

	/**
	 * Updates the end time of an existing Event to the new end time entered by user
	 * @param command
	 * @return a Command object with the updated display
	 */
	private Command updateEnd(Command command) {
		Date endDate = command.getEventEnd();
		Command returnedCommand = new Command();
		String displayedIndex = command.getDisplayedIndex();
		int taskID = Constants.INVALID_INDICATOR;
		if (TGIDMap.containsKey(displayedIndex)) {
			taskID = TGIDMap.get(displayedIndex);
		} else {
			return getErrorCommand(Constants.TANGGUO_INVALID_INDEX);
		}
		Event element = storage.getEventByID(taskID);
		if (endDate.before(element.getStart())) {
			return getErrorCommand(Constants.TANGGUO_INVALID_END);
		}
		Date oldEnd = element.getEnd();
		if (command.isUserCommand()){
			reversedCommandStack.push(reverseUpdateEnd(taskID, oldEnd, displayedIndex));
		}
		storage.updateEndByID(taskID, endDate);
		returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_UPDATE_END_SUCCESS, storage.getEventByID(taskID).getName(),
				endDate.toString()));
		returnedCommand.setDisplayedEventList(updateDisplay());
		return returnedCommand;
	}

	/**
	 * Reverts back the end date of an event
	 * @param id
	 * @param endDate
	 * @return a Command object that updates the existing end date of the Event
	 *         object of @param id to @param endDate
	 */
	private Command reverseUpdateEnd(int id, Date endDate, String displayedIndex) {
		Command temp = new Command();
		temp.setIsUserCommand(false);
		temp.setType(Constants.COMMAND_TYPE.UPDATE_END);
		temp.setEventEnd(endDate);
		temp.setEventID(id);
		temp.setDisplayedIndex(displayedIndex);
		return temp;
	}

	/**
	 * Updates the priority of an existing Event to the new priority entered by user
	 * @param command
	 * @return a Command object with the updated display
	 */
	private Command updatePriority(Command command) {
		int priority = command.getEventPriority();
		String displayedIndex = command.getDisplayedIndex();
		Command returnedCommand = new Command();
		int taskID = Constants.INVALID_INDICATOR;
		if (priority == Constants.INVALID_INDICATOR) {
			return getErrorCommand(Constants.TANGGUO_INVALID_PRIORITY);
		}
		if (TGIDMap.containsKey(displayedIndex)) {
			taskID = TGIDMap.get(displayedIndex);
		} else {
			return getErrorCommand(Constants.TANGGUO_INVALID_INDEX);
		}
		int oldPriority = storage.getEventByID(taskID).getPriority();
		if (command.isUserCommand()) {
			reversedCommandStack.push(reverseUpdatePriority(taskID, oldPriority, displayedIndex));
		}
		storage.updatePriorityByID(taskID, priority);
		returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_UPDATE_PRIORITY_SUCCESS,
				storage.getEventByID(taskID).getName(), priority));
		returnedCommand.setDisplayedEventList(updateDisplay());
		return returnedCommand;
	}

	/**
	 * Reverts back the priority of an event
	 * @param id
	 * @param priority
	 * @return a Command object that updates the existing name of the Event
	 *         object of @param id to @param priority
	 */
	private Command reverseUpdatePriority(int id, int priority, String displayedIndex) {
		Command temp = new Command();
		temp.setIsUserCommand(false);
		temp.setType(Constants.COMMAND_TYPE.UPDATE_NAME);
		temp.setEventPriority(priority);
		temp.setEventID(id);
		temp.setDisplayedIndex(displayedIndex);
		return temp;
	}

	/**
	 * Updates the category of an existing Event to the new category entered by user
	 * @param command
	 * @return a Command object with the updated display
	 */
	private Command updateCategory(Command command) {
		String newCategory = command.getEventCategory();
		String displayedIndex = command.getDisplayedIndex();
		int taskID = Constants.INVALID_INDICATOR;
		Command returnedCommand = new Command();

		if (TGIDMap.containsKey(displayedIndex)) {
			taskID = TGIDMap.get(displayedIndex);
		} else {
			return getErrorCommand(Constants.TANGGUO_INVALID_INDEX);
		}
		String oldCategory = storage.getEventByID(taskID).getCategory();
		if (command.isUserCommand()) {
			reversedCommandStack.push(reverseUpdateCategory(taskID, oldCategory, displayedIndex));
		}
		storage.updateCategoryByID(taskID, newCategory);
		returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_UPDATE_CATEGORY_SUCCESS,
				storage.getEventByID(taskID).getName(), newCategory));
		returnedCommand.setDisplayedEventList(updateDisplay());
		return returnedCommand;
	}

	/**
	 * Reverts back the category of an event
	 * @param id
	 * @param category
	 * @return a Command object that updates the existing name of the Event
	 *         object of @param id to @param category
	 */
	private Command reverseUpdateCategory(int id, String category, String displayedIndex) {
		Command temp = new Command();
		temp.setIsUserCommand(false);
		temp.setType(Constants.COMMAND_TYPE.UPDATE_CATEGORY);
		temp.setEventCategory(category);
		temp.setEventID(id);
		temp.setDisplayedIndex(displayedIndex);
		return temp;
	}

	/**
	 * Marks an existing Event as done
	 * @param command
	 * @return a Command object with the updated display
	 */
	private Command markAsDone(Command command) {
		Command returnedCommand = new Command();
		int taskID;
		if (command.isUserCommand()) {
			String displayedIndex = command.getDisplayedIndex();
			taskID = Constants.INVALID_INDICATOR;
			if (TGIDMap.containsKey(displayedIndex)) {
				taskID = TGIDMap.get(displayedIndex);
			} else {
				return getErrorCommand(Constants.TANGGUO_INVALID_INDEX);
			}
			reversedCommandStack.push(reverseMarkAsDone(taskID));
			storage.updateIsDoneByID(taskID, true);
		} else {
			taskID = command.getEventID();
			storage.updateIsDoneByID(taskID, false);
		}
		returnedCommand.setDisplayMessage(
				String.format(Constants.TANGGUO_UPDATE_DONE_SUCCESS, storage.getEventByID(taskID).getName()));
		returnedCommand.setDisplayedEventList(updateDisplay());
		return returnedCommand;
	}

	/**
	 * Reverts the completion status of an Event object, marks it as undone
	 * @param id
	 * @return a Command object that updates the completion status of the Event
	 *         object of @param id to false
	 */
	private Command reverseMarkAsDone(int id) {
		Command temp = new Command();
		temp.setIsUserCommand(false);
		temp.setType(Constants.COMMAND_TYPE.DONE);
		temp.setEventID(id);
		return temp;
	}

	/**
	 * deletes an existing Event
	 * @param toBeDeleted
	 * @return a Command object with the updated display
	 */
	private Command deleteEvent(Command command) {
		int IDToDelete;
		Command returnedCommand = new Command();
		if (command.isUserCommand()) {
			if (TGIDMap.containsKey(command.getDisplayedIndex())) {
				IDToDelete = TGIDMap.get(command.getDisplayedIndex());
			} else {
				return getErrorCommand(Constants.TANGGUO_INVALID_INDEX);
			}
			reversedCommandStack.push(reverseDeleteEvent(IDToDelete));
		} else {
			IDToDelete = command.getEventID();
		}
		Event deletedEvent = storage.deleteEventByID(IDToDelete);
		returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_DELETE_SUCCESS, fileName,
				deletedEvent.getName()));
		returnedCommand.setDisplayedEventList(updateDisplay());
		return returnedCommand;
	}

	/**
	 * Undoes the prior deletion of an Event object
	 * @param id
	 * @return Command object that adds back the previously deleted Event of @param id
	 */
	private Command reverseDeleteEvent(int id) {
		Command temp = new Command();
		temp.setIsUserCommand(false);
		Event event = storage.getEventByID(id);
		switch (event.getType()) {
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
			assert false : Constants.ASSERT_UNEXPECTED_TYPE_NUM;
			break;
		}
		temp.setEvent(event);
		return temp;
	}

	//@@author A0127604L
	// sorts Events by name by alphabetical order for display
	private Command sortName() {
		storage.sortName();
		Command returnedCommand = new Command();
		returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_SORT_SUCCESS,
				Constants.DISPLAY_NAME));
		returnedCommand.setDisplayedEventList(updateDisplay());
		return returnedCommand;
	}

	// sorts Events by start date (oldest date to newest date) for display
	private Command sortStart() {
		storage.sortStart();
		Command returnedCommand = new Command();
		returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_SORT_SUCCESS,
				Constants.DISPLAY_START));
		returnedCommand.setDisplayedEventList(updateDisplay());
		return returnedCommand;
	}

	// sorts Events by end date (oldest date to newest date) for display
	private Command sortEnd() {
		storage.sortEnd();
		Command returnedCommand = new Command();
		returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_SORT_SUCCESS,
				Constants.DISPLAY_END));
		returnedCommand.setDisplayedEventList(updateDisplay());
		return returnedCommand;
	}

	// sort storage by priority (ascending order)
	private Command sortPriority() {
		storage.sortPriority();
		Command returnedCommand = new Command();
		returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_SORT_SUCCESS,
				Constants.DISPLAY_PRIORITY));
		returnedCommand.setDisplayedEventList(updateDisplay());
		return returnedCommand;
	}

	// search through the storage for event containing the keyword
	private Command search(Command command) {
		lastSearchKey = command.getSearchKey(); // record down as latest searched keyword
		ArrayList<ArrayList<Event>> displayedEvent = updateSearchResult(lastSearchKey);
		if (displayedEvent == null) { // no search results returned
			return getErrorCommand(String.format(Constants.TANGGUO_SEARCH_FAIL,
					command.getSearchKey()));
		}
		TGIDMap.clear();
		Command returnedCommand = new Command();
		returnedCommand.setDisplayedTab(Constants.SEARCH_TAB_NUMBER);
		returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_SEARCH_SUCCESS,
				command.getSearchKey()));
		returnedCommand.setDisplayedEventList(displayedEvent);
		return returnedCommand;
	}

	// return the list of events that contain the searchKey
	private ArrayList<ArrayList<Event>> updateSearchResult(String searchKey) {
		ArrayList<Event> task = storage.searchTask(searchKey);
		ArrayList<Event> deadline = storage.searchDeadline(searchKey);
		ArrayList<Event> schedule = storage.searchSchedule(searchKey);
		if (task.isEmpty() && deadline.isEmpty() && schedule.isEmpty()) { // no result
			return null;
		}
		TGIDMap.clear();
		ArrayList<ArrayList<Event>> displayEvent = new ArrayList<ArrayList<Event>>();
		TGIDMap.clear();
		displayEvent.add(getCache(task, Constants.TASK_IDENTITY));
		displayEvent.add(getCache(deadline, Constants.DEADLINE_IDENTITY));
		displayEvent.add(getCache(schedule, Constants.SCHEDULE_IDENTITY));
		return displayEvent;
	}

	//sets the file path of the storage file
	private Command setPath(Command command) {
		config.setFilePath(command.getPath());
		storage.setFilePath(command.getPath());
		config.writeConfig();
		Command returnedCommand = new Command();
		returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_PATH_SET,
				fileName, command.getPath()));
		return returnedCommand;
	}

	//reads in data from file path specified by the user and sets file path as such
	private Command importData(Command command) {
		int div = command.getPath().lastIndexOf(Constants.SLASH);
		String filePath = command.getPath().substring(0, div + 1);
		fileName = command.getPath().substring(div + 1);
		config.setFilePath(filePath);
		config.setFileName(fileName);
		config.writeConfig();
		storage = new TGStorageManager(filePath, fileName);
		filePath = (filePath.equals(Constants.NULL)) ? Constants.DEFAULT_STRING : filePath;
		Command returnedCommand = new Command();
		returnedCommand.setDisplayMessage(String.format(Constants.TANGGUO_IMPORT_SUCCESS,
				filePath));
		returnedCommand.setDisplayedEventList(updateDisplay());
		return returnedCommand;
	}
}