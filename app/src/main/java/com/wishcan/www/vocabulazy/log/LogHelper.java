package com.wishcan.www.vocabulazy.log;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.wishcan.www.vocabulazy.R;

public class LogHelper {

    Tracker wTracker;

    public LogHelper(Context context) {
        if (wTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            wTracker = analytics.newTracker(R.xml.global_tracker);
        }
    }

    public void d(String TAG, String message) {
        Log.d(TAG, message);
    }

    public void e(String TAG, String message) {
        Log.e(TAG, message);
    }

    /**
     * Call getDefaultTracker to retrieve the tracker used by Google Analytics api.
     * @return wTracker
     */
    public Tracker getDefaultTracker(Context context) {
        if (wTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            wTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return wTracker;
    }

    public void sendScreenViewEvent(String screenName) {
        d(screenName, "Setting screen name: " + screenName);
        wTracker.setScreenName(screenName);
        wTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
