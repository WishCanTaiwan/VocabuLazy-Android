package com.wishcan.www.vocabulazy.main.exam.fragment;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.VLApplication;
import com.wishcan.www.vocabulazy.main.MainActivity;
import com.wishcan.www.vocabulazy.main.exam.view.ExamLessonView;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Lesson;
import com.wishcan.www.vocabulazy.widget.ErrorView;
import com.wishcan.www.vocabulazy.widget.LessonView;

import java.util.ArrayList;
import java.util.LinkedList;

public class ExamLessonFragment extends Fragment implements LessonView.OnLessonClickListener {

    public static final String BOOK_INDEX_STR = "BOOK_INDEX_STR";
    private Database wDatabase;
    private int mBookIndex;
    private int mLessonIndex;

    public ExamLessonFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VLApplication vlApplication = (VLApplication) getActivity().getApplication();
        wDatabase = vlApplication.getDatabase();
        mBookIndex = getArguments() == null ? 0 : getArguments().getInt(BOOK_INDEX_STR);
        mLessonIndex = -1;
        ((MainActivity)getActivity()).switchActionBarStr(MainActivity.FRAGMENT_FLOW.GO, "Book " + mBookIndex);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(wDatabase == null) {
            VLApplication vlApplication = (VLApplication) getActivity().getApplication();
            wDatabase = vlApplication.getDatabase();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ExamLessonView examLessonView = new ExamLessonView(getActivity());
        ArrayList<Lesson> lessons = (wDatabase == null) ? null : wDatabase.getLessonsByBook(mBookIndex);
        LinkedList<Integer> lessonIntegers = new LinkedList<>();
        examLessonView.setOnLessonClickListener(this);
        if(lessons != null)
            for(int i = 0; i < lessons.size(); i++)
                lessonIntegers.add(i + 1);
        else
            return new ErrorView(getActivity()).setErrorMsg("get lesson failed");

        examLessonView.refreshView(lessonIntegers.size(), lessonIntegers);
        return examLessonView;
    }

    private void goExamFragment(int lessonIndex){
        Bundle args = new Bundle();
        args.putInt(ExamFragment.ARG_BOOK_INDEX, mBookIndex);
        args.putInt(ExamFragment.ARG_LESSON_INDEX, lessonIndex);
        ((MainActivity) getActivity()).goFragment(ExamFragment.class, args, "ExamFragment", "ExamLessonFragment");
    }

    @Override
    public void onLessonClick(int lesson) {
        ArrayList<Integer> contentIDs;

        mLessonIndex = lesson;
        contentIDs = wDatabase.getContentIDs(mBookIndex, mLessonIndex);
        if (contentIDs.size() >= 4) {
            goExamFragment(lesson);
        }

    }
}
