package com.wishcan.www.vocabulazy.exam.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wishcan.www.vocabulazy.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by SwallowChen on 8/25/16.
 */
public class ExamView extends LinearLayout {
    public interface ExamEventListener {
        void onExamAnswerClick(int index);
        void onNextIconClick();
    }

    private static final int PROGRESSBAR_RES_ID = R.id.exam_progressbar;

    private static final int VIEW_QUESTION_RES_ID = R.id.exam_question;
    private static final int VIEW_ANSWER_1_RES_ID = R.id.exam_answer_1;
    private static final int VIEW_ANSWER_2_RES_ID = R.id.exam_answer_2;
    private static final int VIEW_ANSWER_3_RES_ID = R.id.exam_answer_3;
    private static final int VIEW_ANSWER_4_RES_ID = R.id.exam_answer_4;

    private static final int VIEW_NEXT_ICON_RES_ID = R.id.exam_next_icon;

    private ProgressBar mExamProgressBar;
    private TextView mExamQuestion;
    private ExamAnswerView mExamAnswer1, mExamAnswer2, mExamAnswer3, mExamAnswer4;
    private LinearLayout mNextIcon;
    private ExamAnswerView EXAM_ANSWER_VIEW_s[] = new ExamAnswerView[5];

    private ExamEventListener mExamEventListener;

    public ExamView(Context context) {
        this(context, null);
    }

    public ExamView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mExamProgressBar = (ProgressBar) findViewById(PROGRESSBAR_RES_ID);
        mExamQuestion = (TextView) findViewById(VIEW_QUESTION_RES_ID);
        EXAM_ANSWER_VIEW_s[1] = mExamAnswer1 = (ExamAnswerView) findViewById(VIEW_ANSWER_1_RES_ID);
        EXAM_ANSWER_VIEW_s[2] = mExamAnswer2 = (ExamAnswerView) findViewById(VIEW_ANSWER_2_RES_ID);
        EXAM_ANSWER_VIEW_s[3] = mExamAnswer3 = (ExamAnswerView) findViewById(VIEW_ANSWER_3_RES_ID);
        EXAM_ANSWER_VIEW_s[4] = mExamAnswer4 = (ExamAnswerView) findViewById(VIEW_ANSWER_4_RES_ID);
        mNextIcon = (LinearLayout) findViewById(VIEW_NEXT_ICON_RES_ID);
        registerEventListener();
    }

    public void setExamEventListener(ExamEventListener listener) {
        mExamEventListener = listener;
    }

    public void initContent(int progressBarInitValue, int progressBarTotalValue) {
        mExamProgressBar.setMax(progressBarTotalValue);
        mExamProgressBar.setProgress(progressBarInitValue);
    }

    public void setContent(HashMap<Integer, ArrayList<String>> map) {
        /** Question put in 0 */
        ArrayList<String> questionStrList = map.get(0);
        mExamQuestion.setText(questionStrList.get(0));
        for (int i = 1; i < EXAM_ANSWER_VIEW_s.length; i++) {
            /** answer put in 1 ~ 4 */
            ArrayList<String> answerStrList = map.get(i);
            ExamAnswerView examAnswerView = EXAM_ANSWER_VIEW_s[i];
            /** 0 is spell, 1 is chinese */
            examAnswerView.setExamAnswerString(answerStrList.get(1));
        }
    }

    public int updateExamProgressBar(int updateValue) {
        mExamProgressBar.setProgress(updateValue);
        return updateValue;
    }

    public boolean setExamAnswerStates(ArrayList<Integer> updateValues) {
        if (updateValues.size() != 4) {
            return false;
        }
        /** i is used to get ExamAnswer obj from EXAM_ANSWER_VIEW_s, j is for updateValues */
        for (int i = 1, j = 0; j < updateValues.size(); i++, j++) {
            ExamAnswerView examAnswerView = EXAM_ANSWER_VIEW_s[i];
            examAnswerView.setExamAnswerState(updateValues.get(j));
        }
        return true;
    }

    public boolean setExamAnswerStates(int ... updateValues) {
        if (updateValues.length != 4) {
            return false;
        }

        /** i is used to get ExamAnswer obj from EXAM_ANSWER_VIEW_s, j is for updateValues */
        for (int i = 1, j = 0; j < updateValues.length; i++, j++) {
            ExamAnswerView examAnswerView = EXAM_ANSWER_VIEW_s[i];
            examAnswerView.setExamAnswerState(updateValues[j]);
        }
        return true;
    }

    private void registerEventListener() {
        for (int i = 1; i < EXAM_ANSWER_VIEW_s.length; i++) {
            final int index = i;
            ExamAnswerView examAnswerView = EXAM_ANSWER_VIEW_s[i];
            examAnswerView.setExamAnswerEventListener(new ExamAnswerView.ExamAnswerEventListener() {
                @Override
                public void onClick() {
                    if (mExamEventListener != null) {
                        mExamEventListener.onExamAnswerClick(index);
                    }
                }
            });
        }

        mNextIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mExamEventListener != null) {
                    mExamEventListener.onNextIconClick();
                }
            }
        });
    }
}
