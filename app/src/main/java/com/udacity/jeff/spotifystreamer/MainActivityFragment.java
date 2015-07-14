package com.udacity.jeff.spotifystreamer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private ArrayList<ArtistInList> resultList;
    private FindArtist findArtist;


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        findArtist = new FindArtist(getActivity(), rootView);

        final EditText editText = (EditText) rootView.findViewById(R.id.editTextArtistSearch);

        if (savedInstanceState == null || !savedInstanceState.containsKey("ArtistParcelable")) {

        }
            else {
            resultList = savedInstanceState.getParcelableArrayList("ArtistParcelable");
            System.out.println(resultList);
            findArtist.onPostExecute(resultList);}



        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String artistSearchQuery = editText.getText().toString();
                    FindArtist findArtist = new FindArtist(getActivity(), rootView);
                    findArtist.setContext(v.getContext());
                    findArtist.execute(artistSearchQuery);
                    resultList = findArtist.ArtistsList();
                }
                return false;
            }
        });

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        System.out.println("LIST: " + resultList);
        savedInstanceState.putParcelableArrayList("ArtistParcelable", resultList);
        super.onSaveInstanceState(savedInstanceState);

    }
}