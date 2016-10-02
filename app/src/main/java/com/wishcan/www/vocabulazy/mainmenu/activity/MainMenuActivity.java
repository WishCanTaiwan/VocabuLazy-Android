package com.wishcan.www.vocabulazy.mainmenu.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.application.GlobalVariable;
import com.wishcan.www.vocabulazy.exam.activity.ExamActivity;
import com.wishcan.www.vocabulazy.mainmenu.fragment.MainMenuFragment;
import com.wishcan.www.vocabulazy.mainmenu.info.ReportPageFragment;
import com.wishcan.www.vocabulazy.mainmenu.info.TNCFragment;
import com.wishcan.www.vocabulazy.mainmenu.info.UserGuideFragment;
import com.wishcan.www.vocabulazy.mainmenu.info.WishCanIntroFragment;
import com.wishcan.www.vocabulazy.mainmenu.model.MainMenuModel;
import com.wishcan.www.vocabulazy.mainmenu.note.fragment.NoteCreateDialogFragment;
import com.wishcan.www.vocabulazy.mainmenu.note.fragment.NoteDeleteDialogFragment;
import com.wishcan.www.vocabulazy.mainmenu.note.fragment.NoteRenameDialogFragment;
import com.wishcan.www.vocabulazy.mainmenu.note.view.NoteDeleteDialogView;
import com.wishcan.www.vocabulazy.player.activity.PlayerActivity;
import com.wishcan.www.vocabulazy.search.activity.SearchActivity;
import com.wishcan.www.vocabulazy.service.AudioService;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.utility.Logger;

public class MainMenuActivity extends AppCompatActivity implements MainMenuFragment.OnMainMenuEventListener, NoteRenameDialogFragment.OnRenameCompleteListener, NoteDeleteDialogFragment.OnNoteDeleteListener, NoteCreateDialogFragment.OnNoteCreateListener {

    // TAG for debugging
    public static final String TAG = "MainMenuActivity";

    // Keys for intent bundle data
    public static final String KEY_IS_PLAYING = "is-playing";

    // request codes
    public static final int REQUEST_CODE_PLAYER_STATE = 0x1;

    private MainMenuFragment mMainMenuFragment;
    private MainMenuModel mMainMenuModel;

    // tag to record the player state
    private boolean isPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // setup title and title color
        setActionBar();

        if (mMainMenuModel == null) {
            mMainMenuModel = new MainMenuModel(getApplicationContext());
        }

