package main;
import java.awt.*;

public class Player extends Snake {
	private String direction;
	public Player(Point head, String name) {
		super(head, name, true);
	}
	
	public void setDirection(int mouseX, int mouseY) {
		mouseX-=headX;
		mouseY-=headY;
		if (mouseX>0) {
			if (mouseY>mouseX)
				direction="down";
			else if (mouseY*-1<mouseX)
				direction="right";
			else 
				direction="up";		
		}
		else {
			if (mouseY<mouseX)
				direction="up";
			else if (mouseY>mouseX*-1)
				direction="down";
			else 
				direction="left";		
		}
		
	}

}