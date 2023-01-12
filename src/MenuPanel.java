import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MenuPanel extends JPanel implements ActionListener, KeyListener, FocusListener {
	CardLayout cardLayout;

	//static JFrame frame;
	static JPanel myPanel;
	JButton playButton, rulesButton;
	JTextField nameTextArea;
	JPanel centrePanel = new JPanel();
	JLabel pbLabel;
	int pb;
	ImageIcon play = new ImageIcon("buttons/play.png");
	ImageIcon rules = new ImageIcon("buttons/rules.png");
	//final Dimension screen = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
	static int width = 1400;
	static int height = 700;
	static String currentScreen = "main";

	long currentTime;
	static BufferedImage mainMenu;
	MainFrame frame;
	

	public MenuPanel(MainFrame frame) {
		this.frame = frame;
		System.out.println(width + "" + height);
		cardLayout = new CardLayout();

		setPreferredSize(new Dimension(width, height));
		setLayout(null);
		
		try { // Load images
			mainMenu = ImageIO.read(new File("screens/main.png"));		
		} catch (IOException e) {
			System.out.println("File cannot be found"); 
		}
		pb = updatePB(pb);
		System.out.println(pb);
		pbLabel = new JLabel("Your personal best is " + pb + "!");
		pbLabel.setBounds((width-250)/2, 270, 300, 80);
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
		
		add(pbLabel);
		add(nameTextArea);
		add(playButton);
		add(rulesButton);
		
		
		//Make panels

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
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
				frame.gameOn();
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
				frame.gameOn();
			}
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
