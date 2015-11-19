package com.wishcan.www.vocabulazy.player;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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

    public static final String ACTION_REMOVE_PENDING_TASK = "removePendingTask";

    public static final String ACTION_POST_STOP_SERVICE = "postStopService";

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

    private Handler mHandler;

    private Runnable mStopServiceRunnable;

    public AudioService() {
        super("AudioService");
        Log.d(TAG, "constructor");
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();

        mMainActivity = MainActivity.mMainActivity;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Notification notification = new Notification.Builder(mMainActivity)
                    .setContentTitle("ContentTitle")
                    .setContentText("ContentText")
                    .setSmallIcon(R.drawable.launcher_icon)
                    .setVisibility(Notification.VISIBILITY_SECRET)
                    .build();
            startForeground(1, notification);
        } else {
            Notification notification = new Notification.Builder(mMainActivity)
                    .setContentInfo("ContentInfo")
                    .setContentTitle("ContentTitle")
                    .setContentText("ContentText")
                    .build();
            startForeground(1, notification);
        }

        if (mDatabase == null) {
            mDatabase = mMainActivity.getDatabase();
        }

        if (mHandler == null) {
            Log.d(TAG, "handler null");
            mHandler = new Handler();
        }

        if (mStopServiceRunnable == null) {
            Log.d(TAG, "runnable null");
            mStopServiceRunnable = new Runnable() {
                @Override
                public void run() {
                    stopSelf();
                }
            };
        }

    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        switch (intent.getAction()) {

            case ACTION_INIT:
                Log.d(TAG, ACTION_INIT);
                initAudioPlayer();
                if (mStopServiceRunnable != null) {
                    mHandler.removeCallbacks(mStopServiceRunnable);
                }
                break;

            case ACTION_SET_CONTENT:
                Log.d(TAG, ACTION_SET_CONTENT);
                Bundle contentBundle = intent.getBundleExtra("contentBundle");
                ArrayList<Integer> content = contentBundle.getIntegerArrayList("mCurrentContentInPlayer");
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

            case ACTION_REMOVE_PENDING_TASK:
                mAudioPlayer.removePendingTask();
                break;

            case ACTION_POST_STOP_SERVICE:
                Log.d(TAG, "stop service");
                if (!isPlaying())
                    mHandler.postDelayed(mStopServiceRunnable, 30 * 60 * 1000); // 30 minutes
                break;

            default:
                break;
        }

        return START_STICKY;
    }

    private void initAudioPlayer() {
        mAudioPlayer = new AudioPlayer(mMainActivity);
        mAudioPlayer.initPlayerLists();
    }

    public void updateOptions(ArrayList<Option> options, int currentMode) {
        mAudioPlayer.updateOptions(options, currentMode);
    }

    public void setContentToPlayer(ArrayList<Integer> content) {
        mAudioPlayer.reset();
        mAudioPlayer.initPlayerLists();
        mAudioPlayer.setOnPlayerStatusChangedListener(new AudioPlayer.OnPlayerStatusChangedListener() {
            @Override
            public void onItemStartPlaying(int itemIndex) {
                Intent intent = new Intent(BROADCAST);
                intent.putExtra(BROADCAST_ACTION, BROADCAST_ACTION_ITEM_START);
                intent.putExtra("itemIndex", itemIndex);
                LocalBroadcastManager.getInstance(mMainActivity).sendBroadcast(intent);

                mDatabase.setCurrentPlayingItem(itemIndex);
            }

            @Override
            public void onSentenceStart(int sentence) {
                Intent intent = new Intent(BROADCAST);
                intent.putExtra(BROADCAST_ACTION, BROADCAST_ACTION_SENTENCE_START);
                intent.putExtra("sentenceIndex", sentence);
                LocalBroadcastManager.getInstance(mMainActivity).sendBroadcast(intent);
            }

            @Override
            public void onSentenceComplete() {
                Intent intent = new Intent(BROADCAST);
                intent.putExtra(BROADCAST_ACTION, BROADCAST_ACTION_SENTENCE_COMPLETE);
                LocalBroadcastManager.getInstance(mMainActivity).sendBroadcast(intent);
            }

            @Override
            public void onItemResumePlaying() {

                Intent intent = new Intent(BROADCAST);
                intent.putExtra(BROADCAST_ACTION, BROADCAST_ACTION_ITEM_RESUME);
                LocalBroadcastManager.getInstance(mMainActivity).sendBroadcast(intent);
            }

            @Override
            public void onItemStopPlaying() {

                Intent intent = new Intent(BROADCAST);
                intent.putExtra(BROADCAST_ACTION, BROADCAST_ACTION_ITEM_STOP);
                LocalBroadcastManager.getInstance(mMainActivity).sendBroadcast(intent);
            }
        });
        mAudioPlayer.setPlaylistContent(content);
        mAudioPlayer.setOnPlayerCompletionListener(new AudioPlayer.OnPlayerCompletionListener() {
            @Override
            public void onItemComplete() {

                Intent intent = new Intent(BROADCAST);
                intent.putExtra(BROADCAST_ACTION, BROADCAST_ACTION_ITEM_COMPLETE);
                LocalBroadcastManager.getInstance(mMainActivity).sendBroadcast(intent);
            }

            @Override
            public void onListComplete() {

                Log.d(TAG, "onListComplete");

                Intent intent = new Intent(BROADCAST);
                intent.putExtra(BROADCAST_ACTION, BROADCAST_ACTION_LIST_COMPLETE);
                LocalBroadcastManager.getInstance(mMainActivity).sendBroadcast(intent);
            }
        });

    }

    public void startPlayingItemAt(int index) {
        mAudioPlayer.startPlayingItemAt(index);
    }

    public boolean isPlaying() {
        return mAudioPlayer.isPlaying();
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
