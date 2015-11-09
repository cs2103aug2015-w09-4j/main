package com.tg.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class HelpComponent extends JPanel {
	public HelpComponent() {
		super();
		String[] columnNames = { "Action", "Command" };

		Object[][] data = {
				{"add new task event", "add <name>"},
				{"add new schedule event", "add <name> from <date> to <date>"},
				{"add new deadline event", "add <name> by <date>"},
				{"add new task event with priority", "add <name> -p <low/mid/high>"},
				{"add new schedule event with priority", "add <name> from <date> to <date> -p <low/mid/high>"},
				{"add new deadline event with priority", "add <name> by <date> -p <low/mid/high>"},
				{"update the name of an event", "update name <t,d,s><num> <name>"},
				{"update the start date of an event", "update start <t,d,s><num> <date>"},
				{"update the end date of an event", "update end <t,d,s><num> <date>"},
				{"update the category of an event", "update category <t,d,s><num> <category>"},
				{"update the priority of an event", "update priority <t,d,s><num> <low/mid/high>"},
				{"delete an event", "delete <t,d,s><num>"},
				{"marks the event as done", "done <t,d,s><num>"},
				{"toggle view done events", "toggle"},
				{"search for event", "search <keyword>"},
				{"sort event by names", "sort name"},
				{"sort event by start dates", "sort start"},
				{"sort event by end dates", "sort end"},
				{"sort event by priorities", "sort priority"},
				{"set save file directory", "path <path directory>"},
				{"import data file from directory","import <path directory>/<file name>"}
							};

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
				if (row % 2 == 0) {
					c.setBackground(new Color(216, 216, 216));
				} else {
					c.setBackground(Color.WHITE);
				}
				return c;
			};
		});
		
		table.setRowSelectionAllowed(false);
		setLayout(new BoxLayout(this,  BoxLayout.Y_AXIS));;
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane);
	}
}
