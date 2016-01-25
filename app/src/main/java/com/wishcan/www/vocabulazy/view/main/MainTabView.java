package com.wishcan.www.vocabulazy.view.main;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.wishcan.www.vocabulazy.MainActivity;
import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.view.books.BooksGridView;
import com.wishcan.www.vocabulazy.view.notes.NotesListAddButtonView;
import com.wishcan.www.vocabulazy.view.reading.ReadingBooksGridView;
import com.wishcan.www.vocabulazy.view.tab.TabView;

/**
 * Created by swallow on 2015/9/19.
 */
public class MainTabView extends TabView {

    private static final int DEFAULT_TAB_STRIPE_COLOR_RES_ID = R.color.main_tab_view_tab_stripe_color;

    private static final int DEFAULT_TAB_ITEM_0_DRAWABLE_RES_ID = R.drawable.main_book;

    private static final int DEFAULT_TAB_ITEM_1_DRAWABLE_RES_ID = R.drawable.main_note;

    private static final int DEFAULT_TAB_ITEM_2_DRAWABLE_RES_ID = R.drawable.main_exam;

    private static final int DEFAULT_TAB_ITEM_3_DRAWABLE_RES_ID = R.drawable.main_info;

    private static final int DEFAULT_TAB_ITEM_4_DRAWABLE_RES_ID = R.drawable.main_info;

    private static final int DEFAULT_TAB_ITEM_0_TAG_STR_RES_ID = R.string.main_book_title;

    private static final int DEFAULT_TAB_ITEM_1_TAG_STR_RES_ID = R.string.main_note_title;

    private static final int DEFAULT_TAB_ITEM_2_TAG_STR_RES_ID = R.string.main_exam_title;

    private static final int DEFAULT_TAB_ITEM_3_TAG_STR_RES_ID = R.string.reading_book_title;

    private static final int DEFAULT_TAB_ITEM_4_TAG_STR_RES_ID = R.string.main_info_title;

    private static final int DEFAULT_TAB_ITEM_2__LAYOUT_RES_ID = R.layout.exam_home_view;

    private static final int[] TAGIDs = {DEFAULT_TAB_ITEM_0_TAG_STR_RES_ID,
            DEFAULT_TAB_ITEM_1_TAG_STR_RES_ID,
            DEFAULT_TAB_ITEM_2_TAG_STR_RES_ID,
            DEFAULT_TAB_ITEM_3_TAG_STR_RES_ID,
            DEFAULT_TAB_ITEM_4_TAG_STR_RES_ID};
    /**
     * mTabItems are used for being added to the mTabStripe, which can contain a ImageView
     * or being added a TextView
     */
    private TabItem mTabItem0;
    private TabItem mTabItem1;
    private TabItem mTabItem2;
    private TabItem mTabItem3;
    private TabItem mTabItem4;

    /**
     * The View Above will be added to a LinkedList in
     */
    private BooksGridView mBooksGridView;
//    private NotesListView mNotesListView;
    private NotesListAddButtonView mNotesListAddButtonView;
    private ViewGroup mExamView;
    private ReadingBooksGridView mReadingBookGridView;
    private RelativeLayout mEmptyLayout4;


    public MainTabView(Context context) {
        this(context, null);
    }

    public MainTabView(final Context context, AttributeSet attrs) {
        super(context, attrs);

        String[] tags = {context.getString(TAGIDs[0]),
                context.getString(TAGIDs[1]),
                context.getString(TAGIDs[2]),
                context.getString(TAGIDs[3]),
                context.getString(TAGIDs[4])};

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
        mTabItem4 = new TabItem(context);
        mTabItem4.setDrawableIcon(ContextCompat.getDrawable(context, DEFAULT_TAB_ITEM_4_DRAWABLE_RES_ID));
        mTabItem4.setTabItemTag(tags[4]);

        mBooksGridView = new BooksGridView(context);
        mNotesListAddButtonView = new NotesListAddButtonView(context);
        mExamView = (ViewGroup)((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(DEFAULT_TAB_ITEM_2__LAYOUT_RES_ID, null);
        mReadingBookGridView = new ReadingBooksGridView(context);
        mEmptyLayout4 = (RelativeLayout) ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.test_layout, null);

        addTabAndTabContent(mTabItem0, mBooksGridView);
        addTabAndTabContent(mTabItem1, mNotesListAddButtonView);
        addTabAndTabContent(mTabItem2, mExamView);
        addTabAndTabContent(mTabItem3, mReadingBookGridView);
        addTabAndTabContent(mTabItem4, mEmptyLayout4);

        setTabStripeColor(getResources().getColor(DEFAULT_TAB_STRIPE_COLOR_RES_ID));

    }

    @Override
    public void initWhenTabIsSelected(int position) {
        ((MainActivity) getContext()).switchActionBarTitle(getContext().getString(TAGIDs[position]));
    }
}
