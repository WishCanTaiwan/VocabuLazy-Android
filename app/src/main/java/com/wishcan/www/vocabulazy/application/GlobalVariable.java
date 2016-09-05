package com.wishcan.www.vocabulazy.application;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.analytics.Tracker;
import com.wishcan.www.vocabulazy.storage.databaseObjects.OptionSettings;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Vocabulary;
import com.wishcan.www.vocabulazy.utility.log.Logger;
import com.wishcan.www.vocabulazy.storage.Database;
//import com.wishcan.www.vocabulazy.storage.Preferences;

import java.util.ArrayList;

/**
 * This class is created for giving a global overview to our application.
 */
public class GlobalVariable extends Application {

    public static final String TAG = "GlobalVariable";

    /**
     * The Logger is to help application logging messages and errors, also helps send Google
     * Analytics event messages through Tracker.
     */
    private Logger wLogger;

    public static final String PLAYER_BROADCAST_INTENT = "player-broadcast-intent";
    public static final String PLAYER_BROADCAST_ACTION = "player-broadcast-action";

    public int playerTextbookIndex;
    public int playerLessonIndex;
    public int playerNoteIndex;

    public ArrayList<Vocabulary> playerContent;
    public ArrayList<OptionSettings> optionSettings;
    public int optionMode;
    public int playerItemIndex;
    public int playerSentenceIndex;
    public String playerState;
    public String playingField;

    public int playerItemLoop;
    public int playerListLoop;
    public int playerPlayTime;

    @Override
    public void onCreate() {
        Log.d(TAG, "Create");
        super.onCreate();

        // initialize the Logger object.
        wLogger = new Logger(getApplicationContext());
    }

    /**
     * Call getDefaultTracker to retrieve the tracker used by Google Analytics api.
     * @return wTracker
     */
    synchronized public Tracker getDefaultTracker() {
        return wLogger.getDefaultTracker(getApplicationContext());
    }

    synchronized public Logger getLogHelper() {
        return wLogger;
    }
}
