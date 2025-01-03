package main;
import java.awt.CardLayout;
import java.awt.Color;
import java.util.Random;

import javax.swing.*;

public class MainFrame extends JFrame{
	
	//INSTANCE VARIABLES
	private static JPanel mainPanel;
	private MenuPanel menuPanel;
	private GamePanel gamePanel;
	private CardLayout cardLayout;

	//CONSTRUCTOR
	public MainFrame() {
		cardLayout = new CardLayout();
		mainPanel = new JPanel(cardLayout);
		
		//make panels
		menuPanel = new MenuPanel(this);
		gamePanel = new GamePanel(this);
        mainPanel.add(menuPanel, "menu");
        mainPanel.add(gamePanel, "game");
       
		add(mainPanel);
		pack();
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	//Get player name
	public String getPlayerName() {
		return menuPanel.getPlayerName();
	}
	
	//switch to game panel
	public void gameOn() {
	    cardLayout.show(mainPanel, "game");
	    gamePanel.startGame();
	}
	
	//switch to menu panel
	public void gameOff() {
		menuPanel.updatePB(gamePanel.pB);
	    cardLayout.show(mainPanel, "menu");

	}

}
