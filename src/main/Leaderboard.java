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
	private int x;
  	private int y;
	int[] table = new int[10];
	int tableY = 0;
	Color color = new Color(50, 50, 50, 128);
	List<Snake> snakesCopy;
	int currentRank;
	
	public Leaderboard() {
		for(int i = 0; i < 10; i++) {
			table[i] = tableY;
			tableY+=30;
		}
	}
	
	public void update(){
		snakesCopy = new ArrayList<Snake>(GamePanel.snakes.values());
		if(snakesCopy.size()>1)
			Collections.sort(snakesCopy, new leaderComparator());
	    currentRank = Collections.binarySearch(snakesCopy, new Snake(new Point(0,0), "", true), new playerComparator());
	}
	
	public void draw(Graphics g) {
		for (int i = 0; i < 10; i++) {
	        g.setColor(color);
		    g.drawRect(0, y + table[i], 125, 30);
		    g.fillRect(0, y + table[i], 125, 30);
		    g.setColor(Color.WHITE);
	        if(GamePanel.snakes.size()>i)
	            g.drawString("#" + (i + 1) + ": " + snakesCopy.get(i).name + " : " + (int) snakesCopy.get(i).bodyParts, x, y + table[i] + 25);
	    }
	    
	    
	}
	
	public int[] playerInfo(Graphics g) {
		int[] playerInfo = new int[2];
		playerInfo[0] = snakesCopy.get(currentRank).bodyParts;
		playerInfo[1] = currentRank + 1;
		g.setColor(new Color(50, 50, 50, 128));
        g.drawRect(0, 700-125, 125, 50);
        g.fillRect(0, 700-125, 125, 50);
        g.setColor(Color.WHITE);
      
        g.drawString("Your length is " + playerInfo[0], 0, 700-105);
        g.drawString("Rank " + playerInfo[1] + " out of " + snakesCopy.size(), 0, 700-85);
        
        return playerInfo;
	}

	
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
	
	private class playerComparator implements Comparator<Snake> {
		public int compare(Snake s1, Snake s2) {
			return (-1 * Boolean.compare(s1.isPlayer, s2.isPlayer));
		}
	}

}

