package com.wishcan.www.vocabulazy.log;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.VLApplication;

public class Logger {

    private static Tracker wTracker;

    public Logger(Context context) {
        if (wTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            wTracker = analytics.newTracker(R.xml.global_tracker);
        }
    }

    public static void d(String TAG, String message) {
        Log.d(TAG, message);
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

    public static void sendScreenViewEvent(String screenName) {
        d(screenName, "Setting screen name: " + screenName);
        wTracker.setScreenName(screenName);
        wTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public static void sendEvent(String category, String action, String label, long value) {
        d(label, action);
        if (wTracker == null) {
//            wTracker = ((VLApplication) getActivity().getApplication()).getDefaultTracker();
        }

        wTracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .setValue(value)
                .build());
    }
}
