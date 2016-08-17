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

/**
 * Created by swallow on 2015/8/20.
 * this Layout will be a TabLayout, which is used for switching between our configuration
 */
public class PlayerOptionView extends LinearLayout {

    public static final String TAG = PlayerOptionView.class.getSimpleName();

    public interface OnOptionChangedListener{
        void onOptionChanged(View v, ArrayList<OptionSettings> optionSettingsLL, int currentMode);
    }

    public static final int PLAYER_OPTION_RANDOM_VIEW_RES_ID = R.id.action_set_random;
    public static final int PLAYER_OPTION_REPEAT_VIEW_RES_ID = R.id.action_set_repeat;
    public static final int PLAYER_OPTION_SENTENCE_VIEW_RES_ID = R.id.action_set_sentence;
    public static final int PLAYER_OPTION_SECOND_PICKER_RES_ID = R.id.action_picker_second;
    public static final int PLAYER_OPTION_FREQUENCY_PICKER_RES_ID = R.id.action_picker_frequency;
    public static final int PLAYER_OPTION_SPEED_PICKER_RES_ID = R.id.action_picker_speed;
    public static final int PLAYER_OPTION_PLAY_TIME_PICKER_RES_ID = R.id.action_picker_play_time;

    private static final int DEFAULT_TRAPEZOID_LONG_HEIGHT = R.dimen.player_option_trapezoid_long_height;
    private static final int DEFAULT_TRAPEZOID_SHORT_HEIGHT = R.dimen.player_option_trapezoid_short_height;
    private static final int DEFAULT_PLAYER_OPTION_VIEW_RES_ID = R.layout.view_player_option_content;
    private static final int COLOR_TAB_0_RES_ID = R.color.player_option_tab0;
    private static final int COLOR_TAB_1_RES_ID = R.color.player_option_tab1;
    private static final int COLOR_TAB_2_RES_ID = R.color.player_option_tab2;
    private static final int COLOR_TAB_TEXT_0_RES_ID = R.color.player_option_tab0_text;
    private static final int COLOR_TAB_TEXT_1_RES_ID = R.color.player_option_tab1_text;
    private static final int COLOR_TAB_TEXT_2_RES_ID = R.color.player_option_tab2_text;
    private static final int DEFAULT_TAB_TEXT_SIZE_RES_ID = R.dimen.player_option_tab_text;
    private static final int STR_TAB_0_RES_ID = R.string.player_option_tab_0;
    private static final int STR_TAB_1_RES_ID = R.string.player_option_tab_1;
    private static final int STR_TAB_2_RES_ID = R.string.player_option_tab_2;

    private final int[] mOptionResIDs = {
            PLAYER_OPTION_RANDOM_VIEW_RES_ID,
            PLAYER_OPTION_REPEAT_VIEW_RES_ID,
            PLAYER_OPTION_SENTENCE_VIEW_RES_ID,
            PLAYER_OPTION_SECOND_PICKER_RES_ID,
            PLAYER_OPTION_FREQUENCY_PICKER_RES_ID,
            PLAYER_OPTION_SPEED_PICKER_RES_ID,
            PLAYER_OPTION_PLAY_TIME_PICKER_RES_ID
    };

    /**
     *
     * */
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
     * These three imageview is used for adding to mTabView
     * */
    private PlayerOptionTabView mTabView0;
    private PlayerOptionTabView mTabView1;
    private PlayerOptionTabView mTabView2;

    /**
     * Three corresponding content is used for adding to mTabView as well
     * */
    private LinearLayout mTabContent0;
    private LinearLayout mTabContent1;
    private LinearLayout mTabContent2;

    private View mCurrentTab;
    private int mCurrentTabIndex;
    private LinkedList<ViewGroup> mTabContentList;
    private ArrayList<OptionSettings> mOptionSettingsLL;
    private OnOptionChangedListener mListener;
    private int mTabCount;

