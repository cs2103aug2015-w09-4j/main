package TGParser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import TGExceptions.AbnormalScheduleTimeException;
import TGExceptions.TaskDateExistenceException;
import TGUtils.Constants;

public class DateTimeHandler {

	public static Date dateConverter(String dateString) throws ParseException{
		DateFormat format = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
		format.setLenient(false); 
		
		Date date = format.parse(dateString);
		return date;
	}
	
	public static String defaultDateTimeCheck(String date, String eventType) {
		String modifiedString = date;
		String todayDate = getTodayDate();
		String thisYear = getThisYear();

		String[] dayMonthYearSplit = date.split(Constants.DAY_MONTH_YEAR_SPLIT);
		String[] hourMinuteSplit = date.split(Constants.HOUR_MINUTE_SPLIT);

		if(dayMonthYearSplit.length == 1) { //case 1: when a user does not enter a date, but with time.
			modifiedString = modifiedString.substring(0, modifiedString.length() - 5) + todayDate + modifiedString.substring(modifiedString.length() - 5);	
		} else if (hourMinuteSplit.length == 2 && dayMonthYearSplit.length == 2) {//case 2, when the user provides a time and date without year
			modifiedString = modifiedString.substring(0, modifiedString.length() - 6) + thisYear + modifiedString.substring(modifiedString.length() - 5);	
		} else if (hourMinuteSplit.length == 1 && dayMonthYearSplit.length != 2) {//case 3: when a user does not enter a time, but with a full date with year.
			if(eventType.equals(Constants.DEADLINE))
				modifiedString = modifiedString + Constants.SPACE + Constants.DEFAULT_DEADLINE_TIME;
			else if(eventType.equals(Constants.SCHEDULE))
				modifiedString = modifiedString + Constants.SPACE + Constants.DEFAULT_SCHEDULE_TIME;	
		} else if (hourMinuteSplit.length == 1 && dayMonthYearSplit.length == 2) {//case 4: when a user does not enter a time, but with a date without year.
			if(eventType.equals(Constants.DEADLINE))
				modifiedString = modifiedString + thisYear + Constants.DEFAULT_DEADLINE_TIME;
			else if(eventType.equals(Constants.SCHEDULE))
				modifiedString = modifiedString + thisYear + Constants.DEFAULT_SCHEDULE_TIME;
		}

		return modifiedString;
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
	public static boolean isNumber(String timeAndDate) throws NumberFormatException, ArrayIndexOutOfBoundsException {
		int hourInteger, minuteInteger, dayInteger, monthInteger, yearInteger;
		String hour, minute, day, month, year;
		
		String[] dayMonthYearSplit, hourMinuteSplit;
		String[] timeAndDateSplit = timeAndDate.split(Constants.SPACE);

		if (timeAndDateSplit.length == 1) {
		    dayMonthYearSplit = timeAndDateSplit[0].split(Constants.DAY_MONTH_YEAR_SPLIT);
			hourMinuteSplit = timeAndDateSplit[0].split(Constants.HOUR_MINUTE_SPLIT);

			if(dayMonthYearSplit.length == 1) {
				hour = hourMinuteSplit[0];
				hourInteger = Integer.parseInt(hour);

				minute = hourMinuteSplit[1];
				minuteInteger = Integer.parseInt(minute);
			} else if (hourMinuteSplit.length == 1) {
				if(dayMonthYearSplit.length == 2) {
					day = dayMonthYearSplit[0];
					dayInteger = Integer.parseInt(day);

					month = dayMonthYearSplit[1];
					monthInteger = Integer.parseInt(month);
				} else {
					day = dayMonthYearSplit[0];
					dayInteger = Integer.parseInt(day);

					month = dayMonthYearSplit[1];
					monthInteger = Integer.parseInt(month);

					year = dayMonthYearSplit[2];
					yearInteger = Integer.parseInt(year);
				
				}
			}
		} else {
			dayMonthYearSplit = timeAndDateSplit[0].split(Constants.DAY_MONTH_YEAR_SPLIT);
			hourMinuteSplit = timeAndDateSplit[1].split(Constants.HOUR_MINUTE_SPLIT);

			day = dayMonthYearSplit[0];
			dayInteger = Integer.parseInt(day);

			month = dayMonthYearSplit[1];
			monthInteger = Integer.parseInt(month);

			hour = hourMinuteSplit[0];
			hourInteger = Integer.parseInt(hour);

			minute = hourMinuteSplit[1];
			minuteInteger = Integer.parseInt(minute);
		}

		return true;
	}
	
	public static void checkTaskValidity(String input) throws TaskDateExistenceException {
		int inputLength = input.length();
		int inputLengthCheck = input.replaceAll(Constants.DATE_DETECTION, Constants.NULL).length();

		if(inputLength - inputLengthCheck != 0)
			throw new TaskDateExistenceException();

		inputLengthCheck = input.replaceAll(Constants.TIME_DETECTION, Constants.NULL).length();

		if(inputLength - inputLengthCheck != 0)
			throw new TaskDateExistenceException();

	}
	
	public static void startAndEndTimeValidation(String start, String end) throws AbnormalScheduleTimeException {
		if(start.compareTo(end) >= 0) {
			throw new AbnormalScheduleTimeException();
		}
	}

	private static String getTodayDate() {
		DateFormat df = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
		Calendar cal = Calendar.getInstance();

		String today = df.format(cal.getTime());
		return today.substring(0, today.length() - 6) + Constants.SPACE; 
	}
	
	private static String getThisYear() {
		DateFormat df = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
		Calendar cal = Calendar.getInstance();

		String today = df.format(cal.getTime());
		return Constants.DAY_MONTH_YEAR_SPLIT + today.substring(6, today.length() - 6) + Constants.SPACE; 		
	}
	
}
