package com.example.pickmymovie;

import java.util.ArrayList;
import java.util.UUID;

import com.example.pickmymovie.PickerFragment.PickerCallback;
import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.Toast;

/**
 * MainActivity is the spacestation from which all of the various fragment
 * starships are launched.
 * 
 * The Picker is initialized in the Main Activity. Picker is a singularity, the
 * only initialization happens right here and that is the picker for the whole
 * app.
 * 
 * @author jbruzek
 * 
 */
public class MainActivity extends Activity implements PickerCallback {

	private PickerFragment pickFrag;
	private MovieListFragment listFrag;
	private AddMovieFragment addFrag;
	private MovieFragment movieFrag;
	public final static String PICK_TAG = "PICKER";
	private Picker picker;
	private static final UUID PEBBLE_APP_UUID = UUID
			.fromString("55d1a5be-6197-4c4f-8c43-90ee8e2773e2");
	private static final int TRANSACTION_ID_KEY = 0x0;

	/**
	 * On creation of the activity Assign fragments create the picker.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		picker = new Picker();

		boolean connected = PebbleKit.isWatchConnected(getApplicationContext());
		Log.i(getLocalClassName(), "Pebble is "
				+ (connected ? "connected" : "not connected"));
		if (connected)
			Toast.makeText(this, "Your Pebble is connected!", Toast.LENGTH_SHORT).show();

		setupPebbleCommunication();

		if (getFragmentManager().findFragmentByTag(PICK_TAG) == null) {
			pickFrag = new PickerFragment();
			getFragmentManager().beginTransaction()
					.add(R.id.frame1, pickFrag, PICK_TAG).commit();
		}
	}

	/**
	 * set up communication with the pebble
	 * listen for a notification and call sendMovieToPebble()
	 */
	private void setupPebbleCommunication() {

		PebbleKit.registerReceivedDataHandler(this,
				new PebbleKit.PebbleDataReceiver(PEBBLE_APP_UUID) {

					@Override
					public void receiveData(Context context, int transactionId,
							PebbleDictionary data) {

						PebbleKit.sendAckToPebble(context, transactionId);

						int myTransactionId = Long.valueOf(
								data.getInteger(TRANSACTION_ID_KEY)).intValue();

						String messageString = "Received a message from the Pebble with myTransactionId == "
								+ myTransactionId;

						if (myTransactionId == 1) { // it always equals 1
							sendMovieToPebble();
						}
					}
				});
	}
	
	/**
     * Send a movie to the Pebble!
     */
    private void sendMovieToPebble() {
    	Movie m = picker.getRandomMyMovie();
    	
    	// Create Pebble dictionary and add movie choice
    	PebbleDictionary movieDict = new PebbleDictionary();
    	movieDict.addString(1, m.title());
    	movieDict.addString(2, m.genre());
    	
    	
    	//Send the PebbleDictionary to the Pebble Watch app with PEBBLE_APP_UUID
    	PebbleKit.sendDataToPebble(this, PEBBLE_APP_UUID, movieDict);
    	Log.i("Pebble", "Movie to Pebble sent.");
    }

	/**
	 * get the array of movies from the Loading Activity
	 */
	@Override
	public void onResume() {
		super.onResume();
		Intent intent = getIntent();
		picker.setMovies(intent.getExtras().getParcelableArrayList("movies"));
		// they're parcelable so they can be passed by intent
	}

	/**
	 * return the picker
	 */
	@Override
	public Picker getPicker() {
		return picker;
	}

	/**
	 * open the list activity
	 */
	@Override
	public void list() {
		// Create new fragment and transaction
		listFrag = new MovieListFragment();
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();

		// Replace whatever is in the fragment_container view with this
		// fragment,
		// and add the transaction to the back stack
		transaction.replace(R.id.frame1, listFrag);
		transaction.addToBackStack(null);

		// Commit the transaction
		transaction.commit();
	}

	/**
	 * open the movie activity, when someone selects a movie from the list
	 * fragment
	 */
	@Override
	public void openMovie(Movie movie) {
		// Create new fragment and transaction
		movieFrag = new MovieFragment(movie);
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();

		// Replace whatever is in the fragment_container view with this
		// fragment,
		// and add the transaction to the back stack
		transaction.remove(listFrag);
		transaction.replace(R.id.frame1, movieFrag);
		transaction.addToBackStack(null);

		// Commit the transaction
		transaction.commit();
	}

	/**
	 * open the add movie fragment
	 */
	@Override
	public void addMovieFragment() {
		// Create new fragment and transaction
		addFrag = new AddMovieFragment();
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();

		// Replace whatever is in the fragment_container view with this
		// fragment,
		transaction.remove(listFrag);
		transaction.replace(R.id.frame1, addFrag);
		transaction.addToBackStack(null);

		// Commit the transaction
		transaction.commit();

	}

	/**
	 * refresh the listFragment after a movie has been added or removed
	 */
	@Override
	public void refreshList() {
		// Create new fragment and transaction
		onBackPressed();
		onBackPressed();
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();

		// Replace whatever is in the fragment_container view with this
		// fragment,
		// do NOT add to backstack
		if (addFrag != null) {
			transaction.remove(addFrag);
		}
		if (movieFrag != null) {
			transaction.remove(movieFrag);
		}

		listFrag = new MovieListFragment();
		transaction.replace(R.id.frame1, listFrag);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	/**
	 * When the back button is pressed
	 */
	@Override
	public void onBackPressed() {
		if (getFragmentManager().getBackStackEntryCount() == 0) {
			this.finish();
		} else {
			getFragmentManager().popBackStack();

		}
	}
}
