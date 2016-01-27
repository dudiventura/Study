public class SoundThread extends Thread {
    private String fName;
    private int playMode;
    
    public SoundThread(String fileName, int mode)
    {
        fName = fileName;
        playMode = mode;
    }
    public void run()
    {
        if(playMode == AudioPlayer.ONCE)
             AudioPlayer.playMultiple(fName,0);
        else if(playMode == AudioPlayer.LOOP)
            AudioPlayer.playMultiple(fName, 10);
    }
    
}
