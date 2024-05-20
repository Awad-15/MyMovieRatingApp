package com.example.mymovieratingapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;


public class DisplayReviewActivity extends Activity {
    /* Variable declaration */
    private TextView mMovieName;
    private TextView mMovieYear;
    private TextView mMovieDuraton;
    private TextView mMovieReview;
    private TextView mMovieDirector;
    private TextView mMovieStarring;
    private RatingBar mMovieRating;
    private TextView mMovieGenres;
    private String name;
    private String genre;
    private String year;
    private String duration;
    private String review;
    private String starcast;
    private String director;
    private double rating;
    private List<String> mReviewData = new ArrayList<String>();
    private com.example.mymovieratingapp.MovieRatingDataHelper mDataHelper;

    /* Variable initialization */
    @SuppressLint("Range")
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review);
        mMovieName = (TextView) findViewById(R.id.textView2);
        mMovieGenres = (TextView) findViewById(R.id.textView4);
        mMovieYear = (TextView) findViewById(R.id.textView6);
        mMovieDuraton = (TextView) findViewById(R.id.textView8);
        mMovieRating = (RatingBar) findViewById(R.id.ratingBar1);
        mMovieReview = (TextView) findViewById(R.id.textView10);
        mMovieDirector = (TextView) findViewById(R.id.textView12);
        mMovieStarring = (TextView) findViewById(R.id.textView14);


        /* Extract the id sent along with the intent */
        Intent mIntent = getIntent();
        Bundle mBundle = mIntent.getExtras();
        int id = mBundle.getInt("id");

        String columns[] = new String[] { "name" , "genre" , "year" , "duration" , "rating" , "review" , "starcast" , "director" };

        Uri movie = Uri.parse(

                "content://com.movierating.provider.Movie/reviews/"+(id+1));

        ContentResolver cr = getContentResolver();

        Cursor c = cr.query(movie, columns, null, null, null );

        if (c.moveToFirst()) {

            do {

                // Get the field values

                mReviewData.add(c.getString(c.getColumnIndex("name"))+";"
                        +c.getString(c.getColumnIndex("genre"))+";"
                        +c.getString(c.getColumnIndex("year"))+";"
                        +c.getString(c.getColumnIndex("duration"))+";"
                        +c.getDouble(c.getColumnIndex("rating"))+";"
                        +c.getString(c.getColumnIndex("review"))+";"
                        +c.getString(c.getColumnIndex("starcast"))+";"
                        +c.getString(c.getColumnIndex("director")));

            } while (c.moveToNext());

        }

        //Closes the cursor object

        if (c != null && !c.isClosed()) {

            c.close();}

	/* Separate the various fields from the fetched record by calling a
	   user-defined function, breakString(). Populate the widgets in the
	   Display Review activity with the values of the various fields. */

        breakString(mReviewData.toString());
        /* Initialize the values of the various fields */
        mMovieName.setText(name);
        mMovieGenres.setText(genre);
        mMovieYear.setText(year);
        mMovieDuraton.setText(duration);
        mMovieRating.setRating((float) rating);
        mMovieReview.setText(review);
        mMovieDirector.setText(director);
        mMovieStarring.setText(starcast);
    }
    /* The breakString() method extracts the values of the various fields from a record sent to it as a String parameter */

    public void breakString(String str){
        name=str.substring(1,str.indexOf(";"));
        str= str.substring(name.length()+2, str.length());
        genre = str.substring(0,str.indexOf(";"));
        str= str.substring(genre.length()+1, str.length());
        year = str.substring(0,str.indexOf(";"));
        str= str.substring(year.length()+1, str.length());
        duration = str.substring(0,str.indexOf(";"));
        str= str.substring(duration.length()+1, str.length());
        String str1 = str.substring(0,str.indexOf(";"));
        rating=Double.valueOf(str1);
        str= str.substring(str1.length()+1, str.length());
        review = str.substring(0,str.indexOf(";"));
        str= str.substring(review.length()+1, str.length());
        starcast = str.substring(0,str.indexOf(";"));
        str= str.substring(starcast.length()+1, str.length()-1);
        director = str;
    }


}

