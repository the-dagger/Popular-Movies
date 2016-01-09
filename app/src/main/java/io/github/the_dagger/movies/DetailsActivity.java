package io.github.the_dagger.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.picassopalette.BitmapPalette;
import com.github.florent37.picassopalette.PicassoPalette;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton f = (FloatingActionButton) findViewById(R.id.fab);
        TextView title = (TextView) findViewById(R.id.movieDetailTitle);
        TextView movieRating = (TextView) findViewById(R.id.ratingDetail);
        NestedScrollView s = (NestedScrollView) findViewById(R.id.scrollView);
        TextView overviewTextView = (TextView) findViewById(R.id.movieSummary);
        ImageView backDrop = (ImageView) findViewById(R.id.backdrop);
        TextView releaseTextView = (TextView) findViewById(R.id.releaseDate);
        ImageView posterImage = (ImageView) findViewById(R.id.posterImageDetail);
        Intent intent = getIntent();
        SingleMovie movie = intent.getParcelableExtra("Poster");
        title.setText(movie.movieTitle);
        Picasso.with(getApplicationContext()).load(movie.movieImage).into(posterImage, PicassoPalette.with(movie.movieImage, posterImage).use(BitmapPalette.Profile.MUTED)
                        .intoBackground(s)
                );
        overviewTextView.setText(movie.movieOverView);
//        String bkDrop = intent.getStringExtra("Backdrop");
//        String rating = intent.getStringExtra("Rating");
        String summary = "";
//        String releaseDate = intent.getStringExtra("ReleaseDate");
        movieRating.setText(movie.movieRating+"/10");
        releaseTextView.setText(movie.movieReleaseDate);
//        for (String sum:overView.split("(?<=[.])\\s+"))
//            if(summary == "")
//                summary = sum;
//            else
//            summary = summary + "\n" + sum;
//        overviewTextView.setText(summary);
        Picasso.with(getApplicationContext()).load(movie.movieBackDropImage).into(backDrop);
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Added to Favourites", Snackbar.LENGTH_LONG).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
