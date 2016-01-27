import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;
import java.util.Random;


public class GameEngine 
{
	private int numOfLives = Settings.START_NUM_OF_LIVES;
	private int height, width;
	private BufferedImage dbImg = null;
	private Image bgImage;
	private Image lifeImage;
	private long lastShootTime;
	private long lastPausedTime;
	private long lastBallEjected;

	private boolean win, lose, newLevel, isPaused, firstEnterPressed;
	private int score, level;
	private LinkedList<Brick> bricks;
	private LinkedList<Ball> balls;
	private LinkedList<Bonus> bonuses;
	private LinkedList<Bullet> bullets;
	private LinkedList<Bullet> deleteBullets;
	private LinkedList<Ball> deleteBalls;
	private Racket racket;

	public GameEngine(int pWidth, int pHeight)
	{
		width = pWidth;
		height = pHeight;
		bgImage = Settings.BG_IMG;
		lifeImage = Settings.LIFE_IMG;
		score = 0;
		level = 1;
		win = lose = isPaused = firstEnterPressed =  false;

		bricks = Settings.initializeLevel(level);
		racket = new Racket(Settings.RACKET_START_LOCATION_X, Settings.RACKET_START_LOCATION_Y);
		bonuses = new LinkedList<Bonus>();
		balls = new LinkedList<Ball>();
		bullets = new LinkedList<Bullet>();
		deleteBullets = new LinkedList<Bullet>();
		deleteBalls = new LinkedList<Ball>();
		balls.add(new Ball(Settings.BALL_START_LOCATION_X, Settings.BALL_START_LOCATION_Y, racket, 0, 0, this));

		lastShootTime = System.currentTimeMillis();
		lastBallEjected = System.currentTimeMillis();
		
		 String fileName = (new File(".")).getAbsolutePath() + "//game_music.wav";
         (new SoundThread(fileName, AudioPlayer.LOOP)).start();
	}

	public boolean isGameOver()
	{
		return win || lose;
	}

	public boolean isLevelOver()
	{
		if(bricks.isEmpty())
			return true;
		return false;
	}

	public BufferedImage drawGame()
	{
		Graphics2D dbg;

		dbImg = new BufferedImage(width, height, BufferedImage.OPAQUE);
		dbg = dbImg.createGraphics();
		dbg.setColor(Color.WHITE);
		dbg.fillRect(0, 0, width, height);
		dbg.drawImage(bgImage, 0, 0, Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT, null);

		if(!firstEnterPressed)
		{
			Settings.preferencesPage(dbImg);
			return dbImg;
		}
		
		// Draw bricks
		if(newLevel)
		{
			dbImg = Settings.createLevel(bricks);
			newLevel = false;
		}
		else
			for(Brick b : bricks)
				b.drawBrick(dbg);

		// Draw racket
		racket.drawRacket(dbg);

		// Draw bonuses
		for(Bonus b: bonuses)
			b.drawBonus(dbg, b.getBonusKind());

		// Draw balls
		for(Ball b : balls)
			b.drawBall(dbg);
		
		// Draw bullets
		for(Bullet b : bullets)
			b.drawBullet(dbg);

		// Draw Life icons
		int lifeLocX = 10;
		for(int i=0; i<numOfLives; i++)
		{
			dbg.drawImage(lifeImage, lifeLocX, 10, null);
			lifeLocX += 30;
		}

		// Draw score title
		dbg.setFont(new Font("Arial", Font.BOLD, 16));
		dbg.setColor(Color.WHITE);
		dbg.drawString("Score: " + score, width - 140, 30);
		
		if(isPaused)
		{
			pauseMessage(dbg);
		}

		if(win || lose)
		{
			gameOverMessage(dbg);
		}

		return dbImg;
	}

	public void updateGame()
	{
		if(isLevelOver() && level == 5)
		{
			win = true;
			for(int i=0; i<numOfLives; i++)
				score += 300;
			score += 100*balls.size();
		}
		
		else if(isLevelOver())
		{
			level++;
			bricks = Settings.initializeLevel(level);
			newLevel=true;
			if(!bullets.isEmpty())
				bullets.clear();
			for(int i=0; i<numOfLives; i++)
				score += 300;
			score += 100*balls.size();
			resetPosition();
		}

		Brick check = checkCollisions();
		if (check != null)
			doCollisionLogic(check);

		for(Bonus b : bonuses)
			b.updateSprite();
		
		removeBalls();

		for(Ball b : balls)
			b.updateSprite();
	
		removeBullets();
		
		for(Bullet b : bullets)
			b.updateSprite();
	}

