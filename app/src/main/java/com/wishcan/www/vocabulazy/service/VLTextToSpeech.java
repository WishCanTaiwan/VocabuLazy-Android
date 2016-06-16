package com.wishcan.www.vocabulazy.service;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;

import com.wishcan.www.vocabulazy.log.Logger;

import java.util.HashMap;
import java.util.Locale;

public class VLTextToSpeech extends VLTextToSpeechListener {

    public static final String TAG = VLTextToSpeech.class.getSimpleName();

    private static final String UTTERANCE_SILENCE = "-silence";

    private Context mContext;
    private TextToSpeech mTextToSpeech;
    private OnUtteranceFinishListener mOnUtteranceFinishListener;

    private boolean isEngineInit = false;
    private String currentUtterance;

    public VLTextToSpeech (Context context) {
        mContext = context;
    }

    @Override
    protected void onEngineInit(int status) {
        switch (status) {
            case TextToSpeech.SUCCESS:
                isEngineInit = true ;
                break;
            default:
                break;
        }
    }

    @Override
    protected void onUtteranceFinished(String utteranceId) {
        String utteranceAfterSilence = currentUtterance + UTTERANCE_SILENCE;
        if (!utteranceId.equals(utteranceAfterSilence))
            return;
        mOnUtteranceFinishListener.onUtteranceFinished(currentUtterance);
    }

    public void initTTS() {
        if (mTextToSpeech != null)
            return;
        mTextToSpeech = new TextToSpeech(mContext, this, "com.google.android.tts");
    }

    public void setUpListener(OnUtteranceFinishListener listener) {
        mOnUtteranceFinishListener = listener;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTextToSpeech.setOnUtteranceProgressListener(this);
        } else {
            mTextToSpeech.setOnUtteranceCompletedListener(this);
        }
    }

    public void speak(String utterance, int rate) {
        currentUtterance = utterance;

        if (!isEngineInit) {
            currentUtterance = utterance;
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTextToSpeech.setSpeechRate(0.7f * rate);
            mTextToSpeech.speak(utterance, TextToSpeech.QUEUE_ADD, null, utterance);
        } else {
            mTextToSpeech.setSpeechRate(0.7f * rate);
            HashMap<String, String> params = new HashMap<>();
            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utterance);
            mTextToSpeech.speak(utterance, TextToSpeech.QUEUE_ADD, params);
        }
    }

    public void speakSilence(long time) {
        if (!isEngineInit) {
            return;
        }

        String silenceUtterance = currentUtterance + UTTERANCE_SILENCE;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTextToSpeech.playSilentUtterance(time, TextToSpeech.QUEUE_ADD, silenceUtterance);
        } else {
            HashMap<String, String> params = new HashMap<>();
            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, silenceUtterance);
            mTextToSpeech.playSilence(time, TextToSpeech.QUEUE_ADD, params);
        }
    }

    public void pause() {
        stop();
    }

    public void stop() {
        if (mTextToSpeech == null)
            return;

        mTextToSpeech.stop();
    }

    public void release() { // releasing TTS resources
        if (mTextToSpeech == null)
            return;

        if (mTextToSpeech.isSpeaking())
            return;

        mTextToSpeech.shutdown();
    }

    public void setLanguage(Locale locale) {
        if (!isEngineInit)
            return;

        mTextToSpeech.setLanguage(locale);
    }

    public interface OnUtteranceFinishListener {
        void onUtteranceFinished(String utterance);
    }
}
