package com.wishcan.www.vocabulazy.view.infinitethreeview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Parcelable;
import android.support.v4.util.CircularArray;
import android.support.v4.util.TimeUtils;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.LinkedList;

import com.wishcan.www.vocabulazy.R;

/**
 * Created by swallow on 2015/9/22.
 */
public class InfiniteThreeView extends RelativeLayout {

    public interface OnPageChangedListener {
        void onPageScrolled();

        void onPageChanged(int direction);
    }

    private static final String TAG = InfiniteThreeView.class.getSimpleName();

    private static final int[] TEST_COLOR_ARRAY = {R.color.test_color_0, R.color.test_color_1, R.color.test_color_2};

    public static final int LEFT_VIEW_INDEX = 0;

    public static final int CENTER_VIEW_INDEX = 1;

    public static final int RIGHT_VIEW_INDEX = 2;

    public static final int MOVE_TO_RIGHT = 1;

    public static final int MOVE_TO_LEFT = -1;

    private static final int MIN_FLING_VELOCITY = 400; // dips

    private Context mContext;

    private RelativeLayout.LayoutParams mDefaultLayoutParams;

    private RelativeLayout.LayoutParams mDefaultContainerParams;

    private RelativeLayout.LayoutParams mDefaultItemLayoutParams;

    private int mThreeViewWidth;

    private int mOneViewWidth;

    private LinearLayout mThreeViewContainer;

    private LinkedList<ViewGroup> mThreeViewLL;

    private OnPageChangedListener mOnPageChangedListener;

    private float mOnInitialTouch;

    private float mOnTouchX;

    private float mOnTouchY;

    private int mMinimumVelocity;

    private int mMaximumVelocity;

    private VelocityTracker mVelocityTracker;

    private int mActivePointerId = -1;

    public InfiniteThreeView(Context context) {
        this(context, null);
    }

    public InfiniteThreeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        init();

    }

    @Override
    protected Parcelable onSaveInstanceState() {
//        Log.d(TAG, "onSaveInstanceState");
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
//        Log.d(TAG, "onRestoreInstanceState");
        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        Log.d(TAG, "onDraw");
        super.onDraw(canvas);
    }

    private void init() {
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
        mOneViewWidth = metrics.widthPixels;
        mThreeViewWidth = mOneViewWidth * 3;

        mDefaultLayoutParams = new LayoutParams(mThreeViewWidth, LayoutParams.MATCH_PARENT);
        setLayoutParams(mDefaultLayoutParams);

        mThreeViewContainer = new LinearLayout(mContext);
        mDefaultContainerParams = new LayoutParams(mThreeViewWidth, LayoutParams.MATCH_PARENT);
        mDefaultContainerParams.setMargins(-1 * mOneViewWidth, 0, 0, 0);
        mThreeViewContainer.setLayoutParams(mDefaultContainerParams);
        addView(mThreeViewContainer);

        mThreeViewLL = new LinkedList<>();

        for (int i = 0; i < 3; i++) {
            RelativeLayout viewGroupItem = new RelativeLayout(mContext);
            viewGroupItem.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1));
