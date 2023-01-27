package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.*;
import java.util.*;

public class SnakeBot extends Snake{

	//Constructor
	public SnakeBot(Point head, int num) {
		super(head, "Worm Bot " + num, false);
	}
	
	public void drawHead(Graphics g) { //for minimap
		g.setColor(Color.MAGENTA);
		g.fillOval(headX-(u*10), headY-(u*10), u*20, u*20);
			
	}
	
	//Description: The method finds the point and distance of closest food
	//Parameteres: HashSet of all food objects (with it's coordinates)
	public TargetPoint findNearestFood(HashSet<Food> foods) {
		TargetPoint smallestTP = new TargetPoint();
		TargetPoint currentTP = new TargetPoint();

		//find the smallest distance to food and its coordinate
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
