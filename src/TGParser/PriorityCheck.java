package TGParser;

public class PriorityCheck {

	// Attributes
	private static String[] inputArray;
	private static String priority;
	
	public PriorityCheck(String input) {
		inputArray = input.split(" ");
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
		
		//inputArray[inputArray.length - 1] = "";
		
		String modifiedEvent = "";
		String finalResult = "";
		
		for(int i = 0; i < inputArray.length - 1; i++) {
			modifiedEvent = modifiedEvent + inputArray[i] + " ";
		}
		
		finalResult = modifiedEvent.substring(0, modifiedEvent.length() - 1);	
		return finalResult;
	}

	// Private methods
	private int checkPriority(String input) {
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

	private boolean hasPriority(String input) {

		return (input.equalsIgnoreCase("HIGH") || input.equalsIgnoreCase("MID") || input.equalsIgnoreCase("LOW"));		
	}
}
