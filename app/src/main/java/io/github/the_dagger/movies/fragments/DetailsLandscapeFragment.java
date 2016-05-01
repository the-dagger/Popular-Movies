package io.github.the_dagger.movies.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import com.bumptech.glide.Glide;

import java.util.List;

import io.github.the_dagger.movies.R;
import io.github.the_dagger.movies.adapter.ReviewAdapter;
import io.github.the_dagger.movies.adapter.TrailersAdapter;
import io.github.the_dagger.movies.api.TmdbAPI;
import io.github.the_dagger.movies.objects.MovieTableTable;
import io.github.the_dagger.movies.objects.Reviews;
import io.github.the_dagger.movies.objects.SingleMovie;
import io.github.the_dagger.movies.objects.Trailers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Harshit on 1/26/2016.
 */
public class DetailsLandscapeFragment extends Fragment {
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    TextView language;
    TextView title;
    TextView overviewTextView;
    TextView releaseTextView;
    ImageView posterImage;
    RatingBar rb;
    ImageView backdrop;
    FloatingActionButton fab;
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
        movie = singleMovie;
        if (movie != null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Base_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            TmdbAPI tmdbApi = retrofit.create(TmdbAPI.class);
            if (movie != null) {
                Call<Trailers> callTr = tmdbApi.getTrailers(movie.id);
                callTr.enqueue(new Callback<Trailers>() {
                    @Override
                    public void onResponse(Response<Trailers> response) {
                        try {
                            trailers = response.body();
                            listTr = trailers.getTrailers();
                            listTr.size();       //ListTr is null here
                            trailersAdapter.swapList(listTr);
                            shareIntent.putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v=" + listTr.get(0).getKey() + "\n" + getResources().getString(R.string.message));
                            shareActionProvider.setShareIntent(shareIntent);
                        } catch (Exception e) {
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
                callRv = tmdbApi.getReview(movie.id);
                callRv.enqueue(new Callback<Reviews>() {
                    @Override
                    public void onResponse(Response<Reviews> response) {
                        try {
                            reviews = response.body();
                            listRv = reviews.getReviews();
                            reviewAdapter.swapList(listRv);
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
//                Picasso.with(getActivity()).load(movie.movieImage).into(posterImage, PicassoPalette.with(movie.movieImage, posterImage).use(BitmapPalette.Profile.MUTED)
//                );
                Glide.with(getActivity()).load(movie.movieImage).crossFade().placeholder(R.drawable.sad).error(R.drawable.sad).into(posterImage);
                String overView = movie.movieOverView;
                String summary = "";
                if (sharedpreferences.contains(String.valueOf(movie.getId()))) {
                    fab.setImageResource(R.drawable.ic_favorite_white_24dp);
                } else
                    fab.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                float d = Float.parseFloat(movie.movieRating);
                rb.setRating((Math.round(d) / 2));
                language.setText(movie.language.toUpperCase());
                releaseTextView.setText(movie.movieReleaseDate);
                for (String sum : overView.split("(?<=[.])\\s+"))
                    if (summary.equals(""))
                        summary = sum;
                    else
                        summary = summary + "\n" + sum;
                overviewTextView.setText(summary);
                try {
//                    Picasso.with(getActivity()).load(movie.movieBackDropImage).into(backdrop);
                    Glide.with(getActivity()).load(movie.movieBackDropImage).crossFade().placeholder(R.drawable.placeholderbackdrop).error(R.drawable.placeholderbackdrop).into(backdrop);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.share, menu);
        MenuItem shareItem = menu.findItem(R.id.action_share);
        shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v=" + "\n" + getResources().getString(R.string.message));
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
        if (savedInstanceState == null || !savedInstanceState.containsKey("movie2")) {
        } else {
            movie = savedInstanceState.getParcelable("movie2");
        }
        sharedpreferences = getActivity().getSharedPreferences("mypref", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.details_land_frag, container, false);
        title = (TextView) view.findViewById(R.id.movieDetailTitle1);
        language = (TextView) view.findViewById(R.id.language1);
        overviewTextView = (TextView) view.findViewById(R.id.movieSummary1);
        backdrop = (ImageView) view.findViewById(R.id.backdrop1);
        fab = (FloatingActionButton) view.findViewById(R.id.fab_master);
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

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (!sharedpreferences.contains(String.valueOf(movie.id))) {
                            Snackbar.make(view, getResources().getText(R.string.add_fav), Snackbar.LENGTH_LONG).show();
                            editor.putInt(String.valueOf(movie.id), movie.id);
                            editor.apply();
                            getActivity().getContentResolver().insert(MovieTableTable.CONTENT_URI,MovieTableTable.getContentValues(movie,false));
                            fab.setImageResource(R.drawable.ic_favorite_white_24dp);
        //                    MainActivityFragment.favAdapter.notifyDataSetChanged();
                        } else {
                            Snackbar.make(view, getResources().getText(R.string.rem_fav), Snackbar.LENGTH_LONG).show();
                            int result = getActivity().getContentResolver().delete(MovieTableTable.CONTENT_URI,MovieTableTable.FIELD_COL_ID + "=?", new String[]{String.valueOf(movie.id)});
                            Log.e("Result", String.valueOf(movie.id));
                            editor.remove(String.valueOf(movie.id));
                            editor.apply();
                            fab.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        //                    MainActivityFragment.favAdapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        return view;
    }
}
