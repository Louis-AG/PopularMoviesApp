package com.labilegal.popularmoviesapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.labilegal.popularmoviesapp.adapter.ReviewsWithClassAdapter;
import com.labilegal.popularmoviesapp.adapter.TrailersWithClassAdapter;
import com.labilegal.popularmoviesapp.data.Movie;
import com.labilegal.popularmoviesapp.data.MovieContract;
import com.labilegal.popularmoviesapp.data.Review;
import com.labilegal.popularmoviesapp.utilities.NetworkUtils;
import com.labilegal.popularmoviesapp.utilities.OpenDataJsonUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/**
 * Created by louisag on 27/02/2017.
 */

public class DetailFragment extends android.support.v4.app.Fragment implements TrailersWithClassAdapter.TrailersWithClassAdapterOnclickHandler {

    private static final int KEEP_TRAILER_SEARCH_LOADER = 23;
    private static final int KEEP_REVIEW_SEARCH_LOADER = 24;
    private static final String STRING_FOR_ON_SAVE_MY_MOVIE = "my_movie";

    private Toast mToast;
    private Context context;

    private Movie mMovie;

    private TrailersWithClassAdapter mTrailersAdapter;
    private ReviewsWithClassAdapter mReviewsAdapter;

    private LoaderManager.LoaderCallbacks<String[]> dataLoaderForTrailers
            = new LoaderManager.LoaderCallbacks<String[]>() {
        @Override
        public Loader<String[]> onCreateLoader(int id, final Bundle args) {

            return new AsyncTaskLoader<String[]>(context) {
                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                    if (args == null) {
                        return;
                    }
                }

                @Override
                public String[] loadInBackground() {

                    URL url = NetworkUtils.buildUrlKeepTrailersInformation(mMovie.getId());

                    String[] tResultsStrings = null;

                    try {
                        String jsonResult = NetworkUtils.getResponseFromHttpUrl(url);

                        tResultsStrings = OpenDataJsonUtils.getSimpleDataStringFromJson(jsonResult);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return tResultsStrings;
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<String[]> loader, String[] data) {
            if (data != null) {
                mTrailersAdapter.setmStringData(data);
                mRecyclerViewTrailers.setAdapter(mTrailersAdapter);
            }
        }

        @Override
        public void onLoaderReset(Loader<String[]> loader) {

        }

    };

    private LoaderManager.LoaderCallbacks<Review[]> dataLoaderForReviews
            = new LoaderManager.LoaderCallbacks<Review[]>() {
        @Override
        public Loader<Review[]> onCreateLoader(int id, final Bundle args) {

            return new AsyncTaskLoader<Review[]>(context) {
                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                    if (args == null) {
                        return;
                    }
                }

                @Override
                public Review[] loadInBackground() {

                    URL url = NetworkUtils.buildUrlKeepReviewsInformation(mMovie.getId());

                    Review[] tResultReviews = null;

                    try {
                        String jsonResult = NetworkUtils.getResponseFromHttpUrl(url);

                        tResultReviews = OpenDataJsonUtils.getSimpleDataReviewsFromJson(jsonResult);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return tResultReviews;
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<Review[]> loader, Review[] data) {
            if (data != null) {
                mReviewsAdapter.setmReviewsData(data);
                mRecyclerViewReviews.setAdapter(mReviewsAdapter);
            }
        }

        @Override
        public void onLoaderReset(Loader<Review[]> loader) {

        }
    };

    TextView mTextViewDetailTitle;
    ImageView mImageViewDetailMovieImg;
    TextView mTextViewOverview;
    TextView mTextViewVoteAverage;

    TextView mTextViewReleaseDate;

    TextView mTextViewDetailTitleTrailers;


    RecyclerView mRecyclerViewTrailers;

    TextView mTextViewDetailTitleReviews;

    RecyclerView mRecyclerViewReviews;

    private Menu mMenu;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        mTextViewDetailTitle = (TextView) view.findViewById(R.id.tv_detail_original_title);
        mImageViewDetailMovieImg = (ImageView) view.findViewById(R.id.iv_detail_movie_img);
        mTextViewOverview = (TextView) view.findViewById(R.id.tv_detail_overview);
        mTextViewVoteAverage = (TextView) view.findViewById(R.id.tv_detail_vote_average);
        mTextViewReleaseDate = (TextView) view.findViewById(R.id.tv_detail_release_date);

        mTextViewDetailTitleTrailers = (TextView) view.findViewById(R.id.tv_detail_title_trailers);
        mRecyclerViewTrailers = (RecyclerView) view.findViewById(R.id.rv_trailers);

        mTextViewDetailTitleReviews = (TextView) view.findViewById(R.id.tv_detail_title_reviews);
        mRecyclerViewReviews = (RecyclerView) view.findViewById(R.id.rv_reviews);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            if (savedInstanceState.getParcelable(STRING_FOR_ON_SAVE_MY_MOVIE) != null) {
                mMovie = savedInstanceState.getParcelable(STRING_FOR_ON_SAVE_MY_MOVIE);
            }
        }
        context = getActivity();
        if (getArguments().containsKey("movie")) {
            mMovie = getArguments().getParcelable("movie");
        }
        updateContent(mMovie);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STRING_FOR_ON_SAVE_MY_MOVIE, mMovie);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detailfragmentmenu, menu);
        this.mMenu = menu;
        super.onCreateOptionsMenu(menu, inflater);
        forChooseFavoriteIcon();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_favorite:
                onClickFavorite();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(String stringClicked) {
        intentForDetail(stringClicked);
    }

    public void forChooseFavoriteIcon(){
        Cursor cursor = context.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI
                , null
                , "movie_id = " + mMovie.getId()
                , null
                , null);

        if (cursor != null) {
            if (cursor.getCount() == 0) {
                mMenu.getItem(0).setIcon(R.drawable.ic_favorite_border_black_48dp);
            } else {
                mMenu.getItem(0).setIcon(R.drawable.ic_favorite_black_48dp);
            }
        }
        cursor.close();
    }

