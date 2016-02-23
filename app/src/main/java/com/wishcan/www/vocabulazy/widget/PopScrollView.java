package com.wishcan.www.vocabulazy.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.utility.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by swallow on 2015/8/13.
 *
 * Here is how our PopView constructed.
 * PopView is a ScrollView, including a LinearLayout child, which is composed with
 * some rectangle views, such as TextViews.
 *
 * Version 2.0 is developed from 2015/1/14.
 * I am ready for an API, related to
 * customize PopItem and PopItemDetail
 *
 */
abstract public class PopScrollView extends RelativeLayout {

    public static final String TAG = PopScrollView.class.getSimpleName();

    protected abstract View getItemDetailView();

    public interface OnPopScrollStoppedListener{
        void onPopScrollStopped(int index);
        void onPopScrolling();
    }

    public interface OnItemPreparedListener{
        void onInitialItemPrepared();
        void onFinalItemPrepared();
    }

    private OnPopScrollStoppedListener mOnPopScrollStoppedListener;
    private OnItemPreparedListener mOnItemPreparedListener;

    public static final int DEFAULT_PLAYER_LIST_ITEM_COUNT = 3;

    private static final float ZOOM_IN_FACTORY = 1.05f;
    private static final int DEFAULT_CHILD_COUNT_IN_SCROLL_VIEW = 5;
    private static final int DEFAULT_LIST_ITEM_FOCUSED_COLOR_RES_ID = R.color.widget_pop_list_item_focused;
    private static final int DEFAULT_LIST_ITEM0_COLOR_RES_ID = R.color.widget_pop_list_item0_border_bottom_color;
    private static final int DEFAULT_DETAILS_COLOR_RES_ID = R.color.widget_pop_details_color;
    private static final int DEFAULT_TOP_GRADIENT_DRAWABLE_RES_ID = R.drawable.widget_pop_top_gradient_filter;
    private static final int DEFAULT_BOT_GRADIENT_DRAWABLE_RES_ID = R.drawable.widget_pop_bottom_gradient_filter;
    private static final int DEFAULT_FOCUSED_ITEM_DRAWABLE_RES_ID = R.drawable.widget_pop_item0;
    private static final int DEFAULT_ITEM_DRAWABLE_RES_ID = R.drawable.widget_pop_item1;

    public static final int STATE_ITEM_DETAIL_SHOW = 1;
    public static final int STATE_ITEM_DETAIL_CHANGING = 0;
    public static final int STATE_ITEM_DETAIL_NOT_SHOW = -1;

    /** Remember to write where's the default layout come from*/

    private Context mContext;
    private MyScrollView mScrollView;

    /**
     * A ViewGroup LinearLayout will be the only child added to the PopView
     * */
    private LinearLayout mLinearLayout;

    /**
     * mItemDetailsLinearLayout will be added in the center of PopScrollView for showing the
     * details of the mCurrentFocusedPopItemPosition
     * */
    private ItemDetailLinearLayout mItemDetailsLinearLayout;
    private LinearLayout mTopGradientMask;
    private LinearLayout mBottomGradientMask;
    private int mPopViewHeight;
    private int mPopViewWidth;
    private int mPopViewTopMargin;
    private int mPopViewBottomMargin;
    private int mPopItemWidth;
    private int mPopItemHeight;
    private int mPopItemDetailWidth;
    private int mPopItemDetailHeight;
    private int mPopItemZoomInWidth;
    private int mPopItemZoomInHeight;
    private int mCurrentFocusedPopItemPosition;
    private int mPopItemCount;
    private int mShowingDetails;
    private boolean mInitialItemCheckFlag;
    private boolean mFinalItemCheckFlag;
    private final Runnable checkFinalItemStateTask;
    private final Runnable checkInitialItemStateTask;

    public PopScrollView(Context context) {
        this(context, null);
    }

    public PopScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        checkFinalItemStateTask = new Runnable() {
            @Override
            public void run() {
                if(getChildView(mPopItemCount - 1) != null) {
                    mFinalItemCheckFlag = true;
                    if(mOnItemPreparedListener != null)
                        mOnItemPreparedListener.onFinalItemPrepared();
                }
                else
                    PopScrollView.this.postDelayed(checkFinalItemStateTask, 100);
            }
        };

