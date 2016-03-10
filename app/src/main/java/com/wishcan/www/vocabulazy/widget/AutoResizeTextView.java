package com.wishcan.www.vocabulazy.widget;

import android.content.Context;
import android.widget.TextView;

/**
 * Created by swallow on 2016/3/6.
 */
public class AutoResizeTextView extends TextView {

    public AutoResizeTextView(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
