package com.wishcan.www.vocabulazy.service;

import android.app.IntentService;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;

import com.wishcan.www.vocabulazy.storage.Option;
import com.wishcan.www.vocabulazy.storage.Vocabulary;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

/**
 * Created by allencheng07 on 2016/1/26.
 */
public class AudioService extends IntentService implements AudioPlayer.OnEventListener, WCTextToSpeech.OnUtteranceStatusListener {

    /**
     * tag for debugging
     */
    public static final String TAG = AudioService.class.getSimpleName();

    /**
     * strings for intent action identification
     */
    public static final String ACTION_START_SERVICE = "start-service";
    public static final String ACTION_STOP_SERVICE = "stop-service";
    public static final String ACTION_SET_CONTENT = "set-content";
    public static final String ACTION_START_PLAYING = "start-playing";
    public static final String ACTION_PAUSE_PLAYING = "pause-playing";
    public static final String ACTION_RESUME_PLAYING = "resume-playing";
    public static final String ACTION_PLAY_BUTTON_CLICKED = "play-button-clicked";
    public static final String ACTION_OPTION_SETTING_CHANGED = "option-setting-changed";

    /**
     * keys for bundle identification
     */
    public static final String KEY_PLAYER_CONTENT = "player-content";
    public static final String KEY_START_ITEM_INDEX = "start-item-index";
    public static final String KEY_START_SENTENCE_INDEX = "start-sentence-index";
    public static final String KEY_PLAYING_FIELD = "playing-field";
    public static final String KEY_OPTION_SETTING = "option-setting";

    /**
     * strings for identifying the status of the player
     */
    public static final String STATUS_IDLE = "status-idle";
    public static final String STATUS_PLAYING = "status-playing";
    public static final String STATUS_PAUSE = "status-pause";
    public static final String STATUS_STOPPED = "status-stopped";

    /**
     * strings for identifying what field of the vocabulary is being played
     */
    public static final String PLAYING_DEFAULT = "playing-default";
    public static final String PLAYING_SPELL = "playing-spell";
    public static final String PLAYING_TRANSLATION = "playing-translation";
    public static final String PLAYING_EnSENTENCE = "playing-ensentence";
    public static final String PLAYING_CnSENTENCE = "playing-cnsentence";

    private AudioPlayer wAudioPlayer;
    private ArrayList<Vocabulary> wVoabularies;
    private Option wOptionSetting;
    private WCTextToSpeech wcTextToSpeech;

    private int wCurrentItemIndex = -1;
    private int wCurrentItemAmount = 0;
    private int wCurrentSentenceIndex = -1;
    private int wCurrentSentenceAmount = 0;
    private String wStatus = STATUS_IDLE;
    private String wPlaying = PLAYING_DEFAULT;

    private boolean wIsRandom = false;
    private boolean wEnSentenceEnabled = true;
    private boolean wCnSentenceEnabled = false;

    public AudioService() {
        super(null);
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AudioService(String name) {
        super(name);
    }

    /**
     * the method is required by the IntentService class.
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {

    }

    /**
     * the purpose of overriding the method startcommand(intent, flags, startid) is to return START_STICKY
     * rather than default return value.
     *
     * @param intent
     * @param flags
     * @param startId
     * @return START_STICKY: representing that the service object will last after each command has
     *                       been executed.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String action = intent.getAction();
        Log.d(TAG, action);

        /**
         * switch-case block for deciding the corresponding tasks for each action
         */
        switch (action) {

            case ACTION_START_SERVICE:
                wAudioPlayer = new AudioPlayer(this);
                wcTextToSpeech = new WCTextToSpeech(getApplicationContext(), this);
                break;

            case ACTION_STOP_SERVICE:
                wAudioPlayer.releasePlayer();
                wcTextToSpeech.shutdown();
                break;

            case ACTION_SET_CONTENT:
                wVoabularies = intent.getParcelableArrayListExtra(KEY_PLAYER_CONTENT);
                wCurrentItemAmount = wVoabularies.size();
//                Log.d(TAG, voabularies.size() + " vocabulary objects received from controller");
                break;

            case ACTION_START_PLAYING:
                int itemIndex = intent.getIntExtra(KEY_START_ITEM_INDEX, -1);
                int sentenceIndex = intent.getIntExtra(KEY_START_SENTENCE_INDEX, -1);
                String playing = intent.getStringExtra(KEY_PLAYING_FIELD);

                wCurrentItemIndex = itemIndex;
                wCurrentSentenceIndex = sentenceIndex;
                wStatus = STATUS_PLAYING;
                wPlaying = playing;
                wCurrentSentenceAmount = wVoabularies.get(wCurrentItemIndex).getEn_Sentence().size();
                startPlayingItemAt(itemIndex, sentenceIndex);
                break;

            case ACTION_PAUSE_PLAYING:
                wcTextToSpeech.pause();
                break;

            case ACTION_RESUME_PLAYING:
                startPlayingItemAt(wCurrentItemIndex, wCurrentSentenceIndex);
                break;

            case ACTION_PLAY_BUTTON_CLICKED:
                if (wStatus.equals(STATUS_PLAYING)) {
                    wcTextToSpeech.pause();
                    wStatus = STATUS_PAUSE;
                }
                else {
                    startPlayingItemAt(wCurrentItemIndex, wCurrentSentenceIndex);
                    wStatus = STATUS_PLAYING;
                }
                break;

            case ACTION_OPTION_SETTING_CHANGED:
                wOptionSetting = intent.getParcelableExtra(KEY_OPTION_SETTING);
                updateOptionSetting(wOptionSetting);
                break;

            default:
                Log.d(TAG, "undefined case in onHandleIntent");
                break;
        }

        return START_STICKY;
    }

