package com.udacity.jeff.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

public class ArtistInList implements Parcelable {
    String artistName;
    String imageURL;
    String artistID;

    public ArtistInList(String artistName
            , String imageURL, String artistID
    ) {
        this.artistName = artistName;
        this.imageURL = imageURL;
        this.artistID = artistID;
    }

    private ArtistInList(Parcel in) {
        this.artistName = in.readString();
        this.imageURL = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(artistName);
        dest.writeString(imageURL);
    }

    public static final Parcelable.Creator<ArtistInList> CREATOR = new Parcelable.Creator<ArtistInList>() {

        @Override
        public ArtistInList createFromParcel(Parcel source) {
            return new ArtistInList(source);
        }

        @Override
        public ArtistInList[] newArray(int size) {
            return new ArtistInList[size];
        }
    };

}

