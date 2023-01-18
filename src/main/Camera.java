//get view map (the map shown on screen) from the board map (the world the snake can be on)
package main;

import java.awt.*;

public class Camera {

	double x, y;

	public Camera(double x, double y) {
		this.x = x;
		this.y = y;
	}



	public void turnOn(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.translate(-x, -y);
	}

	public void turnOff(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.translate(x, y);
	}

	

	public void set(Player player) {
		double scaleFactor = 1;
		x = player.headX + GamePanel.UNIT_SIZE / 2 - GamePanel.VIEW_WIDTH / 2;
		y = player.headY + GamePanel.UNIT_SIZE / 2 - GamePanel.VIEW_HEIGHT/ 2;
	}
}