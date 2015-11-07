package TGUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import TGUtils.Event;
public class GUITools {
	private final static Color GUI_COLOR_CLASH = new Color(255, 160, 0);
	private final static Color GUI_COLOR_HIGH = new Color(246, 150, 121);
	private final static Color GUI_COLOR_MID = new Color(255, 247, 153);
	private final static Color GUI_COLOR_LOW = new Color(130, 202, 156);
	private final static Color GUI_COLOR_EVEN_ROW = new Color(216, 216, 216);
	private static int tableWidth = 1280;
	private static int tableHeight = 112;
	private static int ID_SIZE = 25;
	private static int CATEGORY_SIZE = 100;
	private static int PRIORITY_SIZE = 100;
	private static int FIXED_TOTAL = ID_SIZE + CATEGORY_SIZE + PRIORITY_SIZE;
	
	public static JScrollPane createTaskTable(ArrayList<Event> eventList) {

		String[] columnNames = { "ID", "Event Name", "Category", "Priority" };

		Object[][] data = new Object[eventList.size()][4];
		for (int i = 0; i < eventList.size(); i++) {
			Event curr = eventList.get(i);
			data[i][0] = i + 1;
			data[i][1] = curr.getName();
			data[i][2] = curr.getCategory().equals("DEFAULT") ? "--" : curr.getCategory();
			data[i][3] = getPriority(curr.getPriority());
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

				c = setCellColours(table, c, row, column, 3);
				c = markDoneEvents(c, eventList, row);
				
				return c;
			};
		});

		Dimension tableSize = new Dimension(tableWidth, tableHeight);
		table.setPreferredScrollableViewportSize(tableSize);
		table.getColumn("ID").setPreferredWidth(ID_SIZE);
		table.getColumn("Event Name").setPreferredWidth(offsetWidth(tableSize.width, 1.00f));
		table.getColumn("Category").setPreferredWidth(CATEGORY_SIZE);
		table.getColumn("Priority").setPreferredWidth(PRIORITY_SIZE);

		table.setFillsViewportHeight(true);
		table.setRowSelectionAllowed(false);
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
			data[i][2] = curr.formatDate(curr.getEnd());
			data[i][3] = curr.getCategory().equals("DEFAULT") ? "--" : curr.getCategory();
			data[i][4] = getPriority(curr.getPriority());
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
				
				c = setCellColours(table, c, row, column, 4);
				c = markDoneEvents(c, eventList, row);
				
				return c;
			};
		});

		Dimension tableSize = new Dimension(tableWidth, tableHeight);
		table.setPreferredScrollableViewportSize(tableSize);
		table.getColumn("ID").setPreferredWidth(ID_SIZE);
		table.getColumn("Event Name").setPreferredWidth(offsetWidth(tableSize.width, 0.80f));
		table.getColumn("By").setPreferredWidth(offsetWidth(tableSize.width, 0.20f));
		table.getColumn("Category").setPreferredWidth(CATEGORY_SIZE);
		table.getColumn("Priority").setPreferredWidth(PRIORITY_SIZE);

		table.setFillsViewportHeight(true);
		table.setRowSelectionAllowed(false);
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
			data[i][2] = curr.formatDate(curr.getStart());
			data[i][3] = curr.formatDate(curr.getEnd());
			data[i][4] = curr.getCategory().equals("DEFAULT") ? "--" : curr.getCategory();
			data[i][5] = getPriority(curr.getPriority());
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

				c = setCellColours(table, c, row, column, 5);
				c = markDoneEvents(c, eventList, row);
				
				return c;
			};
		});

		Dimension tableSize = new Dimension(tableWidth, tableHeight);
		table.setPreferredScrollableViewportSize(tableSize);
		table.getColumn("ID").setPreferredWidth(ID_SIZE);
		table.getColumn("Event Name").setPreferredWidth(offsetWidth(tableSize.width, 0.60f));
		table.getColumn("From").setPreferredWidth(offsetWidth(tableSize.width, 0.20f));
		table.getColumn("To").setPreferredWidth(offsetWidth(tableSize.width, 0.20f));
		table.getColumn("Category").setPreferredWidth(CATEGORY_SIZE);
		table.getColumn("Priority").setPreferredWidth(PRIORITY_SIZE);

		table.setFillsViewportHeight(true);
		table.setRowSelectionAllowed(false);
		JScrollPane scrollPane = new JScrollPane(table);

		return scrollPane;
	}

	private static String getPriority(int priority) {
		switch (priority) {
		case 3:
			return "HIGH";
		case 2:
			return "MEDIUM";
		case 1:
			return "LOW";
		default:
			return "LOW";
		}
	}
	
	private static Component setCellColours(JTable table, Component component, int row, int col, int x) {
		if (col == x) {
			switch (table.getModel().getValueAt(row, x).toString()) {
			case "HIGH":
				component.setBackground(GUI_COLOR_HIGH);
				break;
			case "MEDIUM":
				component.setBackground(GUI_COLOR_MID);
				break;
			case "LOW":
				component.setBackground(GUI_COLOR_LOW);
				break;
			default:
				component.setBackground(Color.white);
				break;
			}
		} else if (row % 2 == 0) {
			component.setBackground(GUI_COLOR_EVEN_ROW);
		} else {
			component.setBackground(Color.WHITE);
		}
		
		return component;
	}
	
	private static Component markDoneEvents(Component component, ArrayList<Event> eventList, int row) {
		if (eventList.get(row).isDone()) {
			component.setFont(getStrikeThroughFont());
		} else {
			component.setFont(getDefaultFont());
		}
		
		if (eventList.get(row).hasClash()) {
			component.setBackground(GUI_COLOR_CLASH);
		}
		
		return component;
	}
	
	private static Font getDefaultFont() {
		return new Font("Futura", Font.PLAIN, 12);
	}

	private static Font getStrikeThroughFont() {
		Map fontAttributes = getDefaultFont().getAttributes();
		fontAttributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
		return new Font(fontAttributes);
	}

	private static int offsetWidth(int total, float percentage) {
		float result = (float) (total - FIXED_TOTAL) * percentage;
		return Math.round(result);
	}
}
