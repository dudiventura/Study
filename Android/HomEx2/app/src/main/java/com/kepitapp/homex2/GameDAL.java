package com.kepitapp.homex2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Dudi on 15/12/2015.
 */
public class GameDAL {

    private DBHelper game_DB_Helper;
    private final int DEFAULT_LEVEL = 1;
    private final int DEFAULT_COMPLEXITY = 0;

    public GameDAL(Context context){
        game_DB_Helper = new DBHelper(context);
    }
    public void initializeDefaultValues()
    {
        //get DB
        SQLiteDatabase db = game_DB_Helper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + GameContract.GameConfiguration.TABLE_NAME, null);
        if(c != null) {

            //values to save
            ContentValues values = new ContentValues();
            values.put(GameContract.GameConfiguration.LEVEL, DEFAULT_LEVEL);
            values.put(GameContract.GameConfiguration.COMPLEXITY, DEFAULT_COMPLEXITY);

            //save the values
            db.insert(GameContract.GameConfiguration.TABLE_NAME, null, values);
        }
    }

    public int getComplexity()
    {
        // Get DB
        SQLiteDatabase db = game_DB_Helper.getReadableDatabase();

        // Get the only line that the configuration table contain.
        Cursor c = db.rawQuery("SELECT * FROM " + GameContract.GameConfiguration.TABLE_NAME, null);

        // Get the complexity index from the table.
        int complexityIndex = c.getColumnIndex(GameContract.GameConfiguration.COMPLEXITY);

        // Return the complexity value.
        return c.getInt(complexityIndex);
    }

    public int getLevel()
    {
        // Get DB
        SQLiteDatabase db = game_DB_Helper.getReadableDatabase();

        // Get the only line that the configuration table contain.
        Cursor c = db.rawQuery("SELECT * FROM " + GameContract.GameConfiguration.TABLE_NAME, null);

        // Get the level index from the table.
        int complexityIndex = c.getColumnIndex(GameContract.GameConfiguration.LEVEL);

        // Return the level value.
        return c.getInt(complexityIndex);
    }

    public int getBestResult()
    {
        // Get DB
        SQLiteDatabase db = game_DB_Helper.getReadableDatabase();

        // Get the only line that the configuration table contain.
        Cursor c = db.rawQuery("SELECT * FROM " + GameContract.GameConfiguration.TABLE_NAME, null);

        // Get the best result index from the table.
        int complexityIndex = c.getColumnIndex(GameContract.GameConfiguration.BEST_RESULT);

        // Return the best result value.
        return c.getInt(complexityIndex);
    }

    public int getRecentResult()
    {
        // Get DB
        SQLiteDatabase db = game_DB_Helper.getReadableDatabase();

        // Get the only line that the configuration table contain.
        Cursor c = db.rawQuery("SELECT * FROM " + GameContract.GameConfiguration.TABLE_NAME, null);

        // Get the recent result index from the table.
        int complexityIndex = c.getColumnIndex(GameContract.GameConfiguration.RECENT_RESULT);

        // Return the recent result value.
        return c.getInt(complexityIndex);
    }

}
