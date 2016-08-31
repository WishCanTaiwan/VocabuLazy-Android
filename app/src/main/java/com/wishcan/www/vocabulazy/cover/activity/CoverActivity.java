package com.wishcan.www.vocabulazy.cover.activity;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.application.VLApplication;
import com.wishcan.www.vocabulazy.cover.fragment.CoverFragment;

public class CoverActivity extends FragmentActivity {

    public static final String TAG = "CoverActivity";

    public static final int VIEW_MAIN_RES_ID = R.id.activity_cover_container;
    private static final int VIEW_ACTIVITY_RES_ID = R.layout.activity_cover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Create");
        super.onCreate(savedInstanceState);
        /**
         * Retrieve the Google Analytics tracker from VLApplication
         */
        VLApplication application = (VLApplication) getApplication();
        application.loadDatabase();

        setContentView(VIEW_ACTIVITY_RES_ID);
        FragmentManager mFragmentManager = getSupportFragmentManager();
        CoverFragment mCoverFragment = new CoverFragment();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(VIEW_MAIN_RES_ID, mCoverFragment, "CoverFragment");
        fragmentTransaction.commit();
    }
}
