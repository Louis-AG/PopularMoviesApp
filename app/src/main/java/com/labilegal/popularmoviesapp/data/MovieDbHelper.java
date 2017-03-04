package com.labilegal.popularmoviesapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by louisag on 01/03/2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movies.db";

    private static final int DATABASE_VERSION = 2;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_WEATHER_TABLE =
                "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME+ " (" +

                        MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        MovieContract.MovieEntry.COLUNM_MOVIE_ID  + " INTEGER NOT NULL , " +

                        MovieContract.MovieEntry.COLUNM_MOVIE_TITLE + " TEXT NOT NULL ,"+

                        MovieContract.MovieEntry.COLUNM_MOVIE_POSTER_PATH + " TEXT , "+

                        MovieContract.MovieEntry.COLUNM_MOVIE_OVERVIEW + " TEXT , "+

                        MovieContract.MovieEntry.COLUNM_MOVIE_RELEASE_DATE + " TEXT , "+

                        MovieContract.MovieEntry.COLUNM_MOVIE_VOTE_AVERAGE + " TEXT "+

                        ");";

        db.execSQL(SQL_CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
