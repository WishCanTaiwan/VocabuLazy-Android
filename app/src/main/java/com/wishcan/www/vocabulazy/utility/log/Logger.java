package com.wishcan.www.vocabulazy.utility.log;

import android.content.Context;

import com.google.android.gms.analytics.ExceptionReporter;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.wishcan.www.vocabulazy.R;

import java.util.ArrayList;

public class Logger {

    private static Tracker wTracker;
    private static LogBuffer mLogBuffer;

    public Logger(Context context) {
        if (wTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            wTracker = analytics.newTracker(R.xml.global_tracker);
        }

        if (mLogBuffer == null) {
            mLogBuffer = new LogBuffer();
        }
    }

    public static void d(String TAG, String message) {
//        Log.d(TAG, message);
        mLogBuffer.put(TAG, message);
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

            Thread.UncaughtExceptionHandler handler = new ExceptionReporter(
                    wTracker,
                    Thread.getDefaultUncaughtExceptionHandler(),
                    context);

            // Make handler the new default uncaught exception handler.
            Thread.setDefaultUncaughtExceptionHandler(handler);
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
//            wTracker = ((GlobalVariable) getActivity().getApplication()).getDefaultTracker();
        }

        wTracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .setValue(value)
                .build());
    }

    public static void sendException(Context context, Exception e, boolean isFatal) {
        ArrayList<LogEntry> logs = mLogBuffer.pull();
        d(e.toString(), e.getMessage());

        wTracker.send(new HitBuilders.ExceptionBuilder()
                .setDescription(new StandardExceptionParser(context, null).getDescription(Thread.currentThread().getName(), e))
                .setFatal(isFatal)
                .build());
    }
}
