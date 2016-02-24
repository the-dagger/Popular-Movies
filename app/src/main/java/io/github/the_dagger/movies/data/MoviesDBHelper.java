package io.github.the_dagger.movies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Harshit on 2/24/2016.
 */
public class MoviesDBHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = MoviesDBHelper.class.getSimpleName();

    //name & version
    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;


    public MoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                MoviesContract.MovieEntry.TABLE_MOVIES + "(" + MoviesContract.MovieEntry._ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MoviesContract.MovieEntry.MOVIE_ID + " TEXT NOT NULL, " +
                MoviesContract.MovieEntry.COLUMN_NAME +
                " TEXT NOT NULL, " +
                MoviesContract.MovieEntry.COLUMN_BACKDROP +
                " INTEGER, " +
                MoviesContract.MovieEntry.COLUMN_DESCRIPTION +
                " TEXT, " +
                MoviesContract.MovieEntry.COLUMN_RELEASE_DATE +
                " TEXT, " +
                MoviesContract.MovieEntry.COLUMN_POSTER +
                " INTEGER);";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " +
                newVersion + ". OLD DATA WILL BE DESTROYED");
        // Drop the table
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MovieEntry.TABLE_MOVIES);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                MoviesContract.MovieEntry.TABLE_MOVIES + "'");

        // re-create database
        onCreate(db);

    }
}
