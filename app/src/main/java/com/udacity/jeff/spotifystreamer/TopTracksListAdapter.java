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


public class TopTracksListAdapter extends ArrayAdapter<TopTrack> {

    private Context context;

    public TopTracksListAdapter(Context context, ArrayList<TopTrack> topTracksList) {
        super(context, 0, topTracksList);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get Artist from ArrayAdapter
        TopTrack topTrack = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_toptrack, parent, false);
        }
        ImageView cover = (ImageView) convertView.findViewById(R.id.imageView_list_toptrack);
        TextView track = (TextView) convertView.findViewById(R.id.textView_list__toptrack_track);
        TextView album = (TextView) convertView.findViewById(R.id.textView_list__toptrack_album);


        String imageURL = topTrack.imageURL;
        if (imageURL != null) {
            Picasso.with(context).load(imageURL).into(cover);

        } else {
            cover.setBackgroundColor(Color.parseColor("#bbba94"));
        }


        track.setText(topTrack.track);
        album.setText(topTrack.album);


        return convertView;
    }
}





