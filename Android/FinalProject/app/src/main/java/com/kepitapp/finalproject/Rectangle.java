package com.kepitapp.finalproject;

/**
 * Created by Dudi on 17/2/2016.
 */
public class Rectangle
{
    private int locX,locY,width,height;

    public Rectangle(int locX,int locY,int width,int height)
    {
        this.locX = locX;
        this.locY = locY;
        this.width = width;
        this.height = height;
    }

    public boolean intersects(Rectangle other)
    {
        return pointIntersects(other.locX,other.locY) || pointIntersects(other.locX + other.width,other.locY) ||
                pointIntersects(other.locX + other.width,other.locY + other.height) || pointIntersects(other.locX,other.locY + other.height);
    }
    private boolean pointIntersects(int x, int y)
    {

        if(this.locX <= x && this.locX + width >= x)
             if(this.locY <= y && this.locY + height >= y)
                return true;
        return false;
    }
}
