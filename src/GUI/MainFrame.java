package GUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.Command;
import main.Logic;

public class MainFrame extends JFrame{
	public MainFrame(String title, String fileAddress){
		super(title);
		Logic TGlogic = new Logic(fileAddress);
		setLayout(new BorderLayout());
		MainTab tabbedPane = new MainTab(TGlogic);
		final JPanel commandPane = new JPanel();
		commandPane.setLayout(new BoxLayout(commandPane,BoxLayout.Y_AXIS));
		final JPanel inputPane = new JPanel();
		inputPane.setLayout(new FlowLayout());
		final JTextField textArea = new JTextField();
		final JLabel commandLabel = new JLabel("Command:");
		final JLabel messageLabel = new JLabel("");
		textArea.setPreferredSize(new Dimension(550,20));
		JButton button = new JButton("Submit");
		Container c = getContentPane();
		c.add(tabbedPane,BorderLayout.CENTER);
		commandPane.add(messageLabel);
		inputPane.add(commandLabel);
		inputPane.add(textArea);
		inputPane.add(button);
		commandPane.add(inputPane);
		c.add(commandPane,BorderLayout.SOUTH);
		//c.add(textArea,BorderLayout.SOUTH);
		//c.add(button,BorderLayout.LINE_END);

		textArea.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e){
            	//tabbedPane.setSelectedIndex(0);

            	Command command = TGlogic.executeInputs(textArea.getText());
            	System.out.println(command.getDisplayedEventList());
            	if (command.getDisplayedEventList()!=null){ //valid command
            		tabbedPane.refresh(command.getDisplayedEventList());
            	}
            	messageLabel.setText(command.getDisplayMessage());
            	System.out.println(command.getDisplayMessage());
            	textArea.setText("");
            }});
	}
}
