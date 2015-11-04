package GUI;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.Event;
import main.Logic;
import main.Constants;
public class MainTab extends JTabbedPane {
	TodayComponent todayPanel;
	EventComponent taskPanel;
	EventComponent deadlinePanel;
	EventComponent schedulePanel;
	SearchComponent searchPanel;
	HelpComponent helpPanel;
	public MainTab(Logic logic){
		super();
		todayPanel = new TodayComponent(logic);
		//ImageIcon todayIcon = createImageIcon("img/HEADER_today.png");
		addTab("Today",todayPanel);
		setMnemonicAt(0, KeyEvent.VK_1);
		
		taskPanel = new EventComponent(logic,Constants.TASK_TYPE_NUMBER);
		addTab("Tasks",taskPanel);
		setMnemonicAt(1, KeyEvent.VK_2);
		//setMnemonicAt(1, KeyEvent.VK_T);

		deadlinePanel = new EventComponent(logic,Constants.DEADLINE_TYPE_NUMBER);
		addTab("Deadlines",deadlinePanel);
		setMnemonicAt(2, KeyEvent.VK_3);
		//setMnemonicAt(1, KeyEvent.VK_D);

		schedulePanel = new EventComponent(logic,Constants.SCHEDULE_TYPE_NUMBER);
		addTab("Schedules",schedulePanel);
		setMnemonicAt(3, KeyEvent.VK_4);
		//setMnemonicAt(1, KeyEvent.VK_S);

		searchPanel = new SearchComponent(logic);
		addTab("Search",searchPanel);
		setMnemonicAt(4, KeyEvent.VK_5);

		helpPanel = new HelpComponent();
		addTab("Help",helpPanel);
		setMnemonicAt(5, KeyEvent.VK_6);
		//setMnemonicAt(5, KeyEvent.VK_H);


		addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				System.out.println(getSelectedIndex());
				if (getSelectedIndex()==0){ //today
					todayPanel.refresh();
				}else if (getSelectedIndex()==4){ //search result
					searchPanel.refresh();
				}else{
					refreshAllEventTabs(logic.updateDisplay());
				}

			}
	    });
		refresh(logic.updateDisplay());
	}

	protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }
	public void refresh(ArrayList<ArrayList<Event>> eventList){
		todayPanel.refresh();
		refreshAllEventTabs(eventList);
	}

	private void refreshAllEventTabs(ArrayList<ArrayList<Event>> eventList){
		taskPanel.refresh(eventList);
		deadlinePanel.refresh(eventList);
		schedulePanel.refresh(eventList);
	}
	 protected static ImageIcon createImageIcon(String path) {
	        java.net.URL imgURL = MainTab.class.getResource(path);
	        if (imgURL != null) {
	            return new ImageIcon(imgURL);
	        } else {
	            System.err.println("Couldn't find file: " + path);
	            return null;
	        }
	    }
}
