package TGUtils;

import java.util.Date;

import java.util.ArrayList;
import java.util.Collections;

//@@author A0127604L
public class TimeClash {
	private int START_INDEX = 0;
	private ArrayList<Event> _scheduleCache;
	
	/**
	 * Initialise arraylist which keeps track of all schedule timings
	 */
	public TimeClash() {
		_scheduleCache = new ArrayList<Event>();
	}
	
	/**
	 * updates _scheduleCache with @param newCache, sorts schedules according to start time
	 * @param newCache
	 */
	public void updateCache(ArrayList<Event> newCache) {
		_scheduleCache = newCache;
		Collections.sort(_scheduleCache, Sorters.sortStart());
		updateClashes();
	}
	
	/**
	 * returns new _scheduleCache updated with hasClash flags
	 * @return _scheduleCache
	 */
	public ArrayList<Event> getCache() {
		return _scheduleCache;
	}
	
	/**
	 * Checks _scheduleCache if there are any Event that clashes with another
	 */
	private void updateClashes() {
		if (_scheduleCache.isEmpty()) {
			return;
		} else if (_scheduleCache.size() == 1) {
			_scheduleCache.get(START_INDEX).setHasClash(false);
		} else {
			for (int i = 0; i < _scheduleCache.size(); i ++) {
				Event currEvent = _scheduleCache.get(i);
				boolean hasClash = false;
				for (int j = 0; j < _scheduleCache.size(); j ++) {
					if (i != j) {
						if (hasClash(currEvent, _scheduleCache.get(j))) {
							hasClash = true;
							break;
						} 
					}
				}
				
				if (hasClash)
					currEvent.setHasClash(true);
				else {
					currEvent.setHasClash(false);
				}
			}
		}
	}
	
	/**
	 * Checks if there is a time clash in the two schedules given
	 * @param Event one
	 * @param Event two
	 * @return whether Event one clashes with Event two
	 */
	private boolean hasClash(Event one, Event two) {
		Date oneStart = one.getStart(); Date oneEnd = one.getEnd();
		Date twoStart = two.getStart(); Date twoEnd = two.getEnd();
		
		if (oneStart.before(twoStart)) {
			if (oneEnd.after(twoStart)) {
				return true;
			} else {
				return false;
			}
		} else {
			if (oneStart.before(twoEnd)) {
				return true;
			} else if (oneEnd.before(twoEnd)) {
				return true;
			} else {
				return false;
			}
		}
	}

}
