package com.wishcan.www.vocabulazy.mainmenu.exam.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class ExamIndexViewPager extends ViewPager {

    public static final String TAG = "ExamIndexViewPager";

    public ExamIndexViewPager(Context context) {
        super(context);
    }

    public ExamIndexViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d(TAG, "Intercept Touch [" + ev.getAction() + "]");
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }
}
