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

public class TodayComponent extends JPanel {
	Logic logic;
	public TodayComponent(Logic logic) {
		super();
		this.logic = logic;
		refresh();
	}

	public void refresh(){
		removeAll();
		ArrayList<ArrayList<Event>> eventList = logic.updateTodayDisplay();

		String[] labels = {"Tasks","Deadlines","Schedules"};
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		for (int i = 0;i<3;i++){
			add(new JLabel(labels[i]));
			if (i == 0)
				add(GUITools.createTaskTable(eventList.get(i)));
			if (i == 1)
				add(GUITools.createDeadlineTable(eventList.get(i)));
			if (i == 2)
				add(GUITools.createScheduleTable(eventList.get(i)));
		}
		revalidate();
		repaint();
	}

}
