package com.udacity.jeff.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import kaaes.spotify.webapi.android.models.Image;

public class TopTrack implements Parcelable {
    String track;
    String album;
    String imageURL;
    String trackID;

    public TopTrack(String track
            , String album
            , String imageURL
            , String trackID
    ) {
        this.track = track;
        this.album = album;
        this.imageURL = imageURL;
        this.trackID = trackID;
    }

    private TopTrack(Parcel in) {
        this.track = in.readString();
        this.album = in.readString();
        this.imageURL = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(track);
        dest.writeString(album);
        dest.writeString(imageURL);
    }

    public static final Parcelable.Creator<TopTrack> CREATOR = new Parcelable.Creator<TopTrack>() {

        @Override
        public TopTrack createFromParcel(Parcel source) {
            return new TopTrack(source);
        }

        @Override
        public TopTrack[] newArray(int size) {
            return new TopTrack[size];
        }
    };
}

