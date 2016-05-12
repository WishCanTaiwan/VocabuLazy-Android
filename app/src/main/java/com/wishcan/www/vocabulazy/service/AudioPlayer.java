package com.wishcan.www.vocabulazy.service;

import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.util.Log;

/**
 * Created by allencheng07 on 2016/1/26.
 */
public class AudioPlayer implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    /**
     * tag for debugging
     */
    public static final String TAG = AudioPlayer.class.getSimpleName();

    private MediaPlayer wMediaPlayer;
    private OnEventListener wEventListener;

    public AudioPlayer(OnEventListener eventListener) {
        initPlayer();
        wEventListener = eventListener;
    }

    public void initPlayer() {
        wMediaPlayer = new MediaPlayer();
        wMediaPlayer.setOnCompletionListener(this);
        wMediaPlayer.setOnPreparedListener(this);
        wMediaPlayer.reset();
    }

    public void releasePlayer() {
        if (wMediaPlayer != null) {
            wMediaPlayer.release();
        }
    }

    public void start(String string) {
        Log.d(TAG, "playing " + string);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        wEventListener.onCompletion(mp);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    protected interface OnEventListener {
        void onCompletion(MediaPlayer mp);
    }
}
