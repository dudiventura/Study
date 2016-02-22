package com.kepitapp.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dudi on 21/2/2016.
 */
public class GameDAL {
    private DBHelper game_DB_Helper;
    public GameDAL(Context context){
        game_DB_Helper = new DBHelper(context);
    }


    public LinkedList<Integer> getScores()
    {
        SQLiteDatabase db = game_DB_Helper.getReadableDatabase();
        String query = "SELECT * FROM " + GameContract.GameScores.TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        int i = 0;
        LinkedList<Integer> scoresList = new LinkedList<>();

        while(c.moveToNext())
        {
            scoresList.add(c.getInt(i));
        }
        return scoresList;
    }

    public void updateScoreTable(int score)
    {
        SQLiteDatabase db = game_DB_Helper.getWritableDatabase();
        String query = "SELECT * FROM " + GameContract.GameScores.TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        Boolean addingRequired = false;
        if(c.getCount() < 10)
        {
            addingRequired = true;
        }
        else{
            int i = 0;
            while(c.moveToNext())
            {
                if(c.getInt(i) < score)
                {
                    addingRequired = true;
                    String deleteQuery =  "DELETE FROM " +GameContract.GameScores.TABLE_NAME +
                            "WHERE "+GameContract.GameScores.SCORE +" < "+score +";";
                    db.execSQL(deleteQuery);
                }
            }
        }
        if(addingRequired)
        {
            String insertQuery = "INSERT INTO " + GameContract.GameScores.TABLE_NAME + " VALUES (" + score + ");";
            db.execSQL(insertQuery);
        }
        db.close();
    }
}
