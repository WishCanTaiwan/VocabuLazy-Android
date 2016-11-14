package wishcantw.vocabulazy.service;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;

import wishcantw.vocabulazy.application.GlobalVariable;
import wishcantw.vocabulazy.database.DatabaseUtils;
import wishcantw.vocabulazy.utility.Logger;
import wishcantw.vocabulazy.database.Database;
import wishcantw.vocabulazy.database.object.OptionSettings;
import wishcantw.vocabulazy.database.object.Vocabulary;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class AudioPlayer implements AudioPlayerListener {

    public interface BroadcastTrigger {
        void checkVoiceData();
        void onItemComplete();
        void onListComplete();
        void showDetail();
        void hideDetail();
        void toItem(int itemIndex);
        void toSentence(int sentenceIndex);
        void toNextList();
        void onPlayerStateChanged(String state);
    }

    public static final String TAG = AudioPlayer.class.getSimpleName();

    /* Playing Field */
    public static final String SPELL = "spell";
    public static final String TRANSLATION = "translation";
    public static final String EnSENTENCE = "en-sentence";
    public static final String CnSENTENCE = "cn-sentence";

    /* Player State */
    public static final String IDLE = "idle";
    public static final String PLAYING = "playing";
    public static final String PAUSE = "pause";
    public static final String HALT_BY_SCROLLING = "halt-by-scrolling";
    public static final String HALT_BY_FOCUS_LOSS = "halt-by-focus-loss";

    private GlobalVariable mGlobalVariable;
    private Database mDatabase;
    private DatabaseUtils mDatabaseUtils;
    private BroadcastTrigger mBroadcastTrigger;
    private Handler mHandler;
    private Runnable mTimerRunnable;

    private OptionSettings mOptionSettings;

    private int mItemIndex;
    private int mSentenceIndex;
    private String mPlayingField;
    private String mPlayerState;

    private VLTextToSpeech vlTextToSpeech;

    private boolean isTriggeredFromExam = false;
    private boolean isAudioFocused = false;
    private boolean isTimeOut = false;

    private int spellCountDown = 3;
    private int itemLoopCountDown;
    private int listLoopCountDown;

    private static AudioPlayer audioPlayer = new AudioPlayer();

    private AudioPlayer() {}

    public static AudioPlayer getInstance() {
        return audioPlayer;
    }

    public void init(Context context) {
        mGlobalVariable = (GlobalVariable) context;

        if (mDatabase == null) {
            mDatabase = Database.getInstance();
        }

        if (mDatabaseUtils == null) {
            mDatabaseUtils = DatabaseUtils.getInstance();
        }

        mHandler = new Handler();
        mTimerRunnable = new Runnable() {
            @Override
            public void run() {
                if (vlTextToSpeech == null) {
                    return;
                }
                if (isTimeOut) {
                    return;
                }
                isTimeOut = true;
                vlTextToSpeech.stop();
                updatePlayerInfo(mItemIndex, mSentenceIndex, mPlayingField, PAUSE);
            }
        };
    }

    public void bondToTTSEngine(Context context) {
        vlTextToSpeech = VLTextToSpeech.getInstance();
        vlTextToSpeech.init(context);
        vlTextToSpeech.setOnUtteranceFinishListener(this);
        mPlayerState = IDLE;
    }

    public void releaseTTSResource() {
        if (vlTextToSpeech == null) {
            return;
        }
        vlTextToSpeech.release();
        vlTextToSpeech = null;
    }

    public void getAudioFocus(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        isAudioFocused = (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED);
    }

    public void releaseAudioFocus(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.abandonAudioFocus(this);
        isAudioFocused = false;
    }

    public void setBroadcastTrigger(BroadcastTrigger trigger) {
        mBroadcastTrigger = trigger;
    }

    public void setOptionSettings(OptionSettings optionSettings) {
        if (optionSettings == null) {
            return;
        }

        itemLoopCountDown = optionSettings.getItemLoop();
        listLoopCountDown = optionSettings.getListLoop();
    }

    public void updateOptionSettings(OptionSettings optionSettings) {
//        int oldItemLoop = mGlobalVariable.playerItemLoop;
//        int oldListLoop = mGlobalVariable.playerListLoop;
//        int oldPlayTime = mGlobalVariable.playerPlayTime;
//
//        if (optionSettings == null) {
//            return;
//        }
//
//        int newItemLoop = optionSettings.getItemLoop();
//        int newListLoop = optionSettings.getListLoop();
//        int newPlayTime = optionSettings.getPlayTime();
//
////        mDatabase.setPlayerOptionSettings(optionSettings);
//
//        if (newItemLoop != oldItemLoop)
//            resetItemLoop();
//        if (newListLoop != oldListLoop)
//            resetListLoop();
//        if (newPlayTime != oldPlayTime)
//            resetTimer();
    }

    public void play(String utterance) {
        if (vlTextToSpeech == null) {
            return;
        }
        isTriggeredFromExam = true;
        vlTextToSpeech.stop();
        vlTextToSpeech.setLanguage(Locale.ENGLISH);
        vlTextToSpeech.speak(utterance, 1 /* default speed */);
    }

    /**
     * Start playing vocabulary based on the given information.
     *
     * @param itemIndex the index of the vocabularies
     * @param sentenceIndex the index of the sentence
     * @param playingField the playing field of vocabulary
     */
    void playItemAt(final int itemIndex, final int sentenceIndex, final String playingField) {

        // check vocabulary validity
        if (vlTextToSpeech == null || mOptionSettings == null) {
            return;
        }

        // get player content
        ArrayList<Vocabulary> content = mDatabase.getPlayerContent();

        String string = "";
        switch (playingField) {
            case SPELL:
                string = content.get(itemIndex).getSpell();
                vlTextToSpeech.setLanguage(Locale.ENGLISH);
                break;
            case TRANSLATION:
                string = content.get(itemIndex).getTranslation();
                vlTextToSpeech.setLanguage(Locale.TAIWAN);
                break;
            case EnSENTENCE:
                string = content.get(itemIndex).getEnSentence().get(sentenceIndex);
                vlTextToSpeech.setLanguage(Locale.ENGLISH);
                break;
            case CnSENTENCE:
                string = content.get(itemIndex).getCnSentence().get(sentenceIndex);
                vlTextToSpeech.setLanguage(Locale.TAIWAN);
                break;
            default:
                Logger.d(TAG, "unexpected case in startPlayingItemAt: " + itemIndex + ", " +
                        "playing " + playingField + ", sentence # " + sentenceIndex);
                break;
        }
        vlTextToSpeech.speak(string, mOptionSettings.getSpeed());
        vlTextToSpeech.speakSilence(isItemFinishing(itemIndex, sentenceIndex, playingField, mOptionSettings.getStopPeriod()));
        if (isTimeOut) startTimer();
        updatePlayerInfo(itemIndex, sentenceIndex, playingField, PLAYING);
    }

    public void playNewSentence(int newSentenceIndex) {
//        playItemAt(mItemIndex, newSentenceIndex, EnSENTENCE);
    }

    public void playButtonClick(Context context) {
        String playerState = mPlayerState;
        if (playerState.equals(PLAYING)) {
            playerState = PAUSE;
            if (vlTextToSpeech != null) {
                vlTextToSpeech.stop();
            }
        } else {
            if (!isAudioFocused) getAudioFocus(context);
            playerState = PLAYING;
            playItemAt(mItemIndex, mSentenceIndex, mPlayingField);
        }
        updatePlayerInfo(mItemIndex, mSentenceIndex, mPlayingField, playerState);
    }

    public void haltPlayer() {
        if (vlTextToSpeech != null) {
            vlTextToSpeech.stop();
        }
        updatePlayerInfo(mItemIndex, mSentenceIndex, mPlayingField, HALT_BY_SCROLLING);
    }

    public void startTimer() {
        if (mOptionSettings == null) {
            return;
        }
        int playTime = mOptionSettings.getPlayTime() * 1000 * 60;
        mHandler.postDelayed(mTimerRunnable, playTime);
        isTimeOut = false;
    }

    public void resetTimer() {
//        if (mOptionSettings == null) {
//            return;
//        }
//        mGlobalVariable.playerPlayTime = mOptionSettings.getPlayTime();
//        mHandler.removeCallbacks(mTimerRunnable);
//        startTimer();
    }

    public void resetSpellCountDown() {
        spellCountDown = 3;
    }

    public void resetItemLoop() {
//        if (mOptionSettings == null) {
//            return;
//        }
//        int itemLoop = mOptionSettings.getItemLoop();
//        mGlobalVariable.playerItemLoop = itemLoop;
//        itemLoopCountDown = itemLoop;
    }

    public void resetListLoop() {
//        int listLoop = mOptionSettings.getListLoop();
//        mGlobalVariable.playerListLoop = listLoop;
//        listLoopCountDown = listLoop;
    }

    @Override
    public void onEngineInit() {
        if (mBroadcastTrigger == null) {
            return;
        }
        mBroadcastTrigger.checkVoiceData();
    }

    @Override
    public void onUtteranceFinished(String utterance) {
        if (isTriggeredFromExam) {
            updatePlayerInfo(mItemIndex, mSentenceIndex, mPlayingField, PAUSE);
            isTriggeredFromExam = false;
            return;
        }

        int oldItemIndex = mItemIndex;
        int oldSentenceIndex = mSentenceIndex;
        int newItemIndex = oldItemIndex;
        int newSentenceIndex = oldSentenceIndex;
        String playingField = mPlayingField;
        String playerState = mPlayerState;

        if (playerState.equals(HALT_BY_SCROLLING) || playerState.equals(PAUSE)) return;

        if (mOptionSettings == null || mBroadcastTrigger == null) {
            return;
        }

        switch (playingField) {
            case SPELL:
                spellCountDown--;
                playingField = (spellCountDown > 0 ? SPELL : TRANSLATION); /* if SPELL has been played for three times, next will play TRANSLATION */
                break;
            case TRANSLATION:
                resetSpellCountDown();
                if (mOptionSettings.isSentence()) {
                    newSentenceIndex = 0;
                    playingField = EnSENTENCE;
                    mBroadcastTrigger.showDetail();
                    break;
                }

                itemLoopCountDown--;
                if (itemLoopCountDown > 0) {
                    playingField = SPELL;
                    break;
                }

                mBroadcastTrigger.hideDetail();
                mBroadcastTrigger.onItemComplete();
                resetItemLoop();
                newItemIndex = pickNextItem(oldItemIndex);
                if (newItemIndex < 0) {
                    newItemIndex = 0;
                    listLoopCountDown--;
                    if (listLoopCountDown == 0) {
                        mBroadcastTrigger.onListComplete();
                        resetListLoop();
                        loadNewContent();
                        mBroadcastTrigger.toNextList();
                        sleep(1500);
                    }
                }

                newSentenceIndex = (mOptionSettings.isSentence() ? 0 : -1);
                playingField = SPELL;
                mBroadcastTrigger.toItem(newItemIndex);
                break;
            case EnSENTENCE:
                playingField = CnSENTENCE;
                break;
            case CnSENTENCE:
                if (mOptionSettings.isSentence() && !isLastSentence(oldItemIndex, oldSentenceIndex)) {
                    newSentenceIndex = oldSentenceIndex + 1;
                    playingField = EnSENTENCE;
                    mBroadcastTrigger.toSentence(newSentenceIndex);
                    break;
                }

                itemLoopCountDown--;
                if (itemLoopCountDown > 0) {
                    playingField = SPELL;
                    mBroadcastTrigger.toSentence(0);
                    break;
                }

                mBroadcastTrigger.hideDetail();
                mBroadcastTrigger.onItemComplete();
                resetItemLoop();
                newItemIndex = pickNextItem(oldItemIndex);
                if (newItemIndex < 0) {
                    newItemIndex = 0;
                    listLoopCountDown--;
                    if (listLoopCountDown == 0) {
                        mBroadcastTrigger.onListComplete();
                        resetListLoop();
                        loadNewContent();
                        mBroadcastTrigger.toNextList();
                        sleep(1500);
                    }
                }

                newSentenceIndex = (mOptionSettings.isSentence() ? 0 : -1);
                playingField = SPELL;
                mBroadcastTrigger.toItem(newItemIndex);
                break;
            default:
                break;
        }

        playItemAt(newItemIndex, newSentenceIndex, playingField);
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        String playerState = mPlayerState;
        if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
            if (playerState.equals(HALT_BY_FOCUS_LOSS)) {
                playerState = PLAYING;
                playItemAt(mItemIndex, mSentenceIndex, mPlayingField);
            }
        }

        if (focusChange == AudioManager.AUDIOFOCUS_LOSS ||
                focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
            isAudioFocused = false;
            if (playerState.equals(PLAYING)) {
                playerState = HALT_BY_FOCUS_LOSS;
                if (vlTextToSpeech != null) {
                    vlTextToSpeech.stop();
                }
                mPlayerState = playerState; /* the player is halted by focus loss */
            }
        }
    }

    private int isItemFinishing(int itemIndex, int sentenceIndex, String playingField, int stopPeriod) {
        int lengthOfPause = 400;
        boolean isSentence = mOptionSettings != null && mOptionSettings.isSentence();
        if (isSentence && playingField.equals(CnSENTENCE) && isLastSentence(itemIndex, sentenceIndex)
                || !isSentence && playingField.equals(TRANSLATION))
            lengthOfPause = 400 + stopPeriod * 1000;
        return lengthOfPause;
    }

    private void updatePlayerInfo(int itemIndex, int sentenceIndex, String playingField, String playerState) {
//        if (!playerState.equals(mPlayerState) && mBroadcastTrigger != null) {
//            mBroadcastTrigger.onPlayerStateChanged(playerState);
//        }
//
//        mGlobalVariable.playerItemIndex = itemIndex;
//        mGlobalVariable.playerSentenceIndex = sentenceIndex;
//        mGlobalVariable.playerState = playerState;
//        mGlobalVariable.playingField = playingField;
//
//        mItemIndex = itemIndex;
//        mSentenceIndex = sentenceIndex;
//        mPlayingField = playingField;
//        mPlayerState = playerState;
    }

    private int pickNextItem(int oldItemIndex) {
        int next;
        int itemAmount = mDatabase.getPlayerContent().size();
        if (mOptionSettings != null && mOptionSettings.isRandom()) {
            Random random = new Random(System.currentTimeMillis());
            do {
                next = random.nextInt(itemAmount);
            } while (oldItemIndex == next);
        } else {
            next = (isLastItem(oldItemIndex) ? -1 : oldItemIndex+1);
        }
        return next;
    }

    private void loadNewContent() {
//        if (mGlobalVariable == null || mDatabase == null) {
//            return;
//        }
//        int bookIndex =
//        int numOfLesson = DatabaseUtils.getInstance().getLessonAmount(Database.getInstance().getTextbooks(), bookIndex);
//        int oldLessonIndex = mGlobalVariable.playerLessonIndex;
//        int newLessonIndex = (oldLessonIndex+1) % numOfLesson;
//        mGlobalVariable.playerTextbookIndex = bookIndex;
//        mGlobalVariable.playerLessonIndex = newLessonIndex;
//
//        mDatabase.setPlayerContent(mDatabaseUtils.getVocabulariesByIDs(mDatabase.getVocabularies(), (bookIndex == -1)
//                ? mDatabaseUtils.getNoteContent(mDatabase.getNotes(), newLessonIndex)
//                : mDatabaseUtils.getLessonContent(mDatabase.getTextbooks(), bookIndex, newLessonIndex)));
    }

    private boolean isLastItem(int itemIndex) {
        return (itemIndex == mDatabase.getPlayerContent().size()-1);
    }

    private boolean isLastSentence(int itemIndex, int sentenceIndex) {
        int sentenceAmount = 0;
        ArrayList<Vocabulary> content = mDatabase.getPlayerContent();
        if (itemIndex < content.size() && itemIndex > -1 && content.get(itemIndex) != null) {
            sentenceAmount = content.get(itemIndex).getSentenceAmount();
        }
        return (sentenceIndex == sentenceAmount-1);
    }

    private void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