    public PlayerOptionView(Context context) {
        this(context, null);
    }

    public PlayerOptionView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        /**
         * Initialize the LinearLayout
         * */
        initAllLayout();

        // This part is our customized part, we can call this function outside and add to this view.
        // Just like use other view.
        mTabView0 = new PlayerOptionTabView(mContext);
        mTabView0.setColor(ContextCompat.getColor(mContext, COLOR_TAB_0_RES_ID));
        mTabView0.setTextColor(ContextCompat.getColor(mContext, COLOR_TAB_TEXT_0_RES_ID));
        mTabView0.setTextStr(getResources().getString(STR_TAB_0_RES_ID));
        mTabView0.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(DEFAULT_TAB_TEXT_SIZE_RES_ID));
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            mTabView0.setElevation(50);
        mTabContent0 = (LinearLayout)((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(DEFAULT_PLAYER_OPTION_VIEW_RES_ID, null);
        mTabContent0.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mTabContent0.setBackgroundColor(ContextCompat.getColor(mContext, COLOR_TAB_0_RES_ID));

        mTabView1 = new PlayerOptionTabView(mContext);
        mTabView1.setColor(ContextCompat.getColor(mContext, COLOR_TAB_1_RES_ID));
        mTabView1.setTextColor(ContextCompat.getColor(mContext, COLOR_TAB_TEXT_1_RES_ID));
        mTabView1.setTextStr(getResources().getString(STR_TAB_1_RES_ID));
        mTabView1.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(DEFAULT_TAB_TEXT_SIZE_RES_ID));
        mTabContent1 = (LinearLayout)((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(DEFAULT_PLAYER_OPTION_VIEW_RES_ID, null);
        mTabContent1.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mTabContent1.setBackgroundColor(ContextCompat.getColor(mContext, COLOR_TAB_1_RES_ID));
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            mTabView1.setElevation(20);

        mTabView2 = new PlayerOptionTabView(mContext);
        mTabView2.setColor(ContextCompat.getColor(mContext, COLOR_TAB_2_RES_ID));
        mTabView2.setTextColor(ContextCompat.getColor(mContext, COLOR_TAB_TEXT_2_RES_ID));
        mTabView2.setTextStr(getResources().getString(STR_TAB_2_RES_ID));
        mTabView2.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(DEFAULT_TAB_TEXT_SIZE_RES_ID));
        mTabContent2 = (LinearLayout)((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(DEFAULT_PLAYER_OPTION_VIEW_RES_ID, null);
        mTabContent2.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mTabContent2.setBackgroundColor(ContextCompat.getColor(mContext, COLOR_TAB_2_RES_ID));
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            mTabView2.setElevation(10);

        addTabAndTabContent(mTabView0, mTabContent0);
        addTabAndTabContent(mTabView1, mTabContent1);
        addTabAndTabContent(mTabView2, mTabContent2);

        mTabCount = mTabContentList.size();

        mPagerAdapter = new LinkedListPagerAdapter(mTabContentList);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setPagingEnabled(true);

        registerOptionsListener();
    }

    public void setOptionsInTabContent(ArrayList<OptionSettings> optionSettingsLL){

        if(optionSettingsLL == null) {
            mOptionSettingsLL = new ArrayList<>();
            for(int i = 1; i <= 3; i++)
                mOptionSettingsLL.add(new OptionSettings(i, true, 1, true, i, 0, 1, 2));
        }
        else
            mOptionSettingsLL = optionSettingsLL;

        Iterator<OptionSettings> ii = mOptionSettingsLL.iterator();
        while(ii.hasNext()){
            OptionSettings optionSettings = ii.next();
            for(int i = 0; i < mOptionResIDs.length; i++){
                int optionID = mOptionResIDs[i];
                int mode = optionSettings.getMode() - 1;
                switch (optionID){
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
        if(v instanceof ImageView)
            ((ImageView) v).setImageLevel(option);
        else if (v instanceof NumeralPicker) {
            NumeralPicker picker = (NumeralPicker) v;
            switch (optionID) {
                case PLAYER_OPTION_SECOND_PICKER_RES_ID:
                    picker.calculatePickerRange();
                    picker.setPickerTextStr(String.valueOf(option));
                    break;
                case PLAYER_OPTION_FREQUENCY_PICKER_RES_ID:
                    picker.calculatePickerRange();
                    picker.setPickerTextStr(String.valueOf(option));
                    break;
                case PLAYER_OPTION_SPEED_PICKER_RES_ID:
                    picker.calculatePickerRange();
                    picker.setPickerTextStr(String.valueOf(option));
                    break;
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

    public void addTabAndTabContent(View tab, ViewGroup tabContent){

        LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        tab.setLayoutParams(layoutParams);
        mTabLayout.addView(tab);

        mTabContentList.add(tabContent);

        // Let first added tab as currentTab
        if(mTabLayout.indexOfChild(tab) == 0) {
            mCurrentTab = tab;
            mCurrentTabIndex = 0;
        }

        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentTab(v);
            }
        });

    }

    public void setCurrentTab(View v){
        int nextTabIndex = mTabLayout.indexOfChild(v);
        switchToTabContent(nextTabIndex);
        mCurrentTab = v;
        mCurrentTabIndex = nextTabIndex;
        if(mListener != null)
            mListener.onOptionChanged(v, mOptionSettingsLL, mCurrentTabIndex);
    }

    public boolean isListenerNull() {
        return mListener == null;
    }

    public View getCurrentTab(){ return mCurrentTab; }

    public int getCurrentTabIndex(){ return mCurrentTabIndex; }

    public void setOnOptionChangedListener(OnOptionChangedListener listener){
        mListener = listener;
    }

    private void switchToTabContent(int index){mViewPager.setCurrentItem(index, true);}

    public ViewGroup getTabContent(int index){
        return mTabContentList.get(index);
    }

    private void initAllLayout(){
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        setOrientation(VERTICAL);

        mTabLayout = new LinearLayout(mContext);
        mTabLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        mTabLayout.setOrientation(HORIZONTAL);

        mTabContentList = new LinkedList<>();

        mViewPager = new WrapContentViewPager(mContext);
        mViewPager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        addView(mTabLayout);
        addView(mViewPager);

    }

    private void registerOptionsListener(){
        for(int i = 0; i < mTabCount; i++){
            ViewGroup viewGroup = getTabContent(i);
            for(int j = 0; j < mOptionResIDs.length; j++) {
                final View v = viewGroup.findViewById(mOptionResIDs[j]);
                if(v instanceof ImageView) {

                    v.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            OptionSettings optionSettings = mOptionSettingsLL.get(mCurrentTabIndex);
                            switch (v.getId()){
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
                            if(mListener != null)
                                mListener.onOptionChanged(v, mOptionSettingsLL, mCurrentTabIndex);
                        }
                    });
                }
                else if( v instanceof NumeralPicker){
                    ((NumeralPicker) v).setOnPickerClickedListener(new NumeralPicker.OnPickerClickListener() {
                        @Override
                        public void onPickerClicked(String valueStr) {
                            OptionSettings optionSettings = mOptionSettingsLL.get(mCurrentTabIndex);
                            switch (v.getId()){
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
                            if(mListener != null)
                                mListener.onOptionChanged(v, mOptionSettingsLL, mCurrentTabIndex);
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

        private TextView mTextView;

        private String mTagString;

        private Context mContext;

        private ShapeDrawable shadowDrawable;

        private int mShadowLongHeight;

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
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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

    /**
     * WrapContentViewPager enables ViewPager can be assigned a specific size.
     * */
    private class WrapContentViewPager extends ViewPager {

        private Context mContext;
        private boolean isPagingEnabled = true;

        public WrapContentViewPager(Context context) {
            super(context);

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
}
