import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
// Jackson Borneman
public class Breakout_Shell extends GraphicsProgram 
{
  	// Display settings   
   private static final int WIDTH = 400;           // width of game display
   private static final int HEIGHT = 600;          // height of game display

	// Paddle settings
   private static final int PADDLE_WIDTH = 60;     // width of paddle
   private static final int PADDLE_HEIGHT = 10;    // height of paddle
   private static final int PADDLE_Y_OFFSET = 30;  // offset of paddle up from the bottom
	
  	// Brick settings
   private static final int NBRICKS_PER_ROW = 10;  // number of bricks per row
   private static final int NBRICK_ROWS = 8;      // number of rows of bricks
   private static final int BRICK_SEP = 4;         // separation between bricks both horizontally and vertically
   private static final int BRICK_WIDTH = WIDTH / NBRICKS_PER_ROW - BRICK_SEP;   // width of each brick (based on the display dimensions)
   private static final int BRICK_HEIGHT = 8;      // height of brick
   private static final int BRICK_Y_OFFSET = 70;   // offset of the top brick row from top

	// Ball settings
   private static final int BALL_RADIUS = 6;       // radius of ball in pixels
   
   // Game settings
   private static final int NTURNS = 3;            // number of turns
   private static final int NBRICK_COL = NBRICK_ROWS / 4; //number of brick rows that get the same color
   
	// Game Play variables
                              // paddle - based on ACM GRect object
                              // ball - based on ACM GOval object
   private double dx, dy;                          // ball displacement in both directions (x-direction, and y-direction)
   private int lives;                              // number of lives left in game
   private int nBricks = NBRICKS_PER_ROW * NBRICK_ROWS;  // total number of bricks on game board at start of game
   private int points;                             // number of points scored
   private int toggle = 0;                         // used for mouse events (only moves the paddle every 5th mouse move)
   
   // Label variables
	
   
   public static void main(String[] args)       // main method -- called when the program is run
   {
      String[] sizeArgs = { "width=" + WIDTH, "height=" + HEIGHT };
      new Breakout_Shell().start(sizeArgs);
   }
	
   public void init()                          	// init method -- automatically called on startup
   {
      createBricks();                           // create the bricks
      createPaddle();                           // create the paddle
      createBall();                             // create the ball
                                                // add a mouse listener
   }
   
   public void run()                            // run method -- automatically called after init
   {
      startTheBall();
      playBall();
   }
		
   public void createBricks()                   // createBricks method -- called from the init method
   {
      private int colornum = 0;
   	//make the bricks
      for(int r = 0; r < NBRICK_ROWS; r++)
      {
         for(int c = 0; c < NBRICKS_PER_ROW; c++)
         {
            Brick brick = new Brick(BRICK_SEP + c * (BRICK_WIDTH + BRICK_SEP), BRICK_Y_OFFSET + c*(BRICK_SEP + BRICK_HEIGHT), BRICK_WIDTH, BRICK_HEIGHT);
            switch(colornum){      
               case 1: //RGB 163 30 10  Red
               case 2: //RGB 193 132 10 Orange
               case 3: //RGB 10 135 51  Green
               case 4: //RGB 188 188 40 Yellow
            }
            if(r % 4 == NBRICK_COL)
               colorNum = colorNum + 1;
         
         	/********************************************
            * First, you need to enable the new brick
            * to be filled with color                      				  
            *
         	* Next, write code to correctly color the
            * bricks based on what row they are in
            *
            * Hint: use a switch statement which switches
            *       off of the row number 
         	*
            * Finally, you need to add the brick to the
            * canvas
            *
         	*********************************************/
         }
      }
   }

   class Brick extends GRect                    // Brick class -- class for all brick objects
   {
      // add instance variables here as needed
      
      /** Constructor: a new brick with width w and height h */
      public Brick(double w, double h)
      {
         // code to call the 2-arg constructor from the superclass
      }
    
