package main;
import java.awt.*;
import java.util.ArrayList;

public class Snake {

	static final int u = GamePanel.UNIT_SIZE;
	final ArrayList<Point> body = new ArrayList<Point>();
	
	public int headX;
	public int headY;
	public String name;

	public int bodyParts = 12;

	private int speed = 10;
	private Point lastTail;

	private int goalX;
	private int goalY;
	
	public Snake(Point head, String name) {
		init(head);
		this.name = name;
	}

	public void setGoal(int x, int y) {
		goalX = x;
		goalY = y;
	}

	public Point getHead() {
		return new Point(headX, headY);
	}

	public void Grew() {
		bodyParts++;
		body.add(lastTail);
	}

	public void move() {
		double distance = Math.hypot(headX-goalX, headY-goalY);
		if (distance < u)
			return;

		lastTail = body.get(bodyParts - 1);

		for (int i = bodyParts-1; i > 0; i--)
			body.set(i, body.get(i - 1));

		Point newHead = calcNewPoint(body.get(0));
		setHead(newHead);
	}

	public void draw(Graphics g) {
		g.setColor(Color.GREEN);
		for (Point p : body)
			g.fillOval(p.x, p.y, u, u);
	}
	
	public void drawHead(Graphics g) { //for minimap
		g.setColor(Color.DARK_GRAY);
		g.fillOval(headX, headY, u*70, u*70);
			
	}

	private void init(Point head) {
		for (int i = 0; i < bodyParts; i++) {
			body.add(new Point(headX + u * i, headY));
		}
		setHead(head);
	}
	
	public void setHead(Point head) {
		headX = head.x;
		headY = head.y;
		if (body.size() > 0)
			body.set(0, head);
	}

	private Point calcNewPoint(Point head) {
		//	System.out.println(headX + "," + headY + "->" + mouse.x + "," + mouse.y);
			double degree = 0;

			if (headX < goalX && headY < goalY) {
				degree = 360 - Math.toDegrees(Math.atan((double) (goalY - headY) / (goalX - headX)));
			} else if (headX > goalX && headY > goalY) {
				degree = 180 - Math.toDegrees(Math.atan((double) (headY - goalY) / (headX - goalX)));
			} else if (headY > goalY && headX < goalX) {
				degree = Math.toDegrees(Math.atan((double) (headY - goalY) / (goalX - headX)));
			} else if (headY < goalY && headX > goalX) {
				degree = 180 + Math.toDegrees(Math.atan((double) (goalY - headY) / (headX - goalX)));
			}
			
			Point p = new Point((int) (headX + Math.cos(Math.toRadians(degree)) * speed),
					(int) (headY - Math.sin(Math.toRadians(degree)) * speed));
			return p;
		}

}