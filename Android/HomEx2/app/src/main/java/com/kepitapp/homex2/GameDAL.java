package com.kepitapp.homex2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Dudi and Tzuria on 15/12/2015.
 */
public class GameDAL {

    private DBHelper game_DB_Helper;

    public GameDAL(Context context){
        game_DB_Helper = new DBHelper(context);
    }

    public long updateBestResult(int level, int complexity, int bestResult)
    {
        if(this.getBestResult(level,complexity) != 0 && this.getBestResult(level,complexity) < bestResult)
            return 0;
        // Get DB
        SQLiteDatabase db = game_DB_Helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GameContract.GameScores.BEST_RESULT,bestResult);

        String whereQuery = GameContract.GameScores.LEVEL + "=" + level + " AND " + GameContract.GameScores.COMPLEXITY + "=" + complexity;
        int rows = db.update(GameContract.GameScores.TABLE_NAME,values,whereQuery,null);
        if(rows < 1)
        {
            return insertBest(level,complexity,bestResult);
        }
        db.close();
        return rows;
    }

    private long insertBest(int level, int complexity, double bestResult)
    {
        SQLiteDatabase db = game_DB_Helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GameContract.GameScores.LEVEL,level);
        values.put(GameContract.GameScores.COMPLEXITY,complexity);
        values.put(GameContract.GameScores.BEST_RESULT,bestResult);
        values.put(GameContract.GameScores.RECENT_RESULT,0);

        long successfulInsert = db.insert(GameContract.GameScores.TABLE_NAME,null,values);
        db.close();
        return successfulInsert;
    }

    public int getBestResult(int level, int complexity)
    {
        // Get DB
        SQLiteDatabase db = game_DB_Helper.getReadableDatabase();
        String query = "SELECT " + GameContract.GameScores.BEST_RESULT + " FROM " + GameContract.GameScores.TABLE_NAME
                + " WHERE " + GameContract.GameScores.LEVEL + "=" + level + " AND " + GameContract.GameScores.COMPLEXITY + "=" + complexity;
        // Get the only line that the configuration table contain.
        Cursor c = db.rawQuery(query, null);
        if(c.getCount() == 0)
            return 0;


        while (c.moveToNext()) {
            return c.getInt(c.getColumnIndex(GameContract.GameScores.BEST_RESULT));
        }
        db.close();
        return 0;
    }

    public long updateRecentResult(int level, int complexity, double recentResult)
    {
        // Get DB
        SQLiteDatabase db = game_DB_Helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GameContract.GameScores.RECENT_RESULT,recentResult);

        String whereQuery = GameContract.GameScores.LEVEL + "=" + level + " AND " + GameContract.GameScores.COMPLEXITY + "=" + complexity;
        int rows = db.update(GameContract.GameScores.TABLE_NAME,values,whereQuery,null);
        if(rows < 1)
        {
            return insertRecent(level,complexity,recentResult);
        }
        db.close();
        return rows;
    }

    private long insertRecent(int level, int complexity, double recentResult)
    {
        SQLiteDatabase db = game_DB_Helper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(GameContract.GameScores.LEVEL,level);
        values.put(GameContract.GameScores.COMPLEXITY,complexity);
        values.put(GameContract.GameScores.RECENT_RESULT,recentResult);

        return db.insert(GameContract.GameScores.TABLE_NAME,null,values);
    }

    public int getRecentResult(int level, int complexity)
    {
        // Get DB
        SQLiteDatabase db = game_DB_Helper.getReadableDatabase();

        // Get the only line that the configuration table contain.
        Cursor c = db.rawQuery("SELECT " + GameContract.GameScores.RECENT_RESULT + " FROM " + GameContract.GameScores.TABLE_NAME
                + " WHERE " + GameContract.GameScores.LEVEL + "=" + level + " AND " + GameContract.GameScores.COMPLEXITY + "=" + complexity, null);

        if(c.getCount() == 0)
        {
            return 0;
        }
        c.moveToNext();
        // Get the best result index from the table.
        int complexityIndex = c.getColumnIndex(GameContract.GameScores.RECENT_RESULT);

        // Return the best result value.
        return c.getInt(complexityIndex);
    }


    public Cursor getAllScores()
    {
        SQLiteDatabase db = game_DB_Helper.getReadableDatabase();
        String query = "SELECT * FROM " + GameContract.GameScores.TABLE_NAME;
        // Get the only line that the configuration table contain.
        Cursor c = db.rawQuery(query, null);
        if(c != null)
        {
            while(c.moveToNext())
            {
                int level = c.getInt(c.getColumnIndex(GameContract.GameScores.LEVEL));
            }
        }

        return c;
    }
}
