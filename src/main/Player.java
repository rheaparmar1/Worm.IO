package main;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class Player extends Snake {
	public Player(Point head, String name) {
		super(head, name, true);
	}
	
	private class TimerEventHandler implements ActionListener {
		// The following method is called each time a timer event is
		// generated (every 100 milliseconds in this example)
		// Put your code here that handles this event
		public void actionPerformed (ActionEvent event)
		{

			// Increment the time (you could also count down)
			if (length>8) {
				Point p=body.remove(length - 1);
				length--;
				GamePanel.foods.add(new Food(p));
				//System.out.print("remove");
			}
			if (length<9)
				speed=10;
			// Beep every second for this demo
			// You probably don't want this annoying beep in your game



		}
	}
	public void boost(int key) {
		if (key==1&&length>8) {
			speed=20;
			boost = new Timer (1000, new TimerEventHandler ());
			boost.start();
		}
	}

}