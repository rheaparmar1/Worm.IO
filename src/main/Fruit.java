package main;
import java.awt.*;

public class Fruit{

	Point p;
	static final int u = GamePanel.UNIT_SIZE;
	
	public Fruit(Point p){
		this.p = p;
	}

	public void draw(Graphics g) {
		g.setColor(Color.red);
		g.fillOval(p.x, p.y, u, u);
	}

}