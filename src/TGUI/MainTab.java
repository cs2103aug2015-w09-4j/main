package TGUI;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.tg.backend.Logic;

import TGUtils.Constants;
import TGUtils.Event;

public class MainTab extends JTabbedPane {
	TodayComponent todayPanel;
	EventComponent taskPanel;
	EventComponent deadlinePanel;
	EventComponent schedulePanel;
	SearchComponent searchPanel;
	HelpComponent helpPanel;

	public MainTab(Logic logic) {
		super();
		todayPanel = new TodayComponent(logic);
		//ImageIcon todayIcon = createImageIcon("img/HEADER_today.png");
		addTab("Today", todayPanel);
		setMnemonicAt(Constants.TODAY_TAB_NUMBER, KeyEvent.VK_1);

		taskPanel = new EventComponent(logic, Constants.TASK_TYPE_NUMBER);
		addTab("Tasks", taskPanel);
		setMnemonicAt(Constants.TASK_TAB_NUMBER, KeyEvent.VK_2);
		
		deadlinePanel = new EventComponent(logic,
				Constants.DEADLINE_TYPE_NUMBER);
		addTab("Deadlines", deadlinePanel);
		setMnemonicAt(Constants.DEADLINE_TAB_NUMBER, KeyEvent.VK_3);
		
		schedulePanel = new EventComponent(logic,
				Constants.SCHEDULE_TYPE_NUMBER);
		addTab("Schedules", schedulePanel);
		setMnemonicAt(Constants.SCHEDULE_TAB_NUMBER, KeyEvent.VK_4);
		
		searchPanel = new SearchComponent(logic);
		addTab("Search", searchPanel);
		setMnemonicAt(Constants.SEARCH_TAB_NUMBER, KeyEvent.VK_5);

		helpPanel = new HelpComponent();
		addTab("Help", helpPanel);
		setMnemonicAt(Constants.HELP_TAB_NUMBER, KeyEvent.VK_6);
		
		addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				System.out.println(getSelectedIndex());
				refresh(logic.updateDisplay());
			}
		});
		
		refresh(logic.updateDisplay());
	}

	/**
	 * Refresh the currently selected tab with an updated list of events
	 */
	public void refresh(ArrayList<ArrayList<Event>> eventList) {
		if (getSelectedIndex() == Constants.TODAY_TAB_NUMBER) {
			todayPanel.refresh();
		} else if (getSelectedIndex() == Constants.SEARCH_TAB_NUMBER) {
			searchPanel.refresh();
		} else {
			refreshAllEventTabs(eventList);
		}
	}

	/**
	 * Refresh all three different event type tabs with an updated list of events
	 */
	private void refreshAllEventTabs(ArrayList<ArrayList<Event>> eventList) {
		taskPanel.refresh(eventList);
		deadlinePanel.refresh(eventList);
		schedulePanel.refresh(eventList);
	}
	
	/**
	 * Displays the image from the path given
	 */
	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = MainTab.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
	
	/**
	 * Creates a Text Panel with the text given
	 */
	protected JComponent makeTextPanel(String text) {
		JPanel panel = new JPanel(false);
		JLabel filler = new JLabel(text);
		filler.setHorizontalAlignment(JLabel.CENTER);
		panel.setLayout(new GridLayout(1, 1));
		panel.add(filler);
		return panel;
	}
}
