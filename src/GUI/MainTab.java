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
	EventComponent panel2;
	EventComponent panel3;
	EventComponent panel4;
	SearchComponent panel5;
	public MainTab(Logic logic){
		super();
		todayPanel = new TodayComponent(logic);
		ImageIcon todayIcon = createImageIcon("img/HEADER_today.png");
		addTab("",todayIcon,todayPanel);
		setMnemonicAt(0, KeyEvent.VK_1);
		panel2 = new EventComponent(logic,Constants.TASK_TYPE_NUMBER);
		addTab("Tasks",panel2);
		setMnemonicAt(1, KeyEvent.VK_2);

		panel3 = new EventComponent(logic,Constants.DEADLINE_TYPE_NUMBER);
		addTab("Deadlines",panel3);
		setMnemonicAt(2, KeyEvent.VK_3);

		panel4 = new EventComponent(logic,Constants.SCHEDULE_TYPE_NUMBER);
		addTab("Schedules",panel4);
		setMnemonicAt(3, KeyEvent.VK_4);

		panel5 = new SearchComponent(logic);
		addTab("Search",panel5);
		setMnemonicAt(4, KeyEvent.VK_5);
		addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				System.out.println(getSelectedIndex());
				if (getSelectedIndex()==0){ //today
					todayPanel.refresh();
				}else if (getSelectedIndex()==4){ //search result
					panel5.refresh();
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
		panel2.refresh(eventList);
		panel3.refresh(eventList);
		panel4.refresh(eventList);
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
