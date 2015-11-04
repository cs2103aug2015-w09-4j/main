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
			if (i == 0 && i == _scheduleCache.size() - 1) {
				if (!xEndsAfterYStarts(newSchedule, currEvent)) {
					return true;
				} else if (!xStartsBeforeYEnds(newSchedule, currEvent)) {
					return true;
				}
			} else if (i == 0) {
				if (!xEndsAfterYStarts(newSchedule, currEvent)) {
					return true;
				}
			} else if (i == getLastIndex()) {
				if (!xStartsBeforeYEnds(newSchedule, currEvent)) {
					return true;
				}
			} else {
				prevEvent = _scheduleCache.get(i-1);
				if (!xStartsBeforeYEnds(newSchedule, prevEvent) && 
						!xEndsAfterYStarts(newSchedule, currEvent)) {
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
	private boolean xEndsAfterYStarts(Event x, Event y){
		return x.getEnd().after(y.getStart());
	}
	
	//returns whether x's start time is after y's end time
	private boolean xStartsBeforeYEnds(Event x, Event y){
		return x.getStart().before(y.getEnd());
	}
	
	public boolean updateStart(int id, Date startDate) {
		for (int i = 0; i < _scheduleCache.size(); i ++) {
			if (_scheduleCache.get(i).getID() == id) {
				if (i == 0) {
					return true;
				} else if (!_scheduleCache.get(i-1).getEnd().after(startDate)) {
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
				} else if (!_scheduleCache.get(i+1).getEnd().before(endDate)) {
					return true;
				}
			}
		}
		return false;
	}

}