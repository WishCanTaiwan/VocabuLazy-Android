package wishcantw.vocabulazy.audio;

import android.os.Handler;
import android.support.annotation.NonNull;

class Timer {

    // callback interface
    public abstract static class Callback {
        public void timeUp() {}
    }

    // singleton
    private static Timer timer = new Timer();

    // private constructor
    private Timer() {}

    // singleton getter
    public static Timer getInstance() {
        return timer;
    }

    // handler
    private Handler handler;

    // runnable
    private Runnable runnable;

    // timer time
    private int minute = 0;

    /**
     * Initialize timer
     *
     * @param handler the handler
     */
    void init(@NonNull Handler handler) {
        this.handler = handler;
    }

    /**
     * Start timer
     *
     * @param minute the timer time
     * @param callback the timer callback
     */
    void startTimer(int minute,
                    final @NonNull Callback callback) {
        if (minute < 0) {
            return;
        }

        this.minute = minute;

        runnable = new Runnable() {
            @Override
            public void run() {
                callback.timeUp();
                handler.removeCallbacks(runnable);
            }
        };

        handler.postDelayed(runnable, minute * 1000 * 60);
    }

    /**
     * Stop timer
     */
    @SuppressWarnings("unused")
    void stopTimer() {
        handler.removeCallbacks(runnable);
    }

    /**
     * Rest timer
     */
    @SuppressWarnings("unused")
    void resetTimer() {

        // remove timer runnable
        handler.removeCallbacks(runnable);

        // start timer runnable again
        handler.postDelayed(runnable, minute * 1000 * 60);
    }
}
