package io.github.the_dagger.movies;

import io.github.the_dagger.movies.model.Trailer;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Harshit on 2/11/2016.
 */

    public interface TmdbApi{
        @GET("movie/{id}/videos&api_key="+BuildConfig.MOBDB_API_KEY)
        Call<Trailer> getTrailers(
                @Path("id") String id
        );
    }
