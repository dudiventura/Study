package com.kepitapp.finalproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

/**
 * Created by Dudi on 18/2/2016.
 */
public class GameEngine{
    private int numOfLives = Settings.START_NUM_OF_LIVES;
    private int height, width;
    private long lastShootTime;
    private long lastSoundPressedTime;
    private long lastBallEjected;

    private boolean win, lose, newLevel, isPaused, firstEnterPressed;
    private int score, level;
    private LinkedList<Brick> bricks;
    private LinkedList<Ball> balls;
    private LinkedList<Bonus> bonuses;
    private LinkedList<Bullet> bullets;
    private LinkedList<Bullet> deleteBullets;
    private LinkedList<Ball> deleteBalls;
    private Racket racket;
    Context context;
    GameDAL DAL;
    int maxScore;



    public GameEngine(Context context)
    {
        this.context = context;
        score = 0;
        level = 1;
        win = lose = isPaused = firstEnterPressed =  false;
        width = Settings.SCREEN_WIDTH;
        height = Settings.SCREEN_HEIGHT;

        DAL = new GameDAL(context);
        bricks = Settings.initializeLevel(level);
        racket = new Racket(Settings.RACKET_START_LOCATION_X, Settings.RACKET_START_LOCATION_Y);
        bonuses = new LinkedList<Bonus>();
        balls = new LinkedList<Ball>();
        bullets = new LinkedList<Bullet>();
        deleteBullets = new LinkedList<Bullet>();
        deleteBalls = new LinkedList<Ball>();
        balls.add(new Ball(Settings.BALL_START_LOCATION_X, Settings.BALL_START_LOCATION_Y, racket, 0, 0, this));

        lastShootTime = System.currentTimeMillis();
        lastBallEjected = System.currentTimeMillis();
        newLevel = true;
        maxScore = updateMaxScore();

    }

    private int updateMaxScore() {
        int maximumScore = -1;
        if(DAL != null)
        {
            LinkedList<Integer> Scores =  DAL.getScores();
            if(Scores != null && Scores.size() > 0) {
                maximumScore = Scores.get(0);
                for (int score : Scores) {
                    if (score > maximumScore)
                        maximumScore = score;
                }
            }
        }
        return maximumScore;
    }

    public boolean isGameOver()
    {
        return win || lose;
    }

    public boolean isLevelOver()
    {
        if(bricks.isEmpty())
            return true;
        return false;
    }

    public void drawGame(GameView gv, Canvas canvas)
    {
        Paint paint = new Paint();

        //Draw pause button
        drawSoundIcon(canvas);

        // Draw bricks
        if(newLevel)
        {
            Settings.createLevel(bricks, canvas);
            newLevel = false;
        }
        else
            for(Brick b : bricks)
                b.drawBrick(canvas);

        // Draw racket
        racket.drawRacket(canvas);

        // Draw bonuses
        for(Bonus b: bonuses)
            b.drawBonus(canvas, b.getBonusKind());

        // Draw balls
        for(Ball b : balls)
            b.drawBall(canvas);

        // Draw bullets
        for(Bullet b : bullets)
            b.drawBullet(canvas);

        // Draw Life icons
        int lifeLocX = 10;
        for(int i=0; i<numOfLives; i++)
        {
            canvas.drawBitmap(Settings.LIFE_IMG, lifeLocX, 10, paint);
            lifeLocX += 30;
        }


        // Draw score title
        drawScores(canvas,paint);


        if(win || lose)
        {
            gameOverMessage(canvas);
        }
    }

    private void drawScores(Canvas canvas, Paint paint) {
        paint.setTextSize(Settings.SCREEN_HEIGHT /30);
        paint.setColor(Color.WHITE);
        canvas.drawText("Score: " + score, Settings.SCREEN_WIDTH - 200, 30, paint);

        if(maxScore != -1)
        {
           canvas.drawText("Max score: " + maxScore, Settings.SCREEN_WIDTH - 500, 30, paint);
        }
    }

    private void drawSoundIcon(Canvas canvas)
    {
        Paint paint = new Paint();
        if(Settings.isSoundOn)
            canvas.drawBitmap(Settings.SOUND_ON_IMG, Settings.PAUSE_X, Settings.PAUSE_Y, paint);
        else
            canvas.drawBitmap(Settings.SOUND_OFF_IMG, Settings.PAUSE_X, Settings.PAUSE_Y, paint);
    }

