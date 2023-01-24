package main;

import java.awt.*;
import java.awt.image.*;

public class MiniMap extends TileManager{
	GamePanel gp;
	BufferedImage worldMap;
	
	int width = (int) Math.ceil(GamePanel.BOARD_WIDTH/50.0);
	int height = (int)Math.ceil(GamePanel.BOARD_HEIGHT/50.0);
	int x = GamePanel.VIEW_WIDTH-width - 30;
	int y = 10;
	
	public MiniMap(GamePanel gp) {
		super(gp);
		this.gp = gp;
	}
	
	public void drawMiniMap(Graphics2D g) {
		BufferedImage original = new BufferedImage(GamePanel.MAP_WIDTH, GamePanel.MAP_HEIGHT, BufferedImage.TYPE_INT_RGB);
		

		Graphics graphics = original.getGraphics();
		Graphics2D g1 = (Graphics2D) graphics;
		

		gp.tileM.draw(g1);
		gp.player.drawHead(g1);
		
		
		BufferedImage resized = new BufferedImage(width, height, original.getType());
		Graphics2D gR = resized.createGraphics();
		
		gR.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		gR.drawImage(original, 0, 0, width, height, 0, 0, original.getWidth(),
				original.getHeight(), null);
		gR.dispose();
		
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
		g.drawImage(resized, x, y, width, height, null);
		g.setColor(new Color(22,22,22));
		g.drawRect(x, y, width, height);
	}
	
}
