package com.udacity.jeff.spotifystreamer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
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


public class FindArtist extends AsyncTask<String, Void, ArrayList<ArtistInList>> {

    private Context context;
    private View rootView;
    private List<Artist> artists;
    private String artistName;
    private ArrayList<ArtistInList> resultList;
    private ProgressDialog progressDialog;

    public void setContext(Context context) {
        this.context = context;
    }

    public FindArtist(Context context, View rootView) {
        this.context = context;
        this.rootView = rootView;
        progressDialog=new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        progressDialog.setMessage("Searching...please wait.");
        progressDialog.show();
    }

    // Spotify search request
    @Override
    protected ArrayList<ArtistInList> doInBackground(String... Strings) {
        artistName = Strings[0];
        resultList = new ArrayList<>();

        if (artistName.isEmpty()) {

            artistName = " ";

        }
        try {
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            ArtistsPager results = spotify.searchArtists(artistName);
            artists = results.artists.items;


            for (int i = 0; i < artists.size(); i++) {
                ArtistInList artist;
                if (artists.get(i).images.isEmpty()) {
                    artist = new ArtistInList(artists.get(i).name, null, artists.get(i).id);
                } else {
                    artist = new ArtistInList(artists.get(i).name, artists.get(i).images.get(0).url, artists.get(i).id);
                }
                resultList.add(artist);
            }
        } catch (Exception e) {
            Log.d("SpotifyConnectionError", e.getMessage());
        }


        return resultList;
    }


    protected void onPostExecute(final ArrayList<ArtistInList> results) {
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
                    artistInfo.putExtra("artistID", results.get(position).artistID);
                    artistInfo.putExtra("artistName", results.get(position).artistName);
                    context.startActivity(artistInfo);
                }
            });
        }

        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        resultList = results;
    }

    public ArrayList<ArtistInList> ArtistsList() {
        return resultList;
    }
}





