package wishcantw.vocabulazy.exam.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.analytics.Analytics;
import wishcantw.vocabulazy.exam.view.ExamResultView;
import wishcantw.vocabulazy.ga.GABaseFragment;

/**
 * Created by SwallowChen on 9/7/16.
 */
public class ExamResultFragment extends GABaseFragment implements ExamResultView.OnExamResultEventListener {

    @Override
    protected String getGALabel() {
        return Analytics.ScreenName.RESULT;
    }

    public interface OnExamResultDoneListener {
        void onExamTryAgain();
        void onExamTryOther();
    }

    private static final int LAYOUT_RES_ID = R.layout.view_exam_result;

    private static final String ARG_COUNT = "correctCount";
    private static final String ARG_RATIO = "correctRatio";

    private ExamResultView mExamResultView;

    private OnExamResultDoneListener mOnExamResultDoneListener;

    private int mCorrectCount, mCorrectRatio;

    public static ExamResultFragment newInstance(int correctCount, int correctRatio) {
        ExamResultFragment fragment = new ExamResultFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COUNT, correctCount);
        args.putInt(ARG_RATIO, correctRatio);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCorrectCount = getArguments().getInt(ARG_COUNT);
            mCorrectRatio = getArguments().getInt(ARG_RATIO);
        } else {
            mCorrectCount = mCorrectRatio = 0;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mExamResultView == null) {
            mExamResultView = (ExamResultView) inflater.inflate(LAYOUT_RES_ID, container, false);
        }

        mExamResultView.setCorrectCount(mCorrectCount);
        mExamResultView.setCorrectRatio(mCorrectRatio);
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
