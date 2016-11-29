package wishcantw.vocabulazy.player.view;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import wishcantw.vocabulazy.R;

/**
 * Created by SwallowChen on 11/22/16.
 */

public class PlayerOptionSeekBarsView extends RelativeLayout {
    /**
     * The callback function should implement the function
     */
    public interface EventListener {
        /**
         * The callback function when any of seek bar is dragged or touched
         * @param seekBarIdx indicate which seek bar is performing the action
         * @param seekBar the view that seek bar is performing the action
         * @param i the current value that seek bar after performing the action
         * @param b True if the progress change was initiated by the user.
         */
        void onSeekBarChanged(int seekBarIdx, SeekBar seekBar, int i, boolean b);
    }

    // Only PlayerOptionView can access, TODO : should practice to modify all access ability
    protected static final int IDX_SEEK_BAR_REPEAT       = 0x0;
    protected static final int IDX_SEEK_BAR_SPEED        = 0x1;
    protected static final int IDX_SEEK_BAR_PLAY_TIME    = 0x2;

    private static final int VIEW_REPEAT_SEEK_BAR_RES_ID    = R.id.player_option_repeat_seekbar;
    private static final int VIEW_SPEED_SEEK_BAR_RES_ID     = R.id.player_option_speed_seekbar;
    private static final int VIEW_PLAY_TIME_SEEK_BAR_RES_ID = R.id.player_option_play_time_seekbar;

    private static final int VIEW_PLAYER_BALLOON_RES_ID = R.layout.view_player_option_balloon;

    private static final int DIMEN_MARGIN_BALLOON = R.dimen.player_option_seekbar_balloon_margin;

    private static final int DRAWABLE_BALLOON = R.drawable.ic_room_black_48dp;

    private PlayerOptionBalloonView mBalloonView;

    private SeekBar mRepeatSeekBar, mSpeedSeekBar, mPlayingTimeSeekBar;
    private SeekBar mSeekBars[];

    private EventListener mEventListener;
    public PlayerOptionSeekBarsView(Context context) {
        this(context, null);
    }

    public PlayerOptionSeekBarsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBalloonView = (PlayerOptionBalloonView) ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(VIEW_PLAYER_BALLOON_RES_ID, null);
        mBalloonView.setVisibility(INVISIBLE);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mRepeatSeekBar      = (SeekBar) findViewById(VIEW_REPEAT_SEEK_BAR_RES_ID);
        mSpeedSeekBar       = (SeekBar) findViewById(VIEW_SPEED_SEEK_BAR_RES_ID);
        mPlayingTimeSeekBar = (SeekBar) findViewById(VIEW_PLAY_TIME_SEEK_BAR_RES_ID);

        mSeekBars = new SeekBar[] {mRepeatSeekBar, mSpeedSeekBar, mPlayingTimeSeekBar};

        // To make balloon view on top of all others view, addView after all view is inflated
        addView(mBalloonView);

        registerEventListener();
    }

    /**------------------------------------ public method ---------------------------------------**/

    /**
     * The callback function for registering the event listener, {@link EventListener}
     * @param eventListener the callback function that want to perform some action
     */
    public void setEventListener(EventListener eventListener) {
        mEventListener = eventListener;
    }

    /**
     * The function for user to set the max value of seek bar
     * @param seekBarIdx the seek bar id that user want to set
     * @param max the max value of the seek bar
     */
    public void setSeekBarMax(int seekBarIdx, int max) {
        if (seekBarIdx >= mSeekBars.length) {
            return;
        }
        mSeekBars[seekBarIdx].setMax(max);
    }

    /**
     * The function for user to set the current value of seek bar
     * @param seekBarIdx the seek bar id that user want to set
     * @param value the desire value for the seek bar
     */
    public void setSeekBarProgress(int seekBarIdx, int value) {
        if (seekBarIdx >= mSeekBars.length) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 24) {
            mSeekBars[seekBarIdx].setProgress(value, true);
        } else {
            mSeekBars[seekBarIdx].setProgress(value);
        }
    }

    /**----------------------------------- private method ---------------------------------------**/

    /**
     * Register callback function to seek bar
     */
    private void registerEventListener() {
        for (int j = 0; j < mSeekBars.length; j++) {
            mSeekBars[j].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    int curVal = i;
                    moveBalloon(seekBar, curVal);
                    if (mEventListener != null) {
                        mEventListener.onSeekBarChanged(i, seekBar, i, b);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    showBalloon(true, seekBar);
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    showBalloon(false, seekBar);
                }
            });
        }
    }

    /**
     * Show or hide the balloon image view
     * @param show True if the image view is desired for showing
     * @param seekBar the view of the desired seek bar
     */
    private void showBalloon(boolean show, SeekBar seekBar) {
        if (!show) {
            mBalloonView.setVisibility(INVISIBLE);
            return;
        }
        mBalloonView.setVisibility(VISIBLE);
        moveBalloon(seekBar, seekBar.getProgress());
    }

    /**
     * move the balloon image view according to the seek bar
     * @param seekBar the view of the desired seek bar, used to calculate where balloon should be put
     * @param curVal the current value of the seek bar, used to calculate where balloon should be put
     *               and indicate the value currently the PlayerOptionBalloon should show
     */
    private void moveBalloon(SeekBar seekBar, int curVal) {
        int width, height; /* balloon image view */
        int left, right, top, quantity; /* seek bar */
        int paddingLeft, paddingRight;
        int xStart, xEnd, yStart, yEnd;
        float xOffset, yOffset; /* where balloon should be */
        Rect parentViewRect, seekBarRect;

        width = mBalloonView.getWidth();
        height = mBalloonView.getHeight();

        left = seekBar.getLeft();
        right = seekBar.getRight();
        top = seekBar.getTop();
        quantity = seekBar.getMax();
        paddingLeft = seekBar.getPaddingLeft();
        paddingRight = seekBar.getPaddingRight();

        parentViewRect = new Rect();
        getGlobalVisibleRect(parentViewRect);

        seekBarRect = new Rect();
        seekBar.getGlobalVisibleRect(seekBarRect);

        xStart = left + paddingLeft;
        xEnd = right - paddingRight;
        yStart = top;

        xOffset = xStart /* start position */
                + (curVal * (xEnd - xStart) / quantity)
                - 0.5f * width;
        yOffset = yStart
                + (seekBarRect.top - parentViewRect.top)
                - getResources().getDimension(DIMEN_MARGIN_BALLOON)
                - height;

        mBalloonView.setX(xOffset);
        mBalloonView.setY(yOffset);
        mBalloonView.setBalloonText(String.valueOf(curVal));
    }
}
