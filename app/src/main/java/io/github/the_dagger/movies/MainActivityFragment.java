package io.github.the_dagger.movies;

import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;
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

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment{
    MovieAdapter adapter;
    Boolean sort = false;
    String movieDbUrl = null;
    SingleMovie[] movieDetails = new SingleMovie[20];
    ArrayList<SingleMovie> list;
    static boolean tabletSize;
    String MOVIE_ID;
    MovieDetails weather1;
    LinearLayout l;
    String Base_URL = "http://api.themoviedb.org/3/";
    SingleMovie[] movieList = {};
    Communicator com;
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
            list = new ArrayList<>(Arrays.asList(movieList));
            weather1 = new MovieDetails();
            weather1.execute();
        }
        else {
            list = savedInstanceState.getParcelableArrayList("movies");
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
        RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.fragment_main,container,false);
        rv.setLayoutManager(new GridLayoutManager(rv.getContext(),2));
        rv.setAdapter(adapter);
        com = (Communicator) getActivity();
        return rv;
    }

    public class MovieDetails extends AsyncTask<Void, Void, SingleMovie[]> {
        @Override
        protected void onPostExecute(SingleMovie[] singleMovies) {
            if (singleMovies != null) {
                list.clear();
                for (int i = 0; i < singleMovies.length; i++) {
                    SingleMovie oneMovie = singleMovies[i];
                    list.add(oneMovie);
                }
                com.respond(singleMovies[0]);
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
                MOVIE_ID = currentMovie.getString("id");
                String tempbackDropImage = baseURL + currentMovie.getString("backdrop_path");
                String tempreleaseDate = currentMovie.getString("release_date");
                String movietempOverView = currentMovie.getString("overview");
                String temprating = currentMovie.getString("vote_average");
                String movietitle = currentMovie.getString(MDB_TITLE);
                String moviePosterendURL = currentMovie.getString(MDB_POSTER);
                String moviePosterURL = baseURL + moviePosterendURL;
                movieDetails[i] = new SingleMovie(moviePosterURL, movietitle,movietempOverView,temprating,tempreleaseDate,tempbackDropImage,MOVIE_ID);
            }
            return movieDetails;
        }

        @Override
        protected SingleMovie[] doInBackground(Void... params) {
            try {
                URL url;
                if (sort)
                    url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key="+BuildConfig.MOBDB_API_KEY);
                else
                    url = new URL("http://api.themoviedb.org/3/discover/movie?&sort_by=popularity.desc&api_key="+BuildConfig.MOBDB_API_KEY);
                Log.v(LOG_TAG, String.valueOf(url));
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item,parent,false);
            return  new ViewHolder(view);
//            return h;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.tView.setText(listSM.get(position).movieTitle);
            Picasso.with(this.c).load(listSM.get(position).movieImage).error(R.drawable.placeholder).into(holder.iView, PicassoPalette.with(listSM.get(position).movieImage, holder.iView).use(PicassoPalette.Profile.MUTED)
                    .intoBackground(holder.tView));
            com = (Communicator) getActivity();
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tabletSize = getResources().getBoolean(R.bool.isTab);
                if (!tabletSize) {
                    Intent switchIntent = new Intent(getContext(), DetailsActivity.class)
                            .putExtra("Poster", listSM.get(position));
                    startActivity(switchIntent);}
                else{
                com.respond(listSM.get(position));}

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
