package test;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import com.tg.parser.DateTimeHandler;
import com.tg.parser.Parser;

import TGUtils.Command;
import TGUtils.Constants;

public class ParserTest {

	@Test
	public void addFloatingTaskNormalTest() throws Exception {

		Command c = Parser.parseCommand("add read a educational book");

		assertEquals(c.getType(), Constants.COMMAND_TYPE.ADD_TASK);
		assertEquals(c.getEventName(), "read a educational book");
	}
	
	@Test
	public void addFloatingTaskPriorityTest() throws Exception {

		Command c = Parser.parseCommand("add read a educational book mid");

		assertEquals(c.getType(), Constants.COMMAND_TYPE.ADD_TASK);
		assertEquals(c.getEventName(), "read a educational book");
		assertEquals(c.getEventPriority(), 2);
	}

	@Test
	public void addDeadlineNormalTest() throws Exception {

		Command c = Parser.parseCommand("add cook curry ayam by 21/10/2015 09:00");

		assertEquals(c.getType(), Constants.COMMAND_TYPE.ADD_DEADLINE);
		System.out.println(c.getEventName());
		assertEquals(c.getEventName(), "cook curry ayam");
	}

	@Test
	public void addDeadlinePriorityTest() throws Exception {

		Command c = Parser.parseCommand("add cook curry ayam by 21/10/2015 09:00 high");

		assertEquals(c.getType(), Constants.COMMAND_TYPE.ADD_DEADLINE);
		System.out.println(c.getEventName());
		assertEquals(c.getEventName(), "cook curry ayam");
		assertEquals(c.getEventPriority(), 3);
	}
	
	@Test
	public void addDeadlineNormalWithFlexibleDateTest() throws Exception {
		String testString;
		Date testDate;

		Command c = Parser.parseCommand("add cook curry ayam by 21/10/2015");

		assertEquals(c.getType(), Constants.COMMAND_TYPE.ADD_DEADLINE);
		System.out.println(c.getEventName());
		assertEquals(c.getEventName(), "cook curry ayam");
		assertEquals(c.getEventEnd().toString(), "Wed Oct 21 23:59:00 SGT 2015");
		
		Command c1 = Parser.parseCommand("add cook curry ayam by 21/10");

		assertEquals(c1.getType(), Constants.COMMAND_TYPE.ADD_DEADLINE);
		System.out.println(c1.getEventName());
		assertEquals(c1.getEventName(), "cook curry ayam");
		
		testString = DateTimeHandler.defaultDateTimeCheck("21/10", Constants.DEADLINE);
		testDate = DateTimeHandler.dateConverter(testString);
		assertEquals(c1.getEventEnd(), testDate);
		
		Command c2 = Parser.parseCommand("add cook curry ayam by 15:09");

		assertEquals(c2.getType(), Constants.COMMAND_TYPE.ADD_DEADLINE);
		System.out.println(c2.getEventName());
		assertEquals(c2.getEventName(), "cook curry ayam");
		
		testString = DateTimeHandler.defaultDateTimeCheck("15:09", Constants.DEADLINE);
		testDate = DateTimeHandler.dateConverter(testString);
		assertEquals(c2.getEventEnd(), testDate);
		
		Command c3 = Parser.parseCommand("add cook curry ayam by 21/10 15:05");

		assertEquals(c3.getType(), Constants.COMMAND_TYPE.ADD_DEADLINE);
		System.out.println(c3.getEventName());
		assertEquals(c3.getEventName(), "cook curry ayam");
		
		testString = DateTimeHandler.defaultDateTimeCheck("21/10 15:05", Constants.DEADLINE);
		testDate = DateTimeHandler.dateConverter(testString);
		assertEquals(c3.getEventEnd(), testDate);
	}
	
