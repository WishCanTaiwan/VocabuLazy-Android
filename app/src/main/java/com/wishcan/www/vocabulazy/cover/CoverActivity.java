package com.wishcan.www.vocabulazy.cover;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.VLApplication;
import com.wishcan.www.vocabulazy.cover.fragment.CoverFragment;

//import io.uxtesting.UXTesting;

public class CoverActivity extends FragmentActivity {

    public static final int VIEW_MAIN_RES_ID = R.id.activity_cover_container;

    private static final String TAG = CoverActivity.class.getSimpleName();
    private Tracker wTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Retrieve the Google Analytics tracker from VLApplication
         */
        VLApplication application = (VLApplication) getApplication();
        wTracker = application.getDefaultTracker();

        setContentView(R.layout.activity_cover);
        FragmentManager mFragmentManager = getSupportFragmentManager();
        CoverFragment mCoverFragment = new CoverFragment();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(VIEW_MAIN_RES_ID, mCoverFragment, "CoverFragment");
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Setting screen name: " + TAG);
        wTracker.setScreenName(TAG);
        wTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        UXTesting.onActivityResult(requestCode, resultCode, data);
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        UXTesting.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }

}
