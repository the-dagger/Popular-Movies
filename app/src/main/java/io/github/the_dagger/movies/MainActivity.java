package io.github.the_dagger.movies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import io.github.the_dagger.movies.api.Communicator;
import io.github.the_dagger.movies.fragments.DetailsLandscapeFragment;
import io.github.the_dagger.movies.objects.SingleMovie;

public class MainActivity extends AppCompatActivity implements Communicator {
    public static DetailsLandscapeFragment f;
    SingleMovie Movie;
    public static Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Top Rated");
        f = (DetailsLandscapeFragment) getFragmentManager().findFragmentById(R.id.fragment2);
    }

    @Override
    public void respond(SingleMovie movie) {
        Movie = movie;
        if (f != null && f.isVisible()) {
            f.getMovie(movie);
        }
    }


}
