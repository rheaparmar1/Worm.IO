package main;

import java.awt.*;
import java.io.*;

import javax.imageio.ImageIO;

public class TileManager {
	GamePanel gp;
	Tile[] tile;
	static final int TILE_SIZE = 50;

	
	public TileManager(GamePanel gp) {
		this.gp = gp;
		tile = new Tile[2];
		getTileImage();
	}
	
	public void getTileImage() {
		try {
			tile[0] = new Tile();
			tile[0].image = ImageIO.read(new File("tiles/grass.png"));
			
			tile[1] = new Tile();
			tile[1].image = ImageIO.read(new File("tiles/water.png"));
			
			
		}catch(IOException e) {
			System.out.println("File not found");
		}
	}
	
	public void draw(Graphics2D g2) {
		int maxScreenCol = GamePanel.BOARD_WIDTH/TILE_SIZE;
		int maxScreenRow = GamePanel.BOARD_HEIGHT/TILE_SIZE;
		int maxMapCol = GamePanel.MAP_WIDTH/TILE_SIZE;
		int maxMapRow = GamePanel.MAP_HEIGHT/TILE_SIZE;
		int borderCol = (maxMapCol-maxScreenCol)/2;
		int borderRow = (maxMapRow-maxScreenRow)/2;
		
		
		int col = 0; 
		int row = 0;
		int x = 0;
		int y = 0;
		
		
		
		while(col < maxMapCol && row < maxMapRow) {
			if(col < borderCol || col >= maxMapCol-borderCol) 
				g2.drawImage(tile[1].image, x, y, TILE_SIZE, TILE_SIZE, null);
			else if(col >= borderCol && col <= maxMapCol-borderCol) {
				if(row < borderRow || row >= maxMapRow-borderRow)
					g2.drawImage(tile[1].image, x, y, TILE_SIZE, TILE_SIZE, null);
				else
					g2.drawImage(tile[0].image, x, y, TILE_SIZE, TILE_SIZE, null);
			}
			
			col++;
			x+= TILE_SIZE;
			
			if(col == maxMapCol) {
				col = 0;
				x = 0;
				row++;
				y+= TILE_SIZE;
			}
		}
		
		
		
	}
}
