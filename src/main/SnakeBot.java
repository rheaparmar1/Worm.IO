package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.*;
import java.util.*;

public class SnakeBot extends Snake{

	//CONSTRUCTOR
	public SnakeBot(Point head, int num) {
		super(head, "Worm Bot " + num, false);
	}
	
	//Description: The method draws dot to represent snake
	public void drawHead(Graphics g) { //for minimap
		g.setColor(Color.MAGENTA);
		g.fillOval(headX-(u*10), headY-(u*10), u*20, u*20);
			
	}
	
	//Description: The method finds the point and distance of closest food
	//Parameters: HashSet of all food objects (with it's coordinates)
	public TargetPoint findNearestFood(HashSet<Food> foods) {
		TargetPoint smallestTP = new TargetPoint();
		TargetPoint currentTP = new TargetPoint();
		boolean smoothFound = false;

		Iterator<Food> it = foods.iterator();
		while (it.hasNext()) {
			Food f = it.next();
			currentTP.distance = (int) Math.sqrt((f.x - headX) * (f.x - headX) + (f.y - headY) * (f.y - headY));
			boolean smooth = isSmoothTurn(f);
			if (smooth && currentTP.distance < smallestTP.distance) {
				smallestTP.distance = currentTP.distance;
				smallestTP.x = f.x;
				smallestTP.y = f.y;
				smoothFound = true;
			}
		}

		if (!smoothFound) {
			it = foods.iterator();
			while (it.hasNext()) {
				Food f = it.next();
				currentTP.distance = (int) Math.sqrt((f.x - headX) * (f.x - headX) + (f.y - headY) * (f.y - headY));
				if (currentTP.distance < smallestTP.distance) {
					smallestTP.distance = currentTP.distance;
					smallestTP.x = f.x;
					smallestTP.y = f.y;
					smoothFound = true;
				}
			}
		}

		return smallestTP;
	}
	//Description: The method finds if turn for bot would be sharp based on first and second body part
	//Parameters: Target location
	//Return: If turn is smooth
	private boolean isSmoothTurn(Point target) {
		double facingAngle = calcDegree(body.get(1), body.get(0));
		double angleOfTarget = calcDegree(body.get(0), target);
		double anglediff = (facingAngle - angleOfTarget + 180 + 360) % 360 - 180;

		if (anglediff < 45 && anglediff > -45)
			return true;
		return false;
	}
}
