package com.kepitapp.homex2;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;


public class SettingsActivity extends ActionBarActivity implements View.OnClickListener {

    private final int MIN_LEVEL = 1, MAX_LEVEL = 10, MIN_COMPLEXITY = 0, MAX_COMPLEXITY = 4;
    private NumberPicker numpickLevel, numpickComplexity;
    private Button okButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        numpickLevel = (NumberPicker)findViewById(R.id.numpickerLevel);
        numpickComplexity = (NumberPicker)findViewById(R.id.numpickerComplexity);
        okButton = (Button)findViewById(R.id.btnOK);

        // Put limits to level number picker
        numpickLevel.setMinValue(MIN_LEVEL);
        numpickLevel.setMaxValue(MAX_LEVEL);

        // Put limits to complexity number picker
        numpickComplexity.setMinValue(MIN_COMPLEXITY);
        numpickComplexity.setMaxValue(MAX_COMPLEXITY);

        okButton.setOnClickListener(this);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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
    public void onBackPressed(){

    }

    @Override
    public void onClick(View v) {
        SharedPreferences prefs = getSharedPreferences("LevelAndComplexity", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("level", numpickLevel.getValue());
        editor.putInt("complexity", numpickComplexity.getValue());
        editor.apply();
    }
}
