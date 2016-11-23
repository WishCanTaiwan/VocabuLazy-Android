package wishcantw.vocabulazy.activities.player.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.widget.LevelImageView;
import wishcantw.vocabulazy.widget.NumeralPicker;
import wishcantw.vocabulazy.database.object.OptionSettings;

public class PlayerOptionContentView extends LinearLayout {
    /**
     * OnOptionClickListener is the callback function when any of option, including tab, is clicked
     * */
    public interface OnOptionClickListener {
        void onOptionClick(int optionId, View v, int leftOrRight);
    }

    public static final int IDX_OPTION_RANDOM = 0x1;
    public static final int IDX_OPTION_REPEAT = 0x2;
    public static final int IDX_OPTION_SENTENCE = 0x3;
    public static final int IDX_OPTION_SECOND = 0x4;
    public static final int IDX_OPTION_FREQUENCY = 0x5;
    public static final int IDX_OPTION_SPEED = 0x6;
    public static final int IDX_OPTION_PLAY_TIME = 0x7;
    
    private static final int[] OPTION_IDX_s = {
        0,
        IDX_OPTION_RANDOM,
        IDX_OPTION_REPEAT,
        IDX_OPTION_SENTENCE,
        IDX_OPTION_SECOND,
        IDX_OPTION_FREQUENCY,
        IDX_OPTION_SPEED,
        IDX_OPTION_PLAY_TIME
    };

    private static final int PLAYER_OPTION_RANDOM_VIEW_RES_ID = R.id.action_set_random;
    private static final int PLAYER_OPTION_REPEAT_VIEW_RES_ID = R.id.action_set_repeat;
//    private static final int PLAYER_OPTION_SENTENCE_VIEW_RES_ID = R.id.action_set_sentence;
    private static final int PLAYER_OPTION_SECOND_PICKER_RES_ID = R.id.action_picker_second;
    private static final int PLAYER_OPTION_FREQUENCY_PICKER_RES_ID = R.id.action_picker_frequency;
    private static final int PLAYER_OPTION_SPEED_PICKER_RES_ID = R.id.action_picker_speed;
    private static final int PLAYER_OPTION_PLAY_TIME_PICKER_RES_ID = R.id.action_picker_play_time;

    private static final int COLOR_TAB_RES_ID = R.color.player_option_tab0;

    private static final int PLAYER_OPTION_CONTENT_ATTRIBUTE_s[] = {
        R.styleable.PlayerOptionContent_setPlayerOptionContentColor
    };
    
    private LevelImageView mRandomOptionView, mRepeatOptionView, mSentenceOptionView;
    private NumeralPicker mSecondOptionPicker, mFrequencyOptionPicker, mSpeedOptionPicker, mPlayTimeOptionPicker;
    
    private OnOptionClickListener mOnOptionClickListener;
    private int mContentBackgroundColor;
    
    public PlayerOptionContentView(Context context) {
        this(context, null);
    }
    
    public PlayerOptionContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PlayerOptionTabView, 0, 0);
        try {
            final int N = PLAYER_OPTION_CONTENT_ATTRIBUTE_s.length;
            for (int i = 0; i < N; i++) {
                int attribute = PLAYER_OPTION_CONTENT_ATTRIBUTE_s[i];
                switch (attribute) {
                    case R.styleable.PlayerOptionContent_setPlayerOptionContentColor:
                        mContentBackgroundColor = ta.getColor(attribute, ContextCompat.getColor(context, COLOR_TAB_RES_ID));
                        break;
                }
            }
        } finally {
            setContentBackgroundColor(mContentBackgroundColor);
        }
    }
    
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mRandomOptionView = (LevelImageView) findViewById(PLAYER_OPTION_RANDOM_VIEW_RES_ID);
        mRepeatOptionView = (LevelImageView) findViewById(PLAYER_OPTION_REPEAT_VIEW_RES_ID);
