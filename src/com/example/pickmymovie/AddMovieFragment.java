package com.example.pickmymovie;

import com.example.pickmymovie.PickerFragment.PickerCallback;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.ContextWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Fragment that allows the user to add a movie to the database
 * @author jbruzek
 *
 */
public class AddMovieFragment extends Fragment implements OnClickListener {

	private EditText title;
	private EditText genre;
	private EditText rating;
	private EditText image;
	private Button button;
	private PickerCallback activity;
	private Picker picker;

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
	 * create the interface of the fragment
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.add_movie, container, false);

		title = (EditText) view.findViewById(R.id.inputTitle);
		genre = (EditText) view.findViewById(R.id.inputGenre);
		rating = (EditText) view.findViewById(R.id.inputRating);
		image = (EditText) view.findViewById(R.id.inputImage);
		button = (Button) view.findViewById(R.id.addMovie);
		button.setOnClickListener(this);

		return view;
	}

	/**
	 * handle the clicking of the "Add Movie" button
	 */
	@Override
	public void onClick(View arg0) {
		
		//if the fields are not filled in, create a toast
		if (title.getText().toString().equals("") || genre.getText().toString().equals("")
				|| rating.getText().toString().equals("") || image.getText().toString().equals("")) {
			
			Toast.makeText(getActivity(), "Fill in all fields first", Toast.LENGTH_SHORT).show();
			
		} else {
			
			Movie m = new Movie(title.getText().toString(), genre.getText()
					.toString(), image.getText().toString(),
					Integer.parseInt(rating.getText().toString()));
			picker.addMovie(m);

			// Add the movie to the database
			SQLiteDatabase db;
			String myPath = "/data/data/com.example.pickmymovie/databases/movieDatabase";
			db = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);
			ContentValues insertValues = new ContentValues();
			insertValues.put("title", m.title());
			insertValues.put("genre", m.genre());
			insertValues.put("image", m.image());
			insertValues.put("rating", m.rating());
			db.insert("movies", null, insertValues);
			db.close();

			activity.refreshList();
		}
	}

}
