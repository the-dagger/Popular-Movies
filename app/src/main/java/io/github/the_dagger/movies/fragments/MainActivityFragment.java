package io.github.the_dagger.movies.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.picassopalette.PicassoPalette;
import com.squareup.picasso.Picasso;

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
import java.util.List;

import io.github.the_dagger.movies.BuildConfig;
import io.github.the_dagger.movies.DetailsActivity;
import io.github.the_dagger.movies.MainActivity;
import io.github.the_dagger.movies.R;
import io.github.the_dagger.movies.api.Communicator;
import io.github.the_dagger.movies.objects.SingleMovie;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    MovieAdapter adapter, favAdapter;
    Boolean sort = false;
    String movieDbUrl = null;
    SingleMovie[] movieDetails = new SingleMovie[20];
    ArrayList<SingleMovie> list;
    static ArrayList<SingleMovie> testList;
    static ArrayList<SingleMovie> favList;
    static boolean tabletSize;
    FetchMovies weather1;
    boolean debug = true;  //For running 2 asynctasks on first launch
    SingleMovie[] movieList = {};
    SingleMovie[] favouriteList = {};
    SingleMovie[] testListArray = {};
    Communicator com;
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
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (savedInstanceState == null) {
            list = new ArrayList<>(Arrays.asList(movieList));
            testList = new ArrayList<>(Arrays.asList(testListArray));
            weather1 = new FetchMovies();
            sort = true;
            if(activeNetworkInfo == null){

            }
            else{
                weather1.execute();
            }
            sharedpreferences = getActivity().getSharedPreferences("mypref", Context.MODE_PRIVATE);
        } else {
            list = savedInstanceState.getParcelableArrayList("movies");
            favList = savedInstanceState.getParcelableArrayList("favourites");
            testList = savedInstanceState.getParcelableArrayList("test");
            sort = true;
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
            FetchMovies weather = new FetchMovies();
            sort = true;
            Snackbar.make(getView(), "Sorted by Ratings", Snackbar.LENGTH_LONG).show();
            weather.execute();
            rv.setAdapter(adapter);
            return true;
        }
        if (id == R.id.action_sort) {
            FetchMovies weather = new FetchMovies();
            sort = false;
            Snackbar.make(getView(), "Sorted by Popularity", Snackbar.LENGTH_LONG).show();
            weather.execute();
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

    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    String movieinfo = null;


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

    public class FetchMovies extends AsyncTask<Void, Void, SingleMovie[]> {
        private ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Hold On...");
            this.dialog.show();
        }

        @Override
        protected void onPostExecute(SingleMovie[] singleMovies) {
            connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if(activeNetworkInfo == null){
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    Snackbar.make(getView(),"No network Connection (._.')",Snackbar.LENGTH_LONG).show();
                }
            }
            else {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (singleMovies != null) {
                    list.clear();
                    for (int i = 0; i < singleMovies.length; i++) {
                        SingleMovie oneMovie = singleMovies[i];
                        list.add(oneMovie);
                        testList.add(oneMovie);
                    }
                    com.respond(singleMovies[0]);
                }
                if (!debug)
                    adapter.notifyDataSetChanged();       //Don't show the ratings movie while loading it for testList
                if (debug) {
                    FetchMovies weatherdebug = new FetchMovies();   //Did this to load both popular and top rated in the testList for comparison with sharedPrefs
                    sort = false;
                    debug = false;
                    weatherdebug.execute();
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
            String baseURL = "http://image.tmdb.org/t/p/w500/";
            for (int i = 0; i < 20; i++) {
                JSONObject currentMovie = movieArray.getJSONObject(i);
                String movieID = currentMovie.getString("id");
                String tempbackDropImage = baseURL + currentMovie.getString("backdrop_path");
                String tempreleaseDate = currentMovie.getString("release_date");
                String movietempOverView = currentMovie.getString("overview");
                String temprating = currentMovie.getString("vote_average");
                String movietitle = currentMovie.getString(MDB_TITLE);
                String moviePosterendURL = currentMovie.getString(MDB_POSTER);
                String moviePosterURL = baseURL + moviePosterendURL;
                String language = currentMovie.getString("original_language");
                movieDetails[i] = new SingleMovie(moviePosterURL, movietitle, movietempOverView, temprating, tempreleaseDate, tempbackDropImage, movieID, language);
            }
            return movieDetails;
        }

        @Override
        protected SingleMovie[] doInBackground(Void... params) {
            try {
                URL url;
                if (sort) {
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

    //Movie Adapter
    public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

        List<SingleMovie> listSM;
        Context c;

        public MovieAdapter(FragmentActivity context, List<SingleMovie> resource) {
            c = context;
            listSM = resource;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.tView.setText(listSM.get(position).movieTitle);
            Picasso.with(this.c).load(listSM.get(position).movieImage).into(holder.iView, PicassoPalette.with(listSM.get(position).movieImage, holder.iView).use(PicassoPalette.Profile.MUTED_DARK)
                    .intoBackground(holder.tView));
            com = (Communicator) getActivity();
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tabletSize = getResources().getBoolean(R.bool.isTab);
                    try {
                        if (MainActivity.f.isInLayout()) {
                            com.respond(listSM.get(position));
                        }
                    } catch (Exception e) {
                        Intent switchIntent = new Intent(getContext(), DetailsActivity.class)
                                .putExtra("Poster", listSM.get(position));
                        startActivity(switchIntent);
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return listSM.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView iView;
            View mView;
            TextView tView;

            public ViewHolder(View itemView) {
                super(itemView);
                mView = itemView;
                iView = (ImageView) itemView.findViewById(R.id.movie_poster_image);
                tView = (TextView) itemView.findViewById(R.id.movie_name);
            }

        }
    }

}
