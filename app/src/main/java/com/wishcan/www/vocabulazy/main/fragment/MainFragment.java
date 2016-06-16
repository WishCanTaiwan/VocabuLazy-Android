package com.wishcan.www.vocabulazy.main.fragment;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.ga.GAMainFragment;
import com.wishcan.www.vocabulazy.main.MainActivity;
import com.wishcan.www.vocabulazy.main.view.MainView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends GAMainFragment {

    public static final String TAG = MainFragment.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final int VIEW_RES_ID = R.layout.view_main;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_TAB_INDEX = "tab_index";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MainView mMainView;

    private int mCurrentTabIndex;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainView = (MainView) inflater.inflate(VIEW_RES_ID, container, false);
        if(savedInstanceState != null){
            mCurrentTabIndex = savedInstanceState.getInt(ARG_TAB_INDEX);
        } else {
            mCurrentTabIndex = 0;
        }
        ((MainActivity)getActivity()).switchActionBarStr(MainActivity.FRAGMENT_FLOW.GO, getResources().getString(MainView.TAGIDs[mCurrentTabIndex]));
        mMainView.post(new Runnable() {
            @Override
            public void run() {
                mMainView.setCurrentTab(mCurrentTabIndex);
            }
        });
        mMainView.setOnTabChangeListener(new MainView.OnTabChangeListener() {
            @Override
            public void onTabChange(int position) {
                ((MainActivity)getActivity()).switchActionBarStr(MainActivity.FRAGMENT_FLOW.SAME, getResources().getString(MainView.TAGIDs[position]));
                mCurrentTabIndex = position;
            }
        });
        return mMainView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        outState.putInt(ARG_TAB_INDEX, mCurrentTabIndex);
        super.onSaveInstanceState(outState);
    }
}
