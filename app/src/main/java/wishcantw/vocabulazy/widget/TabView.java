package wishcantw.vocabulazy.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import wishcantw.vocabulazy.R;

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
public class TabView extends LinearLayout implements ViewPager.OnPageChangeListener {

    public interface OnTabChangeListener {
        void onTabSelected(int position);
    }

//    abstract public void initWhenTabIsSelected(int position);

    /**
     * mTabStripe is used to contain several TabItems
     */
    private TabStripe mTabStripe;

    /**
     * mTabContentList is used to contain the content view corresponding to TabItem.
     * mTabContentList will be used in creating our own PagerAdapter TabContentSlidePagerAdapter
     */
//    private LinkedList<Class<?>> mTabContentList;

    /**
     * mViewPager is the ViewGroup for showing tab content view based on the current tab
     */
    private ViewPager mViewPager;

    private Context mContext;
    private LayoutParams mDefaultViewPagerLayoutParams;
    private OnTabChangeListener mOnTabChangeListener;

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
        mContext = context;
        setOrientation(VERTICAL);
        setBackgroundColor(Color.WHITE);
        mDefaultViewPagerLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1);
    }

    public void setUpWithViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(this);
        CustomFragmentPagerAdapter pagerAdapter = (CustomFragmentPagerAdapter) mViewPager.getAdapter();
        mTabStripe = new TabStripe(mContext);
        for (int index = 0; index < pagerAdapter.getCount(); index++) {
            TabItem tabItem = new TabItem(mContext, pagerAdapter.getDrawable(index), pagerAdapter.getTag(index));
            tabItem.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setCurrentTab(v);
                }
            });
            mTabStripe.addTabItem(tabItem);
        }

        addView(mViewPager);
        addView(mTabStripe);
    }

    private void setCurrentTab(View v) {
        int nextTabIndex = mTabStripe.indexOfItem(v);
        setCurrentTab(nextTabIndex);
    }

    public void setCurrentTab(int position){
        switchToTabContent(position);
        mTabStripe.setCurrentTabIndex(position);
//        initWhenTabIsSelected(position);
    }

    public void switchToTabContent(int index) {
        mTabStripe.moveTabMask(index);
        mViewPager.setCurrentItem(index, true);
    }

    public void setTabStripeColor(int color) {
        mTabStripe.setTabStripeColor(color);
    }

    public void addOnTabChangeListener(OnTabChangeListener listener) {
        mOnTabChangeListener = listener;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mTabStripe.moveTabMask(position, positionOffset);
    }

    @Override
    public void onPageSelected(int position) {
        mTabStripe.moveTabMask(position);
//        initWhenTabIsSelected(position);
        mOnTabChangeListener.onTabSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

//    public void addTabAndTabContent(TabItem tabItem, Class<?> clss) {
//
//        mTabStripe.addTabItem(tabItem);
//
////        mTabContentList.add(clss);
//
//        tabItem.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setCurrentTab(v);
//            }
//        });
//
////        mViewPager.setAdapter(new LinkedListFragmentPagerAdapter(
////                ((MainActivity) getContext()), mTabContentList));
//    }

//    public TabItem getCurrentTab() {
//        return mTabStripe.getCurrentTabItem();
//    }

//    public Fragment getCurrentTabContent() {
////        return mTabContentList.get(mTabStripe.getCurrentTabIndex());
//        return null;
//    }

//    public Fragment getTabContent(int index) {
////        return mTabContentList.get(index);
//        return null;
//    }

//    public void setOnPagerChangeListener(OnPageChangeListener listener) {
//        mViewPager.clearOnPageChangeListeners();
//        mViewPager.addOnPageChangeListener(listener);
//    }

//    private void setCurrentTabIndex(int index) {
//        mTabStripe.setCurrentTabIndex(index);
//    }

//    public int getCurrentTabIndex() {
//        return mTabStripe.getCurrentTabIndex();
//    }

//    public int getTabCount() {
//        if(mTabStripe != null)
//            return mTabStripe.getChildCount();
//        else
//            return 0;
//    }

    /**
     * TabStripe is a RelativeLayout because the view have a special capability, mask.
     * The view is used for containing the following objects
     * 1. mTabStripe, which is a LinearLayout containing several TabItems
     * 2. mTabMask, which is just a view with transparent black color, for covering the current Tab
     * to show which Tab is currently focused
     */
    protected class TabStripe extends RelativeLayout {

        private static final int DEFAULT_HEIGHT_RES_ID = R.dimen.widget_tab_height;
        private static final int DEFAULT_MASK_COLOR_RES_ID = R.color.widget_tab_view_mask;

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
            mTabMask.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, mDefaultTabStripeHeight));
            mTabMask.setBackgroundColor(ContextCompat.getColor(getContext(), DEFAULT_MASK_COLOR_RES_ID));
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

        public void setCurrentTabIndex(int index) {
            if (index >= mTabStripe.getChildCount() || index < 0)
                return;
            mCurrentTabIndex = index;
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
            ((LayoutParams) mTabMask.getLayoutParams()).addRule(ALIGN_PARENT_LEFT);
        }

//        public TabItem getCurrentTabItem() {
//            return (TabItem) mTabStripe.getChildAt(mCurrentTabIndex);
//        }

//        public int getCurrentTabIndex() {
//            return mCurrentTabIndex;
//        }
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
        private LayoutParams defaultLayoutParams;

        public TabItem(Context context) {
            super(context);
            mContext = context;
            initDefaultTabItem();
        }

        public TabItem(Context context, Drawable drawable, String tag) {
            super(context);
            mContext = context;
            setDrawableIcon(drawable);
            setTabItemTag(tag);
        }

        private void initDefaultTabItem() {

            mTag = DEFAULT_TAG_STR;

            defaultLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            defaultLayoutParams.addRule(CENTER_IN_PARENT);

            textView = new TextView(mContext);
            textView.setText(DEFAULT_TAG_STR);

            textView.setLayoutParams(defaultLayoutParams);
            addView(textView);
        }

        public void setTabItemTag(String str) {
            mTag = str;
        }


        public void setDrawableIcon(Drawable drawable) {
            if (getChildCount() != 0)
                removeAllViews();

            imageView = new ImageView(mContext);
            imageView.setImageDrawable(drawable);
            addView(imageView);
        }


//        public String getTabItemTag() {
//            return mTag;
//        }

//        public void setTextIcon(String str) {
//            if (getChildCount() != 0)
//                removeAllViews();
//
//            textView = new TextView(mContext);
//            textView.setText(str);
//        }

//        public void setTextIconColor(int color) {
//            textView.setTextColor(color);
//        }

//        public void setIconBackgroundColor(int color) {
//            setBackgroundColor(color);
//        }

    }

//    /**
//     * Customized OnPageChangeListener for moving TabMask when ViewPager scrolling or selected
//     */
//    public class OnPageChangeListener implements ViewPager.OnPageChangeListener {
//
//        @Override
//        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
////            mTabStripe.moveTabMask(position, positionOffset);
//        }
//
//        @Override
//        public void onPageSelected(int position) {
////            mTabStripe.moveTabMask(position);
////            initWhenTabIsSelected(position);
//        }
//
//        @Override
//        public void onPageScrollStateChanged(int state) {
//
//        }
//    }
}