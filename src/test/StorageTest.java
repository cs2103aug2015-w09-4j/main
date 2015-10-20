package test;

import static org.junit.Assert.*;

import java.io.PrintStream;

import main.Constants;
import main.Event;
import main.TGStorageManager;

import org.junit.Before;
import org.junit.Test;

public class StorageTest {
	@Before
	public void clearContent(){
		TGStorageManager tm = new TGStorageManager("testFile");
		tm.clear();
	}
	@Test
	public void addtest() {
		TGStorageManager tm = new TGStorageManager("testFile");
		Event e = new Event(1,"hello");
		tm.addTask(e);
		assertEquals(tm.getTaskCache().size(),1);
		assertEquals(tm.getTaskCache().get(0), e);
	}
	@Test
	public void deleteTest() {
		TGStorageManager tm = new TGStorageManager("testFile");
		Event e = new Event(1,"hello");
		tm.addTask(e);
		assertEquals(tm.getTaskCache().size(),1);
		tm.deleteEventByID(1);
		assertEquals(tm.getTaskCache().size(),0);
	}
	@Test
	public void updateTest() {
		TGStorageManager tm = new TGStorageManager("testFile");
		Event e = new Event(1,"hello");
		tm.addTask(e);
		assertEquals(tm.getTaskCache().size(),1);
		tm.updateNameByID(1, "hi");
		//System.out.println(tm.getTaskCache().get(0).getName());
		assertEquals(tm.getTaskCache().get(0).getName(),"hi");
	}

}
