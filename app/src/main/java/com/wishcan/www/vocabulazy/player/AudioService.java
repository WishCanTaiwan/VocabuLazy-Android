package com.wishcan.www.vocabulazy.player;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;

import com.wishcan.www.vocabulazy.MainActivity;
import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.Option;

/**
 * Created by allencheng07 on 2015/9/22.
 */
public class AudioService extends IntentService {

    private static final String TAG = AudioService.class.getSimpleName();

    public static final String ACTION_INIT = "init";

    public static final String ACTION_SET_CONTENT = "setContent";

    public static final String ACTION_START = "start";

    public static final String ACTION_RESUME = "resume";

    public static final String ACTION_PAUSE = "pause";

    public static final String ACTION_STOP = "stop";

    public static final String ACTION_RESET = "reset";

    public static final String ACTION_PLAYPAUSE = "play_pause";

    public static final String ACTION_UPDATE_OPTION = "updateOption";

    public static final String ACTION_GET_CURRENT_PLAYER_INFO = "getCurrentPlayerInfo";

    public static final String BROADCAST = "broadcast";

    public static final String BROADCAST_ACTION = "broadcastAction";

    public static final String BROADCAST_ACTION_ITEM_START = "itemStart";

    public static final String BROADCAST_ACTION_SENTENCE_START = "sentenceStart";

    public static final String BROADCAST_ACTION_SENTENCE_COMPLETE = "sentenceComplete";

    public static final String BROADCAST_ACTION_ITEM_RESUME = "itemResume";

    public static final String BROADCAST_ACTION_ITEM_STOP = "itemStop";

    public static final String BROADCAST_ACTION_ITEM_COMPLETE = "itemComplete";

    public static final String BROADCAST_ACTION_LIST_COMPLETE = "listComplete";

    public static final String BROADCAST_ACTION_SET_PLAYPAUSE_ICON = "setPlayPauseIcon";

    public static final String BROADCAST_ACTION_CURRENT_PLAYER_INFO = "returnCurrentPlayerInfo";

    private MainActivity mMainActivity;

    private AudioPlayer mAudioPlayer;

    private Database mDatabase;

    private int mCurrentBook;

    private int mCurrentList;

    private int mCurrentItem;

    public AudioService() {
        super("AudioService");
        Log.d(TAG, "constructor");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

//        PendingIntent pausePendingIntent

        mMainActivity = MainActivity.mMainActivity;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Notification notification = new Notification.Builder(mMainActivity)
                    .setVisibility(Notification.VISIBILITY_PRIVATE)
                    .build();
            startForeground(1, notification);
        } else {
            Notification notification = new Notification.Builder(mMainActivity)
                    .build();
            startForeground(1, notification);
        }

        if (mDatabase == null) {
            mDatabase = mMainActivity.getDatabase();
        }

        initAudioPlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        switch (intent.getAction()) {
            case ACTION_INIT:
                Log.d(TAG, ACTION_INIT);
                initAudioPlayer();
                break;
            case ACTION_SET_CONTENT:
                Log.d(TAG, ACTION_SET_CONTENT);

                Bundle contentBundle = intent.getBundleExtra("contentBundle");
                ArrayList<Integer> content = contentBundle.getIntegerArrayList("mCurrentContentInPlayer");
                int currentLessonID = intent.getIntExtra("mCurrentList", -1);
                int currentBook = intent.getIntExtra("mCurrentBook", -1);
                setContentToPlayer(content);
                break;
            case ACTION_START:
                Log.d(TAG, ACTION_START);
                int position = intent.getIntExtra("position", 0);
                startPlayingItemAt(position);
                break;
            case ACTION_RESUME:
                break;
            case ACTION_PAUSE:
                Log.d(TAG, ACTION_PAUSE);
                if (isPlaying()) {
                    stop();
                }
                break;
            case ACTION_RESET:
                break;
            case ACTION_STOP:
                Log.d(TAG, ACTION_STOP);
                if (isPlaying()) {
                    stop();
                }
                break;
            case ACTION_PLAYPAUSE:
                play_pause();

                break;
            case ACTION_UPDATE_OPTION:
                ArrayList<Option> options = mDatabase.getOptions();
                int currentMode = mDatabase.getCurrentOptionMode();
                updateOptions(options, currentMode);
                break;
            case ACTION_GET_CURRENT_PLAYER_INFO:

                // broadcast back to mainactivity

                break;
            default:
                break;
        }

