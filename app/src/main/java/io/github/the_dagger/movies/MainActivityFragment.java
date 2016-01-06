package io.github.the_dagger.movies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    MovieAdapter adapter;

    public MainActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SingleMovie[] movieList = {new SingleMovie(R.drawable.acdisplay, "ACdisplay"),
                new SingleMovie(R.drawable.cabinet, "Cabinet"),
                new SingleMovie(R.drawable.chrome, "Chrome"),
                new SingleMovie(R.drawable.compass, "Compass"),
                new SingleMovie(R.drawable.maps, "Maps"),
                new SingleMovie(R.drawable.soundcloud, "SoundCloud")};
        adapter = new MovieAdapter(getActivity(), Arrays.asList(movieList));
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridview = (GridView) rootView.findViewById(R.id.gridView);
        gridview.setAdapter(adapter);
        return rootView;
    }
}
