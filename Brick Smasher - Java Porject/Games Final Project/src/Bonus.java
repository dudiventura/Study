import java.awt.Graphics2D;



public class Bonus 
{
	public static final int LIFE = 1, MINE = 2, BALL = 3, INCREASE_SIZE = 4, DECREASE_SIZE = 5, BULLETS = 6;
	private final int SIZE = Settings.BONUS_SIZE;
	private int bonusKind;
	private int locX, locY;
	private int dy;
	
	public Bonus(int kind, int x, int y)
	{
		bonusKind = kind;
		locX = x;
		locY = y;
		dy = 1;
	}
	
	public void drawBonus(Graphics2D dbg, int kind)
	{
		switch(kind)
		{
			case LIFE:
				dbg.drawImage(Settings.LIFE_BONUS_IMG, locX, locY, SIZE, SIZE, null);
				break;
				
			case MINE:
				dbg.drawImage(Settings.MINE_IMG, locX, locY, SIZE, SIZE, null);
				break;
				
			case BALL:
				dbg.drawImage(Settings.BALL_BONUS_IMG, locX, locY, SIZE, SIZE, null);
				break;
				
			case INCREASE_SIZE:
				dbg.drawImage(Settings.INCREASE_BONUS_IMG, locX, locY, SIZE, SIZE, null);
				break;
				
			case DECREASE_SIZE:
				dbg.drawImage(Settings.DECREASE_BONUS_IMG, locX, locY, SIZE, SIZE, null);
				break;
				
			case BULLETS:
				dbg.drawImage(Settings.BULLET_BONUS_IMG, locX, locY, SIZE, SIZE, null);
				break;
		}
	}
	
    public void updateSprite()
    {
        locY += dy;
    }
    
    public int getLocX()
    {
    	return locX;
    }
    
    public int getLocY()
    {
    	return locY;
    }
    
    public int getBonusKind()
    {
    	return bonusKind;
    }
}
