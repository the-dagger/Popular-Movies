package io.github.the_dagger.movies.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by the-dagger on 3/4/16.
 */
@Database(version = MovieDatabase.VERSION)
public class MovieDatabase {
    public static final int VERSION = 1;

    @Table(MovieColumns.class) public static final String MOVIE_TABLE = "movie";
}
