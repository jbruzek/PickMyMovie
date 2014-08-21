package com.example.pickmymovie;

import java.io.InputStream;
import java.net.URL;

import com.example.pickmymovie.PickerFragment.PickerCallback;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Display a list of all the movies you currently have, plus one item that 
 * allows the user to add a new movie
 * 
 * @author jbruzek
 *
 */
public class MovieListFragment extends ListFragment {

	private PickerCallback activity;
	private Picker picker;
	private Movie[] movies;

	/**
	 * when an item is clicked, open the movieFragment for that movie,
	 * or open the AddMovieFragment
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if (position == (movies.length - 1)) {
			activity.addMovieFragment();
		} else {
			activity.openMovie(movies[position]);
		}
	}

	/**
	 * initialize the interface
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MovieAdapter adapter = new MovieAdapter(inflater.getContext(), movies);
		setListAdapter(adapter);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	/**
	 * get the picker, add the "Add Movie" item to the list
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (PickerCallback) activity;
		picker = this.activity.getPicker();

		movies = picker.getMovies().toArray(
				new Movie[picker.getMovies().size() + 1]);
		movies[movies.length - 1] = new Movie("Add New Movie", "",
				"http://www.clker.com/cliparts/L/q/T/i/P/S/add-button-white-md.png");
	}

	/**
	 * an adapter that populates the individual list items
	 * 
	 * @author jbruzek
	 *
	 */
	private class MovieAdapter extends ArrayAdapter<Movie> {
		private final Context context;
		private final Movie[] values;

		/**
		 * create the adapter
		 * @param context
		 * @param values
		 */
		public MovieAdapter(Context context, Movie[] values) {
			super(context, R.layout.list_item, values);
			this.context = context;
			this.values = values;
		}

		/**
		 * initialize the item interface
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.list_item, parent, false);
			TextView title = (TextView) rowView.findViewById(R.id.list_title);
			TextView genre = (TextView) rowView.findViewById(R.id.list_genre);
			ImageView imageView = (ImageView) rowView
					.findViewById(R.id.list_pic);
			title.setText(values[position].title());
			genre.setText(values[position].genre());
			Picasso.with(getActivity().getApplicationContext())
					.load(values[position].image()).resize(100, 100)
					.centerInside().into(imageView);
			return rowView;
		}
	}

}
