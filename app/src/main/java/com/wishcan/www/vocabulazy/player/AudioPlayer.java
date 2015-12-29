package com.wishcan.www.vocabulazy.player;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;

import org.json.JSONException;

import com.wishcan.www.vocabulazy.MainActivity;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.Option;
import com.wishcan.www.vocabulazy.storage.Vocabulary;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by allencheng07 on 2015/8/16.
 */
public class AudioPlayer {

    enum AudioCategory {
        IDLE,
        SPELL,
        TRANSLATE,
        EN_SENTENCE,
        CN_SENTENCE
    }

    public static final String TAG = AudioPlayer.class.getSimpleName();

    private AssetManager mAssetManager;
    private Database mDatabase;

    private ArrayList<Integer> mPlaylistContentIDs;
    private ArrayList<String> mSpellAudios;
    private ArrayList<ArrayList<String>> mSentence_Audios;

    private MediaPlayer mPlayer;
    private OnPlayerCompletionListener mOnPlayerCompletionListener;
    private OnPlayerStatusChangedListener mOnPlayerStatusChangedListener;
    private AudioCategory mNowPlaying;

    private int mNumOfVocabulariesInList;
    private int mNumOfNotes;
    private int mCurrentListID;
    private int mCurrentPlayingIndex;

    private int mListLoop;
    private int mItemLoop;
    private int mListLoopCount;
    private int mItemLoopCount;

    private boolean mIsPlaying;
    private boolean stopped;

    private boolean mIsRandom;
    private boolean mIsEnSentenceEnabled;
    private boolean mIsCnSentenceEnabled;

    private int mStopPeriod;
    private int mStopPlaying;

    private ArrayList<Option> mOptions;

    private Handler mHandler;
    private Runnable mStopPeriodRunnable;
    private Runnable mStopPlayingRunnable;

    private int mNumOfSentence;
    private int mSentenceIndex;

    String mCurrentSpellAudio;
    ArrayList<String> mCurrentSentenceAudio;

    private boolean isItemCompleted;

    public AudioPlayer(Context context) {

        Log.d(TAG, "constructor");

        mAssetManager = context.getAssets();
        mDatabase = ((MainActivity) context).getDatabase();

        mOnPlayerCompletionListener = null;
        mOnPlayerStatusChangedListener = null;

        mNumOfNotes = mDatabase.getNumOfNotes();

        initMediaPlayer();

        mNowPlaying = AudioCategory.IDLE;
        mIsPlaying = false;
        stopped = true;

        mOptions = mDatabase.getOptions();

        setOptions(mOptions.get(0));

        mListLoopCount = mListLoop;
        mItemLoopCount = mItemLoop;

        mHandler = new Handler();

        mNumOfSentence = 0;
        mSentenceIndex = -1;

        isItemCompleted = true;
    }

