package io.github.the_dagger.movies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Harshit on 2/15/2016.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>{

    List<Reviews.SingleReview> listReview;

    public ReviewAdapter(List<Reviews.SingleReview> listReview){
        this.listReview = listReview;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_resource_item,parent,false);
        return new ViewHolder(itemView); //What is the use of this command?
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        Log.e(getClass().getSimpleName(),"onBind called");
        Reviews.SingleReview review = listReview.get(position);
        holder.t.setText("Trailer"+position);
    }

    @Override
    public int getItemCount() {
        if (listReview == null){
            return -1;
        }
        return listReview.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView t;
        public ViewHolder(View v){
            super(v);
            t = (TextView) v.findViewById(R.id.review_single_item);
        }
    }

    public void swapList(List<Reviews.SingleReview> list){
        this.listReview = list;
        notifyDataSetChanged();
    }
}
