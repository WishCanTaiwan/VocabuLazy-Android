package com.wishcan.www.vocabulazy.exam.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.application.VLApplication;
import com.wishcan.www.vocabulazy.exam.model.ExamModel;
import com.wishcan.www.vocabulazy.exam.view.ExamAnswerView;
import com.wishcan.www.vocabulazy.exam.view.ExamView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by SwallowChen on 8/25/16.
 */
public class ExamFragment extends Fragment implements ExamView.ExamEventListener {
    public static final String ARG_BOOK_INDEX = "bookIndex";
    public static final String ARG_LESSON_INDEX = "lessonIndex";

    private static final int LAYOUT_RES_ID = R.layout.view_exam;

    private ExamView mExamView;
    private ExamModel mExamModel;
    private ExamModel.PuzzleSetter mPuzzleSetter;

    private int mCurrentBookIndex, mCurrentLessonIndex;

    public static ExamFragment newInstance() {
        ExamFragment fragment = new ExamFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurrentBookIndex = getArguments().getInt(ARG_BOOK_INDEX);
            mCurrentLessonIndex = getArguments().getInt(ARG_LESSON_INDEX);
        } else {
            mCurrentBookIndex = mCurrentLessonIndex = 0;
        }
        mExamModel = new ExamModel((VLApplication) getActivity().getApplication());
        mPuzzleSetter = mExamModel.createPuzzleSetter(mCurrentBookIndex, mCurrentLessonIndex);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mExamView == null) {
            mExamView = (ExamView) inflater.inflate(LAYOUT_RES_ID, container, false);
        }

        mExamView.setExamEventListener(this);
        return mExamView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /** NOTE: must call getANewQuestion to update question index */
        HashMap<Integer, ArrayList<String>> questionArrayList = mPuzzleSetter.getANewQuestion();
        mExamView.initContent(mPuzzleSetter.getCurrentQuestionIndex(), mPuzzleSetter.getTotalQuestionNum());
        mExamView.setContent(questionArrayList);
    }

    @Override
    public void onExamAnswerClick(int index) {
        /** index start from 1 to 4 */
        int correctIndex = mPuzzleSetter.checkAnswer(index);
        ArrayList<Integer> stateList = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            if (i == correctIndex) {
                stateList.add(ExamAnswerView.IDX_EXAM_ANSWER_CORRECT);
            } else if (i == index) {
                stateList.add(ExamAnswerView.IDX_EXAM_ANSWER_WRONG);
            } else {
                stateList.add(ExamAnswerView.IDX_EXAM_ANSWER);
            }
        }
        mExamView.setExamAnswerStates(stateList);
    }

    @Override
    public void onNextIconClick() {
        /** NOTE: must call getANewQuestion to update question index */
        HashMap<Integer, ArrayList<String>> questionArrayList = mPuzzleSetter.getANewQuestion();
        mExamView.updateExamProgressBar(mPuzzleSetter.getCurrentQuestionIndex());
        mExamView.setExamAnswerStates(0, 0, 0, 0);
        mExamView.setContent(questionArrayList);
    }
}
