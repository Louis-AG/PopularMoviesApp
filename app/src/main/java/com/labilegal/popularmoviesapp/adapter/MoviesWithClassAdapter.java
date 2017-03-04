package com.labilegal.popularmoviesapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.labilegal.popularmoviesapp.MainActivity;
import com.labilegal.popularmoviesapp.R;
import com.labilegal.popularmoviesapp.data.Movie;
import com.labilegal.popularmoviesapp.data.MovieList;
import com.labilegal.popularmoviesapp.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

/**
 * Created by louisag on 02/02/2017.
 */

public class MoviesWithClassAdapter extends RecyclerView.Adapter<MoviesWithClassAdapter.MoviesWithClassViewHolder> {

    private static final String TAG = MoviesWithClassAdapter.class.getSimpleName();

    private MovieList mMoviesData;

    private final MoviesWithClassAdapterOnclickHandler mClickHandler;


    private Context mContext;

    public MoviesWithClassAdapter(MoviesWithClassAdapterOnclickHandler clickHandler, Context context) {
        this.mClickHandler = clickHandler;
        this.mContext = context;
    }

    /*
    Interface for ClickHandler
     */
    public interface MoviesWithClassAdapterOnclickHandler {
        void onClick(Movie movieClicked);
    }

    @Override
    public MoviesWithClassViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movies_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new MoviesWithClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesWithClassViewHolder holder, int position) {
        Movie movie = mMoviesData.get(position);
        URL url = NetworkUtils.choseSizeToLoad(movie, mContext);
        Context context = holder.mImageViewMovieItem.getContext();
        Picasso.with(context)
                .load(url.toString())
                .placeholder(R.drawable.user_placeholder)
                .error(R.drawable.user_placeholder_error)
                .into(holder.mImageViewMovieItem);
    }

    @Override
    public int getItemCount() {
        if (mMoviesData == null) {
            return 0;
        } else {
            return mMoviesData.size();
        }
    }

    public MovieList getMovies(){
        return mMoviesData;
    }

    public void setmMoviesData(MovieList moviesData) {
        this.mMoviesData = moviesData;
        notifyDataSetChanged();
    }

    public class MoviesWithClassViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mImageViewMovieItem;

        public MoviesWithClassViewHolder(View itemView) {
            super(itemView);
            this.mImageViewMovieItem = (ImageView) itemView.findViewById(R.id.iv_movie_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie movie = mMoviesData.get(adapterPosition);
            mClickHandler.onClick(movie);
        }
    }
}
