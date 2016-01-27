import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;

public class Settings 
{	
	// Screen
	public static final int SCREEN_HEIGHT = 700;
	public static final int SCREEN_WIDTH = 1000;

	// Images
	public static final Image BG_IMG = Toolkit.getDefaultToolkit().getImage((new File(".")).getAbsolutePath() + "//beach_background.jpg");
	public static final Image LIFE_IMG = Toolkit.getDefaultToolkit().getImage((new File(".")).getAbsolutePath() + "//life.png");
	public static final Image MINE_IMG = Toolkit.getDefaultToolkit().getImage((new File(".")).getAbsolutePath() + "//mine.png");
	public static final Image INCREASE_BONUS_IMG = Toolkit.getDefaultToolkit().getImage((new File(".")).getAbsolutePath() + "//increase_bonus.png");
	public static final Image DECREASE_BONUS_IMG = Toolkit.getDefaultToolkit().getImage((new File(".")).getAbsolutePath() + "//decrease_bonus.png");
	public static final Image RACKET_IMG = Toolkit.getDefaultToolkit().getImage((new File(".")).getAbsolutePath() + "//racket.png");
	public static final Image BALL_IMG = Toolkit.getDefaultToolkit().getImage((new File(".")).getAbsolutePath() + "//ball.png");
	public static final Image LIFE_BONUS_IMG = Toolkit.getDefaultToolkit().getImage((new File(".")).getAbsolutePath() + "//life_bonus.png");
	public static final Image BULLET_IMG = Toolkit.getDefaultToolkit().getImage((new File(".")).getAbsolutePath() + "//bullet.png");
	public static final Image BULLET_BONUS_IMG = Toolkit.getDefaultToolkit().getImage((new File(".")).getAbsolutePath() + "//bullet_bonus.png");
	public static final Image BALL_BONUS_IMG = Toolkit.getDefaultToolkit().getImage((new File(".")).getAbsolutePath() + "//ball_bonus.png");
	
	// Game Engine
	public static final int START_NUM_OF_LIVES = 3;
	public static final int MAX_NUM_OF_LIVES = 6;
	public static final int SHOT_THRESHOLD = 150;
	public static final int TOTAL_SHOTTING_TIME = 5000;

	// Racket
	public static final int RACKET_START_LOCATION_X = (SCREEN_WIDTH/2) - 5;
	public static final int RACKET_START_LOCATION_Y = SCREEN_HEIGHT - 100;
	public static final int RACKET_WIDTH = 100 ;
	public static final int RACKET_MAX_WIDTH = 200;
	public static final int RACKET_MIN_WIDTH = 60;
	public static final int RACKET_HEIGHT = 15;

	// Ball
	public static final int BALL_START_LOCATION_X = (SCREEN_WIDTH/2) + 40;
	public static final int BALL_START_LOCATION_Y = SCREEN_HEIGHT - 120;
	public static final int BALL_SIZE = 20;
	public static final int MAX_NUM_OF_BALLS = 5;

	// Brick
	public static final int BRICK_HEIGHT = 25;
	public static final int BRICK_WIDTH = 55;
	
	// Bonus
	public static final int BONUS_SIZE = 50;
	public static final int BONUS_BOUNCES = 25;
	
	// Bullet
	public static final int BULLET_SIZE = 10;
	public static final int MAX_NUM_OF_SHOTS = 15;
	public static final int BULLET_START_LOCATION_Y = RACKET_START_LOCATION_Y - 10;
	public static final Color BULLET_COLOR = Color.RED;

	// Strength colors
	private static Color maroon = new Color(142, 0, 0);
	private static Color orange = new Color(255,154,0);
	private static Color purple = new Color(159, 0, 216);
	public static final Color STRENGTH_4 = maroon;
	public static final Color STRENGTH_3 = orange;
	public static final Color STRENGTH_2 = purple;
	public static final Color STRENGTH_1 = Color.GREEN;

	// Level
	public static BufferedImage level = null;

