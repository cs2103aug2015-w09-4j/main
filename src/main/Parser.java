package main;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//import java.util.regex.*;

//import com.sun.glass.ui.Pixels.Format;
//import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

//import jdk.nashorn.internal.codegen.CompilerConstants;


public class Parser {
	private static DateFormat format = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
	
	public static Command parseCommand(String input) throws ParseException, IndexOutOfBoundsException, AbnormalScheduleTimeException{
		Command tempCommand = new Command();
		String command = getFirstWord(input);
		Constants.COMMAND_TYPE commandType = findCommandType(command);
		String event = removeFirstWord(input);		
		Date endDate, startDate;
		String displayedIndex;
		tempCommand.setType(commandType);
		tempCommand.setIsUserCommand(true);
		format.setLenient(false); //This allows DateFormat to prevent date overflow
		
		switch (commandType) {
			case ADD:				
				String[] array = event.split(Constants.DEADLINE_SPLIT);			
				try {														//deadline
					if(isNumber(array[array.length - 1])) {
						endDate = dateConverter(array[array.length - 1]);
						
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
							endDate = dateConverter(array2[1]);
							startDate = dateConverter(array2[0]);
							
							tempCommand.setEventStart(startDate);
							tempCommand.setEventEnd(endDate);
							tempCommand.setEventName(getName(Constants.SCHEDULE_FIRST_SPLIT, array1));
							tempCommand.setType(Constants.COMMAND_TYPE.ADD_SCHEDULE);
							break;
						}
						
					} catch (NumberFormatException | ArrayIndexOutOfBoundsException f){
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
				int updatedPriority = Integer.parseInt(removeFirstWord(event));
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
	//	Date date = null;
		
		//if(isRightDateFormat(dateString)) {
		 Date date = format.parse(dateString);
	//	}
		
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
	@SuppressWarnings("unused")
	private static boolean isNumber(String timeAndDate) throws NumberFormatException, ArrayIndexOutOfBoundsException {
		
		String[] timeAndDateSplit = timeAndDate.split(" ");
		String[] dayMonthYearSplit = timeAndDateSplit[0].split("/");
		String[] hourMinuteSplit = timeAndDateSplit[1].split(":");
		
		String day = dayMonthYearSplit[0];
		int dayInteger = Integer.parseInt(day);
		
		String month = dayMonthYearSplit[1];
		int monthInteger = Integer.parseInt(month);
		
		String year = dayMonthYearSplit[2];
		int yearInteger = Integer.parseInt(year);
		
		String hour = hourMinuteSplit[0];
		int hourInteger = Integer.parseInt(hour);
		
		String minute = hourMinuteSplit[1];
		int minuteInteger = Integer.parseInt(minute);

		return true;
	}
	public static boolean startAndEndTimeValidation(String start, String end) throws AbnormalScheduleTimeException {
		
		if(start.compareTo(end) >= 0) {
			throw new AbnormalScheduleTimeException();
		}
		
		return true;
	}
	
/*	private static boolean isRightDateFormat(String dateString) {
		
		String[] imbaTestArray = dateString.split("/");
		
		if(imbaTestArray.length != 3)
			return false;
		
		String[] imbaTestTimeArray = imbaTestArray[2].split(":");
		
		if(imbaTestTimeArray.length != 2)
			return false;
		
		try {
			Integer.parseInt(imbaTestArray[0]);
			Integer.parseInt(imbaTestArray[1]);
			Integer.parseInt(imbaTestTimeArray[0]);
			Integer.parseInt(imbaTestTimeArray[1]);
		} catch (NumberFormatException e) {
			return false;
		}
		
		return true;
	} */
}