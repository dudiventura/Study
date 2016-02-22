package com.kepitapp.finalproject;

import android.provider.BaseColumns;

/**
 * Created by Dudi and Tzuria on 21/2/2016.
 */
public class GameContract {

    public GameContract(){}

    public static abstract class GameScores implements BaseColumns {
        public static final String TABLE_NAME = "Scores";
        public static final String SCORE = "recent_result";
    }
}
