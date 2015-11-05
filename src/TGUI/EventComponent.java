package TGUI;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import TGLogic.Logic;
import TGUtils.Event;

public class EventComponent extends JPanel {
	int eventType;
	public EventComponent(Logic logic, int eventType) {
		super();
		//refresh(logic.updateDisplay());
		this.eventType = eventType - 1;
	}

	public void refresh(ArrayList<ArrayList<Event>> eventList){
		removeAll();

		//String[] labels = {"tasks","deadline","schedules"};

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		//add(new JLabel(labels[i]));
		//add(GUITools.createEventTable(eventList.get(this.eventType-1)));
		if (eventType == 0)
			add(GUITools.createTaskTable(eventList.get(eventType)));
		if (eventType == 1)
			add(GUITools.createDeadlineTable(eventList.get(eventType)));
		if (eventType == 2)
			add(GUITools.createScheduleTable(eventList.get(eventType)));
		
		revalidate();
		repaint();
	}

}
