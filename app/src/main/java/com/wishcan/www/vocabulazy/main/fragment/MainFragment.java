package com.wishcan.www.vocabulazy.main.fragment;


import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.ga.GAFragment;
import com.wishcan.www.vocabulazy.ga.GAMainFragment;
import com.wishcan.www.vocabulazy.main.MainActivity;
import com.wishcan.www.vocabulazy.main.view.MainView;
import com.wishcan.www.vocabulazy.widget.CustomFragmentPagerAdapter;
import com.wishcan.www.vocabulazy.widget.TabView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends GAMainFragment implements TabView.OnTabChangeListener {

    public static final String TAG = MainFragment.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final int VIEW_RES_ID = R.layout.view_main;
    private static final String ARG_TAB_INDEX = "tab_index";

    private String[] mTitles;
    private GAFragment[] mFragments;
    private int[] mDrawablesRID;
    private int[] mTagsRID;
    private MainView mMainView;

    private int mCurrentTabIndex;

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDrawablesRID = new int[]{
                R.drawable.main_book,
                R.drawable.main_note,
                R.drawable.main_exam,
                R.drawable.main_info
        };
        mTagsRID = new int[]{
                R.string.main_book_title,
                R.string.main_note_title,
                R.string.main_exam_title,
                R.string.main_info_title,
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainView = (MainView) inflater.inflate(VIEW_RES_ID, container, false);
        if(savedInstanceState != null){
            mCurrentTabIndex = savedInstanceState.getInt(ARG_TAB_INDEX);
        } else {
            mCurrentTabIndex = 0;
        }
        ((MainActivity)getActivity()).switchActionBarStr(MainActivity.FRAGMENT_FLOW.GO, getResources().getString(MainView.TAGIDs[mCurrentTabIndex]));

        CustomFragmentPagerAdapter pagerAdapter = new CustomFragmentPagerAdapter(getContext(), getActivity().getSupportFragmentManager(), mFragments, mDrawablesRID, mTagsRID);

        ViewPager viewPager = new ViewPager(getContext());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setId(R.id.tab_view_pager_id);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));

        mMainView.setUpWithViewPager(viewPager);
        mMainView.setTabStripeColor(ContextCompat.getColor(getContext(), R.color.main_tab_stripe));
        mMainView.addOnTabChangeListener(this);
        return mMainView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(ARG_TAB_INDEX, mCurrentTabIndex);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onTabSelected(int position) {
        ((MainActivity)getActivity()).switchActionBarStr(MainActivity.FRAGMENT_FLOW.SAME, getResources().getString(MainView.TAGIDs[position]));
        mCurrentTabIndex = position;
    }

    public void setTitles(String[] titles) {
        mTitles = titles;
    }

    public void setFragments(GAFragment[] fragments) {
        mFragments = fragments;
    }


}
