package io.github.the_dagger.movies.fragments;

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

import io.github.the_dagger.movies.R;
import io.github.the_dagger.movies.adapter.MovieAdapter;
import io.github.the_dagger.movies.api.Communicator;
import io.github.the_dagger.movies.api.FetchMovies;
import io.github.the_dagger.movies.objects.SingleMovie;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    public static  MovieAdapter adapter, favAdapter;
    public static Boolean sort = false;  //false means sorted by ratings
    public static SingleMovie[] movieDetails = new SingleMovie[20];
    public static ArrayList<SingleMovie> list;
    public static ArrayList<SingleMovie> testList;
    ArrayList<SingleMovie> favList;
    FetchMovies weather1;
    MovieAdapter movieAdapter;
    FetchMovies fetchMovies;
    SingleMovie[] movieList = {};
    SingleMovie[] favouriteList = {};
    SingleMovie[] testListArray = {};
    public static Communicator com;
    ImageView poster;
    RecyclerView rv;
    SharedPreferences sharedpreferences;
    ConnectivityManager connectivityManager;
    NetworkInfo activeNetworkInfo;

    public MainActivityFragment() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", list);
        outState.putParcelableArrayList("favourites", favList);
        outState.putParcelableArrayList("test", testList);
//        outState.putBoolean("sort",sort);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        fetchMovies = new FetchMovies(getActivity(),getView(),getContext());
        weather1 = new FetchMovies(getActivity(),getView(),getContext());
        com = (Communicator) getActivity();
        movieAdapter = new MovieAdapter(getActivity(),getView(),getContext());
        if (savedInstanceState == null) {
            list = new ArrayList<>(Arrays.asList(movieList));
            testList = new ArrayList<>(Arrays.asList(testListArray));
            weather1 = new FetchMovies(getActivity(),getView(),getContext());
            sort = true;
            sharedpreferences = getActivity().getSharedPreferences("mypref", Context.MODE_PRIVATE);
        } else {
            list = savedInstanceState.getParcelableArrayList("movies");
            favList = savedInstanceState.getParcelableArrayList("favourites");
            testList = savedInstanceState.getParcelableArrayList("test");
//            sort = savedInstanceState.getBoolean("sort");
        }
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
            FetchMovies weather = new FetchMovies(getActivity(),getView(),getContext());
            sort = true;
            Snackbar.make(getView(), getResources().getText(R.string.sort_rat), Snackbar.LENGTH_LONG).show();
            weather.execute();
            weather.progressDialog.show();
            rv.setAdapter(adapter);
            return true;
        }
        if (id == R.id.action_sort) {
            FetchMovies weather = new FetchMovies(getActivity(),getView(),getContext());
            sort = false;
            Snackbar.make(getView(), getResources().getText(R.string.sort_pop), Snackbar.LENGTH_LONG).show();
            weather.execute();
            weather.progressDialog.show();
            rv.setAdapter(adapter);
            return true;
        }
        if (id == R.id.action_fav) {
            rv.setAdapter(favAdapter);
            sharedpreferences = getActivity().getSharedPreferences("mypref", Context.MODE_PRIVATE);
            for (int i = 0; i < 40; i++) {
                try {
                    if (sharedpreferences.contains(testList.get(i).getId())) {    //If the movie is stored in the sharedPref
                        if (favList.contains(testList.get(i))) {
                        } else {
                            favList.add(testList.get(i));
                            favAdapter.notifyDataSetChanged();
                        }
                        //Add that movie to the favList arraylist
                    } else {
                        favList.remove(testList.get(i));
                        favAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        favList = new ArrayList<>(Arrays.asList(favouriteList));
        adapter = new MovieAdapter(getActivity(), list);
        favAdapter = new MovieAdapter(getActivity(), favList);
        rv = (RecyclerView) inflater.inflate(R.layout.fragment_main, container, false);
        rv.setLayoutManager(new GridLayoutManager(rv.getContext(), 2));
        rv.setAdapter(adapter);
        poster = (ImageView) rv.findViewById(R.id.movie_poster_image);
        com = (Communicator) getActivity();
        return rv;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(activeNetworkInfo == null){
            Snackbar.make(getView(),getResources().getText(R.string.no_net),Snackbar.LENGTH_LONG).show();
        }
        else{
            weather1.execute();
            weather1.progressDialog.show();
        }
    }

}
