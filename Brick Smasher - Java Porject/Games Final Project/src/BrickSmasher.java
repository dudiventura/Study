import javax.swing.JFrame;

public class BrickSmasher
{
	public static void main(String[] args) 
	{
		JFrame frame = new JFrame("BrickSmasher");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT);
        GamePanel p = new GamePanel();
        frame.add(p);
        frame.setVisible(true);
	}
}
