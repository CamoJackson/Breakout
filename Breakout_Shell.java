import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import javax.swing.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
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
   private static final int NBRICK_ROWS = 10;      // number of rows of bricks
   private static final int BRICK_SEP = 4;         // separation between bricks both horizontally and vertically
   private static final int BRICK_WIDTH = WIDTH / NBRICKS_PER_ROW - BRICK_SEP;   // width of each brick (based on the display dimensions)
   private static final int BRICK_HEIGHT = 8;      // height of brick
   private static final int BRICK_Y_OFFSET = 70;   // offset of the top brick row from top

	// Ball settings
   private static final int BALL_RADIUS = 6;       // radius of ball in pixel
   
   // Game settings
   private static final int NTURNS = 3;            // number of turns
   
	// Game Play variables
                              // paddle - based on ACM GRect object
                              // ball - based on ACM GOval object
   private double dx, dy;                          // ball displacement in both directions (x-direction, and y-direction)
   private int lives = 3;                              // number of lives left in game
   private int nBricks = NBRICKS_PER_ROW * NBRICK_ROWS;  // total number of bricks on game board at start of game
   private int points;                             // number of points scored
   private int toggle = 0;                         // used for mouse events (only moves the paddle every 5th mouse move)
   
   // Label variables
   private GRect paddle;
   private GOval ball;
   private boolean gameRun = false;
   private GLabel startLabel,gameOver;
   private Font Algerian = new Font("Algerian", BOLD, 50);
	
   
   public static void main(String[] args){       // main method -- called when the program is run
      String[] sizeArgs = { "width=" + WIDTH, "height=" + HEIGHT };
      new Breakout_Shell().start(sizeArgs);
   }
	
   public void init(){                          	// init method -- automatically called on startup
      createBricks();                           // create the bricks
      createPaddle();                           // create the paddle
      createBall();                             // create the ball
      
      startLabel = new GLabel("Press Space to Start!", 30, HEIGHT/4*3);
      startLabel.setColor(Color.BLACK);
      gameOver = new GLabel("Game Over",30, HEIGHT/2);
      gameOver.setFont(Algerian);
      gameOver.setColor(Color.RED);
      
      addMouseListeners();
      addKeyListeners();                                         // add a mouse listener
   }
   
   public void run(){                            // run method -- automatically called after init
      startTheBall();
      playBall();
   }
		
   public void createBricks()                   // createBricks method -- called from the init method
   {
   	//make the bricks
      for(int r = 0; r < NBRICK_ROWS; r++){
         for(int c = 0; c < NBRICKS_PER_ROW; c++){
            Brick brick = new Brick(BRICK_SEP + c * (BRICK_WIDTH + BRICK_SEP), BRICK_Y_OFFSET + r * (BRICK_SEP + BRICK_HEIGHT), BRICK_WIDTH, BRICK_HEIGHT);
               
            brick.setFilled(true);
            switch(r){      
               case 0:
               case 1:
                  brick.setFillColor(Color.RED);
                  brick.setColor(Color.RED);
                  break;
               case 2:
               case 3:
                  brick.setFillColor(Color.ORANGE);
                  brick.setColor(Color.ORANGE);
                  break;
               case 4:
               case 5:
                  brick.setFillColor(Color.YELLOW);
                  brick.setColor(Color.YELLOW);
                  break;
               case 6:
               case 7:
                  brick.setFillColor(Color.GREEN);
                  brick.setColor(Color.GREEN);
                  break;
               case 8:
               case 9:
                  brick.setFillColor(Color.CYAN);
                  brick.setColor(Color.CYAN);
                  break;
            }
            add(brick); 
         }
      }
   }

   class Brick extends GRect{                   // Brick class -- class for all brick objects
      // add instance variables here as needed
      
      /** Constructor: a new brick with width w and height h */
      public Brick(double w, double h){
         super(w,h);
      }
    
      /** Constructor: a new brick at (x,y) with width w and height h */
      public Brick(double x, double y, double w, double h){
         super(x,y,w,h);
      }    
   }

   public void createPaddle(){                   // createPaddle method -- called from the init method
      paddle = new GRect((WIDTH / 2) - (PADDLE_WIDTH / 2), HEIGHT - PADDLE_Y_OFFSET, PADDLE_WIDTH, PADDLE_HEIGHT);
      paddle.setFilled(true);
      paddle.setFillColor(Color.MAGENTA);
      paddle.setColor(Color.BLACK);
      add(paddle);
   }
   public void createBall(){                    // createBall method -- called from the init method
      ball = new ball();
      ball.setFilled(true);
      ball.setFillColor(Color.BLACK);
      ball.setColor(Color.BLACK);
      add(ball);
   }
   class ball extends GOval{
      public ball(){
         super(paddle.getX() + (PADDLE_WIDTH / 2), paddle.getY() - (BALL_RADIUS * 2) - 5,BALL_RADIUS * 2, BALL_RADIUS * 2);
      }
      public void move(int x,int y){
         setLocation(getX() + x,getY() + y);
      }
   }
   public void startTheBall(){                   // startTheBall method -- called from the run method
   	
      dx = Math.random() * 1 + 1;
      dy = 2;
      
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
	
   public void playBall(){           	      // playBall method -- called from the run method
      while( lives > 0 ){                     // game loop - play continues as long as player has lives left
         if(gameRun){
         //move the ball
            ball.move(dx, dy);
         //pause
            pause(4);
         
         //check for contact along the outer walls
            if(ball.getX() <= 0 || ball.getX() >= WIDTH - BALL_RADIUS * 2){
               dx = -dx;
            }
            if(ball.getY() <= 0){
               dy = -dy;
            }
            if(ball.getY() >= HEIGHT - PADDLE_Y_OFFSET){
               lives = lives - 1;
               gameRun = false;
               paddle.setLocation((WIDTH / 2) - (PADDLE_WIDTH / 2), HEIGHT - PADDLE_Y_OFFSET);
               ball.setLocation(paddle.getX() + (PADDLE_WIDTH / 2), paddle.getY() - (BALL_RADIUS * 2) - 5);
            }
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
         else{
            add(startLabel); 
         }
      }
      add(gameOver);
      gameRun = false;
   }
	
   private GObject getCollidingObject(){            // getCollidingObject -- called from the playBall method
   // discovers and returns the object that the ball collided with
   
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
   
   public void mouseMoved(MouseEvent e){
      if(gameRun)
         paddle.setLocation(e.getX()-(PADDLE_WIDTH /2),HEIGHT - PADDLE_Y_OFFSET); //gets the mouses x position
   }
   public void keyPressed(KeyEvent e){
      if(e.getKeyCode() == KeyEvent.VK_SPACE && !gameRun & lives > 0){
         gameRun = true;
         if(startLabel != null)
            remove(startLabel);
      }
   }
}
