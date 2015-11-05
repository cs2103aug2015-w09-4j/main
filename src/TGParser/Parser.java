package TGParser;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Arrays;
import java.util.Date;

import TGExceptions.AbnormalScheduleTimeException;
import TGExceptions.TaskDateExistenceException;
import TGUtils.Command;
import TGUtils.Constants;

public class Parser {
	private static DateFormat format = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);

	public static Command parseCommand(String input) throws ParseException, IndexOutOfBoundsException, AbnormalScheduleTimeException, TaskDateExistenceException{
		Command tempCommand = new Command();
		String command = getFirstWord(input);
		Constants.COMMAND_TYPE commandType = findCommandType(command);
		String event = removeFirstWord(input);
		Date endDate, startDate;
		String displayedIndex;
		tempCommand.setType(commandType);
		tempCommand.setIsUserCommand(true);
		format.setLenient(false); //This allows DateFormat to prevent date overflow
		String modifiedStartDateString, modifiedEndDateString;

		switch (commandType) {
			case ADD:

				String[] inputArray = event.split(" ");
				int eventPriority = checkPriority(inputArray[inputArray.length - 1]);
				if (eventPriority != -1) {
					tempCommand.setEventPriority(eventPriority);
					inputArray[inputArray.length - 1] = "";
					String temp = "";
					
					for(int i = 0; i < inputArray.length - 1; i++) {
						temp = temp + inputArray[i] + " ";
					}
					event = temp.substring(0, temp.length() - 1);
				}
				
				String[] array = event.split(Constants.DEADLINE_SPLIT);
				try {														//deadline
					if(isNumber(array[array.length - 1])) {

						modifiedEndDateString = defaultDateTimeCheck(array[array.length - 1], "deadline");

						endDate = dateConverter(modifiedEndDateString);

						tempCommand.setEventEnd(endDate);
						tempCommand.setEventName(getName(Constants.DEADLINE_SPLIT, array));
						tempCommand.setType(Constants.COMMAND_TYPE.ADD_DEADLINE);
						break;
					}
				} catch (NumberFormatException | ArrayIndexOutOfBoundsException e){
					String[] array1 = event.split(Constants.SCHEDULE_FIRST_SPLIT);
					String[] array2 = array1[array1.length - 1].split(Constants.SCHEDULE_SECOND_SPLIT);

					try {													//schedule
						if(isNumber(array2[1]) && isNumber(array2[0]) && startAndEndTimeValidation(array2[0], array2[1])) {
							modifiedEndDateString = defaultDateTimeCheck(array2[1], "schedule");
							modifiedStartDateString = defaultDateTimeCheck(array2[0], "schedule");

							endDate = dateConverter(modifiedEndDateString);
							startDate = dateConverter(modifiedStartDateString);

							tempCommand.setEventStart(startDate);
							tempCommand.setEventEnd(endDate);
							tempCommand.setEventName(getName(Constants.SCHEDULE_FIRST_SPLIT, array1));
							tempCommand.setType(Constants.COMMAND_TYPE.ADD_SCHEDULE);
							break;
						}

					} catch (NumberFormatException | ArrayIndexOutOfBoundsException f){

						if(array.length > 1) {
							checkTaskValidity(array[array.length - 1]);
						}

						if(array1.length > 1 && array2.length > 1) {

							checkTaskValidity(array2[1]);
							checkTaskValidity(array2[0]);
						}


						tempCommand.setEventName(event);
						tempCommand.setType(Constants.COMMAND_TYPE.ADD_TASK);	//task
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
				Date updatedStart = dateConverter(removeFirstWord(event));
				tempCommand.setDisplayedIndex(displayedIndex);
				tempCommand.setEventStart(updatedStart);
				break;
			case UPDATE_END:
				displayedIndex = getFirstWord(event);
				Date updatedEnd = dateConverter(removeFirstWord(event));
				tempCommand.setDisplayedIndex(displayedIndex);
				tempCommand.setEventEnd(updatedEnd);
				break;
			case UPDATE_PRIORITY:
				displayedIndex = getFirstWord(event);
				int updatedPriority = checkPriority(removeFirstWord(event));
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
				//throw new exception?
				tempCommand.setType(Constants.COMMAND_TYPE.EXCEPTION);
		}
		return tempCommand;
	}

	private static String getName(String splitSeq, String[] array){
		StringBuilder name = new StringBuilder();
		for (int i = 0; i < array.length-1; i++){
			name.append(array[i]);
			if (i != array.length-2){
				name.append(splitSeq);
			}
		}
		return name.toString();
	}

	private static Constants.COMMAND_TYPE findCommandType(String commandTypeString) {
		if (commandTypeString.equalsIgnoreCase("add")){
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
		} else if (commandTypeString.equalsIgnoreCase("done")){
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

		if(inputString.equals("update") || inputString.equals("sort")) {
			inputString += " " + input.trim().split("\\s+")[1];
		}
		return inputString;
	}

	private static Date dateConverter(String dateString) throws ParseException{

		Date date = format.parse(dateString);

		return date;
	}

	/**
	 * Checks whether if the time and date are integers.
	 * This method is the first check as to whether they are in the default format
	 * @param num
	 * @return
	 * @throws NumberFormatException
	 * @throws ArrayIndexOutOfBoundsException
	 */
	/**
	 * Checks whether if the time and date are integers.
	 * This method is the first check as to whether they are in the default format
	 * @param num
	 * @return
	 * @throws NumberFormatException
	 * @throws ArrayIndexOutOfBoundsException
	 */
	@SuppressWarnings("unused")
	private static boolean isNumber(String timeAndDate) throws NumberFormatException, ArrayIndexOutOfBoundsException {
		
		int hourInteger, minuteInteger, dayInteger, monthInteger, yearInteger;
		String hour, minute, day, month, year;
		String[] dayMonthYearSplit, hourMinuteSplit;
		
		String[] timeAndDateSplit = timeAndDate.split(" ");

		if (timeAndDateSplit.length == 1) {

		    dayMonthYearSplit = timeAndDateSplit[0].split("/");
			hourMinuteSplit = timeAndDateSplit[0].split(":");

			if(dayMonthYearSplit.length == 1) {

				hour = hourMinuteSplit[0];
				hourInteger = Integer.parseInt(hour);

				minute = hourMinuteSplit[1];
				minuteInteger = Integer.parseInt(minute);

			} else if (hourMinuteSplit.length == 1) {
				if(dayMonthYearSplit.length == 2) {
				
					day = dayMonthYearSplit[0];
					dayInteger = Integer.parseInt(day);

					month = dayMonthYearSplit[1];
					monthInteger = Integer.parseInt(month);
					
				} else {
					
					day = dayMonthYearSplit[0];
					dayInteger = Integer.parseInt(day);

					month = dayMonthYearSplit[1];
					monthInteger = Integer.parseInt(month);

					year = dayMonthYearSplit[2];
					yearInteger = Integer.parseInt(year);
				
				}
			}

		} else {

			dayMonthYearSplit = timeAndDateSplit[0].split("/");
			hourMinuteSplit = timeAndDateSplit[1].split(":");

			day = dayMonthYearSplit[0];
			dayInteger = Integer.parseInt(day);

			month = dayMonthYearSplit[1];
			monthInteger = Integer.parseInt(month);

		/*	year = dayMonthYearSplit[2];
			yearInteger = Integer.parseInt(year); */

			hour = hourMinuteSplit[0];
			hourInteger = Integer.parseInt(hour);

			minute = hourMinuteSplit[1];
			minuteInteger = Integer.parseInt(minute);
		}

		return true;
	}
	/**
	 * Fills in default date and/or time according to the user inputs
	 *
	 * @param date
	 * @return String
	 */
	private static String defaultDateTimeCheck(String date, String eventType) {

		String modifiedString = date;
		String todayDate = getTodayDate();
		String thisYear = getThisYear();

		String[] dayMonthYearSplit = date.split("/");
		String[] hourMinuteSplit = date.split(":");

		if(dayMonthYearSplit.length == 1) { //case 1: when a user does not enter a date, but with time.
			modifiedString = modifiedString.substring(0, modifiedString.length() - 5) + todayDate + modifiedString.substring(modifiedString.length() - 5);
			//code above is abit hard-coded yes yes, but it literally adds current date to the string in our format.
		} else if (hourMinuteSplit.length == 2 && dayMonthYearSplit.length == 2) {//case 2, when the user provides a time and date without year
			modifiedString = modifiedString.substring(0, modifiedString.length() - 6) + thisYear + modifiedString.substring(modifiedString.length() - 5);
		
		} else if (hourMinuteSplit.length == 1 && dayMonthYearSplit.length != 2) {//case 3: when a user does not enter a time, but with a full date with year.
			if(eventType.equals("deadline"))
				modifiedString = modifiedString + " 23:59";
			else if(eventType.equals("schedule"))
				modifiedString = modifiedString + " 00:00";
		
		} else if (hourMinuteSplit.length == 1 && dayMonthYearSplit.length == 2) {//case 4: when a user does not enter a time, but with a date without year.
			if(eventType.equals("deadline"))
				modifiedString = modifiedString + thisYear + "23:59";
			else if(eventType.equals("schedule"))
				modifiedString = modifiedString + thisYear + "00:00";
		}

		return modifiedString;
	}

	private static int checkPriority(String input) {
		if (input.equalsIgnoreCase("HIGH")) {
			return 3;
		} else if (input.equalsIgnoreCase("MID")) {
			return 2;
		} else if (input.equalsIgnoreCase("LOW")) {
			return 1;
		} else {
			return -1;
		}
	}

	private static String toString(String[] array) {
		String result = "";

		for (String s : array) {
			result += s;
		}

		return result;
	}


	private static String getTodayDate() {
		DateFormat df = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
		Calendar cal = Calendar.getInstance();

		String today = df.format(cal.getTime());
		return today.substring(0, today.length() - 6) + " "; //to remove the unwanted time from today
	}
	
	private static String getThisYear() {
		DateFormat df = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
		Calendar cal = Calendar.getInstance();

		String today = df.format(cal.getTime());
		return "/" + today.substring(6, today.length() - 6) + " "; //to remove the unwanted time from today		
	}

	private static void checkTaskValidity(String input) throws TaskDateExistenceException {

		int inputLength = input.length();
		int inputLengthCheck = input.replaceAll("[0-9]/[0-9]", "").length();

		if(inputLength - inputLengthCheck != 0)
			throw new TaskDateExistenceException();

		inputLengthCheck = input.replaceAll("[0-9]:[0-9]", "").length();

		if(inputLength - inputLengthCheck != 0)
			throw new TaskDateExistenceException();

	}
	public static boolean startAndEndTimeValidation(String start, String end) throws AbnormalScheduleTimeException {

		if(start.compareTo(end) >= 0) {
			throw new AbnormalScheduleTimeException();
		}

		return true;
	}

}