package io.github.the_dagger.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity implements Communicator{
    DetailsLandscapeFragment f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
    public void respond(SingleMovie movie) {
        if(f != null && f.isVisible()){
            f.getMovie(movie);
            Log.v("Hello",movie.movieTitle);
        }
        else{
            Intent switchIntent = new Intent(this, DetailsActivity.class)
                        .putExtra(getString(R.string.Poster), movie);
            startActivity(switchIntent);
        }
    }
}
