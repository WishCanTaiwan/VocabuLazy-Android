package wishcantw.vocabulazy.player.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.database.object.OptionSettings;

/**
 * Created by SwallowChen on 11/18/16.
 */

public class PlayerOptionView extends LinearLayout {
    /**
     * OnOptionChangedListener is the callback function when any of option, including tab, is changed
     * */
    public interface OnOptionChangedListener {
        void onOptionChanged(int optionID, int mode, View v, int value);
    }

    // TODO : To be determined with beibei
    public static final int IDX_OPTION_MODE      = 0x0;
    public static final int IDX_OPTION_RANDOM    = 0x1;
    public static final int IDX_OPTION_REPEAT    = 0x2;
    public static final int IDX_OPTION_SENTENCE  = 0x3;
    public static final int IDX_OPTION_SECOND    = 0x4;
    public static final int IDX_OPTION_FREQUENCY = 0x5;
    public static final int IDX_OPTION_SPEED     = 0x6;
    public static final int IDX_OPTION_PLAY_TIME = 0x7;
    public static final int IDX_OPTION_VOICE     = 0x8;

    public static final int IDX_MODE_0 = 0x10;
    public static final int IDX_MODE_1 = 0x11;
    public static final int IDX_MODE_2 = 0x12;

    private static final int VIEW_PLAYER_OPTION_VOICE_SWITCH_ID           = R.id.player_option_voice_switch;
    private static final int VIEW_PLAYER_OPTION_SENTENCE_SWITCH_ID        = R.id.player_option_sentence_switch;
    private static final int VIEW_PLAYER_OPTION_MODE_RADIO_GROUP_ID       = R.id.player_option_mode_radio_group;
    private static final int VIEW_PLAYER_OPTION_LIST_ORDER_RADIO_GROUP_ID = R.id.player_option_list_order_radio_group;
    private static final int VIEW_PLAYER_OPTION_VOC_ORDER_RADIO_GROUP_ID  = R.id.player_option_voc_order_radio_group;
    private static final int VIEW_PLAYER_OPTION_REPEAT_SEEK_BAR           = R.id.player_option_repeat_seekbar;
    private static final int VIEW_PLAYER_OPTION_SPEED_SEEK_BAR            = R.id.player_option_speed_seekbar;
    private static final int VIEW_PLAYER_OPTION_PLAYING_TIME_SEEK_BAR     = R.id.player_option_playing_time_seekbar;

    private Switch mVoiceSwitch, mSentenceSwitch;
    private RadioGroup mModeRadioGroup, mListOrderRadioGroup, mVocOrderRadioGroup;
    private SeekBar mRepeatSeekBar, mSpeedSeekBar, mPlayingSeekBar;

    private OnOptionChangedListener mOnOptionChangedListener, mRestoreListener;

    public PlayerOptionView(Context context) {
        this(context, null);
    }

    public PlayerOptionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mVoiceSwitch         = (Switch)     findViewById(VIEW_PLAYER_OPTION_VOICE_SWITCH_ID);
        mSentenceSwitch      = (Switch)     findViewById(VIEW_PLAYER_OPTION_SENTENCE_SWITCH_ID);
        mModeRadioGroup      = (RadioGroup) findViewById(VIEW_PLAYER_OPTION_MODE_RADIO_GROUP_ID);
        mListOrderRadioGroup = (RadioGroup) findViewById(VIEW_PLAYER_OPTION_LIST_ORDER_RADIO_GROUP_ID);
        mVocOrderRadioGroup  = (RadioGroup) findViewById(VIEW_PLAYER_OPTION_VOC_ORDER_RADIO_GROUP_ID);
        mRepeatSeekBar       = (SeekBar)    findViewById(VIEW_PLAYER_OPTION_REPEAT_SEEK_BAR);
        mSpeedSeekBar        = (SeekBar)    findViewById(VIEW_PLAYER_OPTION_SPEED_SEEK_BAR);
        mPlayingSeekBar      = (SeekBar)    findViewById(VIEW_PLAYER_OPTION_PLAYING_TIME_SEEK_BAR);

