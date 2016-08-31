package com.wishcan.www.vocabulazy.exam.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.exam.fragment.ExamFragment;

/**
 * Created by SwallowChen on 8/25/16.
 */
public class ExamActivity extends AppCompatActivity {
    public static final String TAG = "ExamActivity";
    public static final String ARG_BOOK_INDEX = "arg-book-index";
    public static final String ARG_LESSON_INDEX = "arg-lesson-index";

    private static final int VIEW_RES_ID = R.layout.activity_exam;
    private static final int TOOLBAR_RES_ID = R.id.toolbar;

    private ExamFragment mExamFragment;

    private int mBookIndex;
    private int mLessonIndex;

    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Create");
        super.onCreate(savedInstanceState);

        mBookIndex = getIntent().getIntExtra(ARG_BOOK_INDEX, 1359);
        mLessonIndex = getIntent().getIntExtra(ARG_LESSON_INDEX, 1359);

        setContentView(VIEW_RES_ID);
        mToolbar = (Toolbar) findViewById(TOOLBAR_RES_ID);

        setSupportActionBar(mToolbar);
        setActionBarTitle("Book " + mBookIndex + " Lesson " + mLessonIndex);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        mExamFragment = (ExamFragment) fragment;
    }

    private void setActionBarTitle(String title) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
    }
}
