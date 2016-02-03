package io.github.the_dagger.movies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {
//    @Bind(R.id.movieDetailTitle)
//    TextView title;
//    @Bind(R.id.movieSummary) TextView overviewTextView;
//    @Bind(R.id.backdrop)
//    ImageView backDrop;
//    @Bind(R.id.releaseDate) TextView releaseTextView;
//    @Bind(R.id.posterImageDetail) ImageView posterImage;
//    @Bind(R.id.ratingBar1)
//    RatingBar rb;
//    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        ButterKnife.bind(getActivity());
        View view = inflater.inflate(R.layout.fragment_details, container, false);
//        title = (TextView) view.findViewById(R.id.movieDetailTitle);
//        overviewTextView = (TextView) view.findViewById(R.id.movieSummary);
//        backDrop = (ImageView) view.findViewById(R.id.backdrop);
//        releaseTextView = (TextView) view.findViewById(R.id.releaseDate);
//        posterImage = view.findViewById(R.id.posterImageDetail)
//        final SingleMovie movie = getActivity().getIntent().getParcelableExtra("Poster");
//        if(movie!=null) {
//            title.setText(movie.movieTitle);
//            Picasso.with(getContext()).load(movie.movieImage).error(R.drawable.placeholder).into(posterImage, PicassoPalette.with(movie.movieImage, posterImage).use(BitmapPalette.Profile.MUTED)
//            );
//            String overView = movie.movieOverView;
//            String summary = "";
//            float d = Float.parseFloat(movie.movieRating);
//            rb.setRating((Math.round(d) / 2));
//            releaseTextView.setText(movie.movieReleaseDate);
//            for (String sum : overView.split("(?<=[.])\\s+"))
//                if (summary == "")
//                    summary = sum;
//                else
//                    summary = summary + "\n" + sum;
//            overviewTextView.setText(summary);
//            Picasso.with(getContext()).load(movie.movieBackDropImage).into(backDrop);
//        }
        return view;
    }

}