        for (int i = 0; i < Math.max(mModeRadioGroup.getChildCount(), Math.max(mListOrderRadioGroup.getChildCount(), mVocOrderRadioGroup.getChildCount())); i++) {
            if (mModeRadioGroup.getChildAt(i) != null) {
                mModeRadioGroup.getChildAt(i).setId(i);
            }
            if (mListOrderRadioGroup.getChildAt(i) != null) {
                mListOrderRadioGroup.getChildAt(i).setId(i);
            }
            if (mVocOrderRadioGroup.getChildAt(i) != null) {
                mVocOrderRadioGroup.getChildAt(i).setId(i);
            }
        }
        registerOptionListener();
    }

    /**
     * Hook the callback function
     * @param listener the callback function
     */
    public void setOnOptionChangedListener(OnOptionChangedListener listener) {
        mOnOptionChangedListener = listener;
    }

    /**
     * The api for setting all options in the PlayerOptionView
     * @param option The object containing all settings about the {@link}OptionSettings
     */
    public void setOptionInModeContent(OptionSettings option, boolean init) {
        // unregister listener first to prevent from dependency
        unregisterListener();

        // Only when init state need to setup mode option
        if (init) {
            // Use input option to determined the initial (or last time saved) mode
            mModeRadioGroup.check(option.getMode());
        }
        // Force voice to turned on
        mVoiceSwitch.setChecked(true);
        // Force sentence to turned off (currently database is not supported)
        mSentenceSwitch.setChecked(false);
        // Force list order to be serial order (1 is random order)
        mListOrderRadioGroup.check(0);
        // Force voc order to be serial and not repeat the list
        mVocOrderRadioGroup.check(0);
        // TODO : Fine tuning the seek bar to increase 1 a time, but layout increase 1/5 seek bar
        Log.d("PlayerOptionViewNew", "setProgress");
        if (Build.VERSION.SDK_INT >= 24) {
            mRepeatSeekBar.setProgress(option.getItemLoop() * 10, true);
            mSpeedSeekBar.setProgress(option.getSpeed() * 10, true);
            mPlayingSeekBar.setProgress(option.getPlayTime() * 10 , true);
        } else {
            mRepeatSeekBar.setProgress(option.getItemLoop() * 10);
            mSpeedSeekBar.setProgress(option.getSpeed() * 10);
            mPlayingSeekBar.setProgress(option.getPlayTime() * 10);
        }

        // register back
        restoreListener();
    }

    /**
     * Register the callback function to monitor all event in PlayerOptionView. The listener will
     * sent event back if there's listener registered on top level, here is PlayerView.
     */
    private void registerOptionListener() {
        // The callback for voice enable switch
        mVoiceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (mOnOptionChangedListener != null) {
                    mOnOptionChangedListener.onOptionChanged(IDX_OPTION_VOICE, mModeRadioGroup.getCheckedRadioButtonId(), mVoiceSwitch, b ? 1 : 0);
                }
            }
        });
        // The callback for sentence switch
        mSentenceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (mOnOptionChangedListener != null) {
                    mOnOptionChangedListener.onOptionChanged(IDX_OPTION_SENTENCE, mModeRadioGroup.getCheckedRadioButtonId(), mSentenceSwitch, b ? 1 : 0);
                }
            }
        });
        // The callback for changing mode choose
        mModeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (mOnOptionChangedListener != null) {
                    mOnOptionChangedListener.onOptionChanged(IDX_OPTION_MODE, i, mModeRadioGroup, i);
                }
            }
        });
        // The callback for list order choose
        mListOrderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (mOnOptionChangedListener != null) {
                    mOnOptionChangedListener.onOptionChanged(IDX_OPTION_REPEAT, mModeRadioGroup.getCheckedRadioButtonId(), mListOrderRadioGroup, i);
                }
            }
        });
        // The callback for vocabulary order choose
        mVocOrderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (mOnOptionChangedListener != null) {
                    mOnOptionChangedListener.onOptionChanged(IDX_OPTION_RANDOM, mModeRadioGroup.getCheckedRadioButtonId(), mVocOrderRadioGroup, i);
                }
            }
        });
        // The callback for repeat time picked
        mRepeatSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (mOnOptionChangedListener != null) {
                    mOnOptionChangedListener.onOptionChanged(IDX_OPTION_FREQUENCY, mModeRadioGroup.getCheckedRadioButtonId(), mRepeatSeekBar, i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        // The callback for playing speed picked
        mSpeedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (mOnOptionChangedListener != null) {
                    mOnOptionChangedListener.onOptionChanged(IDX_OPTION_SPEED, mModeRadioGroup.getCheckedRadioButtonId(), mSpeedSeekBar, i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        // The callback for playing duration (auto playing time, once times up, the player shutdown)
        mPlayingSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (mOnOptionChangedListener != null) {
                    mOnOptionChangedListener.onOptionChanged(IDX_OPTION_PLAY_TIME, mModeRadioGroup.getCheckedRadioButtonId(), mPlayingSeekBar, i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void unregisterListener() {
        mRestoreListener = mOnOptionChangedListener;
        mOnOptionChangedListener = null;
    }

    private void restoreListener() {
        mOnOptionChangedListener = mRestoreListener;
    }
}
