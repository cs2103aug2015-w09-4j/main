package main;

import java.util.ArrayList;
import java.util.Collections;

public class Calendar {
	
	private ArrayList<Event> _scheduleCache;
	
	public Calendar() {
		_scheduleCache = new ArrayList<Event>();
	}
	
	public void updateCalendar(ArrayList<Event> newCache) {
		_scheduleCache = newCache;
		Collections.sort(_scheduleCache, Sorters.sortEnd());
	}
	
	public boolean addSchedule(Event newSchedule) {
		for (int i = 0; i < _scheduleCache.size(); i++) {
			if (i == 0) {
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

}