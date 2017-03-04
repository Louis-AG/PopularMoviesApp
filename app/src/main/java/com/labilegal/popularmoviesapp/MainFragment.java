package com.labilegal.popularmoviesapp;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.labilegal.popularmoviesapp.adapter.MoviesWithClassAdapter;
import com.labilegal.popularmoviesapp.data.Movie;
import com.labilegal.popularmoviesapp.data.MovieContract;
import com.labilegal.popularmoviesapp.data.MovieList;
import com.labilegal.popularmoviesapp.utilities.NetworkUtils;
import com.labilegal.popularmoviesapp.utilities.OpenDataJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/**
 * Created by louisag on 27/02/2017.
 */

public class MainFragment extends android.support.v4.app.Fragment implements MoviesWithClassAdapter.MoviesWithClassAdapterOnclickHandler {

    private static final int DISCOVER_MOVIE_SEARCH_LOADER = 22;
    private static final int FAVORITES_MOVIE_SEARCH_LOADER = 32;

    private static final String STRING_FOR_ON_SAVE_MY_CHOICE = "mychoicesave";
    private static final String STRING_FOR_ON_SAVE_MY_MOVIE_POSITION = "mymoviepositionsave";

    private String myChoice;
    private MoviesWithClassAdapter mMoviesAdapter;

    private LoaderManager.LoaderCallbacks<MovieList> dataLoaderForMovies
            = new LoaderManager.LoaderCallbacks<MovieList>() {
        @Override
        public Loader<MovieList> onCreateLoader(int id, final Bundle args) {
            return new AsyncTaskLoader<MovieList>(context) {
                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                    if (args == null) {
                        return;
                    }
                    mProgresBar.setVisibility(View.VISIBLE);
                }

                @Override
                public MovieList loadInBackground() {
                    URL tUrl;
                    //Log.v(TAG, " we are in loadInBackground " + myChoice);
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

                    MovieList tResultsWithMovieClass = null;

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
        public void onLoadFinished(Loader<MovieList> loader, MovieList data) {
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
        public void onLoaderReset(Loader<MovieList> loader) {
        }
    };

    private LoaderManager.LoaderCallbacks<MovieList> dataLoaderForMoviesFavorites
            = new LoaderManager.LoaderCallbacks<MovieList>() {
        @Override
        public Loader<MovieList> onCreateLoader(int id, final Bundle args) {
            return new AsyncTaskLoader<MovieList>(context) {
                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                    if (args == null) {
                        return;
                    }
                    mProgresBar.setVisibility(View.VISIBLE);
                }

                @Override
                public MovieList loadInBackground() {

                    MovieList allFavoritesMovies;

                    try {
                        Cursor cursor = context.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI
                                , null
                                , null
                                , null
                                , MovieContract.MovieEntry.COLUNM_MOVIE_TITLE);

                        allFavoritesMovies = new MovieList();//cursor.getCount()

                        for (int i = 0; i < cursor.getCount(); i++) {

                            int Id = cursor.getColumnIndex(MovieContract.MovieEntry.COLUNM_MOVIE_ID);
                            int OriginalTitleIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUNM_MOVIE_TITLE);
                            int PosterPathIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUNM_MOVIE_POSTER_PATH);
                            int OverviewIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUNM_MOVIE_OVERVIEW);
                            int VoteAverageIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUNM_MOVIE_VOTE_AVERAGE);
                            int ReleaseDateIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUNM_MOVIE_RELEASE_DATE);

                            cursor.moveToPosition(i);

                            Movie movie = new Movie(cursor.getInt(Id)
                                    , cursor.getString(OriginalTitleIndex)
                                    , cursor.getString(PosterPathIndex)
                                    , cursor.getString(OverviewIndex)
                                    , cursor.getInt(VoteAverageIndex)
                                    , cursor.getString(ReleaseDateIndex));

                            allFavoritesMovies.add(movie);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();

                        return null;
                    }


                    return allFavoritesMovies;
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<MovieList> loader, MovieList data) {
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
        public void onLoaderReset(Loader<MovieList> loader) {

        }
    };

    TextView mTextViewNoConnection;
    TextView mTextViewErrorMessage;
    ProgressBar mProgresBar;
    RecyclerView mRecyclerViewMovies;

    private Context context;

    private boolean mModeTablette;

    private GridLayoutManager gridLayoutManager;

    private EndlessRecyclerViewScrollListener scrollListener;

    OnHeadMovieSelectedListener mCallback;

