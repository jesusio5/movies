package com.movie.challenge.sample.raw.structures;

public class TV_Movie {
    String mTitle;
    String mPoster;
    String mReleaseDate;
    String mOverview;
    public String id;

    public TV_Movie(String mTitle, String mPoster, String mReleaseDate, String mOverview) {
        this.mTitle = mTitle;
        this.mPoster = mPoster;
        this.mReleaseDate = mReleaseDate;
        this.mOverview = mOverview;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmPoster() {
        return mPoster;
    }

    public void setmPoster(String mPoster) {
        this.mPoster = mPoster;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public String getmOverview() {
        return mOverview;
    }

    public void setmOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    @Override
    public String toString() {
        return "TV_Movie{" +
                "mTitle='" + mTitle + '\'' +
                ", mPoster='" + mPoster + '\'' +
                ", mReleaseDate='" + mReleaseDate + '\'' +
                ", mOverview='" + mOverview + '\'' +
                '}';
    }
}
