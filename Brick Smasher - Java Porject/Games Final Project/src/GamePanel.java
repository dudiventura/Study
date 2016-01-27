import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.swing.JPanel;



public class GamePanel extends JPanel implements Runnable
{
	private static final int PERIOD = 60;
	private boolean newLevel;
	private int level;
	private LinkedList<Brick> bricks;
	private BufferedImage dbImg = null;
	private boolean running;
	private GameEngine game;
	private boolean[] keys;
	
	public GamePanel()
	{
		setFocusable(true);
		requestFocusInWindow();
		
		newLevel = true;
		level = 1;
		game = new GameEngine(Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT);
		
		keys = new boolean[150];
		
		addKeyListener(new Listener());
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		gameRender();
		g.drawImage(dbImg, 0, 0, this);
	}
	
	public void gameRender()
	{
		dbImg = game.drawGame();
	}
	
	public void run()
	{
		long before,  sleepTime;
		before = System.currentTimeMillis();
		running = true;

		while(running)
		{	
			gameUpdate();
			gameRender();
			paintScreen();   // active rendering

			sleepTime = PERIOD - before;
			if(sleepTime <= 0)
				sleepTime = 5;

			try 
			{
				Thread.sleep(sleepTime);
			}
			catch(InterruptedException e){}

			before = System.currentTimeMillis();
		}

	}
	
	public void paintScreen()
	{
		Graphics g;
		try {
			g = getGraphics();
			if(g != null)
			{
				g.drawImage(dbImg, 0, 0, null);
			}
		}
		catch(Exception e)
		{
			System.out.println("Graphics error");
			e.printStackTrace();
		}
	}
	
	public void gameUpdate()
	{
		
		if(keys[KeyEvent.VK_ENTER])
			game.enterKeyClicked();
		
		if(!game.isStarted() || !game.isPaused())
		{
			if (keys[KeyEvent.VK_UP])
				game.upKeyClicked();

			if (keys[KeyEvent.VK_LEFT])
				game.leftKeyClicked();

			if (keys[KeyEvent.VK_RIGHT])
				game.rightKeyClicked();

			if (keys[KeyEvent.VK_SPACE])
				game.spaceKeyClicked();
		
		game.updateGame();
		}
		
		if (game.isGameOver())
			running = false;
	}
	
	private class Listener extends KeyAdapter
	{
		public void keyPressed(KeyEvent e) 
		{
			keys[e.getKeyCode()] = true;
		}

		public void keyReleased(KeyEvent e) 
		{
			keys[e.getKeyCode()] = false;
		}

		public void keyTyped(KeyEvent e) 
		{
			keys[e.getKeyCode()] = true;
		}
	}

	// only start the animation once the JPanel has been added to the JFrame
	public void addNotify()
	{ 
		super.addNotify();   // creates the peer
		startGame();    // start the thread
	}

	public void startGame()
	{
		(new Thread(this)).start();
	}
}
