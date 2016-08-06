package com.wishcan.www.vocabulazy.main.exam.fragment;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.VLApplication;
import com.wishcan.www.vocabulazy.main.MainActivity;
import com.wishcan.www.vocabulazy.main.exam.model.ExamModel;
import com.wishcan.www.vocabulazy.main.exam.view.ExamLessonView;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Lesson;
import com.wishcan.www.vocabulazy.widget.ErrorView;
import com.wishcan.www.vocabulazy.widget.LessonView;

import java.util.ArrayList;
import java.util.LinkedList;

public class ExamLessonFragment extends ExamBaseFragment implements LessonView.OnLessonClickListener {

    public interface OnExamLessonClickListener {
        void onExamLessonClicked(int position);
    }

    public static final String TAG = "ExamLessonFragment";

    public static final String BOOK_INDEX_STR = "BOOK_INDEX_STR";

//    private ExamModel mExamModel;
    private ExamLessonView mExamLessonView;
    private OnExamLessonClickListener mOnExamLessonClickListener;

    private int mBookIndex;
    private int mLessonIndex;

    public static ExamLessonFragment newInstance() {
        ExamLessonFragment fragment = new ExamLessonFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ExamLessonFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Create");
        super.onCreate(savedInstanceState);
//        if (mExamModel == null)
//            mExamModel = new ExamModel((VLApplication) getActivity().getApplication());
//        mBookIndex = getArguments() == null ? 0 : getArguments().getInt(BOOK_INDEX_STR);
//        mLessonIndex = -1;
//        ((MainActivity)getActivity()).switchActionBarStr(MainActivity.FRAGMENT_FLOW.GO, mExamModel.getBookTitle(mBookIndex));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "Create View");
        if (mExamModel == null) mExamModel = new ExamModel((VLApplication) getActivity().getApplication());
        ArrayList<Lesson> lessons = mExamModel.getLessons(mBookIndex);
        if(lessons == null)
            return new ErrorView(getActivity()).setErrorMsg("get lesson failed");

        LinkedList<Integer> lessonIntegers = new LinkedList<>();
        for (int i = 0; i < lessons.size(); i++)
            lessonIntegers.add(i + 1);

        if (mExamLessonView == null)
            mExamLessonView = new ExamLessonView(getActivity());
        mExamLessonView.setOnLessonClickListener(this);
        mExamLessonView.refreshView(lessonIntegers.size(), lessonIntegers);
        return mExamLessonView;
    }

    @Override
    public void onResume() {
        Log.d(TAG, "Resume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "Pause");
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Destroy");
        super.onDestroy();
    }

    public void addOnExamLessonClickListener(OnExamLessonClickListener listener) {
        mOnExamLessonClickListener = listener;
    }

    public void setExamBook(int bookIndex) {
        mBookIndex = bookIndex;
    }

//    private void goExamFragment(int lessonIndex){
//        Bundle args = new Bundle();
//        args.putInt(ExamFragment.ARG_BOOK_INDEX, mBookIndex);
//        args.putInt(ExamFragment.ARG_LESSON_INDEX, lessonIndex);
//        ((MainActivity) getActivity()).goFragment(ExamFragment.class, args, "ExamFragment", "ExamLessonFragment");
//    }

    @Override
    public void onLessonClick(int lesson) {
        mOnExamLessonClickListener.onExamLessonClicked(lesson);
//        ArrayList<Integer> contentIDs;
//
//        mLessonIndex = lesson;
//        contentIDs = mExamModel.getContent(mBookIndex, mLessonIndex);
//        if (contentIDs.size() >= 4) {
//            goExamFragment(lesson);
//        }

    }
}
