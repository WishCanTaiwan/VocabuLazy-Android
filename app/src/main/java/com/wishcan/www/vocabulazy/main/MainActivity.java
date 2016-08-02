package com.wishcan.www.vocabulazy.main;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.analytics.Tracker;
import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.VLApplication;
import com.wishcan.www.vocabulazy.ga.GAFragment;
import com.wishcan.www.vocabulazy.log.Logger;
import com.wishcan.www.vocabulazy.main.player.fragment.PlayerFragment;
import com.wishcan.www.vocabulazy.search.SearchActivity;
import com.wishcan.www.vocabulazy.main.fragment.MainFragment;
import com.wishcan.www.vocabulazy.service.AudioPlayer;
import com.wishcan.www.vocabulazy.service.AudioService;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.Preferences;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final int VIEW_MAIN_RES_ID = R.id.activity_main_container;
    public static final int ANIM_ENTER_RES_ID = R.anim.translation_from_right_to_center;
    public static final int ANIM_EXIT_RES_ID = R.anim.translation_from_center_to_right;

    private static final int VIEW_ACTIVITY_RES_ID = R.layout.view_main_activity;
    private static final int DEFAULT_CUSTOM_ACTION_BAR_RES_ID = R.layout.view_main_activity_action_bar;
    private static final int TITLE_RES_ID = R.id.custom_bar_title;
    private static final int ITEM_SEARCH_RES_ID = R.id.action_settings;
    private static final int ITEM_SHORTCUT_RES_ID = R.id.action_goto_player;
    private static final String FONT_RES_STR = "fonts/DFHeiStd-W5.otf";

    private static final String[] TITLES = new String[]{"Book", "Note", "Exam", "Info"};
    private GAFragment[] mFragments = new GAFragment[]{};

    public enum FRAGMENT_FLOW {
        GO, BACK, SAME
    }

    public enum FRAGMENT_ANIM {
        NONE, DEFAULT
    }

    private MainFragment mMainFragment;
    private FragmentManager mFragmentManager;
    private ActionBar mActionBar;
    private TextView mActionBarTitleTextView;
    private LinkedList<String> mActionBarLL;
    private Menu mOptionMenu;

    private Database wDatabase;
    private Preferences wPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(VIEW_ACTIVITY_RES_ID);

        VLApplication application = (VLApplication) getApplication();
        wDatabase = application.getDatabase();
        wPreferences = application.getPreferences();
        mActionBarLL = new LinkedList<>();

        initFragments();
        mFragments = new GAFragment[]{mVocBookFragment, mUsrNoteFragment, mExamIndexFragment, mInfoFragment};

        if (savedInstanceState == null) {
            if (getActionBar() != null)
                getActionBar().setDisplayHomeAsUpEnabled(false);
            mMainFragment = MainFragment.newInstance();
            mMainFragment.setTitles(TITLES);
            mMainFragment.setFragments(mFragments);
            mFragmentManager = getSupportFragmentManager();
            mFragmentManager.addOnBackStackChangedListener(this);
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.add(VIEW_MAIN_RES_ID, mMainFragment, "MainFragment");
            fragmentTransaction.commit();
        }
        startAudioService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mActionBar == null) {
            mActionBar = getActionBar();
        }
        setCustomActionBar();
        if (mActionBarLL != null) {
            setActionBarTitle(mActionBarLL.getFirst());
            if (mFragmentManager == null) {
                mFragmentManager = getSupportFragmentManager();
            }
            if (mFragmentManager.getBackStackEntryCount() <= 0) {
                if (mActionBar != null) {
                    mActionBar.setDisplayHomeAsUpEnabled(false);
                }
            } else {
                if (mActionBar != null) {
                    mActionBar.setDisplayHomeAsUpEnabled(true);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAudioService();
        wDatabase.writeToFile();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mOptionMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == ITEM_SEARCH_RES_ID) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivityForResult(intent, 1);
            return true;
        } else if (id == ITEM_SHORTCUT_RES_ID) {
            // fetching player information from database.
            int bookIndex = wPreferences.getBookIndex();
            int lessonIndex = wPreferences.getLessonIndex();
            if(bookIndex != 1359 && lessonIndex != 1359) {
                Bundle args = new Bundle();
                args.putInt(PlayerFragment.BOOK_INDEX_STR, bookIndex);
                args.putInt(PlayerFragment.LESSON_INDEX_STR, lessonIndex);
                goFragment(PlayerFragment.class, args, "PlayerFragment", "MainFragment");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackStackChanged() {
        if (getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(mFragmentManager.getBackStackEntryCount() > 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        switchActionBarStr(FRAGMENT_FLOW.BACK, null);
    }

    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return super.onNavigateUp();
    }

    public Fragment goFragment(Class<?> cls, Bundle bundle, String newTag, String backStackTag, FRAGMENT_ANIM animEnterResId, FRAGMENT_ANIM animExitResId) {
        int enterResId, exitResId;

        Fragment f = Fragment.instantiate(this, cls.getName(), bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        enterResId = animEnterResId == FRAGMENT_ANIM.DEFAULT ? MainActivity.ANIM_ENTER_RES_ID : -1;
        exitResId = animExitResId == FRAGMENT_ANIM.DEFAULT ? MainActivity.ANIM_EXIT_RES_ID: -1;

        if (enterResId != -1 && exitResId != -1) {
            fragmentTransaction.setCustomAnimations(enterResId, exitResId, enterResId, exitResId);
        }
        if(newTag == null || newTag.equals(""))
            newTag = "newTag";
        fragmentTransaction.add(VIEW_MAIN_RES_ID, f, newTag);
        if(backStackTag != null && !backStackTag.equals(""))
            fragmentTransaction.addToBackStack(backStackTag);
        fragmentTransaction.commit();
        return f;
    }

    public Fragment goFragment(Class<?> cls, Bundle bundle, String newTag, String backStackTag) {
        return goFragment(cls, bundle, newTag, backStackTag, FRAGMENT_ANIM.DEFAULT, FRAGMENT_ANIM.DEFAULT);
    }

    public void switchActionBarStr(FRAGMENT_FLOW flow, String newActionBarStr) {
        Log.d(TAG, "notifyFragmentChange " + newActionBarStr);
        if (newActionBarStr == null) {
            newActionBarStr = "";
        }
        switch (flow) {
            case GO:
                mActionBarLL.addFirst(newActionBarStr);
                setActionBarTitle(mActionBarLL.getFirst());
                break;
            case BACK:
                if(mActionBarLL.size() > 1) {
                    mActionBarLL.removeFirst();
                    setActionBarTitle(mActionBarLL.getFirst());
                }
                break;
            case SAME:
                mActionBarLL.removeFirst();
                mActionBarLL.addFirst(newActionBarStr);
                setActionBarTitle(mActionBarLL.getFirst());
                break;
            default:
                break;
        }
    }

    public void enableExpressWay(boolean enable) {
        MenuItem item = mOptionMenu.findItem(R.id.action_goto_player);
        if(item == null)
            return;
        if(enable) {
            item.setVisible(true);
        } else {
            item.setVisible(false);
        }
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

    private void setActionBarTitle(String titleStr) {
        if(mActionBarTitleTextView == null) {
            mActionBar = getActionBar();
            if(mActionBar != null && mActionBar.getCustomView() != null)
                mActionBarTitleTextView = (TextView) mActionBar.getCustomView().findViewById(TITLE_RES_ID);
        }
        if(mActionBarTitleTextView != null)
            mActionBarTitleTextView.setText(titleStr);
    }

    private void startAudioService() {
        Intent intent = new Intent(this, AudioService.class);
        intent.setAction(AudioService.START_SERVICE);
        startService(intent);
    }

    private void stopAudioService() {
        Intent intent = new Intent(this, AudioService.class);
        intent.setAction(AudioService.STOP_SERVICE);
        startService(intent);
    }
}
