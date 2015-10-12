package main;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Parser {
	static public Command parseCommand(String input) throws ParseException, IndexOutOfBoundsException{
		Command tempCommand = new Command();
		String command = getFirstWord(input);
		Constants.COMMAND_TYPE commandType = findCommandType(command);
		String event = removeFirstWord(input);
		Date endDate, startDate;
		String displayedIndex;
		tempCommand.setType(commandType);
		tempCommand.setIsUserCommand(true);
		switch (commandType) {
			case ADD_DEADLINE:
				String[] array = event.split("by ");
				endDate = dateConverter(array[array.length-1]);
				tempCommand.setEventEnd(endDate);
				tempCommand.setEventName(event);
				break;
			case ADD_SCHEDULE:
				String[] array1 = event.split("from ");
				String[] array2 = array1[array1.length - 1].split("to ");
				endDate = dateConverter(array2[1]);
				startDate = dateConverter(array2[0]);
				tempCommand.setEventStart(startDate);
				tempCommand.setEventStart(endDate);
				tempCommand.setEventName(event);
				break;
			case ADD_TASK:
				tempCommand.setEventName(event);
				break;
			case DISPLAY:
				break;
			case UPDATE:
				displayedIndex = getFirstWord(event);
				String newName;
				newName = event.split("\"")[1];
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
		if (commandTypeString.equalsIgnoreCase("add schedule")) {
			return Constants.COMMAND_TYPE.ADD_SCHEDULE;
		} else if (commandTypeString.equalsIgnoreCase("add task")) {
			return Constants.COMMAND_TYPE.ADD_TASK;
		} else if (commandTypeString.equalsIgnoreCase("add deadline")) {
			return Constants.COMMAND_TYPE.ADD_DEADLINE;
		} else if (commandTypeString.equalsIgnoreCase("display")) {
			return Constants.COMMAND_TYPE.DISPLAY;
		} else if (commandTypeString.equalsIgnoreCase("delete")) {
			return Constants.COMMAND_TYPE.DELETE;
		} else if (commandTypeString.equalsIgnoreCase("exit")) {
			return Constants.COMMAND_TYPE.EXIT;
		} else if (commandTypeString.equalsIgnoreCase("undo")) {
			return Constants.COMMAND_TYPE.UNDO;
		} else if (commandTypeString.equalsIgnoreCase("update name")) {
			return Constants.COMMAND_TYPE.UPDATE;
		} else {
			return Constants.COMMAND_TYPE.INVALID;
		}
	}
	static private String removeFirstWord(String input) {
		return input.replace(getFirstWord(input), "").trim();
	}
	
	static private String getFirstWord(String input) {
		String inputString = input.trim().split("\\s+")[0];
		
		if(inputString.equals("add") || inputString.equals("update")) {
			inputString += " " + input.trim().split("\\s+")[1];
		}	
		return inputString;
	}
	
	private static Date dateConverter(String dateString) throws ParseException {
		DateFormat format = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
		Date date = format.parse(dateString);
		return date;
	}
}
