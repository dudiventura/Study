package com.kepitapp.finalproject;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Dudi on 17/2/2016.
 */
public class Racket
{
    private int pWidth = Settings.SCREEN_WIDTH - 15;
    private final int HEIGHT = Settings.RACKET_HEIGHT;
    private int width = Settings.RACKET_WIDTH;
    private int locX, locY;
    private int resizeBounces;
    private long startShottingTime;
    private int racketSize;


    public Racket(int x, int y)
    {
        locX = x;
        locY = y;
        resizeBounces = Settings.BONUS_BOUNCES;
        racketSize = 2;
    }

    public void drawRacket(Canvas canvas)
    {
        Paint paint = new Paint();
        switch (racketSize) {
            case 0:
                canvas.drawBitmap(Settings.RACKET_IMG_TINY, locX, locY, paint);
                break;
            case 1:
                canvas.drawBitmap(Settings.RACKET_IMG_SMALL, locX, locY, paint);
                break;
            case 2:
                canvas.drawBitmap(Settings.RACKET_IMG_BASIC, locX, locY, paint);
                break;
            case 3:
                canvas.drawBitmap(Settings.RACKET_IMG_BIG, locX, locY, paint);
                break;
            case 4:
                canvas.drawBitmap(Settings.RACKET_IMG_LARGE, locX, locY, paint);
                break;
        }
    }

    public void bonusEffect(int bonusKind)
    {
        switch(bonusKind)
        {
            case Bonus.INCREASE_SIZE:
                if(width < Settings.RACKET_MAX_WIDTH) {
                    width += 60;
                    racketSize++;
                }
                resizeBounces = Settings.BONUS_BOUNCES;
                break;

            case Bonus.DECREASE_SIZE:
                if(width > Settings.RACKET_MIN_WIDTH) {
                    width -= 60;
                    racketSize--;
                }
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
        if(resizeBounces == 0) {
            racketSize = 2;
            return true;
        }
        else
        {
            resizeBounces--;
            return false;
        }
    }

    public void setLocY(int y)
    {
        locY = y;
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

    public void move(int x) {
        if(x< Settings.SCREEN_WIDTH-width-15 && x>=0) {
            locX = x;
        }
    }
}
