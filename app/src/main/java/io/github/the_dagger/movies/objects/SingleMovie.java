package io.github.the_dagger.movies.objects;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Harshit on 1/6/2016.
 */
public class SingleMovie implements Parcelable {
    public String movieImage;
    public String movieTitle;
    public String movieOverView;
    public String movieRating;
    public String movieReleaseDate;
    public String id;
    public String movieBackDropImage;
    public String language;

    SingleMovie(){
        movieImage = null;
        movieTitle = "Constructor was called";
    }

    public SingleMovie(String image, String title, String overView, String rating, String releaseDate, String backDropImage, String id, String language) {
        this.movieImage = image;
        this.movieTitle = title;
        this.movieOverView = overView;
        this.movieRating = rating;
        this.movieReleaseDate = releaseDate;
        this.movieBackDropImage = backDropImage;
        this.id = id;
        this.language = language;
    }

    public SingleMovie(Parcel in) {
        this.movieImage = in.readString();
        this.movieTitle = in.readString();
        this.movieOverView = in.readString();
        this.movieRating = in.readString();
        this.movieReleaseDate = in.readString();
        this.movieBackDropImage = in.readString();
        this.id = in.readString();
        this.language = in.readString();
    }

    public static final Creator<SingleMovie> CREATOR = new Creator<SingleMovie>() {
        @Override
        public SingleMovie createFromParcel(Parcel in) {
            return new SingleMovie(in);
        }

        @Override
        public SingleMovie[] newArray(int size) {
            return new SingleMovie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieImage);
        dest.writeString(movieTitle);
        dest.writeString(movieOverView);
        dest.writeString(movieRating);
        dest.writeString(movieReleaseDate);
        dest.writeString(movieBackDropImage);
        dest.writeString(id);
        dest.writeString(language);
    }

    public static SingleMovie fromCursor(Cursor cursor) {
        SingleMovie singleMovie = new SingleMovie();
        return singleMovie;
    }
}
