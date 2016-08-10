package com.wishcan.www.vocabulazy.main.player.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Outline;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
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
import com.wishcan.www.vocabulazy.widget.NumeralPicker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class PlayerOptionView extends LinearLayout {

    private static final int VIEW_PLAYER_OPTION_TAB_CONTENT_RES_ID = R.layout.player_option_tab_content;
    private static final int VIEW_PLAYER_OPTION_TAB_LAYOUT_RES_ID = R.id.player_option_tab_layout;
    private static final int VIEW_PLAYER_OPTION_TAB_PAGER_RES_ID = R.id.player_option_tab_pager;
    
    public static final int PLAYER_OPTION_RANDOM_VIEW_RES_ID = R.id.action_set_random;
    public static final int PLAYER_OPTION_REPEAT_VIEW_RES_ID = R.id.action_set_repeat;
    public static final int PLAYER_OPTION_SENTENCE_VIEW_RES_ID = R.id.action_set_sentence;
    public static final int PLAYER_OPTION_SECOND_PICKER_RES_ID = R.id.action_picker_second;
    public static final int PLAYER_OPTION_FREQUENCY_PICKER_RES_ID = R.id.action_picker_frequency;
    public static final int PLAYER_OPTION_SPEED_PICKER_RES_ID = R.id.action_picker_speed;
    public static final int PLAYER_OPTION_PLAY_TIME_PICKER_RES_ID = R.id.action_picker_play_time;

    private final int[] mOptionResIDs = {
            PLAYER_OPTION_RANDOM_VIEW_RES_ID,
            PLAYER_OPTION_REPEAT_VIEW_RES_ID,
            PLAYER_OPTION_SENTENCE_VIEW_RES_ID,
            PLAYER_OPTION_SECOND_PICKER_RES_ID,
            PLAYER_OPTION_FREQUENCY_PICKER_RES_ID,
            PLAYER_OPTION_SPEED_PICKER_RES_ID,
            PLAYER_OPTION_PLAY_TIME_PICKER_RES_ID
    };
    
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

    private OnOptionChangedListener mOnOptionChangedListener;

    public PlayerOptionView(Context context) {
        this(context, null);
    }

    public PlayerOptionView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mTabContentList = new LinkedList<>();
        mPagerAdapter = new LinkedListPagerAdapter(mTabContentList);
        mViewPager.setAdapter(mPagerAdapter);
    }
    
    @Override
    protected void onFinishInflate() {
        mTabLayout = findViewById(VIEW_PLAYER_OPTION_TAB_LAYOUT_RES_ID);
        for (int i = 0; i < mTabLayout.childCount(); i++) {
            final nextTabIndex = i;
            View tab = mTabLayout.getChildAt(i);
            tab.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    setCurrentTab(v);
                }
            });
        }

        mViewPager = findViewById(VIEW_PLAYER_OPTION_TAB_PAGER_RES_ID);
        mViewPager.setPagingEnabled(true);
        for (int i = 0; i < mTabLayout.childCount(); i++) {
            ViewGroup tabContent = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(VIEW_PLAYER_OPTION_TAB_CONTENT_RES_ID, null);
            mTabContentList.add(tabContent);
        }

        registerOptionsListener();
    }

    public ViewGroup getTabContent(int index) {
        return mTabContentList.get(index);
    }

    public void setCurrentTab(View v) {
        int nextTabIndex = mTabLayout.indexOfChild(v);
        switchToTabContent(nextTabIndex);
        mCurrentTab = v;
        mCurrentTabIndex = nextTabIndex;
        if (mOnOptionChangedListener != null) {
            mOnOptionChangedListener.onOptionChanged(v, mOptionSettingsLL, mCurrentTabIndex);
        }
    }

    public void setOptionsInTabContent(ArrayList<OptionSettings> optionSettingsLL){

        if (optionSettingsLL == null) {
            mOptionSettingsLL = new ArrayList<>();
            for (int i = 1; i <= 3; i++) {
                mOptionSettingsLL.add(new OptionSettings(i, true, 1, true, i, 0, 1, 2));
            }
        }
        else {
            mOptionSettingsLL = optionSettingsLL;
        }

        Iterator<OptionSettings> ii = mOptionSettingsLL.iterator();
        while (ii.hasNext()) {
            OptionSettings optionSettings = ii.next();
            for (int i = 0; i < mOptionResIDs.length; i++) {
                int optionID = mOptionResIDs[i];
                int mode = optionSettings.getMode() - 1;
                switch (optionID) {
                    case PLAYER_OPTION_RANDOM_VIEW_RES_ID:
                        setOptionInTabContent(mode, optionID, optionSettings.isRandom());
                        break;
                    case PLAYER_OPTION_REPEAT_VIEW_RES_ID:
                        setOptionInTabContent(mode, optionID, optionSettings.getListLoop());
                        break;
                    case PLAYER_OPTION_SENTENCE_VIEW_RES_ID:
                        setOptionInTabContent(mode, optionID, optionSettings.isSentence());
                        break;
                    case PLAYER_OPTION_SECOND_PICKER_RES_ID:
                        setOptionInTabContent(mode, optionID, optionSettings.getStopPeriod());
                        break;
                    case PLAYER_OPTION_FREQUENCY_PICKER_RES_ID:
                        setOptionInTabContent(mode, optionID, optionSettings.getItemLoop());
                        break;
                    case PLAYER_OPTION_SPEED_PICKER_RES_ID:
                        setOptionInTabContent(mode, optionID, optionSettings.getSpeed());
                        break;
                    case PLAYER_OPTION_PLAY_TIME_PICKER_RES_ID:
                        setOptionInTabContent(mode, optionID, optionSettings.getPlayTime());
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void setOptionInTabContent(int mode, int optionID, int option) {
        ViewGroup tabContent = getTabContent(mode);
        View v = tabContent.findViewById(optionID);
        if (v instanceof ImageView) {
            ((ImageView) v).setImageLevel(option);
        }
        else if (v instanceof NumeralPicker) {
            NumeralPicker picker = (NumeralPicker) v;
            switch (optionID) {
                case PLAYER_OPTION_SECOND_PICKER_RES_ID:
                case PLAYER_OPTION_FREQUENCY_PICKER_RES_ID:
                case PLAYER_OPTION_SPEED_PICKER_RES_ID:
                case PLAYER_OPTION_PLAY_TIME_PICKER_RES_ID:
                    picker.calculatePickerRange();
                    picker.setPickerTextStr(String.valueOf(option));
                    break;
                default:
                    break;
            }

        }

    }

    public void setOptionInTabContent(int mode, int optionID, boolean option){
        setOptionInTabContent(mode, optionID, option ? 1 : 0);
    }

    public void setOnOptionChangedListener(OnOptionChangedListener listener) {
        mOnOptionChangedListener = listener;
    }

    private void switchToTabContent(int index) {
        mViewPager.setCurrentItem(index, true);
    }

    private void registerOptionsListener() {
        for (int i = 0; i < mTabCount; i++) {
            ViewGroup viewGroup = getTabContent(i);
            for (int j = 0; j < mOptionResIDs.length; j++) {
                final View v = viewGroup.findViewById(mOptionResIDs[j]);
                if (v instanceof ImageView) {
                    v.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            OptionSettings optionSettings = mOptionSettingsLL.get(mCurrentTabIndex);
                            switch (v.getId()) {
                                case PLAYER_OPTION_RANDOM_VIEW_RES_ID:
                                    optionSettings.setRandom(!optionSettings.isRandom());
                                    ((ImageView) v).setImageLevel(optionSettings.isRandom() ? 1 : 0);
                                    break;
                                case PLAYER_OPTION_REPEAT_VIEW_RES_ID:
                                    optionSettings.setListLoop((optionSettings.getListLoop() + 1) % 4);
                                    ((ImageView) v).setImageLevel(optionSettings.getListLoop());
                                    break;
                                case PLAYER_OPTION_SENTENCE_VIEW_RES_ID:
                                    optionSettings.setSentence(!optionSettings.isSentence());
                                    ((ImageView) v).setImageLevel(optionSettings.isSentence() ? 1 : 0);
                                    break;
                                default:
                                    break;
                            }
                            if (mOnOptionChangedListener != null) {
                                mOnOptionChangedListener.onOptionChanged(v, mOptionSettingsLL, mCurrentTabIndex);
                            }
                        }
                    });
                }
                else if (v instanceof NumeralPicker) {
                    ((NumeralPicker) v).setOnPickerClickedListener(new NumeralPicker.OnPickerClickedListener() {
                        @Override
                        public void onPickerClicked(String valueStr) {
                            OptionSettings optionSettings = mOptionSettingsLL.get(mCurrentTabIndex);
                            switch (v.getId()) {
                                case PLAYER_OPTION_SECOND_PICKER_RES_ID:
                                    optionSettings.setStopPeriod(Integer.valueOf(valueStr));
                                    break;
                                case PLAYER_OPTION_FREQUENCY_PICKER_RES_ID:
                                    optionSettings.setItemLoop(Integer.valueOf(valueStr));
                                    break;
                                case PLAYER_OPTION_SPEED_PICKER_RES_ID:
                                    optionSettings.setSpeed(Integer.valueOf(valueStr));
                                    break;
                                case PLAYER_OPTION_PLAY_TIME_PICKER_RES_ID:
                                    optionSettings.setPlayTime(Integer.valueOf(valueStr));
                                    break;
                                default:
                                    break;
                            }
                            if (mOnOptionChangedListener != null) {
                                mOnOptionChangedListener.onOptionChanged(v, mOptionSettingsLL, mCurrentTabIndex);
                            }
                        }
                    });
                }
            }
        }
    }

    /**
     * PlayerOptionTabView is our Tab items, which is a RelativeLayout containing a
     * PlayerOptionImageView (a trapezoid) and a TextView(the tab item's title)
     * */
    public class PlayerOptionTabView extends RelativeLayout{

        private Context mContext;
        private TextView mTextView;
        private ShapeDrawable shadowDrawable;
        private int mShadowLongHeight;
        private String mTagString;

        public PlayerOptionTabView(Context context) {
            this(context, null);
        }

        public PlayerOptionTabView(Context context, AttributeSet attrs) {
            super(context, attrs);

            mContext = context;

            int shadowWidth = getResources().getDisplayMetrics().widthPixels / 3;
            int shadowLongHeight = mShadowLongHeight =(int) getResources().getDimension(DEFAULT_TRAPEZOID_LONG_HEIGHT);
            int shadowShortHeight =(int) getResources().getDimension(DEFAULT_TRAPEZOID_SHORT_HEIGHT);

            Path path = setPathToTrapezoid(shadowWidth, shadowLongHeight, shadowShortHeight);
            shadowDrawable = new ShapeDrawable(new PathShape(path, shadowWidth, shadowLongHeight));

            init();
            initAllLayout();
        }

        private void init(){
            mTagString = "Tag";
        }

        private void initAllLayout(){

            setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, mShadowLongHeight));

            setBackground(shadowDrawable);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // this is important, change outline make shadow appear
                setOutlineProvider(new TrapezoidOutlineProvider());
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

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(mShadowLongHeight, MeasureSpec.EXACTLY));
        }
    }

    @TargetApi(21)
    private class TrapezoidOutlineProvider extends ViewOutlineProvider {

        int width, longHeight, shortHeight;

        public TrapezoidOutlineProvider() {
            width = getResources().getDisplayMetrics().widthPixels / 3;
            longHeight = (int) getResources().getDimension(DEFAULT_TRAPEZOID_LONG_HEIGHT);
            shortHeight = (int) getResources().getDimension(DEFAULT_TRAPEZOID_SHORT_HEIGHT);
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
    public class WrapContentViewPager extends ViewPager {

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