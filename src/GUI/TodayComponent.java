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

	public TodayComponent(Logic logic) {
		super();
		refresh(logic.updateDisplay());
	}

	public void refresh(ArrayList<ArrayList<Event>> eventList){
		removeAll();

		String[] labels = {"tasks","deadline","schedules"};

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		for (int i = 0;i<3;i++){
			add(new JLabel(labels[i]));
			add(GUITools.createEventTable(eventList.get(i)));
		}
		revalidate();
		repaint();
	}

}
