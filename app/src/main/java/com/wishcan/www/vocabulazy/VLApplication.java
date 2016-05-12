package com.wishcan.www.vocabulazy;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
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
     * The Tracker object defined by Google Analytics.
     */
    private Tracker wTracker;

    /**
     * The preferences which store all arguments and parameters.
     */
    private Preferences wPreferences;

    /**
     * The Database object used in the entire application.
     */
    private Database wDatabase;

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
        wDatabase = new Database(getApplicationContext());

//        /**
//         * call this method to enable UXTesting.
//         */
//        UXTesting.Init(this, UXTESTING_APP_KEY);
    }

    /**
     * Call getDefaultTracker to retrieve the tracker used by Google Analytics api.
     * @return wTracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (wTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            wTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return wTracker;
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
}
