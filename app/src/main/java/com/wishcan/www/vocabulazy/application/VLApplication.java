package com.wishcan.www.vocabulazy.application;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.analytics.Tracker;
import com.wishcan.www.vocabulazy.utility.log.Logger;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.Preferences;

/**
 * This class is created for giving a global overview to our application.
 */
public class VLApplication extends Application {

    public static final String TAG = "VLApplication";

    /**
     * The preferences which store all arguments and parameters.
     */
    private Preferences wPreferences;

    /**
     * The Database object used in the entire application.
     */
    private Database wDatabase;

    /**
     * The Logger is to help application logging messages and errors, also helps send Google
     * Analytics event messages through Tracker.
     */
    private Logger wLogger;

    @Override
    public void onCreate() {
        Log.d(TAG, "Create");
        super.onCreate();

        /* Every-time the application is created, create a Preferences object and
            it will be shared among activities and fragments of this particular application. */
        wPreferences = new Preferences();

//        /**
//         * Every-time entering the application, create a Database object, and throughout entire
//         * application, this is the only Database object to be accessed.
//         */
//        wDatabase = new Database(this);

        /**
         * initialize the Logger object.
         */
        wLogger = new Logger(getApplicationContext());
    }

    /**
     * Call getDefaultTracker to retrieve the tracker used by Google Analytics api.
     * @return wTracker
     */
    synchronized public Tracker getDefaultTracker() {
        return wLogger.getDefaultTracker(getApplicationContext());
    }

    /**
     * Call getPreferences to retrieve the Preferences obejct of this application.
     * @return wPreferences
     */
    synchronized public Preferences getPreferences() {
        return wPreferences;
    }

    public void loadDatabase() {
        wDatabase.loadFiles(getApplicationContext());
        wDatabase.initSettings();
    }

    synchronized public Logger getLogHelper() {
        return wLogger;
    }
}
