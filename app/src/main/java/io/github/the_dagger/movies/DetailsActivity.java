package io.github.the_dagger.movies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
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

import com.github.florent37.picassopalette.BitmapPalette;
import com.github.florent37.picassopalette.PicassoPalette;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.the_dagger.movies.adapter.ReviewAdapter;
import io.github.the_dagger.movies.adapter.TrailersAdapter;
import io.github.the_dagger.movies.api.TmdbAPI;
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
    Boolean setAsFav = false;
    @Bind(R.id.language)
    TextView language;
//    List<SingleMovie> listsharedPref = new ArrayList<>();
    private Trailers trailers;
    Call<Reviews> callRv;
    TmdbAPI tmdbApi;
    private List<Trailers.SingleTrailer> listTr;
    List<Reviews.SingleReview> listRv;
    String Base_URL = "http://api.themoviedb.org/3/";
    SingleMovie movie;
    private static final int CURSOR_LOADER_ID = 0;
    private Reviews reviews;
    private ReviewAdapter reviewAdapter;

    private TrailersAdapter trailersAdapter;
    String EXTRA_MESSAGE = "Sent via Popular Movies app";
    Intent shareIntent;
    MenuItem shareItem;
    ShareActionProvider shareActionProvider;

    @Override
    protected void onResume() {
        super.onResume();
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
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v=" + listTr.get(0).getKey() + "\n" + EXTRA_MESSAGE);
                    shareActionProvider.setShareIntent(shareIntent);
                } catch (Exception e) {
//                    Log.e("exception","Exception");
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
//                    Log.e(getClass().getSimpleName(),response.raw().toString());
                try {
                    reviews = response.body();
                    listRv = reviews.getReviews();
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
                Log.e("getQuestions threw: ", t.getMessage());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        movie = intent.getParcelableExtra("Poster");
        language.setText(movie.language.toUpperCase());
        sharedpreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        trailersAdapter = new TrailersAdapter(listTr, this);
        RecyclerView rvTrailer = (RecyclerView) findViewById(R.id.trailerRv);
        rvTrailer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvTrailer.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }
        });
        rvTrailer.setAdapter(trailersAdapter);
        reviewAdapter = new ReviewAdapter(listRv, this);
        RecyclerView rvReview = (RecyclerView) findViewById(R.id.reviewRv);
        rvReview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvTrailer.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }
        });
        rvReview.setAdapter(reviewAdapter);
        title.setText(movie.movieTitle);
        Picasso.with(getApplicationContext()).load(movie.movieImage).into(posterImage, PicassoPalette.with(movie.movieImage, posterImage).use(BitmapPalette.Profile.MUTED)
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
        Picasso.with(getApplicationContext()).load(movie.movieBackDropImage).into(backDrop);
        if (sharedpreferences.contains(movie.getId())){
            f.setImageResource(R.drawable.ic_favorite_white_24dp);
        }
        else
            f.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!setAsFav && !sharedpreferences.contains(movie.getId())) {
                    Snackbar.make(view, "Added to Favourites", Snackbar.LENGTH_LONG).show();
                    editor.putInt(movie.getId(), Integer.parseInt(movie.getId()));
                    editor.apply();
                    setAsFav = true;
                    f.setImageResource(R.drawable.ic_favorite_white_24dp);

                } else {
                    Snackbar.make(view, "Removed from Favourites", Snackbar.LENGTH_LONG).show();
                    editor.remove(movie.getId());
                    editor.apply();
                    setAsFav = false;
                    f.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                }

            }
        });
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
        }
        ;
        getSupportActionBar().setTitle("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share, menu);
        shareItem = menu.findItem(R.id.action_share);
        shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
//        Log.e("key2",key);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v=" + "\n" + EXTRA_MESSAGE);
        ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
//        shareActionProvider.setOnShareTargetSelectedListener(this);
        shareActionProvider.setShareIntent(shareIntent);

        return true;
    }
}

