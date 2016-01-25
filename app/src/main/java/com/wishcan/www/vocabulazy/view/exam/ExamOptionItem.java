package com.wishcan.www.vocabulazy.view.exam;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wishcan.www.vocabulazy.R;

/**
 * Created by swallow on 2015/12/26.
 */
public class ExamOptionItem extends LinearLayout {

    private static final int IMG_RES_IDs[] = {
            R.drawable.exam_correct_icon, R.drawable.exam_incorrect_icon};

    private static final int PADDING_RES_IDs[] = {
            R.dimen.exam_padding_horizontal, R.dimen.exam_padding_vertical,
            R.dimen.exam_padding_horizontal, R.dimen.exam_padding_vertical };

    private static final int BACKGROUND_RES_IDs[] = {
            R.drawable.exam_rect_gray, R.drawable.exam_rect_deepgray, R.drawable.exam_rect_yellow};

    private static final int TEXT_SIZE_RES_IDs = R.dimen.exam_text_size;

    private Context mContext;

    private Animator mAnimatorDown, mAnimatorUp;

    private ImageView mCorrectImage0, mCorrectImage1;

    private TextView mEnglishTextView, mTranslationTextView;

    private Rect mBoundRect;

    public ExamOptionItem(Context context) {
        this(context, null);
    }

