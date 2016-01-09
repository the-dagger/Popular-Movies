package io.github.the_dagger.movies;

import android.content.Intent;
import android.graphics.Bitmap;
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

import io.techery.properratingbar.ProperRatingBar;

public class DetailsActivity extends AppCompatActivity {
   // private CollapsingToolbarLayout collapsingToolbarLayout = null;
    Bitmap bitmap = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ProperRatingBar rb = (ProperRatingBar) findViewById(R.id.ratingBar1);
//        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        FloatingActionButton f = (FloatingActionButton) findViewById(R.id.fab);
        TextView title = (TextView) findViewById(R.id.movieDetailTitle);
        NestedScrollView s = (NestedScrollView) findViewById(R.id.scrollView);
        TextView overviewTextView = (TextView) findViewById(R.id.movieSummary);
        ImageView backDrop = (ImageView) findViewById(R.id.backdrop);
        TextView releaseTextView = (TextView) findViewById(R.id.releaseDate);
        ImageView posterImage = (ImageView) findViewById(R.id.posterImageDetail);
        Intent intent = getIntent();
        SingleMovie movie = intent.getParcelableExtra("Poster");
//        getImagebit a = new getImagebit();
//        a.execute(movie.movieImage);
//        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
//
//            @Override
//            public void onGenerated(Palette palette) {
//                collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(R.attr.colorPrimary));
//                collapsingToolbarLayout.setStatusBarScrimColor(palette.getMutedColor(R.attr.colorPrimaryDark));
//            }
//        });
        title.setText(movie.movieTitle);
        Picasso.with(getApplicationContext()).load(movie.movieImage).into(posterImage, PicassoPalette.with(movie.movieImage, posterImage).use(BitmapPalette.Profile.MUTED)
                        .intoBackground(s)
                );
        String overView = movie.movieOverView;
        String summary = "";
        float d = Float.parseFloat(movie.movieRating);
        //movieRating.setText(movie.movieRating+"/10");
        rb.setRating(Math.round(d));
        releaseTextView.setText(movie.movieReleaseDate);
        for (String sum:overView.split("(?<=[.])\\s+"))
            if(summary == "")
                summary = sum;
            else
                summary = summary + "\n" + sum;
        overviewTextView.setText(summary);
        Picasso.with(getApplicationContext()).load(movie.movieBackDropImage).into(backDrop);
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Added to Favourites", Snackbar.LENGTH_LONG).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }
//    public class getImagebit extends AsyncTask<String,Void,Bitmap>{
//        @Override
//        protected void onPostExecute(Bitmap bitmap1) {
//            bitmap = bitmap1;
//            super.onPostExecute(bitmap1);
//        }
//
//        @Override
//        protected Bitmap doInBackground(String... params) {
//            Bitmap bitmap = null;
//            try {
//                bitmap = Picasso.with(getApplicationContext()).load(params[0]).get();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return bitmap;
//        }
//    }

}
