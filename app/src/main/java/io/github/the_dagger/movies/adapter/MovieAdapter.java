package io.github.the_dagger.movies.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.picassopalette.PicassoPalette;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.github.the_dagger.movies.DetailsActivity;
import io.github.the_dagger.movies.MainActivity;
import io.github.the_dagger.movies.R;
import io.github.the_dagger.movies.fragments.MainActivityFragment;
import io.github.the_dagger.movies.objects.SingleMovie;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    List<SingleMovie> listSM;
    Context c;
    Activity a;
    public View view;
    MainActivityFragment mainActivityFragment = new MainActivityFragment();
    public MovieAdapter(Activity a, View v, Context c){
        this.a =a;
        this.view = v;
        this.c = c;
    }
    public MovieAdapter(FragmentActivity context, List<SingleMovie> resource) {
        c = context;
        listSM = resource;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tView.setText(listSM.get(position).movieTitle);
        Picasso.with(this.c).load(listSM.get(position).movieImage).into(holder.iView, PicassoPalette.with(listSM.get(position).movieImage, holder.iView).use(PicassoPalette.Profile.MUTED_DARK)
                .intoBackground(holder.tView).intoBackground(holder.dView));
        holder.dView.setText(listSM.get(position).movieReleaseDate);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (MainActivity.f.isInLayout()) {
                        mainActivityFragment.com.respond(listSM.get(position));
                    }
                } catch (Exception e) {
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(a, holder.iView,view.getResources().getString(R.string.activity_image_trans));
                    Intent switchIntent = new Intent(c, DetailsActivity.class)
                            .putExtra("Poster", listSM.get(position));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        c.startActivity(switchIntent,options.toBundle());
                    }
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
        TextView dView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            iView = (ImageView) itemView.findViewById(R.id.movie_poster_image);
            tView = (TextView) itemView.findViewById(R.id.movie_name);
            dView = (TextView) itemView.findViewById(R.id.releaseDateMainActivit);
        }

    }
}