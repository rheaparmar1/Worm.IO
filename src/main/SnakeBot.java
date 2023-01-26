package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class SnakeBot extends Snake{

	public SnakeBot(Point head, int num) {
		super(head, "Snake Bot " + num, false);
		// TODO Auto-generated constructor stub
	}
	public void drawHead(Graphics g) { //for minimap
		g.setColor(Color.MAGENTA);
		g.fillOval(headX-(u*35), headY-(u*35), u*40, u*40);
			
	}
}