    public void updateGame()
    {
        if(isGameOver()) {
            Settings.isGameRunning = false;
            DAL.updateScoreTable(score);
        }

        if(isLevelOver() && level == 5)
        {
            win = true;
            for(int i=0; i<numOfLives; i++)
                score += 300;
            score += 100*balls.size();
        }

        else if(isLevelOver())
        {
            level++;
            bricks = Settings.initializeLevel(level);
            newLevel=true;
            if(!bullets.isEmpty())
                bullets.clear();
            for(int i=0; i<numOfLives; i++)
                score += 300;
            score += 100*balls.size();
            resetPosition();
        }

        shootingBonus();

        Brick check = checkCollisions();
        if (check != null)
            doCollisionLogic(check);

        for(Bonus b : bonuses)
            b.updateSprite();

        removeBalls();

        for(Ball b : balls)
            b.updateSprite();

        removeBullets();

        for(Bullet b : bullets)
            b.updateSprite();

    }

    private Brick checkCollisions()
    {
        // Check collision between ball to brick
        for(Ball ball: balls)
        {
            Rectangle ballBox = new Rectangle(ball.getLocX(), ball.getLocY(), ball.getSize(), ball.getSize());
            for(Brick brick: bricks)
            {
                Rectangle brickBox = new Rectangle(brick.getLocX(), brick.getLocY(), Settings.BRICK_WIDTH, Settings.BRICK_HEIGHT);
                if(ballBox.intersects(brickBox) || brickBox.intersects(ballBox))
                    return brick;
            }
        }

        // Check collision between bullet to brick
        for(Bullet bullet: bullets)
        {
            Rectangle bulletBox = new Rectangle(bullet.getLocX(), bullet.getLocY(), bullet.getSize(), bullet.getSize());
            for(Brick brick: bricks)
            {
                Rectangle brickBox = new Rectangle(brick.getLocX(), brick.getLocY(), Settings.BRICK_WIDTH, Settings.BRICK_HEIGHT);
                if(bulletBox.intersects(brickBox) || brickBox.intersects(bulletBox))
                {
                    bullet.isCollide();
                    return brick;
                }
            }
        }


        // Check collision between racket to bonus
        Rectangle racketBox = new Rectangle(racket.getLocX(), racket.getLocY(), racket.getWdith(), racket.getHeight());
        LinkedList<Bonus> deleteBonuses = new LinkedList<Bonus>();
        for(Bonus bonus : bonuses)
        {
            Rectangle bonusBox = new Rectangle(bonus.getLocX(), bonus.getLocY(), 40 , 40);
            if(racketBox.intersects(bonusBox) || bonusBox.intersects(racketBox))
            {
                if(bonus.getBonusKind() >= 4)
                    racket.bonusEffect(bonus.getBonusKind());
                else
                {
                    if(bonus.getBonusKind() == Bonus.LIFE && numOfLives < Settings.MAX_NUM_OF_LIVES)
                        numOfLives++;
                    else if(bonus.getBonusKind() == Bonus.MINE && numOfLives >= 0)
                    {
                        if(Settings.isSoundOn)
                            Settings.MINE_EXPLODE.start();

                        numOfLives--;
                        if(numOfLives < 0)
                            lose = true;
                        resetPosition();
                    }
                    else if(bonus.getBonusKind() == Bonus.BALL)
                    {
                        Ball oldBall = balls.getLast();
                        addNewBall(oldBall);
                    }
                }
                deleteBonuses.add(bonus);
            }
        }
        bonuses.removeAll(deleteBonuses);
        return null;
    }

    private void doCollisionLogic(Brick b)
    {
        if(b != null) // Collision with brick
        {
            if(b.getStrength() > 1)
                score += 10;
            else
                score += 100;

            if(b.getStrength()-1 > 0)
                b.setStrength(b.getStrength()-1);
            else
            {
                bricks.remove(b);
                if(b.isBonusBrick())
                    giveBonus(b);
            }

            Rectangle brickBox = new Rectangle(b.getLocX(), b.getLocY(), Settings.BRICK_WIDTH, Settings.BRICK_HEIGHT);
            for(Ball ball : balls)
            {
                Rectangle ballBox =new Rectangle(ball.getLocX(), ball.getLocY(), ball.getSize(), ball.getSize());
                if(ballBox.intersects(brickBox) || brickBox.intersects(ballBox))
                    ball.bounceAtBrick(b);
            }
        }
    }


    private void gameOverMessage(Canvas canvas)
    {
        Paint paint = new Paint();
        if (lose)
        {
            if(Settings.isSoundOn)
                Settings.LOSING_GAME.start();

            paint.setTextSize(100);
            paint.setColor(Color.RED);
            canvas.drawText("You Lose!", width / 2 - 120, height / 2, paint);
            paint.setTextSize(90);
            canvas.drawText("Your Final Score: " + score, width / 2 - 170, height / 2 + 100, paint);
        }
        else
        {
            if(Settings.isSoundOn)
                Settings.WINING_GAME.start();

            paint.setTextSize(100);
            paint.setColor(Color.GREEN);
            canvas.drawText("You Win!", width / 2 - 120, height / 2, paint);
            paint.setTextSize(90);
            canvas.drawText("Your Final Score: " + score, width / 2 - 170, height / 2 + 100, paint);
        }
    }

