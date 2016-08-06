package com.wishcan.www.vocabulazy.main;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.VLApplication;
import com.wishcan.www.vocabulazy.ga.GAFragment;
import com.wishcan.www.vocabulazy.main.exam.fragment.ExamFragment;
import com.wishcan.www.vocabulazy.main.exam.fragment.ExamLessonFragment;
import com.wishcan.www.vocabulazy.main.exam.fragment.ExamResultFragment;
import com.wishcan.www.vocabulazy.main.voc.fragment.VocLessonFragment;
import com.wishcan.www.vocabulazy.search.SearchActivity;
import com.wishcan.www.vocabulazy.main.fragment.MainFragment;
import com.wishcan.www.vocabulazy.service.AudioService;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.Preferences;

import java.util.LinkedList;

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
            mMainFragment.addOnTabSelectListener(this);
            mMainFragment.setTitles(TITLES);
            mMainFragment.setFragments(mFragments);
            mFragmentManager = getSupportFragmentManager();
            mFragmentManager.addOnBackStackChangedListener(this);
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.add(VIEW_MAIN_RES_ID, mMainFragment, "MainFragment");
            fragmentTransaction.commit();
            mCurrentFragment = mMainFragment;
        }
        addFragmentsToBackStack(R.id.activity_main_container);
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
//            setActionBarTitle(mActionBarLL.getFirst());
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
//            if(bookIndex != 1359 && lessonIndex != 1359) {
//                Bundle args = new Bundle();
//                args.putInt(PlayerFragment.BOOK_INDEX_STR, bookIndex);
//                args.putInt(PlayerFragment.LESSON_INDEX_STR, lessonIndex);
//                goFragment(PlayerFragment.class, args, "PlayerFragment", "MainFragment");
//            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackStackChanged() {
        Log.d(TAG, "on back stack changed");
        String titleStr = mMainFragment.getCurrentTabTag();
        if (getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(mFragmentManager.getBackStackEntryCount() > 0);
        int entryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (entryCount > 0) {
            Log.d(TAG, "[" + getSupportFragmentManager().getBackStackEntryAt(entryCount-1) + "]");
            titleStr = getSupportFragmentManager().getBackStackEntryAt(entryCount-1).getName();
        }
        setActionBarTitle(titleStr);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.remove(mCurrentFragment);
//        transaction.commit();

        // update
//        mCurrentFragment = getSupportFragmentManager().

//        switchActionBarStr(FRAGMENT_FLOW.BACK, null);
    }

    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return super.onNavigateUp();
    }

    @Override
    public void onTabSelected(int position) {
        super.onTabSelected(position);
        setActionBarTitle(mMainFragment.getCurrentTabTagAt(position));
    }

    @Override
    public void onBookClicked(int position) {
        super.onBookClicked(position);
        mBookIndex = position;
        if (mVocLessonFragment == null) {
            mVocLessonFragment = VocLessonFragment.newInstance();
        }
        mVocLessonFragment.setBook(position);
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.show(mVocLessonFragment);
//        transaction.commit();

//        mCurrentFragment = mVocLessonFragment;
        String str = "Book " + (position+1);
        goFragment(mVocLessonFragment, null, "VocLessonFragment", str);
        setActionBarTitle(str);
    }

    @Override
    public void onLessonClicked(int position) {
        super.onLessonClicked(position);
        mLessonIndex = position;
        mPlayerFragment.setBookAndLesson(mBookIndex, mLessonIndex);
        String str = "Book " + (mBookIndex+1) + " Lesson " + (mLessonIndex+1);
        goFragment(mPlayerFragment, null, "PlayerFragment", str);
        setActionBarTitle(str);
    }

    @Override
    public void onNoteClicked(int position) {
        super.onNoteClicked(position);
        mBookIndex = -1;
        mLessonIndex = position;
        mPlayerFragment.setBookAndLesson(mBookIndex, mLessonIndex);
        String str = "Note " + (mLessonIndex+1);
        goFragment(mPlayerFragment, null, "PlayerFragment", str);
        setActionBarTitle(str);
    }

    @Override
    public void onExamIndexBookClicked() {
        super.onExamIndexBookClicked();
//        mCurrentFragment = mExamBookFragment;
        String str = getString(R.string.fragment_exam_book_title);
        goFragment(mExamBookFragment, null, "ExamBookFragment", str);
        setActionBarTitle(str);
    }

    @Override
    public void onExamIndexNoteClicked() {
        super.onExamIndexNoteClicked();
//        mCurrentFragment = mExamNoteFragment;
        String str = getString(R.string.fragment_exam_note_title);
        goFragment(mExamNoteFragment, null, "ExamNoteFragment", str);
        setActionBarTitle(str);
    }

    @Override
    public void onExamBookClicked(int position) {
        super.onExamBookClicked(position);
        mExamBookIndex = position;
        if (mExamLessonFragment == null) {
            mExamLessonFragment = ExamLessonFragment.newInstance();
        }
        mExamLessonFragment.setExamBook(position);

//        mCurrentFragment = mExamLessonFragment;
        String str = "Test Book " + (position+1);
        goFragment(mExamLessonFragment, null, "ExamLessonFragment", str);
        setActionBarTitle(str);
    }

    @Override
    public void onExamLessonClicked(int position) {
        super.onExamLessonClicked(position);
        mExamLessonIndex = position;
        if (mExamFragment == null) {
            mExamFragment = ExamFragment.newInstance();
        }
        mExamFragment.setExam(mExamBookIndex, mExamLessonIndex);

//        mCurrentFragment = mExamFragment;
        String str = "Test Book " + (mBookIndex+1) + " Lesson " + (position+1);
        goFragment(mExamFragment, null, "ExamFragment", str);
        setActionBarTitle(str);
    }

    @Override
    public void onExamNoteClicked(int position) {
        super.onExamNoteClicked(position);
        mExamBookIndex = -1;
        mExamLessonIndex = position;
        if (mExamFragment == null) {
            mExamFragment = ExamFragment.newInstance();
        }
        mExamFragment.setExam(mExamBookIndex, mExamLessonIndex);

//        mCurrentFragment = mExamFragment;
        String str = "Test Note " + (position+1);
        goFragment(mExamFragment, null, "ExamFragment", str);
        setActionBarTitle(str);
    }

    @Override
    public void onExamCompleted(float correctRatio, int correctCount) {
        super.onExamCompleted(correctRatio, correctCount);
        if (mExamResultFragment == null) {
            mExamResultFragment = ExamResultFragment.newInstance();
        }
        mExamResultFragment.setResult(correctRatio, correctCount);

//        mCurrentFragment = mExamResultFragment;
        String str = "Exam Completed";
        goFragment(mExamResultFragment, null, "ExamResultFragment", str);
        setActionBarTitle(str);
    }

    @Override
    public void onExamTryAgain() {
        super.onExamTryAgain();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(MainActivity.ANIM_ENTER_RES_ID, MainActivity.ANIM_EXIT_RES_ID);
        transaction.remove(mExamResultFragment);
        transaction.commit();

//        mCurrentFragment = mVocLessonFragment;
        mExamFragment.restartExam();
        setActionBarTitle(getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()-1).getName());
    }

    @Override
    public void onExamTryAnother() {
        super.onExamTryAnother();
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().popBackStack();
        setActionBarTitle(getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()-1).getName());
    }

    @Override
    public void onLessonChange(int lesson) {
        super.onLessonChange(lesson);
        mLessonIndex = lesson;
        setActionBarTitle("Book " + (mBookIndex+1) + " Lesson " + (mLessonIndex+1));
    }

    public void goFragment(Fragment fragment, Bundle args, String newTag, String backStackTag) {
        goFragment(fragment, args, newTag, backStackTag, FRAGMENT_ANIM.DEFAULT, FRAGMENT_ANIM.DEFAULT);
    }

    public void goFragment(Fragment fragment, Bundle args, String newTag, String backStackTag, FRAGMENT_ANIM animEnterResId, FRAGMENT_ANIM animExitResId) {
        int enterResId, exitResId;

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        enterResId = animEnterResId == FRAGMENT_ANIM.DEFAULT ? MainActivity.ANIM_ENTER_RES_ID : -1;
        exitResId = animExitResId == FRAGMENT_ANIM.DEFAULT ? MainActivity.ANIM_EXIT_RES_ID: -1;

        if (enterResId != -1 && exitResId != -1) {
            fragmentTransaction.setCustomAnimations(enterResId, exitResId, enterResId, exitResId);
        }
        if (newTag == null || newTag.equals(""))
            newTag = "newTag";
        fragmentTransaction.add(VIEW_MAIN_RES_ID, fragment, newTag);
        if (backStackTag != null && !backStackTag.equals(""))
            fragmentTransaction.addToBackStack(backStackTag);
        fragmentTransaction.commit();
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

//    public Fragment goFragment(Class<?> cls, Bundle bundle, String newTag, String backStackTag) {
//        return goFragment(cls, bundle, newTag, backStackTag, FRAGMENT_ANIM.DEFAULT, FRAGMENT_ANIM.DEFAULT);
//    }

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
                if (mActionBarLL.size() > 1) {
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
        item.setEnabled(enable);
//        if(enable) {
//            item.setVisible(true);
//        } else {
//            item.setVisible(false);
//        }
    }

    private void setCustomActionBar(){
        mActionBar = getActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM /*| ActionBar.DISPLAY_HOME_AS_UP*/);
            mActionBar.setCustomView(DEFAULT_CUSTOM_ACTION_BAR_RES_ID);
            mActionBarTitleTextView = (TextView) mActionBar.getCustomView().findViewById(TITLE_RES_ID);
            Typeface tf = Typeface.createFromAsset(getAssets(), FONT_RES_STR);
            mActionBarTitleTextView.setTypeface(tf);
        }
    }

    private void setActionBarTitle(String titleStr) {
        if (mActionBarTitleTextView == null) {
            mActionBar = getActionBar();
            if (mActionBar != null && mActionBar.getCustomView() != null)
                mActionBarTitleTextView = (TextView) mActionBar.getCustomView().findViewById(TITLE_RES_ID);
        }
        if (mActionBarTitleTextView != null)
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
