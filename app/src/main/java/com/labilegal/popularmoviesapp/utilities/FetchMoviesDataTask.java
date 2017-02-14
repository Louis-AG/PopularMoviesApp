package com.labilegal.popularmoviesapp.utilities;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.labilegal.popularmoviesapp.adapter.MoviesWithClassAdapter;
import com.labilegal.popularmoviesapp.data.Movie;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/**
 * Created by louisag on 08/02/2017.
 */

    /*
  Task for get Movies Data
   */
public class FetchMoviesDataTask extends AsyncTask<URL, Void, Movie[]> {

    private RecyclerView mRecyclerViewMovies;
    private MoviesWithClassAdapter mMoviesAdapter;

    private TextView mTextViewErrorMessage;
    private ProgressBar mProgresBar;

    public FetchMoviesDataTask(RecyclerView mRecyclerViewMovies, MoviesWithClassAdapter mMoviesAdapter, TextView mTextViewErrorMessage, ProgressBar mProgresBar) {
        this.mRecyclerViewMovies = mRecyclerViewMovies;
        this.mMoviesAdapter = mMoviesAdapter;
        this.mTextViewErrorMessage = mTextViewErrorMessage;
        this.mProgresBar = mProgresBar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgresBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected Movie[] doInBackground(URL... params) {
        URL tUrl = params[0];

        Movie[] tResultsWithMovieClass = null;

        try {
            String jsonResults = NetworkUtils.getResponseFromHttpUrl(tUrl);

            tResultsWithMovieClass = OpenDataJsonUtils.getSimpleDataMoviesFromJsonWithMovieClass(jsonResults);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tResultsWithMovieClass;
    }

    @Override
    protected void onPostExecute(Movie[] m) {
        mProgresBar.setVisibility(View.INVISIBLE);
        if (m != null) {
            showJsonDataView();
            // Give Data To Adapter
            mMoviesAdapter.setmMoviesData(m);
        } else {
            showErrorMessage();
        }
    }

    /*
   Display functions
    */
    private void showJsonDataView() {
        mTextViewErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerViewMovies.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerViewMovies.setVisibility(View.INVISIBLE);
        mTextViewErrorMessage.setVisibility(View.VISIBLE);
    }
}



