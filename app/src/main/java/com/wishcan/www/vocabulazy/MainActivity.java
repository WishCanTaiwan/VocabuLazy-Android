package com.wishcan.www.vocabulazy;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.wishcan.www.vocabulazy.player.AudioService;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.Lesson;
import com.wishcan.www.vocabulazy.view.reading.ReadingLessonFragment;
import com.wishcan.www.vocabulazy.view.lessons.LessonsFragment;
import com.wishcan.www.vocabulazy.view.main.MainFragment;
import com.wishcan.www.vocabulazy.view.player.PlayerFragment;
import com.wishcan.www.vocabulazy.view.reading.ReadingMainFragment;

import java.util.ArrayList;

public class MainActivity extends Activity implements PlayerFragment.OptionOnClickListener {

    public static final String PREVIOUS_TITLE = "previous_title";

    public static final String CURRENT_FRAGMENT_TAG = "current_fragment_tag";

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int ACTIVITY_RESOURCE_ID = R.layout.activity_main;

    private static final int MAIN_MENU_RES_ID = R.menu.menu_main;

    private static final int DEFAULT_CUSTOM_ACTION_BAR_RES_ID = R.layout.action_bar_custom;

    private static final int REQUEST_CODE_DATABASE_UPDATE = 1;

    public static MainActivity mMainActivity;

    private TextView mActionBarTitleTextView;

    /**
     * The object is used for recording the user's favorite word
     * */
//    private VocabularyList mFavoriteVocabularyList;

    /**
     * The object is used for recording the user's playing list
     * */
//    private VocabularyList mPlayingVocabularyList;

    /**
     *
     * */
    private MainFragment mMainFragment;

    /**
     *
     * */
    private LessonsFragment mLessonsFragment;

    /**
     *
     * */
    private PlayerFragment mPlayerFragment;

    private ReadingLessonFragment mReadingLessonFragment;

    private ReadingMainFragment mReadingMainFragment;

    /**
     *
     * */
    private FragmentManager mFragmentManager;

    private Database mDatabase;

    private ActionBar mActionBar;

    private String mActionBarTitleWhenStop;

    /**
     * This is an important parameter for restoring the fragment in our activity
     * */
    private String mCurrentFragmentTag;

    private boolean mSearchActivityEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mCurrentFragmentTag = savedInstanceState.getString(MainActivity.CURRENT_FRAGMENT_TAG);
        } else {
            mCurrentFragmentTag = "mainfragment";
        }
//        vocabulary = new Vocabulary(getResources(), "database/Vocabulary.json");

        setContentView(ACTIVITY_RESOURCE_ID);

        setCustomActionBar();

        mMainActivity = this;

        startAudioService();

        mFragmentManager = getFragmentManager();

        mDatabase = new Database(this);

        if (savedInstanceState == null) {

            Log.d(TAG, "savedInstanceState: null");


            mMainFragment = MainFragment.newInstance(mDatabase);
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.activity_main_container, mMainFragment, "mainfragment");
            fragmentTransaction.commit();


        } else {

            Log.d(TAG, "savedInstanceState: not null");

            mMainFragment = (MainFragment) mFragmentManager.findFragmentByTag("mainfragment");
            mLessonsFragment = (LessonsFragment) mFragmentManager.findFragmentByTag("lessonsfragment");
            mPlayerFragment = (PlayerFragment) mFragmentManager.findFragmentByTag("playerfragment");

//            mDatabase = savedInstanceState.getParcelable("database");
        }

        mSearchActivityEnabled = false;

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
//        outState.putParcelable("database", mDatabase);
        outState.putString(MainActivity.CURRENT_FRAGMENT_TAG, mCurrentFragmentTag);
        outState.putString(MainActivity.PREVIOUS_TITLE, mActionBarTitleTextView.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();

//        mDatabase = new Database(this);

        if(mActionBarTitleWhenStop!= null)
            switchActionBarTitle(mActionBarTitleWhenStop);

//        Log.d(TAG, "Current Fragment: " + mCurrentFragmentTag);

        if (mCurrentFragmentTag.equals("mainfragment"))
            mActionBar.setDisplayHomeAsUpEnabled(false);
        else
            mActionBar.setDisplayHomeAsUpEnabled(true);


    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
        mActionBar = getActionBar();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        mDatabase.writeToFile(this);
        super.onPause();


    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        if (!mSearchActivityEnabled) {
            mDatabase.writeToFile(this);
        }
        stopAudioService();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(MAIN_MENU_RES_ID, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.action_search) {
            mSearchActivityEnabled = true;
            Intent intent = new Intent(this, SearchActivity.class);
            Bundle bundle = new Bundle();
//            bundle.putParcelable("database", mDatabase);
            intent.putExtras(bundle);
            // the number "1" is to identify the action
            startActivityForResult(intent, REQUEST_CODE_DATABASE_UPDATE);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_DATABASE_UPDATE) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "result code: ok");
                mSearchActivityEnabled = false;
                Bundle bundle = data.getExtras();
