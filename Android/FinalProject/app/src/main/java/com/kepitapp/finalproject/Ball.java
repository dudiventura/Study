package com.kepitapp.finalproject;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Dudi on 10/2/2016.
 */
public class Ball {
    private final int BOUNCE_TRASHOLD = 100;
    private int pWidth = Settings.SCREEN_WIDTH - 15;
    private int size = Settings.BALL_SIZE;
    private int locX, locY;
    private double dx,dy;
    private Racket racket;
    private GameEngine ge;
    private boolean isGone;
    private boolean bounceRacket;
    private long bounceMomoent;

    public Ball(int x, int y, Racket r, double dx, double dy, GameEngine ge)
    {
        locX = x;
        locY = y;
        this.dy = dy;
        this.dx = dx;
        racket = r;
        this.ge = ge;
        isGone = false;
        bounceRacket = false;
    }

    public void drawBall(Canvas canvas)
    {
        Paint paint = new Paint();
        canvas.drawBitmap(Settings.BALL_IMG,locX,locY,paint);
    }

    public void ejectBall()
    {
        dy = -5;
    }

    public void updateSprite()
    {
        locX += dx;
        locY += dy;
        bounceAtWall();
        bounceAtRacket();
        outOfScreen();
    }

    private void bounceAtWall()
    {
        if(locX <= 0 || locX >= pWidth - size)
        {
            dx *= -1;
            if(racket.isBounced10())
                racket.resetWidth();
        }
        if(locY <= 0)
        {
            if(racket.isBounced10())
                racket.resetWidth();
            dy *= -1;
        }
    }

    private void bounceAtRacket()
    {

        Rectangle ballBox = new Rectangle(locX, locY, size, size);
        Rectangle racketBox = new Rectangle(racket.getLocX(), racket.getLocY(), racket.getWdith(), racket.getHeight());
        if(ballBox.intersects(racketBox) || racketBox.intersects(ballBox))
        {
            if(Settings.isSoundOn)
                Settings.POING.start();

            if(racket.isBounced10())
                racket.resetWidth();
            int intersectPoint1 = locX + (size/4);
            int intersectPoint2 = locX + (size/2);
            int intersectPoint3 = locX + (3*(size/4));
            if((intersectPoint1 >= racket.getLocX() && intersectPoint1 <= racket.getLocX() + racket.getWdith()/4)
                    ||(intersectPoint2 >= racket.getLocX() && intersectPoint2 <= racket.getLocX() + racket.getWdith()/4)
                    ||(intersectPoint3 >= racket.getLocX() && intersectPoint3 <= racket.getLocX() + racket.getWdith()/4))
            {
                dx = -4;
                dy = -3;
            }
            else if((intersectPoint1 < racket.getLocX() + racket.getWdith()/2) || (intersectPoint1 < racket.getLocX() + racket.getWdith()/2)
                    ||(intersectPoint3 < racket.getLocX() + racket.getWdith()/2))
            {

                dx = -2;
                dy = -4;

            }
            else if((intersectPoint1 == racket.getLocX() + racket.getWdith()/2) || (intersectPoint2 == racket.getLocX() + racket.getWdith()/2)
                    ||(intersectPoint3 == racket.getLocX() + racket.getWdith()/2))
            {

                dx = 0;
                dy = -5;

            }
            else if((intersectPoint1 <= racket.getLocX() + (3*racket.getWdith()/4)) || (intersectPoint2 <= racket.getLocX() + (3*racket.getWdith()/4))
                    || (intersectPoint3 <= racket.getLocX() + (3*racket.getWdith()/4)))
            {

                dx = 2;
                dy = -4;

            }
            else if((intersectPoint1 <= racket.getLocX() + racket.getWdith()) || (intersectPoint2 <= racket.getLocX() + racket.getWdith())
                    ||(intersectPoint3 <= racket.getLocX() + racket.getWdith()))
            {

                dx = 4;
                dy = -3;

            }

            racket.setLocY(racket.getLocY() + 5);
            bounceRacket = true;
            bounceMomoent = System.currentTimeMillis();
        }

        long now = System.currentTimeMillis();
        if(bounceRacket)
        {
            if(now - bounceMomoent > BOUNCE_TRASHOLD)
            {
                racket.setLocY(Settings.RACKET_START_LOCATION_Y);
                bounceRacket = false;
            }
        }
    }

    public void bounceAtBrick(Brick b)
    {
        int intersectPointX1 = locX + (size/4);
        int intersectPointX2 = locX + (size/2);
        int intersectPointX3 = locX + (3*(size/4));
        int intersectPointY1 = locX + (size/4);
        int intersectPointY2 = locX + (size/2);
        int intersectPointY3 = locX + (3*(size/4));
        if(((intersectPointX1 >= b.getLocX()) && intersectPointX1 < (b.getLocX() + Settings.BRICK_WIDTH))
                || ((intersectPointX2 >= b.getLocX()) && intersectPointX2 < (b.getLocX() + Settings.BRICK_WIDTH))
                || ((intersectPointX3 >= b.getLocX()) && intersectPointX3 < (b.getLocX() + Settings.BRICK_WIDTH)))
        {
            if(Settings.isSoundOn)
                Settings.BRICK_BOUNCE.start();

            if(racket.isBounced10())
                racket.resetWidth();
            dy*=-1;
        }
        else if(((intersectPointY1 >= b.getLocY()) && intersectPointY1 < (b.getLocY() + Settings.BRICK_HEIGHT))
                ||((intersectPointY2 >= b.getLocY()) && intersectPointY2 < (b.getLocY() + Settings.BRICK_HEIGHT))
                || ((intersectPointY3 >= b.getLocY()) && intersectPointY3 < (b.getLocY() + Settings.BRICK_HEIGHT)))
        {
            if(Settings.isSoundOn)
                Settings.BRICK_BOUNCE.start();

            if(racket.isBounced10())
                racket.resetWidth();
            dx*=-1;
        }
    }

    public void setLocX(int x)
    {
        locX = x;
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

    public double getDY()
    {
        return dy;
    }

    public double getDX()
    {
        return dx;
    }

    private void outOfScreen()
    {
        if(locY >= Settings.SCREEN_HEIGHT)
            ge.loseBall(this);
    }

    public void isGone()
    {
        isGone = true;
    }

    public boolean hasGone()
    {
        return isGone;
    }
}