//            viewGroupItem.setBackgroundColor(getResources().getColor(TEST_COLOR_ARRAY[i]));
            mThreeViewLL.add(viewGroupItem);
            mThreeViewContainer.addView(viewGroupItem);
        }

        mDefaultItemLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        setOnPageChangedListener(new OnPageChangedListener() {
            @Override
            public void onPageScrolled() {
            }

            @Override
            public void onPageChanged(int direction) {
            }
        });

        final float density = mContext.getResources().getDisplayMetrics().density;
        mMinimumVelocity = (int) (MIN_FLING_VELOCITY * density);
        final ViewConfiguration configuration = ViewConfiguration.get(mContext);
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    public void setCurrentItem(int index) {
        if (index < 0 || index > 2)
            return;
        AnimatorSet set = new AnimatorSet();
        ValueAnimator animatorLeft, animatorCenter, animatorRight;
        int moveToLeftOrRight;
        if (index == LEFT_VIEW_INDEX) {
            moveToLeftOrRight = MOVE_TO_RIGHT;
            animatorLeft = ObjectAnimator.ofFloat(mThreeViewLL.get(LEFT_VIEW_INDEX), "X", mThreeViewLL.get(LEFT_VIEW_INDEX).getX(), moveToLeftOrRight * mOneViewWidth);
            animatorRight = ObjectAnimator.ofFloat(mThreeViewLL.get(RIGHT_VIEW_INDEX), "X", mThreeViewLL.get(RIGHT_VIEW_INDEX).getX(), 0);
            mThreeViewLL.get(RIGHT_VIEW_INDEX).setVisibility(INVISIBLE);
        } else {
            moveToLeftOrRight = MOVE_TO_LEFT;
            animatorLeft = ObjectAnimator.ofFloat(mThreeViewLL.get(LEFT_VIEW_INDEX), "X", mThreeViewLL.get(LEFT_VIEW_INDEX).getX(), -2 * moveToLeftOrRight * mOneViewWidth);
            animatorRight = ObjectAnimator.ofFloat(mThreeViewLL.get(RIGHT_VIEW_INDEX), "X", mThreeViewLL.get(RIGHT_VIEW_INDEX).getX(), -1 * moveToLeftOrRight * mOneViewWidth);
            mThreeViewLL.get(LEFT_VIEW_INDEX).setVisibility(INVISIBLE);
        }
        animatorCenter = ObjectAnimator.ofFloat(mThreeViewLL.get(CENTER_VIEW_INDEX), "X", mThreeViewLL.get(CENTER_VIEW_INDEX).getX(), (1 + moveToLeftOrRight) * mOneViewWidth);
        set.play(animatorCenter).with(animatorLeft).with(animatorRight);
        set.setDuration(1000);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.addListener(new ThreeViewAnimatorListener(moveToLeftOrRight));
        set.start();
    }

    public View getCurrentItem() {
        if (mThreeViewLL.get(CENTER_VIEW_INDEX).getChildCount() > 0)
            return mThreeViewLL.get(CENTER_VIEW_INDEX).getChildAt(0);
        return null;
    }

    public void setOnPageChangedListener(OnPageChangedListener listener) {
        mOnPageChangedListener = listener;
    }

    public void destroyItem(int index) {
        mThreeViewLL.get(index).removeAllViews();
    }

    public void refreshItem(int index, View v) {
        mThreeViewLL.get(index).removeAllViews();

        if (v != null) {
            v.setLayoutParams(mDefaultItemLayoutParams);
            mThreeViewLL.get(index).addView(v);
        }
    }

    private void refreshLinkedListOrder(int moveToLeftOrRight) {
        ViewGroup oriLeft = mThreeViewLL.get(LEFT_VIEW_INDEX);
        ViewGroup oriRight = mThreeViewLL.get(RIGHT_VIEW_INDEX);

        if (moveToLeftOrRight == MOVE_TO_RIGHT) {  //right
            mThreeViewLL.removeLast();
            mThreeViewLL.addFirst(oriRight);
        } else {                       //left
            mThreeViewLL.removeFirst();
            mThreeViewLL.addLast(oriLeft);
        }

        mOnPageChangedListener.onPageChanged(moveToLeftOrRight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(mThreeViewWidth, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mThreeViewLL.get(CENTER_VIEW_INDEX).getChildCount() == 0)
                    return true;
                break;
            case MotionEvent.ACTION_MOVE:
                onInterceptTouchEvent(event);
                break;
            case MotionEvent.ACTION_UP:
                onInterceptTouchEvent(event);
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final boolean orderChanged;
        float offset = 0;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(mVelocityTracker == null)
                    mVelocityTracker = VelocityTracker.obtain();
                mVelocityTracker.addMovement(ev);
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                mOnInitialTouch = mOnTouchX = ev.getAxisValue(0);
                break;
//                return true;
            case MotionEvent.ACTION_MOVE:
                offset = (ev.getAxisValue(0) - mOnTouchX);
                if (Math.abs(ev.getAxisValue(0) - mOnInitialTouch) < mOneViewWidth * 0.1)
                    break;

                mVelocityTracker.addMovement(ev);

                mOnTouchX = ev.getAxisValue(0);
                mThreeViewLL.get(LEFT_VIEW_INDEX).setX(mThreeViewLL.get(LEFT_VIEW_INDEX).getX() + offset);
                mThreeViewLL.get(RIGHT_VIEW_INDEX).setX(mThreeViewLL.get(RIGHT_VIEW_INDEX).getX() + offset);
                mThreeViewLL.get(CENTER_VIEW_INDEX).setX(mThreeViewLL.get(CENTER_VIEW_INDEX).getX() + offset);
                mOnPageChangedListener.onPageScrolled();
                return true;
            case MotionEvent.ACTION_UP:
                float differ = ev.getAxisValue(0) - mOnInitialTouch;

                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int initialVelocity = (int) VelocityTrackerCompat.getXVelocity(
                        velocityTracker, mActivePointerId);

                AnimatorSet set = new AnimatorSet();
                ValueAnimator animatorLeft, animatorCenter, animatorRight;
                final int moveToLeftOrRight = differ > 0 ? MOVE_TO_RIGHT : MOVE_TO_LEFT;       // 1: right, -1: left
                if (Math.abs(differ) > mOneViewWidth * 0.5 || Math.abs(initialVelocity) > mMinimumVelocity) {
                    if (moveToLeftOrRight == MOVE_TO_RIGHT) {
                        animatorLeft = ObjectAnimator.ofFloat(mThreeViewLL.get(LEFT_VIEW_INDEX), "X", mThreeViewLL.get(LEFT_VIEW_INDEX).getX(), moveToLeftOrRight * mOneViewWidth);
                        animatorRight = ObjectAnimator.ofFloat(mThreeViewLL.get(RIGHT_VIEW_INDEX), "X", mThreeViewLL.get(RIGHT_VIEW_INDEX).getX(), 0);
                        mThreeViewLL.get(RIGHT_VIEW_INDEX).setVisibility(INVISIBLE);
                    } else {
                        animatorLeft = ObjectAnimator.ofFloat(mThreeViewLL.get(LEFT_VIEW_INDEX), "X", mThreeViewLL.get(LEFT_VIEW_INDEX).getX(), -2 * moveToLeftOrRight * mOneViewWidth);
                        animatorRight = ObjectAnimator.ofFloat(mThreeViewLL.get(RIGHT_VIEW_INDEX), "X", mThreeViewLL.get(RIGHT_VIEW_INDEX).getX() + offset, -1 * moveToLeftOrRight * mOneViewWidth);
                        mThreeViewLL.get(LEFT_VIEW_INDEX).setVisibility(INVISIBLE);
                    }
                    animatorCenter = ObjectAnimator.ofFloat(mThreeViewLL.get(CENTER_VIEW_INDEX), "X", mThreeViewLL.get(CENTER_VIEW_INDEX).getX() + offset, (1 + moveToLeftOrRight) * mOneViewWidth);
                    orderChanged = true;

                } else {
                    if (moveToLeftOrRight == MOVE_TO_RIGHT) {
                        animatorLeft = ObjectAnimator.ofFloat(mThreeViewLL.get(LEFT_VIEW_INDEX), "X", mThreeViewLL.get(LEFT_VIEW_INDEX).getX(), 0);
                        animatorRight = ObjectAnimator.ofFloat(mThreeViewLL.get(RIGHT_VIEW_INDEX), "X", mThreeViewLL.get(RIGHT_VIEW_INDEX).getX(), 2 * moveToLeftOrRight * mOneViewWidth);
                    } else {
                        animatorLeft = ObjectAnimator.ofFloat(mThreeViewLL.get(LEFT_VIEW_INDEX), "X", mThreeViewLL.get(LEFT_VIEW_INDEX).getX(), 0);
                        animatorRight = ObjectAnimator.ofFloat(mThreeViewLL.get(RIGHT_VIEW_INDEX), "X", mThreeViewLL.get(RIGHT_VIEW_INDEX).getX(), -2 * moveToLeftOrRight * mOneViewWidth);
                    }
                    animatorCenter = ObjectAnimator.ofFloat(mThreeViewLL.get(CENTER_VIEW_INDEX), "X", mThreeViewLL.get(CENTER_VIEW_INDEX).getX(), mOneViewWidth);

                    orderChanged = false;
                }
                set.play(animatorCenter).with(animatorLeft).with(animatorRight);
                set.setDuration(100);
                set.setInterpolator(new AccelerateDecelerateInterpolator());
                set.addListener(new ThreeViewAnimatorListener(moveToLeftOrRight, orderChanged));
                set.start();


                if(mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }


                break;
        }
        return false;
    }

    private class ThreeViewAnimatorListener implements Animator.AnimatorListener {

        int moveToLeftOrRight;

        boolean orderChanged;

        ThreeViewAnimatorListener(int moveToLeftOrRight) {
            this(moveToLeftOrRight, true);

        }

        ThreeViewAnimatorListener(int moveToLeftOrRight, boolean orderChanged) {
            this.moveToLeftOrRight = moveToLeftOrRight;
            this.orderChanged = orderChanged;
        }

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (orderChanged) {
                refreshLinkedListOrder(moveToLeftOrRight);
                mThreeViewLL.get(LEFT_VIEW_INDEX).setVisibility(VISIBLE);
                mThreeViewLL.get(RIGHT_VIEW_INDEX).setVisibility(VISIBLE);
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }


}
