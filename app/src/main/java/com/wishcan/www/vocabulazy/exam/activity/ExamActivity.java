package com.wishcan.www.vocabulazy.exam.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.exam.fragment.ExamFragment;
import com.wishcan.www.vocabulazy.exam.fragment.ExamResultFragment;

/**
 * Created by SwallowChen on 8/25/16.
 */
public class ExamActivity extends AppCompatActivity implements ExamFragment.OnExamDoneListener, ExamResultFragment.OnExamResultDoneListener {
    public static final String TAG = "ExamActivity";
    public static final String ARG_BOOK_INDEX = "arg-book-index";
    public static final String ARG_LESSON_INDEX = "arg-lesson-index";

    private static final int VIEW_RES_ID = R.layout.activity_exam;
    private static final int VIEW_MAIN_RES_ID = R.id.exam_fragment_container;
    private static final int TOOLBAR_RES_ID = R.id.toolbar;

    private ExamFragment mExamFragment;
    private ExamResultFragment mExamResultFragment;

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

        ExamFragment fragment = ExamFragment.newInstance(mBookIndex, mLessonIndex);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(ExamActivity.VIEW_MAIN_RES_ID, fragment, "ExamFragment");
        fragmentTransaction.commit();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof ExamFragment) {
            mExamFragment = (ExamFragment) fragment;
            mExamFragment.setOnExamDoneListener(this);
        } else if  (fragment instanceof ExamResultFragment) {
            mExamResultFragment = (ExamResultFragment) fragment;
            mExamResultFragment.setOnExamResultDoneListener(this);
        }
    }

    private void setActionBarTitle(String title) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
    }

    @Override
    public void onExamDone(int totalNumber, int correctNumber) {
        Log.d(TAG, "onExamDone" + correctNumber);
        int correctCount, correctRatio;
        correctCount = correctNumber;
        correctRatio = (int) ((correctNumber / (float)totalNumber) * 100);
        ExamResultFragment fragment = ExamResultFragment.newInstance(correctCount, correctRatio);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(ExamActivity.VIEW_MAIN_RES_ID, fragment, "ExamResultFragment");
        fragmentTransaction.commit();
    }

    @Override
    public void onExamTryAgain() {
        Log.d(TAG, "onExamTryAgain");
        ExamFragment fragment = ExamFragment.newInstance(mBookIndex, mLessonIndex);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(ExamActivity.VIEW_MAIN_RES_ID, fragment, "ExamFragment");
        fragmentTransaction.commit();
    }

    @Override
    public void onExamTryOther() {
        Log.d(TAG, "onExamTryOther");
        finish();
    }
}
