import java.awt.Color;
import java.awt.Graphics2D;

public class Racket
{
	private int pWidth = Settings.SCREEN_WIDTH - 15;
	private final int HEIGHT = Settings.RACKET_HEIGHT;
	private int width = Settings.RACKET_WIDTH;
	private int locX, locY;
	private int resizeBounces;
	private long startShottingTime;
	
	
	public Racket(int x, int y)
	{
		locX = x;
		locY = y;
		resizeBounces = Settings.BONUS_BOUNCES;
	}
	
	public void drawRacket(Graphics2D dbg)
	{
		dbg.drawImage(Settings.RACKET_IMG, locX, locY, width, HEIGHT, null);
	}
	
	public boolean moveRight()
	{
		if(locX + width + 10 <= pWidth)
		{
			locX += 10;
			return true;
		}
		else if(locX + width <= pWidth)
		{
			locX+=5;
			return true;
		}
		return false;
	}
	
	public boolean moveLeft()
	{
		if(locX - 10 >= 0)
		{
			locX -= 10;
			return true;
		}
		else if(locX >= 0)
		{
			locX-=5;
			return true;
		}
		return false;
	}
	
	public void bonusEffect(int bonusKind)
	{
		switch(bonusKind)
		{
			case Bonus.INCREASE_SIZE:
				if(width < Settings.RACKET_MAX_WIDTH)
					width += 50;
				resizeBounces = Settings.BONUS_BOUNCES;
				break;
				
			case Bonus.DECREASE_SIZE:
				if(width > Settings.RACKET_MIN_WIDTH)
					width -= 20;
				resizeBounces = Settings.BONUS_BOUNCES;
				break;
				
			case Bonus.BULLETS:
				startShottingTime = System.currentTimeMillis();
				break;
		}
	}
	
	public void resetWidth()
	{
		width = Settings.RACKET_WIDTH;
	}
	
	public boolean isBounced10()
	{
		if(resizeBounces == 0)
			return true;
		else
		{
			resizeBounces--;
			return false;
		}
	}
	
	public void setLocX(int x)
	{
		locX = x;
	}
	
	public void setLocY(int y)
	{
		locY = y;
	}
	
	public void setWidth(int width)
	{
		this.width = width;
	}
	
	public int getLocX()
	{
		return locX;
	}
	
	public int getLocY()
	{
		return locY;
	}
	
	public int getWdith()
	{
		return width;
	}
	
	public int getHeight()
	{
		return HEIGHT;
	}
	
	public boolean isArmed(long now)
	{
		if(startShottingTime == 0)
			return false;
		if(now - startShottingTime <= Settings.TOTAL_SHOTTING_TIME)
			return true;
		return false;
	}
}
