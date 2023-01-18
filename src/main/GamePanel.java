package main;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable, MouseMotionListener {
	MainFrame maniFrame;

	static final int VIEW_WIDTH = 1400;
	static final int VIEW_HEIGHT = 700;
	static final int MAP_WIDTH = 7000;
	static final int MAP_HEIGHT = 4200;
	static final int BORDER_WIDTH = (int) VIEW_WIDTH / 2;
	static final int BORDER_HEIGHT = (int) VIEW_HEIGHT / 2;
	static final int BOARD_WIDTH = MAP_WIDTH - BORDER_WIDTH * 2;
	static final int BOARD_HEIGHT = MAP_HEIGHT - BORDER_HEIGHT * 2;
	static final Point topLeft = new Point((int) VIEW_WIDTH / 2, (int) VIEW_HEIGHT / 2);
	static final Point bottomRight = new Point(BOARD_WIDTH + (int) VIEW_WIDTH / 2, BOARD_HEIGHT + (int) VIEW_HEIGHT / 2);
	static final Point mapBottomRight = new Point(BOARD_WIDTH + VIEW_WIDTH, BOARD_HEIGHT + VIEW_HEIGHT);
	static final int UNIT_SIZE = 10;
	static final int FRUIT_COUNT = 100;
	static final int FPS = 60;

	static final Camera cam = new Camera(0, 0, 1, 1);
	Player player;
	final Map<Integer, Snake> snakes = new HashMap<Integer, Snake>();
	final HashSet<Food> foods = new HashSet<Food>();

	public BufferedImage backBuffer;

	private Insets insets;
	private Thread gameThread;
	private final Object modelLock = new Object();

	public GamePanel(MainFrame maniFrame) {
		this.maniFrame = maniFrame;
		this.backBuffer = new BufferedImage(VIEW_WIDTH, VIEW_HEIGHT, BufferedImage.TYPE_INT_RGB);
		;

		player = new Player(new Point(randomPoint()));
		cam.Set(player);
		setPreferredSize(new Dimension(VIEW_WIDTH, VIEW_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		setBackground(Color.BLACK);
		addMouseMotionListener(this);

		insets = getInsets();
		startGame();
	}

	public void startGame() {
		this.gameThread = new Thread(this);
		this.gameThread.start();
	}

	@Override
	public void paint(Graphics g) {
		this.draw();
		g.drawImage(backBuffer, insets.left, insets.top, this);
	}

	@Override
	public void run() {
		// game loop
		double drawInterval = 1000000000 / FPS;
		double nextDrawTime = System.nanoTime() + drawInterval;
		while (true) {

			update();
			checkCollision();
			repaint();

			try {
				double sleepTime = nextDrawTime - System.nanoTime();
				if (sleepTime < 0)
					sleepTime = 0;
				Thread.sleep((long) sleepTime / 1000000);
				nextDrawTime += drawInterval;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		player.setGoal((int) (e.getX() / cam.scaleX + cam.x), (int) (e.getY() / cam.scaleY + cam.y));
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	private void update() {

		player.move();
		cam.Set(player);

		synchronized (modelLock) {
			if (foods.size() < 2000) {
				Food newFood = new Food(randomPoint());
				while (!foods.add(newFood))
					;
			}
		}

	}

	private void draw() {

		Graphics graphics = backBuffer.getGraphics();

		Graphics2D g = (Graphics2D) graphics;
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, VIEW_WIDTH, VIEW_HEIGHT);
		
		cam.turnOn(g);
		this.drawBackground(g);

		player.draw(g);
		synchronized (modelLock) {
			Iterator it = foods.iterator();
			while (it.hasNext()) {
				Food f = (Food) it.next();
				f.draw(g);
			}
		}

		cam.turnOff(g);
	}

	private Point randomPoint() {
		Point p = new Point(0, 0);
		p.x = (int) (Math.random() * (MAP_WIDTH - VIEW_WIDTH - UNIT_SIZE) + BORDER_WIDTH);
		p.y = (int) (Math.random() * (MAP_HEIGHT - VIEW_HEIGHT - UNIT_SIZE) + BORDER_HEIGHT);
		return p;
	}

	private void checkCollision() {
		Point h = player.getHead();
		if (h.x < topLeft.x || h.x + UNIT_SIZE > bottomRight.x
				|| h.y < topLeft.y || h.y + UNIT_SIZE > bottomRight.y) {

		}
		synchronized (modelLock) {
			Iterator it = foods.iterator();
			while (it.hasNext()) {
				Food f = (Food) it.next();
				if (f.checkCollide(h)) {
					player.Grew();
					it.remove();
				}
			}
		}

	}

	private void drawBackground(Graphics g) {

		g.setColor(new Color(125, 125, 125));
		g.fillRect(0, 0, mapBottomRight.x, topLeft.y);
		g.fillRect(0, bottomRight.y, mapBottomRight.x, mapBottomRight.y);
		g.fillRect(0, 0, topLeft.x, mapBottomRight.y);
		g.fillRect(bottomRight.x, 0, mapBottomRight.x, mapBottomRight.y);

		int color = 87;
		int tileSize = 80;
		g.setColor(new Color(color % 255, color % 255, color % 255));
		for (int j = 0; j <= BOARD_HEIGHT + VIEW_HEIGHT; j += tileSize)
		g.drawLine(0, j, BOARD_WIDTH + VIEW_WIDTH, j);
		for (int i = 0; i <= BOARD_WIDTH + VIEW_WIDTH; i += tileSize)
		g.drawLine(i, 0, i, BOARD_HEIGHT + VIEW_HEIGHT);
	}
}