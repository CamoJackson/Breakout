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
   private static final int NBRICKS_PER_ROW = 3;  // number of bricks per row
   private static final int NBRICK_ROWS = 10;      // number of rows of bricks
   private static final int BRICK_SEP = 4;         // separation between bricks both horizontally and vertically
   private static final int BRICK_WIDTH = WIDTH / NBRICKS_PER_ROW - BRICK_SEP;   // width of each brick (based on the display dimensions)
   private static final int BRICK_HEIGHT = 8;      // height of brick
   private static final int BRICK_Y_OFFSET = 70;   // offset of the top brick row from top

	// Ball settings
   private static final int BALL_RADIUS = 6;       // radius of ball in pixel
   
   // Game settings
   private static final int NPOWERUPS_MAX = 20;
   private static final int INTILIVES = 5;
   
	// Game Play variables                       
   private double dx, dy,origanlDX;                          // ball displacement in both directions (x-direction, and y-direction)
   private int lives = INTILIVES;                              // number of lives left in game
   private int nBricks = NBRICKS_PER_ROW * NBRICK_ROWS;  // total number of bricks on game board at start of game
   private int score,numPowerups,nPowerup = 0;                             // number of points scored
   
   // Label variables
   private GRect paddle;
   private GOval ball;
   private boolean gameRun = false, speedIncrece = false,sidewaysIncrece = false;
   private GLabel startLabel,gameOver,lblLives,lblScore,gameWin,powerupIDer;
   private GImage[] imgLives;
   private powerupPebble[] pebbels;
   private SwingTimer timer,speedTimer,paddleTimer;
   private AudioClip bounceClip;
      
   public static void main(String[] args){       // main method -- called when the program is run
      String[] sizeArgs = { "width=" + WIDTH, "height=" + HEIGHT };
      new Breakout_Shell().start(sizeArgs);
   }
	
   public void init(){                          	// init method -- automatically called on startup
      GImage background = new GImage("background.jpg",0,0);
      background.scale(2,3);
      add(background);
      background.sendToBack();      
      setTitle("Breakout By Jackson B");
      createBricks();                           // create the bricks
      createPaddle();                           // create the paddle
      createBall();                             // create the ball
      createLives();
      
      startLabel = new GLabel("Press Space to Start!", 30, HEIGHT/4*3);
      startLabel.setColor(Color.BLACK);
      gameOver = new GLabel("Game Over",30, HEIGHT/2);
      gameOver.setFont(new Font("Algerian", Font.BOLD, 50));
      gameOver.setColor(Color.RED);
      gameWin = new GLabel("You Win!",30, HEIGHT/2 + 50);
      gameWin.setFont(new Font("Algerian", Font.BOLD, 50));
      gameWin.setColor(Color.RED);
      lblScore = new GLabel("score: 0", 20, 20);
      lblScore.setColor(Color.BLACK);
      add(lblScore);
      lblLives = new GLabel("lives: ",WIDTH - 100, 20);
      lblLives.setColor(Color.BLACK);
      add(lblLives);
      powerupIDer = new GLabel("",20,HEIGHT - 50);
      powerupIDer.setColor(Color.GREEN.darker());
      add(powerupIDer);
      
      pebbels = new powerupPebble[NPOWERUPS_MAX];
      bounceClip = MediaTools.loadAudioClip("bounce.wav");
      
      timer = new SwingTimer(1000,new Listener());
      timer.setRepeats(false);
      speedTimer = new SwingTimer(5000,new Listener());
      speedTimer.setRepeats(false);
      paddleTimer = new SwingTimer(10000,new Listener());
      paddleTimer.setRepeats(false);
      
      addMouseListeners();
      addKeyListeners();                                    
   }
   
   public void run(){                           
      startTheBall();
      playBall();
   }
	
   public void createLives(){
      int c = 0;
      int L = 1;
      imgLives = new GImage[42];
      for(int k=0; k<imgLives.length;k++){
         imgLives[k] = new GImage("Heart.png",WIDTH - 70 + (10 * c) ,10 * L);
         c++;
         if(c >= 7){
            c = 0;
            L++;
         }
      }
      for(int k=0;k<lives;k++){
         add(imgLives[k]);
      }
   }
   	
   public void createBricks(){
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
                  brick.setPoints(25);
                  break;
               case 2:
               case 3:
                  brick.setFillColor(Color.ORANGE);
                  brick.setColor(Color.ORANGE);
                  brick.setPoints(20);
                  break;
               case 4:
               case 5:
                  brick.setFillColor(Color.YELLOW);
                  brick.setColor(Color.YELLOW);
                  brick.setPoints(15);
                  break;
               case 6:
               case 7:
                  brick.setFillColor(Color.GREEN);
                  brick.setColor(Color.GREEN);
                  brick.setPoints(10);                  
                  break;
               case 8:
               case 9:
                  brick.setFillColor(Color.CYAN);
                  brick.setColor(Color.CYAN);
                  brick.setPoints(5);
                  break;
            }
            add(brick); 
         }
      }
   }

   class Brick extends GRect{                   // Brick class
      private int myPoints;
      private int myHealth;
      private boolean isPowerup = false;
      
      public Brick(double w, double h){
         super(w,h);
         if(Math.random() < 0.94 && numPowerups <= NPOWERUPS_MAX){
            numPowerups++;
            isPowerup = true;
            myHealth = 1;
         }
         else{
            if(Math.random() < 0.85){
               myHealth = 1;
            }
            else{
               myHealth = 2;
            }
         }
      }
      public Brick(double x, double y, double w, double h){
         super(x,y,w,h);
         if(Math.random() < 0.94 && numPowerups <= NPOWERUPS_MAX){
            numPowerups++;
            isPowerup = true;
            myHealth = 1;
         }
         else{
            if(Math.random() < 0.85){
               myHealth = 1;
            }
            else{
               myHealth = 2;
            }
         }        
      }
      public void setPoints(int aPointValue){
         myPoints = aPointValue;
      }
      public int getPoints(){
         return myPoints;
      }
      public void destroy(Brick name){
         myHealth--;
         setFillColor(getFillColor().darker());
         if(isPowerup){
            remove(name);  
         }
         else{
            if(myHealth <= 0){
               remove(name);
            }
         }
      }
      public boolean checkPowerup(){
         return isPowerup;
      }
   }
   class powerupPebble extends GOval{
      private int dy = 1;
      public powerupPebble(double x,double y){
         super(x,y,5,5);
         setColor(Color.GREEN.darker());
         setFillColor(Color.GREEN.darker());
         setFilled(true);
         sendToFront();
      }
      public void move(powerupPebble name){
         setLocation(getX(),getY() + dy);
         if(getY() >= HEIGHT){
            remove(name);
         }
      }
      public void pickup(powerupPebble name){
         double chance = Math.random();
         if(chance <= 0.10){
            powerupIDer.setLabel("Your Score Has Been Doubled!");
            score = score * 2;
         }
         else if(chance <= 0.30){
            powerupIDer.setLabel("Your have gained an extra life!");
            if(lives < 42){
               lives++;
               add(imgLives[lives - 1]);          
            }
         }
         else if(chance <= 0.40 && speedIncrece == false){
            powerupIDer.setLabel("Your Speed has incressed for 5 second");
            dx = dx * 2;
            dy = dy * 2;
            speedTimer.start();
            speedIncrece = true;
         }
         else if(chance <= 0.60){
            powerupIDer.setLabel("Nope");
         }
         else if(chance <= 0.61){
            powerupIDer.setLabel("GAME OVER you lost everything");
            lives = 0;
            for(int k=41;k>=0;k--){
               remove(imgLives[k]);
            }
         }
         else if(chance <= 0.90){
            powerupIDer.setLabel("Paddle Size Increced for 10 Seconds");
            paddle.setSize(PADDLE_WIDTH * 2,PADDLE_HEIGHT);
            paddleTimer.start();
         }
         else if(chance <=1){
            powerupIDer.setLabel("Points Randomised");
            score = score + (int) (Math.random() * 1000);
         }
         timer.start();
         remove(name);
         setLocation(0,HEIGHT);
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
      dx = Math.random() * -1;
      dy = -1.5;
      origanlDX = dx;   	
   }
	
   public void playBall(){           	      // playBall method -- called from the run method
      while( lives > 0 ){                     // game loop - play continues as long as player has lives left
         lblScore.setLabel("Score: " + score);
         if(gameRun){    
            
         
         //move the ball
            ball.move(dx, dy);
         //pause
            pause(4);
         
         //check for contact along the outer walls
            if(ball.getX() <= 0 || ball.getX() >= WIDTH - BALL_RADIUS * 2){
               dx = -dx;
               bounceClip.play();
            }
            if(ball.getY() <= 0){
               dy = -dy;
               bounceClip.play();
            }
            if(ball.getY() >= HEIGHT - PADDLE_Y_OFFSET){
               lives = lives - 1;
               gameRun = false;
               paddle.setLocation((WIDTH / 2) - (PADDLE_WIDTH / 2), HEIGHT - PADDLE_Y_OFFSET);
               ball.setLocation(paddle.getX() + (PADDLE_WIDTH / 2), paddle.getY() - (BALL_RADIUS * 2) - 5);
               if(dy < 0)
                  dy = -dy;
               remove(imgLives[lives]);
            }
         
         //check for collisions with bricks or paddle
            GObject collider = getCollidingBall();
         
         //if the ball collided with the paddle 
            if(collider == paddle)
            {
            //reverse the y velocity
               if(ball.getX() <= paddle.getWidth() / 0.25 + paddle.getX() && !sidewaysIncrece){
                  dx = dx * 1.2;
                  sidewaysIncrece = true;
               }
               else if(ball.getX() >= paddle.getWidth() / 0.75 + paddle.getX() && !sidewaysIncrece){
                  dx = dx * 1.2;
                  sidewaysIncrece = true;
               }
               else{
                  dx = origanlDX;
                  sidewaysIncrece = false;
               }
               dy = -dy;
            }
            //otherwise if the ball collided with a brick
            else if(collider instanceof Brick){
               Brick brick = (Brick)collider;
               score = score + brick.getPoints();
               if(brick.checkPowerup() && nPowerup != NPOWERUPS_MAX){
                  pebbels[nPowerup] = new powerupPebble(brick.getX(),brick.getY());
                  add(pebbels[nPowerup]);
                  nPowerup++;
               }
            //reverse y velocity
             
               dy = -dy;
               bounceClip.play();
            //remove the brick
               brick.destroy(brick);
               nBricks--;
            }
            if(nBricks <= 0){ //checks for win
               gameRun = false;
               lives = 0;
               add(gameOver);// displays mesage
               add(gameWin);
               remove(ball);
            }
            for(int k=0;k<=nPowerup-1; k++){
               if(pebbels[k] != null){
                  pebbels[k].move(pebbels[k]);
                  if(getElementAt(pebbels[k].getX(), pebbels[k].getY()) == paddle){
                     pebbels[k].pickup(pebbels[k]);
                  }
               }
            }
         }            
         else{
            add(startLabel); 
         }
      }
      add(gameOver);
      gameRun = false;
   }	
   private GObject getCollidingBall(){            // getCollidingObject -- called from the playBall method
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
         paddle.setLocation(e.getX()-(paddle.getWidth() /2),HEIGHT - PADDLE_Y_OFFSET); //gets the mouses x position
   }
   public void keyPressed(KeyEvent e){
      if(e.getKeyCode() == KeyEvent.VK_SPACE && !gameRun & lives > 0){
         gameRun = true;
         if(startLabel != null)
            remove(startLabel);
      }
      else if(e.getKeyCode() == KeyEvent.VK_P){
         if(gameRun)
            gameRun = false;
         else
            gameRun = true;
      }
   }
   private class Listener implements ActionListener{
      public void actionPerformed(ActionEvent e){
         if(e.getSource() == timer){
            powerupIDer.setLabel("");
         }
         else if(e.getSource() == speedTimer){
            dx = dx * 0.5;
            dy = dy * 0.5;
            speedIncrece = false;
         }
         else if(e.getSource() == paddleTimer){
            paddle.setSize(PADDLE_WIDTH,PADDLE_HEIGHT);
         }
      }
   }
}