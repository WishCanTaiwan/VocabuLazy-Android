package com.wishcan.www.vocabulazy.main.player.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.storage.databaseObjects.OptionSettings;
import com.wishcan.www.vocabulazy.widget.LinkedListPagerAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class PlayerOptionViewNew extends LinearLayout {

    private static final int DEFAULT_TRAPEZOID_LONG_HEIGHT = R.dimen.player_option_trapezoid_long_height;
    private static final int DEFAULT_TRAPEZOID_SHORT_HEIGHT = R.dimen.player_option_trapezoid_short_height;
    
    public interface OnOptionChangedListener{
        void onOptionChanged(int optionID, int mode, View v);
    }

    private static final int VIEW_PLAYER_OPTION_TAB_LAYOUT_RES_ID = R.id.player_option_tab_layout;
    private static final int VIEW_PLAYER_OPTION_TAB_PAGER_RES_ID = R.id.player_option_tab_pager;

    private Context mContext;

    /**
     * This linearlayout is used to place tabs, all tabs weight is set to 1
     * */
    private LinearLayout mTabLayout;

    /**
     * ViewPager usually doesn't support wrap_content
     * */
    private WrapContentViewPager mViewPager;

    /**
     * TabContentSlidePagerAdapter is used for giving the ViewPager the content
     * */
    private PagerAdapter mPagerAdapter;

    /**
     * mTabContentList contains all TabContent
     */
    private LinkedList<ViewGroup> mTabContentList;

    /**
     *
     * */
    private PlayerOptionTabView mCurrentTab;

    /**
     *
     * */
    private int mCurrentTabIndex;

    private OnOptionChangedListener mOnOptionChangedListener;

    public PlayerOptionViewNew(Context context) {
        this(context, null);
    }

    public PlayerOptionViewNew(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        mTabContentList = new LinkedList<>();

    }
    
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTabLayout = (LinearLayout) findViewById(VIEW_PLAYER_OPTION_TAB_LAYOUT_RES_ID);
        for (int i = 0; i < mTabLayout.getChildCount(); i++) {
            View tab = mTabLayout.getChildAt(i);
            tab.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    setCurrentTab(v);
                }
            });
        }

        mViewPager = (WrapContentViewPager) findViewById(VIEW_PLAYER_OPTION_TAB_PAGER_RES_ID);
        for (int i = 0; i < mViewPager.getChildCount(); i++) {
            PlayerOptionContentView tabContent = (PlayerOptionContentView) mViewPager.getChildAt(i);
            tabContent.setContentBackgroundColor(((PlayerOptionTabView) mTabLayout.getChildAt(i)).getColor());
            mTabContentList.add(tabContent);
        }
        mPagerAdapter = new LinkedListPagerAdapter(mTabContentList);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setPagingEnabled(true);

        registerOptionsListener();
    }

    public PlayerOptionContentView getTabContent(int index) {
        return (PlayerOptionContentView) mTabContentList.get(index);
    }

    public void setCurrentTab(View v) {
        int nextTabIndex = mTabLayout.indexOfChild(v);
        mViewPager.setCurrentItem(nextTabIndex, true);
        mCurrentTab = (PlayerOptionTabView) v;
        for (int i = nextTabIndex, k = 0; k < getChildCount(); i = (i + 1) % mTabLayout.getChildCount(), k++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                PlayerOptionTabView tabView = (PlayerOptionTabView) mTabLayout.getChildAt(i);
                tabView.setElevation(k == 0 ? 50 : k == 1 ? 20 : 10);
            }
        }
        mCurrentTabIndex = nextTabIndex;
    }

    public void setOptionsInTabContent(ArrayList<OptionSettings> optionSettingsLL) {

        if (optionSettingsLL == null) { /** if no option setting, using default value */
            return;
        }

        Iterator<OptionSettings> ii = optionSettingsLL.iterator();
        while (ii.hasNext()) {
            OptionSettings optionSettings = ii.next();
            int mode = optionSettings.getMode() - 1;
            PlayerOptionContentView tabContent = getTabContent(mode);
            tabContent.setOptionSettings(optionSettings);
        }
    }

    public void setOnOptionChangedListener(OnOptionChangedListener listener) {
        mOnOptionChangedListener = listener;
    }

    private void registerOptionsListener() {
        for (int i = 0; i < mTabLayout.getChildCount(); i++) {
            final int nextTabIndex = i;
            PlayerOptionContentView tabContent = getTabContent(i);
            tabContent.setOnOptionClickListener(new PlayerOptionContentView.OnOptionClickListener() {
                @Override
                public void onOptionClick(int optionID, View v) {
                    if (mOnOptionChangedListener != null) {
                        /** Tab index is also mode id */
                        mOnOptionChangedListener.onOptionChanged(optionID, nextTabIndex, v);
                    }
                }
            });
        }
    }

    /**
     * PlayerOptionTabView is our Tab items, which is a RelativeLayout containing a
     * PlayerOptionImageView (a trapezoid) and a TextView(the tab item's title)
     * */
    public static class PlayerOptionTabView extends RelativeLayout{

        private static final int COLOR_TAB_RES_ID = R.color.player_option_tab0;
        private static final int STR_TAB_RES_ID = R.string.player_option_tab_0;
        private static final int COLOR_TAB_TEXT_RES_ID = R.color.player_option_tab0_text;
        private static final int DEFAULT_TAB_TEXT_SIZE_RES_ID = R.dimen.player_option_tab_text;

        private static final int PLAYER_OPTION_TAB_ATTRIBUTE_s[] = {
            R.styleable.PlayerOptionTabView_setPlayerTabBackgroundColor,
            R.styleable.PlayerOptionTabView_setPlayerTabStringColor,
            R.styleable.PlayerOptionTabView_setPlayerTabString,
            R.styleable.PlayerOptionTabView_setPlayerTabStringSize
        };

        private Context mContext;
        private TextView mTextView;
        private ShapeDrawable shadowDrawable;
        private int mShadowLongHeight;
        private String mTagString;
        private int mTagStringColor;
        private int mBackgroundColor;
        private int mTagStringSize;

        public PlayerOptionTabView(Context context) {
            this(context, null);
        }

        public PlayerOptionTabView(Context context, AttributeSet attrs) {
            super(context, attrs);

            mContext = context;
            TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PlayerOptionTabView, 0, 0);

            int shadowWidth = getResources().getDisplayMetrics().widthPixels / 3;
            int shadowLongHeight = mShadowLongHeight =(int) getResources().getDimension(DEFAULT_TRAPEZOID_LONG_HEIGHT);
            int shadowShortHeight =(int) getResources().getDimension(DEFAULT_TRAPEZOID_SHORT_HEIGHT);

            Path path = setPathToTrapezoid(shadowWidth, shadowLongHeight, shadowShortHeight);
            shadowDrawable = new ShapeDrawable(new PathShape(path, shadowWidth, shadowLongHeight));

            initAllLayout();

            try {
                final int N = PLAYER_OPTION_TAB_ATTRIBUTE_s.length;

                for (int i = 0; i < N; i++) {
                    int attribute = PLAYER_OPTION_TAB_ATTRIBUTE_s[i];
                    switch(attribute) {
                        case R.styleable.PlayerOptionTabView_setPlayerTabBackgroundColor:
                            mBackgroundColor = ta.getColor(attribute, ContextCompat.getColor(context, COLOR_TAB_RES_ID));
                            break;
                        case R.styleable.PlayerOptionTabView_setPlayerTabStringColor:
                            mTagStringColor = ta.getColor(attribute, ContextCompat.getColor(context, COLOR_TAB_TEXT_RES_ID));
                            break;
                        case R.styleable.PlayerOptionTabView_setPlayerTabString:
                            mTagString = ta.getString(attribute);
                            break;
                        case R.styleable.PlayerOptionTabView_setPlayerTabStringSize:
                            mTagStringSize = ta.getDimensionPixelSize(attribute, 10);
                            break;
                    }
                }
            } finally {
                setColor(mBackgroundColor);
                setTextStr(mTagString);
                setTextColor(mTagStringColor);
                setTextSize(TypedValue.COMPLEX_UNIT_PX, mTagStringSize);
            }
        }

        private void initAllLayout(){

            setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, mShadowLongHeight));

            setBackground(shadowDrawable);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                /** this is important, change outline make shadow appear */
                setOutlineProvider(new TrapezoidOutlineProvider(mContext));
                setClipToOutline(true);
            }

            setClipToPadding(false);
            mTextView = new TextView(mContext);
            mTextView.setText(mTagString);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(CENTER_IN_PARENT);
            mTextView.setLayoutParams(layoutParams);

            addView(mTextView);
        }

        private Path setPathToTrapezoid(int width, int longHeight, int shortHeight){

            Path path  = new Path();
            path.moveTo(0, 0);
            path.lineTo(0, longHeight);
            path.lineTo(width, longHeight);
            path.lineTo(width, longHeight - shortHeight);
            return path;
        }

        public void setColor(int color){
            shadowDrawable.getPaint().setColor(color);
            shadowDrawable.invalidateSelf();
        }

        public void setTextStr(String str){
            mTextView.setText(str);
        }

        public void setTextColor(int color){
            mTextView.setTextColor(color);
        }

        public void setTextSize(int size){
            mTextView.setTextSize(size);
        }

        public void setTextSize(int unit, float size){
            mTextView.setTextSize(unit, size);
        }

        public int getColor() {
            return mBackgroundColor;
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(mShadowLongHeight, MeasureSpec.EXACTLY));
        }
    }

    @TargetApi(21)
    private static class TrapezoidOutlineProvider extends ViewOutlineProvider {

        int width, longHeight, shortHeight;

        public TrapezoidOutlineProvider(Context context) {
            width = context.getResources().getDisplayMetrics().widthPixels / 3;
            longHeight = (int) context.getResources().getDimension(DEFAULT_TRAPEZOID_LONG_HEIGHT);
            shortHeight = (int) context.getResources().getDimension(DEFAULT_TRAPEZOID_SHORT_HEIGHT);
        }

        @Override
        public void getOutline(View view, Outline outline) {
            Path path  = new Path();
            path.moveTo(0, 0);
            path.lineTo(0, longHeight);
            path.lineTo(width, longHeight);
            path.lineTo(width, longHeight - shortHeight);
            outline.setConvexPath(path);
        }
    }

    /**
     * WrapContentViewPager enables ViewPager can be assigned a specific size.
     * */
    public static class WrapContentViewPager extends ViewPager {

        private Context mContext;
        private boolean isPagingEnabled = true;

        public WrapContentViewPager(Context context) {
            this(context, null);
        }

        public WrapContentViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
            mContext = context;
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int)(mContext.getResources().getDisplayMetrics().heightPixels * 0.5), MeasureSpec.EXACTLY);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return this.isPagingEnabled && super.onTouchEvent(event);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent event) {
            return this.isPagingEnabled && super.onInterceptTouchEvent(event);
        }

        public void setPagingEnabled(boolean b) {
            this.isPagingEnabled = b;
        }
    }
}