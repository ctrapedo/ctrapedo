import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener{

	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/UNIT_SIZE;
	static int DELAY = 125;
	//arrays to hold coordinates for body parts
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 6;
	int applesEaten;
	int appleX;
	int appleY;
	char direction = 'R'; //'R' for right, 'L' for left, 'U' for up, 'D' for down
	boolean running = false;
	Timer timer;
	Random random;
	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	/*
	 * Starts the game 
	*/
	
	public void startGame() {
		
		newApple(); 
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	/*
	 * Draws the snake while running it true. When running is set to false it triggers the gameOver()
	 * method to end the game
	 */
	public void draw(Graphics g) {
		
		if(running) {
			//creates the apple
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
		
			//draw head and body
			for(int i = 0; i < bodyParts; i++) {
				g.setColor(new Color (random.nextInt(255), random.nextInt(255), random.nextInt(255)));
				g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
			}
			//shows the score
			g.setColor(Color.white); 
			g.setFont(new Font("Ink Free", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
		} else {
			gameOver(g);
		}
		
	}
	
	/*
	 * Generate coordinates of a new apple after the snake gets one
	 */
	public void newApple() {
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) *UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE)) *UNIT_SIZE;
	}
	
	/*
	 * Method that moves the snake and directions the locations of the following parts of the
	 * snake
	 */
	public void move() {
		for(int i = bodyParts; i > 0; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
	}
	
	/*
	 * If the snake head touches the apple, we get a point, the game speeds up and the
	 * snake gets bigger. It also resets the apple's locaiton
	 */
	public void checkApple() {
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			DELAY -= 7;
			newApple();
		}
	}
	
	/*
	 * Checks collisions - set off if the head touches a body part or in the snake passes the
	 * window's borders
	 */
	public void checkCollisions() {
		//head touches body
		for(int i = bodyParts; i > 0; i--) {
			if((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		
		//head passes window boundaries
		if(x[0] < 0) {
			running = false;
		}
		
		if(x[0] > SCREEN_WIDTH) {
			running = false;
		}
		
		if(y[0] < 0) {
			running = false;
		}
		
		if(y[0] > SCREEN_HEIGHT) {
			running = false;
		}
		
		if(!running) {
			timer.stop();
		}
	}
	
	/*
	 * This is the screen that is shown when the player loses. Presents the "Game Over" message
	 * and final score
	 */
	public void gameOver(Graphics g) {
		
		//game over text
		g.setColor(Color.red); 
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics = getFontMetrics(g.getFont());
		
		//final score text
		g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
		g.setColor(Color.white); 
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		metrics = getFontMetrics(g.getFont());
		g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());

	}
	
	/*
	 * This is a continuous loop while the boolean of the game is running. It also resets the game 
	 * after the player loses. It runs the method to move, check for points and check for a potential
	 * loss.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}
	
	public class MyKeyAdapter extends KeyAdapter {
		
		/*
		 * Allows the computer to take arrow keys as directions be storing it
		 * in a char variable directions. The variable also allows the program to 
		 * protect the user from making a 180º turn
		 */
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			}
		}
	}

}