	@Test
	public void addDeadlinePriorityWithFlexibleDateTimeTest() throws Exception {
		String testString;
		Date testDate;

		Command c = Parser.parseCommand("add cook curry ayam by 21/10/2015 mid");

		assertEquals(c.getType(), Constants.COMMAND_TYPE.ADD_DEADLINE);
		System.out.println(c.getEventName());
		assertEquals(c.getEventName(), "cook curry ayam");
		assertEquals(c.getEventEnd().toString(), "Wed Oct 21 23:59:00 SGT 2015");
		assertEquals(c.getEventPriority(), 2);
		
		Command c1 = Parser.parseCommand("add cook curry ayam by 21/10 high");

		assertEquals(c1.getType(), Constants.COMMAND_TYPE.ADD_DEADLINE);
		System.out.println(c1.getEventName());
		assertEquals(c1.getEventName(), "cook curry ayam");
		
		testString = DateTimeHandler.defaultDateTimeCheck("21/10", Constants.DEADLINE);
		testDate = DateTimeHandler.dateConverter(testString);
		assertEquals(c1.getEventEnd(), testDate);
		assertEquals(c1.getEventPriority(), 3);
		
		Command c2 = Parser.parseCommand("add cook curry ayam by 15:09 mid");

		assertEquals(c2.getType(), Constants.COMMAND_TYPE.ADD_DEADLINE);
		System.out.println(c2.getEventName());
		assertEquals(c2.getEventName(), "cook curry ayam");
		
		testString = DateTimeHandler.defaultDateTimeCheck("15:09", Constants.DEADLINE);
		testDate = DateTimeHandler.dateConverter(testString);
		assertEquals(c2.getEventEnd(), testDate);
		assertEquals(c2.getEventPriority(), 2);
		
		Command c3 = Parser.parseCommand("add cook curry ayam by 21/10 15:05 low");

		assertEquals(c3.getType(), Constants.COMMAND_TYPE.ADD_DEADLINE);
		System.out.println(c3.getEventName());
		assertEquals(c3.getEventName(), "cook curry ayam");
		
		testString = DateTimeHandler.defaultDateTimeCheck("21/10 15:05", Constants.DEADLINE);
		testDate = DateTimeHandler.dateConverter(testString);
		assertEquals(c3.getEventEnd(), testDate);
		assertEquals(c3.getEventPriority(), 1);
	}

	@Test
	public void addScheduleNormalTest() throws Exception {

		Command c = Parser.parseCommand("add bring Tang Hao to school from 21/10/2015 09:00 to 22/02/2016 09:00");

		assertEquals(c.getType(), Constants.COMMAND_TYPE.ADD_SCHEDULE);
		assertEquals(c.getEventName(), "bring Tang Hao to school");
	}
	
	@Test
	public void addSchedulePriorityTest() throws Exception {

		Command c = Parser.parseCommand("add bring Tang Hao to school from 21/10/2015 09:00 to 22/02/2016 09:00 low");

		assertEquals(c.getType(), Constants.COMMAND_TYPE.ADD_SCHEDULE);
		assertEquals(c.getEventName(), "bring Tang Hao to school");
		assertEquals(c.getEventPriority(), 1);
	}
	
	@Test
	public void addScheduleNormalWithFlexibleDateTimeTest() throws Exception {
		String testStringStart, testStringEnd;
		Date testDateStart, testDateEnd;

		Command c = Parser.parseCommand("add cook curry ayam from 21/10/2015 to 24/10/2015");

		assertEquals(c.getType(), Constants.COMMAND_TYPE.ADD_SCHEDULE);
		System.out.println(c.getEventName());
		assertEquals(c.getEventName(), "cook curry ayam");
		
		testStringStart = DateTimeHandler.defaultDateTimeCheck("21/10/2015", Constants.SCHEDULE);
		testDateStart = DateTimeHandler.dateConverter(testStringStart);
		testStringEnd = DateTimeHandler.defaultDateTimeCheck("24/10/2015", Constants.SCHEDULE);
		testDateEnd = DateTimeHandler.dateConverter(testStringEnd);
		
		assertEquals(c.getEventEnd(), testDateEnd);
		assertEquals(c.getEventStart(), testDateStart);
		
		Command c1 = Parser.parseCommand("add cooking for parents from 16:00 to 19:00");

		assertEquals(c1.getType(), Constants.COMMAND_TYPE.ADD_SCHEDULE);
		System.out.println(c1.getEventName());
		assertEquals(c1.getEventName(), "cooking for parents");
		
		testStringStart = DateTimeHandler.defaultDateTimeCheck("16:00", Constants.SCHEDULE);
		testDateStart = DateTimeHandler.dateConverter(testStringStart);
		testStringEnd = DateTimeHandler.defaultDateTimeCheck("19:00", Constants.SCHEDULE);
		testDateEnd = DateTimeHandler.dateConverter(testStringEnd);
		
		assertEquals(c1.getEventEnd(), testDateEnd);
		assertEquals(c1.getEventStart(), testDateStart);
		
		Command c2 = Parser.parseCommand("add GEK1901 Presentation from 21/10 to 24/10");

		assertEquals(c2.getType(), Constants.COMMAND_TYPE.ADD_SCHEDULE);
		System.out.println(c2.getEventName());
		assertEquals(c2.getEventName(), "GEK1901 Presentation");
		
		testStringStart = DateTimeHandler.defaultDateTimeCheck("21/10", Constants.SCHEDULE);
		testDateStart = DateTimeHandler.dateConverter(testStringStart);
		testStringEnd = DateTimeHandler.defaultDateTimeCheck("24/10", Constants.SCHEDULE);
		testDateEnd = DateTimeHandler.dateConverter(testStringEnd);
		
		assertEquals(c2.getEventEnd(), testDateEnd);
		assertEquals(c2.getEventStart(), testDateStart);
		
		Command c3 = Parser.parseCommand("add guitar rehearsal from 21/10 15:05 to 21/10 16:05");

		assertEquals(c3.getType(), Constants.COMMAND_TYPE.ADD_SCHEDULE);
		System.out.println(c3.getEventName());
		assertEquals(c3.getEventName(), "guitar rehearsal");
		
		testStringStart = DateTimeHandler.defaultDateTimeCheck("21/10 15:05", Constants.SCHEDULE);
		testDateStart = DateTimeHandler.dateConverter(testStringStart);
		testStringEnd = DateTimeHandler.defaultDateTimeCheck("21/10 16:05", Constants.SCHEDULE);
		testDateEnd = DateTimeHandler.dateConverter(testStringEnd);
		
		assertEquals(c3.getEventEnd(), testDateEnd);
		assertEquals(c3.getEventStart(), testDateStart);
		
	}
	
