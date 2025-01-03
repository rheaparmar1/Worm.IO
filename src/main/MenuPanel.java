package main;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class MenuPanel extends JPanel implements ActionListener, KeyListener, FocusListener {
	// INSTANCE VARIABLES
	private MainFrame frame;
	private CardLayout cardLayout;

	private JButton playButton, rulesButton, aboutButton;
	private JTextField nameTextArea;
	private JLabel pbLabel;
	private int pb;
	private ImageIcon play = new ImageIcon("buttons/play.png");
	private ImageIcon rules = new ImageIcon("buttons/rules.png");
	private ImageIcon about = new ImageIcon("buttons/about.png");
	private static int width = 1400;
	private static int height = 700;
	private static BufferedImage mainMenu;

	//CONSTRUCTOR
	public MenuPanel(MainFrame frame) {
		this.frame = frame;
		cardLayout = new CardLayout();
		setPreferredSize(new Dimension(width, height));
		setLayout(null);

		try { // Load image
			mainMenu = ImageIO.read(new File("screens/main.png"));
		} catch (IOException e) {
			System.out.println("File cannot be found");
		}

		pb = updatePB(pb);
		if (pb==0)
			pbLabel = new JLabel("     No personal best!");
		else
			pbLabel = new JLabel("Your personal best is " + pb + "!");
		pbLabel.setBounds((width - 250) / 2, 270, 300, 80);
		pbLabel.setFont(new Font("Helvetica", Font.BOLD, 22));
		pbLabel.setForeground(Color.white);

		nameTextArea = new JTextField("enter name... ");
		nameTextArea.setFont(new Font("Helvetica", Font.BOLD, 17));
		nameTextArea.setBounds((width - 200) / 2, 330, 200, 60);
		nameTextArea.setName("name");
		nameTextArea.addFocusListener(this);
		nameTextArea.addKeyListener(this);

		//set up buttons
		playButton = new JButton(play);
		playButton.setBounds((width - 200) / 2, 410, 200, 60);
		playButton.setActionCommand("play");
		playButton.addActionListener(this);

		rulesButton = new JButton(rules);
		rulesButton.setBounds((width - 200) / 2, 490, 200, 60);
		rulesButton.setActionCommand("rules");
		rulesButton.addActionListener(this);

		aboutButton = new JButton(about);
		aboutButton.setBounds(width - 140, height - 100, 80, 40);
		aboutButton.setActionCommand("about");
		aboutButton.addActionListener(this);

		add(pbLabel);
		add(nameTextArea);
		add(playButton);
		add(rulesButton);
		add(aboutButton);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(mainMenu, 0, 0, null);

	}

	// Description: The method gets greatest personal best from text file
	// Parameters: int new personal best
	// Return: int of highest pb
	public int updatePB(int pb) {
		BufferedReader inFile;
		int currentPB = -1;
		try {
			inFile = new BufferedReader(new FileReader("pb.txt"));
			currentPB = Integer.parseInt(inFile.readLine());
			inFile.close();
		} catch (FileNotFoundException e) {
			System.out.print(e);
		} catch (IOException e) {
			System.out.print(e);
		}

		if (pb > currentPB) { // add new pb
			try {
				PrintWriter outFile = new PrintWriter(new FileWriter("pb.txt"));
				outFile.println(pb);
				outFile.close();
					pbLabel.setText("Your personal best is " + pb + "!");
				return pb;
			} catch (IOException e) {
				System.out.println("File error");
				return 0;
			}
		} else
			return currentPB;
	}

	// Description: The method checks if user entry name is valid
	// Parameters: n/a
	// Return: boolean isValidEntry
	private boolean checkName() {
		boolean isValid = false;
		if (nameTextArea.getText().equals("enter name...") || nameTextArea.getText().equals(""))
			JOptionPane.showMessageDialog(null, "Enter name to play. Try again.", "Enter Name",
					JOptionPane.WARNING_MESSAGE);
		else if (nameTextArea.getText().length() >= 12)
			JOptionPane.showMessageDialog(null, "Entered name is too long. Try again.", "Name too long",
					JOptionPane.WARNING_MESSAGE);
		else {
			isValid = true;
		}
		return isValid;
	}

	public void actionPerformed(ActionEvent e) {
		String event = e.getActionCommand();
		if (event.equals("play")) {
			if (checkName())
				frame.gameOn();
		} else if (event.equals("rules")) {
			JOptionPane.showMessageDialog(null,
					"Use your mousepad/mouse to pilot your worm around the map to eat food and grow. \n"
							+ "Kill other worms by making their head collide into worm body/game borders. \n"
							+ "The objective is to be the largest worm in length. "
							+ "\n\n\t- Turn hacks on/off by pressing the space bar." + "\n\t- Speed boost (with min length 8) by holding down mouse."+ "\n\t- Hit esc key to return to main menu.",
							"How To Play", JOptionPane.QUESTION_MESSAGE);
		} else if (event.equals("about")) {
			JOptionPane.showMessageDialog(null, "Creators: Karen & Rhea \nIdea from: Slither.io\n\n Have fun!",
					"About Worm.io", JOptionPane.QUESTION_MESSAGE);
		}
	}

	// GETTERS + SETTERS
	public String getPlayerName() {
		return nameTextArea.getText();
	}

	//ABSTRACT METHODS
	@Override
	public void focusGained(FocusEvent e) {
		nameTextArea.setText("");
	}

	@Override
	public void focusLost(FocusEvent e) {
		if (nameTextArea.getText().equals("")) {
			nameTextArea.setText("enter name...");
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		String name = e.getComponent().getName();
		if (key == KeyEvent.VK_ENTER && name.equals("name")) {
			if (checkName())
				frame.gameOn();

		}
	}

}