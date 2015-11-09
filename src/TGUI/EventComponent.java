package TGUI;

import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.tg.backend.Logic;

import TGUtils.Constants;
import TGUtils.Event;

public class EventComponent extends JPanel {
	int eventType;
	public EventComponent(Logic logic, int eventType) {
		super();
		this.eventType = eventType - 1;
	}

	public void refresh(ArrayList<ArrayList<Event>> eventList){
		removeAll();

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		if (eventType == Constants.EVENT_LIST_TASK)
			add(GUITools.createTaskTable(eventList.get(eventType)));
		else if (eventType == Constants.EVENT_LIST_DEADLINE)
			add(GUITools.createDeadlineTable(eventList.get(eventType)));
		else if (eventType == Constants.EVENT_LIST_SCHEDULE)
			add(GUITools.createScheduleTable(eventList.get(eventType)));
		
		revalidate();
		repaint();
	}

}
