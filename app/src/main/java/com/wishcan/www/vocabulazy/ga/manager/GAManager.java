package com.wishcan.www.vocabulazy.ga.manager;

import android.content.Context;

import com.google.android.gms.analytics.ExceptionReporter;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.wishcan.www.vocabulazy.R;

import static com.google.android.gms.analytics.internal.zzy.e;

/**
 * Created by allencheng07 on 2016/9/24.\n\n
 *
 * {@link GAManager} is the class to handle objects and methods related to Google Analytics.
 */

public class GAManager {

    // TODO: tooooooooooo dooooooooooooo
    // TODO: allen try to create conflict
    // TODO: swallow : ok

    // TAG for debugging
    public static final String TAG = "GAManager";

    // Google Analytics instance
    private GoogleAnalytics mGoogleAnalytics;

    // Google Analytics tracker
    private Tracker mGlobalTracker;

    // GAManager singleton instance
    private static GAManager mGAManager;

    /**
     * Retrieve the singleton of the {@link GAManager}. This is the only way to get a {@link GAManager}.
     *
     * @return the singleton of {@link GAManager}.
     */
    public static GAManager getInstance() {
        if (mGAManager == null) {
            mGAManager = new GAManager();
        }
        return mGAManager;
    }

    /**
     * Make the constructor as private, thus {@link GAManager} will not be instantiate by others.
     */
    private GAManager() {
    }

    /**
     * Initialize the {@link GAManager}.
     *
     * @param context the context of the application.
     */
    public void init(Context context) {

        // init ga tracker
        initTrackers(context);
    }

    private void initTrackers(Context context) {

        // init global tracker
        initGlobalTracker(context);
    }

    private void initGlobalTracker(Context context) {
        if (mGlobalTracker == null) {
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mGoogleAnalytics = GoogleAnalytics.getInstance(context);
            mGlobalTracker = mGoogleAnalytics.newTracker(R.xml.global_tracker);
            mGlobalTracker.enableExceptionReporting(true);

            // set up uncaught exception handler
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionReporter(
                    mGlobalTracker,
                    Thread.getDefaultUncaughtExceptionHandler(),
                    context));
        }
    }

    /**
     * Send screen events to Google Analytics.
     *
     * @param screenName the name of the screen.
     */
    public void sendScreenEvent(String screenName) {
        mGlobalTracker.setScreenName(screenName);
        mGlobalTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    /**
     * Send events (clicking, swiping, etc.) to Google Analytics.
     *
     * @param category the category of the event.
     * @param action the action of the event.
     * @param label the label of the event.
     * @param value the value of the event.
     */
    public void sendEvent(String category, String action, String label, long value) {
        mGlobalTracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .setValue(value)
                .build());
    }

    /**
     * Send exceptions while they happen.
     *
     * @param context the context of the application.
     * @param e the exception.
     * @param isFatal whether the exception is fatal.
     */
    public void sendException(Context context, Exception e, boolean isFatal) {
        mGlobalTracker.send(new HitBuilders.ExceptionBuilder()
                .setDescription(new StandardExceptionParser(context, null).getDescription(Thread.currentThread().getName(), e))
                .setFatal(isFatal)
                .build());
    }

    /**
     * While debugging and testing, we can disable Google Analytics temporarily to prevent test/debug
     * events sending to Google Analytics.
     *
     * @param isDryRun whether to disable Google Analytics.
     */
    public void disableGATemporary(boolean isDryRun) {
        mGoogleAnalytics.setDryRun(isDryRun);
    }
}
