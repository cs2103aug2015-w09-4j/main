//import java.io.FileReader;
import java.io.FileWriter;
import java.lang.String;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
//import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
//import java.io.FileNotFoundException;
import java.util.ArrayList;
//import java.util.Collections; for future use
import java.util.Scanner;
import java.util.Date;
//import java.util.Locale;
import java.util.HashMap;

public class TangGuo {
	
	//private static ArrayList<String> inputLines;
	private static String fileName;
	private Scanner scanner = new Scanner(System.in);
	private TGStorageManager storage;
	private HashMap<Integer, Integer> idHash;
	private int eventCounter;
	
	public TangGuo(String file) {
		fileName = file;
		storage = new TGStorageManager(fileName);
		idHash = new HashMap<Integer, Integer>();
		eventCounter = 0;
	}
	
	public static void main(String[] args) throws IOException, ParseException {
		TangGuo tg = new TangGuo(args[0]);
		showToUser(String.format(Constants.TANGGUO_START, fileName));
		
		while (true) {
			tg.runUserInput();
		}
	}
	
	private void runUserInput() throws ParseException {
		showToUser("input: ");
		String input = scanner.nextLine();
		String output = executeinputs(input);
		showToUser(output);
	}
	
	private static void showToUser(String display) {
		System.out.println(display);
	}
	
	public String executeinputs(String input) throws ParseException {
		
		String command = getFirstWord(input);
		Constants.COMMAND_TYPE commandType = findCommandType(command);
		String sentence = removeFirstWord(input);
		
		switch (commandType) {
			case ADD_DEADLINE:
				return addDeadline(sentence);
			case ADD_SCHEDULE:
				return addSchedule(sentence);
			case ADD_TASK:
				return addTask(sentence);
			case DISPLAY:
				return displayTangGuo();
			case DELETE:
				return deleteEvent(sentence);
			case CLEAR:
				return clearTangGuo();
			case EXIT:
				//fileUpdate();
				showToUser(Constants.TANGGUO_EXIT);
				System.exit(0);
			case INVALID:
				return Constants.TANGGUO_INVALID_COMMAND;
			default:
				return "Congratulations, you won!";
		}
	}
	
	private String addDeadline(String event) throws ParseException {
		
		// add into storage
		Date endDate = extractDate(event);
		String eventName = event.substring(0, event.length() - Constants.DATE_LENGTH);
		
		storage.addDeadline(eventName, endDate);
		
		//add into hash
		eventCounter++;
		addToHash(eventCounter, storage.getCurrentIndex());
		
		//fileUpdate();
		return String.format(Constants.TANGGUO_ADD_SUCCESS, fileName, event);
	}
	
	private String addSchedule(String event) throws ParseException {
		
		// add into storage
		Date endDate = extractDate(event);
		Date startDate = extractDate(event.substring(0, event.length() - Constants.DATE_LENGTH));
		String eventName = event.substring(0, event.length() - (2 * Constants.DATE_LENGTH));
		
		storage.addSchedule(eventName, startDate, endDate);
		
		//add into hash
		eventCounter++;
		addToHash(eventCounter, storage.getCurrentIndex());
		
		//fileUpdate();
		return String.format(Constants.TANGGUO_ADD_SUCCESS, fileName, event);
	}
	
	private String addTask(String event) {
		// add into storage
		storage.addTask(event);
		
		//add into hash
		eventCounter++;
		addToHash(eventCounter, storage.getCurrentIndex());
		System.out.println(idHash);
		
		//fileUpdate();
		return String.format(Constants.TANGGUO_ADD_SUCCESS, fileName, event);
	}
	
	private String displayTangGuo() {
		String printOut = "";
		int j = 0;
		
		if (storage.getDeadlineCache().isEmpty() && storage.getTaskCache().isEmpty() && storage.getScheduleCache().isEmpty()) {
			return String.format(Constants.TANGGUO_EMPTY_FILE, fileName);
		}
		
		for (int i = 0; i < storage.getDeadlineCache().size(); i++) {
			printOut = printOut + (j++ + 1) + ". " + storage.getDeadlineCache().get(i).name + "\n";
		}
		
		for (int i = 0; i < storage.getTaskCache().size(); i++) {
			printOut = printOut + (j++ + 1) + ". " + storage.getTaskCache().get(i).name + "\n";
		}
		
		for (int i = 0; i < storage.getScheduleCache().size(); i++) {
			printOut = printOut + (j++ + 1) + ". " + storage.getScheduleCache().get(i).name + "\n";
		}
		
		return printOut;
	}
	
	private String deleteEvent(String number) {
		System.out.println("IDHash keys: " + idHash.keySet());
		System.out.println("IDHash values: " + idHash.values());
		
		String deletedLine = "";
		int index;
		
		try {
			System.out.println(idHash);	//for tutorial test
			System.out.println(idHash.get(Integer.parseInt(number)));
			index = idHash.get(number);
		} catch (NumberFormatException e) {
			return Constants.TANGGUO_WRONG_DELETE;
		}
		
		try {
			storage.deleteEventByID(index);
		} catch (IndexOutOfBoundsException e) {
			return Constants.TANGGUO_OUT_BOUNDS;
		}
		// TODO Storage deletion. (Storage ID is different from the number here)
		return String.format(Constants.TANGGUO_DELETE_SUCCESS, fileName, deletedLine);
		
	}
	
	private String clearTangGuo() {
		
		storage.getTaskCache().clear();
		storage.getDeadlineCache().clear();
		storage.getScheduleCache().clear();
		
		return String.format(Constants.TANGGUO_CLEAR, fileName);
	}
	
	private void addToHash(int counter, int index) {
		idHash.put(counter, index);
	}
	
	private Date extractDate(String event) throws ParseException {
		
		boolean isFirstSpaceFound = false;
		String date = null;
		
		for (int i = event.length() - 1; i >= 0; i--) {
			
			if (event.charAt(i) == ' ') {
				if (!isFirstSpaceFound)
					isFirstSpaceFound = true;
				else {
					date = event.substring(i);
					break;
				}
			}
		}
		return dateConverter(date);
	}
	
	private Date dateConverter(String dateString) throws ParseException {
		DateFormat format = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
		Date date = format.parse(dateString);
		return date;
	}
	
	private Constants.COMMAND_TYPE findCommandType(String commandTypeString) {
		if (commandTypeString.equalsIgnoreCase("addschedule")) {
			return Constants.COMMAND_TYPE.ADD_SCHEDULE;
		} else if (commandTypeString.equalsIgnoreCase("addtask")) {
			return Constants.COMMAND_TYPE.ADD_TASK;
		} else if (commandTypeString.equalsIgnoreCase("adddeadline")) {
			return Constants.COMMAND_TYPE.ADD_DEADLINE;
		} else if (commandTypeString.equalsIgnoreCase("display")) {
			return Constants.COMMAND_TYPE.DISPLAY;
		} else if (commandTypeString.equalsIgnoreCase("delete")) {
			return Constants.COMMAND_TYPE.DELETE;
		} else if (commandTypeString.equalsIgnoreCase("clear")) {
			return Constants.COMMAND_TYPE.CLEAR;
		} else if (commandTypeString.equalsIgnoreCase("exit")) {
			return Constants.COMMAND_TYPE.EXIT;
		} else {
			return Constants.COMMAND_TYPE.INVALID;
		}
	}
	
	private String getFirstWord(String input) {
		String inputString = input.trim().split("\\s+")[0];
		return inputString;
	}
	
	private String removeFirstWord(String input) {
		return input.replace(getFirstWord(input), "").trim();
	}
}