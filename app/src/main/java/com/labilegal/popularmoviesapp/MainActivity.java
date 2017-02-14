package com.labilegal.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.labilegal.popularmoviesapp.adapter.MoviesWithClassAdapter;
import com.labilegal.popularmoviesapp.data.Movie;
import com.labilegal.popularmoviesapp.utilities.FetchMoviesDataTask;
import com.labilegal.popularmoviesapp.utilities.NetworkUtils;
import com.labilegal.popularmoviesapp.utilities.OpenDataJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements MoviesWithClassAdapter.MoviesWithClassAdapterOnclickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();

    private MoviesWithClassAdapter mMoviesAdapter;

    @BindView(R.id.tv_indication) TextView mTextViewIndication;
    @BindView(R.id.tv_no_connection_message_display) TextView mTextViewNoConnection;
    @BindView(R.id.tv_error_message_display) TextView mTextViewErrorMessage;
    @BindView(R.id.pb_loading_indicator) ProgressBar mProgresBar;
    @BindView(R.id.rv_movies) RecyclerView mRecyclerViewMovies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        //LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        mRecyclerViewMovies.setLayoutManager(layoutManager);
        mRecyclerViewMovies.setHasFixedSize(false);
        mMoviesAdapter = new MoviesWithClassAdapter(this);
        mRecyclerViewMovies.setAdapter(mMoviesAdapter);

        displayMoviesData();
    }

    /*
    Functions for get Movies Data
     */
    private void displayMoviesData() {
        if (isOnline()) {
            hideNoConnection();
            URL url = NetworkUtils.buildUrlDiscoverMovieFr();
            new FetchMoviesDataTask(mRecyclerViewMovies,mMoviesAdapter, mTextViewErrorMessage,mProgresBar).execute(url);
            mTextViewIndication.setText("Home");
        } else {
            showNoConnection();
        }
    }

    private void displayMoviesDataSortByMostPopular() {
        if (isOnline()) {
            hideNoConnection();
            URL url = NetworkUtils.buildUrlDiscoverMovieSortByMostPopular();
            new FetchMoviesDataTask(mRecyclerViewMovies, mMoviesAdapter, mTextViewErrorMessage, mProgresBar).execute(url);
            mTextViewIndication.setText("Sort by Most Popular");
        } else {
            showNoConnection();
        }
    }

    private void displayMoviesDataSortByTopRated() {
        if (isOnline()) {
            hideNoConnection();
            URL url = NetworkUtils.buildUrlDiscoverMovieSortByTopRated();
            new FetchMoviesDataTask(mRecyclerViewMovies, mMoviesAdapter, mTextViewErrorMessage, mProgresBar).execute(url);
            mTextViewIndication.setText("Sort by Top Rated");
        } else {
            showNoConnection();
        }
    }

    /*
    Function view with the link on "http://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out"
     */
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /*
    Display functions
     */
    private void showNoConnection() {
        mTextViewNoConnection.setVisibility(View.VISIBLE);
    }

    private void hideNoConnection() {
        mTextViewNoConnection.setVisibility(View.INVISIBLE);
    }

    /*
    Intent for Detail
     */
    public void intentForDetail(Movie movie) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("movie", movie);
        startActivity(intent);
    }

    /*
    Click handler on grid
     */
    @Override
    public void onClick(Movie movieClicked) {
        /*if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, movieClicked.getOriginalTitle(), Toast.LENGTH_SHORT);
        mToast.show();*/

        intentForDetail(movieClicked);
    }

    /*
     For Create the menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }

    /*
    For Handle menu clicks
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Handle item selection
            case R.id.menu_home:
                mMoviesAdapter.setmMoviesData(null);
                displayMoviesData();
                return true;

            case R.id.menu_sort_popular:
                mMoviesAdapter.setmMoviesData(null);
                displayMoviesDataSortByMostPopular();
                return true;

            case R.id.menu_sort_top_rated:
                mMoviesAdapter.setmMoviesData(null);
                displayMoviesDataSortByTopRated();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
