package GUI;

import javax.swing.JFrame;

public class App {
	public static void main(String[] args) {
		JFrame frame = new MainFrame("Tangguo",args[0]);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setVisible(true);


	}
}
