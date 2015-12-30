package com.kepitapp.homex2;

import android.app.AlertDialog;
import android.content.Intent;
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
    private SharedPreferences prefs;

    // Warning message for back pressing.
    private final String WARNING = "Pressing back button will take back without saving changes.\nPress the 'Save Changes' button to save";
    private boolean firstBackPress = true; // flag - making sure the back button pressed once and the warning messaged has been popped.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        numpickLevel = (NumberPicker)findViewById(R.id.numpickerLevel);
        numpickComplexity = (NumberPicker)findViewById(R.id.numpickerComplexity);
        okButton = (Button)findViewById(R.id.btnOK);

        prefs = getSharedPreferences("LevelAndComplexity", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Put limits to level number picker
        numpickLevel.setMinValue(MIN_LEVEL);
        numpickLevel.setMaxValue(MAX_LEVEL);

        // Put limits to complexity number picker
        numpickComplexity.setMinValue(MIN_COMPLEXITY);
        numpickComplexity.setMaxValue(MAX_COMPLEXITY);

        // Getting the level and the complexity from the last using.
        int level = prefs.getInt("level", MIN_LEVEL);
        int complexity = prefs.getInt("complexity", MIN_COMPLEXITY);

        // Start the scroll bars on the level and complexity from the last using.
        numpickLevel.setValue(level);
        numpickComplexity.setValue(complexity);

        // Connect the number pickers and the ok button to the onClick method.
        numpickLevel.setOnClickListener(this);
        numpickComplexity.setOnClickListener(this);
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
        if(firstBackPress) // if pressed the back button for the first time show warning message
        {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setMessage(WARNING);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
            firstBackPress = false;
        }
        else // if press the back button again, going back to the main activity without save any change.
        {
            finish();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

    }

    // Pressing the ok button will save the settings changes and go back to the main activity, also provides and action of clicking the number pickers.
    @Override
    public void onClick(View v) {
        if(v.getId() == okButton.getId()) {
            prefs = getSharedPreferences("LevelAndComplexity", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            editor.putInt("level", numpickLevel.getValue());
            editor.putInt("complexity", numpickComplexity.getValue());
            editor.apply();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}
