package com.kepitapp.finalproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.view.View;
import java.util.LinkedList;

/**
 * Created by Dudi on 10/2/2016.
 */
public class Settings {
    // Screen
    public static int SCREEN_HEIGHT;
    public static int SCREEN_WIDTH;
    public static boolean IS_IN_PREFERENCES_PAGE = true;

    public static Bitmap LIFE_IMG, MINE_IMG, INCREASE_BONUS_IMG, DECREASE_BONUS_IMG, RACKET_IMG_BASIC, RACKET_IMG_SMALL, RACKET_IMG_TINY,
            RACKET_IMG_BIG ,RACKET_IMG_LARGE ,BALL_IMG, LIFE_BONUS_IMG, BULLET_IMG, BULLET_BONUS_IMG, BALL_BONUS_IMG,SOUND_ON_IMG,SOUND_OFF_IMG;

    public static MediaPlayer GAME_MUSIC,BRICK_BOUNCE,BULLET_SHOOT,EXPLOSION,GAIN_BONUS,LOSE_BALL,LOSING_GAME,MINE_EXPLODE,POING,WINING_GAME;

    public static boolean isGameRunning = false;
    public static boolean isSoundOn = true;
    public static int SLEEP_TIME = 10;

    public static void initialImageViews(Context context)
    {
        LIFE_BONUS_IMG = BitmapFactory.decodeResource(context.getResources(), R.drawable.life_bonus25x25);

        BALL_IMG = BitmapFactory.decodeResource(context.getResources(),R.drawable.ball10x10);

        LIFE_IMG = BitmapFactory.decodeResource(context.getResources(),R.drawable.life15x15);

        BALL_BONUS_IMG = BitmapFactory.decodeResource(context.getResources(),R.drawable.ball_bonus25x25);

        BULLET_IMG = BitmapFactory.decodeResource(context.getResources(),R.drawable.bullet5x5);

        BULLET_BONUS_IMG = BitmapFactory.decodeResource(context.getResources(),R.drawable.bullet_bonus25x25);

        DECREASE_BONUS_IMG = BitmapFactory.decodeResource(context.getResources(),R.drawable.decrease_bonus25x25);

        INCREASE_BONUS_IMG = BitmapFactory.decodeResource(context.getResources(),R.drawable.increase_bonus25x25);

        MINE_IMG = BitmapFactory.decodeResource(context.getResources(),R.drawable.mine25x25);

        RACKET_IMG_BASIC = BitmapFactory.decodeResource(context.getResources(),R.drawable.racket80x20);

        RACKET_IMG_TINY = BitmapFactory.decodeResource(context.getResources(),R.drawable.racket40x20);

        RACKET_IMG_SMALL = BitmapFactory.decodeResource(context.getResources(),R.drawable.racket60x20);

        RACKET_IMG_BIG = BitmapFactory.decodeResource(context.getResources(),R.drawable.racket100x20);

        RACKET_IMG_LARGE = BitmapFactory.decodeResource(context.getResources(),R.drawable.racket120x20);

        SOUND_ON_IMG = BitmapFactory.decodeResource(context.getResources(),R.drawable.sound_on_icon30x30);

        SOUND_OFF_IMG = BitmapFactory.decodeResource(context.getResources(),R.drawable.sound_off_icon30x30);


        GAME_MUSIC = MediaPlayer.create(context, R.raw.game_music);
        GAME_MUSIC.setLooping(true);
        BRICK_BOUNCE = MediaPlayer.create(context, R.raw.brick_bounce);
        BULLET_SHOOT = MediaPlayer.create(context, R.raw.bullet_shoot);
        EXPLOSION = MediaPlayer.create(context, R.raw.explosion);
        GAIN_BONUS = MediaPlayer.create(context, R.raw.gain_bonus);
        LOSE_BALL = MediaPlayer.create(context, R.raw.lose_ball);
        LOSING_GAME = MediaPlayer.create(context, R.raw.losing_game);
        MINE_EXPLODE = MediaPlayer.create(context, R.raw.mine_explode);
        POING = MediaPlayer.create(context, R.raw.poing);
        WINING_GAME = MediaPlayer.create(context, R.raw.winning_game);

    }


