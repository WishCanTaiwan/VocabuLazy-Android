package com.wishcan.www.vocabulazy.view.reading;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by swallow on 2015/12/6.
 */
public class ReadingMainScrollView extends ScrollView {

    private Context mContext;

    private LinearLayout mReadingLayout;

    private TextView mReadingContentTextView;

    public ReadingMainScrollView(Context context) {
        this(context, null);
    }

    public ReadingMainScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        initLayout();
    }

    private void initLayout(){
        mReadingLayout = new LinearLayout(mContext);
        mReadingLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mReadingContentTextView = new TextView(mContext);
        mReadingContentTextView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mReadingLayout.addView(mReadingContentTextView);
        addView(mReadingLayout);
    }

    public void setReadingContentText(String inputStr){
        mReadingContentTextView.setText(inputStr);
    }

    public void setReadingContentColor(int color){
        mReadingContentTextView.setTextColor(color);
    }

    public void setReadingContentTextSize(float sizeInPx){
        mReadingContentTextView.setTextSize(sizeInPx);
    }
}
