package io.github.the_dagger.movies.objects;

import android.os.Parcel;
import android.os.Parcelable;

import ckm.simple.sql_provider.annotation.SimpleSQLColumn;
import ckm.simple.sql_provider.annotation.SimpleSQLTable;

/**
 * Created by Harshit on 1/6/2016.
 */
@SimpleSQLTable(table = "movieTable", provider = "MovieProvider")

public class SingleMovie implements Parcelable {

    @SimpleSQLColumn("col_id")
    public int id;

    @SimpleSQLColumn("col_movieImage")
    public String movieImage;

    @SimpleSQLColumn("col_movieTitle")
    public String movieTitle;

    @SimpleSQLColumn("col_movieOverView")
    public String movieOverView;

    @SimpleSQLColumn("col_movieRating")
    public String movieRating;

    @SimpleSQLColumn("col_movieReleaseDate")
    public String movieReleaseDate;

    @SimpleSQLColumn("col_backDropImage")
    public String movieBackDropImage;

    @SimpleSQLColumn("col_language")
    public String language;

    public SingleMovie(){
        movieImage = null;
        movieTitle = "Constructor was called";
    }

    public SingleMovie(String image, String title, String overView, String rating, String releaseDate, String backDropImage, int id, String language) {
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
        this.id = in.readInt();
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

    public int getId() {
        return id;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieImage);
        dest.writeString(movieTitle);
        dest.writeString(movieOverView);
        dest.writeString(movieRating);
        dest.writeString(movieReleaseDate);
        dest.writeString(movieBackDropImage);
        dest.writeInt(id);
        dest.writeString(language);
    }

}
