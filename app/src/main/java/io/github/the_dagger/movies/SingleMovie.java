package io.github.the_dagger.movies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Harshit on 1/6/2016.
 */
public class SingleMovie implements Parcelable{
    String movieImage;
    String movieTitle;

    public SingleMovie(String image, String title){
        this.movieImage = image;
        this.movieTitle = title;
    }
    public SingleMovie(Parcel in){
        this.movieImage = in.readString();
        this.movieTitle = in.readString();}

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
    }
}
