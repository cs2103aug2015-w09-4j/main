//@@author A0124503W
package com.tg.parser;

import TGUtils.Constants;

public class PropertyCheck {

	// Attributes
	private static String[] inputArray;
	private static int categoryIndex;
	private static int priorityIndex;
	
	public PropertyCheck(String input) {
		inputArray = input.split(Constants.SPACE);
		priorityIndex = findProperty(inputArray, Constants.ADD_PRIORITY);
	}
	
	// Class methods
	public boolean containsPriority() {
		return (priorityIndex != Constants.ADD_NULL);
	}

	public int getPriority() {
		int priorityNumber = checkPriority(inputArray[priorityIndex]);
		return priorityNumber;
	}
	
	public String removePropertyFromEventName() {
		String modifiedEvent = Constants.NULL;
		String finalResult = Constants.NULL;
		
		for(int i = 0; i < priorityIndex - 1; i++) {
			modifiedEvent = modifiedEvent + inputArray[i] + Constants.SPACE;
		}
		
		finalResult = modifiedEvent.substring(0, modifiedEvent.length() - 1);	
		return finalResult;
	}

	public int checkPriority(String input) {
		if (input.equalsIgnoreCase(Constants.STRING_HIGH)) {
			return Constants.INT_HIGH;
		} else if (input.equalsIgnoreCase(Constants.STRING_MID)) {
			return Constants.INT_MID;
		} else if (input.equalsIgnoreCase(Constants.STRING_LOW)) {
			return Constants.INT_LOW;
		} else {
			return Constants.DEFAULT_PRIORITY;
		}
	}
	
	// private property
	private int findProperty(String[] array, String property) {
		int result = -1;
		for (int i = 0; i < array.length; i ++) {
			if (array[i].equals(property)) {
				result = i + 1;
			}
		}
		return result;
	}
	
}
