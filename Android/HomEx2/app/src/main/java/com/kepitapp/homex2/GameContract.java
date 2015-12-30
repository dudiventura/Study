package com.kepitapp.homex2;

import android.provider.BaseColumns;

/**
 * Created by Dudi on 15/12/2015.
 */
public class GameContract {
    public GameContract(){}

    public static abstract class GameScores implements BaseColumns{
        public static final String TABLE_NAME = "Scores";
        public static final String LEVEL = "level";
        public static final String COMPLEXITY = "complexity";
        public static final String BEST_RESULT = "best_result";
        public static final String RECENT_RESULT = "recent_result";
    }
}
