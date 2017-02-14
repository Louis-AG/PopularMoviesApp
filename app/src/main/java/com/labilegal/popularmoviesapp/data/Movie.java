package com.labilegal.popularmoviesapp.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by louisag on 01/02/2017.
 */

public class Movie implements Parcelable {

    int mId;
    String mOriginalTitle;
    String mPosterPath;
    String mOverview;
    int mVoteAverage;
    String mReleaseDate;

    /*
    Constructor
     */
    public Movie(int id, String originalTitle, String posterPath, String overview, int voteAverage, String releaseDate) {
        super();
        this.mId = id;
        this.mOriginalTitle = originalTitle;
        this.mPosterPath = posterPath;
        this.mOverview = overview;
        this.mVoteAverage = voteAverage;
        this.mReleaseDate = releaseDate;
    }


    /*
    Getters & Setters
     */
    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.mOriginalTitle = originalTitle;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public void setPosterPath(String posterPath) {
        this.mPosterPath = posterPath;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        this.mOverview = overview;
    }

    public int getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(int voteAverage) {
        this.mVoteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.mReleaseDate = releaseDate;
    }

    /*
    For Parcelable
     */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mOriginalTitle);
        dest.writeString(mPosterPath);
        dest.writeString(mOverview);
        dest.writeInt(mVoteAverage);
        dest.writeString(mReleaseDate);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>()
    {

        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    /*
    Constructor for Parcelable
     */
    public Movie(Parcel source) {
        this.mId = source.readInt();
        this.mOriginalTitle = source.readString();
        this.mPosterPath = source.readString();
        this.mOverview = source.readString();
        this.mVoteAverage = source.readInt();
        this.mReleaseDate = source.readString();
    }
}
