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
	public int length = 12;
	int circles = 60;
	public int speed = 10;
	private Point lastTail;

	private int goalX;
	private int goalY;
	private Color color;
	Timer boost = new Timer();

	
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
			Point p =MouseInfo.getPointerInfo().getLocation();
			goalX=p.x + (int)GamePanel.cam.x;
		
			goalY=p.y+ (int)GamePanel.cam.y;
			System.out.println(distance);

			return;
			
		}
		lastTail = body.get(length - 1);
		for (int i = length-1; i > 0; i--)
			body.set(i, body.get(i - 1));

		Point newHead = calcNewPoint();
		setHead(newHead);
	}


	public void draw(Graphics g) {
		g.setColor(color);
		g.fillOval(headX, headY, u+2, u+2);

		for (Point p : body) {
			if(p == null)
				continue;
			g.fillOval(p.x, p.y, u, u);
		}
		g.setColor(Color.BLACK);
		g.fillOval(headX-1, headY-1, 6, 6);
		g.fillOval(headX+7, headY+7, 6, 6);
		g.setColor(Color.white);
		g.fillOval(headX, headY, 3, 3);
		g.fillOval(headX+8, headY+8, 3, 3);
		
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
						//key is the snake that died
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