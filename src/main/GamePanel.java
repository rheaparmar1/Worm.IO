package main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import WormObj.Player;

public class GamePanel extends JPanel implements Runnable{
	
	MainFrame frame;
	static int width = 1400;
	static int height = 700;
	
	int FPS = 60;
	Thread gameThread;
	int playerX = 200;
	int playerY = 200;
	int playerSpeed = 5;
	
	
	public GamePanel(MainFrame frame) {
		this.frame = frame;

		setPreferredSize(new Dimension(width, height));
		setBackground(Color.black);
		setDoubleBuffered(true);
		setFocusable(true);
	}
	
	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	public void run() {
		double drawInterval = 1000000000/FPS; //0.01666 secs
		double nextDrawTime = System.nanoTime() + drawInterval;
		
		while(gameThread != null) {
			//System.out.println("running");
			
			update();
			
			repaint();
			
			try {
				double remainingTime = nextDrawTime - System.nanoTime();
				remainingTime = remainingTime/1000000;
				Thread.sleep((long) remainingTime);
				
				if(remainingTime < 0) {
					remainingTime = 0;
				}
				
				nextDrawTime+= drawInterval;
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void update() {
		playerX += playerSpeed;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		System.out.println("yes");
		g2.setColor(Color.white);
		g2.drawRect(playerX, playerY, 50, 50);
		g2.dispose();
	}

}
