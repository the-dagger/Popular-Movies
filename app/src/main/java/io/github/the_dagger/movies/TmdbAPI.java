package io.github.the_dagger.movies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Harshit on 2/13/2016.
 */
    public interface TmdbAPI {
        @GET ("movie/{id}/videos?api_key="+BuildConfig.MOBDB_API_KEY)
        Call<Trailers> getTrailers(
            @Path("id") String id

        );
        @GET ("movie/{id}/reviews?api_key="+BuildConfig.MOBDB_API_KEY)
        Call<Reviews> getReview(
                @Path("id") String id
        );
    }
