package wishcantw.vocabulazy.utility;

import android.util.Log;

import wishcantw.vocabulazy.BuildConfig;

/**
 * Log information while debugging.
 *
 * @author allencheng07
 */
public class Logger {

    /**
     * Log debug.
     *
     * @param tag the tag of the log.
     * @param message the message of the log.
     */
    public static void d(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message);
        }
    }
}
