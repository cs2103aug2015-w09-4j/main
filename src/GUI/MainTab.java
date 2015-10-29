package GUI;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class MainTab extends JTabbedPane {
	TodayComponent panel1;
	JComponent panel2;
	JComponent panel3;
	JComponent panel4;
	public MainTab(){
		super();
		 panel1 = new TodayComponent();
		addTab("Today",panel1);
		setMnemonicAt(0, KeyEvent.VK_1);

		 panel2 = makeTextPanel("Panel #2");
		addTab("Tab 2",panel2);
		setMnemonicAt(1, KeyEvent.VK_2);

		 panel3 = makeTextPanel("Panel #3");
		addTab("Tab 3",panel3);
		setMnemonicAt(2, KeyEvent.VK_3);

		 panel4 = makeTextPanel(
		        "Panel #4 (has a preferred size of 410 x 50).");
		panel4.setPreferredSize(new Dimension(410, 50));
		addTab("Tab 4",panel4);
		setMnemonicAt(3, KeyEvent.VK_4);
	}

	protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }
	public void refresh(){
		panel1.refresh();
	}
}
