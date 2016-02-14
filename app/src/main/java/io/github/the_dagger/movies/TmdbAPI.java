package io.github.the_dagger.movies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Harshit on 2/13/2016.
 */
    public interface TmdbAPI {
        @GET ("movie/{id}/videos?api_key=9ee088a6d3ed11d3c10ee27466d39427")
        Call<Trailers> getTrailers(
            @Path("id") String id

        );
        @GET ("movie/{id}/reviews?api_key=9ee088a6d3ed11d3c10ee27466d39427")
        Call<Reviews> getReview(
                @Path("id") String id
        );
    }
