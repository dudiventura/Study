package com.kepitapp.finalproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import java.io.IOException;
import java.util.Set;


public class GameActivity extends ActionBarActivity {

    private GameView gameView;
    private long lastPausedTime;
    Thread gameThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Settings.initialImageViews(this);
        setContentView(R.layout.activity_game);
        gameView = (GameView) findViewById(R.id.gameViewID);

        Settings.IS_IN_PREFERENCES_PAGE = true;

    }

    public void startGame()
    {
        gameThread = new Thread(){
            @Override
            public void run() {
                while(Settings.isGameRunning || gameView.ge.isPaused()) {
                    try {
                        Thread.sleep(Settings.SLEEP_TIME);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            if(Settings.isGameRunning)
                            {
                                gameView.ge.updateGame();
                                gameView.invalidate();
                            }
                            else // game has stopped
                            {

                            }

                        }
                    });
                }
            }
        };
        gameThread.start();
    }

    @Override
    public void onStop()
    {
        Settings.GAME_MUSIC.stop();
        Settings.isGameRunning = false;
    }

    @Override
    public void onBackPressed()
    {
        if(Settings.isGameRunning) {
            long now = System.currentTimeMillis();
            if (now - lastPausedTime > Settings.SHOT_THRESHOLD) {
                Settings.isGameRunning = false;
                gameView.ge.pausedGame();

                new AlertDialog.Builder(this)
                        .setTitle("Exit game")
                        .setMessage("Would you like to continue or quit the game?\nYour score will not be saved")
                        .setCancelable(false)
                        .setPositiveButton(R.string.txt_continue, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Settings.isGameRunning = true;
                                gameView.ge.continueGame();
                            }
                        })
                        .setNegativeButton(R.string.txt_quit, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Settings.GAME_MUSIC.stop();
                                gameThread.interrupt();
                                System.exit(1);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                lastPausedTime = now;
            }
        }
        else
            System.exit(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
