package main;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.*;

public class MainFrame extends JFrame{
	
	
	static JPanel mainPanel;
	MenuPanel menuPanel;
	GamePanel gamePanel;
	CardLayout cardLayout;

	
	//ImageIcon play = new ImageIcon("play.jpg");
	//ImageIcon rules = new ImageIcon("rules.jpg");
	
	
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
	    //gamePanel.startGame();
	}
	
	public String getPlayerName() {
		return menuPanel.getPlayerName();
	}
	
	
	//switch to game panel
	public void gameOn() {
	    cardLayout.show(mainPanel, "game");
	    gamePanel.startGame();
	}
	
	//switch back to menu panel
	public void gameOff() {
	    cardLayout.show(mainPanel, "menu");
	}


}
