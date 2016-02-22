package com.kepitapp.finalproject;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by Dudi on 11/2/2016.
 */
public class Brick {
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

    public void drawBrick(Canvas canvas)
    {
        Paint paint = new Paint();
        switch(strength)
        {
            case 1:
                paint.setColor(Color.GREEN);
                break;

            case 2:
                paint.setColor(Color.YELLOW);
                break;

            case 3:
                paint.setColor(Color.BLUE);
                break;

            case 4:
                paint.setColor(Color.RED);
                break;
        }
        canvas.drawRect(locX, locY, locX + width, locY+height, paint);

    }

    public void setStrength(int s)
    {
        strength = s;
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