    // Game Engine
    public static final int START_NUM_OF_LIVES = 3;
    public static final int MAX_NUM_OF_LIVES = 6;
    public static final int SHOT_THRESHOLD = 150;
    public static final int TOTAL_SHOTTING_TIME = 5000;

    //Racket
    public static int RACKET_START_LOCATION_X;
    public static int RACKET_START_LOCATION_Y;
    public static final int RACKET_WIDTH = 215 ;
    public static final int RACKET_HEIGHT = 20;
    public static final int RACKET_MAX_WIDTH = 335;
    public static final int RACKET_MIN_WIDTH = 95;

    //Pause
    public static int PAUSE_SIZE = 90;
    public static int PAUSE_X;
    public static int PAUSE_Y;


   // Ball
    public static int BALL_START_LOCATION_X;
    public static int BALL_START_LOCATION_Y;
    public static final int BALL_SIZE = 10;

    // Brick
    public static final int BRICK_HEIGHT = 60;
    public static final int BRICK_WIDTH = 120;

   // Bonus
    public static final int BONUS_SIZE = 25;
    public static final int BONUS_BOUNCES = 25;

    // Bullet
    public static final int BULLET_SIZE = 5;
    public static int BULLET_START_LOCATION_Y;


    public static LinkedList<Brick> initializeLevel(int lvlNum)
    {
        LinkedList<Brick> bricks = new LinkedList<Brick>();

        switch(lvlNum)
        {

            case 1:
                int locX = SCREEN_WIDTH/6;
                int locY = SCREEN_HEIGHT/12;
                int strength = 4;

                for(int i=0; i<5; i++)
                {
                    for(int j=0; j<10; j++)
                    {
                        Brick b = new Brick(strength,locX,locY);
                        bricks.add(b);
                        locX += BRICK_WIDTH + 10;
                    }
                    if(strength > 1)
                        strength--;
                    locX = SCREEN_WIDTH/6;
                    locY += BRICK_HEIGHT + 10;
                }
                break;

            case 2:
                locX = SCREEN_WIDTH/6;
                locY = SCREEN_HEIGHT/12;
                strength = 4;

                for(int i=0; i<10; i++)
                {
                    Brick b = new Brick(strength, locX, locY);
                    bricks.add(b);
                    if(i<9)
                        locX+= BRICK_WIDTH+10;
                }

                bricks.add(new Brick(strength,SCREEN_WIDTH/6,locY+BRICK_HEIGHT+10));
                bricks.add(new Brick(strength,locX,locY+BRICK_HEIGHT+10));
                locY += BRICK_HEIGHT+10;
                bricks.add(new Brick(strength,SCREEN_WIDTH/6,locY+BRICK_HEIGHT+10));
                bricks.add(new Brick(strength,locX,locY+BRICK_HEIGHT+10));
                locY += BRICK_HEIGHT+10;

                locX = SCREEN_WIDTH/6 + 2*BRICK_WIDTH + 20;
                for(int i = 0; i< 8; i++)
                {
                    Brick b = new Brick(strength, locX, locY);
                    bricks.add(b);
                    if(i<7)
                        locX += BRICK_WIDTH+10;
                }

                bricks.add(new Brick(strength,SCREEN_WIDTH/6,locY+BRICK_HEIGHT+10));
                bricks.add(new Brick(strength,locX,locY+BRICK_HEIGHT+10));
                locY += BRICK_HEIGHT + 10;
                bricks.add(new Brick(strength,locX,locY+BRICK_HEIGHT+10));

                locX = SCREEN_WIDTH/6;
                locY += BRICK_HEIGHT+10;
                for(int i = 0; i< 8; i++)
                {
                    Brick b = new Brick(strength, locX, locY);
                    bricks.add(b);
                    if(i<7)
                        locX += BRICK_WIDTH+10;
                }
                break;

            case 3:
                locX = SCREEN_WIDTH/6;
                locY = SCREEN_HEIGHT/12;
                strength = 4;
                int inALine = 10, startInALineX = 1, startInALineY = locY+5*(BRICK_HEIGHT+10);

                for(int i=1; i<3; i++)
                {
                    for(int j=0; j<inALine; j++)
                    {
                        Brick b1 = new Brick(strength,locX,locY);
                        Brick b2 = new Brick(strength,locX,locY+ startInALineY);
                        bricks.add(b1);
                        bricks.add(b2);
                        locX += BRICK_WIDTH+10;
                    }

                    inALine -= 2;
                    strength--;
                    locX = SCREEN_WIDTH/6 +  i*(BRICK_WIDTH + 10);
                    locY += i*(BRICK_HEIGHT+10);
                    startInALineY = locY+2*(BRICK_HEIGHT+10);
                }

                locX = SCREEN_WIDTH/6 +  2*(BRICK_WIDTH + 10);
                locY = SCREEN_HEIGHT/3;
                for(int i=0; i<inALine; i++)
                {
                    Brick b1 = new Brick(strength,locX,locY);
                    bricks.add(b1);
                    locX += BRICK_WIDTH+10;
                }
                break;

            case 4:
                locX = SCREEN_WIDTH/6;
                locY = SCREEN_HEIGHT/12;
                strength = 2;

                for(int j=0; j<10; j++)
                {
                    Brick b1 = new Brick(strength,locX,locY);
                    Brick b2 = new Brick(strength,locX,locY+5*(BRICK_HEIGHT+10));
                    bricks.add(b1);
                    bricks.add(b2);
                    locX += BRICK_WIDTH + 10;
                }

                locX = SCREEN_WIDTH/6;
                locY += BRICK_HEIGHT+10;
                int rememberLocY = locY;
                strength--;
                for (int i=0; i<4; i++)
                {
                    Brick b1 = new Brick(strength,locX,locY);
                    Brick b2 = new Brick(strength,locX+9*(BRICK_WIDTH+10),locY);
                    bricks.add(b1);
                    bricks.add(b2);
                    locY += BRICK_HEIGHT+10;
                }

                locX = SCREEN_WIDTH/6 + 2*(BRICK_WIDTH+10);
                locY = rememberLocY + BRICK_HEIGHT +10;
                strength=4;
                for (int i=0; i<2; i++)
                {
                    for (int j=0; j<2; j++)
                    {
                        Brick b1 = new Brick(strength,locX,locY);
                        Brick b2 = new Brick(strength,locX+4*(BRICK_WIDTH+10),locY);
                        bricks.add(b1);
                        bricks.add(b2);
                        locX += BRICK_WIDTH+10;
                    }
                    locX = SCREEN_WIDTH/6 + 2*(BRICK_WIDTH+10);
                    locY += BRICK_HEIGHT+10;

                }
                break;

            case 5:
                locY = SCREEN_HEIGHT/12;
                strength = 4;
                boolean evenRaw = false;

                for(int i=0; i<5; i++)
                {
                    int numOfBricks;
                    if(!evenRaw) {
                        locX = SCREEN_WIDTH/8;
                        numOfBricks = 6;
                    }
                    else {
                        locX = SCREEN_WIDTH/8 + BRICK_WIDTH + 10;
                        numOfBricks = 5;
                    }
                    for(int j=0; j<numOfBricks; j++)
                    {
                        Brick b = new Brick(strength,locX,locY);
                        bricks.add(b);
                        locX += 2*(BRICK_WIDTH+10);
                    }
                    locY += BRICK_HEIGHT+10;
                    if(i%2 == 0)
                        evenRaw = true;
                    else
                        evenRaw = false;
                }
                break;
        }
        return bricks;
    }