        checkInitialItemStateTask = new Runnable() {
            @Override
            public void run() {
                if(getChildView(0) != null) {
                    mInitialItemCheckFlag = true;
                    if(mOnItemPreparedListener != null)
                        mOnItemPreparedListener.onInitialItemPrepared();
                    initFocusedPosition();
                }
                else
                    PopScrollView.this.postDelayed(checkInitialItemStateTask, 100);
            }
        };

        initPopView();

        addView(mScrollView);
        addView(mTopGradientMask);
        addView(mBottomGradientMask);

    }
    /**
     * Initialize the PopView, make the view size is about 0.9 & 0.7 screen resolution.
     * Initialize the child size, make it equal to 1/CHILD_COUNT of its parent size
     * */
    private void initPopView(){

        setChildSize();
        setChildZoomInSize();
        setPopViewWithDefaultSize();

        mShowingDetails = STATE_ITEM_DETAIL_NOT_SHOW;
        mLinearLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mLinearLayout.setLayoutParams(mLayoutParams);
        mLinearLayout.setOrientation(LinearLayout.VERTICAL);
        mLinearLayout.setGravity(Gravity.CENTER_HORIZONTAL);

        LayoutTransition testT = new LayoutTransition();

        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofFloat("scaleX", 0f, 1f);
        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofFloat("scaleY", 0f, 1f);
        Animator appearAnimScale = ObjectAnimator.ofPropertyValuesHolder((Object) null ,pvhScaleX, pvhScaleY);
        appearAnimScale.setStartDelay(300);
        appearAnimScale.setDuration(300);
        appearAnimScale.setInterpolator(new AccelerateDecelerateInterpolator());
        testT.setAnimator(LayoutTransition.APPEARING, appearAnimScale);
        mLinearLayout.setLayoutTransition(testT);

        mTopGradientMask = new LinearLayout(mContext);
        LayoutParams mTopGradientMaskLayoutParams = new LayoutParams(mPopItemWidth, mPopItemHeight / 2);
        mTopGradientMaskLayoutParams.addRule(ALIGN_PARENT_TOP | CENTER_HORIZONTAL);
        mTopGradientMask.setLayoutParams(mTopGradientMaskLayoutParams);

        mTopGradientMask.setBackground(ContextCompat.getDrawable(mContext, DEFAULT_TOP_GRADIENT_DRAWABLE_RES_ID));

        mBottomGradientMask = new LinearLayout(mContext);
        LayoutParams mBottomGradientMaskLayoutParams = new LayoutParams(mPopItemWidth, mPopItemHeight /2);
        mBottomGradientMaskLayoutParams.addRule(ALIGN_PARENT_BOTTOM);
        mBottomGradientMaskLayoutParams.addRule(CENTER_HORIZONTAL);
        mBottomGradientMask.setLayoutParams(mBottomGradientMaskLayoutParams);
        mBottomGradientMask.setBackground(ContextCompat.getDrawable(mContext, DEFAULT_BOT_GRADIENT_DRAWABLE_RES_ID));
    }

    private void initFocusedPosition() {
        setCurrentFocusedPosition(0);
        if(getCurrentFocusedView().getBackground() instanceof ColorDrawable)
            ((ColorDrawable) getCurrentFocusedView().getBackground()).setColor(ContextCompat.getColor(mContext, DEFAULT_LIST_ITEM_FOCUSED_COLOR_RES_ID));
        else
            ((GradientDrawable) getCurrentFocusedView().getBackground()).setColor(ContextCompat.getColor(mContext, DEFAULT_LIST_ITEM_FOCUSED_COLOR_RES_ID));
        getCurrentFocusedView().setScaleX(ZOOM_IN_FACTORY);
    }

    private void setPopViewWithDefaultSize(){
        mPopViewHeight = mPopItemHeight * DEFAULT_CHILD_COUNT_IN_SCROLL_VIEW;
        mPopViewWidth = mPopItemZoomInWidth;
        mPopViewTopMargin = (int)(mPopItemHeight * (((float)DEFAULT_CHILD_COUNT_IN_SCROLL_VIEW - 1) / 2));
        mPopViewBottomMargin = mPopViewTopMargin;
        mPopItemDetailHeight = (int) (getContext().getResources().getDisplayMetrics().heightPixels * 0.4);
        mPopItemDetailWidth = mPopViewWidth;

        ViewGroup.LayoutParams viewGroupLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        setLayoutParams(viewGroupLayoutParams);

        LayoutParams layoutParams = new LayoutParams(mPopViewWidth, mPopViewHeight);

        layoutParams.addRule(CENTER_HORIZONTAL);
        mScrollView = new MyScrollView(mContext);
        mScrollView.setLayoutParams(layoutParams);
    }

    /**
     * Use the getScrollY() to find out which position should be the focused. If we successfully
     * find this position out, the corresponding getCurrentFocusedView should be the view located
     * in the center of ScrollView
     * */
    private int findCurrentPosition(int coordinateY){
        /**
         * This parameter is used to find our currently center coordinateY. Because of the marginTop
         * we have set, we don't specific re-calculate centerCoordinate with adding half rect-height
         * e.g. if coordinateY = 0, the the parameter equals .5*ChildHeight, which will correctly
         * located at child view index0
         * */
        int centerRectCenterCoordinateY =
                coordinateY + (int) Math.abs(mPopItemHeight *0.5);
        /**
         * If the center coordinateY is beyond a childheight, current position will change as well
         * */
        return (centerRectCenterCoordinateY / mPopItemHeight);
    }

    public void moveToPosition(int position){
        mScrollView.smoothScrollTo(mScrollView.getScrollX(), position * mPopItemHeight);
    }

    /**
     * The method is used for setting current focused position based on scrollbar's position.
     * Normally, the item in the middle should be focused and the color will be orange.
     * */
    public void setCurrentFocusedPosition(int newPosition){
        int previousFocusedPosition;

        if(newPosition >= mPopItemCount || newPosition < 0)
            return;
        if(mShowingDetails == STATE_ITEM_DETAIL_SHOW)
            hideItemDetails();

        previousFocusedPosition = mCurrentFocusedPopItemPosition;
        mCurrentFocusedPopItemPosition = newPosition;
        performItemAppearanceChanged(previousFocusedPosition);

    }

    private void performItemAppearanceChanged(int previousFocusedPosition){

        int focused_color = ContextCompat.getColor(mContext, DEFAULT_LIST_ITEM_FOCUSED_COLOR_RES_ID);
        int item0_color = ContextCompat.getColor(mContext, DEFAULT_LIST_ITEM0_COLOR_RES_ID);
        final View newFocusedView = getCurrentFocusedView();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            newFocusedView.setElevation(30);

        ValueAnimator newFocusedViewAnim = ObjectAnimator.ofFloat(newFocusedView, "scaleX", 1.0f, ZOOM_IN_FACTORY);
        newFocusedViewAnim.setDuration(100);
        newFocusedViewAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        newFocusedViewAnim.start();
        newFocusedViewAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}
            @Override
            public void onAnimationEnd(Animator animation) {
                if(newFocusedView instanceof ViewGroup){
                    ArrayList<View> arrayList = Utility.findViewRecursive((ViewGroup)newFocusedView, TextView.class);
                    for(View v : arrayList) {
                        ((TextView) v).setTextColor(ContextCompat.getColor(getContext(), R.color.player_list_item_font));
                    }
                }
            }
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ValueAnimator newFocusedViewColorAnim = ObjectAnimator.ofArgb(newFocusedView, "BackgroundColor", item0_color, focused_color);
            newFocusedViewColorAnim.setDuration(100);
            newFocusedViewColorAnim.setInterpolator(new AccelerateDecelerateInterpolator());
            newFocusedViewColorAnim.start();
        }
        else{
            newFocusedView.setBackground(ContextCompat.getDrawable(mContext, DEFAULT_FOCUSED_ITEM_DRAWABLE_RES_ID));
        }

        if(previousFocusedPosition < 0)
            return;

        final View previousFocusedView = getChildView(previousFocusedPosition);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            previousFocusedView.setElevation(0);
        }

        ValueAnimator previousFocusedViewAnim = ObjectAnimator.ofFloat(previousFocusedView, "scaleX", ZOOM_IN_FACTORY, 1.0f);
        previousFocusedViewAnim.setDuration(100);
        previousFocusedViewAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        previousFocusedViewAnim.start();
        previousFocusedViewAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (previousFocusedView instanceof ViewGroup) {
                    ArrayList<View> arrayList = Utility.findViewRecursive((ViewGroup) previousFocusedView, TextView.class);
                    for (View v : arrayList) {
                        ((TextView) v).setTextColor(ContextCompat.getColor(getContext(), R.color.player_list_item_font1));
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ValueAnimator previousFocusedViewColorAnim = ObjectAnimator.ofArgb(previousFocusedView, "BackgroundColor", focused_color, item0_color);
            previousFocusedViewColorAnim.setDuration(100);
            previousFocusedViewColorAnim.setInterpolator(new AccelerateDecelerateInterpolator());
            previousFocusedViewColorAnim.start();
            previousFocusedViewColorAnim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {}
                @Override
                public void onAnimationEnd(Animator animation) {
                    previousFocusedView.setBackground(ContextCompat.getDrawable(mContext, DEFAULT_ITEM_DRAWABLE_RES_ID));
                }
                @Override
                public void onAnimationCancel(Animator animation) {}
                @Override
                public void onAnimationRepeat(Animator animation) {}
            });
        }
        else {
            previousFocusedView.setBackground(ContextCompat.getDrawable(mContext, DEFAULT_ITEM_DRAWABLE_RES_ID));
            if(previousFocusedView instanceof ViewGroup){
                ArrayList<View> arrayList = Utility.findViewRecursive((ViewGroup)previousFocusedView, TextView.class);
                for(View v : arrayList) {
                    ((TextView) v).setTextColor(ContextCompat.getColor(getContext(), R.color.player_list_item_font1));
                }
            }
        }


    }

    /**
     * showItemDetails shows the detail of each item
     * it will do the following thing
     * 1. Add a new faked layout (fakedLinearLayout) which size is the same with ZoomIn item (Immediately)
     * and make the faked layout stretch to the larger size slowly (0.5s)
     * 2. Show the detail content of word (0.3s)
     * */
    public void showItemDetails(){
        if(mShowingDetails == STATE_ITEM_DETAIL_NOT_SHOW)
            mItemDetailsLinearLayout = new ItemDetailLinearLayout(mContext, getCurrentFocusedPosition());
        else
            return;

        // Step 1
        LayoutParams layoutParams = new LayoutParams(mPopItemDetailWidth, mPopItemDetailHeight);
        layoutParams.addRule(CENTER_HORIZONTAL);
        layoutParams.setMargins(0, mPopViewTopMargin - (mPopItemDetailHeight - mPopItemHeight) / 2, 0, 0);
        mItemDetailsLinearLayout.setLayoutParams(layoutParams);
        mItemDetailsLinearLayout.setBackgroundColor(ContextCompat.getColor(mContext, DEFAULT_DETAILS_COLOR_RES_ID));

        PropertyValuesHolder stretchVH = PropertyValuesHolder.ofFloat("scaleY", ((float) mPopItemZoomInHeight) / mPopItemDetailHeight, 1f);
        PropertyValuesHolder elevateVH = PropertyValuesHolder.ofFloat("Elevation", 0, 40);
        Animator animator = ObjectAnimator.ofPropertyValuesHolder(mItemDetailsLinearLayout, stretchVH, elevateVH);
        animator.setDuration(500);
        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.setAnimator(LayoutTransition.APPEARING, animator);
        setLayoutTransition(layoutTransition);

        // step 2
        addView(mItemDetailsLinearLayout);
        View detailContentView = mItemDetailsLinearLayout.getChildAt(0);
        ValueAnimator detailAppearAnim = ObjectAnimator.ofFloat(detailContentView, "alpha", 0f, 1f);
        detailAppearAnim.setStartDelay(500);
        detailAppearAnim.setDuration(300).start();

        mShowingDetails = STATE_ITEM_DETAIL_SHOW;
    }

    /**
     * hideItemDetails shows the detail of each item
     * it will do the following thing
     * 1. Make the detail's content disappear slowly (0.3s)
     * 2. Make faked layout (fakedLinearLayout) ZoomOut and disappear (0.5s)
     * 3. Change Color and show the detail content of word (0.3s)
     * */
    public void hideItemDetails(){

        if(mItemDetailsLinearLayout == null)
            return;

        AnimatorSet animatorSet = new AnimatorSet();

        View detailContentView = mItemDetailsLinearLayout.getChildAt(0);
        ValueAnimator detailDisappearAnim = ObjectAnimator.ofFloat(detailContentView, "alpha", 1.0f, 0f);
        detailDisappearAnim.setDuration(300);

        ValueAnimator detailZoomOutAnim = ObjectAnimator.ofFloat(mItemDetailsLinearLayout, "ScaleY", 1.0f, ((float) mPopItemZoomInHeight) / 500);
        detailDisappearAnim.setDuration(500);

        animatorSet.play(detailDisappearAnim).before(detailZoomOutAnim);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mShowingDetails = STATE_ITEM_DETAIL_CHANGING;
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                mItemDetailsLinearLayout.removeAllViews();
                removeView(mItemDetailsLinearLayout);
                mShowingDetails = STATE_ITEM_DETAIL_NOT_SHOW;
            }
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        animatorSet.start();
    }

    public PopItemAdapter getPopItemAdapter(Context context,int resource, LinkedList<HashMap> dataList, String[] from, int[] to){
        mPopItemCount = dataList.size();
        return new PopItemAdapter(context, resource, dataList, from, to);

    }

    public void setPopItemAdapter(PopItemAdapter adapter){
        mScrollView.removeAllViews();
        mScrollView.addView(mLinearLayout);
        adapter.setChildViewToGroup();
        mCurrentFocusedPopItemPosition = -1;

        checkInitialItemStateTask.run();
        checkFinalItemStateTask.run();
    }

    private void setChildSize(){
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        mPopItemHeight = (int) Math.floor(displayMetrics.heightPixels* ((1 - 0.15) / DEFAULT_CHILD_COUNT_IN_SCROLL_VIEW));
        mPopItemWidth = (int) Math.floor(displayMetrics.widthPixels*0.90);
    }

    private void setChildZoomInSize(){
        mPopItemZoomInHeight = mPopItemHeight;
        mPopItemZoomInWidth = (int) Math.floor(mPopItemWidth *ZOOM_IN_FACTORY);
    }

    public void setOnPopScrollStoppedListener(OnPopScrollStoppedListener listener){
        mOnPopScrollStoppedListener = listener;
    }

    public void setOnItemPreparedListener(OnItemPreparedListener listener){
        mOnItemPreparedListener = listener;
    }

    public View getChildView(int index){
        return mLinearLayout.getChildAt(index);
    }

    public View getCurrentFocusedView(){
        return mLinearLayout.getChildAt(mCurrentFocusedPopItemPosition);
    }

    public boolean getInitialItemCheck(){
        return mInitialItemCheckFlag;
    }

    public boolean getFinalItemCheck(){
        return mFinalItemCheckFlag;
    }

    /**
     * The method used for returning the currently being focused view's position, typically means
     * the view's index in the scrollView center
     */
    public int getCurrentFocusedPosition(){
        return mCurrentFocusedPopItemPosition;
    }

    public int getCurrentFocusedPositionCoordinateY(){
        return mCurrentFocusedPopItemPosition * mPopItemHeight;
    }

    public int getCoordinateYByPosition(int position){
        return position* mPopItemHeight;
    }

    public int isShowingDetails(){
        return mShowingDetails;
    }

    private class PopItemAdapter {

        private ViewGroup mParent;
        private int mResource;
        private LinkedList<HashMap> mDataList;
        private String[] mFrom;
        private int[] mTo;
        private LayoutInflater mInflater;

        PopItemAdapter(Context context, int resource, LinkedList<HashMap> dataList, String[] from, int[] to){
            mParent = mLinearLayout;
            mDataList = dataList;
            mResource = resource;
            mFrom = from;
            mTo = to;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void setChildViewToGroup(){

            mParent.removeAllViews();

            Iterator<HashMap> ii = mDataList.iterator();
            int dataCount = mDataList.size();
            int itemFilledStartIndex = 0;
            int itemFilledEndIndex = DEFAULT_PLAYER_LIST_ITEM_COUNT;

            int index = -1;
            while(ii.hasNext()){
                index++;

                final ViewGroup v = (ViewGroup) mInflater.inflate(mResource, mParent, false);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mPopItemWidth, mPopItemHeight);
                if(index == 0)
                    layoutParams.setMargins(0, mPopViewTopMargin, 0, 0);
                else if(index == dataCount - 1)
                    layoutParams.setMargins(0, 0, 0, mPopViewBottomMargin);
                else
                    layoutParams.setMargins(0, 0, 0, 0);

                final HashMap<String, Object> dataMap = ii.next();
                v.setLayoutParams(layoutParams);
                v.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = mParent.indexOfChild(v);
                        if (position != mCurrentFocusedPopItemPosition) {
                            if (mShowingDetails == STATE_ITEM_DETAIL_SHOW)
                                hideItemDetails();
                        } else {
                            if (mShowingDetails == STATE_ITEM_DETAIL_NOT_SHOW)
                                showItemDetails();
                        }
                    }
                });

                for(int i = itemFilledStartIndex; i < itemFilledEndIndex && i < dataMap.size(); i++){
                    /**
                     * here only contains spell and translation.
                     */
                    TextView textView = (TextView) v.findViewById(mTo[i]);
                    if(textView != null) {
                        Typeface kkTypeFace = Typeface.createFromAsset(mContext.getAssets(), "fonts/tt0142m_.ttf");
                        textView.setTypeface(kkTypeFace);
                        textView.setText(dataMap.get(mFrom[i]).toString());
                        textView.setVisibility(VISIBLE);
                    }
                }

                mParent.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mParent.addView(v);
                    }
                }, index * 100);
            }
        }
    }

    private class MyScrollView extends ScrollView{

        private Runnable scrollerTask;
        private OnScrollStoppedListener onScrollStoppedListener;
        private int newCheck = 100;
        private int initialPosition;

        public MyScrollView(Context context) {
            super(context);

            setVerticalScrollBarEnabled(false);
            onScrollStoppedListener = new OnScrollStoppedListener();
            scrollerTask = new Runnable() {
                @Override
                public void run() {
                    int newPosition = getScrollY();
                    if(initialPosition - newPosition == 0){//has stopped
                        if(onScrollStoppedListener!=null){
                            onScrollStoppedListener.onScrollStopped(newPosition);
                        }
                    }else{
                        initialPosition = getScrollY();
                        MyScrollView.this.postDelayed(scrollerTask, newCheck);
                    }
                }
            };

        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
            super.onScrollChanged(l, t, oldl, oldt);
            int newPosition;
            if ((newPosition = findCurrentPosition(t)) != mCurrentFocusedPopItemPosition)
                setCurrentFocusedPosition(newPosition);
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            if(!mInitialItemCheckFlag)
                return super.onTouchEvent(ev);

            switch (ev.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    mOnPopScrollStoppedListener.onPopScrolling();
                    break;
                case MotionEvent.ACTION_UP:
                    startScrollerTask();
                    break;
                default:
                    break;
            }
            return super.onTouchEvent(ev);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            if(ev.getAction() == MotionEvent.ACTION_DOWN){
                if (mShowingDetails == STATE_ITEM_DETAIL_SHOW) {
                    hideItemDetails();
                    return false;
                }
                if(!getInitialItemCheck())
                    return false;

            }
            return super.onInterceptTouchEvent(ev);
        }

        public void startScrollerTask(){

            initialPosition = getScrollY();
            MyScrollView.this.postDelayed(scrollerTask, newCheck);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(MeasureSpec.makeMeasureSpec(mPopViewWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mPopViewHeight, MeasureSpec.EXACTLY));
        }

        private class OnScrollStoppedListener{
            public void onScrollStopped(int scrollEndPositionY){
                int offset = scrollEndPositionY % mPopItemHeight;
                if(offset > Math.abs(mPopItemHeight *0.5)){
                    offset = mPopItemHeight - offset;
                    smoothScrollTo(getScrollX(), scrollEndPositionY + offset);
                }
                else
                    smoothScrollTo(getScrollX(), scrollEndPositionY - offset);

                int newPosition;
                if((newPosition = findCurrentPosition(getScrollY())) != mCurrentFocusedPopItemPosition)
                    setCurrentFocusedPosition(newPosition);

                if(mOnPopScrollStoppedListener != null)
                    mOnPopScrollStoppedListener.onPopScrollStopped(mCurrentFocusedPopItemPosition);
            }
        }
    }

    private class ItemDetailLinearLayout extends LinearLayout {

        public ItemDetailLinearLayout(Context context, int index) {
            super(context);
            View itemDetailView = getItemDetailView();

            setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

            if(itemDetailView != null ) {
                addView(itemDetailView);
                setOrientation(VERTICAL);
            }
            else {
                addView(new ErrorView(context).setErrorMsg("ERROR"));
                Log.d("PopScrollView", "ItemDetailView Error");
            }
        }

    }

}
