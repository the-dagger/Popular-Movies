package io.github.the_dagger.movies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
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

public class DetailsActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fab)
    FloatingActionButton f;
    @Bind(R.id.movieDetailTitle)
    TextView title;
    @Bind(R.id.movieSummary)
    TextView overviewTextView;
    @Bind(R.id.backdrop)
    ImageView backDrop;
    @Bind(R.id.releaseDate)
    TextView releaseTextView;
    @Bind(R.id.posterImageDetail)
    ImageView posterImage;
    @Bind(R.id.ratingBar1)
    RatingBar rb;
    @Bind(R.id.language)
    TextView language;
    @Nullable
    @Bind(R.id.trailerCardView)
    CardView trailerCardView;
    @Bind(R.id.trailersText)
    TextView trailersText;
    private Trailers trailers;
    Call<Reviews> callRv;
    TmdbAPI tmdbApi;
    private List<Trailers.SingleTrailer> listTr;
    List<Reviews.SingleReview> listRv;
    String Base_URL = "http://api.themoviedb.org/3/";
    SingleMovie movie;
    private Reviews reviews;
    private ReviewAdapter reviewAdapter;
    @Bind(R.id.reviewCardView)
    CardView reviewCard;
    private TrailersAdapter trailersAdapter;
    Intent shareIntent;
    MenuItem shareItem;
    ShareActionProvider shareActionProvider;

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        reviewCard.setVisibility(View.GONE);
        trailersText.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        movie = intent.getParcelableExtra("Poster");
        language.setText(movie.language.toUpperCase());
        sharedpreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        trailersAdapter = new TrailersAdapter(listTr, this);
        final RecyclerView rvTrailer = (RecyclerView) findViewById(R.id.trailerRv);
        rvTrailer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvTrailer.setAdapter(trailersAdapter);
        rvTrailer.setVisibility(View.GONE);
        reviewAdapter = new ReviewAdapter(listRv, this);
        RecyclerView rvReview = (RecyclerView) findViewById(R.id.reviewRv);
        rvReview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvReview.setAdapter(reviewAdapter);
        title.setText(movie.movieTitle);
//        Picasso.with(getApplicationContext()).load(movie.movieImage).networkPolicy(NetworkPolicy.OFFLINE).into(posterImage, PicassoPalette.with(movie.movieImage, posterImage).use(BitmapPalette.Profile.MUTED)
//        );

        Glide.with(getApplicationContext()).load(movie.movieImage).crossFade().placeholder(R.drawable.sad).error(R.drawable.sad).into(posterImage);

        String overView = movie.movieOverView;
        String summary = "";
        float d = Float.parseFloat(movie.movieRating);
        rb.setRating((Math.round(d) / 2));
        releaseTextView.setText(movie.movieReleaseDate);
        for (String sum : overView.split("(?<=[.])\\s+"))
            if (summary.equals(""))
                summary = sum;
            else
                summary = summary + "\n" + sum;
        overviewTextView.setText(summary);
//        Picasso.with(getApplicationContext()).load(movie.movieBackDropImage).networkPolicy(NetworkPolicy.OFFLINE).into(backDrop);
        Glide.with(getApplicationContext()).load(movie.movieBackDropImage).crossFade().placeholder(R.drawable.placeholderbackdrop).error(R.drawable.placeholderbackdrop).into(backDrop);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        tmdbApi = retrofit.create(TmdbAPI.class);
        Call<Trailers> callTr = tmdbApi.getTrailers(movie.id);
        callTr.enqueue(new Callback<Trailers>() {
            @Override
            public void onResponse(Response<Trailers> response) {
                try {
                    trailers = response.body();
                    listTr = trailers.getTrailers();
                    trailersAdapter.swapList(listTr);
                    if (!listTr.isEmpty()) {
                        trailersText.setVisibility(View.VISIBLE);
                        rvTrailer.setVisibility(View.VISIBLE);
                        trailerCardView.setVisibility(View.VISIBLE);
                    }
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v=" + listTr.get(0).getKey() + "\n" + getResources().getString(R.string.message));
                    shareActionProvider.setShareIntent(shareIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast toast = null;
                    if (response.code() == 401) {
                        toast = Toast.makeText(DetailsActivity.this, "Unauthenticated", Toast.LENGTH_SHORT);
                    } else if (response.code() >= 400) {
                        toast = Toast.makeText(DetailsActivity.this, "Client Error " + response.code()
                                + " " + response.message(), Toast.LENGTH_SHORT);
                    }
                    try {
                        toast.show();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
                shareIntent.putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v=" + listTr.get(0).getKey() + "\n" + getResources().getString(R.string.message));
                try {
                    shareActionProvider.setShareIntent(shareIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                try {
                    Log.e("getQuestions threw: ", t.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        callRv = tmdbApi.getReview(movie.id);
        callRv.enqueue(new Callback<Reviews>() {
            @Override
            public void onResponse(Response<Reviews> response) {
                try {
                    reviews = response.body();
                    listRv = reviews.getReviews();
                    if (!listRv.isEmpty()) {
                        reviewCard.setVisibility(View.VISIBLE);
                    }
                    reviewAdapter.swapList(listRv);
                } catch (Exception e) {
                    Toast toast = null;
                    if (response.code() == 401) {
                        toast = Toast.makeText(DetailsActivity.this, "Unauthenticated", Toast.LENGTH_SHORT);
                    } else if (response.code() >= 400) {
                        toast = Toast.makeText(DetailsActivity.this, "Client Error " + response.code()
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
                try {
                    Log.e("getQuestions threw: ", t.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        if (sharedpreferences.contains(String.valueOf(movie.id))) {
            f.setImageResource(R.drawable.ic_favorite_white_24dp);
        } else
            f.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!sharedpreferences.contains(String.valueOf(movie.id))) {
                    Snackbar.make(view, getResources().getText(R.string.add_fav), Snackbar.LENGTH_LONG).show();
                    editor.putInt(String.valueOf(movie.id), movie.id);
                    editor.apply();
                    getContentResolver().insert(MovieTableTable.CONTENT_URI, MovieTableTable.getContentValues(movie, false));
                    getContentResolver().notifyChange(MovieTableTable.CONTENT_URI, null);
                    f.setImageResource(R.drawable.ic_favorite_white_24dp);
                } else {
                    Snackbar.make(view, getResources().getText(R.string.rem_fav), Snackbar.LENGTH_LONG).show();
                    int result = getContentResolver().delete(MovieTableTable.CONTENT_URI, MovieTableTable.FIELD_COL_ID + "=?", new String[]{String.valueOf(movie.id)});
                    Log.e("Result", String.valueOf(movie.id));
                    editor.remove(String.valueOf(movie.id));
                    editor.apply();
                    f.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                }

            }
        });
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        getSupportActionBar().setTitle("");
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (listTr != null) {
//            shareIntent.putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v=" + listTr.get(0).getKey() + "\n" + getResources().getString(R.string.message));
//            shareActionProvider.setShareIntent(shareIntent);
//        }
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share, menu);
        shareItem = menu.findItem(R.id.action_share);
        shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String key = null;
        try {
            key = listTr.get(0).getKey();
        } catch (Exception e) {
            e.printStackTrace();
            key = "";
        }
        shareIntent.putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v=" + key + "\n" + getResources().getString(R.string.message));
        ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        shareActionProvider.setShareIntent(shareIntent);
        return true;
    }

}

