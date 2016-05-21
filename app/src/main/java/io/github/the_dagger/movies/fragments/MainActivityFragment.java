package io.github.the_dagger.movies.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.the_dagger.movies.MainActivity;
import io.github.the_dagger.movies.R;
import io.github.the_dagger.movies.adapter.MovieAdapter;
import io.github.the_dagger.movies.api.Communicator;
import io.github.the_dagger.movies.api.FetchMovies;
import io.github.the_dagger.movies.objects.MovieTableTable;
import io.github.the_dagger.movies.objects.SingleMovie;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    public static MovieAdapter adapter, favAdapter;
    public static Boolean sort = false;  //false means sorted by ratings
    public static SingleMovie[] movieDetails = new SingleMovie[20];
    public static ArrayList<SingleMovie> list;
    FetchMovies weather1;
    List<SingleMovie> favMovieAsList;
    //    SimpleCursorAdapter favMovieAsList;
//    MovieAdapter movieAdapter;
    FetchMovies fetchMovies;
    public static View v;
    public static Activity a;
    public static Context c;
    SingleMovie[] movieList = {};
    public static Communicator com;
    ImageView poster;
    RecyclerView rv;
    SharedPreferences sharedpreferences;
    ConnectivityManager connectivityManager;
    NetworkInfo activeNetworkInfo;
    int currstate = 1;

    public MainActivityFragment() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", list);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = getView();
        c = getContext();
        a = getActivity();
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        fetchMovies = new FetchMovies(getActivity(), getView(), getContext());
        weather1 = new FetchMovies(getActivity(), getView(), getContext());
        com = (Communicator) getActivity();
//        movieAdapter = new MovieAdapter(getActivity(),getView(),getContext());
        if (savedInstanceState == null) {
            list = new ArrayList<>(Arrays.asList(movieList));
            weather1 = new FetchMovies(getActivity(), getView(), getContext());
            sort = true;
            sharedpreferences = getActivity().getSharedPreferences("mypref", Context.MODE_PRIVATE);
        } else {
            list = savedInstanceState.getParcelableArrayList("movies");
        }
        if(activeNetworkInfo == null)
            currstate = 3;
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater Inflater) {
        Inflater.inflate(R.menu.moviefragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            FetchMovies weather = new FetchMovies(getActivity(), getView(), getContext());
            sort = true;
            Snackbar.make(getView(), getResources().getText(R.string.sort_rat), Snackbar.LENGTH_LONG).show();
            weather.execute();
            weather.progressDialog.show();
            currstate = 1;
            rv.setAdapter(adapter);
            MainActivity.toolbar.setTitle("Top Rated");
            return true;
        }
        if (id == R.id.action_sort) {
            FetchMovies weather = new FetchMovies(getActivity(), getView(), getContext());
            sort = false;
            Snackbar.make(getView(), getResources().getText(R.string.sort_pop), Snackbar.LENGTH_LONG).show();
            weather.execute();
            currstate = 2;
            weather.progressDialog.show();
            rv.setAdapter(adapter);
            MainActivity.toolbar.setTitle("Popular");
            return true;
        }
        if (id == R.id.action_fav) {
            favMovieAsList = MovieTableTable.getRows(getActivity().getContentResolver().query(MovieTableTable.CONTENT_URI, null, null, null, null), true);
            favAdapter = new MovieAdapter(getActivity(), favMovieAsList);
//            favAdapter.notifyDataSetChanged();
            rv.setAdapter(favAdapter);
            currstate = 3;
            MainActivity.toolbar.setTitle("Favourites");
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        favMovieAsList = MovieTableTable.getRows(getActivity().getContentResolver().query(MovieTableTable.CONTENT_URI, null, null, null, null), true);
        adapter = new MovieAdapter(getActivity(), list);
//        favAdapter.notifyDataSetChanged();
        rv = (RecyclerView) inflater.inflate(R.layout.fragment_main, container, false);
        rv.setLayoutManager(new GridLayoutManager(rv.getContext(), 2));
        poster = (ImageView) rv.findViewById(R.id.movie_poster_image);
        com = (Communicator) getActivity();
        return rv;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (currstate == 3) {
            favMovieAsList = MovieTableTable.getRows(getActivity().getContentResolver().query(MovieTableTable.CONTENT_URI, null, null, null, null), true);
            favAdapter = new MovieAdapter(getActivity(), favMovieAsList);
            rv.setAdapter(favAdapter);
        } else {
//            rv.setAdapter(adapter);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (activeNetworkInfo == null) {
            Snackbar.make(getView(), getResources().getText(R.string.no_net), Snackbar.LENGTH_LONG).show();
            favMovieAsList = MovieTableTable.getRows(getActivity().getContentResolver().query(MovieTableTable.CONTENT_URI, null, null, null, null), true);
            favAdapter = new MovieAdapter(getActivity(), favMovieAsList);
            rv.setAdapter(favAdapter);
            try {
                com.respond(favMovieAsList.get(0));
            } catch (Exception e) {
                e.printStackTrace();
            }
            currstate = 3;
        } else {
            weather1.execute();
            weather1.progressDialog.show();
            rv.setAdapter(adapter);
        }
    }

}
