package io.github.the_dagger.movies;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.florent37.picassopalette.BitmapPalette;
import com.github.florent37.picassopalette.PicassoPalette;
import com.squareup.picasso.Picasso;

/**
 * Created by Harshit on 1/26/2016.
 */
public class DetailsLandscapeFragment extends Fragment {
    SingleMovie movie ;
    TextView title;
    TextView overviewTextView;
    TextView releaseTextView;
    ImageView posterImage;
    RatingBar rb;

    public void getMovie(SingleMovie singleMovie){
        this.movie = singleMovie;
        if(movie!=null){
        title.setText(movie.movieTitle);
        Picasso.with(getActivity()).load(movie.movieImage).error(R.drawable.placeholder).into(posterImage, PicassoPalette.with(movie.movieImage, posterImage).use(BitmapPalette.Profile.MUTED)
        );
        String overView = movie.movieOverView;
        String summary = "";
        float d = Float.parseFloat(movie.movieRating);
        rb.setRating((Math.round(d)/2));
        releaseTextView.setText(movie.movieReleaseDate);
        for (String sum:overView.split("(?<=[.])\\s+"))
            if(summary == "")
                summary = sum;
            else
                summary = summary + "\n" + sum;
        overviewTextView.setText(summary);}
        else
        {
//            title.setText("Movie Title");
//            Picasso.with(getActivity()).load("k").error(R.drawable.placeholder).into(posterImage, PicassoPalette.with(movie.movieImage, posterImage).use(BitmapPalette.Profile.MUTED)
//            );
//            rb.setRating(0);
//            releaseTextView.setText("N/A");
//            overviewTextView.setText("Summary");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(movie != null) {
            Log.v("LOL2",movie.movieTitle);
            title.setText(movie.movieTitle);
            Picasso.with(getActivity()).load(movie.movieImage).error(R.drawable.placeholder).into(posterImage, PicassoPalette.with(movie.movieImage, posterImage).use(BitmapPalette.Profile.MUTED)
            );
            String overView = movie.movieOverView;
            String summary = "";
            float d = Float.parseFloat(movie.movieRating);
            rb.setRating((Math.round(d)/2));
            releaseTextView.setText(movie.movieReleaseDate);
            for (String sum:overView.split("(?<=[.])\\s+"))
                if(summary == "")
                    summary = sum;
                else
                    summary = summary + "\n" + sum;
            overviewTextView.setText(summary);
        }
        else{
            title.setText("Movie Title");
//            Picasso.with(getActivity()).load(movie.movieImage).error(R.drawable.placeholder).into(posterImage, PicassoPalette.with(movie.movieImage, posterImage).use(BitmapPalette.Profile.MUTED)
//            );
            rb.setRating(0);
            releaseTextView.setText("N/A");
            overviewTextView.setText("Summary");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("movie2",movie);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState == null || !savedInstanceState.containsKey("movie2")){
            title = (TextView) getActivity().findViewById(R.id.movieDetailTitle1);
            overviewTextView = (TextView) getActivity().findViewById(R.id.movieSummary1);
            releaseTextView = (TextView) getActivity().findViewById(R.id.releaseDate1);
            posterImage = (ImageView) getActivity().findViewById(R.id.posterImageDetail1);
            rb = (RatingBar) getActivity().findViewById(R.id.ratingBar11);
//            title.setText("Movie Title");
//            Picasso.with(getActivity()).load(movie.movieImage).error(R.drawable.placeholder).into(posterImage, PicassoPalette.with(movie.movieImage, posterImage).use(BitmapPalette.Profile.MUTED)
//            );
//            rb.setRating(0);
//            releaseTextView.setText("N/A");
//            overviewTextView.setText("Summary");
        }
        else{
            movie = savedInstanceState.getParcelable("movie2");
        }
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.details_land_frag,container,false);
        title = (TextView) view.findViewById(R.id.movieDetailTitle1);
        overviewTextView = (TextView) view.findViewById(R.id.movieSummary1);
        releaseTextView = (TextView) view.findViewById(R.id.releaseDate1);
        posterImage = (ImageView) view.findViewById(R.id.posterImageDetail1);
        rb = (RatingBar) view.findViewById(R.id.ratingBar11);
        return view;
    }
}
