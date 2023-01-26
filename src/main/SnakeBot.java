package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.*;
import java.util.*;

public class SnakeBot extends Snake{

	public SnakeBot(Point head, int num) {
		super(head, "Worm Bot " + num, false);
		// TODO Auto-generated constructor stub
	}
	public void drawHead(Graphics g) { //for minimap
		g.setColor(Color.MAGENTA);
		g.fillOval(headX-(u*35), headY-(u*35), u*20, u*20);
			
	}
	
	
	public TargetPoint findNearestFood(HashSet<Food> foods) {
		TargetPoint smallestTP = new TargetPoint();
		TargetPoint currentTP = new TargetPoint();

		
		Iterator<Food> it = foods.iterator();
		while(it.hasNext()) {
			Food f = it.next();
			currentTP.distance = (int) Math.sqrt((f.x-headX)*(f.x-headX)+(f.y-headY)*(f.y-headY));
			if(currentTP.distance < smallestTP.distance) {
				smallestTP.distance = currentTP.distance;
				smallestTP.x = f.x;
				smallestTP.y = f.y;
			}
			
		}
		
		return smallestTP;
		
	}
}
