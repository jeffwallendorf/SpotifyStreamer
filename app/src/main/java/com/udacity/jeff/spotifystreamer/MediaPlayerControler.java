package com.udacity.jeff.spotifystreamer;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.util.Objects;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;

/**
 * Created by jeffw_000 on 27.07.2015.
 */
public class MediaPlayerControler extends Service implements MediaPlayer.OnPreparedListener {

    MediaPlayer mediaPlayer = new MediaPlayer();
    Boolean isPaused = false;
    int progressOnLaunch;

    private final IBinder MPbinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return MPbinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }

    public class LocalBinder extends Binder {
        public MediaPlayerControler getServerInstance() {
            return MediaPlayerControler.this;
        }
    }

    public void playSong(String trackID, int startFrom, boolean pause) {
        progressOnLaunch = startFrom * 100;
        mediaPlayer.reset();
        try {
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();

            String previewURL = spotify.getTrack(trackID).preview_url;

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    onSongFinished();
                }
            });

            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(previewURL);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.prepareAsync();
            isPaused = pause;

        } catch (Exception e) {
            Log.d("SpotifyConnectionError", e.getMessage());
        }

    }

    public void pauseSong() {
        mediaPlayer.pause();
        isPaused = true;
    }

    public void resumeSong() {
        mediaPlayer.start();
        isPaused = false;
    }

    public void jumpTo(int sec) {
        mediaPlayer.seekTo(sec * 1000);
    }

    public void onSongFinished() {
        Intent intent = new Intent("songFinished");
        intent.putExtra("MusicPlaying", false);
        mediaPlayer.seekTo(0);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public int getProgress() {
        return mediaPlayer.getCurrentPosition() / 100;
    }

    @Override
    public void onDestroy() {
//        mediaPlayer.stop();
    }

    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.seekTo(progressOnLaunch);
        System.out.println("progress on launch: " + progressOnLaunch + " " + getProgress());
        if (!isPaused) {
            mp.start();
        }
    }

}
