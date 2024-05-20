package com.example.mymovieratingapp;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.Context;
import android.content.UriMatcher;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.text.TextUtils;
import android.util.Log;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class MovieRatingContentProvider extends ContentProvider {

    /* Declare and initialize constants and variables */
    public static final String _ID = "id";
    public static final String NAME = "name";

    private static final String TAG = "MovieRatingContentProvider";

    private static final String DATABASE_NAME = "MovieRating.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Review";

    public static final String PROVIDER_NAME =
            "com.movierating.provider.Movie";
    public static final Uri CONTENT_URI =
            Uri.parse("content://"+ PROVIDER_NAME + "/reviews");

    /* Declare a constant to identify a URI that returns all records in the Review table */

    private static final int REVIEWALL = 1;

    /* Declare a constant to identify a URI that returns a record at a specific ID in a table */

    private static final int REVIEW_ID = 2;

    private SQLiteDatabase reviewsDB;

    /* Create a class that extends the SQLiteOpenHelper class */

    private static class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME + "(id INTEGER PRIMARY KEY, name TEXT, genre TEXT, year TEXT, duration TEXT, rating DOUBLE, review TEXT, starcast TEXT, director TEXT)");
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion) {
            Log.w("Example", "Upgrading database, this will drop tables and recreate.");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    private OpenHelper dbHelper;

    /* Override the delete() method to delete the required records from the Review database */

    @Override
    public int delete(Uri arg0, String arg1, String[] arg2) {
        // TODO Auto-generated method stub
        int count=0;

        /* Check whether the request is for deleting all records or a single record. Accordingly take the appropriate action. */

        switch (uriMatcher.match(arg0)){
            case REVIEWALL:
                count = reviewsDB.delete(TABLE_NAME,arg1,arg2);
                break;
            case REVIEW_ID:
                String id = arg0.getPathSegments().get(1);
                count = reviewsDB.delete(
                        TABLE_NAME, _ID + " = " + id +
                                (!TextUtils.isEmpty(arg1) ? " AND (" +
                                        arg1 + ')' : ""), arg2);
                break;
            default: throw new IllegalArgumentException(
                    "Unknown URI" + arg0);
        }


        getContext().getContentResolver().notifyChange(arg0, null);
        return count;
    }

    /* Override the getType() method to return the MIME type of the data */

    @Override
    public String getType(Uri arg0) {
        // TODO Auto-generated method stub
        switch (uriMatcher.match(arg0)){
            //---get all reviews---
            case REVIEWALL:
                return "vnd.android.cursor.dir/vnd.movierating.reviews";
            //---get a particular review---
            case REVIEW_ID:
                return "vnd.android.cursor.item/vnd.movierating.reviews";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + arg0);
        }
    }

    /* Override the insert() method to insert the required record in the database */

    @Override
    public Uri insert(Uri arg0, ContentValues arg1) {
        // TODO Auto-generated method stub

        long rowID = reviewsDB.insert(
                TABLE_NAME, "", arg1);

        //---if added successfully---

        if (rowID>0)
        {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }


        throw new SQLException("Failed to insert row into " + arg0);
    }

    /* Override the onCreate() method */

    @Override
    public boolean onCreate() {
        // TODO Auto-generated method stub
        Context context = getContext();
        OpenHelper dbHelper = new OpenHelper(context);
        reviewsDB = dbHelper.getWritableDatabase();
        return (reviewsDB == null)? false:true;
    }

    /* Override the query() method to query the database */

    @Override
    public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3,
                        String arg4) {
        // TODO Auto-generated method stub
        SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
        sqlBuilder.setTables(TABLE_NAME);

        if (uriMatcher.match(arg0) == REVIEW_ID)
            //---if getting a particular book---
            sqlBuilder.appendWhere(
                    _ID + " = " + arg0.getPathSegments().get(1));

        Cursor c = sqlBuilder.query(reviewsDB, arg1, arg2, arg3, null, null, arg4);

        //---register to watch a content URI for changes---
        c.setNotificationUri(getContext().getContentResolver(), arg0);
        return c;
    }

    /* Override the update method to update the required records */

    @Override
    public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
        // TODO Auto-generated method stub
        int count = 0;
        switch (uriMatcher.match(arg0)){
            case REVIEWALL:
                count = reviewsDB.update(
                        TABLE_NAME,
                        arg1,
                        arg2,
                        arg3);
                break;
            case REVIEW_ID:
                count = reviewsDB.update(
                        TABLE_NAME,
                        arg1,
                        _ID + " = " + arg0.getPathSegments().get(1) +
                                (!TextUtils.isEmpty(arg2) ? " AND (" +
                                        arg2 + ')' : ""),
                        arg3);
                break;
            default: throw new IllegalArgumentException(
                    "Unknown URI " + arg0);
        }

        /* Notify registered observers that a row was updated */

        getContext().getContentResolver().notifyChange(arg0, null);
        return count;
    }

    /* Add available URIs to the URIMatcher object */

    private static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "reviews", REVIEWALL);
        uriMatcher.addURI(PROVIDER_NAME, "reviews/#", REVIEW_ID);
    }
}