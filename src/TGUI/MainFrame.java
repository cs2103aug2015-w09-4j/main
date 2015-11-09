package TGUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Stack;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.tg.backend.Logic;

import TGUtils.Command;

public class MainFrame extends JFrame{
	private Logic TGlogic;
	private MainTab tabbedPane;
	private JPanel commandPane;
	private Stack<String> up;
	private Stack<String> down;
	private JPanel inputPane;
	private JTextField commandField;
	private JLabel commandLabel;
	private JLabel messageLabel;
	public MainFrame(String title){
		super(title);
		TGlogic = new Logic();
		setLayout(new BorderLayout());
		tabbedPane = new MainTab(TGlogic);
		commandPane = new JPanel();
		commandPane.setLayout(new BoxLayout(commandPane,BoxLayout.Y_AXIS));
		inputPane = new JPanel();
		inputPane.setLayout(new FlowLayout());
		commandField = new JTextField();
		commandLabel = new JLabel("Command:");
		messageLabel = new JLabel("Welcome Back to TangGuo");
		commandField.setPreferredSize(new Dimension(600,20));
		JButton button = new JButton("Submit");
		Container c = getContentPane();
		c.add(tabbedPane,BorderLayout.CENTER);
		commandPane.add(messageLabel);
		inputPane.add(commandLabel);
		inputPane.add(commandField);
		inputPane.add(button);
		commandPane.add(inputPane);
		c.add(commandPane,BorderLayout.SOUTH);
		up = new Stack<String>();
		down = new Stack<String>();
		button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				submitCommand();
			}

		});
		commandField.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				//no event

			}

			@Override
			public void keyReleased(KeyEvent e) {
				//no event
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					if (!up.isEmpty()) {
						down.push(commandField.getText());
						commandField.setText(up.pop());
					}
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					if (!down.isEmpty()) {
						up.push(commandField.getText());
						commandField.setText(down.pop());
					}
				}

			}

		});

		commandField.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	submitCommand();
            }
        });
	}

	private void submitCommand(){
		Command command = TGlogic.executeInputs(commandField.getText());
    	up.push(commandField.getText());
    	if (command.getDisplayedTab()!=-1){
    		tabbedPane.setSelectedIndex(command.getDisplayedTab());
    	}
    	if (command.getDisplayedEventList()!=null){ //valid command
    		tabbedPane.refresh(command.getDisplayedEventList());
    	}
    	messageLabel.setText(command.getDisplayMessage());
    	System.out.println(command.getDisplayMessage());
    	commandField.setText(""); //reset the content of textArea
	}
}
