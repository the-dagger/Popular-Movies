package io.github.the_dagger.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.florent37.picassopalette.BitmapPalette;
import com.github.florent37.picassopalette.PicassoPalette;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.net.HttpURLConnection;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity{
    @Bind(R.id.toolbar)Toolbar toolbar;
    @Bind(R.id.fab) FloatingActionButton f;
    @Bind(R.id.movieDetailTitle) TextView title;
    @Bind(R.id.movieSummary) TextView overviewTextView;
    @Bind(R.id.backdrop) ImageView backDrop;
    @Bind(R.id.releaseDate) TextView releaseTextView;
    @Bind(R.id.posterImageDetail) ImageView posterImage;
    @Bind(R.id.ratingBar1) RatingBar rb;
    @Bind(R.id.lViewTrailers)
    ListView lv;
    String movieDbUrl = null;
    SingleMovie movie;
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    String movieinfo = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        movie = intent.getParcelableExtra("Poster");
        if(movie!=null) {
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
