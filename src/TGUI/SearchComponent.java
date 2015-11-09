package TGUI;

import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.tg.backend.Logic;

import TGUtils.Constants;
import TGUtils.Event;

public class SearchComponent extends JPanel {
	Logic logic;
	public SearchComponent(Logic logic) {
		super();
		this.logic = logic;
		refresh();
	}

	public void refresh(){
		removeAll();
		ArrayList<ArrayList<Event>> eventList = logic.updateSearchDisplay();
		if (eventList == null){
			return;
		}
		String[] labels = {"Tasks","Deadlines","Schedules"};

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		for (int i = 0; i < 3; i++){
			add(new JLabel(labels[i]));
			if (i == Constants.EVENT_LIST_TASK)
				add(GUITools.createTaskTable(eventList.get(i)));
			else if (i == Constants.EVENT_LIST_DEADLINE)
				add(GUITools.createDeadlineTable(eventList.get(i)));
			else if (i == Constants.EVENT_LIST_SCHEDULE)
				add(GUITools.createScheduleTable(eventList.get(i)));
		}
		revalidate();
		repaint();
	}

}
