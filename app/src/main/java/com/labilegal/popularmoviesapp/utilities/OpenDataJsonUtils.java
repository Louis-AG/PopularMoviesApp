package com.labilegal.popularmoviesapp.utilities;

import android.content.Context;
import android.util.Log;

import com.labilegal.popularmoviesapp.data.Movie;
import com.labilegal.popularmoviesapp.data.MovieList;
import com.labilegal.popularmoviesapp.data.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Created by louisag on 01/02/2017.
 */

public final class OpenDataJsonUtils {

    private static final String TAG = OpenDataJsonUtils.class.getSimpleName();

    // Constante pour les fonctions
    static final String DM_MESSAGE_CODE = "cod";

    static final String DM_RESULTS = "results";
    static final String DM_ID = "id";
    static final String DM_ORIGINAL_TITLE = "original_title";
    static final String DM_POSTER_PATH = "poster_path";
    static final String DM_OVERVIEW = "overview";
    static final String DM_VOTE_AVERAGE = "vote_average";
    static final String DM_RELEASE_DATE = "release_date";

    static final String DM_AUTHOR = "author";
    static final String DM_CONTENT = "content";
    static final String DM_URL = "url";

    /**
     * Others information
     *
     * final String DM_PAGE = "page";
     * final String DM_ADULT = "adult";
     * final String DM_GENRE_IDS = "genre_ids";
     *
     * final String DM_ORIGINAL_LANGUAGE = "original_language";
     * final String DM_TITLE = "title";
     * final String DM_BACKDROP_PATH = "backdrop_path";
     * final String DM_POPULARITY = "popularity";
     * final String DM_VOTE_COUNT = "vote_count";
     * final String DM_VIDEO = "video";
     */

    public static MovieList getSimpleDataMoviesFromJsonWithMovieClass(String stringJson) throws JSONException {

        /* String array to hold each movies String */
        MovieList parsedDataMovies = null;

        JSONObject forecastJson = new JSONObject(stringJson);

        /* Is there an error? */
        if (forecastJson.has(DM_MESSAGE_CODE)) {
            int errorCode = forecastJson.getInt(DM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray resultsArray = forecastJson.getJSONArray(DM_RESULTS);
        parsedDataMovies = new MovieList();//resultsArray.length()


        for (int i = 0; i < resultsArray.length(); i++){
            JSONObject movieForecast = resultsArray.getJSONObject(i);

            Movie movie = new Movie(movieForecast.getInt(DM_ID)
                    , movieForecast.getString(DM_ORIGINAL_TITLE)
                    , movieForecast.getString(DM_POSTER_PATH)
                    , movieForecast.getString(DM_OVERVIEW)
                    , movieForecast.getInt(DM_VOTE_AVERAGE)
                    , movieForecast.getString(DM_RELEASE_DATE));

            parsedDataMovies.add(movie);

        }

        return parsedDataMovies;
    }

    public static String[] getSimpleDataStringFromJson(String stringJson) throws JSONException {

        /* String array to hold each movies String */
        String[] parsedDataString = null;

        JSONObject forecastJson = new JSONObject(stringJson);

        /* Is there an error? */
        if (forecastJson.has(DM_MESSAGE_CODE)) {
            int errorCode = forecastJson.getInt(DM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray resultsArray = forecastJson.getJSONArray(DM_RESULTS);
        parsedDataString = new String[resultsArray.length()];


        for (int i = 0; i < resultsArray.length(); i++){
            JSONObject trailerForecast = resultsArray.getJSONObject(i);

            String string = trailerForecast.getString("key");
            Log.v(TAG, "String : " + string);

            parsedDataString[i] = string;

        }

        return parsedDataString;
    }

    public static Review[] getSimpleDataReviewsFromJson(String stringJson) throws JSONException {

        /* String array to hold each movies String */
        Review[] parsedDataReviews = null;

        JSONObject forecastJson = new JSONObject(stringJson);

        /* Is there an error? */
        if (forecastJson.has(DM_MESSAGE_CODE)) {
            int errorCode = forecastJson.getInt(DM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray resultsArray = forecastJson.getJSONArray(DM_RESULTS);
        parsedDataReviews = new Review[resultsArray.length()];


        for (int i = 0; i < resultsArray.length(); i++){
            JSONObject reviewForecast = resultsArray.getJSONObject(i);

            Review review = new Review(reviewForecast.getString(DM_ID)
                    , reviewForecast.getString(DM_AUTHOR)
                    , reviewForecast.getString(DM_CONTENT)
                    , reviewForecast.getString(DM_URL));

            parsedDataReviews[i] = review;
        }

        return parsedDataReviews;
    }
}
