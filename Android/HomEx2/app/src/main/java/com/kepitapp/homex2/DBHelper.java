package com.kepitapp.homex2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Locale;

/**
 * Created by Dudi and Tzuria on 15/12/2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "game.db";

    public DBHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);

    }

    // Creating the "Scores" table.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + GameContract.GameScores.TABLE_NAME + " (" +
        GameContract.GameScores.LEVEL + " INTEGER, " +
        GameContract.GameScores.COMPLEXITY + " INTEGER, " +
        GameContract.GameScores.BEST_RESULT + " INTEGER, " +
        GameContract.GameScores.RECENT_RESULT + " INTEGER, " +
        "PRIMARY KEY (" + GameContract.GameScores.LEVEL + ", " + GameContract.GameScores.COMPLEXITY + "));";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + GameContract.GameScores.TABLE_NAME);
        onCreate(db);
    }


}
