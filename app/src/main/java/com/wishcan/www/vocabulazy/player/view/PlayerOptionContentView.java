package com.wishcan.www.vocabulazy.player.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.widget.NumeralPicker;
import com.wishcan.www.vocabulazy.storage.databaseObjects.OptionSettings;

public class PlayerOptionContentView extends LinearLayout {
    
    public interface OnOptionClickListener {
        void onOptionClick(int optionId, View v);
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
    private static final int PLAYER_OPTION_SENTENCE_VIEW_RES_ID = R.id.action_set_sentence;
    private static final int PLAYER_OPTION_SECOND_PICKER_RES_ID = R.id.action_picker_second;
    private static final int PLAYER_OPTION_FREQUENCY_PICKER_RES_ID = R.id.action_picker_frequency;
    private static final int PLAYER_OPTION_SPEED_PICKER_RES_ID = R.id.action_picker_speed;
    private static final int PLAYER_OPTION_PLAY_TIME_PICKER_RES_ID = R.id.action_picker_play_time;

    private static final int COLOR_TAB_RES_ID = R.color.player_option_tab0;

    private static final int PLAYER_OPTION_CONTENT_ATTRIBUTE_s[] = {
        R.styleable.PlayerOptionContent_setPlayerOptionContentColor
    };
    
    private ImageView mRandomOptionView, mRepeatOptionView, mSentenceOptionView;
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
        mRandomOptionView = (ImageView) findViewById(PLAYER_OPTION_RANDOM_VIEW_RES_ID);
        mRepeatOptionView = (ImageView) findViewById(PLAYER_OPTION_REPEAT_VIEW_RES_ID);
        mSentenceOptionView = (ImageView) findViewById(PLAYER_OPTION_SENTENCE_VIEW_RES_ID);
        mSecondOptionPicker = (NumeralPicker) findViewById(PLAYER_OPTION_SECOND_PICKER_RES_ID);
        mFrequencyOptionPicker = (NumeralPicker) findViewById(PLAYER_OPTION_FREQUENCY_PICKER_RES_ID);
        mSpeedOptionPicker = (NumeralPicker) findViewById(PLAYER_OPTION_SPEED_PICKER_RES_ID);
        mPlayTimeOptionPicker = (NumeralPicker) findViewById(PLAYER_OPTION_PLAY_TIME_PICKER_RES_ID);
        
        registerOptionsListener();
    }
    
    public void setOptionSettings(OptionSettings optionSettings) {
        /** OPTION_IDX_s starts from 1 */
        mRandomOptionView.setImageLevel(optionSettings.isRandom() ? 1 : 0);
        mRepeatOptionView.setImageLevel(optionSettings.getListLoop());
        mSentenceOptionView.setImageLevel(optionSettings.isSentence() ? 1 : 0);
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
    
    public void setOnOptionClickListener(OnOptionClickListener listener) {
        mOnOptionClickListener = listener;
    }

    public void setContentBackgroundColor(int color) {
        setBackgroundColor(color);
    }
    
    private void registerOptionsListener() {
        mRandomOptionView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnOptionClickListener != null) {
                    mOnOptionClickListener.onOptionClick(IDX_OPTION_RANDOM, view);
                }
            }
        });
        
        mRepeatOptionView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnOptionClickListener != null) {
                    mOnOptionClickListener.onOptionClick(IDX_OPTION_REPEAT, view);
                }
            }
        });
        
        mSentenceOptionView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnOptionClickListener != null) {
                    mOnOptionClickListener.onOptionClick(IDX_OPTION_SENTENCE, view);
                }
            }
        });
        
        mSecondOptionPicker.setOnPickerClickedListener(new NumeralPicker.OnPickerClickListener() {
            @Override
            public void onPickerClicked(String valueStr) {
                if (mOnOptionClickListener != null) {
                    mOnOptionClickListener.onOptionClick(IDX_OPTION_SECOND, mSecondOptionPicker);
                }
            }
        });
        
        mFrequencyOptionPicker.setOnPickerClickedListener(new NumeralPicker.OnPickerClickListener() {
            @Override
            public void onPickerClicked(String valueStr) {
                if (mOnOptionClickListener != null) {
                    mOnOptionClickListener.onOptionClick(IDX_OPTION_FREQUENCY, mFrequencyOptionPicker);
                }
            }
        });
        
        mSpeedOptionPicker.setOnPickerClickedListener(new NumeralPicker.OnPickerClickListener() {
            @Override
            public void onPickerClicked(String valueStr) {
                if (mOnOptionClickListener != null) {
                    mOnOptionClickListener.onOptionClick(IDX_OPTION_SPEED, mSpeedOptionPicker);
                }
            }
        });

        mPlayTimeOptionPicker.setOnPickerClickedListener(new NumeralPicker.OnPickerClickListener() {
            @Override
            public void onPickerClicked(String valueStr) {
                if (mOnOptionClickListener != null) {
                    mOnOptionClickListener.onOptionClick(IDX_OPTION_PLAY_TIME, mPlayTimeOptionPicker);
                }
            }
        });
    }
}