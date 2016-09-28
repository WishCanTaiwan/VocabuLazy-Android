package com.wishcan.www.vocabulazy.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.wishcan.www.vocabulazy.application.GlobalVariable;
import com.wishcan.www.vocabulazy.utility.Logger;
import com.wishcan.www.vocabulazy.storage.databaseObjects.OptionSettings;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Vocabulary;

import java.util.ArrayList;

public class AudioService extends IntentService {

    public static final String TAG = AudioService.class.getSimpleName();

    /* Action received from other activity or fragment */
    public static final String START_SERVICE = "start-service";
    public static final String STOP_SERVICE = "stop-service";
    public static final String GET_AUDIO_FOCUS = "get-audio-focus";
    public static final String RELEASE_AUDIO_FOCUS = "release-audio-focus";
    public static final String SET_CONTENT = "set-content";
    public static final String START_SINGLE_ITEM = "start-single-item";
    public static final String START_PLAYING = "start-playing";
    public static final String NEW_ITEM_FOCUSED = "new-item-focused";
    public static final String NEW_LIST_FOCUSED = "new-list-focused";
    public static final String NEW_SENTENCE_FOCUSED = "new-sentence-focused";
    public static final String OPTION_SETTINGS_CHANGED = "option-settings-changed";
    public static final String PLAY_BUTTON_CLICKED = "play-button-clicked";
    public static final String PLAYERVIEW_SCROLLING = "playerview-scrolling";
    public static final String START_TIMER = "start-timer";

    /* Action to be broadcast to activity or fragment */
    public static final String CHECK_VOICE_DATA = "check-voice-data";
    public static final String ITEM_COMPLETE = "item-complete";
    public static final String LIST_COMPLETE = "list-complete";
    public static final String SHOW_DETAIL = "show-detail";
    public static final String HIDE_DETAIL = "hide-detail";
    public static final String TO_ITEM = "to-item";
    public static final String TO_SENTENCE = "to-sentence";
    public static final String TO_NEXT_LIST = "to-next-list";
    public static final String PLAYER_STATE_CHANGED = "player-state-changed";

    /* Key used for intent among service, activity and fragment */
    public static final String ITEM_INDEX = "item-index";
    public static final String SENTENCE_INDEX = "sentence-index";
    public static final String PLAYING_FIELD = "playing-field";
    public static final String EXAM_UTTERANCE = "exam-utterance";
    public static final String PLAYER_STATE = "player-state";

    private GlobalVariable mGlobalVariable;
//    private Preferences mPreferences;
    private AudioPlayer mAudioPlayer;

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

