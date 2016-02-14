package io.github.the_dagger.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetailsActivity extends AppCompatActivity{
    @Bind(R.id.toolbar)Toolbar toolbar;
    @Bind(R.id.fab) FloatingActionButton f;
    @Bind(R.id.movieDetailTitle) TextView title;
    @Bind(R.id.movieSummary) TextView overviewTextView;
    @Bind(R.id.backdrop) ImageView backDrop;
    @Bind(R.id.releaseDate) TextView releaseTextView;
    @Bind(R.id.posterImageDetail) ImageView posterImage;
    @Bind(R.id.ratingBar1) RatingBar rb;
    @Bind(R.id.trailerRv) RecyclerView rv;
    Call<Trailers> call;
    List<Trailers.SingleTrailer> list1;
    String Base_URL = "http://api.themoviedb.org/3/";
    SingleMovie movie;
    private Trailers trailers;
    private TrailerAdapter trailerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        movie = intent.getParcelableExtra("Poster");
        trailerAdapter = new TrailerAdapter(list1);
        rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        rv.setAdapter(trailerAdapter);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TmdbAPI tmdbApi = retrofit.create(TmdbAPI.class);
        if(movie!=null) {
//            Log.e("Movie ID",movie.id);
            call = tmdbApi.getTrailers(movie.id);
            call.enqueue(new Callback<Trailers>() {
                @Override
                public void onResponse(Call<Trailers> call, Response<Trailers> response) {
                    try {
                        trailers = response.body();
                        list1 = trailers.getTrailers();
//                        trailerAdapter.swapList(list1);
                        trailerAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        Toast toast = null;
                        if (response.code() == 401){
                            toast = Toast.makeText(DetailsActivity.this, "Unauthenticated", Toast.LENGTH_SHORT);
                        } else if (response.code() >= 400){
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
                public void onFailure(Call<Trailers> call, Throwable t) {
                    Log.e("getQuestions threw: ", t.getMessage());
                }
            });
            title.setText(movie.movieTitle);
            Picasso.with(getApplicationContext()).load(movie.movieImage).error(R.drawable.placeholder).into(posterImage, PicassoPalette.with(movie.movieImage, posterImage).use(BitmapPalette.Profile.MUTED)
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
        }
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Added to Favourites", Snackbar.LENGTH_LONG).show();
            }
        });
        try{
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);}
        catch(NullPointerException e){};
        getSupportActionBar().setTitle("");
    }

}
