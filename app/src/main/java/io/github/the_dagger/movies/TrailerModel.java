package io.github.the_dagger.movies;

/**
 * Created by Harshit on 2/11/2016.
 */
public class TrailerModel {
    private int id;
    private String iso_639_1;
    private String key;
    private String name;
    private String site;
    private String size;
    private String type;

    public String getTrailer(){
        return "https://www.youtube.com/watch?v="+key;
    }
}
