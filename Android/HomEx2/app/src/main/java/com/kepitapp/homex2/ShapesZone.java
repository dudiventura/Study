package com.kepitapp.homex2;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.security.Timestamp;
import java.sql.Time;
import java.util.Random;
import java.util.Timer;

/**
 * Created by Dudi on 15/12/2015.
 */
public class ShapesZone extends View {

    int rectangleX, rectangleY;
    int numOfRectTouches = 0;
    int right,top,width,height;
    private Paint paint;
    private GameDAL DAL;
    private int complexity;
    private int CIRCLE_RADIUS;
    private int RECT_WIDTH;
    private int RECT_HEIGHT;
    private final int RADIUS_PART_OF_WIDTH = 18; // Circle radius will be width(View)/18
    private final int REC_WIDTH_PART_OF_SCREEN = 7, REC_HEIGHT_PART_OF_SCREEN = 10; // Rectangle width: width(View)/7 Rectangle height: height(View)/10;
    private SharedPreferences prefs;
    private GamesCircle[] circles;

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

   @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        CIRCLE_RADIUS = width/RADIUS_PART_OF_WIDTH;
        RECT_HEIGHT = height/REC_HEIGHT_PART_OF_SCREEN;
        RECT_WIDTH = width/REC_WIDTH_PART_OF_SCREEN;

        prefs = getContext().getSharedPreferences("LevelAndComplexity",Context.MODE_PRIVATE);
        if(prefs != null) {
            complexity = prefs.getInt("complexity", -1);
        }
        if(complexity == -1)
            complexity = 0; //default complexity
        circles = new GamesCircle[complexity];

        Random rand = new Random();
        for(int i = 0; i < complexity; i++)
        {
            int locationX,locationY;

            // Randomize the rectangle circles on the view, the number of circles as the chosen complexity.
            do {
                locationX = rand.nextInt(width-2*CIRCLE_RADIUS) + CIRCLE_RADIUS;
                locationY = rand.nextInt(height-2*CIRCLE_RADIUS) + CIRCLE_RADIUS;
            }
            while(checkCircleCollisions(locationX, locationY));

            circles[i] = new GamesCircle(locationX,locationY);

            canvas.drawCircle(locationX,locationY,CIRCLE_RADIUS,paint);
        }

        // Randomize the rectangle location on the view screen.
        do {
            rectangleX = rand.nextInt(width - RECT_WIDTH);
            rectangleY = rand.nextInt(height - RECT_HEIGHT);
        }while(checkRectangleCollisions(rectangleX,rectangleY));

        canvas.drawRect(rectangleX,rectangleY,rectangleX + RECT_WIDTH,rectangleY + RECT_HEIGHT,paint);

    }

    // Check collisions between the rectangle to all the circles. Return true if there is collision, else false.
    public boolean checkRectangleCollisions(int x, int y)
    {
        for(int i = 0; i < circles.length; i++)
        {
            if(circles[i] != null) {
                if (Math.abs(x - circles[i].getX()) < CIRCLE_RADIUS + RECT_WIDTH &&
                        Math.abs(y - circles[i].getY()) < CIRCLE_RADIUS + RECT_HEIGHT)
                    return true;
            }
        }
        return false;
    }

    //check for collisions before painting new circle on the screen. Return true if there is collision, else false
    private boolean checkCircleCollisions(int x, int y)
    {
        for(int i = 0; i < circles.length; i++)
        {
            if(circles[i] != null) {
                if (Math.abs(x - circles[i].getX()) < CIRCLE_RADIUS * 2 ||
                        Math.abs(y - circles[i].getY()) < CIRCLE_RADIUS * 2)
                    return true;
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        prefs = getContext().getSharedPreferences("LevelAndComplexity", getContext().MODE_PRIVATE);
        if(MainActivity.isGameRunning) // Checking the touch event meaning only if the game isn't running.
        {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                float touchedX = event.getX();
                float touchedY = event.getY();
                if (touchedX > rectangleX && touchedX < (rectangleX + RECT_WIDTH)
                        && touchedY > rectangleY && touchedY < (rectangleY + RECT_HEIGHT))
                {
                    numOfRectTouches++;
                    if(numOfRectTouches == prefs.getInt("level",1)) // Checking if the user pressed on the rectangle "level" times.
                    {
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putLong("recentScore", System.currentTimeMillis());
                        editor.apply();
                        numOfRectTouches = 0;
                        MainActivity.isGameRunning = false;
                    }
                    invalidate();
                }

            }
        }
        return true;
    }


}
