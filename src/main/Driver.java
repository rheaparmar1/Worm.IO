package main;

import javax.swing.JFrame;
//Karen
public class Driver {
	private static int width = 1400;
	private static int height = 700;
	
	
	public static void main(String[] args) {
		JFrame frame = new MainFrame();
		
		frame.setSize(width, height);
		frame.setLocationRelativeTo(null);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setVisible(true);
	    
	}
	
	
}
