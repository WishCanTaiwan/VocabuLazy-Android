package com.wishcan.www.vocabulazy.view.tab;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.LinkedList;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.view.adapter.LinkedListPagerAdapter;

/**
 * Created by swallow on 2015/9/19.
 * TabView is a LinearLayout mainly containing two Views
 * 1. ViewPager, mViewPager, which is used to show the tab's content
 * 2. TabStripe, mTabStripe, which is used to contain TabItem
 * The View which extends TabView should do the following steps
 * 1. Create your own TabItem, which can be customized with either String or a drawable
 * 2. Create another content View corresponding to the TabItem
 * 3. Add both TabItem in step1 and content View in step2 by calling addTabAndTabContent
 */
public class TabView extends LinearLayout {

    private LinearLayout.LayoutParams mDefaultViewGroupLayoutParams;

    private LinearLayout.LayoutParams mDefaultViewPagerLayoutParams;

    /**
     * mTabStripe is used to contain several TabItems
     */
    private TabStripe mTabStripe;

    /**
     * mTabContentList is used to contain the content view corresponding to TabItem.
     * mTabContentList will be used in creating our own PagerAdapter TabContentSlidePagerAdapter
     */
    private LinkedList<ViewGroup> mTabContentList;

    /**
     * mViewPager is the ViewGroup for showing tab content view based on the current tab
     */
    private ViewPager mViewPager;

    public TabView(Context context) {
        super(context);
    }

    /**
     * The constructor is used to initialize several object in TabView, including
     * 1. ViewPager
     * 2. TabStripe
     * 3. LinkedList, for containing the tab content, which will be used in PagerAdapter
     */
    public TabView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mDefaultViewGroupLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        setOrientation(VERTICAL);

        mDefaultViewPagerLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 1);

        mViewPager = new ViewPager(context);

        mTabStripe = new TabStripe(context);

        mTabContentList = new LinkedList<>();

        mViewPager.setLayoutParams(mDefaultViewPagerLayoutParams);
        mViewPager.addOnPageChangeListener(new OnPageChangeListener());

        addView(mViewPager);
        addView(mTabStripe);
    }

    public void addTabAndTabContent(TabItem tabItem, ViewGroup tabContent) {

        mTabStripe.addTabItem(tabItem);

        mTabContentList.add(tabContent);

        tabItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentTab(v);
            }
        });

        mViewPager.setAdapter(new LinkedListPagerAdapter(mTabContentList));
    }

    public void setCurrentTab(View v) {

        int nextTabIndex = mTabStripe.indexOfItem(v);
        switchToTabContent(nextTabIndex);
        mTabStripe.setCurrentTabIndex(nextTabIndex);

    }

    public TabItem getCurrentTab() {
        return mTabStripe.getCurrentTabItem();
    }

    public ViewGroup getCurrentTabContent() {
        return mTabContentList.get(mTabStripe.getCurrentTabIndex());
    }

    public ViewGroup getTabContent(int index) {
        return mTabContentList.get(index);
    }

    public void setTabStripeColor(int color) {
        mTabStripe.setTabStripeColor(color);
    }

    public void setOnPagerChangeListener(OnPageChangeListener listener) {
        mViewPager.clearOnPageChangeListeners();
        mViewPager.addOnPageChangeListener(listener);
    }

    public void setCurrentTabIndex(int index) {
        mTabStripe.setCurrentTabIndex(index);
    }

    public int getCurrentTabIndex() {
        return mTabStripe.getCurrentTabIndex();
    }

    public void switchToTabContent(int index) {
        mTabStripe.moveTabMask(index);
        mViewPager.setCurrentItem(index, true);
    }

    /**
     * TabStripe is a RelativeLayout because the view have a special capability, mask.
     * The view is used for containing the following objects
     * 1. mTabStripe, which is a LinearLayout containing several TabItems
     * 2. mTabMask, which is just a view with transparent black color, for covering the current Tab
     * to show which Tab is currently focused
     */
    private class TabStripe extends RelativeLayout {

        private static final int DEFAULT_HEIGHT_RES_ID = R.dimen.tab_view_tab_stripe;

        private static final int DEFAULT_MASK_COLOR_RES_ID = R.color.main_tab_view_tab_mask;

        private LinearLayout mTabStripe;

        private View mTabMask;

        private LinearLayout.LayoutParams defaultViewGroupLayoutParams;

        private LinearLayout.LayoutParams defaultItemLayoutParams;

        private int mCurrentTabIndex;

        private int mDefaultTabStripeHeight;

        public TabStripe(Context context) {
            super(context);

            initDefaultTabStripe();

        }

        /**
         * Initialize the mTabStripe and the LayoutParams, which will be used when TabItem is added
         */
        private void initDefaultTabStripe() {
            // setting ViewGroup LayoutParams
            defaultViewGroupLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            setLayoutParams(defaultViewGroupLayoutParams);
            mTabStripe = new LinearLayout(getContext());
            mTabStripe.setLayoutParams(defaultViewGroupLayoutParams);
            mTabStripe.setOrientation(HORIZONTAL);
            addView(mTabStripe);

            // setting TabItem being added LayoutParams
            mDefaultTabStripeHeight = (int) getResources().getDimension(DEFAULT_HEIGHT_RES_ID);
            defaultItemLayoutParams = new LinearLayout.LayoutParams(0, mDefaultTabStripeHeight, 1);

            // setting TabMask for marking which TabItem is the mCurrentTabItem
            mTabMask = new View(getContext());
            mTabMask.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, mDefaultTabStripeHeight));
            mTabMask.setBackgroundColor(getResources().getColor(DEFAULT_MASK_COLOR_RES_ID));
            addView(mTabMask);

            mCurrentTabIndex = -1;
        }

        public void addTabItem(TabItem item) {
            item.setLayoutParams(defaultItemLayoutParams);
            if (mTabStripe.getChildCount() == 0)
                mCurrentTabIndex = 0;
            mTabStripe.addView(item);
            modifyMaskSize();
        }

        public void setTabStripeColor(int color) {
            mTabStripe.setBackgroundColor(color);
        }

        public int indexOfItem(View v) {
            return mTabStripe.indexOfChild(v);
        }

        public TabItem getCurrentTabItem() {
            return (TabItem) mTabStripe.getChildAt(mCurrentTabIndex);
        }

        public void setCurrentTabIndex(int index) {
            if (index >= mTabStripe.getChildCount() || index < 0)
                return;
            mCurrentTabIndex = index;
        }

        public int getCurrentTabIndex() {
            return mCurrentTabIndex;
        }

        /**
         * Do the same thing with moveTabMask(int, float), but used for confirming the final
         * position of mask will be on the Tab
         */
        private void moveTabMask(int position) {
            int tabStripeWidth = mTabStripe.getMeasuredWidth();
            float offset = position * tabStripeWidth * (1f / mTabStripe.getChildCount());
            mTabMask.setTranslationX(offset);
        }

        /**
         * Move the mTabMask based on the current position and its positionOffset.
         * Among these two parameters,
         * positionOffset is defined with the ViewPager's scrolling portion
         */
        private void moveTabMask(int position, float positionOffset) {
            if (positionOffset <= 0)
                return;
            int tabStripeWidth = mTabStripe.getMeasuredWidth();
            float offsetUnit = tabStripeWidth * (1f / mTabStripe.getChildCount());
            float offset = (position + positionOffset) * offsetUnit;

            mTabMask.setTranslationX(offset);
        }

        /**
         * Because when we create the mask, we don't know the real size of the TabView will be.
         * So we just modified the mask by scaleX() based on the TabItem already added.
         * (Because all TabItems' weight are set with 1)
         */
        private void modifyMaskSize() {

            //setPivotX(0) is due to we want to move mask based on the (0,0)
            mTabMask.setPivotX(0);
            mTabMask.setScaleX(1f / mTabStripe.getChildCount());
            ((RelativeLayout.LayoutParams) mTabMask.getLayoutParams()).addRule(ALIGN_PARENT_LEFT);
        }


    }

    /**
     * Our own TabItem for being added to TabStripe
     */
    protected class TabItem extends RelativeLayout {

        private static final String DEFAULT_TAG_STR = "tagItem";

        private String mTag;

        private Context mContext;

        private ImageView imageView;

        private TextView textView;

        private RelativeLayout.LayoutParams defaultLayoutParams;

        public TabItem(Context context) {
            super(context);

            mContext = context;

            initDefaultTabItem();
        }

        private void initDefaultTabItem() {

            mTag = DEFAULT_TAG_STR;

            defaultLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            defaultLayoutParams.addRule(CENTER_IN_PARENT);

            textView = new TextView(mContext);
            textView.setText(DEFAULT_TAG_STR);

            textView.setLayoutParams(defaultLayoutParams);
            addView(textView);
        }

        public void setTabItemTag(String str) {
            mTag = str;
        }

        public String getTabItemTag() {
            return mTag;
        }

        public void setDrawableIcon(Drawable drawable) {
            if (getChildCount() != 0)
                removeAllViews();

            imageView = new ImageView(mContext);
            imageView.setImageDrawable(drawable);
            addView(imageView);
        }

        public void setTextIcon(String str) {
            if (getChildCount() != 0)
                removeAllViews();

            textView = new TextView(mContext);
            textView.setText(str);
        }

        public void setTextIconColor(int color) {
            textView.setTextColor(color);
        }

        public void setIconBackgroundColor(int color) {
            setBackgroundColor(color);
        }

    }

    /**
     * Customized OnPageChangeListener for moving TabMask when ViewPager scrolling or selected
     */
    protected class OnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            mTabStripe.moveTabMask(position, positionOffset);
        }

        @Override
        public void onPageSelected(int position) {
            mTabStripe.moveTabMask(position);
            getCurrentTabContent().requestFocus();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}