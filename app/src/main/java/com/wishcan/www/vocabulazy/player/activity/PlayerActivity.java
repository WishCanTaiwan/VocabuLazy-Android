package com.wishcan.www.vocabulazy.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.mainmenu.activity.MainMenuActivity;
import com.wishcan.www.vocabulazy.player.fragment.PlayerAddVocToNoteDialogFragment;
import com.wishcan.www.vocabulazy.player.fragment.PlayerFragment;
import com.wishcan.www.vocabulazy.player.fragment.PlayerNewNoteDialogFragment;
import com.wishcan.www.vocabulazy.player.model.PlayerModel;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.utility.Logger;

public class PlayerActivity extends AppCompatActivity implements PlayerFragment.OnPlayerLessonChangeListener,
                                                                 PlayerFragment.OnPlayerOptionFavoriteClickListener,
                                                                 PlayerAddVocToNoteDialogFragment.OnAddVocToNoteDialogFinishListener,
                                                                 PlayerNewNoteDialogFragment.OnNewNoteDialogFinishListener {
    private static final int VIEW_RES_ID = R.layout.activity_player;
    private static final int VIEW_MAIN_RES_ID = R.id.activity_player_container;

    public static final String TAG = "PlayerActivity";

    public static final String ARG_BOOK_INDEX = "arg-book-index";
    public static final String ARG_LESSON_INDEX = "arg-lesson-index";

    private int bookIndex;
    private int lessonIndex;

    private boolean isPlaying;

    private Database mDatabase;
    private PlayerModel mPlayerModel;

    private PlayerFragment mPlayerFragment;
    private PlayerAddVocToNoteDialogFragment mPlayerAddVocToNoteDialogFragment;
    private PlayerNewNoteDialogFragment mPlayerNewNoteDialogFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Logger.d(TAG, "Create");
        super.onCreate(savedInstanceState);

        // receive indices from intent
        bookIndex = getIntent().getIntExtra(ARG_BOOK_INDEX, 1359);
        lessonIndex = getIntent().getIntExtra(ARG_LESSON_INDEX, 1359);

        // set content view
        setContentView(VIEW_RES_ID);

        // get Database instance
        mDatabase = Database.getInstance();

        // set up tool bar as support actionbar, and set up title
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setActionBarTitle(mDatabase.getLessonTitle(bookIndex, lessonIndex));

        // create model for player
        mPlayerModel = new PlayerModel(getApplicationContext());
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if (fragment instanceof PlayerFragment) {
            mPlayerFragment = (PlayerFragment) fragment;
            mPlayerFragment.addOnPlayerLessonChangeListener(this);
            mPlayerFragment.setOnPlayerOptionFavoriteClickListener(this);
            mPlayerFragment.setBookAndLesson(bookIndex, lessonIndex);
        } else if (fragment instanceof PlayerAddVocToNoteDialogFragment) {
            mPlayerAddVocToNoteDialogFragment = (PlayerAddVocToNoteDialogFragment) fragment;
            mPlayerAddVocToNoteDialogFragment.setOnAddVocToNoteDialogFinishListener(this);
        } else if (fragment instanceof PlayerNewNoteDialogFragment) {
            mPlayerNewNoteDialogFragment = (PlayerNewNoteDialogFragment) fragment;
            mPlayerNewNoteDialogFragment.setOnNewNoteDialogFinishListener(this);
        }
    }

    @Override
    public void onBackPressed() {
        // set result and pass back the player state
        setPlayerStateResult();

        super.onBackPressed();
    }

    public PlayerModel getModel() {
        return mPlayerModel;
    }

    private void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    /**-- PlayerFragment callback --**/
    @Override
    public void onLessonChange(int lesson) {
        setActionBarTitle(mDatabase.getLessonTitle(bookIndex, lesson));
        lessonIndex = lesson;
    }

    @Override
    public void onFavoriteClick(int vocId) {
        Logger.d(TAG, "onNewNote");
        PlayerAddVocToNoteDialogFragment dialogFragment = new PlayerAddVocToNoteDialogFragment();
        dialogFragment.setSelectedVocId(vocId);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(PlayerActivity.VIEW_MAIN_RES_ID, dialogFragment, "PlayerAddVocToNoteDialogFragment");
        fragmentTransaction.addToBackStack("PlayerAddVocToNoteDialogFragment");
        fragmentTransaction.commit();
    }

    /**-- PlayerAddVocToNoteDialogFragment callback --**/
    @Override
    public void onNeedNewNote() {
        Logger.d(TAG, "onNeedNewNote");
        PlayerNewNoteDialogFragment dialogFragment = new PlayerNewNoteDialogFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(PlayerActivity.VIEW_MAIN_RES_ID, dialogFragment, "PlayerNewNoteDialogFragment");
        fragmentTransaction.addToBackStack("PlayerNewNoteDialogFragment");
        fragmentTransaction.commit();
    }

    /**-- PlayerNewNoteDialogFragment callback --**/
    @Override
    public void onNewNoteDone(String string) {
        Logger.d(TAG, "onNewNote" + string);
        PlayerAddVocToNoteDialogFragment dialogFragment = new PlayerAddVocToNoteDialogFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(PlayerActivity.VIEW_MAIN_RES_ID, dialogFragment, "PlayerAddVocToNoteDialogFragment");
        fragmentTransaction.addToBackStack("PlayerAddVocToNoteDialogFragment");
        fragmentTransaction.commit();
    }

    private void setPlayerStateResult() {
        Intent intent = new Intent(PlayerActivity.this, MainMenuActivity.class);
        intent.putExtra(MainMenuActivity.KEY_IS_PLAYING, mPlayerModel.isPlaying());
        setResult(RESULT_OK, intent);
    }
}
