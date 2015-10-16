package main;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.regex.*;

import com.sun.glass.ui.Pixels.Format;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

import jdk.nashorn.internal.codegen.CompilerConstants;


public class Parser {
	private static DateFormat format = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
	
	static public Command parseCommand(String input) throws ParseException, IndexOutOfBoundsException{
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
				String[] array = event.split("by ");
				try {
					System.out.println(array[array.length - 1]);
					if(isNumber(array[array.length - 1])) {
						endDate = dateConverter(array[array.length - 1]);
						
						tempCommand.setEventEnd(endDate);
						tempCommand.setEventName(event);
						tempCommand.setType(Constants.COMMAND_TYPE.ADD_DEADLINE);
						break;
					}
				} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
					
					String[] array1 = event.split("from ");
					String[] array2 = array1[array1.length - 1].split("to ");
					
					try {
						System.out.println(array2[1] + " " + array2[0]);
						if(isNumber(array2[1]) && isNumber(array2[0])) {
							endDate = dateConverter(array2[1]);
							startDate = dateConverter(array2[0]);
							
							tempCommand.setEventStart(startDate);
							tempCommand.setEventEnd(endDate);
							tempCommand.setEventName(event);
							tempCommand.setType(Constants.COMMAND_TYPE.ADD_SCHEDULE);
							break;
						}
					} catch(NumberFormatException | ArrayIndexOutOfBoundsException f) {
						tempCommand.setEventName(event);
						tempCommand.setType(Constants.COMMAND_TYPE.ADD_TASK);
					}
				}
				break;		
			case DISPLAY:
				break;
			case UPDATE_NAME:
				displayedIndex = getFirstWord(event);
				String newName = removeFirstWord(event);
				tempCommand.setEventName(newName);
				tempCommand.setDisplayedIndex(displayedIndex);
				break;
			case DELETE:
				displayedIndex = getFirstWord(event);
				tempCommand.setDisplayedIndex(displayedIndex);
				break;
			case UNDO:
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
	private static Constants.COMMAND_TYPE findCommandType(String commandTypeString) {
		if (commandTypeString.equalsIgnoreCase("add")){
			return Constants.COMMAND_TYPE.ADD;
			
		/**(commandTypeString.equalsIgnoreCase("add schedule")) {
			return Constants.COMMAND_TYPE.ADD_SCHEDULE;
		} else if (commandTypeString.equalsIgnoreCase("add task")) {
			return Constants.COMMAND_TYPE.ADD_TASK;
		} else if (commandTypeString.equalsIgnoreCase("add deadline")) {
			return Constants.COMMAND_TYPE.ADD_DEADLINE;**/
			
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
		} else {
			return Constants.COMMAND_TYPE.INVALID;
		}
	}
	
	static private String removeFirstWord(String input) {
		return input.replace(getFirstWord(input), "").trim();
	}
	
	static private String getFirstWord(String input) {
		String inputString = input.trim().split("\\s+")[0];
		
		if(inputString.equals("update")) {
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
}
