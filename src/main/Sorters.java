package main;
import java.util.Comparator;

public class Sorters {

	static Comparator<Event> sortName() {
		return new Comparator<Event>() {
			public int compare(Event x, Event y) {
				return x.getName().compareTo(y.getName());
			}
		};
	}
	
	static Comparator<Event> sortPriority() {
		return new Comparator<Event>() {
			public int compare(Event x, Event y) {
				return y.getPriority() - x.getPriority();
			}
		};
	}
	
	static Comparator<Event> sortStart() {
		return new Comparator<Event>() {
			public int compare(Event x, Event y) {
				if (x.getStart().before(y.getStart()))
					return -1;
				else if (y.getStart().before(x.getStart()))
					return 1;
				else return 0;
			}
		};
	}
	
	static Comparator<Event> sortEnd() {
		return new Comparator<Event>() {
			public int compare(Event x, Event y) {
				if (x.getEnd().before(y.getEnd()))
					return -1;
				else if (y.getEnd().before(x.getEnd()))
					return 1;
				else return 0;
			}
		};
	}
	
}