package com.wishcan.www.vocabulazy.main.exam.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.main.MainActivity;
import com.wishcan.www.vocabulazy.main.exam.view.ExamResultView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExamResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExamResultFragment extends ExamBaseFragment implements ExamResultView.OnTryItemClickListener{

    public static final String TAG = "E.RESULT";

    public static final String BUNDLE_RATIO_STRING = "BUNDLE_RATIO";
    public static final String BUNDLE_COUNT_STRING = "BUNDLE_COUNT";

    private float mRatio;
    private int mCorrectCount;

    public static ExamResultFragment newInstance(float ratio, int correctCount) {
        ExamResultFragment fragment = new ExamResultFragment();
        Bundle args = new Bundle();
        args.putFloat(BUNDLE_RATIO_STRING, ratio);
        args.putInt(BUNDLE_COUNT_STRING, correctCount);
        fragment.setArguments(args);
        return fragment;
    }

    public ExamResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRatio = getArguments().getFloat(BUNDLE_RATIO_STRING);
            mCorrectCount = getArguments().getInt(BUNDLE_COUNT_STRING);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ExamResultView fragmentView;

        // Inflate the layout for this fragment
        fragmentView = new ExamResultView(getActivity(), null, mCorrectCount, mRatio);
        fragmentView.setOnTryItemClickListener(this);

        return fragmentView;
    }

    @Override
    protected String getNameAsGaLabel() {
        return TAG;
    }

    /**--------------- Implements ExamView.OnTryItemClickListener ---------------------**/
    @Override
    public void onTryOtherClick() {
        getActivity().onBackPressed();
        getActivity().onBackPressed();
    }

    @Override
    public void onTryAgainClick() {
        ExamResultFragment fragment = this;   // This is used for removing the fragment self
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(MainActivity.ANIM_ENTER_RES_ID, MainActivity.ANIM_EXIT_RES_ID);
        transaction.remove(fragment);
        transaction.commit();
        ((ExamFragment) getFragmentManager().findFragmentByTag("ExamFragment")).restartExam();
    }

}
