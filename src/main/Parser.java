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
		switch (commandType) {
			case ADD:				
				String[] array = event.split("by ");			
				try {	//deadline
					endDate = dateConverter(array[array.length-1]);
					
					StringBuilder deadlineName = new StringBuilder();
					for (int i = 0; i < array.length-1; i++){
						deadlineName.append(array[i]);
					}
					
					tempCommand.setEventEnd(endDate);
					tempCommand.setEventName(deadlineName.toString());
					tempCommand.setType(Constants.COMMAND_TYPE.ADD_DEADLINE);
				} catch (ParseException e){
					try {	//schedule
						String[] array1 = event.split("from ");
						String[] array2 = array1[array1.length - 1].split("to ");
						endDate = dateConverter(array2[1]);
						startDate = dateConverter(array2[0]);
						
						StringBuilder scheduleName = new StringBuilder();
						for (int i = 0; i < array1.length-1; i++){
							scheduleName.append(array1[i]);
						}
						
						tempCommand.setEventStart(startDate);
						tempCommand.setEventEnd(endDate);
						tempCommand.setEventName(scheduleName.toString());
						tempCommand.setType(Constants.COMMAND_TYPE.ADD_SCHEDULE);
					} catch (Exception f){	//ParseException and IndexOutOfBoundsException
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
		} else if (commandTypeString.equalsIgnoreCase("update priority")){
			return Constants.COMMAND_TYPE.UPDATE_PRIORITY;
		} else if (commandTypeString.equalsIgnoreCase("done")){
			return Constants.COMMAND_TYPE.DONE;
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
}
