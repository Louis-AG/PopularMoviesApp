package com.labilegal.popularmoviesapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by louisag on 04/03/2017.
 */

public class MovieList extends ArrayList<Movie> implements Parcelable
{
    public MovieList()
    {

    }

    public MovieList(Parcel source)
    {
        this.getFromParcel(source);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
    {
        public MovieList createFromParcel(Parcel source)
        {
            return new MovieList(source);
        }

        @Override
        public Object[] newArray(int size) {
            return null;
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        int size = this.size();
        dest.writeInt(size);
        for(int i=0; i < size; i++)
        {
            Movie movie = this.get(i);
            dest.writeInt(movie.getId());
            dest.writeString(movie.mOriginalTitle);
            dest.writeString(movie.mPosterPath);
            dest.writeString(movie.mOverview);
            dest.writeInt(movie.mVoteAverage);
            dest.writeString(movie.mReleaseDate);
        }
    }

    public void getFromParcel(Parcel source)
    {
        this.clear();

        int size = source.readInt();

        for(int i = 0; i < size; i++)
        {
            Movie movie = new Movie();
            movie.setId(source.readInt());
            movie.setOriginalTitle(source.readString());
            movie.setPosterPath(source.readString());
            movie.setOverview(source.readString());
            movie.setVoteAverage(source.readInt());
            movie.setReleaseDate(source.readString());
            this.add(movie);
        }

    }
}