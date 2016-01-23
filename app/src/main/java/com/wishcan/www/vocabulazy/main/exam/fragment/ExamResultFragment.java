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
public class ExamResultFragment extends Fragment {

    private static final String BUNDLE_RATIO_STRING = "BUNDLE_RATIO";
    private static final String BUNDLE_COUNT_STRING = "BUNDLE_COUNT";

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
        final ExamResultFragment fragment = this;   // This is used for removing the fragment self
        ExamResultView fragmentView;

        // Inflate the layout for this fragment
        fragmentView = new ExamResultView(getActivity(), null, mCorrectCount, mRatio);
        fragmentView.setOnTryItemClickListener(new ExamResultView.OnTryItemClickListener() {
            @Override
            public void onTryAgainClick() {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(MainActivity.ANIM_ENTER_RES_ID, MainActivity.ANIM_EXIT_RES_ID);
                transaction.remove(fragment);
                transaction.commit();
                getActivity().onBackPressed();
            }

            @Override
            public void onTryOtherClick() {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(MainActivity.ANIM_ENTER_RES_ID, MainActivity.ANIM_EXIT_RES_ID);
                transaction.remove(fragment);
                transaction.commit();
                ((ExamFragment) getFragmentManager().findFragmentByTag("ExamFragment")).restartExam();
            }
        });

        return fragmentView;
    }


}
