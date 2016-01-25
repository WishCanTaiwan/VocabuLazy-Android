package com.wishcan.www.vocabulazy.view.main;

import android.content.Context;
import android.graphics.Color;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import java.util.LinkedList;
import java.util.List;

import com.wishcan.www.vocabulazy.MainActivity;
import com.wishcan.www.vocabulazy.view.books.BooksGridView;
import com.wishcan.www.vocabulazy.view.notes.NotesListView;
import com.wishcan.www.vocabulazy.R;

/**
 * Created by swallow on 2015/6/19.
 */
public class TabLayout extends TabHost {

    private Context mContext;

    private static final int BOOK_ICON_RES_ID = R.drawable.main_book;

    private static final int NOTE_ICON_RES_ID = R.drawable.main_note;

    private static final int TEST_ICON_RES_ID = R.drawable.main_exam;

    private static final int INFO_ICON_RES_ID = R.drawable.main_info;

    private static final int TAB_WIDGET_COLOR = R.color.main_tab_layout_widget_color;

    TabWidget tabWidget;
    FrameLayout layout;

    RelativeLayout mRelativeLayout;

    CustomizeTabContent tab0Content;
    CustomizeTabContent tab1Content;
    CustomizeTabContent tab2Content;
    CustomizeTabContent tab3Content;

    List<CustomizeTabContent> mTabContents = new LinkedList<>();

    TabSpec tab0;
    TabSpec tab1;
    TabSpec tab2;
    TabSpec tab3;

    int mCurrentTab = -1;

    public TabLayout(Context context) {
        super(context, null);
    }

    public TabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        mRelativeLayout = new RelativeLayout(context);
        mRelativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));


        //mRelativeLayout.setOrientation(LinearLayout.VERTICAL);
        createTabWidget();      // put TabWidget into container, tab will be added here actually
        createFrameLayout();    // what content information when certain tab is pressed
        createTabs();           // create the customized tab
        addView(mRelativeLayout);
        setup();                // always call setup
        addTab(tab0);
        addTab(tab1);
        addTab(tab2);
        addTab(tab3);
    }

    @Override
    public void setCurrentTab(int index) {

        if (index < 0 || index >= getTabWidget().getTabCount()) {
            return;
        }

        if (index == mCurrentTab) {
            return;
        }

        mCurrentTab = index;
        //final TabHost.TabSpec spec = mTabSpecs.get(index);

        // Call the tab widget's focusCurrentTab(), instead of just
        // selecting the tab.
        getTabWidget().focusCurrentTab(mCurrentTab);

        // tab content
        View mCurrentView = mTabContents.get(mCurrentTab).getTabContent();

        if (mCurrentView.getParent() == null) {

            Scene mScene;
            Transition mFadeTransition = new Fade();
            FrameLayout mSceneRoot =  getTabContentView();
            View mViewHierarchy = mCurrentView;
            mScene = new Scene(mSceneRoot, mViewHierarchy);
            TransitionManager.go(mScene, mFadeTransition);
            ((MainActivity)mContext).getActionBar().setDisplayHomeAsUpEnabled(false);

        }

        if (!getTabWidget().hasFocus()) {
            // if the tab widget didn't take focus (likely because we're in touch mode)
            // give the current tab content view a shot
            mCurrentView.requestFocus();
        }

        //mTabContent.requestFocus(View.FOCUS_FORWARD);

    }

    public View getTabContent(int index){
        return mTabContents.get(index).getTabContent();
    }

    // change Indicator from string to drawable
    private void createTabs(){

        // create a new tab for booklist
        // Specify the content by implement TabHost.TabContentFactory with CustomizeTabContent
        // Put the specified content
        String tab0_str = getResources().getString(R.string.tab0_str);
        tab0 = newTabSpec(tab0_str);
        tab0Content = new TabLayout.CustomizeTabContent(tab0_str);
        tab0.setIndicator("", mContext.getDrawable(BOOK_ICON_RES_ID));
        tab0.setContent(tab0Content);
        mTabContents.add(tab0Content);

        String tab1_str = getResources().getString(R.string.tab1_str);
        tab1 = newTabSpec(tab1_str);
        tab1Content = new TabLayout.CustomizeTabContent(tab1_str);
        tab1.setIndicator("", mContext.getDrawable(NOTE_ICON_RES_ID));
        tab1.setContent(tab1Content);
        mTabContents.add(tab1Content);

        String tab2_str = getResources().getString(R.string.tab2_str);
        tab2 = newTabSpec(tab2_str);
        tab2Content = new TabLayout.CustomizeTabContent(tab2_str);
        tab2.setIndicator("", mContext.getDrawable(TEST_ICON_RES_ID));
        tab2.setContent(tab2Content);
        mTabContents.add(tab2Content);

        String tab3_str = getResources().getString(R.string.tab3_str);
        tab3 = newTabSpec(tab3_str);
        tab3Content = new TabLayout.CustomizeTabContent(tab3_str);
        tab3.setIndicator("", mContext.getDrawable(INFO_ICON_RES_ID));
        tab3.setContent(tab3Content);
        mTabContents.add(tab3Content);

    }

    private void createTabWidget(){
        // need a tabWidget for putting tab
        tabWidget = new TabWidget(getContext());
        tabWidget.setBackgroundColor(getResources().getColor(TAB_WIDGET_COLOR));
        tabWidget.setId(android.R.id.tabs);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mRelativeLayout.addView(tabWidget, layoutParams);


    }

    private void createFrameLayout(){
        // need a FrameLayout for putting this thing
        layout = new FrameLayout(getContext());
        layout.setBackgroundColor(Color.WHITE);

        layout.setId(android.R.id.tabcontent);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.ABOVE, android.R.id.tabs);
        mRelativeLayout.addView(layout, layoutParams);

    }


    class CustomizeTabContent implements TabLayout.TabContentFactory{

        private View mTabContent;
        private String mTag;

        CustomizeTabContent(String tag){
            mTag = tag;
            mTabContent = createTabContent(tag);
        }

        @Override
        public View createTabContent(String tag) {

            if (tag.equals(getResources().getString(R.string.tab1_str)))
                return new NotesListView(getContext());

            else if (tag.equals(getResources().getString(R.string.tab2_str)))
                return new BooksGridView(getContext());

            else if (tag.equals(getResources().getString(R.string.tab3_str))) {
                return inflate(getContext(), R.layout.test_layout, null);
            } else       //HOME
                return new BooksGridView(getContext());


        }

        public View getTabContent(){
            return mTabContent;
        }

        public String getTabTag(){
           return mTag;
       }


    }

}
