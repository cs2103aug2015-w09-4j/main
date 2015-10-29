package GUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MainFrame extends JFrame{
	public MainFrame(String title){
		super(title);
		setLayout(new BorderLayout());
		MainTab tabbedPane = new MainTab();
		final JPanel commandPane = new JPanel();
		commandPane.setLayout(new FlowLayout());
		final JTextField textArea = new JTextField();
		final JLabel commandLabel = new JLabel("Command:");
		textArea.setPreferredSize(new Dimension(550,20));
		JButton button = new JButton("Submit");
		Container c = getContentPane();
		c.add(tabbedPane,BorderLayout.CENTER);
		commandPane.add(commandLabel);
		commandPane.add(textArea);
		commandPane.add(button);
		c.add(commandPane,BorderLayout.SOUTH);
		//c.add(textArea,BorderLayout.SOUTH);
		//c.add(button,BorderLayout.LINE_END);

		textArea.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e){
            	tabbedPane.setSelectedIndex(0);
            	tabbedPane.refresh();
            }});
	}
}
