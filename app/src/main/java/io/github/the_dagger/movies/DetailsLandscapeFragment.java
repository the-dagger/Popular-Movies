package io.github.the_dagger.movies;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    TextView language;
    TextView title;
    TextView overviewTextView;
    TextView releaseTextView;
    ImageView posterImage;
    RatingBar rb;
    ImageView backdrop;
    private Call<Trailers> callTr;
    private Trailers trailers;
    Call<Reviews> callRv;
    ShareActionProvider shareActionProvider;
    List<Trailers.SingleTrailer> listTr;
    List<Reviews.SingleReview> listRv;
    String Base_URL = "http://api.themoviedb.org/3/";
    SingleMovie movie;
    RecyclerView rvTrailer;
    RecyclerView rvReview;
    private Reviews reviews;
    private ReviewAdapter reviewAdapter;
    private TrailersAdapter trailersAdapter;
    Intent shareIntent;

    public void getMovie(SingleMovie singleMovie) {
        Log.e("getmovie", "getmovie ran");
        movie = singleMovie;
        if (movie != null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Base_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            TmdbAPI tmdbApi = retrofit.create(TmdbAPI.class);
            if (movie != null) {
                callTr = tmdbApi.getTrailers(movie.id);
                callTr.enqueue(new Callback<Trailers>() {
                    @Override
                    public void onResponse(Response<Trailers> response) {
//                        Log.e(getClass().getSimpleName(), response.raw().toString());
                        try {
                            trailers = response.body();
                            listTr = trailers.getTrailers();
                            listTr.size();       //ListTr is null here
                            trailersAdapter.swapList(listTr);
                            shareIntent.putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v=" + listTr.get(0).getKey() + "\n" + EXTRA_MESSAGE);
                            shareActionProvider.setShareIntent(shareIntent);
//                        trailersAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            Log.e("Exception", "Exception");   //This statement is executed
                            e.printStackTrace();
                            Toast toast = null;
                            if (response.code() == 401) {
                                toast = Toast.makeText(getActivity(), "Unauthenticated", Toast.LENGTH_SHORT);
                            } else if (response.code() >= 400) {
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
                    public void onFailure(Throwable t) {
                        Log.e("getQuestions threw: ", t.getMessage());
                    }
                });
//        if(movie!=null) {
                callRv = tmdbApi.getReview(movie.id);
                callRv.enqueue(new Callback<Reviews>() {
                    @Override
                    public void onResponse(Response<Reviews> response) {
                        Log.e(getClass().getSimpleName(), response.raw().toString());
                        try {
                            reviews = response.body();
                            listRv = reviews.getReviews();
                            reviewAdapter.swapList(listRv);
//                        trailersAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            Toast toast = null;
                            if (response.code() == 401) {
                                toast = Toast.makeText(getActivity(), "Unauthenticated", Toast.LENGTH_SHORT);
                            } else if (response.code() >= 400) {
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
                    public void onFailure(Throwable t) {
                        Log.e("getQuestions threw: ", t.getMessage());
                    }
                });
                title.setText(movie.movieTitle);
                Picasso.with(getActivity()).load(movie.movieImage).into(posterImage, PicassoPalette.with(movie.movieImage, posterImage).use(BitmapPalette.Profile.MUTED)
                );
                String overView = movie.movieOverView;
                String summary = "";
                float d = Float.parseFloat(movie.movieRating);
                rb.setRating((Math.round(d) / 2));
                language.setText(movie.language.toUpperCase());
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

            }
        }
    }

    String EXTRA_MESSAGE = "Sent via Popular Movies app";

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.share, menu);
        MenuItem shareItem = menu.findItem(R.id.action_share);
        shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v=" + "\n" + EXTRA_MESSAGE);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        shareActionProvider.setShareIntent(shareIntent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("movie2", movie);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e("getmovie", "oncreate ran");
        if (savedInstanceState == null || !savedInstanceState.containsKey("movie2")) {
//            getMovie(movie);
        } else {
            movie = savedInstanceState.getParcelable("movie2");
        }
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("getmovie", "oncreateview ran");
        View view = inflater.inflate(R.layout.details_land_frag, container, false);
        title = (TextView) view.findViewById(R.id.movieDetailTitle1);
        language = (TextView) view.findViewById(R.id.language1);
        overviewTextView = (TextView) view.findViewById(R.id.movieSummary1);
        backdrop = (ImageView) view.findViewById(R.id.backdrop1);
        releaseTextView = (TextView) view.findViewById(R.id.releaseDate1);
        posterImage = (ImageView) view.findViewById(R.id.posterImageDetail1);
        rb = (RatingBar) view.findViewById(R.id.ratingBar11);
        trailersAdapter = new TrailersAdapter(listTr, view.getContext());
        rvTrailer = (RecyclerView) view.findViewById(R.id.trailerRv1);
        rvTrailer.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvTrailer.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }
        });
        rvTrailer.setAdapter(trailersAdapter);
        reviewAdapter = new ReviewAdapter(listRv, view.getContext());
        rvReview = (RecyclerView) view.findViewById(R.id.reviewRv1);
        rvReview.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        rvReview.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }
        });
        rvReview.setAdapter(reviewAdapter);
        getMovie(movie);
        return view;
    }
}
