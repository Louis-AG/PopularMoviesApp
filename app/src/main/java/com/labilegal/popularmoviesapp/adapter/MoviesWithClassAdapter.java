package com.labilegal.popularmoviesapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.labilegal.popularmoviesapp.R;
import com.labilegal.popularmoviesapp.data.Movie;
import com.labilegal.popularmoviesapp.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

/**
 * Created by louisag on 02/02/2017.
 */

public class MoviesWithClassAdapter extends RecyclerView.Adapter<MoviesWithClassAdapter.MoviesWithClassViewHolder> {

    private static final String TAG = MoviesWithClassAdapter.class.getSimpleName();

    private Movie[] mMoviesData;

    private final MoviesWithClassAdapterOnclickHandler mClickHandler;

    public MoviesWithClassAdapter(MoviesWithClassAdapterOnclickHandler clickHandler) {
        this.mClickHandler = clickHandler;
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
        Movie movie = mMoviesData[position];
        URL url = NetworkUtils.buildUrlGetImageW500(movie.getPosterPath());
        Context context = holder.mImageViewMovieItem.getContext();
        Picasso.with(context)
                .load(url.toString())
                .placeholder(R.drawable.user_placeholder)
                .error(R.drawable.user_placeholder_error)
                .into(holder.mImageViewMovieItem);
    }

    @Override
    public int getItemCount() {
        if(mMoviesData == null){
            return 0;
        }else {
            return mMoviesData.length;
        }
    }

    public void setmMoviesData(Movie[] mMoviesData) {
        this.mMoviesData = mMoviesData;
        notifyDataSetChanged();
    }

    public class MoviesWithClassViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView mImageViewMovieItem;

        public MoviesWithClassViewHolder(View itemView) {
            super(itemView);
            this.mImageViewMovieItem = (ImageView) itemView.findViewById(R.id.iv_movie_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie movie = mMoviesData[adapterPosition];
            mClickHandler.onClick(movie);
        }
    }
}
