package com.example.mymovieratingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

public class DirectorName extends Activity{
    private AutoCompleteTextView mDirectorName;
    private Button mFinishButton;


    /* Array containing names of directors */
static final String[] mDirectorNameData = new String[] {
        "David Lynch","Martin Scorsese","Joel and Ethan Coen",
        "Steven Soderbergh","Terrence Malick","Abbas Kiarostami",
        "Errol Morris","Hayao Miyazaki","David Cronenberg",
        "Terence Davies","Lukas Moodysson","Lynne Ramsay",
        "Bela Tarr","Wong Kar-wai",	"Pedro Almodovar",
        "Todd Haynes","Quentin Tarantino","Tsai Ming-Liang",
        "Aki Kaurismaki","Michael Winterbottom","Paul Thomas Anderson",
        "Michael Haneke","Walter Salles","Alexander Payne",
        "Spike Jonze","Aleksandr Sokurov","Ang Lee",
        "Michael Moore","Wes Anderson","Takeshi Kitano",
        "Richard Linklater","Gaspar Noé","Pavel Pawlikowski",
        "David O Russell","Larry and Andy Wachowski",
        "Samira Makhmalbaf", "Lars von Trier","Takashi Mike",
        "David Fincher","Gus Van Sant"
        };
@Override
public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.directorname);

        mDirectorName = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView1);
        mFinishButton = (Button) findViewById(R.id.button1);

	/* Create an array adapter and attach it to the
		mDirectorNameData array. Also specify the reference to
		the xml file that specifies the layout for each entry in
		the array adapter. */

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, mDirectorNameData);

        /* Attach the adapter to the autocomplete text view */

        mDirectorName.setAdapter(adapter);

        /* Click listener for the Finish button */

        mFinishButton.setOnClickListener(new OnClickListener() {
@Override
public void onClick(View arg0) {
        // TODO Auto-generated method stub
			/* Create an intent with source activity as
                      DirectorName and target activity as
                      MovieRatingActivity */

        Intent mIntent = new Intent(DirectorName.this, MovieRatingActivity.class);

        /* Create a new bundle object */
        Bundle mBundle = new Bundle();

			/* Specify the director name as a key and put it
			   into the bundle as a key value pair */

        mBundle.putString("key",
        mDirectorName.getText().toString());




        /* Attach the bundle to the intent */

        mIntent.putExtras(mBundle);

/* Send the intent as a result to the MovieRating
	   activity */
        setResult(Activity.RESULT_OK, mIntent);
        finish();
        }
        });
        }
        }
