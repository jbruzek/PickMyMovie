package com.example.pickmymovie;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Movie is a neat little bundle of data that holds the information
 * that one movie has. Namely the title, genre, image, and rating
 * 
 * Movie implements Parcelable so that an ArrayList of movies
 * can be passed by intent to the Main Activity
 * 
 * @author jbruzek
 *
 */
public class Movie implements Parcelable {

	private String title = "";
	private String genre = "other";
	private String image = "";
	private int rating = -1;
	private int id = 0;

	/**
	 * Create a movie with no parameters at all! Spooky
	 */
	public Movie() {
		// Absolutely Nothing!!!
		// MUAHAHAHAAAHAAHAA
	}
	
	/**
	 * constructor that takes a Parcel
	 */
	public Movie(Parcel in) {
		readFromParcel(in);
	}

	/**
	 * Create a movie with an id, name, and a genre
	 */
	public Movie(int id, String name, String genre) {
		this.id = id;
		title = name;
		this.genre = genre;
	}

	/**
	 * create a movie object with a name and a genre
	 */
	public Movie(String name, String genre) {
		title = name;
		this.genre = genre;
		image = "";
	}

	/**
	 * Create a new Movie object with a name, genre, and image
	 */
	public Movie(String name, String genre, String image) {
		title = name;
		this.genre = genre;
		this.image = image;
	}

	/**
	 * Create a new Movie object with a name, genre, image, and rating
	 */
	public Movie(String name, String genre, String image, int rating) {
		title = name;
		this.genre = genre;
		this.image = image;
		this.rating = rating;
	}
	
	

	/**
	 * set the id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * get the id
	 */
	public int Id() {
		return id;
	}

	/**
	 * set the name of the movie
	 */
	public void setName(String name) {
		title = name;
	}

	/**
	 * set the genre of the movie
	 */
	public void setGenre(String genre) {
		this.genre = genre;
	}

	/**
	 * set the image url of the movie
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * get the name of the movie
	 */
	public String title() {
		return title;
	}

	/**
	 * get the genre of the movie
	 */
	public String genre() {
		return genre;
	}

	/**
	 * get the image url of the movie
	 */
	public String image() {
		return image;
	}

	/**
	 * set the rating variable
	 */
	public void setRating(int rating) {
		this.rating = rating;
	}

	/**
	 * get the rating of the movie
	 */
	public int rating() {
		return rating;
	}
	
	/**
	 * describe the contents.
	 * nothing
	 */
	@Override
	public int describeContents() {
		return 0;
	}
	
	/**
	 * write to a parcel
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		String[] values = new String[3];
		values[0] = title;
		values[1] = genre;
		values[2] = image;
		dest.writeStringArray(values);

		int[] ints = new int[2];
		ints[0] = rating;
		ints[1] = id;
		dest.writeIntArray(ints);
	}
	
	/**
	 * read the parcel
	 */
	private void readFromParcel(Parcel in) {
		String[] values = new String[3];
		in.readStringArray(values);
		title = values[0];
		genre = values[1];
		image = values[2];
		int[] ints = new int[2];
		in.readIntArray(ints);
		rating = ints[0];
		id = ints[1];
	}
	
	/**
	 * This field is needed for Android to be able to
	 * create new objects, individually or as arrays. 
	 * This also means that you can use use the default 
	 * constructor to create the object and use another 
	 * method to hyrdate it as necessary. 
	 */
	public static final Parcelable.Creator CREATOR = 
		new Parcelable.Creator() { 
			public Movie createFromParcel(Parcel in) { 
				return new Movie(in); 
			}   
		
			public Movie[] newArray(int size) { 
				return new Movie[size]; 
			} 
		};
}








