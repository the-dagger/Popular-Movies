package io.github.the_dagger.movies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Harshit on 2/12/2016.
 */
public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.ViewHolder> {
    List<Trailers.SingleTrailer> list;
    Context c;
    public TrailersAdapter(List<Trailers.SingleTrailer> list,Context c) {
        Log.e("Constructor","I ran");//Gets Executed
        this.list = list;
        this.c = c;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_trailer_item,parent,false);
        Log.e("onCreate","I ran"); //Isn't Executed
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.with(c).load("http://img.youtube.com/vi/"+list.get(position).getKey()+"/0.jpg").error(R.drawable.placeholder).placeholder(R.drawable.placeholder).into(holder.i);
        Log.e("onBind","I ran"); //Isn't Ecexuted
    }

    @Override
    public int getItemCount() {
        if (list == null){
            return -1;
        }
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView i;
        public ViewHolder(View itemView) {
            super(itemView);
            i = (ImageView) itemView.findViewById(R.id.singleTrailerImageView);
        }
    }
    public void swapList(List<Trailers.SingleTrailer> items){
        this.list = items;
        notifyDataSetChanged();
    }
}