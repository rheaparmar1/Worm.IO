package main;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Food{
	public static Map<Integer, Food> foods = new HashMap<Integer, Food>();

	Point p;
	int x,y;
	static final int size = GamePanel.UNIT_SIZE;
	boolean isVisible;
	
	public Food(Point p){
		this.p = p;
		this.x = p.x;
		this.y = p.y;
		this.isVisible = true;
	}
	
	public boolean checkCollide(Point head){
		int x = head.x;
		int y = head.y;
		return x < this.x + 10 && x + size > this.x && y < this.y + 10 && y + size > this.y;
	}

	public void draw(Graphics g) {
		g.setColor(Color.red);
		g.fillOval(p.x, p.y, size, size);
		
		
	}

}