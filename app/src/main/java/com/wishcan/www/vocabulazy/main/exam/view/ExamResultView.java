package com.wishcan.www.vocabulazy.main.exam.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wishcan.www.vocabulazy.R;

import org.w3c.dom.Text;

/**
 * Created by swallow on 2016/1/23.
 */
public class ExamResultView extends LinearLayout {

    public interface OnTryItemClickListener{
        void onTryAgainClick();
        void onTryOtherClick();
    }

    private static final int VIEW_RES_ID = R.layout.view_exam_result;
    private static final int CORRECT_COUNT_VIEW_RES_ID = R.id.exam_correct_count;
    private static final int CORRECT_RATIO_VIEW_RES_ID = R.id.exam_correct_ratio;
    private static final int TRY_AGAIN_VIEW_RES_ID = R.id.exam_try_again;
    private static final int TRY_OTHER_VIEW_RES_ID = R.id.exam_try_other;

    private View mView;
    private OnTryItemClickListener mOnTryItemClickListener;
    
    public ExamResultView(Context context) {
        this(context, null, 0, 0.0f);
    }

    public ExamResultView(Context context, AttributeSet attrs, int correctCount, float correctRatio) {
        super(context, attrs);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mView = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(VIEW_RES_ID, null);
        mView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(mView);
        setCorrectCountAndRatio(correctCount, correctRatio);
        registerClickListener();
    }

    public void setOnTryItemClickListener(OnTryItemClickListener listener) {
        mOnTryItemClickListener = listener;
    }

    private void setCorrectCountAndRatio(int count, float ratio) {
        View correctCountTextView = mView.findViewById(CORRECT_COUNT_VIEW_RES_ID);
        View correctRatioTextView = mView.findViewById(CORRECT_RATIO_VIEW_RES_ID);
        ((TextView)correctCountTextView).setText(String.valueOf(count));
        ((TextView)correctRatioTextView).setText(String.valueOf((int) (ratio * 100)));
    }

    private void registerClickListener() {
        mView.findViewById(TRY_AGAIN_VIEW_RES_ID).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnTryItemClickListener != null)
                    mOnTryItemClickListener.onTryAgainClick();
            }
        });
        mView.findViewById(TRY_OTHER_VIEW_RES_ID).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnTryItemClickListener != null)
                    mOnTryItemClickListener.onTryOtherClick();
            }
        });
    }
}
