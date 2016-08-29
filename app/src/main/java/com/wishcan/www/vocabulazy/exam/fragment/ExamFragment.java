package com.wishcan.www.vocabulazy.exam.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.exam.view.ExamView;

/**
 * Created by SwallowChen on 8/25/16.
 */
public class ExamFragment extends Fragment {

    private static final int LAYOUT_RES_ID = R.layout.view_exam;

    private ExamView mExamView;

    public static ExamFragment newInstance() {
        ExamFragment fragment = new ExamFragment();
        return fragment;
    }

    public ExamFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mExamView == null) {
            mExamView = (ExamView) inflater.inflate(LAYOUT_RES_ID, container, false);
        }

        /** TODO: for testing only, need a callback */
        mExamView.updateExamProgressBar(45);
        return mExamView;
    }
}
