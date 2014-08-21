package com.example.pickmymovie;

import com.example.pickmymovie.PickerFragment.PickerCallback;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Displays a movie, and allows for deletion of the movie
 * 
 * @author jbruzek
 *
 */
public class MovieFragment extends Fragment implements OnClickListener {

	private TextView title;
	private TextView genre;
	private TextView rating;
	private ImageView image;
	private Button button;
	private Movie movie;
	private Picker picker;
	private PickerCallback activity;

	/**
	 * set the movie for this fragment instance
	 */
	public MovieFragment(Movie m) {
		movie = m;
	}

	/**
	 * get the picker
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (PickerCallback) activity;
		picker = this.activity.getPicker();
	}

	/**
	 * initialize the interface
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.moviefragment, container, false);

		title = (TextView) view.findViewById(R.id.mTitle);
		title.setText(movie.title());
		genre = (TextView) view.findViewById(R.id.mGenre);
		genre.setText(movie.genre());
		rating = (TextView) view.findViewById(R.id.mRating);
		rating.setText(movie.rating() + "% IMDB");
		image = (ImageView) view.findViewById(R.id.mImage);
		// Picasso loads the image from a URL
		Picasso.with(getActivity().getApplicationContext()).load(movie.image())
				.resize(720, 425).centerInside().into(image);
		button = (Button) view.findViewById(R.id.mRemove);
		button.setOnClickListener(this);

		return view;
	}

	/**
	 * prompts the user if they really want to delete a movie
	 * If so, removes it from the current list AND the database
	 */
	@Override
	public void onClick(View arg0) {
		new AlertDialog.Builder(getActivity())
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("Are you sure you want to delete this Movie?")
				.setMessage("This action cannot be undone")
				.setPositiveButton("Delete",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								picker.removeMovie(movie);
								deleteMovie(movie);
								activity.refreshList();
							}

						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// nothing, they cancelled
							}

						}).show();
	}

	/**
	 * delete a movie from the database
	 * @param m
	 */
	private void deleteMovie(Movie m) {
		// Delete the movie from the database
		SQLiteDatabase db;
		String myPath = "/data/data/com.example.pickmymovie/databases/movieDatabase";
		db = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
		String[] args = new String[1];
		args[0] = m.title();
		db.delete("movies", "title=?", args);
		db.close();
	}

}
