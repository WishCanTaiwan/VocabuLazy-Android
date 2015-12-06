package com.wishcan.www.vocabulazy.view.reading;


import android.app.ActionBar;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.MainActivity;
import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.storage.Book;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.Lesson;
import com.wishcan.www.vocabulazy.view.lessons.LessonsFragment;
import com.wishcan.www.vocabulazy.view.lessons.LessonsListView;
import com.wishcan.www.vocabulazy.view.lessons.LessonsViewPager;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReadingLessonFragment extends Fragment {

    private static final int DEFAULT_FRAGMENT_VIEW_RES_ID = R.layout.fragment_reading_lesson;

    public static final String ARG_BOOK_INDEX = "bookIndex";

    private ViewGroup mParentView;

    private ReadingLessonListView mListView;

    private Database mDatabase;

    private int mBookIndex;

    private Book mBook;

    private ArrayList<Lesson> mLessonLL;

    private String mPreviousTitle;

    public static ReadingLessonFragment newInstance(String previousTitle, int bookIndex, Database database) {
        ReadingLessonFragment fragment = new ReadingLessonFragment();
        Bundle args = new Bundle();
        args.putString(MainActivity.PREVIOUS_TITLE, previousTitle);
        args.putInt(ARG_BOOK_INDEX, bookIndex);
        args.putParcelable("database", database);
        fragment.setArguments(args);
        return fragment;
    }

    public ReadingLessonFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mPreviousTitle = getArguments().getString(MainActivity.PREVIOUS_TITLE);
            mBookIndex = getArguments().getInt(ARG_BOOK_INDEX);
            mDatabase = getArguments().getParcelable("database");
        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mParentView = (ViewGroup) inflater.inflate(DEFAULT_FRAGMENT_VIEW_RES_ID, container, false);
        mListView = (ReadingLessonListView)((ReadingLessonViewPager) mParentView.getChildAt(0)).getLessonsListView();


        MainActivity activity = (MainActivity) getActivity();
//        mDatabase = activity.getDatabase();

        // I want use the argument I get to find out how many Unit should be show in ListLiew
        mBook = mDatabase.getBooks().get(mBookIndex);
        mLessonLL = mDatabase.getLessonsByBook(mBookIndex);

        // This LinkedList is used to show how many unit this book has
        LinkedList<Integer> ll = new LinkedList<>();
        for (int i = 0; i < mLessonLL.size(); i++) {
            ll.add(i);
        }
        mListView.setAdapterByIntLL(ll);

        mListView.setOnLessonClickedListener(new LessonsListView.OnLessonClickedListener() {
            @Override
            public void onLessonClicked(int lesson) {
                ((MainActivity) getActivity()).goReadingMainFragment(mBookIndex, lesson);
            }
        });
        return mParentView;
    }


    @Override
    public void onStart() {
        super.onStart();
        ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            ((MainActivity) getActivity()).switchActionBarTitle(mBook.getName());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        MainActivity parentActivity = ((MainActivity) getActivity());
        parentActivity.setActionBarTitleWhenStop(parentActivity.getActionBarTitleTextView());
        parentActivity.switchActionBarTitle(mPreviousTitle);

    }

}