    public static void createLevel(LinkedList<Brick> bricks, Canvas canvas)
    {
        for(Brick b: bricks)
            b.drawBrick(canvas);
    }

    private static void initSpritesSizes(View gameView)
    {
        Settings.SCREEN_HEIGHT = gameView.getHeight();
        Settings.SCREEN_WIDTH = gameView.getWidth();
        RACKET_START_LOCATION_X = (SCREEN_WIDTH/2) - 5;
        RACKET_START_LOCATION_Y = SCREEN_HEIGHT - 100;
        BALL_START_LOCATION_X = (SCREEN_WIDTH/2) + 40;
        BALL_START_LOCATION_Y = SCREEN_HEIGHT - 120;
        BULLET_START_LOCATION_Y = RACKET_START_LOCATION_Y - 10;
        PAUSE_X = SCREEN_WIDTH - PAUSE_SIZE - 20;
        PAUSE_Y = 100;

    }

    public static void printPreferencesPage(View gameView, Canvas canvas) {
        initSpritesSizes(gameView);

        Paint paint = new Paint();

        int newLine;

        paint.setColor(Color.WHITE);
        paint.setTextSize(SCREEN_HEIGHT / 10);
        canvas.drawText("BRICK SEA-MASHER!", SCREEN_WIDTH / 4, SCREEN_HEIGHT / 12, paint);

        paint.setTextSize(SCREEN_HEIGHT / 20);
        newLine = SCREEN_HEIGHT / 6;
        canvas.drawText("Use the ball and the racket to smash all the bricks.", SCREEN_HEIGHT / 9, newLine, paint);
        newLine += SCREEN_HEIGHT / 18;
        canvas.drawText("When you smash a brick, you might get one of those:", SCREEN_HEIGHT / 9, newLine, paint);

        paint.setTextSize(SCREEN_HEIGHT / 25);

        newLine = SCREEN_WIDTH / 8;
        canvas.drawBitmap(LIFE_BONUS_IMG, SCREEN_HEIGHT / 7, newLine, paint);
        canvas.drawText(" - Life bonus (6 at most) - for each life point you have you gain 300 points at the end of each level", SCREEN_WIDTH / 9, newLine + SCREEN_HEIGHT / 18, paint);
        newLine += SCREEN_HEIGHT / 12;
        canvas.drawBitmap(MINE_IMG, SCREEN_HEIGHT / 7, newLine, paint);
        canvas.drawText(" - Mine, don't touch those!", SCREEN_WIDTH / 9, newLine + SCREEN_HEIGHT / 18, paint);
        newLine += SCREEN_HEIGHT / 12;
        canvas.drawBitmap(INCREASE_BONUS_IMG, SCREEN_HEIGHT / 7, newLine, paint);
        canvas.drawText(" - Increase the racket for 25 bounces (on racket, on wall, on brick - any bounce count).", SCREEN_WIDTH / 9, newLine + SCREEN_HEIGHT / 18, paint);
        newLine += SCREEN_HEIGHT / 12;
        canvas.drawBitmap(DECREASE_BONUS_IMG, SCREEN_HEIGHT / 7, newLine, paint);
        canvas.drawText(" - Decrease the racket for 25 bounces (on racket, on wall, on brick - any bounce count).", SCREEN_WIDTH / 9, newLine + SCREEN_HEIGHT / 18, paint);
        newLine += SCREEN_HEIGHT / 12;
        canvas.drawBitmap(BULLET_BONUS_IMG, SCREEN_HEIGHT / 7, newLine, paint);
        canvas.drawText(" - Bullets!! for few seconds you may shoot so - SHOOT THEM ALL!!.", SCREEN_WIDTH / 9, newLine + SCREEN_HEIGHT / 18, paint);
        newLine += SCREEN_HEIGHT / 12;
        canvas.drawBitmap(BALL_BONUS_IMG, SCREEN_HEIGHT / 7, newLine, paint);
        canvas.drawText(" - Extra ball - for each ball you'll have at the end of each level you gain 100 points.", SCREEN_WIDTH / 9, newLine + SCREEN_HEIGHT / 18, paint);

        newLine += SCREEN_HEIGHT / 7;
        canvas.drawText("There are 5 levels.", SCREEN_WIDTH / 9, newLine, paint);
        newLine += SCREEN_HEIGHT / 18;
        canvas.drawText("To Eject new ball press on the screen.", SCREEN_WIDTH / 9, newLine, paint);
        newLine += SCREEN_HEIGHT / 18;
        canvas.drawText("When you take bullets bonus the racket start fire automatically", SCREEN_WIDTH / 9, newLine, paint);
        newLine += SCREEN_HEIGHT / 18;
        canvas.drawText("You may pause the game any time by pressing the pause button.", SCREEN_WIDTH / 9, newLine, paint);

        newLine = 7 * SCREEN_HEIGHT / 8;
        canvas.drawText("Good Luck!!", 7 * SCREEN_WIDTH / 9, newLine, paint);
        newLine += SCREEN_HEIGHT / 18;
        canvas.drawText("Press on the screen", 7 * SCREEN_WIDTH / 9, newLine, paint);
        newLine += SCREEN_HEIGHT / 18;
        canvas.drawText("to continue...", 7 * SCREEN_WIDTH / 9, newLine, paint);
    }
}
