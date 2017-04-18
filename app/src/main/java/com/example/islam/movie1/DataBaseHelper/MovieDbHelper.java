package com.example.islam.movie1.DataBaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.islam.movie1.Models.MovieModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by islam on 18/04/17.
 */

public class MovieDbHelper extends SQLiteOpenHelper {
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "movies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String createMoviesTable = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " ( "
                + MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY, "
                + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_TITLE + " TEXT, "
                + MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT, "
                + MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " REAL, "
                + MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT, "
                + MovieContract.MovieEntry.COLUMN_POSTER_URL + " TEXT, "
                + MovieContract.MovieEntry.COLUMN_COVER_URL + " TEXT" +
                ");";

        db.execSQL(createMoviesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }

    public void insertFavourite(Context context, MovieModel movieModel) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieModel.getId());
        contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movieModel.getTitle());
        contentValues.put(MovieContract.MovieEntry.COLUMN_COVER_URL, movieModel.getBackdropPath());
        contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_URL, movieModel.getPosterPath());
        contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movieModel.getOverview());
        contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movieModel.getReleaseDate());
        contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movieModel.getVoteAverage());
        context.getContentResolver().insert(
                MovieContract.MovieEntry.CONTENT_URI,
                contentValues
        );
    }

    public void deleteFavourite(Context context, Integer movieId) {
        context.getContentResolver().delete(
                MovieContract.MovieEntry.CONTENT_URI,
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{String.valueOf(movieId)}
        );
    }

    public List<MovieModel> getFavouriteMovies(Context context) {
        List<MovieModel> favourites = new ArrayList<>();
        Cursor cursor = context.getContentResolver()
                .query(
                        MovieContract.MovieEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null
                );
        if (cursor.moveToFirst()) {
            do {
                int movieId = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID));
                String title = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE));
                String overview = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW));
                String posterPath = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_URL));
                String backdropPath = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_COVER_URL));
                String releaseDate = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE));
                double voteAverage = cursor.getDouble(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE));

                favourites.add(new MovieModel(movieId, title, overview, posterPath, backdropPath, releaseDate, voteAverage, true));
            } while (cursor.moveToNext());
        }
        return favourites;
    }

    public boolean isFavourite(Context context, Integer movieId) {
        Cursor cursor = context.getContentResolver()
                .query(
                        MovieContract.MovieEntry.CONTENT_URI,
                        null,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[]{String.valueOf(movieId)},
                        null
                );
        boolean isFavourite = cursor.moveToFirst();
        cursor.close();
        return isFavourite;
    }
}
