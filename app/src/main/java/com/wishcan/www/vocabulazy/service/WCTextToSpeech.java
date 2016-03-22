package com.wishcan.www.vocabulazy.service;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.speech.tts.Voice;
import android.util.Log;
import android.widget.Toast;

import com.wishcan.www.vocabulazy.storage.Vocabulary;

import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

/**
 * Created by allencheng07 on 2016/1/27.
 */
public class WCTextToSpeech extends UtteranceProgressListener
        implements TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener {

    public static final String TAG = WCTextToSpeech.class.getSimpleName();

    public static final String UTTERANCE_ID = "utterance-id";
    public static final String UTTERANCE_SLIENCE = "slience";

    private Context mContext;
    private TextToSpeech wTextToSpeech;
    private OnUtteranceStatusListener wOnUtteranceStatusListener;

    private boolean ttsEngineInit;
    private String currentUtterance;
    private HashMap<String, String> utteranceParams;

    private String buffUtterence;
    private int wStopPeriod;
    private int wSpeed;

    public WCTextToSpeech (Context context, OnUtteranceStatusListener listener) {
        mContext = context;
        initializeTTSEngine(context);
        wOnUtteranceStatusListener = listener;
        ttsEngineInit = false;
        currentUtterance = "default";
    }

    public void speak(String text) {
        if (!ttsEngineInit) {
            currentUtterance = text;
            return;
        }
        currentUtterance = text;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wTextToSpeech.setSpeechRate(0.7f * wSpeed);
            wTextToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null, text);
            wTextToSpeech.playSilentUtterance(wStopPeriod, TextToSpeech.QUEUE_ADD, UTTERANCE_SLIENCE);
        } else {
//            setTTSIDParams(text);
            wTextToSpeech.setSpeechRate(0.7f * wSpeed);
            wTextToSpeech.speak(text, TextToSpeech.QUEUE_ADD, createParams(text));
            wTextToSpeech.playSilence(wStopPeriod, TextToSpeech.QUEUE_ADD, createParams(UTTERANCE_SLIENCE));
        }
    }

    public void initializeTTSEngine(Context context) {
        if (wTextToSpeech == null) {
            wTextToSpeech = new TextToSpeech(context, this);
        }
        setTTSListener();
    }

    public void pause() {
        stop();
    }

    public void stop() {
        wTextToSpeech.stop();
    }

    public void shutdown() {
        if (wTextToSpeech != null && !wTextToSpeech.isSpeaking())
            wTextToSpeech.shutdown();
    }

    public void setLanguage(Locale locale) {
        if (ttsEngineInit)
            wTextToSpeech.setLanguage(locale);
    }

    public void setStopPeriod(int stopPeriod) {
        wStopPeriod = stopPeriod * 1000;
    }

    public void setSpeed(int speed) {
        wSpeed = speed;
    }

    private void setTTSListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wTextToSpeech.setOnUtteranceProgressListener(this);
        } else {
            wTextToSpeech.setOnUtteranceCompletedListener(this);
        }
    }

    private HashMap<String, String> createParams(String utteranceid) {
        HashMap<String, String> params = new HashMap<>();
        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceid);
        return params;
    }

    @Override
    public void onInit(int status) {
        Log.d(TAG, "onInit");
        if(status == TextToSpeech.SUCCESS) {
            ttsEngineInit = true;
//            speak(currentUtterance);
//            Log.d(TAG, "engine initialized");
//            Toast.makeText(mContext, "engine initialized", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart(String utteranceId) {
//        Log.d(TAG, "onStart: " + utteranceId);
    }

    @Override
    public void onDone(String utteranceId) {
        Log.d(TAG, "onDone: " + utteranceId);

        if (!utteranceId.equals(UTTERANCE_SLIENCE)) {
            buffUtterence = utteranceId;
            return;
        }

        if (buffUtterence.equals(currentUtterance)) {
            Log.d(TAG, "utterances match");
            currentUtterance = "";
            wOnUtteranceStatusListener.onUtteranceCompleted();
        } else {
            Log.d(TAG, "error: utterance dosent match!");
        }
    }

    @Override
    public void onError(String utteranceId) {
//        Log.d(TAG, "onError: " + utteranceId);
    }

    @Override
    public void onStop(String utteranceId, boolean interrupted) {
        super.onStop(utteranceId, interrupted);
//        Log.d(TAG, "onStop, interrupted: " + utteranceId);
    }

    @Override
    public void onUtteranceCompleted(String utteranceId) {
//        Log.d(TAG, "onUtteranceCompleted: " + utteranceId);
        if (utteranceId.equals(currentUtterance)) {
            currentUtterance = "";
            wOnUtteranceStatusListener.onUtteranceCompleted();
        } else {
//            Log.d(TAG, "error: utterance dosent match!");
        }
    }

    protected interface OnUtteranceStatusListener {
        void onUtteranceCompleted();
    }
}
