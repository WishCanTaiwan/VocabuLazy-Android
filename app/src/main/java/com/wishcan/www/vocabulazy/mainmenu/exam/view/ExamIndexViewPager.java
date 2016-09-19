package com.wishcan.www.vocabulazy.mainmenu.exam.view;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ExamIndexViewPager extends ViewPager implements NestedScrollingChild {

    public static final String TAG = "ExamIndexViewPager";

    public ExamIndexViewPager(Context context) {
        super(context);
    }

    public ExamIndexViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        Log.d(TAG, "intercept "+ev.getAction());
//        return true;
//    }

//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        Log.d(TAG, ""+ev.getAction());
//        return true;
//    }


}
