package com.wishcan.www.vocabulazy.main.exam.view;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.widget.AutoResizeTextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ExamView extends LinearLayout {

    public interface ExamButtonClickListener {
        void onExamOptionClick(int clickedIndex);
        void onExamNextClick();
        void onPlayerClick(String str);
    }

	private static final int VIEW_RES_ID = R.layout.view_exam;
	private static final int VIEW_QUESTION_PARENT_RES_ID = R.id.exam_question_parent;
	private static final int VIEW_QUESTION_TITLE_PARENT_RES_ID = R.id.exam_question_title_parent;
    private static final int VIEW_QUESTION_PLAYER_RES_ID = R.id.exam_question_player;
	private static final int VIEW_OPTION1_RES_ID = R.id.exam_option1_parent;
	private static final int VIEW_OPTION2_RES_ID = R.id.exam_option2_parent;
	private static final int VIEW_OPTION3_RES_ID = R.id.exam_option3_parent;
	private static final int VIEW_OPTION4_RES_ID = R.id.exam_option4_parent;
	private static final int VIEW_TEXT_QUESTION = R.id.exam_question_text;
	private static final int VIEW_TEXT_QUESTION_NUM = R.id.exam_question_number;
	private static final int VIEW_TEXT_QUESTION_COUNT = R.id.exam_question_count;

    public static final int VIEW_NEXT_RES_ID = R.id.exam_next_parent;
	/** This array is mainly used for animation coding convenience */
	public static final int[] EXAM_PARENT_VIEW_RES_IDs = {
            VIEW_QUESTION_PARENT_RES_ID, VIEW_QUESTION_TITLE_PARENT_RES_ID,
            VIEW_OPTION1_RES_ID, VIEW_OPTION2_RES_ID,
            VIEW_OPTION3_RES_ID, VIEW_OPTION4_RES_ID,
            VIEW_NEXT_RES_ID };
    public static final int[] QUESTION_OPTION_TEXT_VIEW_IDs = {
            VIEW_TEXT_QUESTION, VIEW_TEXT_QUESTION_NUM, VIEW_TEXT_QUESTION_COUNT };

    public static final int BUTTON_OPTIONS = 1;
    public static final int BUTTON_NEXT    = 1 << 1;
    public static final int BUTTON_PLAYER  = 1 << 2;
    private View mView;
    private Runnable mPopOutTask;
    private int mSequentialCounter;
    private boolean mAnimLocker;
    private int mButtonsClickableFlag;
    private ExamButtonClickListener mExamButtonClickListener;

	public ExamView(Context context) {
		this(context, null);
	}

	public ExamView(Context context, AttributeSet attr) {
		super(context, attr);
        mView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(VIEW_RES_ID, null);
        mView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(mView);
        mSequentialCounter = 0;
        mAnimLocker = false;
        mPopOutTask = new Runnable() {
            @Override
            public void run() {
                final View view = mView.findViewById(EXAM_PARENT_VIEW_RES_IDs[mSequentialCounter++]);
                view.setVisibility(View.VISIBLE);
                if(mSequentialCounter < EXAM_PARENT_VIEW_RES_IDs.length - 1)      // should not pop out next icon
                    view.postDelayed(mPopOutTask, 200);
                else
                    mSequentialCounter = 0;
            }
        };
        initButtonListener();
        initPopOutAnimation();
	}

    /**------------------------------- Function for animation -----------------------------------**/
    private void initPopOutAnimation() {
        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofFloat("scaleX", 0f, 1f);
        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofFloat("scaleY", 0f, 1f);
        PropertyValuesHolder pvhScaleX2 = PropertyValuesHolder.ofFloat("scaleX", 1f, 0f);
        PropertyValuesHolder pvhScaleY2 = PropertyValuesHolder.ofFloat("scaleY", 1f, 0f);

        View v = mView.findViewById(EXAM_PARENT_VIEW_RES_IDs[0]);

        ValueAnimator popOutAnimator = ObjectAnimator.ofPropertyValuesHolder(v, pvhScaleX, pvhScaleY);
        popOutAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        popOutAnimator.setDuration(300);

        ValueAnimator vanishAnimator = ObjectAnimator.ofPropertyValuesHolder(v, pvhScaleX2, pvhScaleY2);
        vanishAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        vanishAnimator.setDuration(300);

        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.setAnimator(LayoutTransition.DISAPPEARING, vanishAnimator);
        layoutTransition.setAnimator(LayoutTransition.APPEARING, popOutAnimator);
        layoutTransition.addTransitionListener(new LayoutTransition.TransitionListener() {
            @Override
            public void startTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {
                mAnimLocker = true;
            }

            @Override
            public void endTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {
                mAnimLocker = false;
            }
        });

        ((ViewGroup) v).setLayoutTransition(layoutTransition);
        for(int i = 0; i < EXAM_PARENT_VIEW_RES_IDs.length; i++)
            mView.findViewById(EXAM_PARENT_VIEW_RES_IDs[i]).setVisibility(View.GONE);

    }

	public void startPopOut(){
        for(int i = 1; i < EXAM_PARENT_VIEW_RES_IDs.length -1; i++)   // should not pop out next icon
            mView.findViewById(EXAM_PARENT_VIEW_RES_IDs[i]).setVisibility(View.VISIBLE);
    }

    public void popOutNextIcon(){
        mView.findViewById(EXAM_PARENT_VIEW_RES_IDs[EXAM_PARENT_VIEW_RES_IDs.length - 1]).setVisibility(View.VISIBLE);
    }

    public void startVanish(){
        for(int i = 1; i < EXAM_PARENT_VIEW_RES_IDs.length; i++)      // should vanish all icon
                mView.findViewById(EXAM_PARENT_VIEW_RES_IDs[i]).setVisibility(View.GONE);
    }

    public boolean getAnimLocker(){
        return mAnimLocker;
    }

    public void showAnswer(int correctIndex, int chosenIndex){
        for(int i = 2 ; i < EXAM_PARENT_VIEW_RES_IDs.length - 1; i++){
            ((ExamOptionItem) mView.findViewById(EXAM_PARENT_VIEW_RES_IDs[i])).performShowAnswerAction((i - 1) == correctIndex);

            if((i - 1) == correctIndex)
                ((ExamOptionItem) mView.findViewById(EXAM_PARENT_VIEW_RES_IDs[i])).showCorrectImage(true);
            else if((i-1) == chosenIndex)
                ((ExamOptionItem) mView.findViewById(EXAM_PARENT_VIEW_RES_IDs[i])).showCorrectImage(false);
        }
    }

    public void refreshContent(int qIndex, int qCount, HashMap<Integer, ArrayList<String>> map){

        if(map == null)
            return;

        for(int i = 2; i < EXAM_PARENT_VIEW_RES_IDs.length - 1; i++)  // but question and next should not be handled
            ((ExamOptionItem) mView.findViewById(EXAM_PARENT_VIEW_RES_IDs[i])).initState();

        // 0 is question
        ArrayList<String> questionArr = map.get(0);
        ((AutoResizeTextView) mView.findViewById(QUESTION_OPTION_TEXT_VIEW_IDs[0])).resetTextSize();
        ((AutoResizeTextView) mView.findViewById(QUESTION_OPTION_TEXT_VIEW_IDs[0])).setText(questionArr.get(0));
        // 1, 2 is current_question_index and question_count individually
        ((TextView) mView.findViewById(QUESTION_OPTION_TEXT_VIEW_IDs[1])).setText(String.valueOf(qIndex));
        ((TextView) mView.findViewById(QUESTION_OPTION_TEXT_VIEW_IDs[2])).setText(String.valueOf(qCount));


        for(int i = 2, j = 1 ; i < EXAM_PARENT_VIEW_RES_IDs.length - 1 && j < map.size(); j++, i++ ){
            // next option should not be handled
            ArrayList<String> strArr = map.get(j);  // j should be 1~4, i.e. 4 options, 0 is question
            ((ExamOptionItem) mView.findViewById(EXAM_PARENT_VIEW_RES_IDs[i])).setEnglishText(strArr.get(0));
            ((ExamOptionItem) mView.findViewById(EXAM_PARENT_VIEW_RES_IDs[i])).setTransText(strArr.get(1));
        }

    }

    public void startPopOutSequentially(){ mPopOutTask.run(); }

    /**---------------------------- Function for button listener --------------------------------**/
    private void initButtonListener() {
        /** onClick event for 4 option buttons*/
        for(int i = 2; i < EXAM_PARENT_VIEW_RES_IDs.length - 1; i++){   // NEXT icon should not be handle here
            final int clickedIndex = i - 1;
            findViewById(EXAM_PARENT_VIEW_RES_IDs[i]).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mExamButtonClickListener != null && ((mButtonsClickableFlag & BUTTON_OPTIONS) > 0)) {
                        mExamButtonClickListener.onExamOptionClick(clickedIndex);
                    }
                }
            });
        }

        /** onClick event for next button */
        findViewById(VIEW_NEXT_RES_ID).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mExamButtonClickListener != null && ((mButtonsClickableFlag & BUTTON_NEXT) > 0)) {
                    mExamButtonClickListener.onExamNextClick();
                }
            }
        });

        /** onClick event for player button */
        findViewById(VIEW_QUESTION_PLAYER_RES_ID).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mExamButtonClickListener != null && ((mButtonsClickableFlag & BUTTON_PLAYER) > 0 )) {
                    String questionStr = ((TextView) mView.findViewById(QUESTION_OPTION_TEXT_VIEW_IDs[0])).getText().toString();
                    mExamButtonClickListener.onPlayerClick(questionStr);
                }
            }
        });
    }

    public void setExamButtonClickListener(ExamButtonClickListener listener) {
        setButtonsEnable(BUTTON_NEXT | BUTTON_OPTIONS | BUTTON_PLAYER);
        mExamButtonClickListener = listener;
    }

    public void setButtonsEnable(int buttons) {
        mButtonsClickableFlag |= buttons;
        Log.d("ExamView", Integer.toString(mButtonsClickableFlag, 2));
    }

    public void setButtonsDisable(int buttons) {
        buttons = ~buttons;
        mButtonsClickableFlag &= buttons;
        Log.d("ExamView", Integer.toString(mButtonsClickableFlag, 2));
    }
}