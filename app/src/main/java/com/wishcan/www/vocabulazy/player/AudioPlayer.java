package com.wishcan.www.vocabulazy.player;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
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
    private ArrayList<String> mAudios;
    private ArrayList<String> mSentence_Audios;

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

    private boolean mIsRandom;
    private boolean mIsEnSentenceEnabled;
    private boolean mIsCnSentenceEnabled;

    private ArrayList<Option> mOptions;

    public AudioPlayer(Context context) {

        Log.d(TAG, "AudioPlayer");

        mAssetManager = context.getAssets();
        mDatabase = ((MainActivity) context).getDatabase();

        mOnPlayerCompletionListener = null;
        mOnPlayerStatusChangedListener = null;

        mNumOfNotes = mDatabase.getNumOfNotes();

        mPlayer = new MediaPlayer();
        mNowPlaying = AudioCategory.IDLE;
        mIsPlaying = false;

        mOptions = mDatabase.getOptions();

        setOptions(mOptions.get(0));

        //Configurable
//        mIsRandom = false;
//        mIsEnSentenceEnabled = false;
//        mIsCnSentenceEnabled = false;
//        mListLoop = 1;
//        mItemLoop = 1;

        mListLoopCount = mListLoop;
        mItemLoopCount = mItemLoop;

    }

    public void initPlayerLists() {
        if (mPlaylistContentIDs != null) {
            mPlaylistContentIDs.clear();
        } else {
            mPlaylistContentIDs = new ArrayList<>();
        }

        mAudios = new ArrayList<>();
        mSentence_Audios = new ArrayList<>();
    }

    public void setPlaylistContent(ArrayList<Integer> content) {
        mPlaylistContentIDs = content;
        mNumOfVocabulariesInList = content.size();

        Log.d(TAG, "" + mNumOfVocabulariesInList);

        ArrayList<ArrayList<String>> audionames = mDatabase.getAudioNames(mPlaylistContentIDs);
        mAudios = audionames.get(0);
        mSentence_Audios = audionames.get(1);
    }

    public ArrayList<Integer> getPlaylistContentIDs() {
        return mPlaylistContentIDs;
    }

    public void updateOptions(ArrayList<Option> options, int currentMode) {
        mOptions = options;
        setOptions(options.get(currentMode));
    }

    private void setOptions(Option option) {
        mIsRandom = option.isIsRandom();
        mIsEnSentenceEnabled = option.isSentence();
        mListLoop = option.getListLoop();
        mItemLoop = option.getItemLoop();

        mItemLoopCount = mItemLoop;
        mListLoopCount = mListLoop;

//        Log.d(TAG, "random: " + mIsRandom);
//        Log.d(TAG, "listloop: " + mListLoop);
//        Log.d(TAG, "sentence: " + mIsCnSentenceEnabled);
//        Log.d(TAG, "itemloop: " + mItemLoop);
    }

    public void startPlayingItemAt(int index) {
        setCurrentPlayingIndex(index);
        String audio = mAudios.get(mCurrentPlayingIndex);
        setDataSourceAndPlay(audio);
        mNowPlaying = AudioCategory.SPELL;
    }

    private void startPlayingSentence() {
        mOnPlayerStatusChangedListener.onSentenceStart();
        String sentence_audio = mSentence_Audios.get(mCurrentPlayingIndex);
        setDataSourceAndPlay(sentence_audio);
        mNowPlaying = AudioCategory.EN_SENTENCE;
    }

    private void setCurrentPlayingIndex(int index) {
        mCurrentPlayingIndex = index;
    }

    private void setDataSourceAndPlay(String audio) {

        mPlayer.reset();

        try {
            AssetFileDescriptor mAFD = mAssetManager.openFd("audio/" + audio);
            FileDescriptor mFD = mAFD.getFileDescriptor();
            long mOffset = mAFD.getStartOffset();
            long mLength = mAFD.getLength();

            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setDataSource(mFD, mOffset, mLength);
            mPlayer.prepareAsync();
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.d(TAG, "start");
                    mp.start();
                    mIsPlaying = true;
                    mOnPlayerStatusChangedListener.onItemStartPlaying(mCurrentPlayingIndex);
                }
            });
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    playerCompleted();

                }
            });
        } catch (IOException ioe) {
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
                en_sentenceCompleted();
                break;
            case CN_SENTENCE:
                break;
            default:
                break;
        }
    }

    private void spellCompleted() {
        if (mIsEnSentenceEnabled) {
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
            mOnPlayerCompletionListener.onItemComplete();
            lookForNextItem();
        } else {
            startPlayingItemAt(mCurrentPlayingIndex);
        }
    }

    private void lookForNextItem() {
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
        Log.d(TAG, "mListLoopCoount: " + mListLoopCount);
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

    public void resume() {
        Log.d(TAG, "resume");
        if (mPlayer != null) {
            mIsPlaying = true;
            mPlayer.start();
            mOnPlayerStatusChangedListener.onItemResumePlaying();
        }
    }

    public void pause() {
        Log.d(TAG, "pause");
        if (mPlayer != null) {
            mIsPlaying = false;
            mPlayer.pause();
            mOnPlayerStatusChangedListener.onItemStopPlaying();
        }
    }


    public void stop() {
        Log.d(TAG, "stop");
        if (mPlayer != null) {
            mIsPlaying = false;
            mPlayer.pause();
            mPlayer.seekTo(0);
            mOnPlayerStatusChangedListener.onItemStopPlaying();
        }
    }

    public void reset() {
        Log.d(TAG, "reset");
        mIsPlaying = false;
        if (mPlayer != null) mPlayer.reset();
    }

    public void release() {
        Log.d(TAG, "release");
        if (mPlayer != null) mPlayer.release();
    }

    public int getCurrentListID() {
        return mCurrentListID;
    }

    public void setCurrentNoteID(int noteID) {
        mCurrentListID = noteID;
    }

    public void setItemLoop(int itemLoop) {
        mItemLoop = itemLoop;
    }

    public void setListLoop(int listLoop) {
        mListLoop = listLoop;
    }

//    public void setIsCnSentenceEnabled(boolean mIsCnSentenceEnabled) {
//        this.mIsCnSentenceEnabled = mIsCnSentenceEnabled;
//    }

    public void setIsEnSentenceEnabled(boolean isEnSentenceEnabled) {
        mIsEnSentenceEnabled = isEnSentenceEnabled;
    }

    public void setIsRandom(boolean isRandom) {
        mIsRandom = isRandom;
    }

    public boolean isPlaying() {
        return mIsPlaying;
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

        void onSentenceStart();

        void onSentenceComplete();

        void onItemResumePlaying();

        void onItemStopPlaying();
    }

    public void setDefaultList() {

        for (int index = 0; index < 9; index++) {
            mPlaylistContentIDs.add(index);
        }

        mAudios.add("a means of.mp3");
        mAudios.add("African.mp3");
        mAudios.add("air-traffic control.mp3");
        mAudios.add("Arabic.mp3");
        mAudios.add("billion.mp3");
        mAudios.add("communication.mp3");
        mAudios.add("connect.mp3");
        mAudios.add("David Crystal.mp3");
        mAudios.add("embrace.mp3");

        mSentence_Audios.add("a means of-1.mp3");
        mSentence_Audios.add("African-1.mp3");
        mSentence_Audios.add("air-traffic control-1.mp3");
        mSentence_Audios.add("Arabic-1.mp3");
        mSentence_Audios.add("billion-1.mp3");
        mSentence_Audios.add("communication-1.mp3");
        mSentence_Audios.add("connect-1.mp3");
        mSentence_Audios.add("David Crystal-1.mp3");
        mSentence_Audios.add("embrace-1.mp3");
    }
}
