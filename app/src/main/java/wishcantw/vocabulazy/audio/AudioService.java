package wishcantw.vocabulazy.audio;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import wishcantw.vocabulazy.database.AppPreference;
import wishcantw.vocabulazy.database.Database;
import wishcantw.vocabulazy.utility.Logger;

public class AudioService extends IntentService implements AudioManager.OnAudioFocusChangeListener {

    public static final String TAG = AudioService.class.getSimpleName();

    /* Action received from other activity or fragment */
    public static final String START_SERVICE = "start-service";
    public static final String STOP_SERVICE = "stop-service";
    public static final String GET_AUDIO_FOCUS = "get-audio-focus";
    public static final String RELEASE_AUDIO_FOCUS = "release-audio-focus";
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
    public static final String EXAM_UTTERANCE = "exam-utterance";

    private AudioPlayer mAudioPlayer;
    private AudioPlayerUtils audioPlayerUtils;
    private AudioServiceBroadcaster audioServiceBroadcaster;

    @SuppressWarnings("unused")
    public AudioService() {
        super(null);
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    @SuppressWarnings("unused")
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
     * @param intent the intent
     * @param flags the flag
     * @param startId the start id
     * @return START_STICKY: representing that the service object will last after each command has
     *                       been executed.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent == null) {
            return START_STICKY;
        }

        if (audioServiceBroadcaster == null) {
            audioServiceBroadcaster = new AudioServiceBroadcaster(getApplicationContext());
        }

        if (mAudioPlayer == null) {
            mAudioPlayer = AudioPlayer.getInstance();
            mAudioPlayer.init(getApplicationContext(), audioServiceBroadcaster);
        }

        AppPreference appPreference = AppPreference.getInstance();

        String action = intent.getAction();
        Logger.d(TAG, action);

        /**
         * switch-case block for deciding the corresponding tasks for each action
         */
        switch (action) {

            case START_SERVICE:
                if (audioPlayerUtils == null) {
                    audioPlayerUtils = AudioPlayerUtils.getInstance();
                }
                break;

            case STOP_SERVICE:
                ((AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE))
                        .abandonAudioFocus(this);
                appPreference.setAudioFocused(false);
                break;

            case GET_AUDIO_FOCUS:
                int result = ((AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE))
                        .requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
                appPreference.setAudioFocused(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED);
                break;

            case RELEASE_AUDIO_FOCUS:
                ((AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE))
                        .abandonAudioFocus(this);
                appPreference.setAudioFocused(false);
                break;

            case START_SINGLE_ITEM:
                String utterance = intent.getStringExtra(EXAM_UTTERANCE);
                mAudioPlayer.play(utterance);
                break;

            case NEW_ITEM_FOCUSED:
                mAudioPlayer.resetItemLoopCountDown();
                mAudioPlayer.resetSpellLoopCountDown();
                switch (appPreference.getPlayerState()) {
                    case PLAYING: case SCROLLING_WHILE_PLAYING:
                        mAudioPlayer.play(appPreference.getPlayerItemIndex(), appPreference.getPlayerField());
                        break;
                    case STOPPED: case SCROLLING_WHILE_STOPPED: case STOPPED_BY_FOCUS_CHANGE:
                        break;
                }
                break;

            case START_PLAYING:
                mAudioPlayer.resetItemLoopCountDown();
                mAudioPlayer.resetSpellLoopCountDown();
                mAudioPlayer.play(appPreference.getPlayerItemIndex(), appPreference.getPlayerField());
                break;

            case NEW_LIST_FOCUSED:
                mAudioPlayer.resetItemLoopCountDown();
                mAudioPlayer.resetListLoopCountDown();
                mAudioPlayer.resetSpellLoopCountDown();
                break;

            case NEW_SENTENCE_FOCUSED:
                break;

            case OPTION_SETTINGS_CHANGED:
                mAudioPlayer.updateOptionSettings(Database.getInstance().getPlayerOptionSettings());
                break;

            case PLAY_BUTTON_CLICKED:
                if (appPreference.getPlayerState().equals(AudioPlayerUtils.PlayerState.PLAYING)) {
                    mAudioPlayer.stop();
                    appPreference.setPlayerState(AudioPlayerUtils.PlayerState.STOPPED);
                    audioServiceBroadcaster.onPlayerStateChanged();

                } else {
                    if (!appPreference.isAudioFocused()) {
                        result = ((AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE))
                                .requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
                        appPreference.setAudioFocused(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED);
                    }
                    mAudioPlayer.play(appPreference.getPlayerItemIndex(), appPreference.getPlayerField());
                    audioServiceBroadcaster.onPlayerStateChanged();
                }
                break;

            case PLAYERVIEW_SCROLLING:
                mAudioPlayer.stop();
                appPreference.setPlayerState((appPreference.getPlayerState().equals(AudioPlayerUtils.PlayerState.PLAYING))
                        ? AudioPlayerUtils.PlayerState.SCROLLING_WHILE_PLAYING
                        : AudioPlayerUtils.PlayerState.SCROLLING_WHILE_STOPPED);
                audioServiceBroadcaster.onPlayerStateChanged();
                break;

            case START_TIMER:
                break;

            default:
                Logger.d(TAG, "undefined case");
                return START_REDELIVER_INTENT;
        }

        return START_STICKY;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        AppPreference appPreference = AppPreference.getInstance();
        AudioPlayerUtils.PlayerState playerState = appPreference.getPlayerState();

        if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
            if (playerState.equals(AudioPlayerUtils.PlayerState.STOPPED_BY_FOCUS_CHANGE)) {
                appPreference.setPlayerState(AudioPlayerUtils.PlayerState.PLAYING);
                audioServiceBroadcaster.onPlayerStateChanged();
                mAudioPlayer.play(appPreference.getPlayerItemIndex(), appPreference.getPlayerField());
            }
        }

        if (focusChange == AudioManager.AUDIOFOCUS_LOSS ||
                focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
            appPreference.setAudioFocused(false);
            if (playerState.equals(AudioPlayerUtils.PlayerState.PLAYING)) {
                appPreference.setPlayerState(AudioPlayerUtils.PlayerState.STOPPED_BY_FOCUS_CHANGE);
                audioServiceBroadcaster.onPlayerStateChanged();
                // todo: release vl text to speech
            }
        }
    }
}
