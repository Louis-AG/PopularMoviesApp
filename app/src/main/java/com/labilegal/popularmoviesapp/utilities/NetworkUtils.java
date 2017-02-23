package com.labilegal.popularmoviesapp.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;

import com.labilegal.popularmoviesapp.data.Movie;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by louisag on 31/01/2017.
 */

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    // API KEY
    private static final String STATIC_API_KEY =
            "";

    // Url de base
    private static final String STATIC_HTTPS_BASE_URL =
            "https://api.themoviedb.org/3/";

    // Url pour image
    private static final String STATIC_HTTPS_BASE_IMAGE_URL =
            "https://image.tmdb.org/t/p/";

    // Paramètre
    private static final String PATH_MOVIE = "movie";
    private static final String PATH_TOP_RATED = "top_rated";
    private static final String PATH_POPULAR = "popular";

    private static final String SUP_PARAMETRE_API_KEY = "api_key";
    private static final String SUP_PARAMETRE_LANGUAGE = "language";
    private static final String SUP_PARAMETRE_PAGE = "page";

    // Valeur
    private static final String VALEUR_SIZE_W185 = "w185";
    private static final String VALEUR_SIZE_W500 = "w500";
    private static final String VALEUR_SIZE_W780 = "w780";
    private static final String VALEUR_FR = "fr";
    private static final String VALEUR_PAGE_3 = "3";

    /*
    Build the Url to show most popular movies
    */
    public static URL buildUrlDiscoverMovieSortByMostPopular() {
        Uri builtUri = Uri.parse(STATIC_HTTPS_BASE_URL).buildUpon()
                .appendPath(PATH_MOVIE)
                .appendPath(PATH_POPULAR)
                .appendQueryParameter(SUP_PARAMETRE_API_KEY, STATIC_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Built buildUrlDiscoverMovieSortByMostPopular : " + url);
        return url;
    }

    public static URL buildUrlDiscoverMovieSortByMostPopularOtherPage(int pageIndex) {
        Uri builtUri = Uri.parse(STATIC_HTTPS_BASE_URL).buildUpon()
                .appendPath(PATH_MOVIE)
                .appendPath(PATH_POPULAR)
                .appendQueryParameter(SUP_PARAMETRE_PAGE, String.valueOf(pageIndex))
                .appendQueryParameter(SUP_PARAMETRE_API_KEY, STATIC_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Built buildUrlDiscoverMovieSortByMostPopularOtherPage : " + url);
        return url;
    }

    /*
    Build the Url to show top rated movies
    */
    public static URL buildUrlDiscoverMovieSortByTopRated() {
        Uri builtUri = Uri.parse(STATIC_HTTPS_BASE_URL).buildUpon()
                .appendPath(PATH_MOVIE)
                .appendPath(PATH_TOP_RATED)
                .appendQueryParameter(SUP_PARAMETRE_API_KEY, STATIC_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Built buildUrlDiscoverMovieSortByTopRated : " + url);
        return url;
    }

    public static URL buildUrlDiscoverMovieSortByTopRatedOtherPage(int pageIndex) {
        Uri builtUri = Uri.parse(STATIC_HTTPS_BASE_URL).buildUpon()
                .appendPath(PATH_MOVIE)
                .appendPath(PATH_TOP_RATED)
                .appendQueryParameter(SUP_PARAMETRE_PAGE, String.valueOf(pageIndex))
                .appendQueryParameter(SUP_PARAMETRE_API_KEY, STATIC_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Built buildUrlDiscoverMovieSortByTopRatedOtherPage : " + url);
        return url;
    }

    /*
    Build the Url to get image
    */
    public static URL buildUrlGetImageW185(String path) {
        Uri builtUri = Uri.parse(STATIC_HTTPS_BASE_IMAGE_URL + VALEUR_SIZE_W185 + path).buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Built buildUrlGetImageW185 : " + url);
        return url;
    }

    public static URL buildUrlGetImageW500(String path) {
        Uri builtUri = Uri.parse(STATIC_HTTPS_BASE_IMAGE_URL + VALEUR_SIZE_W500 + path).buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Built buildUrlGetImageW500 : " + url);
        return url;
    }

    public static URL buildUrlGetImageW780(String path) {
        Uri builtUri = Uri.parse(STATIC_HTTPS_BASE_IMAGE_URL + VALEUR_SIZE_W780 + path).buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Built buildUrlGetImageW780 : " + url);
        return url;
    }

    /*
    For selecting the good size of images for the device
     */
    public static URL choseSizeToLoad(Movie movie, Context context) {
        URL url;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        if (displayMetrics.densityDpi < DisplayMetrics.DENSITY_XHIGH ) {
            url = NetworkUtils.buildUrlGetImageW185(movie.getPosterPath());

        } else if ((DisplayMetrics.DENSITY_XHIGH <= displayMetrics.densityDpi)
                && (displayMetrics.densityDpi < DisplayMetrics.DENSITY_XXHIGH )) {
            url = NetworkUtils.buildUrlGetImageW500(movie.getPosterPath());

        } else if (DisplayMetrics.DENSITY_XXHIGH < displayMetrics.densityDpi) {
            url = NetworkUtils.buildUrlGetImageW780(movie.getPosterPath());

        } else{
            url = NetworkUtils.buildUrlGetImageW185(movie.getPosterPath());

        }
        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
