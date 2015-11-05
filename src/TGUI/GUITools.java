package TGUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.Map;
import java.util.jar.Attributes;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import TGUtils.Event;
public class GUITools {
	private final static Color GUI_COLOR_HIGH = new Color(246, 150, 121);
	private final static Color GUI_COLOR_MID = new Color(255, 247, 153);
	private final static Color GUI_COLOR_LOW = new Color(130, 202, 156);
	private final static Color GUI_COLOR_EVEN_ROW = new Color(216, 216, 216);
	private static int tableWidth = 1280;
	// private static int tableWidth = 1000;
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
			switch ((int) curr.getPriority()) {
			case 3:
				data[i][3] = "HIGH";
				break;
			case 2:
				data[i][3] = "MEDIUM";
				break;
			case 1:
				data[i][3] = "LOW";
				break;
			default:
				data[i][3] = "LOW";
				break;

			}
			// data[i][5] = curr.getPriority();
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

				if (column == 3) {
					switch (table.getModel().getValueAt(row, 3).toString()) {
					case "HIGH":
						c.setBackground(GUI_COLOR_HIGH);
						break;
					case "MEDIUM":
						c.setBackground(GUI_COLOR_MID);
						break;
					case "LOW":
						c.setBackground(GUI_COLOR_LOW);
						break;
					default:
						c.setBackground(Color.white);
						break;
					}
				} else if (row % 2 == 0) {
					c.setBackground(GUI_COLOR_EVEN_ROW);
				} else {
					c.setBackground(Color.WHITE);
				}

				if (eventList.get(row).isDone()) {
					c.setFont(getStrikeThroughFont());
				} else {
					c.setFont(getDefaultFont());
				}
				return c;
			};
		});

		Dimension tableSize = new Dimension(tableWidth, tableHeight);
		table.setPreferredSize(tableSize);
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
			switch ((int) curr.getPriority()) {
			case 3:
				data[i][4] = "HIGH";
				break;
			case 2:
				data[i][4] = "MEDIUM";
				break;
			case 1:
				data[i][4] = "LOW";
				break;
			default:
				data[i][4] = "LOW";
				break;

			}
			// data[i][5] = curr.getPriority();
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
				if (column == 4) {
					switch (table.getModel().getValueAt(row, 4).toString()) {
					case "HIGH":
						c.setBackground(GUI_COLOR_HIGH);
						break;
					case "MEDIUM":
						c.setBackground(GUI_COLOR_MID);
						break;
					case "LOW":
						c.setBackground(GUI_COLOR_LOW);
						break;
					default:
						c.setBackground(Color.white);
						break;
					}
				} else if (row % 2 == 0) {
					c.setBackground(GUI_COLOR_EVEN_ROW);
				} else {
					c.setBackground(Color.WHITE);
				}
				if (eventList.get(row).isDone()) {
					c.setFont(getStrikeThroughFont());
				} else {
					c.setFont(getDefaultFont());
				}
				return c;
			};
		});

		Dimension tableSize = new Dimension(tableWidth, tableHeight);
		table.setPreferredSize(tableSize);
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
			switch ((int) curr.getPriority()) {
			case 3:
				data[i][5] = "HIGH";
				break;
			case 2:
				data[i][5] = "MEDIUM";
				break;
			case 1:
				data[i][5] = "LOW";
				break;
			default:
				data[i][5] = "LOW";
				break;

			}
			// data[i][5] = curr.getPriority();
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

				if (column == 5) {
					switch (table.getModel().getValueAt(row, 5).toString()) {
					case "HIGH":
						c.setBackground(GUI_COLOR_HIGH);
						break;
					case "MEDIUM":
						c.setBackground(GUI_COLOR_MID);
						break;
					case "LOW":
						c.setBackground(GUI_COLOR_LOW);
						break;
					default:
						c.setBackground(Color.white);
						break;
					}
				} else if (row % 2 == 0) {
					c.setBackground(GUI_COLOR_EVEN_ROW);
				} else {
					c.setBackground(Color.WHITE);
				}
				if (eventList.get(row).isDone()) {
					c.setFont(getStrikeThroughFont());
				} else {
					c.setFont(getDefaultFont());
				}
				return c;
			};
		});

		Dimension tableSize = new Dimension(tableWidth, tableHeight);
		table.setPreferredSize(tableSize);
		table.getColumn("ID").setPreferredWidth(ID_SIZE);
		table.getColumn("Event Name").setPreferredWidth(offsetWidth(tableSize.width, 0.60f));
		table.getColumn("From").setPreferredWidth(offsetWidth(tableSize.width, 0.20f));
		table.getColumn("To").setPreferredWidth(offsetWidth(tableSize.width, 0.20f));
		table.getColumn("Category").setPreferredWidth(CATEGORY_SIZE);
		table.getColumn("Priority").setPreferredWidth(PRIORITY_SIZE);

		table.setFillsViewportHeight(true);
		table.setRowSelectionAllowed(false);
		JScrollPane scrollPane = new JScrollPane(table);
		System.out.println(scrollPane.getWidth());

		return scrollPane;
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
