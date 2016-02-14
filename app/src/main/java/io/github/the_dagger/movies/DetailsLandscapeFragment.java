package io.github.the_dagger.movies;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.picassopalette.BitmapPalette;
import com.github.florent37.picassopalette.PicassoPalette;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Harshit on 1/26/2016.
 */
public class DetailsLandscapeFragment extends Fragment {
    SingleMovie movie;
    TextView title;
    TextView overviewTextView;
    TextView releaseTextView;
    ImageView posterImage;
    RatingBar rb;
    ImageView backdrop;
    RecyclerView rv;
    Call<Trailers> call;
    List<Trailers.SingleTrailer> list1;
    String Base_URL = "http://api.themoviedb.org/3/";
    private Trailers trailers;
    private TrailerAdapter trailerAdapter;
    public void getMovie(SingleMovie singleMovie) {
        trailerAdapter = new TrailerAdapter(list1);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TmdbAPI tmdbApi = retrofit.create(TmdbAPI.class);
        movie = singleMovie;
            if(movie!= null){
                call = tmdbApi.getTrailers(movie.id);
                call.enqueue(new Callback<Trailers>() {
                    @Override
                    public void onResponse(Call<Trailers> call, Response<Trailers> response) {
                        try {
                            trailers = response.body();
                            list1 = trailers.getTrailers();
                            trailerAdapter.swapList(list1);
                        } catch (Exception e) {
                            Toast toast = null;
                            if (response.code() == 401){
                                toast = Toast.makeText(getActivity(), "Unauthenticated", Toast.LENGTH_SHORT);
                            } else if (response.code() >= 400){
                                toast = Toast.makeText(getActivity(), "Client Error " + response.code()
                                        + " " + response.message(), Toast.LENGTH_SHORT);
                            }
                            try {
                                toast.show();
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Trailers> call, Throwable t) {
                        Log.e("getQuestions threw: ", t.getMessage());
                    }
                });
            title.setText(movie.movieTitle);
            Picasso.with(getActivity()).load(movie.movieImage).error(R.drawable.placeholder).into(posterImage, PicassoPalette.with(movie.movieImage, posterImage).use(BitmapPalette.Profile.MUTED)
            );
            String overView = movie.movieOverView;
            String summary = "";
            float d = Float.parseFloat(movie.movieRating);
            rb.setRating((Math.round(d) / 2));
            releaseTextView.setText(movie.movieReleaseDate);
            for (String sum : overView.split("(?<=[.])\\s+"))
                if (summary == "")
                    summary = sum;
                else
                    summary = summary + "\n" + sum;
            overviewTextView.setText(summary);
            try {
                Picasso.with(getActivity()).load(movie.movieBackDropImage).into(backdrop);
            } catch (IllegalArgumentException e) {
            }

        }}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("movie2", movie);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState == null || !savedInstanceState.containsKey("movie2")){}
        else{
            movie = savedInstanceState.getParcelable("movie2");
        }
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.details_land_frag, container, false);
        rv = (RecyclerView) view.findViewById(R.id.trailerRecyclerViewFragment);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        rv.setAdapter(trailerAdapter);
        title = (TextView) view.findViewById(R.id.movieDetailTitle1);
        overviewTextView = (TextView) view.findViewById(R.id.movieSummary1);
        backdrop = (ImageView) view.findViewById(R.id.backdrop1);
        releaseTextView = (TextView) view.findViewById(R.id.releaseDate1);
        posterImage = (ImageView) view.findViewById(R.id.posterImageDetail1);
        rb = (RatingBar) view.findViewById(R.id.ratingBar11);
        if(movie!=null){
        title.setText(movie.movieTitle);
        Picasso.with(getActivity()).load(movie.movieImage).error(R.drawable.placeholder).into(posterImage, PicassoPalette.with(movie.movieImage, posterImage).use(BitmapPalette.Profile.MUTED)
        );
        String overView = movie.movieOverView;
        String summary = "";
        float d = Float.parseFloat(movie.movieRating);
        rb.setRating((Math.round(d) / 2));
        releaseTextView.setText(movie.movieReleaseDate);
        for (String sum : overView.split("(?<=[.])\\s+"))
            if (summary == "")
                summary = sum;
            else
                summary = summary + "\n" + sum;
        overviewTextView.setText(summary);
        try {
            Picasso.with(getActivity()).load(movie.movieBackDropImage).into(backdrop);
        } catch (IllegalArgumentException e) {
        }}
        return view;
    }
}
