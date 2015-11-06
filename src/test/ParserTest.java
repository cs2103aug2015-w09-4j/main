package test;

import static org.junit.Assert.*;

import org.junit.Test;

import TGParser.Parser;
import TGUtils.Command;
import TGUtils.Constants;

public class ParserTest {

	@Test
	public void addTaskTest() throws Exception {

		Command c = Parser.parseCommand("add ayam a test");

		assertEquals(c.getType(), Constants.COMMAND_TYPE.ADD_TASK);
		assertEquals(c.getEventName(), "ayam a test");
	}

	@Test
	public void addDeadlineTest() throws Exception {

		Command c = Parser.parseCommand("add cook curry ayam by 21/10/2015 09:00");

		assertEquals(c.getType(), Constants.COMMAND_TYPE.ADD_DEADLINE);
		System.out.println(c.getEventName());
		assertEquals(c.getEventName(), "cook curry ayam");
	}

	@Test
	public void addScheduleTest() throws Exception {

		Command c = Parser.parseCommand("add bring ayam to school from 21/10/2015 09:00 to 22/02/2016 09:00");

		assertEquals(c.getType(), Constants.COMMAND_TYPE.ADD_SCHEDULE);
		assertEquals(c.getEventName(), "bring ayam to school");
	}
	
	@Test 
	public void deleteTest() throws Exception {
		
		Command c = Parser.parseCommand("delete t1");
		
		assertEquals(c.getType(), Constants.COMMAND_TYPE.DELETE);
		assertEquals(c.getDisplayedIndex(), "t1");
	}
	
}
