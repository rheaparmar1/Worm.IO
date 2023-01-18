package main;

import java.awt.*;

public class Camera {

	double x, y;
	double scaleX, scaleY;

	public Camera(double x, double y, double scaleX, double scaleY) {
		this.x = x;
		this.y = y;
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}

	public double map(double x, double min1, double max1, double min2, double max2) {
		return (x - min1) * (max2 - min2) / (max1 - min1) + min2;
	}

	public void turnOn(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.scale(scaleX, scaleY);
		g2.translate(-x, -y);
	}

	public void turnOff(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.translate(x, y);
	}

	public void scale(double scaleX, double scaleY) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}

	public void Set(Player player) {
		double scaleFactor = 1;
		scale(scaleFactor, scaleFactor);
		x = player.headX + GamePanel.UNIT_SIZE / 2 - GamePanel.VIEW_WIDTH / scaleX / 2;
		y = player.headY + GamePanel.UNIT_SIZE / 2 - GamePanel.VIEW_HEIGHT / scaleY / 2;
	}
}