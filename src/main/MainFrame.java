package main;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainFrame extends JFrame{
	
	
	static JPanel mainPanel;
	MenuPanel menuPanel;
	GamePanel gamePanel;
	CardLayout cardLayout;


	
	JButton playButton, rulesButton;
	JTextField nameTextArea;
	JPanel centrePanel = new JPanel();
	JLabel pb;
	//ImageIcon play = new ImageIcon("play.jpg");
	//ImageIcon rules = new ImageIcon("rules.jpg");
	
	static BufferedImage mainMenu;
	
	public MainFrame() {
		cardLayout = new CardLayout();
		mainPanel = new JPanel(cardLayout);
		
		//make panels
		menuPanel = new MenuPanel(this);
		gamePanel = new GamePanel(this);

        mainPanel.add(gamePanel, "game");
        mainPanel.add(menuPanel, "menu");
        
      
		add(mainPanel);
		pack();
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	    gamePanel.startGame();
	}
	
	public String getPlayerName() {
		return menuPanel.getPlayerName();
	}
	
	
	//switch to game panel
	public void gameOn() {
	    cardLayout.show(mainPanel, "game");
	}
	
	//switch back to menu panel
	public void gameOff() {
	    cardLayout.show(mainPanel, "menu");
	}
}
