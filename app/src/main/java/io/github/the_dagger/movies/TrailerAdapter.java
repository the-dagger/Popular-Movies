package io.github.the_dagger.movies;

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
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {
    List<Trailers.SingleTrailer> list;
    public TrailerAdapter(List<Trailers.SingleTrailer> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_trailer_item,parent,false);
//        Toast.makeText(parent.getContext(),list.get(0).getKey(),Toast.LENGTH_LONG).show();
        Log.e("Adapter","I ran");
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        Trailers.SingleTrailer trailer = list.get(position);
        holder.i.setImageResource(R.drawable.placeholder);
        holder.setImage(holder.i,position);
//        Picasso.with(c).load("http://img.youtube.com/vi/"+holder.trailer.key+"/0.jpg").error(R.drawable.placeholder).into(holder.i);
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
        public void setImage(ImageView v,int position){
            Picasso.with(itemView.getContext()).load("http://img.youtube.com/vi/"+list.get(position).getKey()+"/0.jpg").error(R.drawable.placeholder).placeholder(R.drawable.placeholder).into(v);
        }
    }
    public void swapList(List<Trailers.SingleTrailer> items){
        this.list = items;
        notifyDataSetChanged();
    }
}
