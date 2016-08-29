package com.wishcan.www.vocabulazy.exam.view;

import android.content.Context;
import android.graphics.drawable.LevelListDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.wishcan.www.vocabulazy.R;

/**
 * Created by SwallowChen on 8/25/16.
 */
public class ExamView extends LinearLayout{

    private static final int PROGRESSBAR_RES_ID = R.id.exam_progressbar;

    private static final int ANSWER_1_RES_ID = R.id.exam_answer_1;
    private static final int ANSWER_2_RES_ID = R.id.exam_answer_2;
    private static final int ANSWER_3_RES_ID = R.id.exam_answer_3;
    private static final int ANSWER_4_RES_ID = R.id.exam_answer_4;

    private ProgressBar mExamProgressBar;

    private LinearLayout mAnswer1, mAnswer2, mAnswer3, mAnswer4;

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
        mAnswer1 = (LinearLayout) findViewById(ANSWER_1_RES_ID);
        mAnswer2 = (LinearLayout) findViewById(ANSWER_2_RES_ID);
        mAnswer3 = (LinearLayout) findViewById(ANSWER_3_RES_ID);
        mAnswer4 = (LinearLayout) findViewById(ANSWER_4_RES_ID);

        registerEventListener();
    }

    public int updateExamProgressBar(int updateValue) {
        mExamProgressBar.setProgress(updateValue);
        return 0;
    }

    private void registerEventListener() {
        LevelListDrawable drawable1 = (LevelListDrawable) mAnswer1.getBackground();
        drawable1.setLevel(2);
        mAnswer1.setBackground(drawable1);
        mAnswer1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mAnswer2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mAnswer3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mAnswer4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        LevelListDrawable drawable3 = (LevelListDrawable) mAnswer3.getBackground();
        drawable3.setLevel(1);
        mAnswer3.setBackground(drawable3);
    }
}