      /** Constructor: a new brick at (x,y) with width w and height h */
      public Brick(double x, double y, double w, double h)
      {
         // code to call the 4-arg constructor from the superclass
      }
   
   }

   public void createPaddle()                   // createPaddle method -- called from the init method
   {
   	/******************************************
      *                       				  
   	* write code to create and add the paddle
   	*                      				  
   	*******************************************/
   }
	
   public void createBall()                     // createBall method -- called from the init method
   {
   	/******************************************
      *                       				  
   	* write code to create and add the ball 
   	*                      				  
   	*******************************************/
   }
		
   public void startTheBall()                   // startTheBall method -- called from the run method
   {
   	
   	
      /******************************************
      *                       				  
      * create a random double between 1 and 3
      * (between 1.0 and 2.99) for dx         
      *            				  
      * set initial y displacement, dy, to 3
      *
      * you may need to tweek these values to
      * optimize game playability
   	*                      				  
   	*******************************************/
         	
   }
	
   public void playBall()           	      // playBall method -- called from the run method
   {
      while( lives > 0 )                     // game loop - play continues as long as player has lives left
      {
      	//move the ball
         ball.move(dx, dy);
      	//pause
         pause(1);
      	
      	//check for contact along the outer walls
      	
      	/**********************************************
        	*
        	* if ball contacts the ceiling,	  
        	* reverse the y velocity 				      
        	*                      				      
        	* otherwise, if ball contacts the left wall, 
        	* reverse the x velocity 				      
        	*                      				      
        	* otherwise, if ball contacts the right wall,
        	* reverse the x velocity 				      
        	* 
         * otherwise, if ball gets past the paddle,
         * turn is over; either go on to next turn or
         * end game if there are no lives left 				      
         *           				      
        	**********************************************/
      
      	
      	//check for collisions with bricks or paddle
         GObject collider = getCollidingObject();
      	
      	//if the ball collided with the paddle 
         if(collider == paddle)
         {
         	//reverse the y velocity
            dy = -dy;
         }
         //otherwise if the ball collided with a brick
         else if(collider instanceof Brick) 
         {
         	//reverse y velocity
            dy = -dy;
         	//remove the brick
            remove(collider);
         }
      }
   }
	
   private GObject getCollidingObject()            // getCollidingObject -- called from the playBall method
   {  // discovers and returns the object that the ball collided with
   
      if(getElementAt(ball.getX(), ball.getY()) != null)
         return getElementAt(ball.getX(), ball.getY());
      else if(getElementAt(ball.getX()+BALL_RADIUS*2, ball.getY()) != null)
         return getElementAt(ball.getX()+BALL_RADIUS*2, ball.getY());
      else if(getElementAt(ball.getX()+BALL_RADIUS*2, ball.getY()+BALL_RADIUS*2) != null)
         return getElementAt(ball.getX()+BALL_RADIUS*2, ball.getY()+BALL_RADIUS*2);
      else if(getElementAt(ball.getX(), ball.getY()+BALL_RADIUS*2) != null)
         return getElementAt(ball.getX(), ball.getY()+BALL_RADIUS*2);
      else	
         return null;
   }
   
   public void mouseMoved(MouseEvent e)         // mouseMoved method -- called by the mouseListener
   {  // triggered whenever the mouse is moved anywhere within the boundaries of the run window
   
   	//only move the paddle every 5th mouse event, otherwise the play slows every time the mouse moves
      if(toggle == 5)
      {
      	/******************************************
         *                       				  
      	* write code to move the paddle so that
         * it lines up with the current position 
         * of the mouse 
         *
         * make sure the paddle stops at the left
         * and right walls
      	*                      				  
      	*******************************************/
      	
      	//reset toggle to 1 
         toggle = 1;
      }
      else
      {
      	//increment toggle by 1
      	//(when toggle gets to 5 the code will move the paddle 
      	// and reset toggle back to 1)
         toggle++;
      }
   }
}
