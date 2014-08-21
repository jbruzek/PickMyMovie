package com.example.pickmymovie;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

/**
 * LoadingActivity is the launcher activity for the app
 * LoadingActivity creates a LoadingFragment. 
 * In the fragment, an AsyncTask queries the database
 * and loops through the results, adding them to the current 
 * list of movies. Once the task finished, the MainActivity is
 * launched
 * 
 * @author jbruzek
 *
 */
public class LoadingActivity extends Activity {

	private ProgressBar bar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);

		bar = (ProgressBar) findViewById(R.id.progress1);
		bar.setMax(10000);

		LoadingTask task = new LoadingTask(this);
		task.execute();
	}

	/**
	 * A dummy task that performs some (dumb) background work and proxies
	 * progress updates and results back to the Activity.
	 * 
	 * Note that we need to check if the callbacks are null in each method in
	 * case they are invoked after the Activity's and Fragment's onDestroy()
	 * method have been called.
	 */
	private class LoadingTask extends AsyncTask<Cursor, Integer, Boolean> {

		private Cursor cursor;
		private ArrayList<Movie> movieList;
		private DataBaseHelper DbHelper;
		private Context context;

		public LoadingTask(Context context) {
			this.context = context;
		}

		/**
		 * Create the databasee from the DataBaseHelper
		 */
		@Override
		protected void onPreExecute() {
			// Create and open the database.
			DbHelper = new DataBaseHelper(context);
			try {
				DbHelper.createDataBase();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				DbHelper.openDataBase();
			} catch (SQLException sqle) {
				throw sqle;
			}
			cursor = DbHelper.getCursor();
		}

		/**
		 * Note that we do NOT call the callback object's methods directly from
		 * the background thread, as this could result in a race condition.
		 */
		@Override
		protected Boolean doInBackground(Cursor... param) {
			int total = cursor.getCount();
			Log.w("cursor.getCount()", Integer.toString(cursor.getCount()));
			int margin = 10000 / total;
			movieList = new ArrayList<Movie>();

			cursor.moveToFirst();
			// do the stuff and report back to the home activity.
			for (int i = 0; i < cursor.getCount(); i++) {
					try {
						// I want people to be able to see my loading screen
						// It's too fast if I don't sleep
						Thread.sleep(10);
						// Shhh, only dreams now.
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Movie movie = new Movie();
					movie.setId(Integer.parseInt(cursor.getString(0)));
					movie.setName(cursor.getString(1));
					movie.setGenre(cursor.getString(2));
					movie.setImage(cursor.getString(3));
					movie.setRating(Integer.parseInt(cursor.getString(4)));
					// Adding movie to the list
					movieList.add(movie);
					publishProgress(i * margin);
					cursor.moveToNext();
			}
			return true;
		}

		/**
		 * cancel the thing
		 */
		@Override
		protected void onCancelled() {
			// nothing
		}

		/**
		 * update the activity
		 */
		protected void publishProgress(Integer progress) {
			bar.setProgress(progress);
		}

		/**
		 * publish the result
		 */
		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				Intent intent = new Intent(LoadingActivity.this,
						MainActivity.class);
				intent.putParcelableArrayListExtra("movies", movieList);
				startActivity(intent);
				finish();
			}
		}
	}
}
