package com.wishcan.www.vocabulazy.exam.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wishcan.www.vocabulazy.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by SwallowChen on 8/25/16.
 */
public class ExamView extends LinearLayout implements FloatingActionButton.OnClickListener{

    /**
     * The listener contains all events of Exam's children
     * */
    public interface ExamEventListener {
        /**
         * The callback function when ExamAnswerView is clicked
         * @see ExamAnswerView
         * */
        void onExamAnswerClick(int index);

        /**
         * The callback function when NextIcon bar is clicked
         * */
        void onNextIconClick();
    }

    private static final int PROGRESSBAR_RES_ID = R.id.exam_progressbar;

    private static final int VIEW_QUESTION_RES_ID = R.id.exam_question;
    private static final int VIEW_ANSWER_1_RES_ID = R.id.exam_answer_1;
    private static final int VIEW_ANSWER_2_RES_ID = R.id.exam_answer_2;
    private static final int VIEW_ANSWER_3_RES_ID = R.id.exam_answer_3;
    private static final int VIEW_ANSWER_4_RES_ID = R.id.exam_answer_4;

    private static final int VIEW_NEXT_ICON_RES_ID = R.id.exam_next_fab;

    private ProgressBar mExamProgressBar;
    private TextView mExamQuestion;
    private ExamAnswerView mExamAnswer1, mExamAnswer2, mExamAnswer3, mExamAnswer4;
    private ExamAnswerView EXAM_ANSWER_VIEW_s[] = new ExamAnswerView[5];
    private FloatingActionButton mNextIconFab;

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

        mNextIconFab = (FloatingActionButton) findViewById(VIEW_NEXT_ICON_RES_ID);

        registerEventListener();
    }

    /**
     * Hook the callback function for ExamView
     * @param listener the callback function
     * */
    public void setExamEventListener(ExamEventListener listener) {
        mExamEventListener = listener;
    }

    /**
     * Fill in the init status of bar and clean all ExamAnswerView
     * @param progressBarInitValue the init value of progress bar
     * @param progressBarTotalValue the total value of progress bar
     * @see ProgressBar
     * @see ExamAnswerView
     * */
    public void initContent(int progressBarInitValue, int progressBarTotalValue) {
        mExamProgressBar.setMax(progressBarTotalValue * 10);
        mExamProgressBar.setProgress(progressBarInitValue * 10);
    }

    /**
     * Fill in the ExamAnswerView with input map
     * @param map contains the both Question and Answer information
     * */
    public void setContent(HashMap<Integer, ArrayList<String>> map) {
        /** Question put in 0 */
        ArrayList<String> questionStrList = map.get(0);
        mExamQuestion.setText(questionStrList.get(0));
        for (int i = 1; i < EXAM_ANSWER_VIEW_s.length; i++) {
            /** answer put in 1 ~ 4 */
            ArrayList<String> answerStrList = map.get(i);
            ExamAnswerView examAnswerView = EXAM_ANSWER_VIEW_s[i];
            /** map.get(0) is spell, map.get(1) is chinese */
            examAnswerView.setExamAnswerString(answerStrList.get(1));
        }
    }

    /**
     * Update the progress bar on top of ExamView
     * @param updateValue the desired value that want to be shown
     * */
    public int updateExamProgressBar(int updateValue) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ObjectAnimator animation = ObjectAnimator.ofInt(mExamProgressBar, "progress", updateValue * 10);
            animation.setDuration(300); // 0.5 second
            animation.setInterpolator(new DecelerateInterpolator());
            animation.start();
        } else {
            mExamProgressBar.setProgress(updateValue);
        }
        return updateValue;
    }

    /**
     * There are 4 ExamAnswerView in ExamView, the input parameters will update these 4
     * ExamAnswerView according to parameters
     * @param updateValues size must equal to 4
     * @see ExamAnswerView
     * */
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

    /**
     * There are 4 ExamAnswerView in ExamView, the input parameters will update these 4
     * ExamAnswerView according to parameters
     * @param updateValues length must equal to 4
     * */
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
        mNextIconFab.setOnClickListener(this);
    }

    public void hideNextIcon() {
        mNextIconFab.hide();
    }

    public void showNextIcon() {
        mNextIconFab.show();
    }

    /**
     * FloatingActionButtons' callback
     * */
    @Override
    public void onClick(View view) {
        if (mExamEventListener != null) {
            mExamEventListener.onNextIconClick();
        }
    }
}
