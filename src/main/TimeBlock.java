package main;

import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;

public class TimeBlock {
	
	private ArrayList<Event> _scheduleCache;
	
	public TimeBlock() {
		_scheduleCache = new ArrayList<Event>();
	}
	
	public void updateCache(ArrayList<Event> newCache) {
		_scheduleCache = newCache;
		Collections.sort(_scheduleCache, Sorters.sortEnd());
	}
	
	public boolean addSchedule(Event newSchedule) {
		if (_scheduleCache.isEmpty()){
			return true;
		}
		for (int i = 0; i < _scheduleCache.size(); i++) {
			if (i == 0 && i == _scheduleCache.size() - 1) {
				if (newSchedule.getEnd().before(_scheduleCache.get(i).getStart())) {
					return true;
				} else if (newSchedule.getStart().after(_scheduleCache.get(i).getEnd())) {
					return true;
				}
			} else if (i == 0) {
				if (newSchedule.getEnd().before(_scheduleCache.get(i).getStart())) {
					return true;
				}
			} else if (i == _scheduleCache.size() - 1) {
				if (newSchedule.getStart().after(_scheduleCache.get(i).getEnd())) {
					return true;
				}
			} else {
				if (newSchedule.getStart().after(_scheduleCache.get(i-1).getEnd()) && 
						newSchedule.getEnd().before(_scheduleCache.get(i).getStart())) {
					return true;
				}
			}
		}
		return false;
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
				} else if (_scheduleCache.get(i + 1).getEnd().before(endDate)) {
					return true;
				}
			}
		}
		return false;
	}

}