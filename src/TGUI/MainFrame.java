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

import javafx.scene.input.KeyCode;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.sun.corba.se.impl.oa.poa.ActiveObjectMap.Key;

import TGLogic.Logic;
import TGUtils.Command;

public class MainFrame extends JFrame{
	public MainFrame(String title){
		super(title);
		Logic TGlogic = new Logic();
		setLayout(new BorderLayout());
		MainTab tabbedPane = new MainTab(TGlogic);
		final JPanel commandPane = new JPanel();
		commandPane.setLayout(new BoxLayout(commandPane,BoxLayout.Y_AXIS));
		final JPanel inputPane = new JPanel();
		inputPane.setLayout(new FlowLayout());
		final JTextField textArea = new JTextField();
		final JLabel commandLabel = new JLabel("Command:");
		final JLabel messageLabel = new JLabel("Welcome Back to TangGuo");
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
		
		Stack<String> up = new Stack<String>();
		Stack<String> down = new Stack<String>();
		textArea.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					if (!up.isEmpty()) {
						down.push(textArea.getText());
						textArea.setText(up.pop());
					}
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					if (!down.isEmpty()) {
						up.push(textArea.getText());
						textArea.setText(down.pop());
					}
				}
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});

		textArea.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e){
            	//tabbedPane.setSelectedIndex(0);
            	Command command = TGlogic.executeInputs(textArea.getText());
            	up.push(textArea.getText());
            	System.out.println(command.getDisplayedEventList());
            	if (command.getDisplayedTab()!=-1){
            		tabbedPane.setSelectedIndex(command.getDisplayedTab());
            	}
            	if (command.getDisplayedEventList()!=null){ //valid command
            		tabbedPane.refresh(command.getDisplayedEventList());
            	}
            	messageLabel.setText(command.getDisplayMessage());
            	System.out.println(command.getDisplayMessage());
            	textArea.setText("");
            }});
	}
}
