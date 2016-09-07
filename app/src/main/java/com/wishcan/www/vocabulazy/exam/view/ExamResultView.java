package com.wishcan.www.vocabulazy.exam.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.wishcan.www.vocabulazy.R;

/**
 * Created by SwallowChen on 9/7/16.
 */
public class ExamResultView extends LinearLayout{

    public interface OnExamResultEventListener {
        void onTryAgainEvent();
        void onTryOtherEvent();
    }

    private static final int VIEW_TRY_AGAIN_RES_ID = R.id.exam_result_try_again;
    private static final int VIEW_TRY_OTHER_RES_ID = R.id.exam_result_try_other;

    private ExamResultAnswerView mExamResultAnswerTryAgain, mExamResultAnswerTryOther;

    private OnExamResultEventListener mOnExamResultEventListener;

    public ExamResultView(Context context) {
        super(context);
    }

    public ExamResultView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mExamResultAnswerTryAgain = (ExamResultAnswerView) findViewById(VIEW_TRY_AGAIN_RES_ID);
        mExamResultAnswerTryOther = (ExamResultAnswerView) findViewById(VIEW_TRY_OTHER_RES_ID);

        registerEventListener();
    }

    public void setOnExamResultEventListener(OnExamResultEventListener listener) {
        mOnExamResultEventListener = listener;
    }

    private void registerEventListener() {
        mExamResultAnswerTryAgain.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnExamResultEventListener != null) {
                    mOnExamResultEventListener.onTryAgainEvent();
                }
            }
        });

        mExamResultAnswerTryOther.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnExamResultEventListener != null) {
                    mOnExamResultEventListener.onTryOtherEvent();
                }
            }
        });
    }
}