    private void initMediaPlayer() {
        mPlayer = new MediaPlayer();
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
//                    Log.d(TAG, "start");
                mPlayer.start();
                mIsPlaying = true;
                if (stopped) {
                    startStopPlayingRunnable();
                }
                stopped = false;
                isItemCompleted = false;
                mOnPlayerStatusChangedListener.onItemStartPlaying(mCurrentPlayingIndex);
            }
        });
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playerCompleted();
            }
        });
        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.d(TAG, "onError");

                return true;
            }
        });
    }

    public void initPlayerLists() {

        Log.d(TAG, "init");

        if (mPlaylistContentIDs != null) {
            mPlaylistContentIDs.clear();
        } else {
            mPlaylistContentIDs = new ArrayList<>();
        }

        if (mSpellAudios == null) {
            mSpellAudios = new ArrayList<>();
        }

        if (mSentence_Audios == null) {
            mSentence_Audios = new ArrayList<>();
        }
    }

    public void setPlaylistContent(ArrayList<Integer> content) {
        mPlaylistContentIDs = content;
        mNumOfVocabulariesInList = content.size();

        Log.d(TAG, "" + mNumOfVocabulariesInList);

        mSpellAudios = mDatabase.getSpellAudios(mPlaylistContentIDs);
        mSentence_Audios = mDatabase.getSentenceAudios(mPlaylistContentIDs);
    }

    public ArrayList<Integer> getPlaylistContentIDs() {
        return mPlaylistContentIDs;
    }

    public void updateOptions(ArrayList<Option> options, int currentMode) {
        mOptions = options;
        setOptions(options.get(currentMode));
        startStopPlayingRunnable();
    }

    private void setOptions(Option option) {
        mIsRandom = option.isIsRandom();
        mIsEnSentenceEnabled = option.isSentence();
        mListLoop = option.getListLoop();
        mItemLoop = option.getItemLoop();

        mStopPeriod = option.getStopPeriod();
        mStopPlaying = option.mPlayTime;


        mItemLoopCount = mItemLoop;
        mListLoopCount = mListLoop;

    }

    public void startPlayingItemAt(int index) {
        setCurrentPlayingIndex(index);
        mCurrentSpellAudio = mSpellAudios.get(mCurrentPlayingIndex);
        mCurrentSentenceAudio = mSentence_Audios.get(mCurrentPlayingIndex);
        mNumOfSentence = mCurrentSentenceAudio.size();
        mSentenceIndex = 0;
        startPlayingSpell();
    }

    private void startPlayingSpell() {
        setDataSourceAndPlay(mCurrentSpellAudio);
        mNowPlaying = AudioCategory.SPELL;
    }

    private void startPlayingSentence() {
        mOnPlayerStatusChangedListener.onSentenceStart(mSentenceIndex);
//        Log.d(TAG, "sentence: " + mCurrentSentenceAudio.get(mSentenceIndex));
        setDataSourceAndPlay(mCurrentSentenceAudio.get(mSentenceIndex));
        mNowPlaying = AudioCategory.EN_SENTENCE;
    }

    private void setCurrentPlayingIndex(int index) {
        mCurrentPlayingIndex = index;
    }

    private void setDataSourceAndPlay(String audio) {

        mPlayer.reset();

        try {
//            Log.d(TAG, "audio: " + audio);
            AssetFileDescriptor mAFD = mAssetManager.openFd("audio2/" + audio);
            FileDescriptor mFD = mAFD.getFileDescriptor();
            long mOffset = mAFD.getStartOffset();
            long mLength = mAFD.getLength();

            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setDataSource(mFD, mOffset, mLength);
            mPlayer.prepareAsync();

        } catch (IOException ioe) {
            ioe.printStackTrace();
            if (ioe instanceof FileNotFoundException && mNowPlaying == AudioCategory.EN_SENTENCE) {
                en_sentenceCompleted();
            }
        }

    }

    private void playerCompleted() {
        switch (mNowPlaying) {
            case IDLE:
                break;
            case SPELL:
                spellCompleted();
                break;
            case TRANSLATE:
                break;
            case EN_SENTENCE:

                mSentenceIndex++;

                if (mSentenceIndex == mNumOfSentence)
                    en_sentenceCompleted();
                else
                    startPlayingSentence();

                break;
            case CN_SENTENCE:
                break;
            default:
                break;
        }
    }

    private void spellCompleted() {
        if (mIsEnSentenceEnabled) {
            mSentenceIndex = 0;
            startPlayingSentence();
        } else {
            itemCompleted();
        }
    }

    private void en_sentenceCompleted() {
        mOnPlayerStatusChangedListener.onSentenceComplete();
        itemCompleted();
    }

    private void itemCompleted() {
        mItemLoopCount--;
        if (mItemLoopCount == 0) {
            mItemLoopCount = mItemLoop;
            isItemCompleted = true;
            mOnPlayerCompletionListener.onItemComplete();

            mStopPeriodRunnable = new Runnable() {
                @Override
                public void run() {
                    lookForNextItem();
                }
            };

            mHandler.postDelayed(mStopPeriodRunnable, mStopPeriod * 1000);

        } else {
            startPlayingItemAt(mCurrentPlayingIndex);
        }
    }

    private void lookForNextItem() {
        mStopPeriodRunnable = null;
        int indexOfNextItem;
        if (mIsRandom) {
            indexOfNextItem = randomNextIndex(mNumOfVocabulariesInList, mCurrentPlayingIndex);
            startPlayingItemAt(indexOfNextItem);
        } else {
            indexOfNextItem = mCurrentPlayingIndex + 1;
            if (indexOfNextItem < mNumOfVocabulariesInList) {
                startPlayingItemAt(indexOfNextItem);
            } else {
                listCompleted();
            }
        }
    }

    private void listCompleted() {
        mListLoopCount--;
        if (mListLoopCount == 0) {
            mListLoopCount = mListLoop;
            mOnPlayerCompletionListener.onListComplete();
        } else {
            startPlayingItemAt(0);
        }
    }

    private int randomNextIndex(int size, int currentIndex) {
        Random random = new Random(System.currentTimeMillis());
        int next;
        do {
            next = random.nextInt(size);
        } while (currentIndex == next);
        return next;
    }

    public void removePendingTask() {
        if (mStopPeriodRunnable != null)
            mHandler.removeCallbacks(mStopPeriodRunnable);
    }

    public void resume() {
        if (mPlayer != null && !isItemCompleted) {
            mIsPlaying = true;
            mPlayer.start();
            mOnPlayerStatusChangedListener.onItemResumePlaying();
        } else if (mPlayer != null && isItemCompleted) {
            lookForNextItem();
        }
    }

    public void pause() {
        if (mPlayer != null) {
            mIsPlaying = false;
            mPlayer.pause();
            removePendingTask();
            mOnPlayerStatusChangedListener.onItemStopPlaying();
        }
    }

    public void stop() {
        if (mPlayer != null) {
            mIsPlaying = false;
            mPlayer.pause();
            mPlayer.seekTo(0);
            mOnPlayerStatusChangedListener.onItemStopPlaying();
            removePendingTask();
            stopped = true;
        }
    }

    public void reset() {
        if (mPlayer != null) mPlayer.reset();
    }

    public void release() {
        if (mPlayer != null) mPlayer.release();
    }

    public int getCurrentListID() {
        return mCurrentListID;
    }

    public void setCurrentNoteID(int noteID) {
        mCurrentListID = noteID;
    }

    public boolean isPlaying() {
        return mIsPlaying;
    }

    /**
     * Every time the PLAY started by users should triggered this function, which
     * set up the time to stop playing.
     */
    public void startStopPlayingRunnable() {
        mHandler.removeCallbacks(mStopPlayingRunnable);
        mStopPlayingRunnable = new Runnable() {
            @Override
            public void run() {
                stop();
            }
        };
        mHandler.postDelayed(mStopPlayingRunnable, mStopPlaying * 60 * 1000);
    }

    public void setOnPlayerCompletionListener(OnPlayerCompletionListener listener) {
        mOnPlayerCompletionListener = listener;
    }

    public void setOnPlayerStatusChangedListener(OnPlayerStatusChangedListener listener) {
        mOnPlayerStatusChangedListener = listener;
    }

    public interface OnPlayerCompletionListener {

        void onItemComplete();

        void onListComplete();
    }

    public interface OnPlayerStatusChangedListener {

        void onItemStartPlaying(int itemIndex);

        void onSentenceStart(int sentenceIndex);

        void onSentenceComplete();

        void onItemResumePlaying();

        void onItemStopPlaying();
    }

    public void setDefaultList() {

        for (int index = 0; index < 9; index++) {
            mPlaylistContentIDs.add(index);
        }

//        mAudios.add("a means of.mp3");
//        mAudios.add("African.mp3");
//        mAudios.add("air-traffic control.mp3");
//        mAudios.add("Arabic.mp3");
//        mAudios.add("billion.mp3");
//        mAudios.add("communication.mp3");
//        mAudios.add("connect.mp3");
//        mAudios.add("David Crystal.mp3");
//        mAudios.add("embrace.mp3");

//        mSentence_Audios.add("a means of-1.mp3");
//        mSentence_Audios.add("African-1.mp3");
//        mSentence_Audios.add("air-traffic control-1.mp3");
//        mSentence_Audios.add("Arabic-1.mp3");
//        mSentence_Audios.add("billion-1.mp3");
//        mSentence_Audios.add("communication-1.mp3");
//        mSentence_Audios.add("connect-1.mp3");
//        mSentence_Audios.add("David Crystal-1.mp3");
//        mSentence_Audios.add("embrace-1.mp3");
    }
}
