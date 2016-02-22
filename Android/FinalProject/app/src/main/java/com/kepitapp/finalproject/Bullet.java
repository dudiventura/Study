package com.kepitapp.finalproject;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Dudi on 17/2/2016.
 */
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

    public void drawBullet(Canvas canvas)
    {
        Paint paint = new Paint();
        canvas.drawBitmap(Settings.BULLET_IMG,locX,locY,paint);
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
