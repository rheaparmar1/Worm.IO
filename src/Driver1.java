import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Driver1 extends JPanel implements ActionListener, KeyListener, FocusListener {
	static JFrame frame;
	static JPanel menuPanel;
	static JPanel gamePanel;
	static JPanel losePanel;


	int width = 1400;
	int height = 700;
	final Dimension screen = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
	static String currentScreen = "main";

	
	JButton playButton, rulesButton;
	JTextField nameTextArea;
	JLabel pbLabel;
	ImageIcon play = new ImageIcon("buttons/play.png");
	ImageIcon rules = new ImageIcon("buttons/rules.png");
	
	static BufferedImage mainMenu;
	
	int pb = 0;
	
	public Driver1() {
		frame = new JFrame("Worm.io");
		frame.setPreferredSize(new Dimension(width, height));
		
		menuPanel = new JPanel() {
			protected void paintComponent(Graphics g) {
		        super.paintComponent(g);
		        g.drawImage(mainMenu, 0, 0, null);
			}
		};
		menuPanel.setPreferredSize(new Dimension(width, height));
		menuPanel.setLayout(null);
		
		try { // Load all images
			mainMenu = ImageIO.read(new File("screens/main.png"));		
		} catch (IOException e) {
			System.out.println("File cannot be found"); 
		}
		
		pb = updatePB(pb);
		pbLabel = new JLabel("Your personal best is " + pb + "!");
		pbLabel.setBounds((width-250)/2, 270, 250, 60);
		pbLabel.setFont(new Font("Helvetica",Font.BOLD,22));
		pbLabel.setForeground(Color.white);
		
		nameTextArea = new JTextField ("enter name... ");
		nameTextArea.setFont(new Font("Helvetica",Font.BOLD,17));
		nameTextArea.setBounds((width-200)/2, 330, 200, 60);
		nameTextArea.setName("name");
		nameTextArea.addFocusListener(this);
		nameTextArea.addKeyListener(this);
		
		//Make JButton
		playButton = new JButton(play);
		playButton.setFont(new Font("Helvetica",Font.BOLD,17));
		playButton.setBounds((width-200)/2, 410, 200, 60);
		playButton.setActionCommand("play");
		playButton.addActionListener(this);
		
		rulesButton = new JButton(rules);
		rulesButton.setFont(new Font("Helvetica",Font.BOLD,17));
		rulesButton.setBounds((width-200)/2, 490, 200, 60);
		rulesButton.setActionCommand("rules");
		rulesButton.addActionListener(this);
		
		menuPanel.add(pbLabel);
		menuPanel.add(nameTextArea);
		menuPanel.add(playButton);
		menuPanel.add(rulesButton);
		
		gamePanel = new JPanel() {
			protected void paintComponent(Graphics g) {
		        super.paintComponent(g);
		       // g.drawImage(mainMenu, 0, 0, null);
			}
		};
		gamePanel.setPreferredSize(new Dimension(width, height));
		gamePanel.setLayout(null);

		
		
		
		frame.add(gamePanel);
		frame.add(menuPanel);

		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		new Driver1();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		System.out.println(currentScreen);
		if(currentScreen.equals("main")) { 
			g.drawImage(mainMenu, 0, 0, null);
		}
	}
	
	public static int updatePB(int pb) {
		BufferedReader inFile;
		int currentPB=-1;
		try {
			inFile = new BufferedReader(new FileReader("pb.txt"));
			currentPB=Integer.parseInt(inFile.readLine());
			inFile.close();
		}
		catch (FileNotFoundException e) {
			System.out.print(e);
		}catch (IOException e) {
			System.out.print(e);
		}
		
		if (pb>currentPB) {
			try {
				PrintWriter outFile = new PrintWriter (new FileWriter ("pb.txt"));
				outFile.println (pb);
				outFile.close ();
				return pb;
			}
			catch (IOException e ) {
				System.out.println("File error");
				return 0;
			}
		}
		else
			return currentPB;

	}

	public void actionPerformed(ActionEvent e) {
		String event = e.getActionCommand();
		if(event.equals("play")) {
			if(nameTextArea.getText().equals("enter name...")) 
				JOptionPane.showMessageDialog(null, "Enter name to play. Try again.", "Enter Name", JOptionPane.WARNING_MESSAGE);
			else if(nameTextArea.getText().length() >= 12)
				JOptionPane.showMessageDialog(null, "Entered name is too long. Try again.", "Name too long", JOptionPane.WARNING_MESSAGE);
			else {
				currentScreen = "play";
				frame.remove(menuPanel);
				frame.add(gamePanel);
				frame.revalidate();
			}
			
		}
		else if(event.equals("rules")) {
			JOptionPane.showMessageDialog(null, "Use your mousepad/mouse to pilot your snake around the map to eat food and grow. \n"
					+ "Kill other snakes by making their head collide into snake body/game borders. \n"
					+ "The objective is to be the largest snake in length. ", "How To Play", JOptionPane.PLAIN_MESSAGE);

		}
		
	}

	
	public void focusGained(FocusEvent e) {
		nameTextArea.setText("");
	}

	@Override
	public void focusLost(FocusEvent e) {
		if(nameTextArea.getText().equals("")) {
			nameTextArea.setText("enter name...");
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		String name = e.getComponent().getName();
		if(key == KeyEvent.VK_ENTER && name.equals("name")) {
			if(nameTextArea.getText().length() >= 12)
				JOptionPane.showMessageDialog(null, "Entered name is too long. Try again.", "Name too long", JOptionPane.WARNING_MESSAGE);
			else {
				currentScreen = "play";
				frame.remove(menuPanel);
				frame.add(gamePanel);
				frame.revalidate();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}

