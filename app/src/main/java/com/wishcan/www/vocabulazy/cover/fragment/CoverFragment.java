package com.wishcan.www.vocabulazy.cover.fragment;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.cover.CoverActivity;
import com.wishcan.www.vocabulazy.cover.view.CoverDialogView;
import com.wishcan.www.vocabulazy.ga.GACoverFragment;
import com.wishcan.www.vocabulazy.log.Logger;
import com.wishcan.www.vocabulazy.main.MainActivity;
import com.wishcan.www.vocabulazy.widget.DialogFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CoverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoverFragment extends GACoverFragment implements DialogFragment.OnDialogFinishListener<Boolean>{

    public static final String TAG = CoverFragment.class.getSimpleName();

    public static String M_TAG;

    private static final int VIEW_RES_ID = R.layout.view_cover;
    private static final String PACKAGE_NAME_GOOGLE_TTS = "com.google.android.tts";
    private static final String PACKAGE_NAME_TESTING_APP = "cc.forestapp";

    private boolean hasUserLeftAppFirst = false;

    private View mView;

    public CoverFragment() {
        // Required empty public constructor
    }

    public static CoverFragment newInstance() {
        CoverFragment fragment = new CoverFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        M_TAG = getTag();
        hasUserLeftAppFirst = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView =  inflater.inflate(VIEW_RES_ID, container, false);
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
//        directToVocabuLazy();
        if (checkAppInstalledOrNot(PACKAGE_NAME_GOOGLE_TTS)) {
            directToVocabuLazy();

        } else {
                CoverDialogFragment dialogFragment = new CoverDialogFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.add(CoverActivity.VIEW_MAIN_RES_ID, dialogFragment, "CoverDialogFragment");
                fragmentTransaction.addToBackStack("CoverFragment");
                fragmentTransaction.commit();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hasUserLeftAppFirst = true;
    }

    @Override
    public void onDialogFinish(Boolean ifYesClicked) {
        if (ifYesClicked) {
            Log.d(TAG, "YES");
            directToGooglePlay(PACKAGE_NAME_GOOGLE_TTS);
        } else {
            Log.d(TAG, "NO");
            directToVocabuLazy();
        }
    }

    private boolean checkAppInstalledOrNot(String uri) {
        PackageManager pm = getActivity().getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    private void directToVocabuLazy() {

        if (mView == null) return;

        final Intent intent = new Intent(getActivity(), MainActivity.class);
        mView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!hasUserLeftAppFirst) {
                    Logger.d(TAG, "use hasn't left app");
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    Logger.d(TAG, "user already left app");
                }
            }
        }, 2000);
    }

    private void directToGooglePlay(String appPackageName) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }
}