        return START_STICKY;
    }

    private void initAudioPlayer() {
//        Log.d(TAG, "initAudioPlayer");
        mAudioPlayer = new AudioPlayer(mMainActivity);
        mAudioPlayer.initPlayerLists();
    }

    public void updateOptions(ArrayList<Option> options, int currentMode) {
        mAudioPlayer.updateOptions(options, currentMode);
    }

    public void sendPlayerInfo() {
        Intent intent = new Intent(BROADCAST);
        intent.putExtra(BROADCAST_ACTION, BROADCAST_ACTION_CURRENT_PLAYER_INFO);
        intent.putExtra("mCurrentListID", mAudioPlayer.getCurrentListID());
        LocalBroadcastManager.getInstance(mMainActivity).sendBroadcast(intent);
    }

    public void setContentToPlayer(ArrayList<Integer> content) {
        mAudioPlayer.reset();
        mAudioPlayer.initPlayerLists();
        mAudioPlayer.setOnPlayerStatusChangedListener(new AudioPlayer.OnPlayerStatusChangedListener() {
            @Override
            public void onItemStartPlaying(int itemIndex) {

//                Log.d(TAG, "onItemStartPlaying");

                Intent intent = new Intent(BROADCAST);
                intent.putExtra(BROADCAST_ACTION, BROADCAST_ACTION_ITEM_START);
                intent.putExtra("itemIndex", itemIndex);
                LocalBroadcastManager.getInstance(mMainActivity).sendBroadcast(intent);

                mDatabase.setCurrentPlayingItem(itemIndex);
//                mOnServiceStatusChangedListener.onItemStartPlaying(itemIndex);
            }

            @Override
            public void onSentenceStart() {

                Intent intent = new Intent(BROADCAST);
                intent.putExtra(BROADCAST_ACTION, BROADCAST_ACTION_SENTENCE_START);
                LocalBroadcastManager.getInstance(mMainActivity).sendBroadcast(intent);

//                mOnServiceStatusChangedListener.onSentenceStart();
            }

            @Override
            public void onSentenceComplete() {

                Intent intent = new Intent(BROADCAST);
                intent.putExtra(BROADCAST_ACTION, BROADCAST_ACTION_SENTENCE_COMPLETE);
                LocalBroadcastManager.getInstance(mMainActivity).sendBroadcast(intent);

//                mOnServiceStatusChangedListener.onSentenceComplete();
            }

            @Override
            public void onItemResumePlaying() {

                Intent intent = new Intent(BROADCAST);
                intent.putExtra(BROADCAST_ACTION, BROADCAST_ACTION_ITEM_RESUME);
                LocalBroadcastManager.getInstance(mMainActivity).sendBroadcast(intent);

//                mOnServiceStatusChangedListener.onItemResumePlaying();
            }

            @Override
            public void onItemStopPlaying() {

                Intent intent = new Intent(BROADCAST);
                intent.putExtra(BROADCAST_ACTION, BROADCAST_ACTION_ITEM_STOP);
                LocalBroadcastManager.getInstance(mMainActivity).sendBroadcast(intent);

//                mOnServiceStatusChangedListener.onItemStopPlaying();
            }
        });
        mAudioPlayer.setPlaylistContent(content);
//        mAudioPlayer.startPlayingItemAt(0);
//        mAudioPlayer.setCurrentNoteID(noteID);
        mAudioPlayer.setOnPlayerCompletionListener(new AudioPlayer.OnPlayerCompletionListener() {
            @Override
            public void onItemComplete() {

                Intent intent = new Intent(BROADCAST);
                intent.putExtra(BROADCAST_ACTION, BROADCAST_ACTION_ITEM_COMPLETE);
                LocalBroadcastManager.getInstance(mMainActivity).sendBroadcast(intent);

//                mOnServiceCompletionListener.onItemCompleted();
            }

            @Override
            public void onListComplete() {

                Log.d(TAG, "onListComplete");

                Intent intent = new Intent(BROADCAST);
                intent.putExtra(BROADCAST_ACTION, BROADCAST_ACTION_LIST_COMPLETE);
                LocalBroadcastManager.getInstance(mMainActivity).sendBroadcast(intent);

//                mOnServiceCompletionListener.onListCompleted();
            }
        });

    }

    public void startPlayingItemAt(int index) {
//        Log.d(TAG, "startPlayingItemAt: " + index);
        mAudioPlayer.startPlayingItemAt(index);
    }

    public AudioPlayer getAudioPlayer() {
        return mAudioPlayer;
    }

    public boolean isPlaying() {
        return mAudioPlayer.isPlaying();
    }

    public void reset() {
        mAudioPlayer.reset();
    }

    public void resume() {
        mAudioPlayer.resume();
    }

    public void pause() {
        mAudioPlayer.pause();
    }

    public void stop() {
        mAudioPlayer.stop();
    }

    public void play_pause() {
        if (isPlaying()) {
            pause();
        } else {
            resume();
        }

        Intent intent = new Intent(BROADCAST);
        intent.putExtra(BROADCAST_ACTION, BROADCAST_ACTION_SET_PLAYPAUSE_ICON);
        intent.putExtra("isPlaying", isPlaying());
        LocalBroadcastManager.getInstance(mMainActivity).sendBroadcast(intent);
    }

}
