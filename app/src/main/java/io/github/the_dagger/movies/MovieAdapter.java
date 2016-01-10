package io.github.the_dagger.movies;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.picassopalette.PicassoPalette;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Harshit on 1/6/2016.
 */
public class MovieAdapter extends ArrayAdapter<SingleMovie> {
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SingleMovie singleMovie = getItem(position);
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item, parent, false);
        ImageView poster = (ImageView) rootView.findViewById(R.id.movie_poster_image);
        TextView name = (TextView) rootView.findViewById(R.id.movie_name);
        Picasso.with(getContext()).load(singleMovie.movieImage).into(poster, PicassoPalette.with(singleMovie.movieImage, poster).use(PicassoPalette.Profile.MUTED)
                .intoBackground(name));
        name.setText(singleMovie.movieTitle);
        return rootView;
    }


    public MovieAdapter(FragmentActivity context, List<SingleMovie> resource) {
        super(context, 0, resource);
    }
}
