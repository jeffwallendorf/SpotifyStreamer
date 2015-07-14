package com.udacity.jeff.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;

/**
 * Created by jeffw_000 on 14.06.2015.
 */


public class FindArtist extends AsyncTask<String, Void, ArrayList<ArtistInList>> {

    private Context context;
    private View rootView;
    private List<Artist> artists;
    private String artistName;
    private ArrayList<ArtistInList> resultList;
    private ArrayList<ArtistInList> resultList2;


    public void setContext(Context context) {
        this.context = context;
    }

    public FindArtist(Context context, View rootView) {
        this.context = context;
        this.rootView = rootView;
    }

    // Spotify search request
    @Override
    protected ArrayList<ArtistInList> doInBackground(String... Strings) {
        artistName = Strings[0];

        if (artistName.isEmpty()) {

            artistName = " ";

        }

        SpotifyApi api = new SpotifyApi();
        SpotifyService spotify = api.getService();
        ArtistsPager results = spotify.searchArtists(artistName);
        artists = results.artists.items;

        resultList = new ArrayList<>();

        for (int i = 0; i < artists.size(); i++) {
            ArtistInList artist;
            if (artists.get(i).images.isEmpty()) {
                artist = new ArtistInList(artists.get(i).name, null);
            } else {
                artist = new ArtistInList(artists.get(i).name, artists.get(i).images.get(0).url);
            }
            resultList.add(artist);
        }


        return resultList;
    }


    protected void onPostExecute(ArrayList<ArtistInList> results) {
        if (results.isEmpty()) {
            Toast toast = Toast.makeText(context, "Sorry, no artist '" + artistName + "' found. Please refine your search and try again.", Toast.LENGTH_LONG);
            toast.show();
        } else {
            final ListView listView = (ListView) rootView.findViewById(R.id.listview_artist);
            // Populate listView
            ArtistListAdapter ArtistAdapter = new ArtistListAdapter(context, results);
            listView.setAdapter(ArtistAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent artistInfo = new Intent(context, TopTracksActivity.class);
                    artistInfo.putExtra("artistID", artists.get(position).id);
                    artistInfo.putExtra("artistName", artists.get(position).name);
                    context.startActivity(artistInfo);
                }
            });
        }
        System.out.println(results);
      resultList2=results;
    }

    public ArrayList<ArtistInList> ArtistsList() {
        System.out.println("ArtistList called, resultList: "+resultList2);

        return resultList2;
    }
}





