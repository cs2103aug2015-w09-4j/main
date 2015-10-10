package test;

import static org.junit.Assert.*;
import main.*;

import org.junit.Test;

public class ParserTest {

	@Test
	public void addTaskTest() throws Exception {

		Command c = Parser.parseCommand("add task ayam a test");

		assertEquals(c.getType(), Constants.COMMAND_TYPE.ADD_TASK);
		assertEquals(c.getEventName(), "ayam a test");
	}

	@Test
	public void addDeadlineTest() throws Exception {

		Command c = Parser.parseCommand("add deadline cook curry ayam by 21/10/2015 09:00");

		assertEquals(c.getType(), Constants.COMMAND_TYPE.ADD_DEADLINE);
		assertEquals(c.getEventName(), "cook curry ayam by 21/10/2015 09:00");
	}

	@Test
	public void addScheduleTest() throws Exception {

		Command c = Parser.parseCommand("add schedule bring ayam to school from 21/10/2015 09:00 to 22/02/2016 09:00");

		assertEquals(c.getType(), Constants.COMMAND_TYPE.ADD_SCHEDULE);
		assertEquals(c.getEventName(), "bring ayam to school from 21/10/2015 09:00 to 22/02/2016 09:00");
	}
	
	@Test 
	public void deleteTest() throws Exception {
		
		Command c = Parser.parseCommand("delete t1");
		
		assertEquals(c.getType(), Constants.COMMAND_TYPE.DELETE);
		assertEquals(c.getEventType(), Constants.TASK_TYPE_NUMBER);
	}
	
}
