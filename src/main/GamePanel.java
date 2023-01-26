package main;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
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

	private static final Camera cam = new Camera(0, 0);
	private Thread gameThread;
    private Leaderboard lb = new Leaderboard(this);
    
	public Player player;
	public MiniMap miniMap = new MiniMap(this);
    public TileManager tileM = new TileManager(this);

	
	private BufferedImage backBuffer;
	private BufferedImage loseScreen;
	
	static Map<Integer, Snake> snakes = new HashMap<Integer, Snake>();
	static HashSet<Food> foods = new HashSet<Food>();

	private int[] playerInfo = new int[2];
	private boolean runGame = false;
	private boolean lose = false;
	int pB;

	
	public GamePanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.backBuffer = new BufferedImage(VIEW_WIDTH, VIEW_HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		try { //load game over image
			loseScreen = ImageIO.read(new File("screens/losescreen.png"));		
		} catch (IOException e) {
			System.out.println("File cannot be found"); 
		}
				
		
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
        	}
        };
        ActionMap actionMap = this.getActionMap();
        actionMap.put("escapeAction", escapeAction);
	}

	public void startGame() {
		reset();
		player = new Player(new Point(randomPoint()), mainFrame.getPlayerName(), true); //create player
		snakes.put(0, player); //player always map index 0
		cam.set(player);
		
		this.gameThread = new Thread(this);
		this.gameThread.start();
		runGame = true;

	}
	
	public void stopGame() {
		runGame = false;
		pB = playerInfo[0];
		this.gameThread = null;
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

	@Override
	public void run() {
		// game loop
		
			double drawInterval = 1000000000 / FPS;
			double nextDrawTime = System.nanoTime() + drawInterval;
			
			while(runGame) {
				System.out.println("yes");

				update();
				checkCollision();
				if(runGame)
					draw();
				if(!lose) {		
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

	private void update() {

		player.move();
		cam.set(player);
		if (foods.size() < 2000) {
			Food newFood = new Food(randomPoint());
			while (!foods.add(newFood))
				;
		}
		lb.update();
	}
	
	public void draw() {
		Graphics g4 = this.getGraphics();
		Graphics graphics = backBuffer.getGraphics();
		Graphics g2 = backBuffer.getGraphics();
		Graphics2D g3 = (Graphics2D) backBuffer.getGraphics();
		Graphics2D g = (Graphics2D) graphics;
	
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, VIEW_WIDTH, VIEW_HEIGHT);	
		
		cam.turnOn(g);
		tileM.draw(g);
		player.draw(g);

		Iterator<Food> it = foods.iterator();
		while (it.hasNext()) {
			Food f = it.next();
			f.draw(g);
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


	
	private void checkCollision() {
		Point h = player.getHead();
		if (h.x < topLeft.x || h.x + UNIT_SIZE > bottomRight.x
				|| h.y < topLeft.y || h.y + UNIT_SIZE > bottomRight.y) {
			lose = true;
			stopGame();
			
		}
		if(!lose) {
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
	
	private Point randomPoint() {
		Point p = new Point(0, 0);
		p.x = (int) (Math.random() * (MAP_WIDTH - VIEW_WIDTH - UNIT_SIZE) + BORDER_WIDTH);
		p.y = (int) (Math.random() * (MAP_HEIGHT - VIEW_HEIGHT - UNIT_SIZE) + BORDER_HEIGHT);
		return p;
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
	
	@Override
	public void mouseMoved(MouseEvent e) {
		if(!lose)
			player.setGoal((int) (e.getX() + cam.x), (int) (e.getY() + cam.y));
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}
}