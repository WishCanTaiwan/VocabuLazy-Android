package com.wishcan.www.vocabulazy;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.wishcan.www.vocabulazy.player.AudioService;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.Lesson;
import com.wishcan.www.vocabulazy.view.exam.ExamBooksFragment;
import com.wishcan.www.vocabulazy.view.exam.ExamFragment;
import com.wishcan.www.vocabulazy.view.exam.ExamNoteFragment;
import com.wishcan.www.vocabulazy.view.exam.ExamResultFragment;
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
     */
//    private VocabularyList mFavoriteVocabularyList;

    /**
     * The object is used for recording the user's playing list
     */
//    private VocabularyList mPlayingVocabularyList;

    /**
     *
     */
    private MainFragment mMainFragment;

    /**
     *
     */
    private LessonsFragment mLessonsFragment;

    /**
     *
     */
    private PlayerFragment mPlayerFragment;

    private ReadingMainFragment mReadingMainFragment;

    private ExamBooksFragment mExamBooksFragment;

    private ExamNoteFragment mExamNoteFragment;

    private ExamFragment mExamFragment;

    private ExamResultFragment mExamResultFragment;

    /**
     *
     */
    private FragmentManager mFragmentManager;

    private static Database mDatabase;

    private ActionBar mActionBar;

    private String mActionBarTitleWhenStop;

    /**
     * This is an important parameter for restoring the fragment in our activity
     */
    private String mCurrentFragmentTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Log.d(TAG, "onCreate");

        if (savedInstanceState != null) {
            mCurrentFragmentTag = savedInstanceState.getString(MainActivity.CURRENT_FRAGMENT_TAG);
        } else {
            mCurrentFragmentTag = "mainfragment";
        }

        setContentView(ACTIVITY_RESOURCE_ID);

        setCustomActionBar();

        mMainActivity = this;

        mFragmentManager = getFragmentManager();

        if (mDatabase == null) {
            mDatabase = new Database(this);
        } else {
            Log.d(TAG, "database alrdy exist.");
        }
        if (savedInstanceState == null) {
            mMainFragment = MainFragment.newInstance(mDatabase);
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.activity_main_container, mMainFragment, "mainfragment");
            fragmentTransaction.commit();
        } else {
            mMainFragment = (MainFragment) mFragmentManager.findFragmentByTag("mainfragment");
            mLessonsFragment = (LessonsFragment) mFragmentManager.findFragmentByTag("lessonsfragment");
            mPlayerFragment = (PlayerFragment) mFragmentManager.findFragmentByTag("playerfragment");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        Log.d(TAG, "onActivityResult");

        if (requestCode == REQUEST_CODE_DATABASE_UPDATE) {
            if (resultCode == RESULT_OK) {
                mDatabase.loadNotes();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

//        Log.d(TAG, "onStart");

        mActionBar = getActionBar();
        switchActionBarTitle(getResources().getString(R.string.book_title));

        if (mActionBarTitleWhenStop != null)
            switchActionBarTitle(mActionBarTitleWhenStop);

        mActionBar.setDisplayHomeAsUpEnabled(!mCurrentFragmentTag.equals("mainfragment"));

        startAudioService();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
//        Log.d(TAG, "onRestoreInstanceState");
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDatabase.writeToFile(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(MainActivity.CURRENT_FRAGMENT_TAG, mCurrentFragmentTag);
        outState.putString(MainActivity.PREVIOUS_TITLE, mActionBarTitleTextView.getText().toString());
        super.onSaveInstanceState(outState);
//        Log.d(TAG, "onSaveInstanceState");
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
            Intent intent = new Intent(this, SearchActivity.class);
            Bundle bundle = new Bundle();
            intent.putExtras(bundle);
            // the number "1" is to identify the action
            startActivityForResult(intent, REQUEST_CODE_DATABASE_UPDATE);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {

        // For ExamResultFragment, doing some specific handling
        // TODO: DEBUG
        Fragment fragment;
        if ((fragment = getFragmentManager().findFragmentByTag("examresultfragment")) != null) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.fragment_translate_slide_from_right_to_center, R.anim.fragment_translate_slide_from_center_to_right);
            transaction.remove(fragment);
            transaction.commit();
            super.onBackPressed();
        }

        // only when there's no dialog, a popup will occur
        if(mMainFragment.getDialog() == null){

            int backStackEntryCount = getFragmentManager().getBackStackEntryCount();

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
        intent.setAction(AudioService.ACTION_START_SERVICE);
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

    // because there may be different entry use goLessonFragment
    public void goLessonFragment(int bookIndex, int mode){

        mLessonsFragment = LessonsFragment.newInstance(mActionBarTitleTextView.getText().toString(), bookIndex, mDatabase, mode);
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fragment_translate_slide_from_right_to_center, R.anim.fragment_translate_slide_from_center_to_left, R.anim.fragment_translate_slide_from_left_to_center, R.anim.fragment_translate_slide_from_center_to_right);
        fragmentTransaction.add(R.id.activity_main_container, mLessonsFragment, "lessonsfragment");
        fragmentTransaction.addToBackStack(mCurrentFragmentTag);
        mCurrentFragmentTag = "lessonsfragment";
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

    public void goExamBooksFragment(){
        mExamBooksFragment = ExamBooksFragment.newInstance(mActionBarTitleTextView.getText().toString());
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fragment_translate_slide_from_right_to_center, R.anim.fragment_translate_slide_from_center_to_left, R.anim.fragment_translate_slide_from_left_to_center, R.anim.fragment_translate_slide_from_center_to_right);
        fragmentTransaction.add(R.id.activity_main_container, mExamBooksFragment, "exambooksfragment");
        fragmentTransaction.addToBackStack(mCurrentFragmentTag);
        mCurrentFragmentTag = "exambooksfragment";
        fragmentTransaction.commit();
    }

    public void goExamNoteFragment(){
        mExamNoteFragment = ExamNoteFragment.newInstance(mActionBarTitleTextView.getText().toString());
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fragment_translate_slide_from_right_to_center, R.anim.fragment_translate_slide_from_center_to_left, R.anim.fragment_translate_slide_from_left_to_center, R.anim.fragment_translate_slide_from_center_to_right);
        fragmentTransaction.add(R.id.activity_main_container, mExamNoteFragment, "examnotefragment");
        fragmentTransaction.addToBackStack(mCurrentFragmentTag);
        mCurrentFragmentTag = "examnotefragment";
        fragmentTransaction.commit();
    }

    public void goExamMainFragment(int bookIndex, int lessonIndex){
        mExamFragment = ExamFragment.newInstance(mActionBarTitleTextView.getText().toString(), bookIndex, lessonIndex);
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fragment_translate_slide_from_right_to_center, R.anim.fragment_translate_slide_from_center_to_left, R.anim.fragment_translate_slide_from_left_to_center, R.anim.fragment_translate_slide_from_center_to_right);
        fragmentTransaction.add(R.id.activity_main_container, mExamFragment, "exammainfragment");
        fragmentTransaction.addToBackStack(mCurrentFragmentTag);
        mCurrentFragmentTag = "exammainfragment";
        fragmentTransaction.commit();
    }

    public void goExamResultFragment(float ratio, int correctCount){
        mExamResultFragment = ExamResultFragment.newInstance(mActionBarTitleTextView.getText().toString(), ratio, correctCount);
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fragment_translate_slide_from_right_to_center, R.anim.fragment_translate_slide_from_center_to_left, R.anim.fragment_translate_slide_from_left_to_center, R.anim.fragment_translate_slide_from_center_to_right);
        fragmentTransaction.add(R.id.activity_main_container, mExamResultFragment, "examresultfragment");
        /** Here can not addToBackStack because the transaction is determined according to different situation*/
        mCurrentFragmentTag = "examresultfragment";
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
            case R.id.player_option_parent:
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
