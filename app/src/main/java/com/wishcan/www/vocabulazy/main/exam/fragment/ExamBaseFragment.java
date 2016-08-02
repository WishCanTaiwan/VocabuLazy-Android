package com.wishcan.www.vocabulazy.main.exam.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.wishcan.www.vocabulazy.VLApplication;
import com.wishcan.www.vocabulazy.ga.GAExamFragment;
import com.wishcan.www.vocabulazy.main.exam.model.ExamModel;

/**
 * Created by allencheng07 on 2016/6/16.
 */
public class ExamBaseFragment extends GAExamFragment {
    protected ExamModel mExamModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mExamModel == null) mExamModel = new ExamModel((VLApplication) getActivity().getApplication());
    }
}
