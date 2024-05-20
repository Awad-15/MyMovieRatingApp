package com.example.mymovieratingapp;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;


public class ReviewListActivity extends ListActivity {
	/* Variable declarations */
	private ListView mRatingList;
	private List<String> mReviewData = new ArrayList<String>();
	private final int DELETE=1;
	private final int SHARE=2;
	private ArrayAdapter<String> mReview;
	private int lRecId;
	private com.example.mymovieratingapp.MovieRatingDataHelper mDataHelper;

	/* Override the onCreate() method */
	@SuppressLint("Range")
	@Override
	public void onCreate(Bundle savedInstanceState){
	super.onCreate(savedInstanceState);
	//setContentView(R.layout.reviewlist);

		String columns[] = new String[] { "name" };

		Uri movie = Uri.parse(

				"content://com.movierating.provider.Movie/reviews/");

		ContentResolver cr = getContentResolver();

		Cursor c = cr.query(movie, columns, null, null, null );

		if (c.moveToFirst()) {

			do {

				// Get the field values

				mReviewData.add(c.getString(c.getColumnIndex("name")));

			} while (c.moveToNext());

		}

		if (c != null && !c.isClosed()) {

			c.close();}


		/* Create an array adapter to initialize the list view */

	}

	/* Overriding onResume() method helps in updating the list view */
	@Override
	public void onResume(){
		super.onResume();
		refreshList();
	}
	/* Method updates the list view and also sets noreview as the layout if no data is available */

	@SuppressLint("Range")
	public void refreshList (){
		mReviewData.clear();
		String columns[] = new String[] {"name"};
		Uri movie = Uri.parse(
				"content://com.movierating.provider.Movie/reviews/");
		Cursor c = managedQuery(movie, columns, null, null, null);
		if (c.moveToFirst()) {
			do {
				// Get the field values
				mReviewData.add(c.getString(c.getColumnIndex("name")));	}
			while (c.moveToNext());
		}
		setLayout();
		if (c != null && !c.isClosed()) {
			c.close();}
	}

	//Sets the layout when the activity is started or resumed
	private AlertDialog setLayout() {
		if(!mReviewData.isEmpty()){
			setContentView(R.layout.reviewlist);
			mRatingList = (ListView)findViewById(android.R.id.list);
			mReview = new ArrayAdapter<String>(this,R.layout.list_item, mReviewData);
			setListAdapter(mReview);
			registerForContextMenu(getListView());
			mRatingList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
					// TODO Auto-generated method stub
					Intent mReviewListIntent = new
							Intent(ReviewListActivity.this,
							DisplayReviewActivity.class);

					Bundle mBundle = new Bundle();
					mBundle.putInt("id", arg2);
					mReviewListIntent.putExtras(mBundle);
					startActivity(mReviewListIntent);
				}
			});
		}else{

			setContentView(R.layout.noreview);
		}
		return null;
	}
	private AlertDialog showDeleteDialog (int recId){
		// TODO Auto-generated method stub
		lRecId=recId;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to delete this review?")
				.setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int id) {
						Uri movie = Uri.parse("content://com.movierating.provider.Movie/reviews/");

						getApplicationContext().getContentResolver().delete(movie, "rowid = (select max(rowid) from (select rowid from Review limit" + (lRecId+1)+"));", null);
						refreshList();
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		return alert;
	}




	/*This method consumes the menu.xml file for creating the options menu for this activity */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	//Creates the Context menu with the items Delete and Share.
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo){
		// TODO Auto-generated method stub
		menu.setHeaderTitle("Review");
		menu.add(Menu.NONE, DELETE, Menu.NONE, "Delete");
		menu.add(Menu.NONE, SHARE, Menu.NONE, "Share");
	}

	// Add functionality to the context menu items

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		int id = (int) getListAdapter().getItemId(info.position);

		/* Check which item was selected in ListView and take an action accordingly */
		switch (item.getItemId()) {
			case DELETE:
				AlertDialog alert = showDeleteDialog(id);
				alert.show();
				return(true);

			case SHARE:
				return(true);
		}
		return(super.onOptionsItemSelected(item));
	}


	//Functionality for the menu options

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		int itemId = item.getItemId();
		if (itemId == R.id.add) {/* Create an intent to launch the Movie Rating activity */
			Intent myIntent2 = new Intent(getApplicationContext(), MovieRatingActivity.class);
			startActivity(myIntent2);
			return (true);
		} else if (itemId == R.id.exit) {/* Close the app */
			System.exit(0);
			return (true);
		}
		return(super.onOptionsItemSelected(item));
	}

}

