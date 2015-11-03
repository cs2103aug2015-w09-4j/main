package GUI;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import main.Event;

public class GUITools {
	public static JScrollPane createEventTable(ArrayList<Event> eventList) {
		String[] columnNames = { "ID", "Event Name", "Start Date", "End Date", "Category", "Priority" };

		Object[][] data = new Object[eventList.size()][6];
		for (int i = 0; i < eventList.size(); i++) {
			Event curr = eventList.get(i);
			data[i][0] = i + 1;
			data[i][1] = curr.getName();
			data[i][2] = curr.getStart() == null ? '-' : curr.getStart();
			data[i][3] = curr.getEnd() == null ? '-' : curr.getEnd();
			data[i][4] = curr.getCategory();
			switch ((int) curr.getPriority()){
				case 3:
					data[i][5]="HIGH";
					break;
				case 2:
					data[i][5]="MEDIUM";
					break;
				case 1:
					data[i][5]="LOW";
					break;
				default:
					data[i][5]="LOW";
					break;


			}
			//data[i][5] = curr.getPriority();
		}
		JTable table = new JTable(data, columnNames) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if (column==5){
				switch ( table.getModel().getValueAt(row, 5).toString()){
					case "HIGH":
						c.setBackground(Color.red);
						break;
					case "MEDIUM":
						c.setBackground(Color.yellow);
						break;
					case "LOW":
						c.setBackground(Color.green);
						break;
					default:
						c.setBackground(Color.white);
						break;
				}
				}else{
					c.setBackground(Color.white);
				}
				return c;
			};
		});
		table.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(table);
		return scrollPane;
	}
}