//                mDatabase = bundle.getParcelable("database");

                mDatabase.loadNotes();
                ArrayList<Lesson> note = mDatabase.getLessonsByBook(-1);

                mMainFragment.refreshFragment();
            }
        }

    }

    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {

        // only when there's no dialog, a popup will occur
        if(mMainFragment.getDialog() == null){

            int backStackEntryCount = getFragmentManager().getBackStackEntryCount();
//            Log.d("MainActivity", "onBackPressed " + backStackEntryCount);
//            Log.d("MainActivity", "onBackPressed " + getFragmentManager().getBackStackEntryAt(backStackEntryCount -1).getName());

            if (backStackEntryCount > 0) {
                if (getFragmentManager().getBackStackEntryAt(backStackEntryCount - 1).getName().equals("mainfragment")) {
                    if (getActionBar() != null)
                        getActionBar().setDisplayHomeAsUpEnabled(false);
                } else {
                    if (getActionBar() != null)
                        getActionBar().setDisplayHomeAsUpEnabled(true);
                }
                mCurrentFragmentTag = getFragmentManager().getBackStackEntryAt(backStackEntryCount - 1).getName();
            } else {

            }

            super.onBackPressed();
        }
        else
            mMainFragment.closeDialog(mMainFragment.getDialog());
    }

    private void startAudioService() {
        Intent intent = new Intent(this, AudioService.class);
        intent.setAction(AudioService.ACTION_INIT);
        startService(intent);
    }

    private void stopAudioService() {
        Intent intent = new Intent(this, AudioService.class);
        intent.setAction(AudioService.ACTION_POST_STOP_SERVICE);
        startService(intent);
    }

    public void goPlayerFragment(int bookIndex, int lessonIndex) {

        mPlayerFragment = PlayerFragment.newInstance(mActionBarTitleTextView.getText().toString(), bookIndex, lessonIndex, mDatabase);
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fragment_translate_slide_from_right_to_center, R.anim.fragment_translate_slide_from_center_to_left, R.anim.fragment_translate_slide_from_left_to_center, R.anim.fragment_translate_slide_from_center_to_right);
        fragmentTransaction.add(R.id.activity_main_container, mPlayerFragment, "playerfragment");
        fragmentTransaction.addToBackStack(mCurrentFragmentTag);
        mCurrentFragmentTag = "playerfragment";
        fragmentTransaction.commit();
    }

    public void goLessonFragment(int bookIndex){

        mLessonsFragment = LessonsFragment.newInstance(mActionBarTitleTextView.getText().toString(), bookIndex, mDatabase);
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fragment_translate_slide_from_right_to_center, R.anim.fragment_translate_slide_from_center_to_left, R.anim.fragment_translate_slide_from_left_to_center, R.anim.fragment_translate_slide_from_center_to_right);
        fragmentTransaction.add(R.id.activity_main_container, mLessonsFragment, "lessonsfragment");
        fragmentTransaction.addToBackStack(mCurrentFragmentTag);
        mCurrentFragmentTag = "lessonsfragment";
        fragmentTransaction.commit();
    }

    public void goReadingLessonFragment(int bookIndex){

        mReadingLessonFragment = ReadingLessonFragment.newInstance(mActionBarTitleTextView.getText().toString(), bookIndex, mDatabase);
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fragment_translate_slide_from_right_to_center, R.anim.fragment_translate_slide_from_center_to_left, R.anim.fragment_translate_slide_from_left_to_center, R.anim.fragment_translate_slide_from_center_to_right);
        fragmentTransaction.add(R.id.activity_main_container, mReadingLessonFragment, "readinglessonsfragment");
        fragmentTransaction.addToBackStack(mCurrentFragmentTag);
        mCurrentFragmentTag = "readinglessonsfragment";
        fragmentTransaction.commit();
    }

    public void goReadingMainFragment(int bookIndex, int lessonIndex){
        mReadingMainFragment = ReadingMainFragment.newInstance(mActionBarTitleTextView.getText().toString(), bookIndex, lessonIndex, mDatabase);
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fragment_translate_slide_from_right_to_center, R.anim.fragment_translate_slide_from_center_to_left, R.anim.fragment_translate_slide_from_left_to_center, R.anim.fragment_translate_slide_from_center_to_right);
        fragmentTransaction.add(R.id.activity_main_container, mReadingMainFragment, "readingmainfragment");
        fragmentTransaction.addToBackStack(mCurrentFragmentTag);
        mCurrentFragmentTag = "readingmainfragment";
        fragmentTransaction.commit();
    }

    public void switchActionBarTitle(String str) {
        if(mActionBarTitleTextView != null)
            mActionBarTitleTextView.setText(str);
        invalidateOptionsMenu();
    }

    public void setActionBarTitleWhenStop(String str){
        mActionBarTitleWhenStop = str;
    }

    public String getActionBarTitleTextView(){
        return mActionBarTitleTextView.getText().toString();
    }

    @Override
    public void onOptionClicked(View v) {
        int optionViewId = v.getId();
        mPlayerFragment = (PlayerFragment) mFragmentManager.findFragmentByTag("playerfragment");
        switch(optionViewId){
            case R.id.action_player_favorite:
                mPlayerFragment.onOptionClicked(v);
                break;
            case R.id.action_player_option:
                mPlayerFragment.onOptionClicked(v);
                break;
            case R.id.action_player_play:
                mPlayerFragment.onOptionClicked(v);
                break;
            case R.id.action_reading_main_mute:
                break;
            case R.id.action_reading_main_play:
                break;
            case R.id.action_reading_main_next:
                break;
            case R.id.action_reading_main_previous:
                break;
            default:
                break;

        }


    }

    public Database getDatabase() {
        return mDatabase;
    }

    private void setCustomActionBar(){
        mActionBar = getActionBar();
        if(mActionBar != null) {
            mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_HOME_AS_UP);
            mActionBar.setCustomView(DEFAULT_CUSTOM_ACTION_BAR_RES_ID);


            mActionBarTitleTextView = (TextView) mActionBar.getCustomView().findViewById(R.id.custom_bar_title);
            Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/DFHeiStd-W5.otf");
            mActionBarTitleTextView.setTypeface(tf);
        }

    }

}
