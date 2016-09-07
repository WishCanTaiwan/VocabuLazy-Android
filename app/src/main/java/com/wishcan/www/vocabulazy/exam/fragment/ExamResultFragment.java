package com.wishcan.www.vocabulazy.exam.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.exam.view.ExamResultView;

/**
 * Created by SwallowChen on 9/7/16.
 */
public class ExamResultFragment extends Fragment implements ExamResultView.OnExamResultEventListener {

    public interface OnExamResultDoneListener {
        void onExamTryAgain();
        void onExamTryOther();
    }

    private static final int LAYOUT_RES_ID = R.layout.view_exam_result;


    private ExamResultView mExamResultView;

    private OnExamResultDoneListener mOnExamResultDoneListener;

    public static ExamFragment newInstance() {
        ExamFragment fragment = new ExamFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mExamResultView == null) {
            mExamResultView = (ExamResultView) inflater.inflate(LAYOUT_RES_ID, container, false);
        }

        mExamResultView.setOnExamResultEventListener(this);
        return mExamResultView;
    }

    public void setOnExamResultDoneListener(OnExamResultDoneListener listener) {
        mOnExamResultDoneListener = listener;
    }

    @Override
    public void onTryAgainEvent() {
        if (mOnExamResultDoneListener != null) {
            mOnExamResultDoneListener.onExamTryAgain();
        }
    }

    @Override
    public void onTryOtherEvent() {
        if (mOnExamResultDoneListener != null) {
            mOnExamResultDoneListener.onExamTryOther();
        }
    }
}
