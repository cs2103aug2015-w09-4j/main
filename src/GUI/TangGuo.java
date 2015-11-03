package GUI;

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
		}*/
		JFrame frame = new MainFrame("TangGuo",args[0]);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 800);
		frame.setVisible(true);


	}
}
