package WormObj;
import java.awt.Color;

import main.GamePanel;
public class Player extends Worm{
	GamePanel gamePanel;
	
	public Player(GamePanel gp) {
		this.gamePanel = gp;
	}
	
	public void setDefaultValues() {
		x = 100;
		y = 100;
		speed = 5;
	}

	public void update() {
		//playerX += playerSpeed;

	}
	
	public void draw() {
		//g2.setColor(Color.white);
		//g2.drawRect(playerX, playerY, 50, 50);
	}
}
