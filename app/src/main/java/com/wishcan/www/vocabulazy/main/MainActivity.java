package com.wishcan.www.vocabulazy.main;


import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.search.SearchActivity;
import com.wishcan.www.vocabulazy.main.fragment.MainFragment;
import com.wishcan.www.vocabulazy.service.AudioService;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.Lesson;
import com.wishcan.www.vocabulazy.widget.FragmentWithActionBarTitle;

import java.util.ArrayList;

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
    private int mBackStackCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.d(TAG, "onCreate");

        setContentView(VIEW_ACTIVITY_RES_ID);
        if (savedInstanceState == null) {
            mMainFragment = new MainFragment();
            mFragmentManager = getSupportFragmentManager();
            mFragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                @Override
                public void onBackStackChanged() {
                    Log.d("MainActivity BackStack", "Stack Change");
                    int backStackCount;
                    if((backStackCount = mFragmentManager.getBackStackEntryCount()) > 0) {      //Not in MainFragment
                        if(backStackCount < mBackStackCount) {  // Back button is pressed
                            Log.d("MainActivity BackStack", "Fragment out BackStack");
                            String str = mFragmentManager.getBackStackEntryAt(backStackCount - 1).getName();
                            Fragment f = mFragmentManager.findFragmentByTag(str);
                            if (f != null && f instanceof FragmentWithActionBarTitle)
                                setActionBarTitle(((FragmentWithActionBarTitle) f).getActionBarTitle());
                        }
                        else {
                            Log.d("MainActivity BackStack", "Fragment into BackStack");
                            mActionBar = getActionBar();
                            if(mActionBar != null) {
                                mActionBar.setDisplayHomeAsUpEnabled(true);
                            }
                        }
                    }
                    else {    //Back to MainFragment
                        Log.d("MainActivity BackStack", "" + MainFragment.class.getName());
                        mActionBar = getActionBar();
                        if(mActionBar != null) {
                            mActionBar.setDisplayHomeAsUpEnabled(false);
                        }
                    }
                    mBackStackCount = backStackCount;
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

//        Log.d(TAG, "" + mDatabase);

        mMainActivity = this;

        mBackStackCount = 0;

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
        stopAudioService();
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
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SearchActivity.class);
            // the number "1" is to identify the action
            startActivityForResult(intent, 1);
            return true;
        }

        return super.onOptionsItemSelected(item);
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
