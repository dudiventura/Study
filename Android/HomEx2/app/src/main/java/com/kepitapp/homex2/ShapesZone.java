package com.kepitapp.homex2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

/**
 * Created by Dudi on 15/12/2015.
 */
public class ShapesZone extends View {

    int right,top,width,height;
    private Paint paint;
    private GameDAL DAL;
    private int complexity;
    private final int CIRCLE_RADIUS = 35;

    public ShapesZone(Context context) {
        super(context);
        DAL = new GameDAL(context);
        init(null,0);
    }

    public ShapesZone(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs,0);
    }

    public ShapesZone(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs,defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH)
    {
        super.onSizeChanged(w,h,oldW,oldH);

        right = getPaddingRight();
        top = getPaddingTop();
        width = w - (getPaddingLeft()+getPaddingRight());
        height = h -(getPaddingTop() + getPaddingBottom());
    }

    public void init(AttributeSet attrs, int defStyle)
    {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(10);
    }

/*    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.drawCircle(100,100,50,paint);

        complexity = DAL.getComplexity();

        for(int i = 0; i < complexity; i++)
        {
            int locationX,locationY;
            Random rand = new Random();
            do {
                locationX = rand.nextInt(width);
                locationY = rand.nextInt(height);
            }while(checkCollisions(locationX,locationY));
            canvas.drawCircle(locationX,locationY,CIRCLE_RADIUS,paint);

        }

    }*/

    private boolean checkCollisions(int x, int y)
    {
        return true;
    }
}