        String action = (intent != null) ? intent.getAction() : "";
        Log.d(TAG, action);
        /**
         * switch-case block for deciding the corresponding tasks for each action
         */
        switch (action) {

            case START_SERVICE:
                mGlobalVariable = (GlobalVariable) getApplication();
                mAudioPlayer = new AudioPlayer(mGlobalVariable);
                mAudioPlayer.bondToTTSEngine();
                Broadcaster broadcaster = new Broadcaster(mGlobalVariable.getApplicationContext());
                mAudioPlayer.setBroadcastTrigger(broadcaster);
                break;

            case STOP_SERVICE:
                if (mAudioPlayer == null)
                    break;
                mAudioPlayer.releaseAudioFocus();
                mAudioPlayer.releaseTTSResource();
                break;

            case GET_AUDIO_FOCUS:
                if (mAudioPlayer == null)
                    break;
                mAudioPlayer.getAudioFocus();
                break;

            case RELEASE_AUDIO_FOCUS:
                if (mAudioPlayer == null)
                    break;
                mAudioPlayer.releaseAudioFocus();
                break;

            case SET_CONTENT:
                if (mAudioPlayer == null)
                    break;
                ArrayList<Vocabulary> vocabularies = mGlobalVariable.playerContent;
                OptionSettings optionSetting = mGlobalVariable.optionSettings.get(mGlobalVariable.optionMode);
                mAudioPlayer.setContent(vocabularies);
                mAudioPlayer.setOptionSettings(optionSetting);
                break;

            case START_SINGLE_ITEM:
                if (intent == null)
                    break;
                if (mAudioPlayer == null)
                    break;
                String utterance = intent.getStringExtra(EXAM_UTTERANCE);
                mAudioPlayer.play(utterance);
                break;

            case NEW_ITEM_FOCUSED:
                if (mAudioPlayer == null)
                    break;
                mAudioPlayer.resetSpellCountDown();
                mAudioPlayer.resetItemLoop();
                mAudioPlayer.resetListLoop();
                mAudioPlayer.resetTimer();
            case START_PLAYING:
                if (intent == null)
                    break;
                if (mAudioPlayer == null)
                    break;
                int itemIndex = intent.getIntExtra(ITEM_INDEX, -1);
                int sentenceIndex = intent.getIntExtra(SENTENCE_INDEX, -1);
                String playingField = intent.getStringExtra(PLAYING_FIELD);
                mAudioPlayer.playItemAt(itemIndex, sentenceIndex, playingField);
                mAudioPlayer.resetSpellCountDown();
                mAudioPlayer.resetItemLoop();
                mAudioPlayer.resetListLoop();
                mAudioPlayer.resetTimer();
                break;

            case NEW_LIST_FOCUSED:
                if (mAudioPlayer == null)
                    break;
                mAudioPlayer.resetSpellCountDown();
                mAudioPlayer.resetItemLoop();
                mAudioPlayer.resetListLoop();
                mAudioPlayer.resetTimer();
                break;

            case NEW_SENTENCE_FOCUSED:
                if (intent == null)
                    break;
                if (mAudioPlayer == null)
                    break;
                int newSentenceIndex = intent.getIntExtra(SENTENCE_INDEX, -1);
                mAudioPlayer.playNewSentence(newSentenceIndex);
                break;

            case OPTION_SETTINGS_CHANGED:
                if (mAudioPlayer == null)
                    break;
                OptionSettings optionSettings = mGlobalVariable.optionSettings.get(mGlobalVariable.optionMode);
                mAudioPlayer.updateOptionSettings(optionSettings);
                break;

            case PLAY_BUTTON_CLICKED:
                if (mAudioPlayer == null)
                    break;
                mAudioPlayer.playButtonClick();
                break;

            case PLAYERVIEW_SCROLLING:
                if (mAudioPlayer == null)
                    break;
                mAudioPlayer.haltPlayer();
                break;

            case START_TIMER:
                if (mAudioPlayer == null)
                    break;
                mAudioPlayer.startTimer();
                break;

            default:
                Logger.d(TAG, "undefined case");
                return START_REDELIVER_INTENT;
        }

        return START_STICKY;
    }

    private class Broadcaster implements AudioPlayer.BroadcastTrigger {
        private Context context;

        private String broadcastIntent = GlobalVariable.PLAYER_BROADCAST_INTENT;
        private String broadcastAction = GlobalVariable.PLAYER_BROADCAST_ACTION;

        public Broadcaster(Context context) {
            this.context = context;
        }

        @Override
        public void checkVoiceData() {
            Intent intent = new Intent(GlobalVariable.PLAYER_BROADCAST_INTENT)
                    .putExtra(broadcastAction, CHECK_VOICE_DATA);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }

        @Override
        public void onItemComplete() {
            Intent intent = new Intent(broadcastIntent)
                    .putExtra(broadcastAction, ITEM_COMPLETE);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }

        @Override
        public void onListComplete() {
            Intent intent = new Intent(broadcastIntent)
                    .putExtra(broadcastAction, LIST_COMPLETE);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }

        @Override
        public void showDetail() {
            Intent intent = new Intent(broadcastIntent)
                    .putExtra(broadcastAction, SHOW_DETAIL);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }

        @Override
        public void hideDetail() {
            Intent intent = new Intent(broadcastIntent)
                    .putExtra(broadcastAction, HIDE_DETAIL);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }

        @Override
        public void toItem(int nextItemIndex) {
            Intent intent = new Intent(broadcastIntent)
                    .putExtra(broadcastAction, TO_ITEM)
                    .putExtra(ITEM_INDEX, nextItemIndex);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }

        @Override
        public void toSentence(int sentenceIndex) {
            Intent intent = new Intent(broadcastIntent)
                    .putExtra(broadcastAction, TO_SENTENCE)
                    .putExtra(SENTENCE_INDEX, sentenceIndex);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }

        @Override
        public void toNextList() {
            Intent intent = new Intent(broadcastIntent)
                    .putExtra(broadcastAction, TO_NEXT_LIST);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }

        @Override
        public void onPlayerStateChanged(String state) {
            Intent intent = new Intent(broadcastIntent)
                    .putExtra(broadcastAction, PLAYER_STATE_CHANGED)
                    .putExtra(PLAYER_STATE, state);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }
    }
}
