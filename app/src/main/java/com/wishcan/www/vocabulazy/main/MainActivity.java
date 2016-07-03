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

public class MainActivity extends FragmentActivity {

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

    private Tracker wTracker;
    private Database wDatabase;
    private Preferences wPreferences;

    private BroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        VLApplication application = (VLApplication) getApplication();
        wTracker = application.getDefaultTracker();
        wDatabase = application.getDatabase();
        wPreferences = application.getPreferences();

        mActionBarLL = new LinkedList<>();

        setContentView(VIEW_ACTIVITY_RES_ID);
        if (savedInstanceState == null) {
            mMainFragment = new MainFragment();
            mFragmentManager = getSupportFragmentManager();
            if(getActionBar() != null) {
                getActionBar().setDisplayHomeAsUpEnabled(false);
            }
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
        registerBroadcastReceiver();
        startAudioService();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
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
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        stopAudioService();
        unregisterBroadcastReceiver();
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
                Log.d(TAG, "retrive bookIndex " + bookIndex + " lessonIndex " + lessonIndex);
                Log.d(TAG, wDatabase.toString());

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
        if (animEnterResId == FRAGMENT_ANIM.DEFAULT) {
            enterResId = MainActivity.ANIM_ENTER_RES_ID;
        } else {
            enterResId = -1;
        }
        if (animExitResId == FRAGMENT_ANIM.DEFAULT) {
            exitResId = MainActivity.ANIM_EXIT_RES_ID;
        } else {
            exitResId = -1;
        }
        if (enterResId != -1 && exitResId != -1) {
            fragmentTransaction.
                    setCustomAnimations(enterResId, exitResId,
                            enterResId, exitResId);
        }
        if(newTag == null || newTag.equals(""))
            newTag = "newTag";
        fragmentTransaction.add(MainActivity.VIEW_MAIN_RES_ID, f, newTag);
        if(backStackTag != null && !backStackTag.equals(""))
            fragmentTransaction.addToBackStack(backStackTag);
        fragmentTransaction.commit();
        return f;
    }

    public Fragment goFragment(Class<?> cls, Bundle bundle, String newTag, String backStackTag) {
        return goFragment(cls, bundle, newTag, backStackTag, FRAGMENT_ANIM.DEFAULT, FRAGMENT_ANIM.DEFAULT);
    }

    public void switchActionBarStr(FRAGMENT_FLOW flow, String newActionBarStr) {
        Log.d(TAG, "notifyFragmentChange()");
        if(newActionBarStr == null) {
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
                Log.d(TAG, "switchActionBar SAME");
                mActionBarLL.removeFirst();
                mActionBarLL.addFirst(newActionBarStr);
                setActionBarTitle(mActionBarLL.getFirst());
                break;
            default:
                break;
        }
        Log.d(TAG, "switchActionBarStr " +mActionBarLL.size());
        if(mActionBarLL.size() > 0)
            Log.d(TAG, "switchActionBarStr " +mActionBarLL.getFirst());
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

    private void registerBroadcastReceiver() {
        /* register broadcast receiver */
        IntentFilter intentFilter = new IntentFilter(Preferences.VL_BROADCAST_INTENT);
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getStringExtra(Preferences.VL_BROADCAST_ACTION);
                if (action.equals(AudioService.CHECK_VOICE_DATA)) {
                    checkTTSData();
                }

            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, intentFilter);
        Log.d(TAG, "register receiver");
    }

    private void unregisterBroadcastReceiver() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        Log.d(TAG, "unregister receiver");
    }

    private void startAudioService() {
        Log.d(TAG, "start service");
        Intent intent = new Intent(this, AudioService.class);
        intent.setAction(AudioService.START_SERVICE);
        startService(intent);
    }

    private void stopAudioService() {
        Intent intent = new Intent(this, AudioService.class);
        intent.setAction(AudioService.STOP_SERVICE);
        startService(intent);
    }

    private void checkTTSData() {
        Log.d(TAG, "check TTS data");
        Intent intent = new Intent();
        intent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            // Make sure the request was successful
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                Log.d(TAG, "check voice data pass");
                ArrayList<String> voices = data.getStringArrayListExtra(TextToSpeech.Engine.EXTRA_AVAILABLE_VOICES);
                Log.d(TAG, "available voices:");
                for (String voice : voices) {
                    Log.d(TAG, voice);
                }
            } else {
                Log.d(TAG, "check voice data fail");
            }
        }
    }
}
