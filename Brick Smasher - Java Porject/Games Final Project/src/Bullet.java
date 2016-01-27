import java.awt.Color;
import java.awt.Graphics2D;


public class Bullet
{
	private final int size = Settings.BULLET_SIZE;
	private int locX, locY;
	private int dy;
	private boolean isCollide;
	
	public Bullet(int x, int y)
	{
		locX = x;
		locY = y;
		dy = -2;
		isCollide = false;
	}
	
	public void drawBullet(Graphics2D dbg)
	{
		//dbg.setColor(Settings.BULLET_COLOR);
		//dbg.fillOval(locX, locY, size, size);
		dbg.drawImage(Settings.BULLET_IMG, locX, locY, size, size, null);
	}
	
	public int getLocX()
	{
		return locX;
	}
	
	public int getLocY()
	{
		return locY;
	}
	
	public int getSize()
	{
		return size;
	}
	
	public void updateSprite()
	{
		locY += dy;
	}
	
	public void isCollide()
	{
		isCollide = true;
	}
	
	public boolean collided()
	{
		return isCollide;
	}
}
