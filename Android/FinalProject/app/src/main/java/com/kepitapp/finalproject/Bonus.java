package com.kepitapp.finalproject;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Dudi on 17/2/2016.
 */
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

    public void drawBonus(Canvas canvas, int kind)
    {
        Paint paint = new Paint();
        switch(kind)
        {
            case LIFE:
                canvas.drawBitmap(Settings.LIFE_BONUS_IMG,locX,locY,paint);
                break;
            case MINE:
                canvas.drawBitmap(Settings.MINE_IMG,locX,locY,paint);
                break;

            case BALL:
                canvas.drawBitmap(Settings.BALL_BONUS_IMG,locX,locY,paint);
                break;

            case INCREASE_SIZE:
                canvas.drawBitmap(Settings.INCREASE_BONUS_IMG,locX,locY,paint);
                break;

            case DECREASE_SIZE:
                canvas.drawBitmap(Settings.DECREASE_BONUS_IMG,locX,locY,paint);
                break;

            case BULLETS:
                canvas.drawBitmap(Settings.BULLET_BONUS_IMG,locX,locY,paint);
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
