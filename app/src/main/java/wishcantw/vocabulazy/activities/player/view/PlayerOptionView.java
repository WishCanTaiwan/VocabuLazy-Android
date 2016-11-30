package wishcantw.vocabulazy.activities.player.view;

import android.content.Context;
import android.util.AttributeSet;
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

    private static final int IDX_SEEK_BAR_REPEAT = PlayerOptionSeekBarsView.IDX_SEEK_BAR_REPEAT;
    private static final int IDX_SEEK_BAR_SPEED  = PlayerOptionSeekBarsView.IDX_SEEK_BAR_SPEED;
    private static final int IDX_SEEK_BAR_PLAY_TIME = PlayerOptionSeekBarsView.IDX_SEEK_BAR_PLAY_TIME;

    private static final int VIEW_PLAYER_OPTION_VOICE_SWITCH_ID           = R.id.player_option_voice_switch;
    private static final int VIEW_PLAYER_OPTION_SENTENCE_SWITCH_ID        = R.id.player_option_sentence_switch;
    private static final int VIEW_PLAYER_OPTION_MODE_RADIO_GROUP_ID       = R.id.player_option_mode_radio_group;
    private static final int VIEW_PLAYER_OPTION_LIST_ORDER_RADIO_GROUP_ID = R.id.player_option_list_order_radio_group;
    private static final int VIEW_PLAYER_OPTION_VOC_ORDER_RADIO_GROUP_ID  = R.id.player_option_voc_order_radio_group;
    private static final int VIEW_PLAYER_OPTION_SEEK_BARS_ID              = R.id.player_option_seekbars;

    private Switch mVoiceSwitch, mSentenceSwitch;
    private RadioGroup mModeRadioGroup, mListOrderRadioGroup, mVocOrderRadioGroup;
    private PlayerOptionSeekBarsView mPlayerOptionSeekBarsView;

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
        mPlayerOptionSeekBarsView = (PlayerOptionSeekBarsView) findViewById(VIEW_PLAYER_OPTION_SEEK_BARS_ID);

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

        mPlayerOptionSeekBarsView.setSeekBarMax(IDX_SEEK_BAR_REPEAT, 5);
        mPlayerOptionSeekBarsView.setSeekBarMax(IDX_SEEK_BAR_SPEED, 2);
        mPlayerOptionSeekBarsView.setSeekBarMax(IDX_SEEK_BAR_PLAY_TIME, 40);
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
        boolean voiceEnable, sentenceEnable;
        int modeIdx, listOrderIdx, vocOrderIdx;

        unregisterListener();
        voiceEnable = true;
        sentenceEnable = option.isSentence();
        modeIdx = option.getMode();
        listOrderIdx = option.isRandom() ? 1 : 0;
        vocOrderIdx = option.isRandom() ? 1 : 0;

        // Only when init state need to setup mode option
        if (init) {
            // Use input option to determined the initial (or last time saved) mode
            mModeRadioGroup.check(modeIdx);
        }

        mVoiceSwitch.setChecked(voiceEnable);
        mSentenceSwitch.setChecked(sentenceEnable);
        mListOrderRadioGroup.check(listOrderIdx);
        mVocOrderRadioGroup.check(vocOrderIdx);

        mPlayerOptionSeekBarsView.setSeekBarProgress(IDX_SEEK_BAR_REPEAT, option.getItemLoop());
        mPlayerOptionSeekBarsView.setSeekBarProgress(IDX_SEEK_BAR_SPEED, option.getSpeed());
        mPlayerOptionSeekBarsView.setSeekBarProgress(IDX_SEEK_BAR_PLAY_TIME, option.getPlayTime());

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
        // The callback for any of seek bars is changed
        mPlayerOptionSeekBarsView.setEventListener(new PlayerOptionSeekBarsView.EventListener() {
            @Override
            public void onSeekBarChanged(int seekBarIdx, SeekBar seekBar, int i, boolean b) {
                switch (seekBarIdx) {
                    case IDX_SEEK_BAR_REPEAT:
                    case IDX_SEEK_BAR_SPEED:
                    case IDX_SEEK_BAR_PLAY_TIME:
                        if (mOnOptionChangedListener != null) {
                            // Because Seek bar idx is started from IDX_SEEK_BAR_REPEAT (FREQUENCY) (0)
                            int startIdx = IDX_OPTION_FREQUENCY;
                            int modeId = mModeRadioGroup.getCheckedRadioButtonId();
                            mOnOptionChangedListener.onOptionChanged(startIdx + seekBarIdx, modeId, seekBar, i);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**----------------------------------- private method ---------------------------------------**/

    private void unregisterListener() {
        mRestoreListener = mOnOptionChangedListener;
        mOnOptionChangedListener = null;
    }

    private void restoreListener() {
        mOnOptionChangedListener = mRestoreListener;
    }
}
