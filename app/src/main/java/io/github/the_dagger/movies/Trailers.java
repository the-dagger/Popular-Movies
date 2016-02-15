package io.github.the_dagger.movies;

import java.util.List;

/**
 * Created by Harshit on 2/13/2016.
 */
public class Trailers {
    public List<SingleTrailer> trailers;
    public List<SingleTrailer> getTrailers(){
        return trailers;
    }
    public void setTrailers(List<SingleTrailer> trailers) {
        this.trailers = trailers;
    }
    public static class SingleTrailer{
        String key;
        public String getKey(){
            return key;
        }
        public void setKey(String key){
            this.key = key;
        }
    }
}
