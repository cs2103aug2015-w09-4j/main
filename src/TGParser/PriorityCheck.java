package TGParser;

//@@author A0124503W
import TGUtils.Constants;

public class PriorityCheck {

	// Attributes
	private static String[] inputArray;
	private static String priority;
	
	public PriorityCheck(String input) {
		inputArray = input.split(Constants.SPACE);
		priority = inputArray[inputArray.length - 1];
	}

	// Class methods
	public boolean containsPriority() {
		boolean hasPriority = hasPriority(priority);
		return hasPriority;
	}

	public int getPriorityNumber() {
		int priorityNumber = checkPriority(priority);
		return priorityNumber;
	}
	
	public String removePriorityFromEventName() {
		String modifiedEvent = Constants.NULL;
		String finalResult = Constants.NULL;
		
		for(int i = 0; i < inputArray.length - 1; i++) {
			modifiedEvent = modifiedEvent + inputArray[i] + Constants.SPACE;
		}
		
		finalResult = modifiedEvent.substring(0, modifiedEvent.length() - 1);	
		return finalResult;
	}

	// Private methods
	private int checkPriority(String input) {
		if (input.equalsIgnoreCase(Constants.HIGH)) {
			return 3;
		} else if (input.equalsIgnoreCase(Constants.MID)) {
			return 2;
		} else if (input.equalsIgnoreCase(Constants.LOW)) {
			return 1;
		} else {
			return -1;
		}
	}

	private boolean hasPriority(String input) {
		return (input.equalsIgnoreCase(Constants.HIGH) || input.equalsIgnoreCase(Constants.MID) || input.equalsIgnoreCase(Constants.LOW));		
	}
}
