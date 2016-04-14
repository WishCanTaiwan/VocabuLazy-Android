package com.wishcan.www.vocabulazy.main;


import android.app.ActionBar;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.VLApplication;
import com.wishcan.www.vocabulazy.main.player.fragment.PlayerFragment;
import com.wishcan.www.vocabulazy.search.SearchActivity;
import com.wishcan.www.vocabulazy.main.fragment.MainFragment;
import com.wishcan.www.vocabulazy.service.AudioService;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.Lesson;
import com.wishcan.www.vocabulazy.widget.FragmentWithActionBarTitle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

//import io.uxtesting.UXTesting;

public class MainActivity extends FragmentActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static MainActivity mMainActivity;
    public static final int VIEW_MAIN_RES_ID = R.id.activity_main_container;
    public static final int ANIM_ENTER_RES_ID = R.anim.translation_from_right_to_center;
    public static final int ANIM_EXIT_RES_ID = R.anim.translation_from_center_to_right;

    private static final int VIEW_ACTIVITY_RES_ID = R.layout.view_main_activity;
    private static final int DEFAULT_CUSTOM_ACTION_BAR_RES_ID = R.layout.view_main_activity_action_bar;
    private static final int TITLE_RES_ID = R.id.custom_bar_title;
    private static final String FONT_RES_STR = "fonts/DFHeiStd-W5.otf";

    private MainFragment mMainFragment;
    private FragmentManager mFragmentManager;
    private ActionBar mActionBar;
    private TextView mActionBarTitleTextView;
    private static Database mDatabase;

    private Tracker wTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        VLApplication application = (VLApplication) getApplication();
        wTracker = application.getDefaultTracker();

        setContentView(VIEW_ACTIVITY_RES_ID);
        if (savedInstanceState == null) {
            mMainFragment = new MainFragment();
            mFragmentManager = getSupportFragmentManager();
            if(getActionBar() != null)
                getActionBar().setDisplayHomeAsUpEnabled(true);
            mFragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                @Override
                public void onBackStackChanged() {
                    if(mFragmentManager.getBackStackEntryCount() <= 0) {
                        getActionBar().setDisplayHomeAsUpEnabled(false);
                    } else {
                        getActionBar().setDisplayHomeAsUpEnabled(true);
                    }
                }
            });
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.add(VIEW_MAIN_RES_ID, mMainFragment, "MainFragment");
            fragmentTransaction.commit();
        }

        if (mDatabase == null) {
            mDatabase = new Database(this);
        } else {
            Log.d("MainActivity", "database already exist.");
        }

        mMainActivity = this;

//        downloadTTSFiles();

        startAudioService();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mDatabase != null) {
            mDatabase.loadNotes();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        setCustomActionBar();
        Fragment f = getSupportFragmentManager().findFragmentByTag("MainFragment");
        if(f != null && f instanceof FragmentWithActionBarTitle)
            setActionBarTitle(((FragmentWithActionBarTitle)f).getActionBarTitle());
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDatabase.writeToFile(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAudioService();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivityForResult(intent, 1);
            return true;
        } else if (id == R.id.action_goto_player) {

            // fetching player information from database.
            Bundle playerInfo = mDatabase.loadPlayerInfo();
            if(playerInfo != null) {
                int bookIndex = playerInfo.getInt(PlayerFragment.KEY_BOOK_INDEX, 1359);
                int lessonIndex = playerInfo.getInt(PlayerFragment.KEY_LESSON_INDEX, 1359);

                Log.d(TAG, "retrive bookIndex " + bookIndex + " lessonIndex " + lessonIndex);
                Log.d(TAG, mDatabase.toString());

                Bundle args = new Bundle();
                args.putInt(PlayerFragment.BOOK_INDEX_STR, bookIndex);
                args.putInt(PlayerFragment.LESSON_INDEX_STR, lessonIndex);
                goFragment(PlayerFragment.class, args, "PlayerFragment", "MainFragment");
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        notifyFragmentChange();
    }

    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return super.onNavigateUp();
    }

    public Database getDatabase() {
        return mDatabase;
    }

    public void goFragment(Class<?> cls, Bundle bundle, String newTag, String backStackTag) {
        Fragment f = Fragment.instantiate(this, cls.getName(), bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.
                setCustomAnimations(MainActivity.ANIM_ENTER_RES_ID, MainActivity.ANIM_EXIT_RES_ID,
                        MainActivity.ANIM_ENTER_RES_ID, MainActivity.ANIM_EXIT_RES_ID);
        if(newTag == null || newTag.equals(""))
            newTag = "newTag";
        fragmentTransaction.add(MainActivity.VIEW_MAIN_RES_ID, f, newTag);
        if(backStackTag != null && !backStackTag.equals(""))
            fragmentTransaction.addToBackStack(backStackTag);
        fragmentTransaction.commit();
        if(f instanceof FragmentWithActionBarTitle)
            setActionBarTitle(((FragmentWithActionBarTitle) f).getActionBarTitle());

        notifyFragmentChange();
    }

    private void notifyFragmentChange() {
        Log.d(TAG, "notifyFragmentChange()");
    }

    private void downloadTTSFiles() {

        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.tts")));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.tts")));
        }

//        Intent intent = new Intent(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
//        startActivityForResult(intent, -1);

//        Intent intent = new Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setPackage("com.google.android.tts");
//        try {
//            Log.d(TAG, "Installing voice data: " + intent.toUri(0));
//            startActivity(intent);
//        } catch (ActivityNotFoundException ex) {
//            Log.d(TAG, "Failed to install TTS data, no acitivty found for " + intent + ")");
//        }
    }

    private void startAudioService() {
        Intent intent = new Intent(this, AudioService.class);
        intent.setAction(AudioService.ACTION_START_SERVICE);
        startService(intent);
    }

    private void setCustomActionBar(){
        mActionBar = getActionBar();
        if(mActionBar != null) {
            mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM /*| ActionBar.DISPLAY_HOME_AS_UP*/);
            mActionBar.setCustomView(DEFAULT_CUSTOM_ACTION_BAR_RES_ID);
            mActionBarTitleTextView = (TextView) mActionBar.getCustomView().findViewById(TITLE_RES_ID);
            Typeface tf = Typeface.createFromAsset(getAssets(), FONT_RES_STR);
            mActionBarTitleTextView.setTypeface(tf);
        }
    }

    public void setActionBarTitle(FragmentWithActionBarTitle f) {
        setActionBarTitle(f.getActionBarTitle());
    }

    public void setActionBarTitle(String titleStr) {
        if(mActionBarTitleTextView == null) {
            mActionBar = getActionBar();
            if(mActionBar != null && mActionBar.getCustomView() != null)
                mActionBarTitleTextView = (TextView) mActionBar.getCustomView().findViewById(TITLE_RES_ID);
        }
        if(mActionBarTitleTextView != null)
            mActionBarTitleTextView.setText(titleStr);
    }

    private void stopAudioService() {
        Intent intent = new Intent(this, AudioService.class);
        intent.setAction(AudioService.ACTION_STOP_SERVICE);
        startService(intent);
    }
}
