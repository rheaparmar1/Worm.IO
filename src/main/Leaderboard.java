package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class Leaderboard{
	//Instance variables
	private GamePanel gp;
	private int x;
  	private int y;
	private int[] table = new int[10];
	private int tableY = 0;
	private List<Snake> snakesCopy;
	private int rank;
	
	//Constructor
	public Leaderboard(GamePanel gp) {
		this.gp = gp;
		for(int i = 0; i < 10; i++) {
			table[i] = tableY;
			tableY+=30;
		}
	}
	
	//Description: The method orders snakes from largest to smallest
	//Parameteres: n/a
	public void update(){
		snakesCopy = new ArrayList<Snake>(GamePanel.snakes.values());
		if(snakesCopy.size()>1)
			Collections.sort(snakesCopy, new leaderComparator());
	    rank = Collections.binarySearch(snakesCopy, new Snake(new Point(0,0), "", true), new playerComparator());
	}
	
	//Description: The method draws leaderboard onto view screen directly
	//Parameteres: Graphics
	public void draw(Graphics g) {
		for (int i = 0; i < 10; i++) {
	        g.setColor(new Color(50, 50, 50, 128));
		    g.drawRect(0, y + table[i], 125, 30);
		    g.fillRect(0, y + table[i], 125, 30);
		    g.setColor(Color.WHITE);
	        if(GamePanel.snakes.size()>i)
	            g.drawString("#" + (i + 1) + ": " + snakesCopy.get(i).name + " : " + (int) snakesCopy.get(i).bodyParts, x, y + table[i] + 25);
	    }    
	}
	
	//Description: The method gets player rank and length
	//Parameteres: Graphics
	public int[] playerInfo(Graphics g) {
		int[] playerInfo = new int[2];
		playerInfo[0] = snakesCopy.get(rank).bodyParts;
		playerInfo[1] = rank + 1;
		g.setColor(new Color(50, 50, 50, 128));
        g.drawRect(0, GamePanel.VIEW_HEIGHT-125, 125, 50);
        g.fillRect(0, GamePanel.VIEW_HEIGHT-125, 125, 50);
        g.setColor(Color.WHITE);
      
        g.drawString("Your length is " + playerInfo[0], 0, GamePanel.VIEW_HEIGHT-105);
        g.drawString("Rank " + playerInfo[1] + " out of " + snakesCopy.size(), 0, GamePanel.VIEW_HEIGHT-85);
        
        return playerInfo;
	}

	//Description: This is a comparator class to compare snake body lengths 
	private class leaderComparator implements Comparator<Snake> {
		public int compare(Snake s1, Snake s2) {
			if(s1.bodyParts == s2.bodyParts)
		        return 0;
		    else if(s1.bodyParts > s2.bodyParts)
		        return -1;
		    else
		    	return 1;
		}
	}
	
	//Description: This is a comparator class to compare whether snake is a player
	private class playerComparator implements Comparator<Snake> {
		public int compare(Snake s1, Snake s2) {
			return (-1 * Boolean.compare(s1.isPlayer, s2.isPlayer));
		}
	}

}

