package GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.Map;
import java.util.jar.Attributes;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import main.Event;
//test commit

public class GUITools {

	public static JScrollPane createTaskTable(ArrayList<Event> eventList) {

		String[] columnNames = { "ID", "Event Name", "Category", "Priority" };

		Object[][] data = new Object[eventList.size()][4];
		for (int i = 0; i < eventList.size(); i++) {
			Event curr = eventList.get(i);
			data[i][0] = i + 1;
			data[i][1] = curr.getName();
			data[i][2] = curr.getCategory();
			switch ((int) curr.getPriority()){
				case 3:
					data[i][3]="HIGH";
					break;
				case 2:
					data[i][3]="MEDIUM";
					break;
				case 1:
					data[i][3]="LOW";
					break;
				default:
					data[i][3]="LOW";
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
				if (column==3){
				switch ( table.getModel().getValueAt(row, 3).toString()){
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
				if (eventList.get(row).isDone()){
					c.setFont(getStrikeThroughFont());
				}else{
					c.setFont(getDefaultFont());
				}
				return c;
			};
		});
		
		table.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(table);
		return scrollPane;
	}

	public static JScrollPane createDeadlineTable(ArrayList<Event> eventList) {

		String[] columnNames = { "ID", "Event Name", "By", "Category", "Priority" };

		Object[][] data = new Object[eventList.size()][5];
		for (int i = 0; i < eventList.size(); i++) {
			Event curr = eventList.get(i);
			data[i][0] = i + 1;
			data[i][1] = curr.getName();
			data[i][2] = curr.getEnd() == null ? '-' : curr.getEnd();
			data[i][3] = curr.getCategory();
			switch ((int) curr.getPriority()){
				case 3:
					data[i][4]="HIGH";
					break;
				case 2:
					data[i][4]="MEDIUM";
					break;
				case 1:
					data[i][4]="LOW";
					break;
				default:
					data[i][4]="LOW";
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
				if (column==4){
				switch ( table.getModel().getValueAt(row, 4).toString()){
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
				if (eventList.get(row).isDone()){
					c.setFont(getStrikeThroughFont());
				}else{
					c.setFont(getDefaultFont());
				}
				return c;
			};
		});
		
		table.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(table);
		return scrollPane;
	}
	
	public static JScrollPane createScheduleTable(ArrayList<Event> eventList) {

		String[] columnNames = { "ID", "Event Name", "From", "To", "Category", "Priority" };

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
				if (eventList.get(row).isDone()){
					c.setFont(getStrikeThroughFont());
				}else{
					c.setFont(getDefaultFont());
				}
				return c;
			};
		});
		
		table.setFillsViewportHeight(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setRowSelectionAllowed(false);
		JScrollPane scrollPane = new JScrollPane(table);

		return scrollPane;
	}
	
	private static Font getDefaultFont(){
		return new Font("arial", Font.PLAIN, 12);
	}

	private static Font getStrikeThroughFont(){
		Map fontAttributes = getDefaultFont().getAttributes();
		fontAttributes.put(TextAttribute.STRIKETHROUGH,
	            TextAttribute.STRIKETHROUGH_ON);
		return new Font(fontAttributes);
	}
}
