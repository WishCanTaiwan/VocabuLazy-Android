package com.wishcan.www.vocabulazy.view.exam;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.wishcan.www.vocabulazy.MainActivity;
import com.wishcan.www.vocabulazy.view.adapter.LinkedListPagerAdapter;

import java.util.LinkedList;

import com.wishcan.www.vocabulazy.view.books.BooksGridView;

/**
 * Created by swallow on 2015/12/12.
 */
public class ExamBooksViewPager extends ViewPager {

    private Context mContext;

    private LinkedListPagerAdapter mAdapter;

    private LinkedList<ViewGroup> ll;

    private LinearLayout mEmptyLayoutForNavigateUp;

    private BooksGridView mExamBooksGridView;

    public ExamBooksViewPager(Context context) {
        this(context, null);
    }

    public ExamBooksViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        ll = new LinkedList<>();

        mEmptyLayoutForNavigateUp = new LinearLayout(mContext);
        mEmptyLayoutForNavigateUp.setBackgroundColor(Color.argb(0, 0, 0, 0));

        mExamBooksGridView = setMainPage();

        ll.add(mEmptyLayoutForNavigateUp);
        ll.add(mExamBooksGridView);

        mAdapter = new LinkedListPagerAdapter(ll);
        setAdapter(mAdapter);
        addOnPageChangeListener(new OnPageChangeToNavigateUpListener());

        setCurrentItem(1);
    }

    public BooksGridView getExamBooksGridView(){ return mExamBooksGridView; }

    private BooksGridView setMainPage(){ return new BooksGridView(mContext); }

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
