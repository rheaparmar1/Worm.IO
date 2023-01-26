package main;
import java.awt.*;
import java.util.*;
import java.util.Map.Entry;

public class Snake{
	static final int u = GamePanel.UNIT_SIZE;
	final ArrayList<Point> body = new ArrayList<Point>();
	
	public int headX;
	public int headY;
	public String name;

	boolean isPlayer;
	public int length = 20;
	int circles = 60;
	private int speed = 10;
	private Point lastTail;

	private int goalX;
	private int goalY;
	private Color color;
	
	
	public Snake(Point head, String name, boolean isPlayer) {
		init(head);
		this.name = name;
		this.isPlayer = isPlayer;
		color = randomColour();
	}
	
	public String getName() {
		return this.name;
	}

	public void setTarget(int x, int y) {
		goalX = x;
		goalY = y;
	}

	public Point getHead() {
		return new Point(headX, headY);
	}

	public void Grew() {
		length++;
		body.add(lastTail);
	}

	public void move() {
		double distance = Math.hypot(headX-goalX, headY-goalY);
		
		if (distance < u) {
			return;
			
		}
		lastTail = body.get(length - 1);
		lastTail = new Point(lastTail.x, lastTail.y);
		for (int i = length-1; i > 0; i--)
			body.set(i, body.get(i - 1));

		Point newHead = calcNewPoint();
		setHead(newHead);
	}


	public void draw(Graphics g) {
		g.setColor(color);
		g.fillOval(headX, headY, u+2, u+2);
		for (Point p : body)
			g.fillOval(p.x, p.y, u, u);

	}
	

	
	public void drawHead(Graphics g) { //for minimap
		g.setColor(Color.DARK_GRAY);
		g.fillOval(headX-(u*25), headY-(u*25), u*50, u*50);
			
	}
	
	
	public boolean checkBodyCollide(int key, Map<Integer, Snake> snakes) {
		int distance = 0;
		
		for(Entry<Integer, Snake> entry: snakes.entrySet()) {
			if(entry.getKey() != key) {

				Snake s = entry.getValue();
				for(Point point: s.body) {
					if(point == null) 
						continue;
					distance = (int) Math.sqrt((point.x-headX)*(point.x-headX)+(point.y-headY)*(point.y-headY));
					if(distance < u) {
						
						return true;
					}
				}
			}
		}
		
		return false;
	}

	private void init(Point head) {
		

		for(int i = 0; i < length; i++) {
			body.add(new Point(headX, headY));
		}
		setHead(head);

	}
	
	public void setHead(Point head) {
		headX = head.x;
		headY = head.y;
		if (body.size() > 0)
			body.set(0, head);
	}

	private Color randomColour() {
		Random random = new Random();
		return Color.getHSBColor(random.nextFloat(), 0.6f, 1.0f);
	}
	
	private Point calcNewPoint() {
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
         Point p = new Point((int) 
                 (headX + Math.cos(Math.toRadians(degree)) * speed), (int) 
                 (headY - Math.sin(Math.toRadians(degree)) * speed));
         return p;
		}


}