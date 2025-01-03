package main;

import java.awt.*;
import java.util.*;
import java.util.Map.Entry;
import java.awt.event.*;
import javax.swing.Timer;

public abstract class Snake {

	//INSTANCE VARIABLES
	public static final int u = GamePanel.UNIT_SIZE;
	protected final ArrayList<Point> body = new ArrayList<Point>();

	protected int headX;
	protected int headY;
	public String name;

	public boolean isPlayer;
	public int length = 8;
	public int speed = 10;
	private Point lastTail;
	private double degree = 0;
	private int goalX;
	private int goalY;
	protected Color color;

	//CONSTUCTOR
	public Snake(Point head, String name, boolean isPlayer) {
		for (int i = 0; i < length; i++) {
			body.add(new Point(head.x, head.y));
		}
		lastTail = head;
		setHead(head);
		this.name = name;
		this.isPlayer = isPlayer;
		color = randomColour();
	}

	//Description: Calculates if food is visible in cam
	//Parameters: Location of food
	//Return: boolean representing if food is visible
	public boolean inView(Point food) {
		if(headX + GamePanel.BORDER_WIDTH < food.x || food.x + GamePanel.BORDER_WIDTH < headX
				|| headY + GamePanel.BORDER_HEIGHT < food.y || food.y + GamePanel.BORDER_HEIGHT < headY)
			return false;
		return true;
	}

	//Description: Increases snake size
	public void grow() {
		length++;
		body.add(lastTail);
	}
	
	//Description: Moves snake towards target (mouse for player vs food for bot)
	public void move() {
		double distance = Math.hypot(headX - goalX, headY - goalY);

		if (distance < u) {
			Point newPoint = GamePanel.getMousePoint();
			setTarget(newPoint.x, newPoint.y);

		}
		lastTail = body.get(length - 1);
		for (int i = length - 1; i > 0; i--)
			body.set(i, body.get(i - 1));

		Point newHead = calcNewPoint();
		setHead(newHead);
	}

	//Description: The method draws snakes
	//Parameters: Graphics
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillOval(headX, headY, u + 2, u + 2);
		
		//draws circles
		for (Point p : body) {
			g.fillOval(p.x, p.y, u, u);
		}
		//draws spine
		for (int i = 0; i < length - 1; i++) {
			Point p1 = body.get(i);
			Point p2 = body.get(i + 1);
			g.drawLine(p1.x + u / 2, p1.y + u / 2, p2.x + u / 2, p2.y + u / 2);
		}
		drawEyes(g);
	}
	//Description: The method draws snake eyes
	//Parameters: Graphics
	private void drawEyes(Graphics g) {
		double eyeRadius = u / 3;
		double pupilRadius = u / 5.5;
		double angle1 = degree + 60;
		double angle2 = degree - 60;
		Point mid = new Point(headX + (u / 2), headY + (u / 2));
		Point eyeL = new Point((int) (mid.x + Math.cos(Math.toRadians(angle1)) * (u / 2)),
				(int) (mid.y - Math.sin(Math.toRadians(angle1)) * (u / 2)));
		Point eyeR = new Point((int) (mid.x + Math.cos(Math.toRadians(angle2)) * (u / 2)),
				(int) (mid.y - Math.sin(Math.toRadians(angle2)) * (u / 2)));
		g.setColor(Color.BLACK);
		g.fillOval((int) (eyeL.x - eyeRadius), (int) (eyeL.y - eyeRadius), (int) (eyeRadius * 2),
				(int) (eyeRadius * 2));
		g.fillOval((int) (eyeR.x - eyeRadius), (int) (eyeR.y - eyeRadius), (int) (eyeRadius * 2),
				(int) (eyeRadius * 2));
		g.setColor(Color.white);
		g.fillOval((int) (eyeL.x - eyeRadius), (int) (eyeL.y - pupilRadius), (int) (pupilRadius * 2),
				(int) (pupilRadius * 2));
		g.fillOval((int) (eyeR.x - eyeRadius), (int) (eyeR.y - pupilRadius), (int) (pupilRadius * 2),
				(int) (pupilRadius * 2));
	}
	
	//Description: The method used to display hack mode
	//Parameters: Graphics
	public void drawHacks(Graphics g) {
		g.drawLine(headX, headY, goalX, goalY); // hacker mode

	}
	
	//Description: The method draws snakes on map
	//Parameters: Graphics
	public void drawHead(Graphics g) { // for minimap
		g.setColor(Color.DARK_GRAY);
		g.fillOval(headX - (u * 25), headY - (u * 25), u * 50, u * 50);

	}
	
	//Description: The method checks collision of snakes with other snakes
	//Parameters: key of snake and map of snakes
	public boolean checkBodyCollide(int key, Map<Integer, Snake> snakes) {
		int distance = 0;
		Snake currentS = snakes.get(key);
		Point head = currentS.getHead();

		for (Entry<Integer, Snake> entry : snakes.entrySet()) {
			if (entry.getKey() != key) {

				Snake s = entry.getValue();
				for (Point point : s.body) {
					distance = (int) Math
							.sqrt((point.x - head.x) * (point.x - head.x) + 
									(point.y - head.y) * (point.y - head.y));
					if (distance < u) {
						// key is the snake that died
						return true;
					}
				}
			}
		}

		return false;
	}

	//Description: The method chooses random pastel colour
	//Parameters: n/a
	//Return: color
	private Color randomColour() {
		Random random = new Random();
		return Color.getHSBColor(random.nextFloat(), 0.6f, 1.0f);
	}
	
	//Description: This method calculates new point of head for movement
	//Parameters: Graphics
	private Point calcNewPoint() {
		double degree = calcDegree(new Point(headX, headY), new Point(goalX, goalY));
		Point p = new Point((int) (headX + Math.cos(Math.toRadians(degree)) * speed),
				(int) (headY - Math.sin(Math.toRadians(degree)) * speed));
		return p;
	}
	
	//Description: This method calculates angle between two points
	//Parameters: Graphics
	protected double calcDegree(Point start, Point end) {
		if (start.x < end.x && start.y <= end.y) {
			degree = 360 - Math.toDegrees(Math.atan((double) (end.y - start.y) / (end.x - start.x)));
		} else if (start.x > end.x && start.y >= end.y) {
			degree = 180 - Math.toDegrees(Math.atan((double) (start.y - end.y) / (start.x - end.x)));
		} else if (start.y >= end.y && start.x < end.x) {
			degree = Math.toDegrees(Math.atan((double) (start.y - end.y) / (end.x - start.x)));
		} else if (start.y <= end.y && start.x > end.x) {
			degree = 180 + Math.toDegrees(Math.atan((double) (end.y - start.y) / (start.x - end.x)));
		} else if (start.y < end.y && start.x == end.x) {
			degree = 270;
		} else if (start.y > end.y && start.x == end.x) {
			degree = 90;
		}

		return degree;
	}
	
	//COMPARETO
	public int compareTo(Snake s) {
		return this.length-s.length;
	}
	
	//GETTERS + SETTERS
	public String getName() {
		return this.name;
	}
	public void setHead(Point head) {
		headX = head.x;
		headY = head.y;
		if (body.size() > 0)
			body.set(0, head);
	}
	public void setTarget(int x, int y) {
		goalX = x;
		goalY = y;
	}
	public Point getHead() {
		return new Point(headX, headY);
	}


}