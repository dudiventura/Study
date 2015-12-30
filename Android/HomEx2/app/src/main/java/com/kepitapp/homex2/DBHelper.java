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

    @Override
    public void onCreate(SQLiteDatabase db) {
String quiry = "CREATE TABLE " + GameContract.GameConfiguration.TABLE_NAME + " (" +
        GameContract.GameConfiguration.LEVEL + " INTEGER PRIMARY KEY, " +
        GameContract.GameConfiguration.COMPLEXITY + "INTEGER, " +
        GameContract.GameConfiguration.BEST_RESULT + "INTEGER, " +
        GameContract.GameConfiguration.RECENT_RESULT + "INTEGER"+
        ");";
        Log.e("Chen", quiry);
        db.execSQL(quiry

        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + GameContract.GameConfiguration.TABLE_NAME);
        onCreate(db);
    }
}
