package io.github.the_dagger.movies;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import io.github.the_dagger.movies.api.Communicator;
import io.github.the_dagger.movies.fragments.DetailsLandscapeFragment;
import io.github.the_dagger.movies.objects.SingleMovie;

public class MainActivity extends AppCompatActivity implements Communicator {
    @Override
    public void onBackPressed() {
        count--;
        if(count == 1){
            Snackbar.make(this.findViewById(android.R.id.content),getResources().getText(R.string.exit),Snackbar.LENGTH_LONG).show();
        }
        else
            super.onBackPressed();

    }

    int count = 2;
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
