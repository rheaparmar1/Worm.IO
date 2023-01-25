package main;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable, MouseMotionListener {
	MainFrame mainFrame;

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
	static String playerName;
	private int[] playerInfo = new int[2];

	private boolean runGame = false;
	private boolean lose = false;

	static final Camera cam = new Camera(0, 0);
	TileManager tileM = new TileManager(this);
	MiniMap miniMap = new MiniMap(this);
	Player player;
	final static Map<Integer, Snake> snakes = new HashMap<Integer, Snake>();
	final HashSet<Food> foods = new HashSet<Food>();

	public BufferedImage backBuffer;
	private BufferedImage loseScreen;

	private Thread gameThread;
	private final Object modelLock = new Object();
    public static Leaderboard lb = new Leaderboard();

	public GamePanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.backBuffer = new BufferedImage(VIEW_WIDTH, VIEW_HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		try { // Load images
			loseScreen = ImageIO.read(new File("screens/losescreen.png"));		
		} catch (IOException e) {
			System.out.println("File cannot be found"); 
		}
		
		player = new Player(new Point(randomPoint()), mainFrame.getPlayerName(), true); //create player
		snakes.put(0, player); //player always index 0 in map
		cam.set(player);
		setPreferredSize(new Dimension(VIEW_WIDTH, VIEW_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		setBackground(Color.BLACK);
		addMouseMotionListener(this);

        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escapeAction");
        Action escapeAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
            	stopGame();
    			mainFrame.gameOff();
    			reset();

        	}

        };
        ActionMap actionMap = this.getActionMap();
        actionMap.put("escapeAction", escapeAction);
	}

	public void startGame() {
		this.gameThread = new Thread(this);
		this.gameThread.start();
		runGame = true;
	}
	
	public void stopGame() {
		runGame = false;
		this.gameThread = null;
		if(lose) {
			repaint();
		}
	}


	@Override
	public void run() {
		// game loop
		while(runGame) {
			double drawInterval = 1000000000 / FPS;
			double nextDrawTime = System.nanoTime() + drawInterval;
			while (true) {
	
				update();
				checkCollision();
				if(!lose) {
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
				else
					break;
				
			}
		}
		
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(!lose)
			player.setGoal((int) (e.getX() + cam.x), (int) (e.getY() + cam.y));
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}
	


	private void update() {

		player.move();
		cam.set(player);

		synchronized (modelLock) {
			if (foods.size() < 2000) {
				Food newFood = new Food(randomPoint());
				while (!foods.add(newFood))
					;
			}
		}
		lb.update();
	}

	@Override
	public void paint(Graphics g4) {
		Graphics graphics = backBuffer.getGraphics();
		Graphics g2 = backBuffer.getGraphics();
		Graphics2D g3 = (Graphics2D) backBuffer.getGraphics();
		Graphics2D g = (Graphics2D) graphics;
	
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, VIEW_WIDTH, VIEW_HEIGHT);	
		
		cam.turnOn(g);
		tileM.draw(g);
		player.draw(g);


		synchronized (modelLock) {
			Iterator<Food> it = foods.iterator();
			while (it.hasNext()) {
				Food f = it.next();
				f.draw(g);
			}
		}
		
		cam.turnOff(g);

		if(lose) {
			drawLoseScreen(g2);
		}
		else {
			miniMap.drawMiniMap(g3);
			lb.draw(g2);
			playerInfo = lb.playerInfo(g2);
		}

		g4.drawImage(backBuffer, 0, 0, this);
	}
	
	private void reset() {
		lose = false;
		Iterator it = snakes.entrySet().iterator();
		while (it.hasNext()) {
		    Entry item = (Entry) it.next();
		    it.remove();
		}
		foods.removeAll(foods);
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
			lose = true;
			stopGame();
			
		}
		if(!lose) {
			synchronized (modelLock) {
				Iterator<Food> it = foods.iterator();
				while (it.hasNext()) {
					Food f = it.next();
					if (f.checkCollide(h)) {
						player.Grew();
						Point newPoint = randomPoint();
						f.x = newPoint.x;
						f.y = newPoint.y;
					}
				}
				
			}
		}
		

	}
	
	private void drawLoseScreen(Graphics g) {
		g.setColor(new Color(50, 50, 50, 128));
		g.drawRect(VIEW_WIDTH/3, VIEW_HEIGHT/4, VIEW_WIDTH/3, VIEW_HEIGHT/2);
	    g.fillRect(VIEW_WIDTH/3, VIEW_HEIGHT/4, VIEW_WIDTH/3, VIEW_HEIGHT/2);
	    g.setColor(Color.WHITE);
		g.drawImage(loseScreen, VIEW_WIDTH/3, VIEW_HEIGHT/4, null);
		g.setFont(new Font("Helvetica", Font.PLAIN, 24));
		g.drawString("Your length was " + playerInfo[0], VIEW_WIDTH/3+130, VIEW_HEIGHT/4+165);
		g.drawString("Your rank was " + playerInfo[1], VIEW_WIDTH/3+150, VIEW_HEIGHT/4+200);

	    
	}

//	private void drawBackground(Graphics g) {
//
//		g.setColor(new Color(125, 125, 125));
//		g.fillRect(0, 0, mapBottomRight.x, topLeft.y);
//		g.fillRect(0, bottomRight.y, mapBottomRight.x, mapBottomRight.y);
//		g.fillRect(0, 0, topLeft.x, mapBottomRight.y);
//		g.fillRect(bottomRight.x, 0, mapBottomRight.x, mapBottomRight.y);
//
//		int color = 87;
//		int tileSize = 80;
//		g.setColor(new Color(color % 255, color % 255, color % 255));
//		for (int j = 0; j <= BOARD_HEIGHT + VIEW_HEIGHT; j += tileSize)
//		g.drawLine(0, j, BOARD_WIDTH + VIEW_WIDTH, j);
//		for (int i = 0; i <= BOARD_WIDTH + VIEW_WIDTH; i += tileSize)
//		g.drawLine(i, 0, i, BOARD_HEIGHT + VIEW_HEIGHT);
//	}

}