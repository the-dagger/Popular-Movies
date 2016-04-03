package io.github.the_dagger.movies.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by the-dagger on 3/4/16.
 */
public interface MovieColumns {
    @DataType(DataType.Type.INTEGER)
    @PrimaryKey
    @AutoIncrement
    String _ID = "_id";
    @DataType(DataType.Type.TEXT)
    @NotNull
    String COLUMN_DESCRIPTION = "description";
    @DataType(DataType.Type.TEXT)
    @NotNull
    String COLUMN_RELEASE_DATE = "release_date";
    @DataType(DataType.Type.TEXT)
    @NotNull
    String COLUMN_NAME = "name";
    @DataType(DataType.Type.TEXT)
    @NotNull
    String MOVIE_ID = "m_id";
    @DataType(DataType.Type.TEXT)
    @NotNull
    String COLUMN_BACKDROP = "backdrop";
    @DataType(DataType.Type.TEXT)
    @NotNull
    String COLUMN_POSTER = "poster";
}
