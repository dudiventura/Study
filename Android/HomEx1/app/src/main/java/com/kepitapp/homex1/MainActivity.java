package com.kepitapp.homex1;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;

import java.util.Random;


public class MainActivity extends Activity implements View.OnClickListener, View.OnLongClickListener {

    private  Button cmdLeft;

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    private Button cmdRight;
    private TextView txtTopText;
    private TextView txtBottomText;
    private TextView txtInstructions;
    private long startTime = 0, difference;
    private boolean startTimeFlag; /* Flag - making sure that the '1' button pressed once already and the
                                      the next time the user will press it cause nothing */

    // Instructions strings
    private final String INSTRUCTION_1 = "Press 1 to start \npress the \"high score\" bar to initialize high score";
    private final String INSTRUCTION_2 = "Running... \n(press 2 as fast as you can)";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing
        startTimeFlag = false;
        cmdLeft = (Button)findViewById(R.id.cmdLeft);
        cmdRight = (Button)findViewById(R.id.cmdRight);
        txtTopText = (TextView)findViewById(R.id.newScoreTime);
        txtBottomText = (TextView)findViewById(R.id.highScoreTime);
        txtInstructions = (TextView)findViewById(R.id.instructions);

        // Checking the bundle, if isn't null that means the user turned the phone state to landscape state.
        if(savedInstanceState != null)
        {
            difference = savedInstanceState.getLong("difference");
            cmdRight.setText(savedInstanceState.getString("rightButton"));
            cmdLeft.setText(savedInstanceState.getString("leftButton"));
            txtInstructions.setText(savedInstanceState.getString("instructions"));

            if(difference < Long.MAX_VALUE)
                txtTopText.setText(difference + "");
        }
        else // bundle is null -> the user just didn't change state yet.
        {
            randomButtons();
            txtInstructions.setText(INSTRUCTION_1);
        }

            // connecting the application buttons to the listener.
            cmdLeft.setOnClickListener(this);
            cmdRight.setOnClickListener(this);
            txtBottomText.setOnClickListener(this);
            txtBottomText.setOnLongClickListener(this);
            txtTopText.setOnLongClickListener(this);

            // Getting data from the shared data file.
            SharedPreferences prefs = getSharedPreferences("MinTime", MODE_PRIVATE);
            long value = prefs.getLong("MinDifference", Long.MAX_VALUE);

            // Provide printing of the max value of long type.
            if(value < Long.MAX_VALUE)
                txtBottomText.setText(value + "");
    }

    // Method for random the buttons numbers each time the app should do that.
    private void randomButtons()
    {
        Random rand = new Random();
        int randNum = rand.nextInt(2) + 1;
        if (randNum == 1) {
            cmdRight.setText("1");
            cmdLeft.setText("2");
        } else {
            cmdRight.setText("2");
            cmdLeft.setText("1");
        }
    }



    @Override
    public void onClick(View v)
    {
        if(v.getId() == txtBottomText.getId())
        {
            SharedPreferences prefs = getSharedPreferences("MinTime", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong("MinDifference", Long.MAX_VALUE);
            editor.apply();

            randomButtons();
            startTimeFlag = false;
            txtInstructions.setText(INSTRUCTION_1);
            txtBottomText.setText("");
        }
        else
        {
            Button button = (Button) v;

            if (button.getText() == "1") {
                if (!startTimeFlag) // Pressing the '1' button for the first time.
                {
                    startTime = System.currentTimeMillis();
                    startTimeFlag = true;
                    txtInstructions.setText(INSTRUCTION_2);
                }
            } else if (button.getText() == "2") {
                if (startTime != 0 && startTimeFlag) // Pressing the '2' button after pressing '1' button.
                {
                    difference = System.currentTimeMillis() - startTime;
                    txtTopText.setText(difference + "");
                    startTime = 0;
                    setMinTime(difference);
                    startTimeFlag = false;
                    txtInstructions.setText(INSTRUCTION_1);
                    randomButtons();
                }

            }
        }

    }

    // Sharing data between states.
    @Override
    protected void onSaveInstanceState(Bundle onState)
    {
        super.onSaveInstanceState(onState);

        txtInstructions.setText(INSTRUCTION_1);
        onState.putLong("difference",difference);
        onState.putString("rightButton",cmdRight.getText().toString());
        onState.putString("leftButton",cmdLeft.getText().toString());
        onState.putString("instructions", txtInstructions.getText().toString());
    }

     //check if the current difference is the minimum difference, and if so, it saved it.
    private void setMinTime(long difference)
    {
        SharedPreferences prefs = getSharedPreferences("MinTime",MODE_PRIVATE);

        long value = prefs.getLong("MinDifference", Long.MAX_VALUE);

        if(difference < value)
        {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong("MinDifference",difference);

            txtBottomText.setText(difference + "");
            editor.apply();
        }
        else
        {
            if(value < Long.MAX_VALUE)
                txtBottomText.setText(value + "");
        }

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


}