    public interface OnHeadMovieSelectedListener {
        public void onMovieSelected(Movie movie);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = getActivity();

        try {
            mCallback = (OnHeadMovieSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadMovieSelectedListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mTextViewNoConnection = (TextView) view.findViewById(R.id.tv_no_connection_message_display);
        mTextViewErrorMessage = (TextView) view.findViewById(R.id.tv_error_message_display);
        mProgresBar = (ProgressBar) view.findViewById(R.id.pb_loading_indicator);
        mRecyclerViewMovies = (RecyclerView) view.findViewById(R.id.rv_movies);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context = getActivity();

        if (getArguments().containsKey("mModeTablette")) {
            mModeTablette = getArguments().getBoolean("mModeTablette");
        }

        if (mModeTablette == true) {
            gridLayoutManager = new GridLayoutManager(context, calculateNoOfColumns() / 2);
        } else {
            gridLayoutManager = new GridLayoutManager(context, calculateNoOfColumns());
        }

        mRecyclerViewMovies.setLayoutManager(gridLayoutManager);
        mRecyclerViewMovies.setHasFixedSize(true);

//        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//                loadNextDataFromApi(page);
//            }
//        };
//        mRecyclerViewMovies.addOnScrollListener(scrollListener);
//
        mMoviesAdapter = new MoviesWithClassAdapter(this, context);

        if (savedInstanceState != null) {
            if (savedInstanceState.getString(STRING_FOR_ON_SAVE_MY_CHOICE) != null) {
                myChoice = savedInstanceState.getString(STRING_FOR_ON_SAVE_MY_CHOICE);
                Parcelable listState = savedInstanceState.getParcelable(STRING_FOR_ON_SAVE_MY_MOVIE_POSITION);
                mRecyclerViewMovies.getLayoutManager().onRestoreInstanceState(listState);
            }
        } else {
            myChoice = getString(R.string.sort_by_most_popular);
            mRecyclerViewMovies.setAdapter(mMoviesAdapter);
        }
        switch (myChoice) {

            case "SortByMostPopular":
                //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.menu_popular));
                displayMoviesData();
                break;
            case "SortByTopRated":
                //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.menu_top_rated));
                displayMoviesData();
                break;
            case "SortByFavorites":
                //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.menu_favorites));
                displayMoviesFavorites();
                break;
            default:
                //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.app_name));
                displayMoviesData();
                break;
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STRING_FOR_ON_SAVE_MY_CHOICE, myChoice);
        Parcelable listState = mRecyclerViewMovies.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(STRING_FOR_ON_SAVE_MY_MOVIE_POSITION, listState);

    }

    @Override
    public void onClick(Movie movieClicked) {
        mCallback.onMovieSelected(movieClicked);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.mainfragmentmenu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_sort_popular:
                onClickMenuSortPopular();
                return true;

            case R.id.menu_sort_top_rated:
                onClickMenuSortTopRated();
                return true;

            case R.id.menu_sort_favorites:
                onClickMenuSortFavorites();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void onClickMenuSortPopular() {
        mMoviesAdapter.setmMoviesData(null);
        myChoice = getString(R.string.sort_by_most_popular);
        displayMoviesData();
    }

    public void onClickMenuSortTopRated() {
        mMoviesAdapter.setmMoviesData(null);
        myChoice = getString(R.string.sort_by_top_rated);
        displayMoviesData();
    }

    public void onClickMenuSortFavorites() {
        mMoviesAdapter.setmMoviesData(null);
        myChoice = getString(R.string.sort_by_favorites);
        displayMoviesFavorites();
    }

    private void displayMoviesData() {
        if (isOnline()) {
            hideNoConnection();
            getLoaderManager().restartLoader(DISCOVER_MOVIE_SEARCH_LOADER, null, dataLoaderForMovies).forceLoad();
        } else {
            showNoConnection();
        }
    }

    public void displayMoviesFavorites() {
        if (isOnline()) {
            hideNoConnection();
            getLoaderManager().restartLoader(FAVORITES_MOVIE_SEARCH_LOADER, null, dataLoaderForMoviesFavorites).forceLoad();
        } else {
            showNoConnection();
        }
    }

    private void loadNextDataFromApi(int page) {
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
    Function view with the link on "http://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out"
     */
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public int calculateNoOfColumns() {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.densityDpi;
        int noOfColumns = (int) Math.max(1, dpWidth);
        return noOfColumns;
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

}
