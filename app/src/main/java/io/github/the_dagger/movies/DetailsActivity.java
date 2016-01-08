package io.github.the_dagger.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
        Intent intent = getIntent();
        String overView = intent.getStringExtra("OverView");
        String bkDrop = intent.getStringExtra("Backdrop");
        String rating = intent.getStringExtra("Rating");
        String releaseDate = intent.getStringExtra("ReleaseDate");
        TextView movieRating = (TextView) findViewById(R.id.ratingDetail);
        movieRating.setText(rating+"/10");
        LinearLayout l = (LinearLayout) findViewById(R.id.linearLayout);
        ImageView backDrop = (ImageView) findViewById(R.id.backdrop);
        TextView releaseTextView = (TextView) findViewById(R.id.releaseDate);
        releaseTextView.setText(releaseDate);
        TextView overviewTextView = (TextView) findViewById(R.id.movieSummary);
        overviewTextView.setText(overView);
        RelativeLayout r = (RelativeLayout) findViewById(R.id.detail_activity_relative_layout);
        ScrollView s = (ScrollView) findViewById(R.id.scrollView);
        Picasso.with(getApplicationContext()).load(bkDrop).fit().into(backDrop, PicassoPalette.with(bkDrop, backDrop).use(BitmapPalette.Profile.MUTED)
                        .intoBackground(l)
                        .intoBackground(movieRating)
                        .intoBackground(releaseTextView)
                        .intoBackground(overviewTextView)
                        .intoBackground(r)
                        .intoBackground(s)
        );
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Added to Favourites", Snackbar.LENGTH_LONG).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
