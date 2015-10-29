package GUI;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class TodayComponent extends JPanel {

	public TodayComponent() {
		super();
		refresh();
	}

	public void refresh(){
		removeAll();
		String[] columnNames = { "ID", "Event Name", "Start Date", "End Date", "Category"};

		Object[][] data = { { "Kathy", "Smith", "Snowboarding", new Integer(5), new Boolean(false) },
				{ "John", "Doe", "Rowing", new Integer(3), new Boolean(true) },
				{ "Sue", "Black", "Knitting", new Integer(2), new Boolean(false) },
				{ "Jane", "White", "Speed reading", new Integer(20), new Boolean(true) },
				{ "Joe", "Brown", "Pool", new Integer(10), new Boolean(false) } };
		JTable table = new JTable(data, columnNames);
		JTable  table1 = new JTable(data, columnNames);
		JTable  table2 = new JTable(data, columnNames);

	        table.setFillsViewportHeight(true);
	        JScrollPane scrollPane = new JScrollPane(table);
	        JScrollPane scrollPane1 = new JScrollPane(table1);
	        JScrollPane scrollPane2 = new JScrollPane(table2);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JLabel taskLabel = new JLabel("tasks");
		JLabel scheduleLabel = new JLabel("scedules");
		JLabel deadlineLabel = new JLabel("deadlines");
		add(taskLabel);
		add(scrollPane);
		add(scheduleLabel);
		add(scrollPane1);
		add(deadlineLabel);
		add(scrollPane2);
		revalidate();
		repaint();
	}
}
