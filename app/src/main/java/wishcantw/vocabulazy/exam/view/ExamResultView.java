package wishcantw.vocabulazy.exam.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import wishcantw.vocabulazy.R;

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
    private static final int VIEW_CORRECT_COUNT_RES_ID = R.id.exam_result_answer_correct_count_text;
    private static final int VIEW_CORRECT_RATIO_RES_ID = R.id.exam_result_answer_correct_ratio_text;

    private ExamResultAnswerView mExamResultAnswerTryAgain, mExamResultAnswerTryOther;
    private TextView mCorrectCountTextView, mCorrectRatioTextView;

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
        mCorrectCountTextView = (TextView) findViewById(VIEW_CORRECT_COUNT_RES_ID);
        mCorrectRatioTextView = (TextView) findViewById(VIEW_CORRECT_RATIO_RES_ID);

        registerEventListener();
    }

    public void setOnExamResultEventListener(OnExamResultEventListener listener) {
        mOnExamResultEventListener = listener;
    }

    /**
     * Setting the correct count text view
     * @param count the input to show the correct answer count
     * */
    public void setCorrectCount(int count) {
        mCorrectCountTextView.setText("答對 " + count + "題");
    }

    /**
     * Setting the correct ratio text view
     * @param ratio the input to show the correct ratio, should be an integer ratio %
     * */
    public void setCorrectRatio(int ratio) {
        mCorrectRatioTextView.setText("答對率 " + ratio + "%");
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
