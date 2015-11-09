package com.tg.test;

public class Test_Commands {
	/**
	 * Since we have no clear function, every time you run this test,
	 * change the name of the test file to instantiate a brand new xml storage file
	 *
	 * Currently the tests run in alphabetical order of the testNames, so if you're
	 * adding new tests and want them to run after the last test in this file,
	 * say, "NtestBlahBlah", just name your test as "OtestNewBlahBlah" || "NAtestBlahBlah"
	 * @author JingYin
	 *
	 */

	/*
	@FixMethodOrder(MethodSorters.NAME_ASCENDING)
	public class Test_Commands {

		public static String testFileName = "JUnit testing2";
		public static Logic tg = new Logic(testFileName);

		public static final String COMMAND_ADD = "add ";
		public static final String COMMAND_UPDATE_NAME = "update name ";
		public static final String COMMAND_UNDO = "undo";
		public static final String COMMAND_DELETE = "delete ";
		public static final String COMMAND_DISPLAY = "display";

		public void intializeHashMap(){
			tg.executeInputs(COMMAND_DISPLAY);
		}

		String floatTaskNameA = "test Float";
		@Test
		public void AtestAddFloating() {
			intializeHashMap();

			String actual = tg.executeInputs(COMMAND_ADD+floatTaskNameA);
			String expected = String.format(Constants.TANGGUO_ADD_SUCCESS, testFileName, floatTaskNameA);
			assertEquals(expected, actual);
		}

		String deadlineTaskNameA = "test Deadline by 11/11/2015 11:11";
		@Test
		public void BtestAddDeadline() {
			String actual = tg.executeInputs(COMMAND_ADD+deadlineTaskNameA);
			String expected = String.format(Constants.TANGGUO_ADD_SUCCESS, testFileName, deadlineTaskNameA);
			assertEquals(expected, actual);
		}

		String scheduleTaskNameA = "test Schedule from 11/11/2015 11:11 to 11/11/2015 12:12";
		@Test
		public void CtestAddSchedule() {
			String actual = tg.executeInputs(COMMAND_ADD+scheduleTaskNameA);
			String expected = String.format(Constants.TANGGUO_ADD_SUCCESS, testFileName, scheduleTaskNameA);
			assertEquals(expected, actual);
		}

		@Test
		public void DtestAddedTaskTypesCorrectly(){
			String actual = tg.executeInputs(COMMAND_DISPLAY);
			String expected = "Tasks:\n1. "+floatTaskNameA+"\nDeadlines:\n1. "+deadlineTaskNameA
					+"\nSchedules:\n1. "+scheduleTaskNameA+"\n";
			assertEquals(expected, actual);
		}

		String floatTaskNameB = "updated test Float";
		String deadlineTaskNameB = "updated test Deadline";
		String scheduleTaskNameB = "updated test Schedule";
		//test use case of updating tasks' names
		@Test
		public void EtestUpdateName(){
			//test float
			String actual = tg.executeInputs(COMMAND_UPDATE_NAME + "t1 "+ floatTaskNameB);
			String expected = String.format(Constants.TANGGUO_UPDATE_NAME_SUCCESS, floatTaskNameA, floatTaskNameB) +
					"\nTasks:\n1. "+floatTaskNameB+"\nDeadlines:\n1. "+deadlineTaskNameA
					+"\nSchedules:\n1. "+scheduleTaskNameA+"\n";
			assertEquals(expected, actual);

			//test deadline
			actual = tg.executeInputs(COMMAND_UPDATE_NAME + "d1 " + deadlineTaskNameB);
			expected = String.format(Constants.TANGGUO_UPDATE_NAME_SUCCESS, deadlineTaskNameA, deadlineTaskNameB) +
					"\nTasks:\n1. "+floatTaskNameB+"\nDeadlines:\n1. "+deadlineTaskNameB
					+"\nSchedules:\n1. "+scheduleTaskNameA+"\n";
			assertEquals(expected, actual);

			//test schedule
			actual = tg.executeInputs(COMMAND_UPDATE_NAME + "s1 " + scheduleTaskNameB);
			expected = String.format(Constants.TANGGUO_UPDATE_NAME_SUCCESS, scheduleTaskNameA, scheduleTaskNameB) +
					"\nTasks:\n1. "+floatTaskNameB+"\nDeadlines:\n1. "+deadlineTaskNameB
					+"\nSchedules:\n1. "+scheduleTaskNameB+"\n";
			assertEquals(expected, actual);
		}

		@Test
		public void FtestDelete(){
			//test float
			String actual = tg.executeInputs(COMMAND_DELETE + "t1");
			String expected = String.format(Constants.TANGGUO_DELETE_SUCCESS, testFileName, floatTaskNameB) +
					"\nTasks:"+"\nDeadlines:\n1. "+deadlineTaskNameB +"\nSchedules:\n1. "+scheduleTaskNameB+"\n";
			assertEquals(expected, actual);

			//test deadline
			actual = tg.executeInputs(COMMAND_DELETE + "d1");
			expected = String.format(Constants.TANGGUO_DELETE_SUCCESS, testFileName, deadlineTaskNameB) +
					"\nTasks:"+"\nDeadlines:"+"\nSchedules:\n1. "+scheduleTaskNameB+"\n";
			assertEquals(expected, actual);

			//test schedule
			actual = tg.executeInputs(COMMAND_DELETE + "s1");
			expected = String.format(Constants.TANGGUO_DELETE_SUCCESS, testFileName, scheduleTaskNameB) +
					"\n" + String.format(Constants.TANGGUO_EMPTY_FILE, testFileName);
			assertEquals(expected, actual);
		}

		@Test
		public void GtestUndoDelete(){
			String actual = tg.executeInputs(COMMAND_UNDO) + tg.executeInputs(COMMAND_DISPLAY);
			String expected = Constants.TANGGUO_UNDO_SUCCESS + "Tasks:"+"\nDeadlines:"+"\nSchedules:\n1. "
					+scheduleTaskNameB+"\n";
			assertEquals(expected, actual);

			actual = tg.executeInputs(COMMAND_UNDO) + tg.executeInputs(COMMAND_DISPLAY);
			expected = Constants.TANGGUO_UNDO_SUCCESS + "Tasks:"+"\nDeadlines:\n1. "+deadlineTaskNameB +
					"\nSchedules:\n1. "+scheduleTaskNameB+"\n";
			assertEquals(expected, actual);

			actual = tg.executeInputs(COMMAND_UNDO) + tg.executeInputs(COMMAND_DISPLAY);
			expected = Constants.TANGGUO_UNDO_SUCCESS + "Tasks:\n1. "+floatTaskNameB+"\nDeadlines:\n1. "+
					deadlineTaskNameB+"\nSchedules:\n1. "+scheduleTaskNameB+"\n";
			assertEquals(expected, actual);
		}

		@Test
		public void HtestUndoUpdateName(){
			String actual = tg.executeInputs(COMMAND_UNDO) + tg.executeInputs(COMMAND_DISPLAY);
			String expected = Constants.TANGGUO_UNDO_SUCCESS + "Tasks:\n1. "+floatTaskNameB+"\nDeadlines:\n1. "+
					deadlineTaskNameB+"\nSchedules:\n1. "+scheduleTaskNameA+"\n";
			assertEquals(expected, actual);

			actual = tg.executeInputs(COMMAND_UNDO) + tg.executeInputs(COMMAND_DISPLAY);
			expected = Constants.TANGGUO_UNDO_SUCCESS + "Tasks:\n1. "+floatTaskNameB+"\nDeadlines:\n1. "+
					deadlineTaskNameA+"\nSchedules:\n1. "+scheduleTaskNameA+"\n";
			assertEquals(expected, actual);

			actual = tg.executeInputs(COMMAND_UNDO) + tg.executeInputs(COMMAND_DISPLAY);
			expected = Constants.TANGGUO_UNDO_SUCCESS + "Tasks:\n1. "+floatTaskNameA+"\nDeadlines:\n1. "+
					deadlineTaskNameA+"\nSchedules:\n1. "+scheduleTaskNameA+"\n";
			assertEquals(expected, actual);
		}

		@Test
		public void ItestUndoAdd(){
			String actual = tg.executeInputs(COMMAND_UNDO) + tg.executeInputs(COMMAND_DISPLAY);
			String expected = Constants.TANGGUO_UNDO_SUCCESS + "Tasks:\n1. "+floatTaskNameA+"\nDeadlines:\n1. "+
					deadlineTaskNameA+"\nSchedules:\n";
			assertEquals(expected, actual);

			actual = tg.executeInputs(COMMAND_UNDO) + tg.executeInputs(COMMAND_DISPLAY);
			expected = Constants.TANGGUO_UNDO_SUCCESS + "Tasks:\n1. "+floatTaskNameA+"\nDeadlines:"+"\nSchedules:\n";
			assertEquals(expected, actual);

			actual = tg.executeInputs(COMMAND_UNDO) + tg.executeInputs(COMMAND_DISPLAY);
			expected = Constants.TANGGUO_UNDO_SUCCESS + String.format(Constants.TANGGUO_EMPTY_FILE, testFileName);
			assertEquals(expected, actual);
		}
	}
	*/

}
