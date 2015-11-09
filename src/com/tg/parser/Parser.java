package com.tg.parser;

import java.text.ParseException;
import java.util.Date;

import TGUtils.Command;
import TGUtils.Constants;

public class Parser {

	/*  public static void main(String[] args) throws IndexOutOfBoundsException,
	  ParseException, AbnormalScheduleTimeException, TaskDateExistenceException
	  { Parser parser = new Parser();

	  	Command test = Parser.parseCommand("add task by 6/11/2015 15:09");
	  	Command test1 = Parser.parseCommand("update end d1 6/11");
	  }*/

	public static Command parseCommand(String input) throws ParseException, IndexOutOfBoundsException {
		String command = getFirstWord(input);
		String event = removeFirstWord(input);
		String displayedIndex;
		String deadlineDateAndTime, scheduleStartDateAndTime, scheduleEndDateAndTime;
		String finalStartDate, finalEndDate;

		Constants.COMMAND_TYPE commandType = findCommandType(command);

		Date endDate, startDate;

		Command tempCommand = new Command();
		tempCommand.setType(commandType);
		tempCommand.setIsUserCommand(true);

		PropertyCheck propertyCheck = new PropertyCheck(event);
		EventCheck eventCheck = new EventCheck(event);

		switch (commandType) {
		case ADD:

			boolean withPriority = propertyCheck.containsPriority();
			if (withPriority == true) {
				int eventPriority = propertyCheck.getPriority();
				tempCommand.setEventPriority(eventPriority);

				event = propertyCheck.removePropertyFromEventName();
			}

			eventCheck = eventCheck.reInitialize(event);
			try { // deadline
				if (eventCheck.possibleDate(Constants.DEADLINE) == true) {

					deadlineDateAndTime = eventCheck.getDeadlineDateAndTime();

					finalEndDate = DateTimeHandler.defaultDateTimeCheck(deadlineDateAndTime, Constants.DEADLINE);

					endDate = DateTimeHandler.dateConverter(finalEndDate);

					tempCommand.setEventEnd(endDate);
					tempCommand.setEventName(eventCheck.getEventName(Constants.DEADLINE));
					tempCommand.setType(Constants.COMMAND_TYPE.ADD_DEADLINE);
					break;
				}
			} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {

				try { // schedule
					if (eventCheck.possibleDate(Constants.SCHEDULE_START) && eventCheck.possibleDate(Constants.SCHEDULE_END)) {

						scheduleStartDateAndTime = eventCheck.getScheduleStartDateAndTime();
						scheduleEndDateAndTime = eventCheck.getScheduleEndDateAndTime();

						finalEndDate = DateTimeHandler.defaultDateTimeCheck(scheduleEndDateAndTime, Constants.SCHEDULE);
						finalStartDate = DateTimeHandler.defaultDateTimeCheck(scheduleStartDateAndTime, Constants.SCHEDULE);

						endDate = DateTimeHandler.dateConverter(finalEndDate);
						startDate = DateTimeHandler.dateConverter(finalStartDate);

						tempCommand.setEventStart(startDate);
						tempCommand.setEventEnd(endDate);
						tempCommand.setEventName(eventCheck.getEventName(Constants.SCHEDULE));
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
			String newStart = removeFirstWord(event);
			newStart = updateDateTimeCheck(displayedIndex, newStart);
			Date updatedStart = DateTimeHandler.dateConverter(newStart);

			tempCommand.setDisplayedIndex(displayedIndex);
			tempCommand.setEventStart(updatedStart);
			break;
		case UPDATE_END:
			displayedIndex = getFirstWord(event);
			String newEnd = removeFirstWord(event);
			newEnd = updateDateTimeCheck(displayedIndex, newEnd);
			Date updatedEnd = DateTimeHandler.dateConverter(newEnd);

			tempCommand.setDisplayedIndex(displayedIndex);
			tempCommand.setEventEnd(updatedEnd);
			break;
		case UPDATE_PRIORITY:
			displayedIndex = getFirstWord(event);
			int updatedPriority = propertyCheck.checkPriority(removeFirstWord(event));

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
			tempCommand.setType(Constants.COMMAND_TYPE.EXCEPTION);
		}
		return tempCommand;
	}

	private static String updateDateTimeCheck(String displayedIndex, String date) {
		if (displayedIndex.charAt(0) == Constants.DEADLINE_CHAR) {
			date = DateTimeHandler.defaultDateTimeCheck(date, Constants.DEADLINE);
		} else if (displayedIndex.charAt(0) == Constants.SCHEDULE_CHAR) {
			date = DateTimeHandler.defaultDateTimeCheck(date, Constants.SCHEDULE);
		}
		return date;
	}

	private static Constants.COMMAND_TYPE findCommandType(String commandTypeString) {
		if (commandTypeString.equalsIgnoreCase(Constants.ADD)) {
			return Constants.COMMAND_TYPE.ADD;
		} else if (commandTypeString.equalsIgnoreCase(Constants.DISPLAY)) {
			return Constants.COMMAND_TYPE.DISPLAY;
		} else if (commandTypeString.equalsIgnoreCase(Constants.DELETE)) {
			return Constants.COMMAND_TYPE.DELETE;
		} else if (commandTypeString.equalsIgnoreCase(Constants.EXIT)) {
			return Constants.COMMAND_TYPE.EXIT;
		} else if (commandTypeString.equalsIgnoreCase(Constants.UNDO)) {
			return Constants.COMMAND_TYPE.UNDO;
		} else if (commandTypeString.equalsIgnoreCase(Constants.UPDATE_NAME)) {
			return Constants.COMMAND_TYPE.UPDATE_NAME;
		} else if (commandTypeString.equalsIgnoreCase(Constants.UPDATE_END)) {
			return Constants.COMMAND_TYPE.UPDATE_END;
		} else if (commandTypeString.equalsIgnoreCase(Constants.UPDATE_START)) {
			return Constants.COMMAND_TYPE.UPDATE_START;
		} else if (commandTypeString.equalsIgnoreCase(Constants.UPDATE_PRIORITY)) {
			return Constants.COMMAND_TYPE.UPDATE_PRIORITY;
		} else if (commandTypeString.equalsIgnoreCase(Constants.UPDATE_CATEGORY)) {
			return Constants.COMMAND_TYPE.UPDATE_CATEGORY;
		} else if (commandTypeString.equalsIgnoreCase(Constants.DONE)) {
			return Constants.COMMAND_TYPE.DONE;
		} else if (commandTypeString.equalsIgnoreCase(Constants.SORT_NAME)) {
			return Constants.COMMAND_TYPE.SORT_NAME;
		} else if (commandTypeString.equalsIgnoreCase(Constants.SORT_START)) {
			return Constants.COMMAND_TYPE.SORT_START;
		} else if (commandTypeString.equalsIgnoreCase(Constants.SORT_END)) {
			return Constants.COMMAND_TYPE.SORT_END;
		} else if (commandTypeString.equalsIgnoreCase(Constants.SORT_PRIORITY)) {
			return Constants.COMMAND_TYPE.SORT_PRIORITY;
		} else if (commandTypeString.equalsIgnoreCase(Constants.SEARCH)) {
			return Constants.COMMAND_TYPE.SEARCH;
		} else if (commandTypeString.equalsIgnoreCase(Constants.PATH)) {
			return Constants.COMMAND_TYPE.PATH;
		} else if (commandTypeString.equalsIgnoreCase(Constants.IMPORT)) {
			return Constants.COMMAND_TYPE.IMPORT;
		} else if (commandTypeString.equalsIgnoreCase(Constants.TOGGLE)) {
			return Constants.COMMAND_TYPE.TOGGLE;
		} else {
			return Constants.COMMAND_TYPE.INVALID;
		}
	}

	private static String removeFirstWord(String input) {
		return input.replaceFirst(getFirstWord(input), Constants.NULL).trim();
	}

	private static String getFirstWord(String input) {
		String inputString = input.trim().split(Constants.WORD_SPLIT)[0];

		if (inputString.equals(Constants.UPDATE) || inputString.equals(Constants.SORT)) {
			inputString += Constants.SPACE + input.trim().split(Constants.WORD_SPLIT)[1];
		}
		return inputString;
	}
}