package com.wishcan.www.vocabulazy.exam.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

/**
 * Created by SwallowChen on 8/28/16.
 */
public class ExamProgressBar extends ProgressBar {

    private Context mContext;
    public ExamProgressBar(Context context) {
        this(context, null);
    }

    public ExamProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, mContext.getResources().getDisplayMetrics());
        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) px, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
