import javax.swing.JFrame;

public class Driver {
	static int width = 1400;
	static int height = 700;
	
	public static void main(String[] args) {
		JFrame frame = new MainFrame();
		
		frame.setSize(width, height);
		frame.setLocationRelativeTo(null);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setVisible(true);
	}
	
	
}
