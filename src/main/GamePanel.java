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
	public int pB;

	public Player player;
	public MiniMap miniMap = new MiniMap(this);
    public BackgroundTiles tiles = new BackgroundTiles(this);
	public static Map<Integer, Snake> snakes = new HashMap<Integer, Snake>();
	public static HashSet<Food> foods = new HashSet<Food>();
	
	private MainFrame mainFrame;
	private static final Camera cam = new Camera(0, 0);
	private Thread gameThread;
    private Leaderboard lb = new Leaderboard(this);
    
	private BufferedImage backBuffer;
	private BufferedImage loseScreen;
	
	private int[] playerInfo = new int[2];
	private boolean runGame = false;
	private boolean lose = false;

	//Constructor
	public GamePanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.backBuffer = new BufferedImage(VIEW_WIDTH, VIEW_HEIGHT, BufferedImage.TYPE_INT_RGB); //gameboard
		
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
		bindEscKey();
	}
	
	//Description: The method starts game thread
	//Parameters: n/a
	public void startGame() {
		init();
		this.gameThread = new Thread(this);
		this.gameThread.start();
		runGame = true;

	}
	
	//Description: The method contains gamethread
	//Parameters: n/a
	@Override
	public void run() {
		//game loop
		double drawInterval = 1000000000 / FPS;
		double nextDrawTime = System.nanoTime() + drawInterval;
		
		while(runGame) {
			update();
			checkCollision();
			draw();
			if(!lose) {		
				try { //calculate time passed 
					double sleepTime = nextDrawTime - System.nanoTime();
					if (sleepTime < 0)
						sleepTime = 0;
					Thread.sleep((long) sleepTime / 1000000); //sleep thread for time left
					nextDrawTime += drawInterval;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			else
				break;
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
	
	//Description: The method stops game thread
	//Parameters:
	private void stopGame() {
		runGame = false;
		pB = playerInfo[0]; //set current length (personal best)
		this.gameThread = null;
	}
	
	//Description: The method updates game status (player location, focus camera on player, generate food, leaderboard)
	//Parameters: n/a
	private void update() {
		player.move(); 
		cam.focus(player); //move view screen to centre on player
		if (foods.size() < 2000) {
			Food newFood = new Food(randomPoint());
			while (!foods.add(newFood))
				;
		}
		lb.update();
	}

	//Description: The method draws game components
	//Parameters: n/a
	private void draw() {
		Graphics g = this.getGraphics(); //panel graphics
		Graphics2D gB = (Graphics2D) backBuffer.getGraphics(); //gameboard graphics
		Graphics2D gV = (Graphics2D) backBuffer.getGraphics(); //view screen graphics
	
		gB.setColor(Color.BLACK);
		gB.fillRect(0, 0, VIEW_WIDTH, VIEW_HEIGHT);	
		
		cam.turnOn(gB);
		tiles.draw(gB);
		player.draw(gB);
		Iterator<Food> it = foods.iterator();
		while (it.hasNext()) {
			Food f = it.next();
			f.draw(gB);
		}
		cam.turnOff(gB);

		if(lose) 
			drawLoseScreen(gV);
		else {
			miniMap.drawMiniMap(gV);
			lb.draw(gV);
			playerInfo = lb.playerInfo(gV);
		}

		g.drawImage(backBuffer, 0, 0, this);
		
	}

	//Description: The method initialise game variables
	//Parameters: n/a
	private void init() {
		lose = false;
		Iterator<Entry<Integer, Snake>> it = snakes.entrySet().iterator();
		while (it.hasNext()) {
		    it.next();
		    it.remove();
		}
		foods.removeAll(foods);
	
		player = new Player(new Point(randomPoint()), mainFrame.getPlayerName()); //create player
		snakes.put(0, player); //player always at map index 0
		cam.focus(player);
	}
	
	//Description: The method checks collision of snake head 
	//Parameters: n/a
	private void checkCollision() {
		Point h = player.getHead();
		if (h.x < topLeft.x || h.x + UNIT_SIZE > bottomRight.x || h.y < topLeft.y || h.y + UNIT_SIZE > bottomRight.y) { //snake head and border
			lose = true;
			stopGame();
			
		}
		
		if(!lose) { //snake head with food
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
	
	//Description: The method generate random point on board
	//Parameters: n/a
	//Return: Point
	private Point randomPoint() {
		Point p = new Point(0, 0);
		p.x = (int) (Math.random() * (MAP_WIDTH - VIEW_WIDTH - UNIT_SIZE) + BORDER_WIDTH);
		p.y = (int) (Math.random() * (MAP_HEIGHT - VIEW_HEIGHT - UNIT_SIZE) + BORDER_HEIGHT);
		return p;
	}
	
	//Description: The method draws random point on board
	//Parameters: n/a
	//Return: Point
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
	private void bindEscKey() {
        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escapeAction");
        Action escapeAction = new AbstractAction() {
			private static final long serialVersionUID = 1L; //suppress a serializable warning

			public void actionPerformed(ActionEvent e) {
            	stopGame();
            	try {
					Thread.sleep(200); //get main Thread to finish before exiting to menu
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
            	mainFrame.gameOff();
        	}
        };
        ActionMap actionMap = this.getActionMap();
        actionMap.put("escapeAction", escapeAction);
	}

}