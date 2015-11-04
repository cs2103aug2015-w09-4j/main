package main;

import java.util.Date;

import com.sun.corba.se.spi.orbutil.fsm.State;

import java.util.ArrayList;
import java.util.Collections;

public class TimeBlock {
	private int START_INDEX = 0;
	private ArrayList<Event> _scheduleCache;
	
	/**
	 * Initialise arraylist which keeps track of all schedule timings
	 */
	public TimeBlock() {
		_scheduleCache = new ArrayList<Event>();
	}
	
	/**
	 * updates _scheduleCache with @param newCache, sorts schedules according to end time
	 * @param newCache
	 */
	public void updateCache(ArrayList<Event> newCache) {
		_scheduleCache = newCache;
		Collections.sort(_scheduleCache, Sorters.sortEnd());
	}
	
	/**
	 * Iterates through _scheduleCache and see if there are conflicts in timings
	 * @param newSchedule
	 * @return whether @param newSchedule can be added in to the schedule list
	 */
	public boolean canFitSchedule(Event newSchedule) {
		if (_scheduleCache.isEmpty()){
			return true;
		}
		Event currEvent, prevEvent;
		for (int i = START_INDEX; i < _scheduleCache.size(); i++) {
			currEvent = _scheduleCache.get(i);
			if (i == START_INDEX && i == getLastIndex()) {	//start and end of _scheduleCache
				if (xEndsBeforeYStarts(newSchedule, currEvent)) {
					return true;
				} else if (xStartsAfterYEnds(newSchedule, currEvent)) {
					return true;
				}
			} else if (i == START_INDEX) {		//start of _scheduleCache
				if (xEndsBeforeYStarts(newSchedule, currEvent)) {
					return true;
				}
			} else if (i == getLastIndex()) {	//end of _scheduleCache
				if (xStartsAfterYEnds(newSchedule, currEvent)) {
					return true;
				}
			} else {
				prevEvent = _scheduleCache.get(i-1);
				if (xStartsAfterYEnds(newSchedule, prevEvent) && xEndsBeforeYStarts(newSchedule, currEvent)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private int getLastIndex(){
		return _scheduleCache.size() - 1;
	}
	
	//returns whether x's end time is before y's start time
	private boolean xEndsBeforeYStarts(Event x, Event y){
		return x.getEnd().before(y.getStart());
	}
	
	//returns whether x's start time is after y's end time
	private boolean xStartsAfterYEnds(Event x, Event y){
		return x.getStart().after(y.getEnd());
	}
	
	public boolean updateStart(int id, Date startDate) {
		for (int i = 0; i < _scheduleCache.size(); i ++) {
			if (_scheduleCache.get(i).getID() == id) {
				if (i == 0) {
					return true;
				} else if (_scheduleCache.get(i - 1).getEnd().before(startDate)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean updateEnd(int id, Date endDate) {
		for (int i = 0; i < _scheduleCache.size(); i ++) {
			if (_scheduleCache.get(i).getID() == id) {
				if (i == _scheduleCache.size() - 1) {
					return true;
				} else if (_scheduleCache.get(i + 1).getEnd().after(endDate)) {
					return true;
				}
			}
		}
		return false;
	}

}