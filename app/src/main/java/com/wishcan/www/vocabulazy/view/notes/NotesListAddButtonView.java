package com.wishcan.www.vocabulazy.view.notes;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.RelativeLayout;

import com.wishcan.www.vocabulazy.R;
/**
 * Created by swallow on 2015/12/9.
 */
public class NotesListAddButtonView extends RelativeLayout {

    private static final String TAG = NotesListAddButtonView.class.getSimpleName();

    private interface ScrollStoppedListener{
        void onScrollStopped();
    }

    public interface AddButtonOnClickListener{
        void onAddButtonOnClick();
    }

    private final static int COUNTER_SECS = 15;

    private final static int CIRCLE_ENLARGE = 0;

    private final static int CIRCLE_DWINDLE = 1;

    private NotesListView mNotesListView;

    private View mAddButton;

    private Runnable mScrollStopCounterTask;

    private ScrollStoppedListener mOnScrollStoppedListener;

    private AddButtonOnClickListener mAddOnButtonClickListener;

    private int mInitialPosition, mCounter;

    public NotesListAddButtonView(Context context) {
        this(context, null);
    }

    public NotesListAddButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mNotesListView = new NotesListView(context);
        mAddButton = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.add_new_note_button, null);

        /** mNotesListView layoutParams*/
        RelativeLayout.LayoutParams notesListViewLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        /** mAddButton layoutParams*/
        int marginPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
        RelativeLayout.LayoutParams addButtonLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addButtonLayoutParams.addRule(ALIGN_PARENT_BOTTOM);
        addButtonLayoutParams.addRule(ALIGN_PARENT_RIGHT);
        addButtonLayoutParams.setMargins(0, 0, marginPixels, marginPixels);

        addView(mNotesListView, notesListViewLayoutParams);
        addView(mAddButton, addButtonLayoutParams);

        mScrollStopCounterTask = new Runnable() {
            @Override
            public void run() {
                int newPosition = mNotesListView.getScrollY();
                if(mInitialPosition - newPosition == 0 && mCounter >= COUNTER_SECS){    //has stopped and keep for 3 secs
                    if(mOnScrollStoppedListener != null)
                        mOnScrollStoppedListener.onScrollStopped();


                }else{
                    mInitialPosition = mNotesListView.getScrollY();
//                    Log.d(TAG, "Counter " + mCounter);
                    mCounter++;
                    NotesListAddButtonView.this.postDelayed(mScrollStopCounterTask, 100);
                }
            }
        };


        mNotesListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if(mCounter == 0){
                    mInitialPosition = mNotesListView.getScrollY();
                    startAddButtonAnimation(CIRCLE_DWINDLE);
                    mScrollStopCounterTask.run();
                }

            }
        });

        mOnScrollStoppedListener = new ScrollStoppedListener() {
            @Override
            public void onScrollStopped() {

                startAddButtonAnimation(CIRCLE_ENLARGE);
                mCounter = 0;
            }
        };

        mAddButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAddOnButtonClickListener != null) {
                    startAddButtonAnimation(CIRCLE_DWINDLE);
                    mAddOnButtonClickListener.onAddButtonOnClick();
                }
            }
        });

    }

    public NotesListView getNotesListView(){ return mNotesListView; }

    public void setAddOnButtonClickListener(AddButtonOnClickListener listener){
        mAddOnButtonClickListener = listener;
    }

    private void startAddButtonAnimation(final int action){

        ValueAnimator buttonAnimator;

        if(action == CIRCLE_ENLARGE) {
            PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofFloat("scaleX", 0f, 1f);
            PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofFloat("scaleY", 0f, 1f);
            buttonAnimator = ObjectAnimator.ofPropertyValuesHolder(mAddButton, pvhScaleX, pvhScaleY);
        }
        else{
            PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0f);
            PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0f);
            buttonAnimator = ObjectAnimator.ofPropertyValuesHolder(mAddButton, pvhScaleX, pvhScaleY);
        }

        buttonAnimator.setDuration(300);
        buttonAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        buttonAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (action == CIRCLE_ENLARGE)
                    mAddButton.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (action == CIRCLE_DWINDLE)
                    mAddButton.setVisibility(GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        buttonAnimator.start();
    }
}