	private Brick checkCollisions()
	{
		// Check collision between ball to brick
		for(Ball ball: balls)
		{
			Rectangle ballBox = new Rectangle(ball.getLocX(), ball.getLocY(), ball.getSize(), ball.getSize());
			for(Brick brick: bricks)
			{
				Rectangle brickBox = new Rectangle(brick.getLocX(), brick.getLocY(), Settings.BRICK_WIDTH, Settings.BRICK_HEIGHT);
				if(ballBox.intersects(brickBox))
					return brick;
			}
		}
		
		// Check collision between bullet to brick
		for(Bullet bullet: bullets)
		{
			Rectangle bulletBox = new Rectangle(bullet.getLocX(), bullet.getLocY(), bullet.getSize(), bullet.getSize());
			for(Brick brick: bricks)
			{
				Rectangle brickBox = new Rectangle(brick.getLocX(), brick.getLocY(), Settings.BRICK_WIDTH, Settings.BRICK_HEIGHT);
				if(bulletBox.intersects(brickBox))
				{
					bullet.isCollide();
					return brick;
				}
			}
		}
		
		
		// Check collision between racket to bonus
		Rectangle racketBox = new Rectangle(racket.getLocX(), racket.getLocY(), racket.getWdith(), racket.getHeight());
		LinkedList<Bonus> deleteBonuses = new LinkedList<Bonus>();
		for(Bonus bonus : bonuses)
		{
			Rectangle bonusBox = new Rectangle(bonus.getLocX(), bonus.getLocY(), 40 , 40);
			if(racketBox.intersects(bonusBox))
			{
				if(bonus.getBonusKind() >= 4)
					racket.bonusEffect(bonus.getBonusKind());
				else
				{
					if(bonus.getBonusKind() == Bonus.LIFE && numOfLives < Settings.MAX_NUM_OF_LIVES)
						numOfLives++;
					else if(bonus.getBonusKind() == Bonus.MINE && numOfLives >= 0)
					{
						String fileName = (new File(".")).getAbsolutePath() + "\\mine_explode.wav";
				        (new SoundThread(fileName, AudioPlayer.ONCE)).start();
				        
						numOfLives--;
						if(numOfLives < 0)
							lose = true;
						resetPosition();
					}
					else if(bonus.getBonusKind() == Bonus.BALL)
					{
						Ball oldBall = balls.getLast();
						addNewBall(oldBall);
					}
				}
				deleteBonuses.add(bonus);
			}
		}
		bonuses.removeAll(deleteBonuses);
		return null;
	}

	private void doCollisionLogic(Brick b)
	{
		if(b != null) // Collision with brick
		{
			if(b.getStrength() > 1)
				score += 10;
			else
				score += 100;
			
			if(b.getStrength()-1 > 0)
				b.setStrength(b.getStrength()-1);
			else
			{
				bricks.remove(b);
				if(b.isBonusBrick())
					giveBonus(b);
			}

			Rectangle brickBox = new Rectangle(b.getLocX(), b.getLocY(), Settings.BRICK_WIDTH, Settings.BRICK_HEIGHT);
			for(Ball ball : balls)
			{
				Rectangle ballBox =new Rectangle(ball.getLocX(), ball.getLocY(), ball.getSize(), ball.getSize());
				if(ballBox.intersects(brickBox))
					ball.bounceAtBrick(b);
			}
		}
	}


	private void gameOverMessage(Graphics g)
	{
		if (lose)
		{
			String fileName = (new File(".")).getAbsolutePath() + "\\losing_game.wav";
	        (new SoundThread(fileName, AudioPlayer.ONCE)).start();
			
			g.setFont(new Font("Arial", Font.PLAIN, 34));
			g.setColor(Color.RED);
			g.drawString("You Lose!", width/2 - 70, height/2);
			g.setFont(new Font("Arial", Font.PLAIN, 22));
			g.drawString("Your Final Score: " + score,width/2 - 76, height/2 + 40);
		}
		else
		{
			String fileName = (new File(".")).getAbsolutePath() + "\\winning_game.wav";
	        (new SoundThread(fileName, AudioPlayer.ONCE)).start();
			
			g.setFont(new Font("Arial", Font.PLAIN, 34));
			g.setColor(Color.GREEN);
			g.drawString("You Win!", width/2 - 70, height/2);
			g.setFont(new Font("Arial", Font.PLAIN, 22));
			g.drawString("Your Final Score: " + score,width/2 - 100, height/2 + 40);
		}
	}
	
	private void pauseMessage(Graphics g)
	{
		g.setFont(new Font("Arial", Font.PLAIN, 34));
		g.setColor(Color.WHITE);
		g.drawString("     pause", width/2 - 70, height/2);
		g.setFont(new Font("Arial", Font.PLAIN, 22));
		g.drawString("(Press enter to continue...)",width/2 - 100, height/2 + 40);
	}

