package com.wishcan.www.vocabulazy;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

//import io.uxtesting.UXTeting;

/**
 * This class is created for applying UXTesting.
 */
public class VLApplication extends Application {

    private static final String UXTESTING_APP_KEY = "JzskKAJgBGPlmP-NIBxBug";
    private Tracker wTracker;

    @Override
    public void onCreate() {
        super.onCreate();
//        UXTesting.Init(this, UXTESTING_APP_KEY);
    }

    synchronized public Tracker getDefaultTracker() {
        if (wTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            wTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return wTracker;
    }
}
