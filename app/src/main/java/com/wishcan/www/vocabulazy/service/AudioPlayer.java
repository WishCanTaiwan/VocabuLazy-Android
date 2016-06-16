package com.wishcan.www.vocabulazy.service;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.util.Log;

import com.wishcan.www.vocabulazy.VLApplication;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.Preferences;
import com.wishcan.www.vocabulazy.storage.databaseObjects.OptionSettings;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Vocabulary;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class AudioPlayer implements AudioPlayerListener {

    public interface BroadcastTrigger {
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

    private Context mContext;
    private Preferences mPreferences;
    private Database mDatabase;
    private BroadcastTrigger mBroadcastTrigger;
    private Handler mHandler;
    private Runnable mTimerRunnable;

    private ArrayList<Vocabulary> mVocabularies;
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

    public AudioPlayer(VLApplication application) {
        mContext = application.getApplicationContext();
        mPreferences = application.getPreferences();
        mDatabase = application.getDatabase();
        mHandler = new Handler();
        mTimerRunnable = new Runnable() {
            @Override
            public void run() {
                if (vlTextToSpeech == null)
                    return;
                if (isTimeOut)
                    return;
                isTimeOut = true;
                vlTextToSpeech.pause();
                updatePlayerInfo(mItemIndex, mSentenceIndex, mPlayingField, PAUSE);
            }
        };
    }

    public void bondToTTSEngine() {
        vlTextToSpeech = new VLTextToSpeech(mContext);
        vlTextToSpeech.initTTS();
        vlTextToSpeech.setUpListener(this);
        mPlayerState = IDLE;
    }

    public void releaseTTSResource() {
        if (vlTextToSpeech == null)
            return;
        vlTextToSpeech.release();
        vlTextToSpeech = null;
    }

    public void getAudioFocus() {
        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        Log.d(TAG, "get audio focus, result " + result);
        isAudioFocused = (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED);
    }

    public void releaseAudioFocus() {
        Log.d(TAG, "release audio focus");
        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        audioManager.abandonAudioFocus(this);
        isAudioFocused = false;
    }

    public void setBroadcastTrigger(BroadcastTrigger trigger) {
        mBroadcastTrigger = trigger;
    }

    public void setContent(ArrayList<Vocabulary> vocabularies) {
        mVocabularies = vocabularies;
    }

    public void setOptionSettings(OptionSettings optionSettings) {
        mOptionSettings = optionSettings;
        itemLoopCountDown = optionSettings.getItemLoop();
        listLoopCountDown = optionSettings.getListLoop();

        mPreferences.setItemLoop(optionSettings.getItemLoop());
        mPreferences.setListLoop(optionSettings.getListLoop());
        mPreferences.setPlayTime(optionSettings.getPlayTime());
    }

    public void updateOptionSettings(OptionSettings optionSettings) {
        int oldItemLoop = mPreferences.getItemLoop();
        int oldListLoop = mPreferences.getListLoop();
        int oldPlayTime = mPreferences.getPlayTime();

        int newItemLoop = optionSettings.getItemLoop();
        int newListLoop = optionSettings.getListLoop();
        int newPlayTime = optionSettings.getPlayTime();

        mOptionSettings = optionSettings;
        if (newItemLoop != oldItemLoop)
            resetItemLoop();
        if (newListLoop != oldListLoop)
            resetListLoop();
        if (newPlayTime != oldPlayTime)
            resetTimer();
    }

    public void play(String utterance) {
        isTriggeredFromExam = true;
        vlTextToSpeech.stop();
        vlTextToSpeech.setLanguage(Locale.ENGLISH);
        vlTextToSpeech.speak(utterance, 1 /* default speed */);
    }

    public void playItemAt(int itemIndex, int sentenceIndex, String playingField) {
        String string = "";
        switch (playingField) {
            case SPELL:
                string = mVocabularies.get(itemIndex).getSpell();
                vlTextToSpeech.setLanguage(Locale.ENGLISH);
                break;
            case TRANSLATION:
                string = mVocabularies.get(itemIndex).getTranslation();
                vlTextToSpeech.setLanguage(Locale.TAIWAN);
                break;
            case EnSENTENCE:
                string = mVocabularies.get(itemIndex).getEnSentences().get(sentenceIndex);
                vlTextToSpeech.setLanguage(Locale.ENGLISH);
                break;
            case CnSENTENCE:
                string = mVocabularies.get(itemIndex).getCnSentences().get(sentenceIndex);
                vlTextToSpeech.setLanguage(Locale.TAIWAN);
                break;
            default:
                Log.d(TAG, "unexpected case in startPlayingItemAt: " + itemIndex + ", " +
                        "playing " + playingField + ", sentence # " + sentenceIndex);
                break;
        }
        vlTextToSpeech.speak(string, mOptionSettings.getSpeed());
        vlTextToSpeech.speakSilence(isItemFinishing(itemIndex, sentenceIndex, playingField, mOptionSettings.getStopPeriod()));
        if (isTimeOut) startTimer();
        updatePlayerInfo(itemIndex, sentenceIndex, playingField, PLAYING);
    }

    public void playNewSentence(int newSentenceIndex) {
        playItemAt(mItemIndex, newSentenceIndex, EnSENTENCE);
    }

    public void playButtonClick() {
        String playerState = mPlayerState;
        if (playerState.equals(PLAYING)) {
            playerState = PAUSE;
            vlTextToSpeech.pause();
        } else {
            if (!isAudioFocused) getAudioFocus();
            playerState = PLAYING;
            playItemAt(mItemIndex, mSentenceIndex, mPlayingField);
        }
        updatePlayerInfo(mItemIndex, mSentenceIndex, mPlayingField, playerState);
    }

    public void haltPlayer() {
        vlTextToSpeech.stop();
        updatePlayerInfo(mItemIndex, mSentenceIndex, mPlayingField, HALT_BY_SCROLLING);
    }

    public void startTimer() {
        int playTime = mOptionSettings.getPlayTime() * 1000 * 60;
        mHandler.postDelayed(mTimerRunnable, playTime);
        isTimeOut = false;
    }

    public void resetTimer() {
        Log.d(TAG, "reset timer");
        mPreferences.setPlayTime(mOptionSettings.getPlayTime());
        mHandler.removeCallbacks(mTimerRunnable);
        startTimer();
    }

    public void resetSpellCountDown() {
        spellCountDown = 3;
    }

    public void resetItemLoop() {
        int itemLoop = mOptionSettings.getItemLoop();
        mPreferences.setItemLoop(itemLoop);
        itemLoopCountDown = itemLoop;
    }

    public void resetListLoop() {
        int listLoop = mOptionSettings.getListLoop();
        mPreferences.setListLoop(listLoop);
        listLoopCountDown = listLoop;
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
                        mVocabularies = loadNewContent();
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
                        mVocabularies = loadNewContent();
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
                vlTextToSpeech.pause();
                mPlayerState = playerState; /* the player is halted by focus loss */
            }
        }
    }

    private int isItemFinishing(int itemIndex, int sentenceIndex, String playingField, int stopPeriod) {
        int lengthOfPause = 400;
        boolean isSentence = mOptionSettings.isSentence();
        if (isSentence && playingField.equals(CnSENTENCE) && isLastSentence(itemIndex, sentenceIndex)
                || !isSentence && playingField.equals(TRANSLATION))
            lengthOfPause = 400 + stopPeriod * 1000;
        return lengthOfPause;
    }

    private void updatePlayerInfo(int itemIndex, int sentenceIndex, String playingField, String playerState) {
        if (!playerState.equals(mPlayerState))
            mBroadcastTrigger.onPlayerStateChanged(playerState);

        if (mPreferences != null) {
            mPreferences.setItemIndex(itemIndex);
            mPreferences.setSentenceIndex(sentenceIndex);
            mPreferences.setPlayerState(playerState);
            mPreferences.setPlayingField(playingField);
        }

        mItemIndex = itemIndex;
        mSentenceIndex = sentenceIndex;
        mPlayingField = playingField;
        mPlayerState = playerState;
    }

    private int pickNextItem(int oldItemIndex) {
        int next;
        int itemAmount = mVocabularies.size();
        if (mOptionSettings.isRandom()) {
            Random random = new Random(System.currentTimeMillis());
            do {
                next = random.nextInt(itemAmount);
            } while (oldItemIndex == next);
        } else {
            next = (isLastItem(oldItemIndex) ? -1 : oldItemIndex+1);
        }
        return next;
    }

    private ArrayList<Vocabulary> loadNewContent() {
        int bookIndex = mPreferences.getBookIndex();
        int numOfLesson = mDatabase.getNumOfLesson(bookIndex);
        int oldLessonIndex = mPreferences.getLessonIndex();
        int newLessonIndex = (oldLessonIndex+1) % numOfLesson;
        ArrayList<Vocabulary> vocabularies = mDatabase.getVocabulariesByIDs(mDatabase.getContentIDs(bookIndex, newLessonIndex));
        mPreferences.setCurrentContent(vocabularies);
        mPreferences.setBookIndex(bookIndex);
        mPreferences.setLessonIndex(newLessonIndex);
        return vocabularies;
    }

    private boolean isLastItem(int itemIndex) {
        int itemAmount = mVocabularies.size();
        return (itemIndex == itemAmount-1);
    }

    private boolean isLastSentence(int itemIndex, int sentenceIndex) {
        int sentenceAmount = mVocabularies.get(itemIndex).getSentenceAmount();
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
