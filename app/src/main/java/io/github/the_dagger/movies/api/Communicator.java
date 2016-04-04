package io.github.the_dagger.movies.api;

import io.github.the_dagger.movies.objects.SingleMovie;

/**
 * Created by Harshit on 1/26/2016.
 */
public interface Communicator {
    void respond(SingleMovie movie);
}
