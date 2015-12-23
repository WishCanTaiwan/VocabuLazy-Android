package com.wishcan.www.vocabulazy.view.exam;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wishcan.www.vocabulazy.MainActivity;
import com.wishcan.www.vocabulazy.view.adapter.LinkedListPagerAdapter;
import com.wishcan.www.vocabulazy.view.books.BooksGridView;
import com.wishcan.www.vocabulazy.view.notes.NotesListView;

import java.util.LinkedList;

/**
 * Created by swallow on 2015/12/17.
 */
public class ExamNoteViewPager extends ViewPager {

    private Context mContext;

    private LinkedListPagerAdapter mAdapter;

    private LinkedList<ViewGroup> ll;

    private LinearLayout mEmptyLayoutForNavigateUp;

    private NotesListView mExamNotesListView;

    public ExamNoteViewPager(Context context) {
        this(context, null);
    }

    public ExamNoteViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        ll = new LinkedList<>();

        mEmptyLayoutForNavigateUp = new LinearLayout(mContext);
        mEmptyLayoutForNavigateUp.setBackgroundColor(Color.argb(0, 0, 0, 0));

        mExamNotesListView = setMainPage();

        ll.add(mEmptyLayoutForNavigateUp);
        ll.add(mExamNotesListView);

        mAdapter = new LinkedListPagerAdapter(ll);
        setAdapter(mAdapter);
        addOnPageChangeListener(new OnPageChangeToNavigateUpListener());

        setCurrentItem(1);

    }

    public NotesListView getExamNotesListView(){ return mExamNotesListView; }

    private NotesListView setMainPage(){
        NotesListView view = new NotesListView(mContext);
        view.setEnableEtcFunction(false);
        return view;
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
