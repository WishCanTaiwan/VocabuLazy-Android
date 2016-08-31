package com.wishcan.www.vocabulazy.exam.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.graphics.drawable.LevelListDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wishcan.www.vocabulazy.R;
/**
 * @author Swallow Chen
 * @since 2016/8/30
 */
public class ExamAnswerView extends LinearLayout {
    /**
     * The callback function of ExamAnswerView
     * */
    public interface ExamAnswerEventListener {
        /**
         * The callback when View is clicked
         * */
        void onClick();
    }
    
    private static final int VIEW_EXAM_ANSWER_IMG_RES_ID = R.id.exam_answer_image;
    private static final int VIEW_EXAM_ANSWER_TXT_RES_ID = R.id.exam_answer_text;
    
    private static final int STYLE_TXT_EXAM_ANSWER_RES_ID = R.style.AppTextStyle_Exam_Answer;
    private static final int STYLE_TXT_EXAM_ANSWER_CHOSEN_RES_ID = R.style.AppTextStyle_Exam_Answer_Chosen;
    private static final int STYLE_TXT_EXAM_ANSWER_CORRECT_RES_ID = R.style.AppTextStyle_Exam_Answer_Correct;
    private static final int STYLE_TXT_EXAM_ANSWER_WRONG_RES_ID = R.style.AppTextStyle_Exam_Answer_Wrong;
    
    private static final int IDX_DRAWABLE_BGD_EXAM_ANSWER_LV = 0x0;
    private static final int IDX_DRAWABLE_BGD_EXAM_ANSWER_CHOSEN_LV = 0x1;
    private static final int IDX_DRAWABLE_BGD_EXAM_ANSWER_CORRECT_LV = 0x2;
    private static final int IDX_DRAWABLE_BGD_EXAM_ANSWER_WRONG_LV = 0x1;  // WRONG background is the same with CHOSEN
    
    private static final int IDX_DRAWABLE_IMG_EXAM_ANSWER_LV = 0x0;
    private static final int IDX_DRAWABLE_IMG_EXAM_ANSWER_CHOSEN_LV = 0x0;
    private static final int IDX_DRAWABLE_IMG_EXAM_ANSWER_CORRECT_LV = 0x1;
    private static final int IDX_DRAWABLE_IMG_EXAM_ANSWER_WRONG_LV = 0x2;

    /**
     * There are four states for ExamAnswerView, each state correspond to one appearance
     * IDX_EXAM_ANSWER is the default state
     * */
    public static final int IDX_EXAM_ANSWER = 0x0;
    /**
     * IDX_EXAM_ANSWER_CHOSEN is the state when view is being pressed
     * */
    public static final int IDX_EXAM_ANSWER_CHOSEN = 0x1;
    /**
     * IDX_EAM_ANSWER_CORRECT is the state of ExamAnswerView showing correct
     * */
    public static final int IDX_EXAM_ANSWER_CORRECT = 0x2;
    /**
     * IDX_EAM_ANSWER_CORRECT is the state of ExamAnswerView showing wrong
     * */
    public static final int IDX_EXAM_ANSWER_WRONG = 0x3;

    private Context mContext;

    private ImageView mExamAnswerImageView;
    private TextView mExamAnswerTextView;
    
    private ExamAnswerEventListener mExamAnswerEventListener;

    public ExamAnswerView(Context context) {
        this(context, null);
    }

    public ExamAnswerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }
    
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mExamAnswerImageView = (ImageView) findViewById(VIEW_EXAM_ANSWER_IMG_RES_ID);
        mExamAnswerTextView = (TextView) findViewById(VIEW_EXAM_ANSWER_TXT_RES_ID);
        
        registerEventListener();
    }

    /**
     * Hook the callback function for ExamAnswerView
     * @param listener the callback function
     * */
    public void setExamAnswerEventListener(ExamAnswerEventListener listener) {
        mExamAnswerEventListener = listener;
    }

    /**
     * Customizing The ExamAnswerView string
     * @param str the showing string
     * */
    public void setExamAnswerString(String str) {
        mExamAnswerTextView.setText(str);
    }

    /**
     * Changing the state of ExamAnswerView. Changing the state will cause the appearance of View
     * change as well.
     * @param state
     * */
    public void setExamAnswerState(int state) {
        int textStyleResId;
        LevelListDrawable bgdDrawable = (LevelListDrawable) this.getBackground();
        /** TODO: Wait Tom and Daisy to supply the image */
//        LevelListDrawable imgDrawable = (LevelListDrawable) mExamAnswerImageView.getBackground();
        switch (state) {
            case IDX_EXAM_ANSWER:
                textStyleResId = STYLE_TXT_EXAM_ANSWER_RES_ID;
                bgdDrawable.setLevel(IDX_DRAWABLE_BGD_EXAM_ANSWER_LV);
//                imgDrawable.setLevel(IDX_DRAWABLE_IMG_EXAM_ANSWER_LV);
                break;
            case IDX_EXAM_ANSWER_CHOSEN:
                textStyleResId = STYLE_TXT_EXAM_ANSWER_CHOSEN_RES_ID;
                bgdDrawable.setLevel(IDX_DRAWABLE_BGD_EXAM_ANSWER_CHOSEN_LV);
//                imgDrawable.setLevel(IDX_DRAWABLE_IMG_EXAM_ANSWER_CHOSEN_LV);
                break;
            case IDX_EXAM_ANSWER_CORRECT:
                textStyleResId = STYLE_TXT_EXAM_ANSWER_CORRECT_RES_ID;
                bgdDrawable.setLevel(IDX_DRAWABLE_BGD_EXAM_ANSWER_CORRECT_LV);
//                imgDrawable.setLevel(IDX_DRAWABLE_IMG_EXAM_ANSWER_CORRECT_LV);
                break;
            case IDX_EXAM_ANSWER_WRONG:
                textStyleResId = STYLE_TXT_EXAM_ANSWER_WRONG_RES_ID;
                bgdDrawable.setLevel(IDX_DRAWABLE_BGD_EXAM_ANSWER_WRONG_LV);
//                imgDrawable.setLevel(IDX_DRAWABLE_IMG_EXAM_ANSWER_WRONG_LV);
                break;
            default:
                textStyleResId = STYLE_TXT_EXAM_ANSWER_RES_ID;
                bgdDrawable.setLevel(IDX_DRAWABLE_BGD_EXAM_ANSWER_LV);
//                imgDrawable.setLevel(IDX_DRAWABLE_IMG_EXAM_ANSWER_LV);
                break;
        }

        if (Build.VERSION.SDK_INT < 23) {
            mExamAnswerTextView.setTextAppearance(mContext, textStyleResId);
        } else {
            mExamAnswerTextView.setTextAppearance(textStyleResId);
        }
        this.setBackground(bgdDrawable);
//        mExamAnswerImageView.setBackground(imgDrawable);
    }
    
    private void registerEventListener() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mExamAnswerEventListener != null) {
                    mExamAnswerEventListener.onClick();
                }
            }
        });
    }
}