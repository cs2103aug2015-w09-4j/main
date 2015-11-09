package com.tg.gui;

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

import com.tg.util.Constants;
import com.tg.util.Event;
//@@author A0127604L
public class GUITools {
	
	/**
	 * Creates the table for Floating Task events
	 * @param eventList
	 * @return JScrollPane the table
	 */
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
				c = displayEventProperties(c, eventList, row);
				
				return c;
			};
		});

		Dimension tableSize = Constants.TABLE_DIMENSION;
		setTableProperties(table, tableSize);
		table.getColumn("Event Name").setPreferredWidth(offsetWidth(tableSize.width, Constants.CELL_NAME_TASK_PERCENTAGE));

		JScrollPane scrollPane = new JScrollPane(table);

		return scrollPane;
	}

	/**
	 * Creates the table for Deadline events
	 * @param eventList
	 * @return JScrollPane the table
	 */
	public static JScrollPane createDeadlineTable(ArrayList<Event> eventList) {

		String[] columnNames = { "ID", "Event Name", "By", "Category", "Priority" };

		Object[][] data = new Object[eventList.size()][5];
		for (int i = 0; i < eventList.size(); i++) {
			Event curr = eventList.get(i);
			data[i][0] = i + 1;
			data[i][1] = curr.getName();
			data[i][2] = Event.formatDate(curr.getEnd());
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
				c = displayEventProperties(c, eventList, row);
				
				return c;
			};
		});

		Dimension tableSize = Constants.TABLE_DIMENSION;
		setTableProperties(table, tableSize);
		table.getColumn("Event Name").setPreferredWidth(offsetWidth(tableSize.width, Constants.CELL_NAME_DEADLINE_PERCENTAGE));
		table.getColumn("By").setPreferredWidth(offsetWidth(tableSize.width, Constants.CELL_TIME_PERCENTAGE));

		JScrollPane scrollPane = new JScrollPane(table);

		return scrollPane;
	}

	/**
	 * Creates the table for Schedule events
	 * @param eventList
	 * @return JScrollPane the table
	 */
	public static JScrollPane createScheduleTable(ArrayList<Event> eventList) {

		String[] columnNames = { "ID", "Event Name", "From", "To", "Category", "Priority" };

		Object[][] data = new Object[eventList.size()][6];
		for (int i = 0; i < eventList.size(); i++) {
			Event curr = eventList.get(i);
			data[i][0] = i + 1;
			data[i][1] = curr.getName();
			data[i][2] = Event.formatDate(curr.getStart());
			data[i][3] = Event.formatDate(curr.getEnd());
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
				c = displayEventProperties(c, eventList, row);
				
				return c;
			};
		});

		Dimension tableSize = Constants.TABLE_DIMENSION;
		setTableProperties(table, tableSize);
		table.getColumn("Event Name").setPreferredWidth(offsetWidth(tableSize.width, Constants.CELL_NAME_SCHEDULE_PERCENTAGE));
		table.getColumn("From").setPreferredWidth(offsetWidth(tableSize.width, Constants.CELL_TIME_PERCENTAGE));
		table.getColumn("To").setPreferredWidth(offsetWidth(tableSize.width, Constants.CELL_TIME_PERCENTAGE));

		JScrollPane scrollPane = new JScrollPane(table);

		return scrollPane;
	}

	/**
	 * Interprets the priority of the event from its number
	 * @param priority
	 * @return HIGH/MEDIUM/LOW
	 */
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

	/**
	 * Set the color for priority and even row events
	 * @param table
	 * @param component
	 * @param row
	 * @param col
	 * @param x
	 * @return the modified component
	 */
	private static Component setCellColours(JTable table, Component component, int row, int col, int x) {
		if (col == x) {
			switch (table.getModel().getValueAt(row, x).toString()) {
			case "HIGH":
				component.setBackground(Constants.GUI_COLOR_HIGH);
				break;
			case "MEDIUM":
				component.setBackground(Constants.GUI_COLOR_MID);
				break;
			case "LOW":
				component.setBackground(Constants.GUI_COLOR_LOW);
				break;
			default:
				component.setBackground(Color.white);
				break;
			}
		} else if (row % 2 == 0) {
			component.setBackground(Constants.GUI_COLOR_EVEN_ROW);
		} else {
			component.setBackground(Color.WHITE);
		}

		return component;
	}

	/**
	 * Set the visual feedback for isDone and hasClash properties
	 * @param component
	 * @param eventList
	 * @param row
	 * @return the modified component
	 */
	private static Component displayEventProperties(Component component, ArrayList<Event> eventList, int row) {
		if (eventList.get(row).isDone()) {
			component.setFont(getStrikeThroughFont());
		} else {
			component.setFont(getDefaultFont());
		}

		if (eventList.get(row).hasClash()) {
			component.setBackground(Constants.GUI_COLOR_CLASH);
		}

		return component;
	}
	
	/**
	 * Set the standard table properties
	 * @param table
	 * @param tableSize
	 */
	private static void setTableProperties(JTable table, Dimension tableSize) {
		table.setPreferredScrollableViewportSize(tableSize);
		table.getColumn("ID").setPreferredWidth(Constants.COLUMN_ID_SIZE);
		table.getColumn("Category").setPreferredWidth(Constants.COLUMN_CATEGORY_SIZE);
		table.getColumn("Priority").setPreferredWidth(Constants.COLUMN_PRIORITY_SIZE);

		table.setFillsViewportHeight(true);
		table.setRowSelectionAllowed(false);
	}

	/**
	 * Set the width of the cell according to percentage
	 * @param total
	 * @param percentage
	 * @return the width of the cell in integer
	 */
	private static int offsetWidth(int total, float percentage) {
		float result = (float) (total - Constants.COLUMN_FIXED_TOTAL) * percentage;
		return Math.round(result);
	}
	
	private static Font getDefaultFont() {
		return new Font("Futura", Font.PLAIN, 12);
	}

	private static Font getStrikeThroughFont() {
		Map fontAttributes = getDefaultFont().getAttributes();
		fontAttributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
		return new Font(fontAttributes);
	}

}