        startAudioService();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        mMainMenuFragment = (MainMenuFragment) getSupportFragmentManager().findFragmentById(R.id.main_menu_fragment);
        mMainMenuFragment.addOnMainMenuEventListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Database.getInstance().writeToFile(getApplicationContext());
                return null;
            }
        }.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAudioService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        menu.getItem(1).setVisible(isPlaying);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:
                navigateToSearch();
                return true;
            case R.id.action_back_to_player:
                GlobalVariable globalVariable = (GlobalVariable) getApplication();
                navigateToPlayer(globalVariable.playerTextbookIndex, globalVariable.playerLessonIndex);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_PLAYER_STATE) {

            Logger.d(TAG, "result code " + resultCode);
            if (resultCode == RESULT_OK){

                // enable the express way returning to player
                enableBackToPlayerMenuItem(data.getBooleanExtra(KEY_IS_PLAYING, false));

                Logger.d(TAG, "result ok");
            }

        }
    }

    @Override
    public void onTextbookSelected(int bookIndex, int lessonIndex) {
        navigateToPlayer(bookIndex, lessonIndex);
    }

    @Override
    public void onNoteSelected(int noteIndex) {
        navigateToPlayer(-1, noteIndex);
    }

    @Override
    public void onNoteRename(int noteIndex, String originalName) {
        // add fragment to mainmenufragment
        NoteRenameDialogFragment fragment = new NoteRenameDialogFragment();
        fragment.addOnRenameCompleteListener(this);
        fragment.setNoteIndex(noteIndex);
        fragment.setOriginalString(originalName);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, fragment, "NoteRenameDialogFragment");
        transaction.addToBackStack("NoteRenameDialogFragment");
        transaction.commit();
    }

    @Override
    public void onRenameCompleted() {
        Log.d(TAG, "Rename completed");
        mMainMenuFragment.updateFragmentsContent();
        mMainMenuFragment.refreshFragments();
    }

    @Override
    public void onNoteDelete(int noteIndex, String noteTitle) {
        NoteDeleteDialogFragment fragment = new NoteDeleteDialogFragment();
        fragment.addOnNoteDeleteListener(this);
        fragment.setNoteIndex(noteIndex);
        fragment.setNoteTitle(noteTitle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, fragment, "NoteDeleteDialogFragment");
        transaction.addToBackStack("NoteDeleteDialogFragment");
        transaction.commit();
    }

    @Override
    public void onNoteDeleted() {
        Log.d(TAG, "Note deleted");
        mMainMenuFragment.updateFragmentsContent();
        mMainMenuFragment.refreshFragments();
    }

    @Override
    public void onNoteCreate() {
        NoteCreateDialogFragment fragment = new NoteCreateDialogFragment();
        fragment.addOnNoteCreateListener(this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, fragment, "NoteCreateDialogFragment");
        transaction.addToBackStack("NoteCreateDialogFragment");
        transaction.commit();
    }

    @Override
    public void onNoteCreated() {
        mMainMenuFragment.updateFragmentsContent();
        mMainMenuFragment.refreshFragments();
    }

    @Override
    public void onExamTextbookSelected(int examBookIndex, int examLessonIndex) {
        Intent intent = new Intent(MainMenuActivity.this, ExamActivity.class);
        intent.putExtra(ExamActivity.ARG_BOOK_INDEX, examBookIndex);
        intent.putExtra(ExamActivity.ARG_LESSON_INDEX, examLessonIndex);
        startActivity(intent);
    }

    @Override
    public void onExamNoteSelected(int examNoteIndex) {
        Intent intent = new Intent(MainMenuActivity.this, ExamActivity.class);
        intent.putExtra(ExamActivity.ARG_BOOK_INDEX, -1);
        intent.putExtra(ExamActivity.ARG_LESSON_INDEX, examNoteIndex);
        startActivity(intent);
    }

    public MainMenuModel getModel() {
        return mMainMenuModel;
    }

    public void setActionBar() {
        // find toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // set toolbar as action bar
        setSupportActionBar(toolbar);

        // set action bar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        }
    }

    public void navigateToGooglePlay() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    public void navigateToPlayer(int bookIndex, int lessonIndex) {
        // TODO: should use startActivityForResult, expecting return the status of player, playing or not?
        Intent intent = new Intent(MainMenuActivity.this, PlayerActivity.class);
        intent.putExtra(PlayerActivity.ARG_BOOK_INDEX, bookIndex);
        intent.putExtra(PlayerActivity.ARG_LESSON_INDEX, lessonIndex);
        startActivityForResult(intent, REQUEST_CODE_PLAYER_STATE);
    }

    public void navigateToSearch() {
        Intent intent = new Intent(MainMenuActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    public void displayReportPage() {
        ReportPageFragment fragment = new ReportPageFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, fragment, ReportPageFragment.TAG);
        transaction.addToBackStack(ReportPageFragment.TAG);
        transaction.commit();
    }

    public void displayUserGuide() {
        UserGuideFragment fragment = new UserGuideFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, fragment, UserGuideFragment.TAG);
        transaction.addToBackStack(UserGuideFragment.TAG);
        transaction.commit();
    }

    public void displayTNCPage() {
        TNCFragment fragment = new TNCFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, fragment, TNCFragment.TAG);
        transaction.addToBackStack(TNCFragment.TAG);
        transaction.commit();
    }

    public void displayIntroPage() {
        WishCanIntroFragment fragment = new WishCanIntroFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, fragment, WishCanIntroFragment.TAG);
        transaction.addToBackStack(WishCanIntroFragment.TAG);
        transaction.commit();
    }

    public void sendReport(String message) {

    }

    public void enableBackToPlayerMenuItem(boolean isPlaying) {

        // update playing status
        setIsPlaying(isPlaying);

        // will update the menu according to playing status
        invalidateOptionsMenu();
    }

    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
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
