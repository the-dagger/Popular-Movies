package io.github.the_dagger.movies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Harshit on 1/6/2016.
 */
public class SingleMovie implements Parcelable {
    String movieImage;
    String movieTitle;
    String movieOverView;
    String movieRating;
    String movieReleaseDate;
    String id;
    String movieBackDropImage;
    String language;

    public SingleMovie(String image, String title, String overView, String rating, String releaseDate, String backDropImage,String id,String language) {
        this.movieImage = image;
        this.movieTitle = title;
        this.movieOverView = overView;
        this.movieRating = rating;
        this.movieReleaseDate = releaseDate;
        this.movieBackDropImage = backDropImage;
        this.id = id;
        this.language=language;
    }

    public SingleMovie(Parcel in) {
        this.movieImage = in.readString();
        this.movieTitle = in.readString();
        this.movieOverView = in.readString();
        this.movieRating = in.readString();
        this.movieReleaseDate = in.readString();
        this.movieBackDropImage = in.readString();
        this.id = in.readString();
        this.language=in.readString();
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

}
