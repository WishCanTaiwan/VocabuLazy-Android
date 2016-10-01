package com.wishcan.www.vocabulazy.cover.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.mainmenu.activity.MainMenuActivity;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.utility.Logger;

public class CoverActivity extends FragmentActivity {

    // TAG for debugging
    public static final String TAG = "CoverActivity";

    // layour reource ids
    private static final int VIEW_ACTIVITY_RES_ID = R.layout.activity_cover;

    // google text-to-speech package name
    private static final String PACKAGE_NAME_GOOGLE_TTS_ENGINE = "com.google.android.tts";


    /** Life cycles **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(VIEW_ACTIVITY_RES_ID);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // check whether Text-to-Speech Engine is installed
        checkTTSEngine();
    }

    /** Private methods **/

    private void checkTTSEngine() {
        try {

            // load packages and check google tts engine
            getPackageManager().getPackageInfo(PACKAGE_NAME_GOOGLE_TTS_ENGINE, PackageManager.GET_ACTIVITIES);

            // if no exception happens, means tts engine is installed, then load the database
            loadDatabase();
        } catch (PackageManager.NameNotFoundException e) {

            // exception caught means google tts engine is not installed,
            // pop alert dialog and ask user to install tts engine
            askUserToInstallTTS();
        }
    }

    private void loadDatabase() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Logger.d(TAG, "Start loading database");
                Database database = Database.getInstance();
                database.loadFiles(getApplicationContext());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Logger.d(TAG, "Finish loading database");
                super.onPostExecute(aVoid);
                navigateToMainMenu();
            }
        }.execute();
    }

    private void askUserToInstallTTS() {
        new AlertDialog.Builder(CoverActivity.this)
                .setTitle(getResources().getString(R.string.cover_dialog_title))
                .setMessage(getResources().getString(R.string.cover_dialog_message))
                .setPositiveButton(getResources().getString(R.string.cover_dialog_button_confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        navigateToGooglePlay(PACKAGE_NAME_GOOGLE_TTS_ENGINE);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cover_dialog_button_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadDatabase();
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        // do nothing
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

    private void navigateToMainMenu() {
        startActivity(new Intent(CoverActivity.this, MainMenuActivity.class));
        finish();
    }

    private void navigateToGooglePlay(String appPackageName) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }
}
