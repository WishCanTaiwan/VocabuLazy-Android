package com.wishcan.www.vocabulazy.main.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.ga.GAFragment;
import com.wishcan.www.vocabulazy.main.exam.fragment.ExamIndexFragment;
import com.wishcan.www.vocabulazy.main.info.fragment.InfoFragment;
import com.wishcan.www.vocabulazy.main.usr.fragment.UsrNoteFragment;
import com.wishcan.www.vocabulazy.main.voc.fragment.VocBookFragment;
import com.wishcan.www.vocabulazy.widget.TabView;

/**
 * Created by swallow on 2015/9/19.
 */
public class MainView extends TabView {

    public interface OnTabChangeListener {
        void onTabChange(int position);
    }

    private static final String TAG = "MainView";

    private static final int DEFAULT_TAB_STRIPE_COLOR_RES_ID = R.color.main_tab_stripe;
    private static final int DEFAULT_TAB_ITEM_0_DRAWABLE_RES_ID = R.drawable.main_book;
    private static final int DEFAULT_TAB_ITEM_1_DRAWABLE_RES_ID = R.drawable.main_note;
    private static final int DEFAULT_TAB_ITEM_2_DRAWABLE_RES_ID = R.drawable.main_exam;
    private static final int DEFAULT_TAB_ITEM_3_DRAWABLE_RES_ID = R.drawable.main_info;
    private static final int DEFAULT_TAB_ITEM_0_TAG_STR_RES_ID = R.string.main_book_title;
    private static final int DEFAULT_TAB_ITEM_1_TAG_STR_RES_ID = R.string.main_note_title;
    private static final int DEFAULT_TAB_ITEM_2_TAG_STR_RES_ID = R.string.main_exam_title;
    private static final int DEFAULT_TAB_ITEM_3_TAG_STR_RES_ID = R.string.main_info_title;

    public static final int[] TAGIDs = {
            DEFAULT_TAB_ITEM_0_TAG_STR_RES_ID,
            DEFAULT_TAB_ITEM_1_TAG_STR_RES_ID,
            DEFAULT_TAB_ITEM_2_TAG_STR_RES_ID,
            DEFAULT_TAB_ITEM_3_TAG_STR_RES_ID
    };

    private TabItem[] mTabItems;
    private TabStripe mTabStripe;
    private OnTabChangeListener mListener;

    public MainView(Context context) {
        this(context, null);
    }

    public MainView(final Context context, AttributeSet attrs) {
        super(context, attrs);

//        String[] tags = {
//                context.getString(TAGIDs[0]),
//                context.getString(TAGIDs[1]),
//                context.getString(TAGIDs[2]),
//                context.getString(TAGIDs[3])
//        };

        /**
         * mTabItems are used for being added to the mTabStripe, which can contain a ImageView
         * or being added a TextView
         */
//        TabItem mTabItem0;
//        TabItem mTabItem1;
//        TabItem mTabItem2;
//        TabItem mTabItem3;
//        mTabItems = new TabItem[4];

//        mTabItems[0] = new TabItem(context);
//        mTabItems[0].setDrawableIcon(ContextCompat.getDrawable(context, DEFAULT_TAB_ITEM_0_DRAWABLE_RES_ID));
//        mTabItems[0].setTabItemTag(tags[0]);
//        mTabItems[1] = new TabItem(context);
//        mTabItems[1].setDrawableIcon(ContextCompat.getDrawable(context, DEFAULT_TAB_ITEM_1_DRAWABLE_RES_ID));
//        mTabItems[1].setTabItemTag(tags[1]);
//        mTabItems[2] = new TabItem(context);
//        mTabItems[2].setDrawableIcon(ContextCompat.getDrawable(context, DEFAULT_TAB_ITEM_2_DRAWABLE_RES_ID));
//        mTabItems[2].setTabItemTag(tags[2]);
//        mTabItems[3] = new TabItem(context);
//        mTabItems[3].setDrawableIcon(ContextCompat.getDrawable(context, DEFAULT_TAB_ITEM_3_DRAWABLE_RES_ID));
//        mTabItems[3].setTabItemTag(tags[3]);

//        addTabAndTabContent(mTabItem0, VocBookFragment.class);
//        addTabAndTabContent(mTabItem1, UsrNoteFragment.class);
//        addTabAndTabContent(mTabItem2, ExamIndexFragment.class);
//        addTabAndTabContent(mTabItem3, InfoFragment.class);

//        setTabStripeColor(ContextCompat.getColor(context, DEFAULT_TAB_STRIPE_COLOR_RES_ID));

    }

//    public void addOnTabChangeListener() {
//
//    }

//    @Override
//    public void initWhenTabIsSelected(int position) {
//        Log.d(TAG, "initWhenTabIsSelected " + position);
//        if (mListener != null)
//            mListener.onTabChange(position);
//    }
}