//        mSentenceOptionView = (LevelImageView) findViewById(PLAYER_OPTION_SENTENCE_VIEW_RES_ID);
        mSecondOptionPicker = (NumeralPicker) findViewById(PLAYER_OPTION_SECOND_PICKER_RES_ID);
        mFrequencyOptionPicker = (NumeralPicker) findViewById(PLAYER_OPTION_FREQUENCY_PICKER_RES_ID);
        mSpeedOptionPicker = (NumeralPicker) findViewById(PLAYER_OPTION_SPEED_PICKER_RES_ID);
        mPlayTimeOptionPicker = (NumeralPicker) findViewById(PLAYER_OPTION_PLAY_TIME_PICKER_RES_ID);
        
        registerOptionsListener();
    }

    /**
     * Hook the callback function
     * @param listener the callback function
     * */
    public void setOnOptionClickListener(OnOptionClickListener listener) {
        mOnOptionClickListener = listener;
    }

    /**
     * The api for the controller the customized the showing items
     * @param optionSettings the settings for customizing the option content
     * */
    public void setOptionSettings(OptionSettings optionSettings) {
        /** OPTION_IDX_s starts from 1 */
        mRandomOptionView.setImageLevel(optionSettings.isRandom() ? 1 : 0);
        mRepeatOptionView.setImageLevel(optionSettings.getListLoop());
//        mSentenceOptionView.setImageLevel(optionSettings.isSentence() ? 1 : 0);
        /** TODO: refine calculatePickerRange into NumeralPicker constructor */
        mSecondOptionPicker.calculatePickerRange();
        mSecondOptionPicker.setPickerTextStr(String.valueOf(optionSettings.getStopPeriod()));
        mFrequencyOptionPicker.calculatePickerRange();
        mFrequencyOptionPicker.setPickerTextStr(String.valueOf(optionSettings.getItemLoop()));
        mSpeedOptionPicker.calculatePickerRange();
        mSpeedOptionPicker.setPickerTextStr(String.valueOf(optionSettings.getSpeed()));
        mPlayTimeOptionPicker.calculatePickerRange();
        mPlayTimeOptionPicker.setPickerTextStr(String.valueOf(optionSettings.getPlayTime()));
    }

    /**
     * The api for customizing Option Content background
     * @param color the color for Option content
     * */
    public void setContentBackgroundColor(int color) {
        setBackgroundColor(color);
    }

    /**
     * Register all the child's event
     * */
    private void registerOptionsListener() {
        mRandomOptionView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnOptionClickListener != null) {
                    LevelImageView v = (LevelImageView) view;
                    v.setImageLevel(v.getImageLevel() + 1);
                    mOnOptionClickListener.onOptionClick(IDX_OPTION_RANDOM, view, 0);
                }
            }
        });
        
        mRepeatOptionView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnOptionClickListener != null) {
                    LevelImageView v = (LevelImageView) view;
                    v.setImageLevel(v.getImageLevel() + 1);
                    mOnOptionClickListener.onOptionClick(IDX_OPTION_REPEAT, view, 0);
                }
            }
        });
        
//        mSentenceOptionView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mOnOptionClickListener != null) {
//                    LevelImageView v = (LevelImageView) view;
//                    v.setImageLevel(v.getImageLevel() + 1);
//                    mOnOptionClickListener.onOptionClick(IDX_OPTION_SENTENCE, view, 0);
//                }
//            }
//        });
        
        mSecondOptionPicker.setOnPickerClickedListener(new NumeralPicker.OnPickerClickListener() {
            @Override
            public void onPickerClicked(int leftOrRight, String valueStr) {
                if (mOnOptionClickListener != null) {
                    mOnOptionClickListener.onOptionClick(IDX_OPTION_SECOND, mSecondOptionPicker, leftOrRight);
                }
            }
        });
        
        mFrequencyOptionPicker.setOnPickerClickedListener(new NumeralPicker.OnPickerClickListener() {
            @Override
            public void onPickerClicked(int leftOrRight, String valueStr) {
                if (mOnOptionClickListener != null) {
                    mOnOptionClickListener.onOptionClick(IDX_OPTION_FREQUENCY, mFrequencyOptionPicker, leftOrRight);
                }
            }
        });
        
        mSpeedOptionPicker.setOnPickerClickedListener(new NumeralPicker.OnPickerClickListener() {
            @Override
            public void onPickerClicked(int leftOrRight, String valueStr) {
                if (mOnOptionClickListener != null) {
                    mOnOptionClickListener.onOptionClick(IDX_OPTION_SPEED, mSpeedOptionPicker, leftOrRight);
                }
            }
        });

        mPlayTimeOptionPicker.setOnPickerClickedListener(new NumeralPicker.OnPickerClickListener() {
            @Override
            public void onPickerClicked(int leftOrRight, String valueStr) {
                if (mOnOptionClickListener != null) {
                    mOnOptionClickListener.onOptionClick(IDX_OPTION_PLAY_TIME, mPlayTimeOptionPicker, leftOrRight);
                }
            }
        });
    }
}