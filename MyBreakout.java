/*
 * MyBreakout.java
 * Written by: Tim Lobes
 * My take on assignment #3 for the CS106A class at Stanford for Spring 2013
 * 
 * http://www.stanford.edu/class/cs106a/
 */
 
import acm.graphics.*;
import acm.program.*;
import acm.util.*;
 
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
 
public class MyBreakout extends GraphicsProgram {
 
/** Width and height of application window in pixels */
  public static final int APPLICATION_WIDTH = 400;
  public static final int APPLICATION_HEIGHT = 600;
 
/** Dimensions of game board */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;
 
/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;
 
/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;
 
/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 4;
 
/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 2;
 
/** Separation between bricks */
	private static final int BRICK_SEP = 3;
 
/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;
	
/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;
 
/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;
 
/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;
 
/** Number of turns */
	private static final int NTURNS = 3;
 
/** Rows per sets of color */
	private static final int ROWS_PER_COLOR = 2;
	
/** Brick colors array */
	private static final Color[] COLORS =
		{Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN};
	
/** Initial ball Y velocity */
	private static final double VELOCITY_Y = 3.0;
	
/** Ball velocities */	
	private double vX;
	private double vY;
	
/** Initialize random number generation */	
	private RandomGenerator rgen = RandomGenerator.getInstance();
	
/** Frame delay for ball animation */	
	private static final int DELAY = 10;
	
	
/** Runs the Breakout program. */
	public void run() {
		removeAll(); // Ensure the board is empty
		
		brickCounter = NBRICKS_PER_ROW * NBRICK_ROWS;
		
		// Loop for as many turns as we have defined for the player
		for(int i = 0; i < NTURNS; i++) {
			
			// Creates the game objects
			setupGame();
			
			// Starts and loops the game
			startGame();
			
			// If at the end of the game the bricks are all gone, sweet.
			if(brickCounter == 0) {
				
				// Remove the ball
				ball.setVisible(false);
				endGameMessage(true); // Player wins!
				pause(DELAY * 300); // Let them read it
				run(); // Restart the game
			}
			
			// New round
			if(brickCounter > 0) {
				removeAll(); 
				brickCounter = NBRICKS_PER_ROW * NBRICK_ROWS;
			}
		}
		
		// If bricks remain and the turns are up
		if(brickCounter > 0)
		{
			endGameMessage(false); // Player loses :(
			pause(DELAY * 300); // Let them read it
			run(); // Restart the game
		}
	}
	
	
	/**
	 * Creates the essentials of the game
	 */
	private void setupGame() {
		createLevel();
		createPaddle();
		createBall();
	}
	
	
	/**
	 * Creates and draws a single brick at a specified coordinate (x,y)
	 * @param posX Horizontal position in the x direction
	 * @param posY Vertical position in the y direction
	 * @param colorNum Element number in the global COLORS array (0-4)
	 */
	private void drawBrick(int posX, int posY, int colorNum) {
		
		// Creates a new brick
		GRect rect = new GRect(posX, posY, BRICK_WIDTH, BRICK_HEIGHT);
		
		// Set it's color and fill using the defined colors
		rect.setColor(COLORS[colorNum]); 
		rect.setFillColor(COLORS[colorNum]);
		
		// Enable the fill
		rect.setFilled(true);
		
		// Add the brick
		add(rect);
	}
	
	
	/**
	 * Creates the rows of multicolored bricks
	 */
	private void createLevel() {
		
		// Find the x position where our centered row of bricks will start
		int start = (int) (getWidth() - ((BRICK_WIDTH * NBRICKS_PER_ROW) + (BRICK_SEP * (NBRICKS_PER_ROW - 1)))) / 2;
		
		for(int i = 0; i < NBRICK_ROWS; i++){
            for (int j = 0; j < NBRICKS_PER_ROW; j++) {
            	
				// Find the x and y coordinates for each brick based on the start position
		        int posX = start + (BRICK_WIDTH * j) + (BRICK_SEP * j);
		        int posY = BRICK_Y_OFFSET + (BRICK_HEIGHT * i) + (BRICK_SEP * i);
		        
		        // For each set of rows of bricks, assign an element number for the global color array
		        int colorNum = (int) Math.floor(i/ROWS_PER_COLOR);
		        
		        // Draw the brick based on color
		        drawBrick(posX, posY, colorNum);    
            }	
		}
	}
	
	
	/**
	 * Creates and centers the game paddle using as a GRect
	 */
	private void createPaddle() {
		
		// Center the paddle
		int x = (getWidth()/2) - (PADDLE_WIDTH/2); 
		
		// Figure out the start y position to draw the paddle based on our globals
		int y = getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT;
		
		// Create, color fill, and draw the paddle object
		paddle = new GRect (x, y, PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setColor(Color.BLACK);
		paddle.setFilled(true);
		add(paddle);
	}
	
	
	/**
	 * Creates the game ball
	 */
	private void createBall() {
		
		// Start the ball at the center of the application at it's midpoint
		double x = (getWidth()/2) - BALL_RADIUS;
		double y = (getHeight()/2) - BALL_RADIUS;
		
		// Create, color fill, and draw the ball object
		ball = new GOval(x, y, BALL_RADIUS * 2, BALL_RADIUS * 2);
		ball.setColor(Color.BLACK);
		ball.setFilled(true);
		add(ball);
	}
 
	
	/**
	 * Invoked when the mouse cursor is moved.
	 * Allows the mouse to control the paddle in the x direction from the paddle's center
	 * @param e Object representing the mouse
	 */
	public void mouseMoved(MouseEvent e) {		
		// Check to see if our paddle is within the bounds of the game board by taking into account the paddle's width
		// Set the new x position of the paddle while keeping the y the same
		if ( (e.getX() < (getWidth() - (PADDLE_WIDTH/2))) && (e.getX() > (PADDLE_WIDTH/2)) )
			paddle.setLocation(e.getX() - (PADDLE_WIDTH/2), getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT);
	}
	
	
	/**
	 * Set the initial ball velocity
	 */
	private void setBallVelocity() {
		vY = VELOCITY_Y;
		vX = rgen.nextDouble(1.0, 3.0);
		if (rgen.nextBoolean(0.5)) {
			vX = -vX; 
		}
	}
	
	
	/**
	 * Start the game and loops until the ball leaves the screen
	 */
	private void startGame() {
		
		// Create a welcome message
		GLabel label = new GLabel("Breakout! Click to play");
        label.setFont(new Font("Courier New", Font.BOLD, 24));
        
        // Center the text, set it's position, and add it
        double labelX = (getWidth() - label.getWidth() )/ 2;
        double labelY = (getHeight() - label.getHeight() ) / 2;
        label.setLocation(labelX, labelY - 50);
        add(label);
        
		// Initialize mouse 
		addMouseListeners();
        
		// Wait for the user to click to start the game
        // Remove the start game message upon the user clicking
		waitForClick();
		remove(label);
		
		// Start moving the ball
		setBallVelocity();
		
		// While the game's loop hasn't been broken
		while (true) {
			
			// New frame, move the ball
			moveBall();
			
			// If the ball goes beyond the lower bounds, stop
			if (ball.getY() >= getHeight()) {
				break;
			}
			
			// If all the bricks are gone, stop
			if(brickCounter == 0) {
				break; 
			}
		}
	}
	
	
	/**
	 * Moves the ball during the frame and checks for collisions
	 */
	private void moveBall() {
		
		// Move the ball for this frame
		ball.move(vX, vY);
		
		// Check to see if the ball hits on either side
		if ( (((ball.getX() - vX) <= 0) && vX < 0 ) || ((ball.getX() + vX) >= (getWidth() - BALL_RADIUS*2) && vX > 0) ) {
			vX = -vX;
		}
		// Check the ceiling too
		if ((ball.getY() - vY <= 0 && vY < 0 )) {
			vY = -vY;
		}
		
		// Grab the object that first collides with the ball if one exists
		GObject collider = getCollidingObject();
		
		// If the colliding object is the paddle
		if (collider == paddle) {
			// Check against it's position including current velocity as well to avoid it passing through
			if((ball.getY() >= getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT - (BALL_RADIUS * 2)) && 
					(ball.getY() < getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT - (BALL_RADIUS *2)  + VELOCITY_Y))
			{
				// If so, reverse it's y velocity if it hits
				vY = -vY; 
			}
		}
		
		// If the colliding object is a brick, remove it, decrement the counter, and reverse the y velocity
		else if (collider != null) {
			remove(collider); // Remove the element
			brickCounter--;
			vY = -vY;
		}
		
		// Pause a certain amount of time to keep the game playable by a human
		pause(DELAY);
	}
	
	
	/**
	 * Check around the ball's four sides and grab the element closest (and most forward drawn) that it touches
	 * @return The colliding object if one exists
	 */
	private GObject getCollidingObject() {
		
		// Check to see what is at the ball's bottom
		if((getElementAt(ball.getX(), ball.getY())) != null) {
	         return getElementAt(ball.getX(), ball.getY());
	      }
		// Top
		else if (getElementAt( (ball.getX() + BALL_RADIUS*2), ball.getY()) != null ){
	         return getElementAt(ball.getX() + BALL_RADIUS*2, ball.getY());
	      }
		// Left
		else if(getElementAt(ball.getX(), (ball.getY() + BALL_RADIUS*2)) != null ){
	         return getElementAt(ball.getX(), ball.getY() + BALL_RADIUS*2);
	      }
		// Right
		else if(getElementAt((ball.getX() + BALL_RADIUS*2), (ball.getY() + BALL_RADIUS*2)) != null ){
	         return getElementAt(ball.getX() + BALL_RADIUS*2, ball.getY() + BALL_RADIUS*2);
	      }
		// Otherwise, no collision so return nothing
		else{
	         return null;
	      }
		
	}
	
	
	/**
	 * Draw the appropriate end game message
	 * @param win true = win | false = lose 
	 */
	private void endGameMessage(boolean win) {
		GLabel label;
		
		if (win) {
			label = new GLabel ("The winner is you!", getWidth()/2, getHeight()/2);
			label.setColor(Color.BLUE);
		}
		else {
			label = new GLabel ("Dang.", getWidth()/2, getHeight()/2);
			label.setColor(Color.BLACK);
		}
			
		label.setFont(new Font("Courier New", Font.BOLD, 24));
		double labelX = (getWidth() - label.getWidth())/ 2;
        double labelY = (getHeight() - label.getHeight()) / 2;
        label.setLocation(labelX, labelY);
		add(label);
	}
	
	
	// Initialize the paddle object 
	private GRect paddle;
	
	// Initialize the ball object
	private GOval ball;
	
	// Initialize the total brick counter
	private int brickCounter;
}
