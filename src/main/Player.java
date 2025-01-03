package main;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class Player extends Snake {
	//INSTANCE VARIABLE
	private Timer boost;
	//CONSTRUCTOR
	public Player(Point head, String name) {
		super(head, name, true);
	}
	// inner class to deal with the timer events
	private class TimerEventHandler implements ActionListener {
		// The action performed is called every second
		public void actionPerformed (ActionEvent event){
			
			//reduces snake length + creates "poop"
			if (length>8) {
				Point p=body.remove(length - 1);
				length--;
				GamePanel.foods.add(new Food(p, color));
			}
			//prevents going below limit of 8 for length
			if (length<9)
				speed=10;
			
		}
	}
	//Description: The method increases player speed
	public void boost() {
		if (length>8) {
			speed=20;
			boost = new Timer (1000, new TimerEventHandler ());
			boost.start();
		}
	}
	//Description: The method decreases player speed
	public void reduce() {
		speed=10;
		if (boost!=null&&boost.isRunning())
			boost.stop();
	}

}