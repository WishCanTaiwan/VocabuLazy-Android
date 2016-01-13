package com.wishcan.www.vocabulazy.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wishcan.www.vocabulazy.main.MainActivity;
import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.view.adapter.LinkedListPagerAdapter;

import java.util.LinkedList;

/**
 * Created by swallow on 2015/9/20.
 */
abstract public class SlideBackViewPager extends ViewPager {

    abstract public ViewGroup getMainPage();
    abstract public boolean enableSlideBack();

    private static final int DEFAULT_BACKGROUND_COLOR = R.color.default_transparent_background;

    public SlideBackViewPager(Context context) {
        this(context, null);
    }

    public SlideBackViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        Context mContext;
        LinearLayout mEmptyLayoutForNavigateUp;
        ViewGroup mMainView;
        LinkedList<ViewGroup> ll;
        LinkedListPagerAdapter mAdapter;


        mContext = context;
        mEmptyLayoutForNavigateUp = new LinearLayout(mContext);
        mEmptyLayoutForNavigateUp.setBackgroundColor(ContextCompat.getColor(mContext, DEFAULT_BACKGROUND_COLOR));
        mMainView = getMainPage();

        ll = new LinkedList<>();

        if(!enableSlideBack()){
            ll.add(mMainView);
            mAdapter = new LinkedListPagerAdapter(ll);
            setAdapter(mAdapter);
            setCurrentItem(0);
        }
        else{
            ll.add(mEmptyLayoutForNavigateUp);
            ll.add(mMainView);
            mAdapter = new LinkedListPagerAdapter(ll);
            setAdapter(mAdapter);

            addOnPageChangeListener(new OnPageChangeToNavigateUpListener());
            setCurrentItem(1);
        }

    }

    private class OnPageChangeToNavigateUpListener implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0)
                ((MainActivity) getContext()).onBackPressed();
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
}
