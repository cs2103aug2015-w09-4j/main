package TGParser;

import java.text.ParseException;
import java.util.Date;

import TGExceptions.AbnormalScheduleTimeException;
import TGExceptions.TaskDateExistenceException;
import TGUtils.Command;
import TGUtils.Constants;

public class Parser {

	/*
	 * public static void main(String[] args) throws IndexOutOfBoundsException,
	 * ParseException, AbnormalScheduleTimeException, TaskDateExistenceException
	 * { Parser parser = new Parser();
	 * 
	 * Command test = Parser.parseCommand("add task by 6/11/2015 15:09"); }
	 */

	public static Command parseCommand(String input) throws ParseException, IndexOutOfBoundsException,
			AbnormalScheduleTimeException, TaskDateExistenceException {
		String command = getFirstWord(input);
		String event = removeFirstWord(input);
		String displayedIndex;
		String scheduleStartDateAndTime, scheduleEndDateAndTime;
		String finalStartDate, finalEndDate;

		Constants.COMMAND_TYPE commandType = findCommandType(command);

		Date endDate, startDate;

		Command tempCommand = new Command();
		tempCommand.setType(commandType);
		tempCommand.setIsUserCommand(true);

		PriorityCheck priorityCheck = new PriorityCheck(event);

		switch (commandType) {
		case ADD:

			boolean withPriority = priorityCheck.containsPriority();

			if (withPriority == true) {
				int eventPriority = priorityCheck.getPriorityNumber();
				tempCommand.setEventPriority(eventPriority);

				event = priorityCheck.removePriorityFromEventName();
			}

			EventCheck eventCheck = new EventCheck(event);

			try { // deadline
				if (eventCheck.possibleDate("deadline") == true) {

					finalEndDate = DateTimeHandler.defaultDateTimeCheck(eventCheck.getDeadlineDateAndTime(),
							"deadline");

					endDate = DateTimeHandler.dateConverter(finalEndDate);

					tempCommand.setEventEnd(endDate);
					tempCommand.setEventName(eventCheck.getEventName("deadline"));
					tempCommand.setType(Constants.COMMAND_TYPE.ADD_DEADLINE);
					break;
				}

			} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
				
				try { // schedule
					if (eventCheck.possibleDate("scheduleStart") && eventCheck.possibleDate("scheduleEnd")) {

						scheduleStartDateAndTime = eventCheck.getScheduleStartDateAndTime();
						scheduleEndDateAndTime = eventCheck.getScheduleEndDateAndTime();

						DateTimeHandler.startAndEndTimeValidation(scheduleStartDateAndTime, scheduleEndDateAndTime);

						finalEndDate = DateTimeHandler.defaultDateTimeCheck(eventCheck.getScheduleEndDateAndTime(),
								"schedule");
						finalStartDate = DateTimeHandler.defaultDateTimeCheck(eventCheck.getScheduleStartDateAndTime(),
								"schedule");

						endDate = DateTimeHandler.dateConverter(finalEndDate);
						startDate = DateTimeHandler.dateConverter(finalStartDate);

						tempCommand.setEventStart(startDate);
						tempCommand.setEventEnd(endDate);
						tempCommand.setEventName(eventCheck.getEventName("schedule"));
						tempCommand.setType(Constants.COMMAND_TYPE.ADD_SCHEDULE);
						break;
					}

				} catch (NumberFormatException | ArrayIndexOutOfBoundsException f) {
					
					//task
					eventCheck.isProperFloatingTaskCheck();

					tempCommand.setEventName(event);
					tempCommand.setType(Constants.COMMAND_TYPE.ADD_TASK); 
				}
			}
			break;
		case DISPLAY:
			break;
		case UPDATE_NAME:
			displayedIndex = getFirstWord(event);
			String updatedName = removeFirstWord(event);
			tempCommand.setDisplayedIndex(displayedIndex);
			tempCommand.setEventName(updatedName);
			break;
		case UPDATE_START:
			displayedIndex = getFirstWord(event);
			Date updatedStart = DateTimeHandler.dateConverter(removeFirstWord(event));
			tempCommand.setDisplayedIndex(displayedIndex);
			tempCommand.setEventStart(updatedStart);
			break;
		case UPDATE_END:
			displayedIndex = getFirstWord(event);
			Date updatedEnd = DateTimeHandler.dateConverter(removeFirstWord(event));
			tempCommand.setDisplayedIndex(displayedIndex);
			tempCommand.setEventEnd(updatedEnd);
			break;
		case UPDATE_PRIORITY:
			displayedIndex = getFirstWord(event);
			int updatedPriority = priorityCheck.getPriorityNumber();
			tempCommand.setDisplayedIndex(displayedIndex);
			tempCommand.setEventPriority(updatedPriority);
			break;
		case UPDATE_CATEGORY:
			displayedIndex = getFirstWord(event);
			String updatedCategory = removeFirstWord(event);
			tempCommand.setDisplayedIndex(displayedIndex);
			tempCommand.setEventCategory(updatedCategory);
			break;
		case DELETE:
			displayedIndex = getFirstWord(event);
			tempCommand.setDisplayedIndex(displayedIndex);
			break;
		case UNDO:
			break;
		case DONE:
			displayedIndex = getFirstWord(event);
			tempCommand.setDisplayedIndex(displayedIndex);
			break;
		case SORT_NAME:
			break;
		case SORT_START:
			break;
		case SORT_END:
			break;
		case SORT_PRIORITY:
			break;
		case SEARCH:
			tempCommand.setSearchKey(event.toLowerCase());
			break;
		case TOGGLE:
			break;
		case PATH:
			tempCommand.setPath(event);
			break;
		case IMPORT:
			tempCommand.setPath(event);
			break;
		case EXIT:
			break;
		case INVALID:
			break;
		default:
			// throw new exception?
			tempCommand.setType(Constants.COMMAND_TYPE.EXCEPTION);
		}
		return tempCommand;
	}

	private static Constants.COMMAND_TYPE findCommandType(String commandTypeString) {
		if (commandTypeString.equalsIgnoreCase("add")) {
			return Constants.COMMAND_TYPE.ADD;
		} else if (commandTypeString.equalsIgnoreCase("display")) {
			return Constants.COMMAND_TYPE.DISPLAY;
		} else if (commandTypeString.equalsIgnoreCase("delete")) {
			return Constants.COMMAND_TYPE.DELETE;
		} else if (commandTypeString.equalsIgnoreCase("exit")) {
			return Constants.COMMAND_TYPE.EXIT;
		} else if (commandTypeString.equalsIgnoreCase("undo")) {
			return Constants.COMMAND_TYPE.UNDO;
		} else if (commandTypeString.equalsIgnoreCase("update name")) {
			return Constants.COMMAND_TYPE.UPDATE_NAME;
		} else if (commandTypeString.equalsIgnoreCase("update end")) {
			return Constants.COMMAND_TYPE.UPDATE_END;
		} else if (commandTypeString.equalsIgnoreCase("update start")) {
			return Constants.COMMAND_TYPE.UPDATE_START;
		} else if (commandTypeString.equalsIgnoreCase("update priority")) {
			return Constants.COMMAND_TYPE.UPDATE_PRIORITY;
		} else if (commandTypeString.equalsIgnoreCase("update category")) {
			return Constants.COMMAND_TYPE.UPDATE_CATEGORY;
		} else if (commandTypeString.equalsIgnoreCase("done")) {
			return Constants.COMMAND_TYPE.DONE;
		} else if (commandTypeString.equalsIgnoreCase("sort name")) {
			return Constants.COMMAND_TYPE.SORT_NAME;
		} else if (commandTypeString.equalsIgnoreCase("sort start")) {
			return Constants.COMMAND_TYPE.SORT_START;
		} else if (commandTypeString.equalsIgnoreCase("sort end")) {
			return Constants.COMMAND_TYPE.SORT_END;
		} else if (commandTypeString.equalsIgnoreCase("sort priority")) {
			return Constants.COMMAND_TYPE.SORT_PRIORITY;
		} else if (commandTypeString.equalsIgnoreCase("search")) {
			return Constants.COMMAND_TYPE.SEARCH;
		} else if (commandTypeString.equalsIgnoreCase("path")) {
			return Constants.COMMAND_TYPE.PATH;
		} else if (commandTypeString.equalsIgnoreCase("import")) {
			return Constants.COMMAND_TYPE.IMPORT;
		} else if (commandTypeString.equalsIgnoreCase("toggle")) {
			return Constants.COMMAND_TYPE.TOGGLE;
		} else {
			return Constants.COMMAND_TYPE.INVALID;
		}
	}

	static private String removeFirstWord(String input) {
		return input.replaceFirst(getFirstWord(input), "").trim();
	}

	static private String getFirstWord(String input) {
		String inputString = input.trim().split("\\s+")[0];

		if (inputString.equals("update") || inputString.equals("sort")) {
			inputString += " " + input.trim().split("\\s+")[1];
		}
		return inputString;
	}
}