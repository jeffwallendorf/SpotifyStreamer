package com.udacity.jeff.spotifystreamer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

/**
 * Created by jeffw_000 on 14.06.2015.
 */


public class FindTopTracks extends AsyncTask<String, Void, ArrayList<TopTrack>> {

    private Context context;
    private View rootView;
    private ArrayList<TopTrack> resultList;
    private ProgressDialog progressDialog;
    private String artistName;

    public void setContext(Context context) {
        this.context = context;
    }

    public FindTopTracks(Context context, View rootView) {
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
    protected ArrayList<TopTrack> doInBackground(String... Strings) {
        String artistID = Strings[0];
        artistName=Strings[1];
        resultList = new ArrayList<>();

        try {
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            Map<String, Object> options = new HashMap<>();
            options.put(SpotifyService.COUNTRY, Locale.getDefault().getCountry());

            Tracks topTracks = spotify.getArtistTopTrack(artistID, options);


            for (int i = 0; i < topTracks.tracks.size() && i < 10; i++) {
                TopTrack topTrack;
                if (topTracks.tracks.get(i).album.images.isEmpty()) {
                    topTrack = new TopTrack(topTracks.tracks.get(i).name,
                                            topTracks.tracks.get(i).album.name,
                                            null,
                                            topTracks.tracks.get(i).id
                                            );
                } else {
                    topTrack = new TopTrack(topTracks.tracks.get(i).name,
                                            topTracks.tracks.get(i).album.name,
                                            topTracks.tracks.get(i).album.images.get(0).url,
                                            topTracks.tracks.get(i).id
                                            );
                }
                resultList.add(topTrack);
            }
        } catch (Exception e) {
            Log.d("SpotifyConnectionError", e.getMessage());
        }
        return resultList;

    }


    protected void onPostExecute(final ArrayList<TopTrack> results) {

        if (results.isEmpty()) {

            Toast toast = Toast.makeText(context, "Sorry, no Top Tracks were found for this artist.", Toast.LENGTH_LONG);
            toast.show();


        } else {
            final ListView listView = (ListView) rootView.findViewById(R.id.listview_top_tracks);
            // Populate listView
            TopTracksListAdapter TopTracksAdapter = new TopTracksListAdapter(context, results);
            listView.setAdapter(TopTracksAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    // Create Bundle to pass StringArrays with results-informations to next activity
                    Bundle topTracks = new Bundle();
                    String[] trackIDs=new String[results.size()];
                    String[] trackNames=new String[results.size()];
                    String[] albumNames=new String[results.size()];
                    String[] coverURLs=new String[results.size()];

                    for (int i=0;i<results.size();i++){
                        trackIDs[i]=results.get(i).trackID;
                        trackNames[i]=results.get(i).track;
                        albumNames[i]=results.get(i).album;
                        coverURLs[i]=results.get(i).imageURL;
                    }

                    topTracks.putStringArray("trackIDs",trackIDs);
                    topTracks.putStringArray("trackNames",trackNames);
                    topTracks.putStringArray("albumNames", albumNames);
                    topTracks.putStringArray("coverURLs", coverURLs);

                    Intent trackInfo = new Intent(context, MediaPlayerActivity.class);
                    trackInfo.putExtras(topTracks);
                    trackInfo.putExtra("artistName", artistName);
                    trackInfo.putExtra("position", position);
   //                 trackInfo.putExtra("duration",trackDurations[position]);
                    context.startActivity(trackInfo);
                }
            });

        }
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        resultList = results;
    }

    public ArrayList<TopTrack> TTList() {
        return resultList;
    }


}











