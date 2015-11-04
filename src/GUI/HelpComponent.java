package GUI;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import main.Logic;

public class HelpComponent extends JPanel {
	public HelpComponent() {
		super();
		String[] columnNames = { "Action", "Command" };

		Object[][] data = {{"add new task","add <task>"},
				{"add new schedule","add <task> from <date> to <date>"},
				{"add new deadline","add <task> by <date>"},
				{"delete an event","delete <t,s, d><num>"},
				{"update the name of an event","update name <task name and index, ie. t1> <name>"},
				{"update the start date of an event","update start <task name and index> <date>"},
				{"update the end date of an event","update end <task name and index> <date>"},
				{"update the category of an event","update category <task name and index> <category>"},
				{"update the priority of an event","update priority <task name and index> <low,mid or high>"},
				{"search for event","search <keyword>"},
				{"sort event by names","sort name"},
				{"sort event by start dates","sort start"},
				{"sort event by end dates","sort end"},
				{"sort event by priorities","sort priority"},
				{"set task file directory","path <path directory>"},

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
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane);
	}
}
