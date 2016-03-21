package com.wishcan.www.vocabulazy.widget;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.ViewTreeObserver;
import android.widget.TextView;

/**
 * Created by swallow on 2016/3/6.
 */
public class AutoResizeTextView extends TextView {

    public AutoResizeTextView(Context context) {
        this(context, null);
    }

    public AutoResizeTextView(Context context, AttributeSet attr) {
        super(context, attr);
        setVisibility(INVISIBLE);
        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        if(viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    while(getLineCount() > 1) {
                        float textSize = getTextSize();
                        float density = getContext().getResources().getDisplayMetrics().density;
                        Log.d("InfoFragment", "textSize = " + textSize);
                        setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize - 1.0f*density);
                    }
                }
            });
        } else {
            Log.d("AutoResizeTextView", "viewTreeObserver is not alive");
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d("AutoResizeTextView", "onSizeChanged");
        super.onSizeChanged(w, h, oldw, oldh);
        if(getLineCount() <= 1)
            setVisibility(VISIBLE);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.d("AutoResizeTextView", "onLayout" + left + " " + top + " " + right + " " + bottom + " " + getLineCount());

        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
