package com.wishcan.www.vocabulazy;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.playlog.internal.LogEvent;
import com.wishcan.www.vocabulazy.log.LogHelper;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.Preferences;

//import io.uxtesting.UXTeting;

/**
 * This class is created for giving a global overview to our application.
 */
public class VLApplication extends Application {

    /**
     * The unique key used by UXTesting api.
     */
    private static final String UXTESTING_APP_KEY = "JzskKAJgBGPlmP-NIBxBug";

    /**
     * The preferences which store all arguments and parameters.
     */
    private Preferences wPreferences;

    /**
     * The Database object used in the entire application.
     */
    private Database wDatabase;

    /**
     * The LogHelper is to help application logging messages and errors, also helps send Google
     * Analytics event messages through Tracker.
     */
    private LogHelper wLogHelper;

    @Override
    public void onCreate() {
        super.onCreate();

        /* Every-time the application is created, create a Preferences object and
            it will be shared among activities and fragments of this particular application. */
        wPreferences = new Preferences();

        /**
         * Every-time entering the application, create a Database object, and throughout entire
         * application, this is the only Database object to be accessed.
         */
        wDatabase = new Database(this);

//        /**
//         * call this method to enable UXTesting.
//         */
//        UXTesting.Init(this, UXTESTING_APP_KEY);

        /**
         * initialize the LogHelper object.
         */
        wLogHelper = new LogHelper(getApplicationContext());
    }

    /**
     * Call getDefaultTracker to retrieve the tracker used by Google Analytics api.
     * @return wTracker
     */
    synchronized public Tracker getDefaultTracker() {
        return wLogHelper.getDefaultTracker(getApplicationContext());
    }

    /**
     * Call getPreferences to retrieve the Preferences obejct of this application.
     * @return wPreferences
     */
    synchronized public Preferences getPreferences() {
        return wPreferences;
    }

    /**
     * Call getDatabase to retrieve Database object.
     * @return wDatabase
     */
    synchronized public Database getDatabase() {
        return wDatabase;
    }

    public void loadDatabase() {
        wDatabase.loadFiles();
        wDatabase.initSettings();
    }

    synchronized public LogHelper getLogHelper() {
        return wLogHelper;
    }
}
