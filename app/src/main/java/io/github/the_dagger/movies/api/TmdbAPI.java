package io.github.the_dagger.movies.api;

import io.github.the_dagger.movies.BuildConfig;
import io.github.the_dagger.movies.objects.Reviews;
import io.github.the_dagger.movies.objects.Trailers;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Harshit on 2/13/2016.
 */
    public interface TmdbAPI {
        @GET ("movie/{id}/videos?api_key="+ BuildConfig.MOBDB_API_KEY)
        Call<Trailers> getTrailers(
            @Path("id") int id

        );
        @GET ("movie/{id}/reviews?api_key="+BuildConfig.MOBDB_API_KEY)
        Call<Reviews> getReview(
                @Path("id") int id
        );
    }
