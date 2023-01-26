package main;
import java.awt.CardLayout;


import javax.swing.*;

public class MainFrame extends JFrame{
	

	static JPanel mainPanel;
	MenuPanel menuPanel;
	GamePanel gamePanel;
	CardLayout cardLayout;

	
	
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
		menuPanel.updatePB(gamePanel.pB);
	    cardLayout.show(mainPanel, "menu");
		menuPanel.repaint();

	}


}
