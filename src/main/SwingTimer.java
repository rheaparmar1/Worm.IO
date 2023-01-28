package main;
/** The "SwingTimer" class.
 * Demonstrates a Swing Timer
 */
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class SwingTimer extends JPanel 
{
	Timer timer;
	boolean timerOn;
	int time;

	public SwingTimer ()
	{


		timerOn = true;
		time = 0;

		// Create a timer object. This object generates an event every
		// 1/10 second (100 milliseconds)
		// The TimerEventHandler object that will handle this timer
		// event is defined below as a inner class
		timer = new Timer (1000, new TimerEventHandler ());

	} // Constructor


	// An inner class to deal with the timer events
	private class TimerEventHandler implements ActionListener
	{
		// The following method is called each time a timer event is
		// generated (every 100 milliseconds in this example)
		// Put your code here that handles this event
		public void actionPerformed (ActionEvent event)
		{
			
				// Increment the time (you could also count down)
				
				time++;

				// Beep every second for this demo
				// You probably don't want this annoying beep in your game
				
				
			
		}
	}

	
	
	public static void main (String [] args)
	{
		JFrame frame = new JFrame ("Timer");
		SwingTimer timer = new SwingTimer ();
		timer.timer.start();
		int current=-1;
		while(timer.time<30) {
			if (current!=timer.time) {
			System.out.println(timer.time);
			current=timer.time;
			}
		}

	} // main method
} // SwingTimer class


