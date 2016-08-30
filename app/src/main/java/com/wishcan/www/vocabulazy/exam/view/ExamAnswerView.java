package com.wishcan.www.vocabulazy.exam.view;

import android.content.Context;
import android.util.AttributeSet;
import android.graphics.drawable.LevelListDrawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.wishcan.www.vocabulazy.R;
/**
 * @author Swallow Chen
 * @since 2016/8/30
 */
public class ExamAnswerView extends LinearLayout {
    public interface ExamAnswerEventListener {
        void onExamAnswerClick();
    }
    
    private static final int VIEW_EXAM_ANSWER_IMG_RES_ID = R.id.exam_answer_image;
    private static final int VIEW_EXAM_ANSWER_TXT_RES_ID = R.id.exam_answer_text;
    
    private static final int STYLE_TXT_EXAM_ANSWER_RES_ID = R.style.AppTextStyle.Exam.Answer;
    private static final int STYLE_TXT_EXAM_ANSWER_CHOSEN_RES_ID = R.style.AppTextStyle.Exam.Answer.Chosen;
    private static final int STYLE_TXT_EXAM_ANSWER_CORRECT_RES_ID = R.style.AppTextStyle.Exam.Answer.Correct;
    private static final int STYLE_TXT_EXAM_ANSWER_WRONG_RES_ID = R.style.AppTextStyle.Exam.Answer.Wrong;
    
    private static final int IDX_DRAWABLE_BGD_EXAM_ANSWER_LV = 0x0;
    private static final int IDX_DRAWABLE_BGD_EXAM_ANSWER_CHOSEN_LV = 0x1;
    private static final int IDX_DRAWABLE_BGD_EXAM_ANSWER_CORRECT_LV = 0x2;
    private static final int IDX_DRAWABLE_BGD_EXAM_ANSWER_WRONG_LV = 0x1;  // WRONG background is the same with CHOSEN
    
    private static final int IDX_DRAWABLE_IMG_EXAM_ANSWER_LV = 0x0;
    private static final int IDX_DRAWABLE_IMG_EXAM_ANSWER_CORRECT_LV = 0x1;
    private static final int IDX_DRAWABLE_IMG_EXAM_ANSWER_WRONG_LV = 0x2;
    
    public static final int IDX_EXAM_ANSWER = 0x0;
    public static final int IDX_EXAM_ANSWER_CHOSEN = 0x1;
    public static final int IDX_EXAM_ANSWER_CORRECT = 0x2;
    public static final int IDX_EXAM_ANSWER_WRONG = 0x3;
    
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
        mExamAnswerImageView = findViewById(VIEW_EXAM_ANSWER_IMG_RES_ID);
        mExamAnswerTextView = findViewById(VIEW_EXAM_ANSWER_TXT_RES_ID);
        
        registerEventListener();
    }
    
    public void setExamAnswerEventListener(ExamAnswerEventListener listener) {
        mExamAnswerEventListener = listener;
    }
    
    public void setExamAnswerState(int state) {
        int textStyleResId;
        LevelListDrawable bgdDrawable = (LevelListDrawable) this.getBackground();
        LevelListDrawable imgDrawable = (LevelListDrawable) mExamAnswerImageView.getBackground();
        switch (state) {
            case IDX_EXAM_ANSWER:
                textStyleResId = STYLE_TXT_EXAM_ANSWER_RES_ID;
                break;
            case IDX_EXAM_ANSWER_CHOSEN:
                textStyleResId = STYLE_TXT_EXAM_ANSWER_CHOSEN_RES_ID;
                break;
            case IDX_EXAM_ANSWER_CORRECT:
                textStyleResId = STYLE_TXT_EXAM_ANSWER_CORRECT_RES_ID;
                break;
            case IDX_EXAM_ANSWER_WRONG:
                textStyleResId = STYLE_TXT_EXAM_ANSWER_WRONG_RES_ID;
                break;
            default:
                textStyleResId = 0;
                break;
        }
        mExamAnswerTextView.setTextAppearance(textStyleResId);
    }
    
    private void registerEventListener() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mExamAnswerEventListener != null) {
                    mExamAnswerEventListener.onExamAnswerClick();
                }
            }
        });
    }
}