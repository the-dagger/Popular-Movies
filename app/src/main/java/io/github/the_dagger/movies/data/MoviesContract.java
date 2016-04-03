//package io.github.the_dagger.movies.data;
//
//import android.content.ContentResolver;
//import android.content.ContentUris;
//import android.net.Uri;
//import android.provider.BaseColumns;
//
///**
// * Created by Harshit on 2/24/2016.
// */
//public class MoviesContract {
//    public static final String CONTENT_AUTHORITY = "io.github.the_dagger.movies";
//    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
//
//    public static final class MovieEntry implements BaseColumns {
//        public static final String TABLE_MOVIES = "movies";
//        public static final String _ID = "_id";
//        public static final String COLUMN_POSTER = "poster";
//        public static final String COLUMN_DESCRIPTION = "description";
//        public static final String COLUMN_RELEASE_DATE = "release_date";
//        public static final String COLUMN_NAME = "movie_name";
//        public static final String MOVIE_ID = "movie_id";
//        public static final String COLUMN_BACKDROP = "backdrop";
//
//        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
//                .appendPath(TABLE_MOVIES).build();
//        public static final String CONTENT_DIR_TYPE =
//                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_MOVIES;
//        public static final String CONTENT_ITEM_TYPE =
//                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_MOVIES;
//
//        public static Uri buildMoviesUri(long id) {
//            return ContentUris.withAppendedId(CONTENT_URI, id);
//        }
//    }
//}
