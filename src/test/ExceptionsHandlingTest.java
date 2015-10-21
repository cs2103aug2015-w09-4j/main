package test;

import static org.junit.Assert.*;

import org.junit.Before;

import main.*;

import org.junit.Test;

public class ExceptionsHandlingTest {
	
	@Test
	public void dateOutOfBoundsTest() throws Exception {
		TangGuo tg = new TangGuo("testFile");
		assertEquals(tg.executeInputs("add ayamdatedeadlinetest by 32/10/2015 15:09"), Constants.TANGGUO_DATE_OUT_OF_BOUNDS);
	}
	
	@Test
	public void timeOutOfBoundsTest() throws Exception {
		TangGuo tg = new TangGuo("testFile");
		assertEquals(tg.executeInputs("add ayamtimedeadlinetest by 31/10/2015 24:09"), Constants.TANGGUO_DATE_OUT_OF_BOUNDS);
	}

	@Test
	public void leapYearTest() throws Exception {
		TangGuo tg = new TangGuo("testFile");
		assertEquals(tg.executeInputs("add ayamleapyeardeadlinetest by 29/02/2015 23:09"), Constants.TANGGUO_DATE_OUT_OF_BOUNDS);
	}

}
