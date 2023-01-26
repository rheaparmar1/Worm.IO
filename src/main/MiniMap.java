package main;

import java.awt.*;
import java.awt.image.*;
import java.util.Map.Entry;

public class MiniMap extends BackgroundTiles{
	//Instance variables
	private GamePanel gp;
	private BufferedImage worldMap;
	private int width = (int) Math.ceil(GamePanel.BOARD_WIDTH/50.0);
	private int height = (int)Math.ceil(GamePanel.BOARD_HEIGHT/50.0);
	private int x = GamePanel.VIEW_WIDTH-width - 30;
	private int y = 10;
	
	//Constructor
	public MiniMap(GamePanel gp) {
		super(gp);
		this.gp = gp;
	}
	
	//Description: The method draws mini map onto view screen directly
	//Parameteres: Graphics2D
	public void drawMiniMap(Graphics2D g) {
		BufferedImage original = new BufferedImage(GamePanel.MAP_WIDTH, GamePanel.MAP_HEIGHT, BufferedImage.TYPE_INT_RGB); //full gameboard image
		Graphics graphics = original.getGraphics();
		Graphics2D g1 = (Graphics2D) graphics;
		
		gp.tiles.draw(g1);
		for(Entry<Integer, Snake> entry: GamePanel.snakes.entrySet()) {
			Snake s = entry.getValue();
			if(entry.getKey() == 0) {
				s.drawHead(g1);
			}
			else {
				SnakeBot sB = (SnakeBot) s;
				sB.drawHead(g1);
			}
		}
		
		BufferedImage resized = new BufferedImage(width, height, original.getType()); //resize full gameboard image
		Graphics2D gR = resized.createGraphics(); 
		
		
		gR.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		gR.drawImage(original, 0, 0, width, height, 0, 0, original.getWidth(), original.getHeight(), null); //draw resized image
		gR.dispose();
		
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
		g.drawImage(resized, x, y, width, height, null);
		g.setColor(new Color(22,22,22));
		g.drawRect(x, y, width, height);
	}
	
}
