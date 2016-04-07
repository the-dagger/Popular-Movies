package io.github.the_dagger.movies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.github.the_dagger.movies.R;
import io.github.the_dagger.movies.objects.Reviews;

/**
 * Created by Harshit on 2/15/2016.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>{
    Context c;
    int position;
    List<Reviews.SingleReview> listReview;
    boolean isTextViewClicked = false;

    public ReviewAdapter(List<Reviews.SingleReview> listReview,Context c){
        this.listReview = listReview;
        this.c = c;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_review_item,parent,false);
        return new ViewHolder(itemView); //What is the use of this command?
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        this.position = position;
        Reviews.SingleReview review = listReview.get(position);
        if(getItemCount() == -1){
            holder.t.setText("No Reviews");
            holder.t.setGravity(View.TEXT_ALIGNMENT_CENTER);
        }
        else{
            holder.t.setText(review.getContent());
        }

    }

    @Override
    public int getItemCount() {
        if (listReview == null){
            return -1;
        }
        return listReview.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView t;
        public ViewHolder(View v){
            super(v);
            t = (TextView) v.findViewById(R.id.review_single_item);
            t.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            Uri reviewLink = Uri.parse(listReview.get(position).getUrl());
//            Intent reviewIntent = new Intent(Intent.ACTION_VIEW,reviewLink);
//            c.startActivity(reviewIntent);

            if(isTextViewClicked){
                //This will shrink textview to 2 lines if it is expanded.
                t.setMaxLines(4);
                isTextViewClicked = false;
            } else {
                //This will expand the textview if it is of 2 lines
                t.setMaxLines(Integer.MAX_VALUE);
                isTextViewClicked = true;
            }
        }
    }

    public void swapList(List<Reviews.SingleReview> list){
        this.listReview = list;
        notifyDataSetChanged();
    }
}
