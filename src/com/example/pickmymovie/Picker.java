package com.example.pickmymovie;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import android.os.Parcelable;
import android.util.Log;

// -------------------------------------------------------------------------
/**
 * Model for the App The Picker handles the list of movies as well as chosing
 * random movies
 * 
 * @author Joe
 * @version August 9 2014
 */
public class Picker {
	private ArrayList<Parcelable> movies;

	private int movieNumOld = 0;
	ArrayList<Integer> collection;

	/**
	 * Create a new MoviePicker object.
	 */
	public Picker() {
		movies = new ArrayList<Parcelable>();
		collection = new ArrayList<Integer>();
	}

	/**
	 * set the movie list
	 */
	public void setMovies(ArrayList<Parcelable> arrayList) {
		this.movies = arrayList;
	}

	/**
	 * return the array with my movies
	 * 
	 * @return the array of movies
	 */
	public ArrayList<Parcelable> getMovies() {
		return movies;
	}

	/**
	 * return the size of my movies array
	 * 
	 * @return the size of the myMovies array
	 */
	public int getMoviesSize() {
		return movies.size();
	}

	/**
	 * get a random one of my movies with no genre parameter
	 * 
	 * @return the random movie
	 */
	public Movie getRandomMyMovie() {
		int movieNum = new Random().nextInt(movies.size());
		return (Movie) movies.get(movieNum);
	}

	/**
	 * get a random movie from my movies with a genre parameter
	 * 
	 * @param genre
	 *            to pick from
	 * @return the movie
	 */
	public Movie getRandomMyMovie(String genre) {
		Log.w("movies", String.valueOf(movies.size()));
		Movie[] temp = new Movie[movies.size()];
		int count = 0;

		int movieNum = 0;
		for (int i = 0; i < movies.size(); i++) {
			if (((Movie) movies.get(i)).genre().equals(genre)) {
				temp[count] = (Movie) movies.get(i);
				count++;
			}
		}
		
		// make sure it sycles through all of the movies
		while (true) {

			movieNum = new Random().nextInt(count);

			if (!collection.contains(movieNum))
				break;
			if (collection.size() == count) {
				collection.clear();
				break;
			}
		}
		collection.add(movieNum);
		return temp[movieNum];
	}

	/**
	 * add a movie to the list of movies
	 */
	public void addMovie(Movie movie) {
		movies.add(movie);
	}

	/**
	 * remove a movie
	 */
	public void removeMovie(Movie m) {
		for (int i = 0; i < movies.size(); i++) {
			if (((Movie) movies.get(i)).title() == m.title()) {
				movies.remove(i);
			}
		}
	}
}
