package com.wishcan.www.vocabulazy.cover;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.VLApplication;
import com.wishcan.www.vocabulazy.cover.fragment.CoverFragment;
import com.wishcan.www.vocabulazy.log.Logger;

//import io.uxtesting.UXTesting;

public class CoverActivity extends FragmentActivity {

    public static final int VIEW_MAIN_RES_ID = R.id.activity_cover_container;

    private static final String TAG = CoverActivity.class.getSimpleName();
    private static final int VIEW_ACTIVITY_RES_ID = R.layout.activity_cover;

    private Logger wLogger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * Retrieve the Google Analytics tracker from VLApplication
         */
        VLApplication application = (VLApplication) getApplication();
        wLogger = application.getLogHelper();
        application.loadDatabase();

        setContentView(VIEW_ACTIVITY_RES_ID);
        FragmentManager mFragmentManager = getSupportFragmentManager();
        CoverFragment mCoverFragment = new CoverFragment();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(VIEW_MAIN_RES_ID, mCoverFragment, "CoverFragment");
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.sendScreenViewEvent(TAG);
    }
}
