package test;

import static org.junit.Assert.*;
import main.*;

import org.junit.Test;

public class ParserTest {

	@Test
	public void test() throws Exception {

		Command c = Parser.parseCommand("add task ayam a test");

		assertEquals(c.getType(), Constants.COMMAND_TYPE.ADD_TASK);
		assertEquals(c.getEventName(), "ayam a test");
	}

}
