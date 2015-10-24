package com.wishcan.www.vocabulazy.view.main;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.view.books.BooksGridView;
import com.wishcan.www.vocabulazy.view.notes.NotesListView;
import com.wishcan.www.vocabulazy.view.tab.TabView;
import com.wishcan.www.vocabulazy.view.infinitethreeview.InfiniteThreeView;

/**
 * Created by swallow on 2015/9/19.
 */
public class MainTabView extends TabView {

    private static final int DEFAULT_TAB_STRIPE_COLOR_RES_ID = R.color.main_tab_view_tab_stripe_color;

    private static final int DEFAULT_TAB_ITEM_0_DRAWABLE_RES_ID = R.drawable.bar_book_off;

    private static final int DEFAULT_TAB_ITEM_1_DRAWABLE_RES_ID = R.drawable.bar_list_off;

    private static final int DEFAULT_TAB_ITEM_2_DRAWABLE_RES_ID = R.drawable.bar_test_off;

    private static final int DEFAULT_TAB_ITEM_3_DRAWABLE_RES_ID = R.drawable.bar_i_off;

    private static final int DEFAULT_TAB_ITEM_0_TAG_STR_RES_ID = R.string.book_title;

    private static final int DEFAULT_TAB_ITEM_1_TAG_STR_RES_ID = R.string.list_title;

    private static final int DEFAULT_TAB_ITEM_2_TAG_STR_RES_ID = R.string.test_title;

    private static final int DEFAULT_TAB_ITEM_3_TAG_STR_RES_ID = R.string.info_title;

    /**
     * mTabItems are used for being added to the mTabStripe, which can contain a ImageView
     * or being added a TextView
     */
    private TabItem mTabItem0;
    private TabItem mTabItem1;
    private TabItem mTabItem2;
    private TabItem mTabItem3;

    /**
     * The View Above will be added to a LinkedList in
     */
    private BooksGridView mBooksGridView;
    private NotesListView mNotesListView;
    private InfiniteThreeView mEmptyLayout2;
    private LinearLayout mEmptyLayout3;


    public MainTabView(Context context) {
        this(context, null);
    }

    public MainTabView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mTabItem0 = new TabItem(context);
        mTabItem0.setDrawableIcon(ContextCompat.getDrawable(context, DEFAULT_TAB_ITEM_0_DRAWABLE_RES_ID));
        mTabItem0.setTabItemTag(context.getString(DEFAULT_TAB_ITEM_0_TAG_STR_RES_ID));
        mTabItem1 = new TabItem(context);
        mTabItem1.setDrawableIcon(ContextCompat.getDrawable(context, DEFAULT_TAB_ITEM_1_DRAWABLE_RES_ID));
        mTabItem1.setTabItemTag(context.getString(DEFAULT_TAB_ITEM_1_TAG_STR_RES_ID));
        mTabItem2 = new TabItem(context);
        mTabItem2.setDrawableIcon(ContextCompat.getDrawable(context, DEFAULT_TAB_ITEM_2_DRAWABLE_RES_ID));
        mTabItem2.setTabItemTag(context.getString(DEFAULT_TAB_ITEM_2_TAG_STR_RES_ID));
        mTabItem3 = new TabItem(context);
        mTabItem3.setDrawableIcon(ContextCompat.getDrawable(context, DEFAULT_TAB_ITEM_3_DRAWABLE_RES_ID));
        mTabItem3.setTabItemTag(context.getString(DEFAULT_TAB_ITEM_3_TAG_STR_RES_ID));

        mBooksGridView = new BooksGridView(context);
        mNotesListView = new NotesListView(context);
        mEmptyLayout2 = new InfiniteThreeView(context);
        mEmptyLayout3 = (LinearLayout) ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.test_layout, null);

        addTabAndTabContent(mTabItem0, mBooksGridView);
        addTabAndTabContent(mTabItem1, mNotesListView);
        addTabAndTabContent(mTabItem2, mEmptyLayout2);
        addTabAndTabContent(mTabItem3, mEmptyLayout3);

        setTabStripeColor(getResources().getColor(DEFAULT_TAB_STRIPE_COLOR_RES_ID));

    }


}
