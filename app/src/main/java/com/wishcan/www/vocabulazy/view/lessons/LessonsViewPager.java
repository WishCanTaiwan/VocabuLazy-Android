package com.wishcan.www.vocabulazy.view.lessons;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.LinkedList;

import com.wishcan.www.vocabulazy.MainActivity;
import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.view.adapter.LinkedListPagerAdapter;

/**
 * Created by swallow on 2015/9/20.
 */
public class LessonsViewPager extends ViewPager {

    protected static final int DEFAULT_BACKGROUND_COLOR = R.color.default_color_white;

    private LinkedListPagerAdapter mAdapter;

    private LinkedList<ViewGroup> ll;

    private LinearLayout mEmptyLayoutForNavigateUp;

    private ViewGroup mLessonsListView;

    private Context mContext;

    public LessonsViewPager(Context context) {
        super(context);
    }

    public LessonsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        ll = new LinkedList<>();

        mEmptyLayoutForNavigateUp = new LinearLayout(mContext);
        mEmptyLayoutForNavigateUp.setBackgroundColor(Color.argb(0, 0, 0, 0));

        mLessonsListView = setMainPage();

        ll.add(mEmptyLayoutForNavigateUp);
        ll.add(mLessonsListView);

        mAdapter = new LinkedListPagerAdapter(ll);
        setAdapter(mAdapter);
        addOnPageChangeListener(new OnPageChangeToNavigateUpListener());

        setCurrentItem(1);
    }

    public ViewGroup setMainPage(){

        return mLessonsListView = new LessonsListView(mContext);

    }

    public ViewGroup getLessonsListView() {
        return mLessonsListView;
    }

    private class OnPageChangeToNavigateUpListener implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            Log.d("onPageSelected", " " + position);
            if (position == 0)
                ((MainActivity) getContext()).onBackPressed();
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
}
