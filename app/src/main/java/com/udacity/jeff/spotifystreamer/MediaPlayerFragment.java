package com.udacity.jeff.spotifystreamer;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.IBinder;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import android.os.Handler;


public class MediaPlayerFragment extends Fragment implements View.OnClickListener {

    private String[] trackIDs;
    private String[] trackNames;
    private String[] albumNames;
    private String[] coverURLs;
    String trackID;
    Boolean musicPlaying=true;
    private String artistName;
    private View rootView;
    private Context context;
    private ImageButton PlayPauseButton, NextButton, PreviousButton;
    private TextView DurationMax, Time;
    private SeekBar seekBar;
    private int position;
    private Intent serviceIntent;
    private Boolean musicBound=false;
    private MediaPlayerControler MPControler;
    private Handler handler=new Handler();
    private int startFrom;
    private int positionInList;
    private boolean isPaused=false;

    public MediaPlayerFragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        startFrom =0;
        positionInList=getActivity().getIntent().getIntExtra("position", 0);
        if(savedInstanceState!=null){
           startFrom =savedInstanceState.getInt("position");
            positionInList=savedInstanceState.getInt("positionInList");
            isPaused=savedInstanceState.getBoolean("pause");

            System.out.println("savedInstance, progress "+startFrom+"; position: "+positionInList);
        }

        rootView=inflater.inflate(R.layout.fragment_media_player, container, false);
        context=getActivity().getApplicationContext();

        // Get track information from intent
        artistName = getActivity().getIntent().getStringExtra("artistName");

        Bundle topTracksBundle=getActivity().getIntent().getExtras();
        trackIDs = topTracksBundle.getStringArray("trackIDs");
        trackNames=topTracksBundle.getStringArray("trackNames");
        albumNames=topTracksBundle.getStringArray("albumNames");
        coverURLs=topTracksBundle.getStringArray("coverURLs");

        PlayPauseButton=(ImageButton) rootView.findViewById(R.id.Player_Button_PlayPause);
        NextButton=(ImageButton) rootView.findViewById(R.id.Player_Button_Next);
        PreviousButton=(ImageButton) rootView.findViewById(R.id.Player_Button_Previous);
        PlayPauseButton.setOnClickListener(this);
        NextButton.setOnClickListener(this);
        PreviousButton.setOnClickListener(this);
        seekBar=(SeekBar) rootView.findViewById(R.id.Player_ScrubBar);

        DurationMax=(TextView) rootView.findViewById(R.id.textView_Player_Max_Duration);
        Time=(TextView) rootView.findViewById(R.id.textView_Player_Time);

    //    serviceIntent=new Intent(getActivity(),MediaPlayerControler.class);
     //   getActivity().stopService(serviceIntent);
        position=positionInList;
        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();
        if (serviceIntent==null){
            serviceIntent=new Intent(context, MediaPlayerControler.class);
            getActivity().startService(serviceIntent);
            getActivity().bindService(serviceIntent, musicConnection, Context.BIND_AUTO_CREATE);
        }
    }

    public ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaPlayerControler.LocalBinder binder = (MediaPlayerControler.LocalBinder)service;
            MPControler=binder.getServerInstance();
            populateInfoViews(position);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    // Fill in Information about Track and Artist
    public void populateInfoViews(int position){

        trackID = trackIDs[position];
        System.out.println("TrackID: " + trackID);
        MPControler.playSong(trackID, startFrom, isPaused);
        musicPlaying=true;

        String trackName=trackNames[position];
        String albumName=albumNames[position];
        String coverURL=coverURLs[position];


        TextView TVArtistName=(TextView) rootView.findViewById(R.id.textView_Player_ArtistName);
        TextView TVTrackName=(TextView) rootView.findViewById(R.id.textView_Player_TrackName);
        TextView TVAlbumName=(TextView) rootView.findViewById(R.id.textView_Player_AlbumName);
        ImageView IVCoverArtwork=(ImageView) rootView.findViewById(R.id.imageView_Player_Cover);

        TVArtistName.setText(artistName);
        TVTrackName.setText(trackName);
        TVAlbumName.setText(albumName);
        seekBar.setMax(300);
        DurationMax.setText("0:30");
        Time.setText("0:00");

        if(coverURL !=null){
            Picasso.with(context).load(coverURL).into(IVCoverArtwork);
        }
        else {
            IVCoverArtwork.setBackgroundColor(Color.parseColor("#bbba94"));
            IVCoverArtwork.setMinimumHeight(IVCoverArtwork.getWidth());
        }
        System.out.println("Position vum gespillten Lidd: " + position);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!MPControler.isPaused){
                    int progress=MPControler.getProgress();
                    seekBar.setProgress(progress);
                    String timeString;
                    if(progress<100){timeString="0:0";}
                    else{timeString="0:";}
                    Time.setText(timeString + (progress / 10));
                }
                handler.postDelayed(this,100);
            }
        });


seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(fromUser){MPControler.jumpTo(progress/10);}
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

});
        PlayPauseButton.setImageResource(android.R.drawable.ic_media_pause);

    }


    @Override
    public void onClick(View v) {

        if(v==PlayPauseButton){
            if(!musicPlaying){MPControler.playSong(trackID,0,false);
                PlayPauseButton.setImageResource(android.R.drawable.ic_media_pause);
                musicPlaying=true;
            }
            else{
                if(MPControler.isPaused){
                    PlayPauseButton.setImageResource(android.R.drawable.ic_media_pause);
                    MPControler.resumeSong();
                }
                        else{
                    PlayPauseButton.setImageResource(android.R.drawable.ic_media_play);
                    MPControler.pauseSong();
                    isPaused=true;
                }
        }}

        else{

            if (v == NextButton) {
                if (position == trackIDs.length - 1) {
                    position = 0;
                } else {
                    position++;
                }
                startFrom=0;
                isPaused=false;
                populateInfoViews(position);
                System.out.println("Position: " + position);
            } else if (v == PreviousButton) {
                if (position == 0) {
                    position = trackIDs.length - 1;
                } else {
                    position = position - 1;
                }
                startFrom=0;
                isPaused=false;
                populateInfoViews(position);
                System.out.println("Position: " + position);
            }
        }
        }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            musicPlaying = intent.getBooleanExtra("MusicPlaying",true);
            if (!musicPlaying){
                PlayPauseButton.setImageResource(android.R.drawable.ic_media_play);
            }
        }
    };

    @Override
    public void onResume(){
        super.onResume();
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, new IntentFilter("songFinished"));
    }

    @Override
    public void onStop(){
        super.onStop();
        getActivity().unbindService(musicConnection);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("position",seekBar.getProgress());
        savedInstanceState.putInt("positionInList",position);
        savedInstanceState.putBoolean("pause",isPaused);

        super.onSaveInstanceState(savedInstanceState);

    }

    }

