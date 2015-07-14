package com.udacity.jeff.spotifystreamer;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class TopTracksActivityFragment extends Fragment {

    private ArrayList<TopTrack> resultList;
    private FindTopTracks findTopTracks;

    public TopTracksActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_top_tracks, container, false);


        findTopTracks = new FindTopTracks(getActivity(), rootView);

        if (savedInstanceState == null || !savedInstanceState.containsKey("TTParcelable")) {

            String artistID = getActivity().getIntent().getStringExtra("artistID");
            String artistName = getActivity().getIntent().getStringExtra("artistName");

            findTopTracks.setContext(rootView.getContext());
            findTopTracks.execute(artistID, artistName);
        } else {


            resultList = savedInstanceState.getParcelableArrayList("TTParcelable");
            findTopTracks.onPostExecute(resultList);
        }

        return rootView;
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        resultList = findTopTracks.TTList();

        savedInstanceState.putParcelableArrayList("TTParcelable", resultList);
        super.onSaveInstanceState(savedInstanceState);

    }
}
