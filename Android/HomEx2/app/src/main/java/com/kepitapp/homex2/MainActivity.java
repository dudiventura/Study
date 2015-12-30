package com.kepitapp.homex2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.locks.ReentrantLock;


public class MainActivity extends Activity implements View.OnClickListener {

    private int m_Level;
    private int m_Complexity;
    private Button startButton, settingsButton;
    SharedPreferences prefs;
    TextView txtCurRecentTime,txtCurRecentTimeValue,txtBestScoreValue;
    private Context context;
    private long startTime,curTime; // contain the current time of each pressing on the "start" button.
    Thread thStopper; // Timer thread.
    private final int MILLISEC_IN_SEC = 1000, SLEEP_TIME = 10, DEFAULT_LVL = 1, DEFAULT_COMPLEX = 0, EXIT_SUCCESS = 0;
    public static boolean isGameRunning; // Static flag - connecting between the MainActivity and the ShapeZone classes in order to keep the drawing while the game running.
    public GameDAL DAL; // Pulling and pushing data to the database.
    View shapezone; // Access to the shapezone class in order to invalidate.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = (Button)findViewById(R.id.btnStart);
        settingsButton = (Button)findViewById(R.id.btnSettings);
        txtCurRecentTime = (TextView)findViewById(R.id.lblCurRecentScore);
        txtCurRecentTimeValue = (TextView)findViewById(R.id.lblCurRecentScoreValue);
        txtBestScoreValue = (TextView)findViewById(R.id.lblBestScoreValue);
        shapezone = (View)findViewById(R.id.shapeZoneView);

        startButton.setOnClickListener(this);
        settingsButton.setOnClickListener(this);
        txtBestScoreValue.setOnClickListener(this);

        DAL = new GameDAL(this);

        context = this;
        printScores();
        isGameRunning = false;
    }


    // Print the best and the recent score on position according to the level, complexity and data from the database.
    private void printScores()
    {
        String bestTimePrint = "", recentTimePrint = "";

        // Getting the level and complexity for the shared data.
        prefs = getSharedPreferences("LevelAndComplexity", MODE_PRIVATE);
        m_Level = prefs.getInt("level", DEFAULT_LVL);
        m_Complexity = prefs.getInt("complexity", DEFAULT_COMPLEX);

        // Getting best result form the database according to the level and complexity, adding 0's from the left two numbers with 1 or 2 digits.
        int bestTime = DAL.getBestResult(m_Level, m_Complexity);
        int bestTimeSeconds = bestTime/MILLISEC_IN_SEC;
        bestTimePrint += "" + bestTimeSeconds;
        int bestTimeMilliSeconds = bestTime%MILLISEC_IN_SEC;
        if(bestTimeMilliSeconds<10)
            bestTimePrint += ".00" + bestTimeMilliSeconds;
        else if(bestTimeMilliSeconds<100)
                bestTimePrint += ".0" + bestTimeMilliSeconds;
        else
            bestTimePrint += "." + bestTimeMilliSeconds;

        // Getting recent result form the database according to the level and complexity, adding 0's from the left two numbers with 1 or 2 digits.
        int recentTime = DAL.getRecentResult(m_Level,m_Complexity);
        int recentTimeSeconds = recentTime/MILLISEC_IN_SEC;
        recentTimePrint += "" + recentTimeSeconds;
        int recentTimeMilliSeconds = recentTime%MILLISEC_IN_SEC;
        if(recentTimeMilliSeconds<10)
            recentTimePrint += ".00" + recentTimeMilliSeconds;
        else if(recentTimeMilliSeconds<100)
            recentTimePrint += ".0" + recentTimeMilliSeconds;
        else
            recentTimePrint += "." + recentTimeMilliSeconds;

        // Printing results on position.
        txtBestScoreValue.setText(bestTimePrint);
        txtCurRecentTimeValue.setText(recentTimePrint);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    @Override
    public void onClick(View v)
    {
        // Pressing the best score time - Initialize the best score.
        if(v.getId() == txtBestScoreValue.getId())
        {
            DAL.updateBestResult(m_Level,m_Complexity,0);
            printScores();
        }
        // Pressing the start button - start the timer, run the game.
        else if(v.getId() == startButton.getId() && !isGameRunning) {
            shapezone.invalidate(); // Draw the game screen
            txtCurRecentTime.setText("Current Time:");
            isGameRunning = true;
            thStopper = new Thread(){
                @Override
                public void run() {
                    startTime = System.currentTimeMillis();

                    while(isGameRunning) {
                        try {
                            Thread.sleep(SLEEP_TIME);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                    if(isGameRunning)
                                    {
                                        curTime = System.currentTimeMillis();
                                        long interval = curTime - startTime; // refresh after every time the thread "awake" from the sleep command.
                                        // Separate the interval time from milliseconds to seconds part and milliseconds and print it on the current time position.
                                        int seconds = (int) (interval / MILLISEC_IN_SEC);
                                        int milliSeconds = (int) (interval % MILLISEC_IN_SEC);
                                        txtCurRecentTimeValue.setText("" + seconds + "." + milliSeconds);
                                    }
                                    else // Checking if the user pressed the rectangle "level" times and the game isn't running anymore.
                                    {
                                        if(curTime < startTime)
                                        {
                                            curTime = System.currentTimeMillis();
                                        }
                                        DAL.updateBestResult(m_Level, m_Complexity, (int) (curTime - startTime));
                                        DAL.updateRecentResult(m_Level, m_Complexity, (int) (curTime - startTime));

                                        txtCurRecentTime.setText("Recent Time:");
                                        printScores();


                                    }

                            }
                        });
                    }
                }
            };
            thStopper.start();
        }
        else if(v.getId() == settingsButton.getId() && !isGameRunning)
        {
            // Going to settings activity.
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();
    }

    // Back button close the application.
    @Override
    public void onBackPressed(){
        finish();
        System.exit(EXIT_SUCCESS);
    }
}
