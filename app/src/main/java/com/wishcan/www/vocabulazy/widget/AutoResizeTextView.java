package com.wishcan.www.vocabulazy.widget;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.wishcan.www.vocabulazy.log.Logger;

/**
 * Created by swallow on 2016/3/6.
 */
public class AutoResizeTextView extends TextView {

    private float mInitTextSize;

    public AutoResizeTextView(Context context) {
        this(context, null);
    }

    public AutoResizeTextView(Context context, AttributeSet attr) {
        super(context, attr);
        setVisibility(INVISIBLE);
        mInitTextSize = getTextSize();
        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        if(viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    while(getLineCount() > 1) {
                        float textSize = getTextSize();
                        float density = getContext().getResources().getDisplayMetrics().density;
                        setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize - 1.0f*density);
                    }
                }
            });
        } else {
            Logger.d("AutoResizeTextView", "viewTreeObserver is not alive");
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        Logger.d("AutoResizeTextView", "onSizeChanged");
        super.onSizeChanged(w, h, oldw, oldh);
        if(getLineCount() <= 1)
            setVisibility(VISIBLE);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        Logger.d("AutoResizeTextView", "onLayout" + left + " " + top + " " + right + " " + bottom + " " + getLineCount());

        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void resetTextSize() {
        setTextSize(TypedValue.COMPLEX_UNIT_PX, mInitTextSize);
    }
}
