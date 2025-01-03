package main;

import java.awt.*;

public class Food extends Point {
	//INSTANCE VARIABLES
	static final int size = GamePanel.UNIT_SIZE;
	Color color = new Color(124, 24, 14);
	boolean fromBody = false;
	long timestamp = System.currentTimeMillis();

	//CONSTRUCTORS
	public Food(Point p) {
		this.x = p.x;
		this.y = p.y;
	}
	public Food(Point p, Color c) {
		this.x = p.x;
		this.y = p.y;
		color=c;
	}
	//Description: The method checks if the snake head collides with food
	//Parameters: Point of snake head
	//Return: boolean if collide = true
	public boolean checkFoodCollide(Point head) {
		int x = head.x;
		int y = head.y;
		return x < this.x + size && x + size > this.x && y < this.y + size && y + size > this.y;
	}
	//Description: The method draws food
	//Parameters: Graphics
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillOval(x, y, size, size);
	}
}