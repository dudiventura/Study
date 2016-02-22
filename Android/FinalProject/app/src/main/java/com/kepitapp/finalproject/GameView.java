package com.kepitapp.finalproject;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Dudi on 11/2/2016.
 */
public class GameView extends View implements GestureDetector.OnGestureListener {

    GameEngine ge;
    private GestureDetectorCompat gdc;
    Context context;
    public GameView(Context context) {
        super(context);
        this.context = context;
        initializeGame();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initializeGame();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initializeGame();
    }

    private void initializeGame()
    {
        gdc = new GestureDetectorCompat(getContext(),this);
        Settings.GAME_MUSIC.start();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        if(Settings.IS_IN_PREFERENCES_PAGE)
        {
            Settings.printPreferencesPage(this,canvas);
        }
        else {
            ge.drawGame(this, canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(ge == null)
            ge = new GameEngine(getContext());
        if(Settings.IS_IN_PREFERENCES_PAGE)
        {
            Settings.IS_IN_PREFERENCES_PAGE = false;
            Settings.isGameRunning = true;
            invalidate();
            ((GameActivity)context).startGame();


        }
        else {
            ge.touchEventHandler(event);

        }
        return gdc.onTouchEvent(event);
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if(!Settings.IS_IN_PREFERENCES_PAGE && Settings.isGameRunning)
        {
            if(ge.isRacketDragged((int)e1.getY()))
            {
                ge.moveRacket((int)e2.getX());
            }
        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}