package com.wishcan.www.vocabulazy.main.exam.view;

import android.animation.Animator;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wishcan.www.vocabulazy.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExamView extends LinearLayout {

	private static final int VIEW_RES_ID = R.layout.view_exam;
	private static final int VIEW_QUESTION_PARENT_RES_ID = R.id.exam_question_parent;
	private static final int VIEW_QUESTION_TITLE_PARENT_RES_ID = R.id.exam_question_title_parent;
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

    private View mView;
    private Runnable mPopOutTask;
    private int mSequentialCounter;
    private boolean mAnimLocker;
	public ExamView(Context context) {
		this(context, null);
	}

	public ExamView(Context context, AttributeSet attr) {
		super(context, attr);
        mView = (ViewGroup) ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(VIEW_RES_ID, null);
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
        initPopOutAnimation();
        addView(mView);
	}

	private void initPopOutAnimation() {
		PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofFloat("scaleX", 0f, 1f);
        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofFloat("scaleY", 0f, 1f);
        PropertyValuesHolder pvhScaleX2 = PropertyValuesHolder.ofFloat("scaleX", 1f, 0f);
        PropertyValuesHolder pvhScaleY2 = PropertyValuesHolder.ofFloat("scaleY", 1f, 0f);
        List<Animator> animatorList = new ArrayList<>();

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
            public void startTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) { mAnimLocker = true; }

            @Override
            public void endTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) { mAnimLocker = false; }
        });

        ((ViewGroup) v).setLayoutTransition(layoutTransition);
        for(int i = 0; i < EXAM_PARENT_VIEW_RES_IDs.length; i++)
            mView.findViewById(EXAM_PARENT_VIEW_RES_IDs[i]).setVisibility(View.GONE);

	}

	public void startPopOut(){
        for(int i = 0; i < EXAM_PARENT_VIEW_RES_IDs.length -1; i++)   // should not pop out next icon
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
        ((TextView) mView.findViewById(QUESTION_OPTION_TEXT_VIEW_IDs[0])).setText(questionArr.get(0));
        // 1, 2 is current_question_index and question_count individually
        ((TextView) mView.findViewById(QUESTION_OPTION_TEXT_VIEW_IDs[1])).setText(String.valueOf(qIndex));
        ((TextView) mView.findViewById(QUESTION_OPTION_TEXT_VIEW_IDs[2])).setText(String.valueOf(qCount));


        for(int i = 2, j = 1 ; i < EXAM_PARENT_VIEW_RES_IDs.length - 1 && j < map.size(); j++, i++ ){
            // next option(0) should not be handled
            // 1 and 2 are parent view
            ArrayList<String> strArr = map.get(j);  // j should be 1~4, i.e. 4 options, 0 is question
            ((ExamOptionItem) mView.findViewById(EXAM_PARENT_VIEW_RES_IDs[i])).setEnglishText(strArr.get(0));
            ((ExamOptionItem) mView.findViewById(EXAM_PARENT_VIEW_RES_IDs[i])).setTransText(strArr.get(1));
        }

    }

    public void startPopOutSequentially(){ mPopOutTask.run(); }



}