package com.wishcan.www.vocabulazy.service;

import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

public abstract class VLTextToSpeechListener extends UtteranceProgressListener implements TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener {

    abstract void onEngineInit(int status);
    abstract void onUtteranceFinished(String utteranceId);

    @Override
    public void onInit(int status) {
        onEngineInit(status);
    }

    @Override
    public void onUtteranceCompleted(String utteranceId) {
        onUtteranceFinished(utteranceId);
    }

    @Override
    public void onStart(String utteranceId) {

    }

    @Override
    public void onDone(String utteranceId) {
        onUtteranceFinished(utteranceId);
    }

    @Override
    public void onError(String utteranceId) {

    }
}