    void startPlayingItemAt(int itemIndex, int sentenceIndex) {

        String string;

        switch (wPlaying) {

            case PLAYING_SPELL:
                string = wVoabularies.get(itemIndex).getSpell();
                wcTextToSpeech.setLanguage(Locale.ENGLISH);
                wcTextToSpeech.speak(string);
                break;

            case PLAYING_TRANSLATION:
                string = wVoabularies.get(itemIndex).getTranslationInOneString();
                wcTextToSpeech.setLanguage(Locale.TAIWAN);
                wcTextToSpeech.speak(string);
                break;

            case PLAYING_EnSENTENCE:
                string = wVoabularies.get(itemIndex).getEn_Sentence().get(sentenceIndex);
                wcTextToSpeech.setLanguage(Locale.ENGLISH);
                wcTextToSpeech.speak(string);
                break;

            case PLAYING_CnSENTENCE:
                string = wVoabularies.get(itemIndex).getCn_Sentence().get(sentenceIndex);
                wcTextToSpeech.setLanguage(Locale.TAIWAN);
                wcTextToSpeech.speak(string);
                break;

            default:
                Log.d(TAG, "unexpected case in startPlayingItemAt: " + itemIndex + ", " +
                        "playing " + wPlaying + ", sentence # " + sentenceIndex);
                break;
        }
    }

    int pickNextItem(int currentIndex) {
        int nextItem = -1;
        if (wIsRandom) {
            Random random = new Random(System.currentTimeMillis());
            do {
                nextItem = random.nextInt(wCurrentItemAmount);
            } while (currentIndex == nextItem);
            return nextItem;
        } else {
            currentIndex++;
            if (currentIndex < wCurrentItemAmount) return currentIndex;
        }
        return nextItem;
    }

    void updateOptionSetting(Option optionSetting) {
        wIsRandom = optionSetting.mIsRandom;
        wEnSentenceEnabled = optionSetting.mSentence;
        wCnSentenceEnabled = optionSetting.mSentence;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestory");
        if (wcTextToSpeech != null)
            wcTextToSpeech.shutdown();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(TAG, "onCompletion");
    }

    @Override
    public void onUtteranceCompleted() {
        Log.d(TAG, "onUtteranceCompleted");

        if (!wStatus.equals(STATUS_PLAYING)) return;

        switch (wPlaying) {

            case PLAYING_SPELL:
                wPlaying = PLAYING_TRANSLATION;
                break;

            case PLAYING_TRANSLATION:
                if (wEnSentenceEnabled) {
                    wCurrentSentenceIndex = 0;
                    wPlaying = PLAYING_EnSENTENCE;
                } else if (wCnSentenceEnabled) {
                    wCurrentSentenceIndex = 0;
                    wPlaying = PLAYING_CnSENTENCE;
                } else {
                    wCurrentItemIndex = pickNextItem(wCurrentItemIndex);
                    wCurrentSentenceIndex = -1;
                    wCurrentSentenceAmount = wVoabularies.get(wCurrentItemIndex).getEn_Sentence().size();
                    wPlaying = PLAYING_SPELL;
                }
                break;

            case PLAYING_EnSENTENCE:
                if (wCnSentenceEnabled) {
                    wPlaying = PLAYING_CnSENTENCE;
                    break;
                }

                wCurrentSentenceIndex++;
                if (wCurrentSentenceIndex < wCurrentSentenceAmount) {
                    wPlaying = PLAYING_EnSENTENCE;
                } else if (wCurrentSentenceIndex == wCurrentSentenceAmount) {
                    wCurrentItemIndex = pickNextItem(wCurrentItemIndex);
                    wCurrentSentenceIndex = -1;
                    wCurrentSentenceAmount = wVoabularies.get(wCurrentItemIndex).getEn_Sentence().size();
                    wPlaying = PLAYING_SPELL;
                }

                break;

            case PLAYING_CnSENTENCE:

                wCurrentSentenceIndex++;
                if (wCurrentSentenceIndex < wCurrentSentenceAmount) {
                    if (wEnSentenceEnabled) {
                        wPlaying = PLAYING_EnSENTENCE;
                    } else if (wCnSentenceEnabled) {
                        wPlaying = PLAYING_CnSENTENCE;
                    }
                } else if (wCurrentSentenceIndex == wCurrentSentenceAmount) {
                    wCurrentItemIndex = pickNextItem(wCurrentItemIndex);
                    wCurrentSentenceIndex = -1;
                    wCurrentSentenceAmount = wVoabularies.get(wCurrentItemIndex).getEn_Sentence().size();
                    wPlaying = PLAYING_SPELL;
                }

                break;

            default:

                break;
        }

        startPlayingItemAt(wCurrentItemIndex, wCurrentSentenceIndex);
    }
}
