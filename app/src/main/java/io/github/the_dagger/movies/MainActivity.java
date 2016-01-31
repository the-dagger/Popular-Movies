package io.github.the_dagger.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity extends AppCompatActivity implements Communicator{
    DetailsLandscapeFragment f;
    SingleMovie Movie = new SingleMovie("lol","lol","lol","5","n/a","lol2");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            Movie = new SingleMovie("lol","lol","lol","5","n/a","lol2");
        }
        else{
            Movie = savedInstanceState.getParcelable("movieMain");
        }
        f = (DetailsLandscapeFragment) getFragmentManager().findFragmentById(R.id.fragment2);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("movieMain",Movie);
    }

    @Override
    public void respond(SingleMovie movie) {
        Movie = movie;
//        Log.e("lol",movie.movieTitle);
        if(f != null && f.isVisible()){
            f.getMovie(movie);
        }
        else{
            Intent switchIntent = new Intent(this, DetailsActivity.class)
                        .putExtra(getString(R.string.Poster), movie);
            startActivity(switchIntent);
        }
    }
}