	public void upKeyClicked()
	{
		long now = System.currentTimeMillis();
		for(Ball b : balls)
			if(b.getDY() == 0 && b.getDX() == 0)
			{
				if(now - lastBallEjected > 200)
				{
					b.ejectBall();
					lastBallEjected = now;
				}
				break;
			}
	}

	public void rightKeyClicked()
	{
		if(racket.moveRight())
			for(Ball ball : balls)
				if(ball.getDX() == 0 && ball.getDY() == 0)
					ball.setLocX(ball.getLocX()+10);
	}

	public void leftKeyClicked()
	{
		if(racket.moveLeft())
			for(Ball ball : balls)
				if(ball.getDX() == 0 && ball.getDY() == 0)
					ball.setLocX(ball.getLocX()-10);
	}

	public void spaceKeyClicked()
	{
		long now = System.currentTimeMillis();
		if(racket.isArmed(now))
		{
			if (now - lastShootTime > Settings.SHOT_THRESHOLD)
			{
				Bullet leftBullet = new Bullet(racket.getLocX(), Settings.BULLET_START_LOCATION_Y);
				Bullet rightBullet = new Bullet(racket.getLocX()+racket.getWdith()-15, Settings.BULLET_START_LOCATION_Y);
				bullets.add(leftBullet);
				bullets.add(rightBullet);
				lastShootTime = now;
				
	            String fileName = (new File(".")).getAbsolutePath() + "\\bullet_shoot.wav";
	            (new SoundThread(fileName, AudioPlayer.ONCE)).start();
			}
		}
	}
	
	public void enterKeyClicked()
	{
		long now = System.currentTimeMillis();
		if (now - lastPausedTime > Settings.SHOT_THRESHOLD)
		{
			if(!firstEnterPressed)
				firstEnterPressed = true;
			else
			{
				if(!isPaused)
					isPaused = true;
				else
					isPaused = false;
			}
			lastPausedTime = now;
		}
	}

	public void loseBall(Ball b)
	{
		b.isGone();
		
		if(numOfLives > 0 && balls.size()-1 == 0)
		{
			String fileName = (new File(".")).getAbsolutePath() + "\\lose_ball.wav";
			(new SoundThread(fileName, AudioPlayer.ONCE)).start();
		}
	}
	
	private void resetPosition()
	{
		while(!balls.isEmpty())
			balls.removeLast();
		
		racket.setLocX(Settings.RACKET_START_LOCATION_X);
		racket.resetWidth();
		Ball newBall = new Ball(Settings.BALL_START_LOCATION_X, Settings.BALL_START_LOCATION_Y, racket, 0, 0, this);
		balls.add(newBall);
	}

	private void giveBonus(Brick b)
	{
		Random rand = new Random();
		int randNum = rand.nextInt(7)+1;
		bonuses.add(new Bonus(randNum, b.getLocX()+(Settings.BRICK_WIDTH/2)-5, b.getLocY()));
	}
	
	private void removeBullets()
	{
		deleteBullets.clear();
		
		for(Bullet bullet : bullets)
			if(bullet.collided())
				deleteBullets.add(bullet);
		
		bullets.removeAll(deleteBullets);
	}
	
	private void removeBalls()
	{
		deleteBalls.clear();
		
		for(Ball ball : balls)
			if(ball.hasGone())
				deleteBalls.add(ball);
		
		balls.removeAll(deleteBalls);
		
		if(balls.isEmpty())
		{
			if(numOfLives > 0)
			{
				numOfLives--;
				resetPosition();
			}
			else
				lose = true;
		}
		
	}
	
	private void addNewBall(Ball old)
	{
		double randDX, randDY;
		double oldDY = old.getDY();
		double oldDX = old.getDX();
		int oldLocX = old.getLocX();
		int oldLocY = old.getLocY();
		
		if((oldDX ==0 ) && (oldDY == 0))
		{
			balls.add(new Ball(oldLocX, oldLocY, racket, oldDX, oldDY, this));
			return;
		}
		
		Random rand = new Random();
		do
		{
			randDX = rand.nextInt(5)+1;
			if(randDX < 3)
				randDX = 3;
			randDY = rand.nextInt(5)+1;
			if(randDY < 3)
				randDY = 3;
			if(oldDX < 0)
				randDX*=-1;
			randDY *= -1;
		}while((randDY == oldDY) || (randDX == oldDX));
		
		balls.add(new Ball(oldLocX, oldLocY, racket, randDX, randDY, this));
	}
	
	public boolean isPaused()
	{
		return isPaused;
	}
	
	public boolean isStarted()
	{
		return firstEnterPressed;
	}
}
