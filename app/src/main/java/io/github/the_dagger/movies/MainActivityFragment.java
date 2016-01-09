package io.github.the_dagger.movies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    MovieAdapter adapter;
    Boolean sort = false;
    String movieDbUrl = null;
    SingleMovie[] movieDetails = new SingleMovie[20];
    ArrayList<SingleMovie> list;
    SingleMovie[] movieList = {};
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
        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            list = new ArrayList<SingleMovie>(Arrays.asList(movieList));
            MovieDetails weather = new MovieDetails();
            weather.execute();
        }
        else {
            list = savedInstanceState.getParcelableArrayList("movies");
        }
        setHasOptionsMenu(true);
       }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater Inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Inflater.inflate(R.menu.moviefragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            MovieDetails weather = new MovieDetails();
            sort = true;
            Snackbar.make(getView(), "Sorted by Ratings", Snackbar.LENGTH_LONG).show();
            weather.execute();
            return true;
        }
        if (id == R.id.action_sort) {
            MovieDetails weather = new MovieDetails();
            sort = false;
            Snackbar.make(getView(), "Sorted by Popularity", Snackbar.LENGTH_LONG).show();
            weather.execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    String movieinfo = null;
    private final String LOG_TAG = MovieDetails.class.getSimpleName();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        adapter = new MovieAdapter(getActivity(), list);
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridview = (GridView) rootView.findViewById(R.id.gridView);
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                Intent switchIntent = new Intent(getActivity(), DetailsActivity.class)
                        .putExtra("PosterImage", adapter.getItem(position))
                        .putExtra("Poster", adapter.getItem(position))
                        .putExtra("OverView", adapter.getItem(position))
                        .putExtra("Backdrop", adapter.getItem(position))
                        .putExtra("Rating", adapter.getItem(position))
                        .putExtra("ReleaseDate", adapter.getItem(position));
                startActivity(switchIntent);
            }
        });
        return rootView;
    }

    public class MovieDetails extends AsyncTask<Void, Void, SingleMovie[]> {
        @Override
        protected void onPostExecute(SingleMovie[] singleMovies) {
            if (singleMovies != null) {
                adapter.clear();
                for (int i = 0; i < singleMovies.length; i++) {
                    SingleMovie oneMovie = singleMovies[i];
                    adapter.add(oneMovie);
                }
            }
            super.onPostExecute(singleMovies);
        }

        private SingleMovie[] getmovieData(String movieInfo)
                throws JSONException {
            final String MDB_RESULT = "results";
            final String MDB_TITLE = "title";
            final String MDB_POSTER = "poster_path";
            JSONObject moviejson = new JSONObject(movieInfo);
            JSONArray movieArray = moviejson.getJSONArray(MDB_RESULT);
            String baseURL = "http://image.tmdb.org/t/p/w342/";
            for (int i = 0; i < 20; i++) {
                JSONObject currentMovie = movieArray.getJSONObject(i);
                String tempbackDropImage = baseURL + currentMovie.getString("backdrop_path");
                String tempreleaseDate = currentMovie.getString("release_date");
                String movietempOverView = currentMovie.getString("overview");
                String temprating = currentMovie.getString("vote_average");
                String movietitle = currentMovie.getString(MDB_TITLE);
                String moviePosterendURL = currentMovie.getString(MDB_POSTER);
                String moviePosterURL = baseURL + moviePosterendURL;
                movieDetails[i] = new SingleMovie(moviePosterURL, movietitle,movietempOverView,temprating,tempreleaseDate,tempbackDropImage);
            }
            return movieDetails;
        }

        @Override
        protected SingleMovie[] doInBackground(Void... params) {
            try {
                URL url;
                if (sort)
                    url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=9ee088a6d3ed11d3c10ee27466d39427");
                else
                    url = new URL("http://api.themoviedb.org/3/discover/movie?&sort_by=popularity.desc&api_key=9ee088a6d3ed11d3c10ee27466d39427");
                movieDbUrl = url.toString();
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieinfo = buffer.toString();
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            try {
                return getmovieData(movieinfo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


}
