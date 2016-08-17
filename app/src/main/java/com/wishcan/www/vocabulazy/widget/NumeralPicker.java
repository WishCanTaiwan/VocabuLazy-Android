package com.wishcan.www.vocabulazy.widget;

/**
 * Created by swallow on 2016/1/17.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wishcan.www.vocabulazy.R;
/**
 * Created by swallow on 2015/8/21.
 * Our NumeralPicker is a LinearLayout, mainly composed with 4 Views,
 * each of them are two ImageViews, mLeftArrowImageView and mRightArrowImageView;
 * two TextViews, mNumberTextView and mTitleTextView;
 */
public class NumeralPicker extends LinearLayout {

    public static final String TAG = NumeralPicker.class.getSimpleName();

    public interface OnPickerClickListener {
        void onPickerClicked(String valueStr);
    }

    private static final int DEFAULT_MAXIMUM = 9;
    private static final int DEFAULT_MINIMUM = 0;
    private static final int DEFAULT_ARROW_IMAGE_PADDING_RES_ID = R.dimen.widget_picker_arrow_image_padding;
    private static final int DEFAULT_TEXT_PADDING_RES_ID = R.dimen.widget_picker_text_padding;
    private static final int DEFAULT_PICKER_TEXT_SIZE_RES_ID = R.dimen.widget_picker_text_size;
    private static final int DEFAULT_LEFT_ARROW_RES_ID = R.drawable.widget_picker_arrow_yellow_left;
    private static final int DEFAULT_RIGHT_ARROW_RES_ID = R.drawable.widget_picker_arrow_yellow_right;
    private static final int DEFAULT_PICKER_TEXT_COLOR = R.color.widget_picker_text;
    private static final int DEFAULT_PICKER_BACKGROUND_COLOR = R.color.widget_picker_background;
    private static final int DEFAULT_PICKER_BACKGROUND_DRAWABLE = R.drawable.widget_picker_background;
    /**
     * There are mainly four views in this
     * */
    private TextView mNumberTextView;
    private ImageView mLeftArrowImageView;
    private ImageView mRightArrowImageView;
    private int mMaximumNumber;
    private int mMinimumNumber;
    private int mPickerRange;
    private int mPickerBackgroundColor;
    private int mPickerTextColor;
    private Context mContext;
    private OnPickerClickListener mOnPickerClickListener;

    public NumeralPicker(Context context) {
        this(context, null);
    }

    public NumeralPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.NumeralPicker, 0, 0);
        try {
            mMaximumNumber = ta.getInteger(R.styleable.NumeralPicker_setPickerMaximumNumber, DEFAULT_MAXIMUM);
            mMinimumNumber = ta.getInteger(R.styleable.NumeralPicker_setPickerMinimumNumber, DEFAULT_MINIMUM);
            mPickerTextColor = ta.getColor(R.styleable.NumeralPicker_setPickerNumberTextColor, ContextCompat.getColor(context, DEFAULT_PICKER_TEXT_COLOR));
            mPickerBackgroundColor = ta.getColor(R.styleable.NumeralPicker_setPickerBackgroundColor, ContextCompat.getColor(context, DEFAULT_PICKER_BACKGROUND_COLOR));
        } finally {

        }
        mContext = context;
        initAllLayout();

    }

    private void initAllLayout() {

        setLeftArrowImageView(DEFAULT_LEFT_ARROW_RES_ID);
        setRightArrowImageView(DEFAULT_RIGHT_ARROW_RES_ID);
        setNumberTextView();
/*
        int arrowImagePadding = (int) getResources().getDimension(DEFAULT_ARROW_IMAGE_PADDING_RES_ID);
        int numberTextPadding = (int) getResources().getDimension(DEFAULT_TEXT_PADDING_RES_ID);
*/
        int arrowImagePadding = 0;
        int numberTextPadding = 0;

        mLeftArrowImageView.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.2f));
        mRightArrowImageView.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.2f));

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mLeftArrowImageView.setPaddingRelative(arrowImagePadding, 0, arrowImagePadding, 0);
            mRightArrowImageView.setPaddingRelative(arrowImagePadding, 0, arrowImagePadding, 0);
        }
        else {
            mLeftArrowImageView.setPadding(arrowImagePadding, 0, arrowImagePadding, 0);
            mRightArrowImageView.setPadding(arrowImagePadding, 0, arrowImagePadding, 0);
        }
/*
        mNumberTextView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
*/
        mNumberTextView.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.6f));
        mNumberTextView.setPaddingRelative(numberTextPadding, 0, numberTextPadding, 0);
        mNumberTextView.setGravity(Gravity.CENTER);
/*
        setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
*/
        setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        GradientDrawable pickerBackgroundDrawable = (GradientDrawable) ContextCompat.getDrawable(getContext(), DEFAULT_PICKER_BACKGROUND_DRAWABLE);
        pickerBackgroundDrawable.setColor(mPickerBackgroundColor);
        setBackground(pickerBackgroundDrawable);

        addView(mLeftArrowImageView);
        addView(mNumberTextView);
        addView(mRightArrowImageView);
    }


    public void setNumberTextView(){
        mNumberTextView = new TextView(mContext);
        mNumberTextView.setText(Integer.toString(mMinimumNumber));
        mNumberTextView.setTextColor(mPickerTextColor);
        mNumberTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(DEFAULT_PICKER_TEXT_SIZE_RES_ID));
    }

    public void setRightArrowImageView(int resId) {
        mRightArrowImageView = new ImageView(mContext);
        mRightArrowImageView.setImageResource(resId);
        registerRightArrowOnClickListener(mRightArrowImageView);
    }

    public void setLeftArrowImageView(int resId) {
        mLeftArrowImageView = new ImageView(mContext);
        mLeftArrowImageView.setImageResource(resId);
        registerLeftArrowOnClickListener(mLeftArrowImageView);
    }

    public void setMaximumNumber(int maximum){
        mMaximumNumber = maximum;
    }

    public void setMinimumNumber(int minimum){
        mMinimumNumber = minimum;
    }

    public void calculatePickerRange() {
        mPickerRange = mMaximumNumber - mMinimumNumber + 1;
    }

    public void setOnPickerClickedListener(OnPickerClickListener listener) {
        mOnPickerClickListener = listener;
    }

    public void setPickerTextStr(String str) {
        mNumberTextView.setText(str);
    }

    private void registerRightArrowOnClickListener(ImageView v) {
        v.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("NumeralPicker", "right on click");
                int oldValue = Integer.parseInt(mNumberTextView.getText().toString());
                int newValue = (((oldValue - mMinimumNumber) + 1 + mPickerRange) % mPickerRange) + mMinimumNumber;
                String newValueStr = String.valueOf(newValue);
                mNumberTextView.setText(newValueStr);
                if (mOnPickerClickListener != null) {
                    mOnPickerClickListener.onPickerClicked(newValueStr);
                }
            }
        });
    }

    private void registerLeftArrowOnClickListener(ImageView v) {
        v.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("NumeralPicker", "left on click");
                int oldValue = Integer.parseInt(mNumberTextView.getText().toString());
                int newValue = (((oldValue - mMinimumNumber) - 1 + mPickerRange) % mPickerRange) + mMinimumNumber;
                String newValueStr = String.valueOf(newValue);
                mNumberTextView.setText(newValueStr);
                if (mOnPickerClickListener != null) {
                    mOnPickerClickListener.onPickerClicked(newValueStr);
                }
            }
        });
    }
}