	@Test
	public void addSchedulePriorityWithFlexibleDateTimeTest() throws Exception {
		String testStringStart, testStringEnd;
		Date testDateStart, testDateEnd;

		Command c = Parser.parseCommand("add cook curry ayam from 21/10/2015 to 24/10/2015 high");

		assertEquals(c.getType(), Constants.COMMAND_TYPE.ADD_SCHEDULE);
		System.out.println(c.getEventName());
		assertEquals(c.getEventName(), "cook curry ayam");
		assertEquals(c.getEventPriority(), 3);
		
		testStringStart = DateTimeHandler.defaultDateTimeCheck("21/10/2015", Constants.SCHEDULE);
		testDateStart = DateTimeHandler.dateConverter(testStringStart);
		testStringEnd = DateTimeHandler.defaultDateTimeCheck("24/10/2015", Constants.SCHEDULE);
		testDateEnd = DateTimeHandler.dateConverter(testStringEnd);
		
		assertEquals(c.getEventEnd(), testDateEnd);
		assertEquals(c.getEventStart(), testDateStart);
		
		Command c1 = Parser.parseCommand("add cooking for parents from 16:00 to 19:00 mid");

		assertEquals(c1.getType(), Constants.COMMAND_TYPE.ADD_SCHEDULE);
		System.out.println(c1.getEventName());
		assertEquals(c1.getEventName(), "cooking for parents");
		assertEquals(c1.getEventPriority(), 2);
		
		testStringStart = DateTimeHandler.defaultDateTimeCheck("16:00", Constants.SCHEDULE);
		testDateStart = DateTimeHandler.dateConverter(testStringStart);
		testStringEnd = DateTimeHandler.defaultDateTimeCheck("19:00", Constants.SCHEDULE);
		testDateEnd = DateTimeHandler.dateConverter(testStringEnd);
		
		assertEquals(c1.getEventEnd(), testDateEnd);
		assertEquals(c1.getEventStart(), testDateStart);
		
		Command c2 = Parser.parseCommand("add GEK1901 Presentation from 21/10 to 24/10 low");

		assertEquals(c2.getType(), Constants.COMMAND_TYPE.ADD_SCHEDULE);
		System.out.println(c2.getEventName());
		assertEquals(c2.getEventName(), "GEK1901 Presentation");
		assertEquals(c2.getEventPriority(), 1);
		
		testStringStart = DateTimeHandler.defaultDateTimeCheck("21/10", Constants.SCHEDULE);
		testDateStart = DateTimeHandler.dateConverter(testStringStart);
		testStringEnd = DateTimeHandler.defaultDateTimeCheck("24/10", Constants.SCHEDULE);
		testDateEnd = DateTimeHandler.dateConverter(testStringEnd);
		
		assertEquals(c2.getEventEnd(), testDateEnd);
		assertEquals(c2.getEventStart(), testDateStart);
		
		Command c3 = Parser.parseCommand("add guitar rehearsal from 21/10 15:05 to 21/10 16:05 high");

		assertEquals(c3.getType(), Constants.COMMAND_TYPE.ADD_SCHEDULE);
		System.out.println(c3.getEventName());
		assertEquals(c3.getEventName(), "guitar rehearsal");
		assertEquals(c.getEventPriority(), 3);
		
		testStringStart = DateTimeHandler.defaultDateTimeCheck("21/10 15:05", Constants.SCHEDULE);
		testDateStart = DateTimeHandler.dateConverter(testStringStart);
		testStringEnd = DateTimeHandler.defaultDateTimeCheck("21/10 16:05", Constants.SCHEDULE);
		testDateEnd = DateTimeHandler.dateConverter(testStringEnd);
		
		assertEquals(c3.getEventEnd(), testDateEnd);
		assertEquals(c3.getEventStart(), testDateStart);
		
	}

	@Test 
	public void deleteTest() throws Exception {
		
		Command c = Parser.parseCommand("delete t1");
		
		assertEquals(c.getType(), Constants.COMMAND_TYPE.DELETE);
		assertEquals(c.getDisplayedIndex(), "t1");
	}
	
}
