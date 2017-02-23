package com.labilegal.popularmoviesapp;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.labilegal.popularmoviesapp.adapter.MoviesWithClassAdapter;
import com.labilegal.popularmoviesapp.data.Movie;
import com.labilegal.popularmoviesapp.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.tv_detail_original_title) TextView mTextViewDetailTitle;
    @BindView(R.id.iv_detail_movie_img) ImageView mImageViewDetailMovieImg;
    @BindView(R.id.tv_detail_overview) TextView mTextViewOverview;
    @BindView(R.id.tv_detail_vote_average) TextView mTextViewVoteAverage;
    @BindView(R.id.tv_detail_release_date) TextView mTextViewReleaseDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Movie movie = getIntent().getExtras().getParcelable("movie");

        ButterKnife.bind(this);

        mTextViewDetailTitle.setText(movie.getOriginalTitle());
        Picasso.with(this)
                .load(String.valueOf(NetworkUtils.choseSizeToLoad(movie, this)))
                .into(mImageViewDetailMovieImg);
        mTextViewOverview.append(movie.getOverview());
        mTextViewVoteAverage.append(String.valueOf(movie.getVoteAverage()));
        mTextViewReleaseDate.append(movie.getReleaseDate());
    }
}