    public void shootingBonus()
    {
        long now = System.currentTimeMillis();
        if(racket.isArmed(now))
        {
            if (now - lastShootTime > Settings.SHOT_THRESHOLD)
            {
                Bullet leftBullet = new Bullet(racket.getLocX(), Settings.BULLET_START_LOCATION_Y);
                Bullet rightBullet = new Bullet(racket.getLocX()+racket.getWdith()-15, Settings.BULLET_START_LOCATION_Y);
                bullets.add(leftBullet);
                bullets.add(rightBullet);
                lastShootTime = now;

            }
        }
    }

    public void loseBall(Ball b)
    {
        b.isGone();

        if(numOfLives > 0 && balls.size()-1 == 0)
        {
            if(Settings.isSoundOn)
                Settings.LOSE_BALL.start();
        }
    }

    private void resetPosition()
    {
        while(!balls.isEmpty())
            balls.removeLast();

        Ball newBall = new Ball(racket.getLocX() + racket.getWdith()/2-5, Settings.BALL_START_LOCATION_Y, racket, 0, 0, this);
        balls.add(newBall);
        racket.resetWidth();

    }

    private void giveBonus(Brick b)
    {
        Random rand = new Random();
        int randNum = rand.nextInt(7)+1;
        bonuses.add(new Bonus(randNum, b.getLocX()+(Settings.BRICK_WIDTH/2)-5, b.getLocY()));
    }

    private void removeBullets()
    {
        deleteBullets.clear();

        for(Bullet bullet : bullets)
            if(bullet.collided())
                deleteBullets.add(bullet);

        bullets.removeAll(deleteBullets);
    }

    private void removeBalls()
    {
        deleteBalls.clear();

        for(Ball ball : balls)
            if(ball.hasGone())
                deleteBalls.add(ball);

        balls.removeAll(deleteBalls);

        if(balls.isEmpty())
        {
            if(numOfLives > 0)
            {
                numOfLives--;
                resetPosition();
            }
            else
                lose = true;
        }

    }

    private void addNewBall(Ball old)
    {
        double randDX, randDY;
        double oldDY = old.getDY();
        double oldDX = old.getDX();
        int oldLocX = old.getLocX();
        int oldLocY = old.getLocY();

        if((oldDX ==0 ) && (oldDY == 0))
        {
            balls.add(new Ball(oldLocX, oldLocY, racket, oldDX, oldDY, this));
            return;
        }

        Random rand = new Random();
        do
        {
            randDX = rand.nextInt(5)+1;
            if(randDX < 3)
                randDX = 3;
            randDY = rand.nextInt(5)+1;
            if(randDY < 3)
                randDY = 3;
            if(oldDX < 0)
                randDX*=-1;
            randDY *= -1;
        }while((randDY == oldDY) || (randDX == oldDX));

        balls.add(new Ball(oldLocX, oldLocY, racket, randDX, randDY, this));
    }

    public void continueGame()
    {
        isPaused = false;
    }

    public void pausedGame()
    {
        isPaused = true;
    }

    public boolean isPaused()
    {
        return isPaused;
    }

    public void touchEventHandler(MotionEvent event) {
        Ball ball;
        if(!isPauseTouched(event))
        {
            if((ball = isBallOnRacket()) != null)
            {
                ball.ejectBall();
            }
            if(isRacketDragged((int)event.getY())) {
                racket.move((int) (event.getX()));
            }
        }
        else //Sound touched
        {
            long now = System.currentTimeMillis();
            if (now - lastSoundPressedTime > Settings.SHOT_THRESHOLD) {
                if(Settings.isSoundOn)
                    Settings.GAME_MUSIC.pause();
                else
                    Settings.GAME_MUSIC.start();
                Settings.isSoundOn = !Settings.isSoundOn;
                lastSoundPressedTime = now;
            }

        }
    }

    public boolean isRacketDragged(int locY) {
        {
            if(locY >= Settings.RACKET_START_LOCATION_Y - 100 && locY <= Settings.SCREEN_HEIGHT)
            {
               return true;
            }
        }
        return false;
    }

    private Ball isBallOnRacket() {
        for(Ball b: balls)
        {
            if(b.getDY() == 0)
            {
                return b;
            }
        }
        return null;
    }

    private boolean isPauseTouched(MotionEvent event)
    {
        Rectangle touchBox = new Rectangle((int)event.getX(),(int)event.getY(),5,5);
        Rectangle pauseBox = new Rectangle(Settings.PAUSE_X,Settings.PAUSE_Y,Settings.PAUSE_SIZE,Settings.PAUSE_SIZE);
        return touchBox.intersects(pauseBox) || pauseBox.intersects(touchBox);
    }

    public void moveRacket(int x) {
        racket.move(x);
    }
}
