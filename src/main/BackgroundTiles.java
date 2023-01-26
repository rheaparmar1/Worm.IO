package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

public class BackgroundTiles {
	
	//Static Variable
	static final int TILE_SIZE = 50;
		
	//Instance Variables
	private GamePanel gp;
	private BufferedImage[] tile;
	
	//Constructor
	public BackgroundTiles(GamePanel gp) {
		this.gp = gp;
		tile = new BufferedImage[2];
		getTileImage();
	}
	
	//Description: The method imports tile images
	//Parameteres: n/a
	public void getTileImage() {
		try {
			tile[0] = ImageIO.read(new File("tiles/grass.png"));
			tile[1] = ImageIO.read(new File("tiles/water.png"));
			
			
		}catch(IOException e) {
			System.out.println("File not found");
		}
	}
	
	//Description: The method draw tiles onto map
	//Parameteres: Graphics2D
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
			if(col < borderCol || col >= maxMapCol-borderCol) //draw top and bottom border
				g2.drawImage(tile[1], x, y, TILE_SIZE, TILE_SIZE, null);
			else if(col >= borderCol && col <= maxMapCol-borderCol) { 
				if(row < borderRow || row >= maxMapRow-borderRow) //draw left and right border
					g2.drawImage(tile[1], x, y, TILE_SIZE, TILE_SIZE, null);
				else
					g2.drawImage(tile[0], x, y, TILE_SIZE, TILE_SIZE, null); //draw playing board
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
