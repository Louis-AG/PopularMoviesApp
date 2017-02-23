package com.labilegal.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.labilegal.popularmoviesapp.adapter.MoviesWithClassAdapter;
import com.labilegal.popularmoviesapp.data.Movie;
import com.labilegal.popularmoviesapp.utilities.NetworkUtils;
import com.labilegal.popularmoviesapp.utilities.OpenDataJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v7.recyclerview.R.styleable.RecyclerView;


public class MainActivity extends AppCompatActivity implements MoviesWithClassAdapter.MoviesWithClassAdapterOnclickHandler,
        LoaderManager.LoaderCallbacks<Movie[]> {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int DISCOVER_MOVIE_SEARCH_LOADER = 22;

    private static final String STRING_FOR_ON_SAVE_MY_CHOICE = "mychoicesave";
    private String myChoice;

    private MoviesWithClassAdapter mMoviesAdapter;

    @BindView(R.id.tv_no_connection_message_display)
    TextView mTextViewNoConnection;
    @BindView(R.id.tv_error_message_display)
    TextView mTextViewErrorMessage;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar mProgresBar;
    @BindView(R.id.rv_movies)
    RecyclerView mRecyclerViewMovies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        //LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, calculateNoOfColumns(this));

        mRecyclerViewMovies.setLayoutManager(gridLayoutManager);
        mRecyclerViewMovies.setHasFixedSize(true);
        mMoviesAdapter = new MoviesWithClassAdapter(this, this);
        mRecyclerViewMovies.setAdapter(mMoviesAdapter);

        if (savedInstanceState != null) {
            myChoice = savedInstanceState.getString(STRING_FOR_ON_SAVE_MY_CHOICE);
        } else {
            myChoice = getString(R.string.sort_by_most_popular);
        }
        displayMoviesData();
    }

    /*
    Function view with the link on "http://stackoverflow.com/questions/33575731/gridlayoutmanager-how-to-auto-fit-columns"
     */
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.densityDpi;
        int noOfColumns = (int) Math.max(1, dpWidth);
        return noOfColumns;
    }

    /*
    Functions for get Movies Data
     */
    private void displayMoviesData() {
        if (isOnline()) {
            hideNoConnection();

            getSupportLoaderManager().restartLoader(DISCOVER_MOVIE_SEARCH_LOADER, null, this).forceLoad();

            switch (myChoice) {

                case "SortByMostPopular":
                    getSupportActionBar().setTitle(getString(R.string.menu_popular));
                    break;
                case "SortByTopRated":
                    getSupportActionBar().setTitle(getString(R.string.menu_top_rated));
                    break;
                default:
                    getSupportActionBar().setTitle(getString(R.string.app_name));
                    break;
            }
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

            case R.id.menu_sort_popular:
                mMoviesAdapter.setmMoviesData(null);
                myChoice = getString(R.string.sort_by_most_popular);
                displayMoviesData();
                return true;

            case R.id.menu_sort_top_rated:
                mMoviesAdapter.setmMoviesData(null);
                myChoice = getString(R.string.sort_by_top_rated);
                displayMoviesData();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STRING_FOR_ON_SAVE_MY_CHOICE, myChoice);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        myChoice = savedInstanceState.getString(STRING_FOR_ON_SAVE_MY_CHOICE);
    }

    /*
    Loader for movies infos
    */
    @Override
    public Loader<Movie[]> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Movie[]>(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (args == null) {
                    return;
                }
                mProgresBar.setVisibility(View.VISIBLE);
            }

            @Override
            public Movie[] loadInBackground() {
                URL tUrl;
                Log.v(TAG, " we are in loadInBackground " + myChoice);
                switch (myChoice) {
                    case "SortByMostPopular":
                        tUrl = NetworkUtils.buildUrlDiscoverMovieSortByMostPopular();
                        break;
                    case "SortByTopRated":
                        tUrl = NetworkUtils.buildUrlDiscoverMovieSortByTopRated();
                        break;
                    default:
                        return null;
                }

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

        };
    }

    @Override
    public void onLoadFinished(Loader<Movie[]> loader, Movie[] data) {
        mProgresBar.setVisibility(View.INVISIBLE);
        if (data != null) {
            showJsonDataView();
            // Give Data To Adapter
            mMoviesAdapter.setmMoviesData(data);
            mRecyclerViewMovies.setAdapter(mMoviesAdapter);
        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<Movie[]> loader) {
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

    /*
    class movie list

     */
}
