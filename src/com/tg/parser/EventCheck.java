//@@author A0124503W
package com.tg.parser;

import java.text.ParseException;

import TGUtils.Constants;

public class EventCheck {

	// Attributes
	private static String[] deadlineCheckArray;
	private static String[] scheduleStartAndEnd;
	private static String[] scheduleCheckArray;

	private static String deadlineDateAndTime;
	private static String scheduleStartDateAndTime;
	private static String scheduleEndDateAndTime;

	public EventCheck(String input) {
		deadlineCheckArray = input.split(Constants.DEADLINE_SPLIT);
		deadlineDateAndTime = deadlineCheckArray[deadlineCheckArray.length - 1];

		scheduleStartAndEnd = input.split(Constants.SCHEDULE_FIRST_SPLIT);
		scheduleCheckArray = scheduleStartAndEnd[scheduleStartAndEnd.length - 1].split(Constants.SCHEDULE_SECOND_SPLIT);
		scheduleStartDateAndTime = scheduleCheckArray[0];

		if(scheduleCheckArray.length > 1)
			scheduleEndDateAndTime = scheduleCheckArray[1];
	}

	public String getDeadlineDateAndTime() {
		return deadlineDateAndTime;
	}

	public String getScheduleStartDateAndTime() {
		return scheduleStartDateAndTime;
	}

	public String getScheduleEndDateAndTime() {
		return scheduleEndDateAndTime;
	}

	public EventCheck reInitialize(String input) {
		EventCheck newEventCheck = new EventCheck(input);
		return newEventCheck;
	}

	public boolean possibleDate(String input) throws NumberFormatException, ArrayIndexOutOfBoundsException {
		boolean isPossibleDate = false;

		if (input.equals(Constants.DEADLINE)) {
			isPossibleDate = DateTimeHandler.isNumber(getDeadlineDateAndTime());
		} else if (input.equals(Constants.SCHEDULE_START)) {
			isPossibleDate = DateTimeHandler.isNumber(getScheduleStartDateAndTime());
		} else if (input.equals(Constants.SCHEDULE_END)) {
			isPossibleDate = DateTimeHandler.isNumber(getScheduleEndDateAndTime());
		}

		return isPossibleDate;
	}

	public String getEventName(String input) {
		String eventName = Constants.NULL;

		if (input.equals(Constants.DEADLINE)) {
			eventName = getName(Constants.DEADLINE_SPLIT, deadlineCheckArray);
		} else if (input.equals(Constants.SCHEDULE)) {
			eventName = getName(Constants.SCHEDULE_FIRST_SPLIT, scheduleStartAndEnd);
		}

		return eventName;
	}

	public void isProperFloatingTaskCheck() throws ParseException {
		if(deadlineCheckArray.length > 1) {
			DateTimeHandler.checkTaskValidity(getDeadlineDateAndTime());
		}

		if(scheduleStartAndEnd.length > 1 && scheduleCheckArray.length > 1) {
			DateTimeHandler.checkTaskValidity(getScheduleEndDateAndTime());
			DateTimeHandler.checkTaskValidity(getScheduleStartDateAndTime());
		}
	}

	private String getName(String splitSeq, String[] array) {
		StringBuilder name = new StringBuilder();
		for (int i = 0; i < array.length - 1; i++) {
			name.append(array[i]);
			if (i != array.length - 2) {
				name.append(splitSeq);
			}
		}
		return name.toString();
	}

}
