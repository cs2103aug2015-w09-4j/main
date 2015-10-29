package GUI;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import main.Event;
import main.Logic;

public class EventComponent extends JPanel {
	int eventType;
	public EventComponent(Logic logic, int eventType) {
		super();
		//refresh(logic.updateDisplay());
		this.eventType = eventType;
	}

	public void refresh(ArrayList<ArrayList<Event>> eventList){
		removeAll();

		//String[] labels = {"tasks","deadline","schedules"};

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		//add(new JLabel(labels[i]));
		add(GUITools.createEventTable(eventList.get(this.eventType-1)));

		revalidate();
		repaint();
	}

}
