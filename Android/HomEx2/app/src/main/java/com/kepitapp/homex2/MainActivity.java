package com.kepitapp.homex2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private float m_BestScore;
    private float m_CurrentScore;
    private int m_Level;
    private int m_Complexity;
    private Button startButton, settingsButton;

    public GameDAL DAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = (Button)findViewById(R.id.btnStart);
        settingsButton = (Button)findViewById(R.id.btnSettings);

        startButton.setOnClickListener(this);
        settingsButton.setOnClickListener(this);

        DAL = new GameDAL(this);

       // initialize defaults values on data base
        DAL.initializeDefaultValues();

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
        if(v.getId() == startButton.getId()) {
            //TO DO!!
        }
        else if(v.getId() == settingsButton.getId())
        {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        }
    }

    @Override
    public void onBackPressed(){

    }
}
