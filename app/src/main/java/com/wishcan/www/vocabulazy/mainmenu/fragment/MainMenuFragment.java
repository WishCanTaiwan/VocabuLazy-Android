package com.wishcan.www.vocabulazy.mainmenu.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.ga.GABaseFragment;
import com.wishcan.www.vocabulazy.ga.manager.GAManager;
import com.wishcan.www.vocabulazy.ga.tags.GAScreenName;
import com.wishcan.www.vocabulazy.mainmenu.activity.MainMenuActivity;
import com.wishcan.www.vocabulazy.mainmenu.adapter.MainMenuFragmentPagerAdapter;
import com.wishcan.www.vocabulazy.mainmenu.exam.fragment.ExamIndexFragment;
import com.wishcan.www.vocabulazy.mainmenu.info.InfoFragment;
import com.wishcan.www.vocabulazy.mainmenu.model.MainMenuModel;
import com.wishcan.www.vocabulazy.mainmenu.note.fragment.NoteFragment;
import com.wishcan.www.vocabulazy.mainmenu.textbook.fragment.TextbookFragment;
import com.wishcan.www.vocabulazy.mainmenu.view.MainMenuViewPager;

public class MainMenuFragment extends GABaseFragment implements TextbookFragment.OnTextbookClickListener, NoteFragment.OnNoteClickListener, ExamIndexFragment.OnExamIndexClickListener, ViewPager.OnPageChangeListener {

    // callback interface
    public interface OnMainMenuEventListener {
        void onTextbookSelected(int bookIndex, int lessonIndex);
        void onNoteSelected(int noteIndex);
        void onNoteRename(int noteIndex, String originalName);
        void onNoteDelete(int noteIndex, String noteTitle);
        void onNoteCreate();
        void onExamTextbookSelected(int examBookIndex, int examLessonIndex);
        void onExamNoteSelected(int examNoteIndex);
    }

    // TAG for debugging
    public static final String TAG = "MainMenuFragment";

    // data model
    private MainMenuModel mMainMenuModel;

    // listener
    private OnMainMenuEventListener mOnMainMenuEventListener;

    // fragments
    private TextbookFragment mTextbookFragment;
    private NoteFragment mNoteFragment;
    private ExamIndexFragment mExamIndexFragment;
    private InfoFragment mInfoFragment;

    // screen name of the fragment
    private String[] mScreenName = {
            GAScreenName.TEXTBOOK,
            GAScreenName.NOTE,
            GAScreenName.EXAM_INDEX,
            GAScreenName.INFO
    };

    // record the position/index of selected tab
    private int selectedTab = -1;

    // required empty constructor
    public MainMenuFragment() {

    }

    /** Life cycles **/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize the fragments
        initFragments();

        // initialize the selected tab
        selectedTab = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_menu, container, false);

        Fragment[] fragments = new Fragment[]{mTextbookFragment, mNoteFragment, mExamIndexFragment, mInfoFragment};
        String[] titles = getResources().getStringArray(R.array.main_menu_tab_title);
        MainMenuFragmentPagerAdapter pagerAdapter = new MainMenuFragmentPagerAdapter(getActivity().getSupportFragmentManager(), fragments, titles);

        MainMenuViewPager viewPager = (MainMenuViewPager) rootView.findViewById(R.id.main_menu_viewpager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(fragments.length);
        viewPager.addOnPageChangeListener(this);

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.main_menu_tab_container);
        tabLayout.setupWithViewPager(viewPager);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // get the model from activity
        mMainMenuModel = ((MainMenuActivity) getActivity()).getModel();
    }

    @Override
    public void onResume() {
        super.onResume();

        // update fragment contents
        updateFragmentsContent();

        // send GA screen events
        GAManager.getInstance().sendScreenEvent(mScreenName[selectedTab]);
    }

    /** Abstracts and Interfaces **/

    @Override
    protected String getGALabel() {
        return null;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        // update the selected tab
        selectedTab = position;

        // set screen name
        GAManager.getInstance().sendScreenEvent(mScreenName[selectedTab]);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTextbookClicked(int bookIndex, int lessonIndex) {
        if (mOnMainMenuEventListener != null) {
            mOnMainMenuEventListener.onTextbookSelected(bookIndex, lessonIndex);
        }
    }

    @Override
    public void onNotePlay(int noteIndex) {
        if (mOnMainMenuEventListener != null) {
            mOnMainMenuEventListener.onNoteSelected(noteIndex);
        }
    }

    @Override
    public void onNoteRename(int noteIndex, String name) {
        if (mOnMainMenuEventListener != null) {
            mOnMainMenuEventListener.onNoteRename(noteIndex, name);
        }
    }

    @Override
    public void onNoteCopy() {
    }

    @Override
    public void onNoteDelete(int noteIndex, String name) {
        if (mOnMainMenuEventListener != null) {
            mOnMainMenuEventListener.onNoteDelete(noteIndex, name);
        }
    }

    @Override
    public void onNoteCreate() {
        if (mOnMainMenuEventListener != null) {
            mOnMainMenuEventListener.onNoteCreate();
        }
    }

    @Override
    public void onExamTextbookClicked(int bookIndex, int lessonIndex) {
        if (mOnMainMenuEventListener != null) {
            mOnMainMenuEventListener.onExamTextbookSelected(bookIndex, lessonIndex);
        }
    }

    @Override
    public void onExamNoteClicked(int noteIndex) {
        if (mOnMainMenuEventListener != null) {
            mOnMainMenuEventListener.onExamNoteSelected(noteIndex);
        }
    }

    /** Public methods **/

    /**
     * Set {@link OnMainMenuEventListener} to receive interface callback.
     *
     * @param listener {@link OnMainMenuEventListener} instance
     */
    public void addOnMainMenuEventListener(OnMainMenuEventListener listener) {
        mOnMainMenuEventListener = listener;
    }

    /**
     * Update content the content of the fragments
     */
    public void updateFragmentsContent() {
        mMainMenuModel.generateBookItems();
        mMainMenuModel.generateNoteItems();
        mMainMenuModel.generateExamIndexItems();
        mTextbookFragment.updateBookContent(mMainMenuModel.getTextbookGroupItems(), mMainMenuModel.getTextbookChildItemsMap());
        mNoteFragment.updateNoteContent(mMainMenuModel.getNoteGroupItems(), mMainMenuModel.getNoteChildItemsMap());
        mExamIndexFragment.updateExamIndexContent(mMainMenuModel.getExamIndexTextbookGroupItems(), mMainMenuModel.getExamIndexTextbookChildItemsMap(), mMainMenuModel.getExamIndexNoteGroupItems(), mMainMenuModel.getExamIndexNoteChildItemsMap());
    }

    /**
     * Refresh fragments when needed.
     */
    public void refreshFragments() {
        mTextbookFragment.refresh();
        mNoteFragment.refresh();
        mExamIndexFragment.refresh();
    }

    /** Private methods **/

    private void initFragments() {
        if (mTextbookFragment == null) {
            mTextbookFragment = TextbookFragment.newInstance();
            mTextbookFragment.addOnTextbookClickListener(this);
        }

        if (mNoteFragment == null) {
            mNoteFragment = NoteFragment.newInstance();
            mNoteFragment.addOnNoteClickListener(this);
        }

        if (mExamIndexFragment == null) {
            mExamIndexFragment = ExamIndexFragment.newInstance();
            mExamIndexFragment.addOnExamIndexClickListener(this);
        }

        if (mInfoFragment == null) {
            mInfoFragment = InfoFragment.getInstance();
        }
    }
}
