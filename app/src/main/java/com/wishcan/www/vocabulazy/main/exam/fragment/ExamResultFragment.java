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

    public interface OnExamTryAgainOrAnotherListener {
        void onExamTryAgain();
        void onExamTryAnother();
    }

//    public static final String BUNDLE_RATIO_STRING = "BUNDLE_RATIO";
//    public static final String BUNDLE_COUNT_STRING = "BUNDLE_COUNT";

    private float mRatio;
    private int mCorrectCount;

    private ExamResultView mExamResultView;
    private OnExamTryAgainOrAnotherListener mOnExamTryAgainOrAnotherListener;

    public static ExamResultFragment newInstance() {
        ExamResultFragment fragment = new ExamResultFragment();
        Bundle args = new Bundle();
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
//            mRatio = getArguments().getFloat(BUNDLE_RATIO_STRING);
//            mCorrectCount = getArguments().getInt(BUNDLE_COUNT_STRING);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        mExamResultView fragmentView;

        // Inflate the layout for this fragment
        if (mExamResultView == null)
            mExamResultView = new ExamResultView(getActivity(), null, mCorrectCount, mRatio);
        mExamResultView.setOnTryItemClickListener(this);

        return mExamResultView;
    }

    /**--------------- Implements ExamView.OnTryItemClickListener ---------------------**/
    @Override
    public void onTryOtherClick() {
        mOnExamTryAgainOrAnotherListener.onExamTryAnother();
//        getActivity().onBackPressed();
//        getActivity().onBackPressed();
    }

    @Override
    public void onTryAgainClick() {
        mOnExamTryAgainOrAnotherListener.onExamTryAgain();
//        ExamResultFragment fragment = this;   // This is used for removing the fragment self0
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(MainActivity.ANIM_ENTER_RES_ID, MainActivity.ANIM_EXIT_RES_ID);
//        transaction.remove(fragment);
//        transaction.commit();
//        ((ExamFragment) getFragmentManager().findFragmentByTag("ExamFragment")).restartExam();
    }

    public void setResult(float ratio, int count) {
        mRatio = ratio;
        mCorrectCount = count;
    }

    public void addOnExamTryAgainOrAnotherListener(OnExamTryAgainOrAnotherListener listener) {
        mOnExamTryAgainOrAnotherListener = listener;
    }
}
