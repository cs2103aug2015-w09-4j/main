//import java.io.FileReader;
//import java.io.FileWriter;
import java.lang.String;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
import java.io.IOException;
//import java.io.FileNotFoundException;
//import java.util.ArrayList;
//import java.util.Collections; for future use
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Date;
//import java.util.Locale;


public class TangGuo {
	
	//private static ArrayList<String> inputLines;
	private static String fileName;
	private Scanner scanner = new Scanner(System.in);
	private TGStorageManager storage;
	private ArrayList<Integer> taskIDCache;
	private ArrayList<Integer> scheduleIDCache;
	private ArrayList<Integer> deadlineIDCache;
	
	public TangGuo(String file) {
		fileName = file;
		storage = new TGStorageManager(fileName);
		initialiseIDCaches();
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
		String[] array = event.split("by ");
		
		Date endDate = dateConverter(array[array.length-1]);
		
		deadlineIDCache.add(storage.getCurrentIndex());
		storage.addDeadline(event, endDate);
		
		//fileUpdate();
		return String.format(Constants.TANGGUO_ADD_SUCCESS, fileName, event);
	}
	
	private String addSchedule(String event) throws ParseException {
		
		// add into storage
		String[] array1 = event.split("from ");
		String[] array2 = array1[array1.length - 1].split("to ");

		Date endDate = dateConverter(array2[1]);
		Date startDate = dateConverter(array2[0]);
		
		scheduleIDCache.add(storage.getCurrentIndex());
		storage.addSchedule(event, startDate, endDate);
	
		//fileUpdate();
		return String.format(Constants.TANGGUO_ADD_SUCCESS, fileName, event);
	}
	
	private String addTask(String event) {
		// add into storage
		taskIDCache.add(storage.getCurrentIndex());
		storage.addTask(event);
		
		//add into hash

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
			printOut = printOut + (j++ + 1) + ". " + storage.getDeadlineCache().get(i).getName() + "\n";
		}
		
		for (int i = 0; i < storage.getTaskCache().size(); i++) {
			printOut = printOut + (j++ + 1) + ". " + storage.getTaskCache().get(i).getName() + "\n";
		}
		
		for (int i = 0; i < storage.getScheduleCache().size(); i++) {
			printOut = printOut + (j++ + 1) + ". " + storage.getScheduleCache().get(i).getName() + "\n";
		}
		
		return printOut;
	}
	
	private String clearTangGuo() {
		
		storage.getTaskCache().clear();
		storage.getDeadlineCache().clear();
		storage.getScheduleCache().clear();
		
		return String.format(Constants.TANGGUO_CLEAR, fileName);
	}
	
	private Date dateConverter(String dateString) throws ParseException {
		DateFormat format = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
		Date date = format.parse(dateString);
		return date;
	}
	
	private void initialiseIDCaches() {
		
		taskIDCache = new ArrayList<Integer>();
		scheduleIDCache = new ArrayList<Integer>();
		deadlineIDCache = new ArrayList<Integer>();
		
		ArrayList<Event> tasks = storage.getTaskCache();
		ArrayList<Event> schedules = storage.getScheduleCache(); 
		ArrayList<Event> deadlines = storage.getDeadlineCache();
		
		if(!tasks.isEmpty()) {
			for(int i = 0; i < tasks.size(); i++) {
				taskIDCache.add(tasks.get(i).getID());
			}
		}
		
		if(!schedules.isEmpty()) {
			for(int i = 0; i < schedules.size(); i++) {
				scheduleIDCache.add(schedules.get(i).getID());
			}
		}	
		
		if(!deadlines.isEmpty()) {	
			for(int i = 0; i < deadlines.size(); i++) {
				deadlineIDCache.add(deadlines.get(i).getID());
			}
		}		
	}
	
	private Constants.COMMAND_TYPE findCommandType(String commandTypeString) {
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
		
		if(inputString.equals("add")) {
			inputString += " " + input.trim().split("\\s+")[1];
		}
		
		return inputString;
	}
	
	private String removeFirstWord(String input) {
		return input.replace(getFirstWord(input), "").trim();
	}
}