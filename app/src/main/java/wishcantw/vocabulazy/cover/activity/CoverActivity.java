package wishcantw.vocabulazy.cover.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.database.DatabaseCallback;
import wishcantw.vocabulazy.mainmenu.activity.MainMenuActivity;
import wishcantw.vocabulazy.database.Database;
import wishcantw.vocabulazy.database.VersionCode;
import wishcantw.vocabulazy.utility.Logger;

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

        // check version code
        checkVersion();
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
        Database.getInstance().init(getApplicationContext(), new DatabaseCallback() {
            @Override
            public void succeed() {
                super.succeed();
                // navigate to main menu
                navigateToMainMenu();
            }

            @Override
            public void failed() {
                super.failed();
                // should show some information
            }
        });
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

    private void checkVersion() {

        /**
         * step 1: read a config from internal storage which stores the version code
         step 2.1: if filenotfoundexception caught, then show nothing and create a config with version code
         step 2.2: if config found, check the version code
         step 2.2.1: if version code match the latest, return
         step 2.2.2: if version code is older, show update dialog and update version code in config
         */

        int versionCode = -1;
        String versionName = "";
        FileInputStream fis = null;

        try {
            versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            Logger.d(TAG, "version code in apk: " + versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            // package name not found
        }

        try {
            fis = openFileInput(getString(R.string.version_config));
        } catch (FileNotFoundException foe) {
            try {
                Logger.d(TAG, "Config not found, create version code config");
                // config not found, create one with version code
                FileOutputStream fos = openFileOutput(getString(R.string.version_config), Context.MODE_PRIVATE);
                fos.write(new Gson().toJson(new VersionCode(versionCode)).getBytes());
                fos.close();

                // check whether Text-to-Speech Engine is installed
                checkTTSEngine();
            } catch (IOException ioe) {
                // io exception
            }
        }

        if (fis == null) return;
        StringBuilder builder = new StringBuilder();
        try {
            String readLine;
            BufferedReader bfdReader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));

            while ((readLine = bfdReader.readLine()) != null) {
                builder.append(readLine);
            }

            bfdReader.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int versionCodeInConfig = new Gson().fromJson(builder.toString(), VersionCode.class).getVersionCode();

        Logger.d(TAG, "version code in config: " + versionCodeInConfig);

        if (versionCodeInConfig >= versionCode) {
            // check whether Text-to-Speech Engine is installed
            checkTTSEngine();
            return;
        }

        // if version code is not the latest
        // show the dialog and update the version code
        new AlertDialog.Builder(CoverActivity.this)
                .setTitle(String.format(Locale.ENGLISH, getString(R.string.cover_dialog_update_title), versionName))
                .setMessage(getString(R.string.cover_dialog_update_message))
                .setPositiveButton(getString(R.string.cover_dialog_update_positive_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        // check whether Text-to-Speech Engine is installed
                        checkTTSEngine();
                    }
                })
                .setCancelable(false)
                .create()
                .show();

        // update version code config
        try {
            Logger.d(TAG, "update config version");
            FileOutputStream fos = openFileOutput(getString(R.string.version_config), Context.MODE_PRIVATE);
            fos.write(new Gson().toJson(new VersionCode(versionCode)).getBytes());
            fos.close();
        } catch (IOException ioe) {
            // io exception
        }
    }
}
