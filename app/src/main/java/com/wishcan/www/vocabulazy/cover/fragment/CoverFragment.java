package com.wishcan.www.vocabulazy.cover.fragment;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.cover.activity.CoverActivity;
import com.wishcan.www.vocabulazy.ga.GABaseFragment;
import com.wishcan.www.vocabulazy.ga.manager.GAManager;
import com.wishcan.www.vocabulazy.ga.tags.GAScreenName;
import com.wishcan.www.vocabulazy.mainmenu.activity.MainMenuActivity;
import com.wishcan.www.vocabulazy.search.activity.SearchActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CoverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoverFragment extends GABaseFragment {

    // TAG for debugging
    public static final String TAG = "CoverFragment";

    // layout resource id
    private static final int VIEW_RES_ID = R.layout.view_cover;

    // Required empty public constructor
    public CoverFragment() {

    }

    // factory method for instantiate CoverFragment
    public static CoverFragment newInstance() {
        CoverFragment fragment = new CoverFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /** Life cycles **/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate cover fragment xml
        return inflater.inflate(VIEW_RES_ID, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        // send GA screen event
        GAManager.getInstance().sendScreenEvent(GAScreenName.SPLASH);
    }

    /** Abstracts and Interfaces **/

    @Override
    protected String getGALabel() {
        return GAScreenName.SPLASH;
    }
}
