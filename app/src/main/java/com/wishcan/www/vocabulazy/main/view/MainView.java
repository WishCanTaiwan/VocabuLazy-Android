package com.wishcan.www.vocabulazy.main.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.main.exam.fragment.ExamIndexFragment;
import com.wishcan.www.vocabulazy.main.info.fragment.InfoFragment;
import com.wishcan.www.vocabulazy.main.usr.fragment.UsrNoteFragment;
import com.wishcan.www.vocabulazy.main.voc.fragment.VocBookFragment;
import com.wishcan.www.vocabulazy.widget.TabView;

/**
 * Created by swallow on 2015/9/19.
 */
public class MainView extends TabView {

    private static final int DEFAULT_TAB_STRIPE_COLOR_RES_ID = R.color.main_tab_stripe;
    private static final int DEFAULT_TAB_ITEM_0_DRAWABLE_RES_ID = R.drawable.main_book;
    private static final int DEFAULT_TAB_ITEM_1_DRAWABLE_RES_ID = R.drawable.main_note;
    private static final int DEFAULT_TAB_ITEM_2_DRAWABLE_RES_ID = R.drawable.main_exam;
    private static final int DEFAULT_TAB_ITEM_3_DRAWABLE_RES_ID = R.drawable.main_info;
    private static final int DEFAULT_TAB_ITEM_0_TAG_STR_RES_ID = R.string.main_book_title;
    private static final int DEFAULT_TAB_ITEM_1_TAG_STR_RES_ID = R.string.main_note_title;
    private static final int DEFAULT_TAB_ITEM_2_TAG_STR_RES_ID = R.string.main_exam_title;
    private static final int DEFAULT_TAB_ITEM_3_TAG_STR_RES_ID = R.string.main_info_title;

    private static final int[] TAGIDs = {DEFAULT_TAB_ITEM_0_TAG_STR_RES_ID,
            DEFAULT_TAB_ITEM_1_TAG_STR_RES_ID,
            DEFAULT_TAB_ITEM_2_TAG_STR_RES_ID,
            DEFAULT_TAB_ITEM_3_TAG_STR_RES_ID,};

    public MainView(Context context) {
        this(context, null);
    }

    public MainView(final Context context, AttributeSet attrs) {
        super(context, attrs);

        String[] tags = {context.getString(TAGIDs[0]),
                context.getString(TAGIDs[1]),
                context.getString(TAGIDs[2]),
                context.getString(TAGIDs[3])};

        /**
         * mTabItems are used for being added to the mTabStripe, which can contain a ImageView
         * or being added a TextView
         */
        TabItem mTabItem0;
        TabItem mTabItem1;
        TabItem mTabItem2;
        TabItem mTabItem3;

        mTabItem0 = new TabItem(context);
        mTabItem0.setDrawableIcon(ContextCompat.getDrawable(context, DEFAULT_TAB_ITEM_0_DRAWABLE_RES_ID));
        mTabItem0.setTabItemTag(tags[0]);
        mTabItem1 = new TabItem(context);
        mTabItem1.setDrawableIcon(ContextCompat.getDrawable(context, DEFAULT_TAB_ITEM_1_DRAWABLE_RES_ID));
        mTabItem1.setTabItemTag(tags[1]);
        mTabItem2 = new TabItem(context);
        mTabItem2.setDrawableIcon(ContextCompat.getDrawable(context, DEFAULT_TAB_ITEM_2_DRAWABLE_RES_ID));
        mTabItem2.setTabItemTag(tags[2]);
        mTabItem3 = new TabItem(context);
        mTabItem3.setDrawableIcon(ContextCompat.getDrawable(context, DEFAULT_TAB_ITEM_3_DRAWABLE_RES_ID));
        mTabItem3.setTabItemTag(tags[3]);

        addTabAndTabContent(mTabItem0, VocBookFragment.class);
        addTabAndTabContent(mTabItem1, UsrNoteFragment.class);
        addTabAndTabContent(mTabItem2, ExamIndexFragment.class);
        addTabAndTabContent(mTabItem3, InfoFragment.class);

        setTabStripeColor(ContextCompat.getColor(context, DEFAULT_TAB_STRIPE_COLOR_RES_ID));

    }

    @Override
    public void initWhenTabIsSelected(int position) {
//        ((MainActivity) getContext()).switchActionBarTitle(getContext().getString(TAGIDs[position]));
    }
}
