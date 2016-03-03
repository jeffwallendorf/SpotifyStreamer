package com.udacity.jeff.spotifystreamer;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by jeffw_000 on 01.07.2015.
 */
public class ArtistListAdapter extends ArrayAdapter<ArtistInList> {

    private Context context;

    public ArtistListAdapter(Context context, ArrayList<ArtistInList> artistsInList) {
        super(context, 0, artistsInList);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get Artist from ArrayAdapter
        ArtistInList artistInList = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_artist, parent, false);
        }
        ImageView icon = (ImageView) convertView.findViewById(R.id.imageView_list_artist);
        TextView artistName = (TextView) convertView.findViewById(R.id.textView_list_artist);


        String imageURL = artistInList.imageURL;
        if (imageURL != null) {
            Picasso.with(context).load(imageURL).into(icon);
        } else {
            icon.setBackgroundColor(Color.parseColor("#bbba94"));
        }


        artistName.setText(artistInList.artistName);


        return convertView;
    }
}





