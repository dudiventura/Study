import java.awt.Graphics2D;
import java.util.Random;

public class Brick 
{
	private int height = Settings.BRICK_HEIGHT;
	private int width = Settings.BRICK_WIDTH;
	private int strength;
	private int locX, locY;
	boolean bonusBrick;
	
	public Brick(int strength, int x, int y)
	{
		this.strength = strength;
		locX = x;
		locY = y;
		decideIfBonus();
	}
	
	public void drawBrick(Graphics2D dbg)
	{
		switch(strength)
		{
		 	case 1:
		 		dbg.setColor(Settings.STRENGTH_1);
		 		break;
		 		
		 	case 2:
		 		dbg.setColor(Settings.STRENGTH_2);
		 		break;
		 		
		 	case 3:
		 		dbg.setColor(Settings.STRENGTH_3);
		 		break;
		 		
		 	case 4:
		 		dbg.setColor(Settings.STRENGTH_4);
		 		break;
		}
		dbg.fillRect(locX, locY, width, height);
	}
	
	public void setStrength(int s)
	{
		strength = s;
	}
	
	public void setLocX(int x)
	{
		locX = x;
	}
	
	public void setLocY(int y)
	{
		locY = y;
	}
	
	public int getStrength()
	{
		return strength;
	}
	
	public int getLocX()
	{
		return locX;
	}
	
	public int getLocY()
	{
		return locY;
	}
	
	private void decideIfBonus()
	{
		Random rand = new Random();
		int randNum = rand.nextInt(100);
		if(randNum <= 25)
			bonusBrick = true;
		else
			bonusBrick = false;
	}
	
	public boolean isBonusBrick()
	{
		return bonusBrick;
	}
}