	public static LinkedList<Brick> initializeLevel(int lvlNum)
	{
		LinkedList<Brick> bricks = new LinkedList<Brick>();
		
		switch(lvlNum)
		{
			
			case 1:
				 int locX = 200;
				 int locY = 75;
				 int strength = 4;

				for(int i=0; i<5; i++)
				{
					for(int j=0; j<10; j++)
					{
						Brick b = new Brick(strength,locX,locY);
						bricks.add(b);
						locX += 60;
					}
					strength--;
					locX = 200;
					locY +=30;
				}
				break;
			case 2:
				locX = 200;
				locY = 75;
				strength = 4;
				
				for(int i=0; i<10; i++)
				{
					Brick b = new Brick(strength, locX, locY);
					bricks.add(b);
					locX+=60;
				}
				
				bricks.add(new Brick(strength,200,105));
				bricks.add(new Brick(strength,740,105));
				bricks.add(new Brick(strength,200,135));
				bricks.add(new Brick(strength,740,135));
				bricks.add(new Brick(strength,200,165));
				
				locX = 320;
				locY = 165;
				for(int i = 0; i< 8; i++)
				{
					Brick b = new Brick(strength, locX, locY);
					bricks.add(b);
					locX+=60;
				}
				
				bricks.add(new Brick(strength,200,195));
				bricks.add(new Brick(strength,740,195));
				bricks.add(new Brick(strength,200,225));
				bricks.add(new Brick(strength,740,225));
				bricks.add(new Brick(strength,740,255));
				
				locX = 200;
				locY = 255;
				for(int i = 0; i< 8; i++)
				{
					Brick b = new Brick(strength, locX, locY);
					bricks.add(b);
					locX+=60;
				}
				break;
				
			case 3:
				locX = 200;
				locY = 75;
				strength = 4;
				int inALine = 10, startInALineX = 1, startInALineY = 6;
				
				for(int i=0; i<3; i++)
				{
					for(int j=0; j<inALine; j++)
					{
						Brick b1 = new Brick(strength,locX,locY);
						Brick b2 = new Brick(strength,locX,locY+ 30*startInALineY);
						bricks.add(b1);
						bricks.add(b2);
						locX += 60;
					}
					
					inALine = inALine -2;
					strength--;
					locX = 200 + startInALineX*60;
					startInALineX++;
					startInALineY-=2;
					locY +=30;
				}
				break;
			case 4:
				locX = 200; 
				locY = 75;
				strength = 2;
				
				for(int j=0; j<10; j++)
				{
					Brick b1 = new Brick(strength,locX,locY);
					Brick b2 = new Brick(strength,locX,locY+180);
					bricks.add(b1);
					bricks.add(b2);
					locX += 60;
				}
				
				locX = 200;
				locY = 105;
				strength--;
				for (int i=0; i<5; i++)
				{
					Brick b1 = new Brick(strength,locX,locY);
					Brick b2 = new Brick(strength,locX+540,locY);
					bricks.add(b1);
					bricks.add(b2);
					locY +=30;
				}
				
				locX = 320;
				locY = 135;
				strength+=2;
				for (int i=0; i<3; i++)
				{
					for (int j=0; j<2; j++)
					{
						Brick b1 = new Brick(strength,locX,locY);
						Brick b2 = new Brick(strength,locX+240,locY);
						bricks.add(b1);
						bricks.add(b2);
						locX += 60;
					}
					locX = 320;
					locY +=30;
						
				}
				break;
			case 5:
				 locX = 185;
				 locY = 75;
				 strength = 4;
				 int LocateOrNot = 0;

				for(int i=0; i<5; i++)
				{
					for(int j=0; j<11; j++)
					{
						if (j%2!=LocateOrNot)
						{
							locX += 60;
							continue;
						}
						Brick b = new Brick(strength,locX,locY);
						bricks.add(b);
						locX += 60;
					}
					if (LocateOrNot==0)
						LocateOrNot=1;
					else
						LocateOrNot=0;
					locX = 185;
					locY +=30;
				}
				break;
		}

		return bricks;
	}
	
	public static BufferedImage createLevel(LinkedList<Brick> bricks)
	{
		if(level != null)
			level = null;

		level = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.OPAQUE);

		Graphics2D dbg;
		dbg = level.createGraphics();
		dbg.drawImage(BG_IMG, 0, 0, 1000, 700, null);
		
		for(Brick b: bricks)
			b.drawBrick(dbg);
		
		return level;
	}
	
	public static void preferencesPage(BufferedImage b)
	{
		Graphics2D dbg;
		dbg = b.createGraphics();
		
		dbg.setColor(Color.WHITE);
		dbg.setFont(new Font("David", Font.PLAIN, 65));
		dbg.drawString("BRICK SMASHER", SCREEN_WIDTH/2 - 270, 100);
		
		dbg.setFont(new Font("Arial", Font.PLAIN, 26));
		dbg.drawString("Use the ball and the racket to smash all the bricks.", 100 , 150);
		dbg.drawString("When you smash a brick, you might get one of those:", 100 , 180);
		
		dbg.setFont(new Font("Arial", Font.PLAIN, 20));
		dbg.drawImage(Settings.LIFE_BONUS_IMG, 120, 200, 35, 35, null);
		dbg.drawString(" - Life bonus (6 at most) - for each life point you have you gain 300 points", 160 , 225);
		dbg.drawString("   at the end of each level.", 160 , 245);
		dbg.drawImage(Settings.MINE_IMG, 120, 250, 35, 35, null);
		dbg.drawString(" - Mine, don't touch those!", 160 , 275);
		dbg.drawImage(Settings.INCREASE_BONUS_IMG, 120, 300, 35, 35, null);
		dbg.drawString(" - Increase the racket for 25 bounces (on racket, on wall, on brick - any bounce count).", 160 , 325);
		dbg.drawImage(Settings.DECREASE_BONUS_IMG, 120, 350, 35, 35, null);
		dbg.drawString(" - Decrease the racket for 25 bounces (on racket, on wall, on brick - any bounce count).", 160 , 375);
		dbg.drawImage(Settings.BULLET_BONUS_IMG, 120, 400, 35, 35, null);
		dbg.drawString(" - Bullets!! for few seconds you may shoot so - SHOOT THEM ALL!!.", 160 , 425);
		dbg.drawImage(Settings.BALL_BONUS_IMG, 120, 450, 35, 35, null);
		dbg.drawString(" - Extra ball - for each ball you'll have at the end of each level you gain 100 points.", 160 , 475);
		
		dbg.setFont(new Font("Arial", Font.PLAIN, 21));
		dbg.drawString("There are 5 levels.", 100 , 520);
		dbg.drawString("To Eject new ball press the Up key. To move right and left use the Right and Left keys.", 100 , 550);
		dbg.drawString("To shoot bullets press the Space key.", 100 , 580);
		dbg.drawString("You may pause the game any time by pressing \"Enter\". ", 100 , 610);
		dbg.drawString("Good Luck!! ", 800 ,600);
		
		dbg.setFont(new Font("Arial", Font.PLAIN, 18));
		dbg.drawString("press \"Enter\" to start...", 765 , 630);
		
	}

}
