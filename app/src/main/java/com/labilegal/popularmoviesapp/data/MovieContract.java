package com.labilegal.popularmoviesapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by louisag on 01/03/2017.
 */

public class MovieContract {
    public static final String AUTHORITY = "com.labilegal.popularmoviesapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movies";
        public static final String COLUNM_MOVIE_ID = "movie_id";
        public static final String COLUNM_MOVIE_TITLE = "movie_title";
        public static final String COLUNM_MOVIE_POSTER_PATH = "movie_poster_path";
        public static final String COLUNM_MOVIE_OVERVIEW = "movie_overview";
        public static final String COLUNM_MOVIE_VOTE_AVERAGE = "movie_vote_average";
        public static final String COLUNM_MOVIE_RELEASE_DATE = "movie_release_date";

    }
}
