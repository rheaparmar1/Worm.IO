package main;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.awt.image.DataBufferByte;

import javax.imageio.ImageIO;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable {
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

	final Snake player;
	final Map<Integer, Snake> snakes = new HashMap<Integer, Snake>();
	final HashSet<Food> foods = new HashSet<Food>();

	public BufferedImage fullMap;
	public BufferedImage originalMap;

	Graphics graphics;

	Thread gameThread;
    final Object modelLock = new Object();

	public GamePanel(MainFrame maniFrame) {
		this.maniFrame = maniFrame;
		this.originalMap = loadMap();

		player = new Snake(new Point(randomPoint()));
		setPreferredSize(new Dimension(VIEW_WIDTH, VIEW_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		setBackground(Color.BLACK);
		startGame();
	}

	public void startGame() {
		this.gameThread = new Thread(this);
		this.gameThread.start();
	}

	@Override
	public void paint(Graphics g) {
		this.fullMap = copyImage(originalMap); 
		
		this.graphics = fullMap.getGraphics();
		this.drawTile(graphics);
		this.draw(graphics);
		Point viewPoint = getViewPoint();
		BufferedImage viewImage = this.fullMap.getSubimage(viewPoint.x, viewPoint.y, VIEW_WIDTH, VIEW_HEIGHT);
		g.drawImage(viewImage, 0, 0, this);
	}
	
	public static BufferedImage copyImage(BufferedImage source){
	    BufferedImage bi = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
	    byte[] sourceData = ((DataBufferByte)source.getRaster().getDataBuffer()).getData();
	    byte[] biData = ((DataBufferByte)bi.getRaster().getDataBuffer()).getData();
	    System.arraycopy(sourceData, 0, biData, 0, sourceData.length);
	    return bi;
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
	
	public BufferedImage loadMap(){
		try {
			fullMap = ImageIO.read(new File("screens/background.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(fullMap.getWidth() + " x " + fullMap.getHeight());
		return fullMap;
	}
	
	public static Point randomPoint() {
		Point p = new Point(0,0);
		p.x=(int)(Math.random()*(MAP_WIDTH-VIEW_WIDTH-UNIT_SIZE)+BORDER_WIDTH); 
		p.y=(int)(Math.random()*(MAP_HEIGHT-VIEW_HEIGHT-UNIT_SIZE)+BORDER_HEIGHT); 
		return p;
	}

	private void update() {
		synchronized(modelLock) {
			if(foods.size() < 2000){
				Food newFood = new Food(randomPoint());
				while(!foods.add(newFood));
			}
		}
		
		
		Point m = MouseInfo.getPointerInfo().getLocation();
		Point f = maniFrame.getLocation();
		Point v = getViewPoint();
		Point p = new Point(m.x - f.x + v.x, m.y - f.y + v.y - 24);
		player.move(p);
	}
	


	private void draw(Graphics g) {
		synchronized(modelLock) {
			player.draw(g);
			for(Food f: foods){
				f.draw(g);
			}
		}
		
		
	}

	private void checkCollision() {
		Point h = player.getHead();
		if (h.x < topLeft.x || h.x + UNIT_SIZE > bottomRight.x
			|| h.y < topLeft.y || h.y + UNIT_SIZE > bottomRight.y) {

		}
		synchronized(modelLock) {
			Iterator it = foods.iterator();
			while (it.hasNext()) {
			    Food f = (Food) it.next();
			    if(f.checkCollide(h)) {
					player.Grew();
					System.out.println(player.bodyParts);
					System.out.println(foods.size());
				    it.remove();
				}
			}
			
		}
		
	}
	
	

	private Point getViewPoint() {
		Point h = player.getHead();
		int left = (int) (h.x - topLeft.x);
		if (left < 0)
			left = 0;
		if (left > BOARD_WIDTH)
			left = BOARD_WIDTH;
		int top = (int) (h.y - topLeft.y);
		if (top < 0)
			top = 0;
		if (top > BOARD_HEIGHT)
			top = BOARD_HEIGHT;
		return new Point(left, top);
	}

	private void drawTile(Graphics g) {

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