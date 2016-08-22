package com.wishcan.www.vocabulazy.mainmenu.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by allencheng07 on 2016/8/16.
 */
public class MainMenuViewPager extends ViewPager {

    public MainMenuViewPager(Context context) {
        super(context);
    }

    public MainMenuViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
//            return true;
//        }
//        return super.onInterceptTouchEvent(ev);
//    }
}
