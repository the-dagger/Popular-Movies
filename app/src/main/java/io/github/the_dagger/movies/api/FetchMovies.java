package io.github.the_dagger.movies.api;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import io.github.the_dagger.movies.BuildConfig;
import io.github.the_dagger.movies.R;
import io.github.the_dagger.movies.fragments.MainActivityFragment;
import io.github.the_dagger.movies.objects.SingleMovie;

/**
 * Created by the-dagger on 9/4/16.
 */
public class FetchMovies extends AsyncTask<Void, Void, SingleMovie[]> {
    Activity activity;
    View v;
    ConnectivityManager connectivityManager;
    NetworkInfo activeNetworkInfo;
    public static boolean hasLoaded = false;   //For running 2 asynctasks on first launch
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    String movieinfo = null;
    String movieDbUrl = null;
    Context c;
    MainActivityFragment mainActivityFragment = new MainActivityFragment();
    public ProgressDialog progressDialog;

    public FetchMovies(Activity a, View v, Context c) {
        this.activity = a;
        this.v = v;
        progressDialog = new ProgressDialog(c);
        this.c = c;
    }

    Communicator com;

    @Override
    protected void onPreExecute() {
        progressDialog.setMessage("Hold On...");
    }

    @Override
    protected void onPostExecute(SingleMovie[] singleMovies) {
        com = (Communicator) activity;
        connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            progressDialog.dismiss();
            try {
                Snackbar.make(v, activity.getResources().getText(R.string.no_net), Snackbar.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            progressDialog.dismiss();
            if (singleMovies != null) {
                MainActivityFragment.list.clear();
                for (int i = 0; i < singleMovies.length; i++) {
                    SingleMovie oneMovie = singleMovies[i];
                    MainActivityFragment.list.add(oneMovie);
                }
                com.respond(singleMovies[0]);
                hasLoaded = true;
                MainActivityFragment.adapter.notifyDataSetChanged();       //Don't show the ratings movie while loading it for testList
        }}
        super.onPostExecute(singleMovies);
    }

    private SingleMovie[] getmovieData(String movieInfo)
            throws JSONException {
        final String MDB_RESULT = "results";
        final String MDB_TITLE = "title";
        final String MDB_POSTER = "poster_path";

        JSONObject moviejson = new JSONObject(movieInfo);
        JSONArray movieArray = moviejson.getJSONArray(MDB_RESULT);
        String baseURL = "http://image.tmdb.org/t/p/w500/";
        for (int i = 0; i < 20; i++) {
            JSONObject currentMovie = movieArray.getJSONObject(i);
            int movieID = Integer.parseInt(currentMovie.getString("id"));
            String tempbackDropImage = baseURL + currentMovie.getString("backdrop_path");
            String tempreleaseDate = currentMovie.getString("release_date");
            String movietempOverView = currentMovie.getString("overview");
            String temprating = currentMovie.getString("vote_average");
            String movietitle = currentMovie.getString(MDB_TITLE);
            String moviePosterendURL = currentMovie.getString(MDB_POSTER);
            String moviePosterURL = baseURL + moviePosterendURL;
            String language = currentMovie.getString("original_language");
            MainActivityFragment.movieDetails[i] = new SingleMovie(moviePosterURL, movietitle, movietempOverView, temprating, tempreleaseDate, tempbackDropImage, movieID, language);
        }
        return MainActivityFragment.movieDetails;
    }

    @Override
    protected SingleMovie[] doInBackground(Void... params) {
        try {
            URL url;
            if (MainActivityFragment.sort) {
                url = new URL("http://api.themoviedb.org/3/movie/popular?api_key=" + BuildConfig.MOBDB_API_KEY); //sort by popularity by default
            } else {
                url = new URL("http://api.themoviedb.org/3/movie/top_rated?api_key=" + BuildConfig.MOBDB_API_KEY);  //sort by ratings
            }

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
