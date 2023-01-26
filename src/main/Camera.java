//This class gets the view image/coordinates (the part of map shown on screen) from the game board map (the world the snake can be on)
package main;

import java.awt.*;

public class Camera {
	//Instance variables
	double x, y; //top left coordinate of view screen based on board

	//Constructor
	public Camera(double x, double y) {
		this.x = x;
		this.y = y;
	}

	//Description: The method begins translating board coordinates to view coordinates
	//Parameteres: Graphics
	public void turnOn(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.translate(-x, -y); //translation based on player (to centre it on view screen)
	}

	//Description: The method stops translating board coordinates to view coordinates
	//Parameteres: Graphics
	public void turnOff(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.translate(x, y); //translation stops
	}
	
	//Description: The method sets player coordinates to view screen central point
	//Parameteres: Player obj
	public void focus(Player player) {
		x = player.headX + GamePanel.UNIT_SIZE / 2 - GamePanel.VIEW_WIDTH / 2;
		y = player.headY + GamePanel.UNIT_SIZE / 2 - GamePanel.VIEW_HEIGHT/ 2;
	}
}