    public ExamOptionItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        initLayout();
        setOnClickAnimator();
    }

    private void initLayout(){
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams layoutParams0 =
                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams layoutParams1 =
                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        LinearLayout linearLayout = new LinearLayout(mContext);

        setBackgroundResource(BACKGROUND_RES_IDs[0]);
        setGravity(Gravity.CENTER_VERTICAL);

        mCorrectImage0 = new ImageView(mContext);
        layoutParams0.setMargins(getResources().getDimensionPixelSize(R.dimen.exam_image_margin), 0, 0, 0);
        mCorrectImage0.setLayoutParams(layoutParams0);
        mCorrectImage0.setImageResource(IMG_RES_IDs[0]);
        mCorrectImage0.setVisibility(INVISIBLE);

        mCorrectImage1 = new ImageView(mContext);
        layoutParams1.setMargins(0, 0, getResources().getDimensionPixelSize(R.dimen.exam_image_margin), 0);
        mCorrectImage1.setLayoutParams(layoutParams1);
        mCorrectImage1.setImageResource(IMG_RES_IDs[0]);
        mCorrectImage1.setVisibility(INVISIBLE);

        linearLayout.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
        linearLayout.setGravity(Gravity.CENTER);

        mEnglishTextView = new TextView(mContext);
        mEnglishTextView.setLayoutParams(layoutParams);
        mEnglishTextView.setTextColor(Color.BLACK);
        mEnglishTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(TEXT_SIZE_RES_IDs));
        mEnglishTextView.setPadding(getResources().getDimensionPixelSize(PADDING_RES_IDs[0]),
                getResources().getDimensionPixelSize(PADDING_RES_IDs[1]),
                getResources().getDimensionPixelSize(PADDING_RES_IDs[2]),
                getResources().getDimensionPixelSize(PADDING_RES_IDs[3]));
        mEnglishTextView.setSingleLine();
        mEnglishTextView.setVisibility(GONE);

        mTranslationTextView = new TextView(mContext);
        mTranslationTextView.setLayoutParams(layoutParams);
        mTranslationTextView.setTextColor(Color.BLACK);
        mTranslationTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(TEXT_SIZE_RES_IDs));
        mTranslationTextView.setPadding(getResources().getDimensionPixelSize(PADDING_RES_IDs[0]),
                getResources().getDimensionPixelSize(PADDING_RES_IDs[1]),
                getResources().getDimensionPixelSize(PADDING_RES_IDs[2]),
                getResources().getDimensionPixelSize(PADDING_RES_IDs[3]));
        mTranslationTextView.setSingleLine();

        linearLayout.addView(mEnglishTextView);
        linearLayout.addView(mTranslationTextView);

        addView(mCorrectImage0);
        addView(linearLayout);
        addView(mCorrectImage1);
    }

    private void setOnClickAnimator() {


        PropertyValuesHolder stretchX0 = PropertyValuesHolder.ofFloat("scaleX", 1f, 0.8f);
        PropertyValuesHolder stretchY0 = PropertyValuesHolder.ofFloat("scaleY", 1f, 0.8f);

        PropertyValuesHolder stretchX = PropertyValuesHolder.ofFloat("scaleX", 0.8f, 1f);
        PropertyValuesHolder stretchY = PropertyValuesHolder.ofFloat("scaleY", 0.8f, 1f);

        mAnimatorDown = ObjectAnimator.ofPropertyValuesHolder(this, stretchX0, stretchY0);
        mAnimatorDown.setDuration(200);
        mAnimatorDown.setInterpolator(new AccelerateDecelerateInterpolator());

        mAnimatorUp = ObjectAnimator.ofPropertyValuesHolder(this, stretchX, stretchY);
        mAnimatorUp.setDuration(200);
        mAnimatorUp.setInterpolator(new AccelerateDecelerateInterpolator());

    }

    public void initState(){
        setBackgroundResource(BACKGROUND_RES_IDs[0]);
        mCorrectImage0.setImageResource(IMG_RES_IDs[0]);
        mCorrectImage0.setVisibility(INVISIBLE);
        mEnglishTextView.setTextColor(Color.BLACK);
        mEnglishTextView.setVisibility(GONE);
        mTranslationTextView.setTextColor(Color.BLACK);
    }

    public void performActionDownAnimation() {
        mAnimatorDown.start();
    }

    public void performActionUpAnimation() { mAnimatorUp.start(); }

    public void performShowAnswerAction(boolean correct) {
        changeBackground(correct);
        setTransTextColor(Color.WHITE);

        if(!correct) {
            mEnglishTextView.setVisibility(VISIBLE);
            setEnglishTextColor(Color.WHITE);
        }
    }

    public void setEnglishText(String textStr){
        mEnglishTextView.setText(textStr);
    }

    public void setTransText(String textStr){
        mTranslationTextView.setText(textStr);
    }

    public void setEnglishTextColor(int color){
        mEnglishTextView.setTextColor(color);
    }

    public void setTransTextColor(int color){
        mTranslationTextView.setTextColor(color);
    }

    public void showCorrectImage(boolean correct){
        if(correct)
            mCorrectImage0.setImageResource(IMG_RES_IDs[0]);
        else
            mCorrectImage0.setImageResource(IMG_RES_IDs[1]);
        mCorrectImage0.setVisibility(VISIBLE);
    }

    public void changeBackground(boolean correct){
        if(correct)
            setBackgroundResource(BACKGROUND_RES_IDs[2]);
        else
            setBackgroundResource(BACKGROUND_RES_IDs[1]);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if(ev.getAction() == MotionEvent.ACTION_DOWN) {
            mBoundRect = new Rect(getLeft(), getTop(), getRight(), getBottom());
            Log.d("ExamOptionItem", "ACTION_DOWN "+ mBoundRect.left +" "+ mBoundRect.right);
            performActionDownAnimation();
            return false;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch(ev.getAction()){
            case MotionEvent.ACTION_UP:
                Log.d("ExamOptionItem", "ACTION_UP");
                if(mBoundRect.contains(getLeft() + (int) ev.getX(), getTop() + (int) ev.getY())) {
                    mAnimatorUp.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {}
                        @Override
                        public void onAnimationEnd(Animator animation) {performClick();}
                        @Override
                        public void onAnimationCancel(Animator animation) {}
                        @Override
                        public void onAnimationRepeat(Animator animation) {}
                    });
                    performActionUpAnimation();
                }
                else {
                    mAnimatorUp.removeAllListeners();
                    performActionUpAnimation();
                }

                break;
            default:
                break;
        }

        return true;
    }
}
