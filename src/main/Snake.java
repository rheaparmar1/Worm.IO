package main;
import java.awt.*;
import java.util.ArrayList;

public class Snake {

	static final int u = GamePanel.UNIT_SIZE;
	final ArrayList<Point> body = new ArrayList<Point>();
	Point lastTail = new Point();
	int bodyParts = 12;

	public Snake(Point head) {
		init(head);
	}

	public Point getHead() {
		return new Point(body.get(0));
	}

	public void Grew() {
		bodyParts++;
		body.add(lastTail);
	}

	public void move(Point mouse) {
		Point h = getHead();

		double distance = Math.hypot(h.x-mouse.x, h.y-mouse.y);
		if (distance < u)
			return;

		lastTail = body.get(bodyParts - 1);

		for (int i = bodyParts-1; i > 0; i--)
			body.set(i, body.get(i - 1));

		Point newHead = calcNewPoint(body.get(0), mouse);
		body.set(0, newHead);
	}

	public void draw(Graphics g) {
		g.setColor(Color.GREEN);
		for (Point p : body)
			g.fillOval(p.x, p.y, u, u);
	}

	private void init(Point head) {
		for (int i = 0; i < bodyParts; i++) {
			body.add(new Point(head.x + u * i, head.y));
		}

	}

	private Point calcNewPoint(Point head, Point mouse) {
	//	System.out.println(head.x + "," + head.y + "->" + mouse.x + "," + mouse.y);
		double degree = 0;
		if (head.x < mouse.x && head.y < mouse.y) {
			degree = 360 - Math.toDegrees(Math.atan((double) (mouse.y - head.y) / (mouse.x - head.x)));
		} else if (head.x > mouse.x && head.y > mouse.y) {
			degree = 180 - Math.toDegrees(Math.atan((double) (head.y - mouse.y) / (head.x - mouse.x)));
		} else if (head.y > mouse.y && head.x < mouse.x) {
			degree = Math.toDegrees(Math.atan((double) (head.y - mouse.y) / (mouse.x - head.x)));
		} else if (head.y < mouse.y && head.x > mouse.x) {
			degree = 180 + Math.toDegrees(Math.atan((double) (mouse.y - head.y) / (head.x - mouse.x)));
		}
		Point p = new Point((int) (head.x + Math.cos(Math.toRadians(degree)) * 10),
				(int) (head.y - Math.sin(Math.toRadians(degree)) * 10));
		return p;
	}
}