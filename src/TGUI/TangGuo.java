package TGUI;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
public class TangGuo {
	public static void main(String[] args) {
		/*
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeels");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		JFrame frame = new MainFrame("TangGuo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setVisible(true);
		/*
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		System.out.println(screenSize.getWidth());
		JFrame frame = new MainFrame("TangGuo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize((int)screenSize.getWidth(), (int)screenSize.getHeight());
		frame.setVisible(true);
		*/
	}
}
