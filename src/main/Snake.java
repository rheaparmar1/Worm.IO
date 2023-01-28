package main;

import java.awt.*;
import java.util.*;
import java.util.Map.Entry;
import java.awt.event.*;
import javax.swing.Timer;

public class Snake implements ActionListener {
	static final int u = GamePanel.UNIT_SIZE;
	final ArrayList<Point> body = new ArrayList<Point>();

	public int headX;
	public int headY;
	public String name;

	boolean isPlayer;
	public int length = 8;
	int circles = 60;
	public int speed = 10;
	private Point lastTail;
	private double degree = 0;
	private int goalX;
	private int goalY;
	Color color;
	Timer boost;

	public Snake(Point head, String name, boolean isPlayer) {
		init(head);
		this.name = name;
		this.isPlayer = isPlayer;
		color = randomColour();
	}

	public void reduce() {
		speed=10;
		if (boost!=null&&boost.isRunning())
			boost.stop();
	}

	public String getName() {
		return this.name;
	}
	
	public boolean inView(Point food) {
		if(headX + GamePanel.BORDER_WIDTH < food.x || food.x + GamePanel.BORDER_WIDTH < headX
				|| headY + GamePanel.BORDER_HEIGHT < food.y || food.y + GamePanel.BORDER_HEIGHT < headY)
			return false;
		return true;
	}

	public void setTarget(int x, int y) {
		goalX = x;
		goalY = y;
	}

	public Point getHead() {
		return new Point(headX, headY);
	}

	public void grow() {
		length++;
		body.add(lastTail);
	}

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

	public void draw(Graphics g) {
		g.setColor(color);
		g.fillOval(headX, headY, u + 2, u + 2);

		for (Point p : body) {
			g.fillOval(p.x, p.y, u, u);
		}

		for (int i = 0; i < length - 1; i++) {
			Point p1 = body.get(i);
			Point p2 = body.get(i + 1);
			g.drawLine(p1.x + u / 2, p1.y + u / 2, p2.x + u / 2, p2.y + u / 2);
		}
		drawEyes(g);

	}

	private void drawEyes(Graphics g) {
		double eyeRadius = u / 3;
		double pupilRadius = u / 4;
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

	public void drawHacks(Graphics g) {
		g.drawLine(headX, headY, goalX, goalY); // hacker mode

	}

	public void drawHead(Graphics g) { // for minimap
		g.setColor(Color.DARK_GRAY);
		g.fillOval(headX - (u * 25), headY - (u * 25), u * 50, u * 50);

	}

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

	private void init(Point head) {
		for (int i = 0; i < length; i++) {
			body.add(new Point(head.x, head.y));
		}
		lastTail = head;
		setHead(head);

	}

	public void setHead(Point head) {
		headX = head.x;
		headY = head.y;
		if (body.size() > 0)
			body.set(0, head);
	}

	protected boolean isSmoothTurn(Point target) {
		double facingAngle = calcDegree(body.get(1), body.get(0));
		double angleOfTarget = calcDegree(body.get(0), target);
		double anglediff = (facingAngle - angleOfTarget + 180 + 360) % 360 - 180;

		if (anglediff < 45 && anglediff > -45)
			return true;
		return false;
	}

//	public int compareTo(Snake s) {
//		return this.length-s.length;
//	}
	private Color randomColour() {
		Random random = new Random();
		return Color.getHSBColor(random.nextFloat(), 0.6f, 1.0f);
	}

	private Point calcNewPoint() {
		double degree = calcDegree(new Point(headX, headY), new Point(goalX, goalY));
		Point p = new Point((int) (headX + Math.cos(Math.toRadians(degree)) * speed),
				(int) (headY - Math.sin(Math.toRadians(degree)) * speed));
		return p;
	}

	private double calcDegree(Point start, Point end) {
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

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}