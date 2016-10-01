package com.wishcan.www.vocabulazy.cover.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.cover.fragment.CoverDialogFragment;
import com.wishcan.www.vocabulazy.mainmenu.activity.MainMenuActivity;
import com.wishcan.www.vocabulazy.storage.Database;

public class CoverActivity extends FragmentActivity implements CoverDialogFragment.OnDialogClickListener {

    public static final String TAG = "CoverActivity";

    public static final int VIEW_MAIN_RES_ID = R.id.activity_cover_container;
    private static final int VIEW_ACTIVITY_RES_ID = R.layout.activity_cover;
    private static final String PACKAGE_NAME_GOOGLE_TTS_ENGINE = "com.google.android.tts";

    private boolean isEngineInstalled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(VIEW_ACTIVITY_RES_ID);

        // check whether Google TTS engine has been installed
        isEngineInstalled = checkAppInstalledOrNot(PACKAGE_NAME_GOOGLE_TTS_ENGINE);

        // use AsyncTask to load database, prevent UI thread from being held
        loadDatabase();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "Resume");
        super.onResume();

        // if the tts engine is not installed, launch the dialoag asking user to download and
        // install Google Text-to-Speech Engine
//        if (!isEngineInstalled) {
//            Log.d(TAG, "launching cover dialog");
//            CoverDialogFragment fragment = new CoverDialogFragment();
//            fragment.addOnDialogClickListener(this);
//            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.add(VIEW_MAIN_RES_ID, fragment, "CoverDialogFragment");
//            fragmentTransaction.commit();
//        }
    }

    @Override
    public void onYesClicked() {
        directToGooglePlay(PACKAGE_NAME_GOOGLE_TTS_ENGINE);
    }

    @Override
    public void onNoClicked() {
        directToVocabuLazy();
    }

    private void loadDatabase() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Log.d(TAG, "start loading database");
                Database database = Database.getInstance();
                database.loadFiles(getApplicationContext());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Log.d(TAG, "finish loading database");
                super.onPostExecute(aVoid);
                if (isEngineInstalled) {
                    directToVocabuLazy();

                } else {
                    CoverDialogFragment fragment = new CoverDialogFragment();
                    fragment.addOnDialogClickListener(CoverActivity.this);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.add(VIEW_MAIN_RES_ID, fragment, "CoverDialogFragment");
                    fragmentTransaction.commit();
                }
            }
        }.execute();
    }

    private boolean checkAppInstalledOrNot(String appUri) {
        boolean isAppInstalled;
        try {
            getPackageManager().getPackageInfo(appUri, PackageManager.GET_ACTIVITIES);
            isAppInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            isAppInstalled = false;
        }
        return isAppInstalled;
    }

    private void directToVocabuLazy() {
        startActivity(new Intent(CoverActivity.this, MainMenuActivity.class));
        finish();
    }

    private void directToGooglePlay(String appPackageName) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }
}
