package com.example.islam.movie1.DataBaseHelper;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Created by islam on 18/04/17.
 */

public class MovieProvider extends ContentProvider {
    public static final String LOG_TAG = MovieProvider.class.getSimpleName();

    public static final int FAVOURITES = 100;

    private static final UriMatcher sUriMatcher = createUriMatcher();
    private SQLiteOpenHelper mOpenHelper;

    /**
     * Create the {@code UriMatcher} for pointing records into the DB
     *
     * @return A {@code UriMatcher} instance
     */
    public static UriMatcher createUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIE, FAVOURITES);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    /**
     * Queries the DB for the data requested by the caller of the method
     *
     * @param uri           The Uri of the data queried, containing information about where to search
     * @param projection    An array of required columns from the table ({@code null} for all columns)
     * @param selection     The where clause
     * @param selectionArgs The arguments of the where clause
     * @param sortOrder     The order of sorting the movies
     * @return A {@code Cursor} instance with the data
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor cursor;

        switch (match) {
            case FAVOURITES:
                cursor = db.query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        int match = sUriMatcher.match(uri);

        switch (match) {
            case FAVOURITES:
                return MovieContract.MovieEntry.CONTENT_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri insertionUri;

        long insertedId;

        switch (match) {
            case FAVOURITES:
                insertedId = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                if (insertedId > 0) {
                    insertionUri = MovieContract.MovieEntry.buildMovieWithId(insertedId);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return insertionUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowsDeleted;

        if (selection == null) {
            selection = "1";
        }

        switch (match) {
            case FAVOURITES:
                rowsDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case FAVOURITES:
                rowsUpdated = db.update(
                        MovieContract.MovieEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, @NonNull ContentValues[] values) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int matcher = sUriMatcher.match(uri);

        switch (matcher) {
            case FAVOURITES: {
                db.beginTransaction();
                int count = 0;

                for (ContentValues item : values) {
                    long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, item);
                    if (_id != -1) {
                        count++;
                    }
                }
                db.setTransactionSuccessful();
                db.endTransaction();

                getContext().getContentResolver().notifyChange(uri, null);
                return count;
            }

            default:
                return super.bulkInsert(uri, values);
        }
    }
}
