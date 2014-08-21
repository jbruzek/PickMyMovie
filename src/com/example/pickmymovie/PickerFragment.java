package com.example.pickmymovie;

import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * pickerFragment is where the magic happens
 * this is where the user picks their movie
 * 
 * @author jbruzek
 *
 */
public class PickerFragment extends Fragment implements OnClickListener {

	/**
	 * interface to call back to the main activity
	 */
	static interface PickerCallback {
		Picker getPicker();
		void list();
		void openMovie(Movie m);
		void refreshList();
		void addMovieFragment();
	}
	
	private PickerCallback 	  activity;
	private Picker 			  picker;
    private Movie             currentMovie;
    private TextView          movieTitle;
    private TextView          movieGenre;
    private Spinner   		  genre;
    private TextView          movieNum;
    private Button            pick;
    private Button            addMovie;
    private ImageView         imageView;

    /**
     * initialize the interface
     */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.movie_picker,
				container, false);
		
		return view;
	}
	
	/**
	 * create the various buttons and stuff
	 */
	@Override
	public void onResume() {
		super.onResume();
		Activity view = getActivity();
		pick = (Button)view.findViewById(R.id.pick);
		addMovie = (Button)view.findViewById(R.id.addMovie);
		genre = (Spinner)view.findViewById(R.id.genre);
		movieTitle = (TextView)view.findViewById(R.id.movieTitle);
		movieGenre = (TextView)view.findViewById(R.id.movieGenre);
		movieGenre.setVisibility(4);
		movieNum = (TextView)view.findViewById(R.id.movieNum);
        movieNum.setVisibility(4);
		imageView = (ImageView)view.findViewById(R.id.imageView);
		
		pick.setOnClickListener(this);
		addMovie.setOnClickListener(this);
	}

	/**
	 * get the picker
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (PickerCallback)activity;
		picker = this.activity.getPicker();
	}

	/**
	 * Handles click events
	 */
	@Override
	public void onClick(View v) {
		if (v == pick) {

	        if (genre.getSelectedItem().equals("No Preference"))
	        {
	            currentMovie = picker.getRandomMyMovie();
	        }
	        else
	        {
	        	// get a movie that matches the selected genre
	            currentMovie = picker.getRandomMyMovie((String)genre.getSelectedItem());
	        }

	        movieTitle.setText(currentMovie.title());
	        movieGenre.setText(currentMovie.genre());
	        movieGenre.setVisibility(0);

	        if (currentMovie.rating() >= 0)
	        {
	            movieNum.setText(currentMovie.rating() + "% Rotten Tomatoes");
	            movieNum.setVisibility(0);
	        }
	        else
	            movieNum.setVisibility(4);

	        if (currentMovie.image() != null)
	        {
	            String url = currentMovie.image();
	            // Picasso works really well.
	            // Downloaded from here:
	            // http://square.github.io/picasso/
	            Picasso.with(getActivity().getApplicationContext()).load(url).resize(720, 425).centerInside().into(imageView);
	            imageView.setVisibility(0);
	        }
	        else
	        {
	            imageView.setVisibility(4);
	        }
		} else if (v == addMovie) {
			activity.list();
		}
	}

}