    public void onClickFavorite() {

        Cursor cursor = context.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI
                , null
                , "movie_id = " + mMovie.getId()
                , null
                , null);

        if (cursor != null) {
            if (cursor.getCount() == 0) {
                onFavoriteInsert();
                mMenu.getItem(0).setIcon(R.drawable.ic_favorite_black_48dp);
            } else {
                onFavoriteDelete();
                mMenu.getItem(0).setIcon(R.drawable.ic_favorite_border_black_48dp);
            }
        }
        cursor.close();
    }

    public void onFavoriteInsert() {

        String inputTitle = mMovie.getOriginalTitle();

        if (inputTitle.length() == 0) {
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry.COLUNM_MOVIE_TITLE, inputTitle);
        contentValues.put(MovieContract.MovieEntry.COLUNM_MOVIE_ID, mMovie.getId());
        contentValues.put(MovieContract.MovieEntry.COLUNM_MOVIE_OVERVIEW, mMovie.getOverview());
        contentValues.put(MovieContract.MovieEntry.COLUNM_MOVIE_POSTER_PATH, mMovie.getPosterPath());
        contentValues.put(MovieContract.MovieEntry.COLUNM_MOVIE_RELEASE_DATE, mMovie.getReleaseDate());
        contentValues.put(MovieContract.MovieEntry.COLUNM_MOVIE_VOTE_AVERAGE, mMovie.getVoteAverage());

        Uri uri = context.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(context, "Added to Favorites", Toast.LENGTH_SHORT);
        mToast.show();
    }

    public void onFavoriteDelete() {

        context.getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, "movie_id = " + mMovie.getId(), null);

        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(context, "Deleted to Favorites", Toast.LENGTH_SHORT);
        mToast.show();
    }

    public void updateContent(Movie movie) {

        mTextViewDetailTitle.setText(movie.getOriginalTitle());
        Picasso.with(context)
                .load(String.valueOf(NetworkUtils.choseSizeToLoad(movie, context)))
                .placeholder(R.drawable.user_placeholder)
                .error(R.drawable.user_placeholder_error)
                .into(mImageViewDetailMovieImg);
        mTextViewOverview.setText(movie.getOverview());
        mTextViewVoteAverage.append(String.valueOf(movie.getVoteAverage()));
        mTextViewReleaseDate.setText(movie.getReleaseDate());

        // regarder si l'id du film exist en bdd
        // et definir le bouton comme mis en favoris



        LinearLayoutManager layoutManagerTrailers = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManagerReviews = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        mRecyclerViewTrailers.setLayoutManager(layoutManagerTrailers);
        mRecyclerViewReviews.setLayoutManager(layoutManagerReviews);

        mRecyclerViewTrailers.setHasFixedSize(true);
        mRecyclerViewReviews.setHasFixedSize(false);

        mTrailersAdapter = new TrailersWithClassAdapter(this, context);
        mReviewsAdapter = new ReviewsWithClassAdapter(context);

        mRecyclerViewTrailers.setAdapter(mTrailersAdapter);
        mRecyclerViewReviews.setAdapter(mReviewsAdapter);


        getLoaderManager().restartLoader(KEEP_TRAILER_SEARCH_LOADER, null, dataLoaderForTrailers).forceLoad();
        getLoaderManager().restartLoader(KEEP_REVIEW_SEARCH_LOADER, null, dataLoaderForReviews).forceLoad();
    }

    public void intentForDetail(String key) {
        String video_path = "http://www.youtube.com/watch?v=" + key;
        Uri uri = Uri.parse(video_path);

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

}
