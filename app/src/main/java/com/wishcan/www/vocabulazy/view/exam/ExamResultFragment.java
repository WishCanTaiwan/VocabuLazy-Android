package com.wishcan.www.vocabulazy.view.exam;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wishcan.www.vocabulazy.MainActivity;
import com.wishcan.www.vocabulazy.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExamResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExamResultFragment extends Fragment {

    private static final String BUNDLE_RATIO_STRING = "BUNDLE_RATIO";

    private static final String BUNDLE_COUNT_STRING = "BUNDLE_COUNT";

    private static final int CORRECT_COUNT_VIEW_RES_ID = R.id.exam_correct_count;

    private static final int CORRECT_RATIO_VIEW_RES_ID = R.id.exam_correct_ratio;

    private static final int TRY_AGAIN_VIEW_RES_ID = R.id.exam_try_again;

    private static final int TRY_OTHER_VIEW_RES_ID = R.id.exam_try_other;

    private String mPreviousTitle;

    private float mRatio;

    private int mCorrectCount;

    private View mFragmentView;

    private TextView mCorrectCountTextView;

    private TextView mCorrectRatioTextView;

    private View mTryAgainView;

    private View mTryOtherView;

    public static ExamResultFragment newInstance(String previousTitle, float ratio, int correctCount) {
        ExamResultFragment fragment = new ExamResultFragment();
        Bundle args = new Bundle();
        args.putString(MainActivity.PREVIOUS_TITLE, previousTitle);
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
            mPreviousTitle = getArguments().getString(MainActivity.PREVIOUS_TITLE);
            mRatio = getArguments().getFloat(BUNDLE_RATIO_STRING);
            mCorrectCount = getArguments().getInt(BUNDLE_COUNT_STRING);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ExamResultFragment fragment = this;   // This is used for removing the fragment self

        // Inflate the layout for this fragment
        mFragmentView = inflater.inflate(R.layout.fragment_exam_result, container, false);

        mCorrectCountTextView = (TextView) mFragmentView.findViewById(CORRECT_COUNT_VIEW_RES_ID);

        mCorrectRatioTextView = (TextView) mFragmentView.findViewById(CORRECT_RATIO_VIEW_RES_ID);

        mTryAgainView = mFragmentView.findViewById(TRY_AGAIN_VIEW_RES_ID);

        mTryOtherView = mFragmentView.findViewById(TRY_OTHER_VIEW_RES_ID);

        mCorrectCountTextView.setText(String.valueOf(mCorrectCount));

        mCorrectRatioTextView.setText( String.valueOf((int)(mRatio * 100)));


        mTryOtherView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /**
                 * The transaction of adding ExamResultFragment didn't call addToBackStack(),
                 * so we should remove current fragment by ourselves and call onNavigateUp() for
                 * popping out the top of transaction, which reverses the action,
                 * i.e. doing removes ExamFragment.
                 * */
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.fragment_translate_slide_from_right_to_center, R.anim.fragment_translate_slide_from_center_to_right);
                transaction.remove(fragment);
                transaction.commit();
                getActivity().onNavigateUp();
            }
        });

        mTryAgainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.fragment_translate_slide_from_right_to_center, R.anim.fragment_translate_slide_from_center_to_right);
                transaction.remove(fragment);
                transaction.commit();
                ((ExamFragment) getFragmentManager().findFragmentByTag("exammainfragment")).restartExam();
            }
        });

        return mFragmentView;
    }


}
