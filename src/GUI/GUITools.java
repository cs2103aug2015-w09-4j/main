package GUI;

import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import main.Event;

public class GUITools {
	public static JScrollPane createEventTable(ArrayList<Event> eventList){
		String[] columnNames = { "ID", "Event Name", "Start Date", "End Date", "Category","Priority"};

		Object[][] data = new Object[eventList.size()][6];
		for (int i = 0;i<eventList.size();i++){
			Event curr = eventList.get(i);
			data[i][0] = i+1;
			data[i][1] = curr.getName();
			data[i][2] = curr.getStart()==null ? '-':curr.getStart();
			data[i][3] = curr.getEnd()==null ? '-':curr.getEnd();
			data[i][4] = curr.getCategory();
			data[i][5] = curr.getPriority();
		}
		JTable table = new JTable(data, columnNames);
		table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        return scrollPane;
	}
}
