package wishcantw.vocabulazy.activities.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.activities.ParentActivity;
import wishcantw.vocabulazy.activities.mainmenu.activity.MainMenuActivity;
import wishcantw.vocabulazy.activities.player.fragment.PlayerAddVocToNoteDialogFragment;
import wishcantw.vocabulazy.activities.player.fragment.PlayerFragment;
import wishcantw.vocabulazy.activities.player.fragment.PlayerNewNoteDialogFragment;
import wishcantw.vocabulazy.activities.player.fragment.PlayerVocTooLessDialogFragment;
import wishcantw.vocabulazy.activities.player.model.PlayerModel;
import wishcantw.vocabulazy.utility.Logger;

public class PlayerActivity extends ParentActivity implements PlayerFragment.OnPlayerLessonChangeListener,
                                                                 PlayerFragment.OnPlayerOptionFavoriteClickListener,
                                                                 PlayerAddVocToNoteDialogFragment.OnAddVocToNoteDialogFinishListener,
                                                                 PlayerNewNoteDialogFragment.OnNewNoteDialogFinishListener,
                                                                 PlayerVocTooLessDialogFragment.OnPlayerAlertDoneListener {
    // layout ids
    private static final int VIEW_RES_ID = R.layout.activity_player;
    private static final int VIEW_MAIN_RES_ID = R.id.activity_player_container;

    // tag for debugging
    public static final String TAG = "PlayerActivity";

    // args for bundle
    public static final String ARG_BOOK_INDEX = "arg-book-index";
    public static final String ARG_LESSON_INDEX = "arg-lesson-index";

    // tag for lesson change
    private boolean isLessonChanged = true;

    // player model
    private PlayerModel mPlayerModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // receive indices from intent
        int bookIndex = getIntent().getIntExtra(ARG_BOOK_INDEX, 1359);
        int lessonIndex = getIntent().getIntExtra(ARG_LESSON_INDEX, 1359);

        // create model for player
        mPlayerModel = PlayerModel.getInstance();
        mPlayerModel.init();

        // check lesson changed
        isLessonChanged = (bookIndex != mPlayerModel.getBookIndex()) || (lessonIndex != mPlayerModel.getLessonIndex());

        // set indices to player model
        mPlayerModel.setBookIndex(bookIndex);
        mPlayerModel.setLessonIndex(lessonIndex);

        // set content view
        setContentView(VIEW_RES_ID);

        // set up tool bar as support actionbar, and set up title
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        setActionBarTitle(mPlayerModel.getTitle(PlayerActivity.this, bookIndex, lessonIndex));
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if (fragment instanceof PlayerFragment) {
            PlayerFragment mPlayerFragment = (PlayerFragment) fragment;
            mPlayerFragment.addOnPlayerLessonChangeListener(this);
            mPlayerFragment.setOnPlayerOptionFavoriteClickListener(this);

        } else if (fragment instanceof PlayerAddVocToNoteDialogFragment) {
            PlayerAddVocToNoteDialogFragment mPlayerAddVocToNoteDialogFragment = (PlayerAddVocToNoteDialogFragment) fragment;
            mPlayerAddVocToNoteDialogFragment.setOnAddVocToNoteDialogFinishListener(this);

        } else if (fragment instanceof PlayerNewNoteDialogFragment) {
            PlayerNewNoteDialogFragment mPlayerNewNoteDialogFragment = (PlayerNewNoteDialogFragment) fragment;
            mPlayerNewNoteDialogFragment.setOnNewNoteDialogFinishListener(this);

        } else if (fragment instanceof PlayerVocTooLessDialogFragment) {
            PlayerVocTooLessDialogFragment mPlayerVocTooLessDialogFragment = (PlayerVocTooLessDialogFragment) fragment;
            mPlayerVocTooLessDialogFragment.setOnExamAlertDoneListener(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // check the amount of vocabularies
        checkContentAmount(mPlayerModel.getBookIndex(), mPlayerModel.getLessonIndex());
    }

    @Override
    public void onBackPressed() {
        // set result and pass back the player state
        setPlayerStateResult();

        super.onBackPressed();
    }

    /**
     * Get the player model instance
     *
     * @return player model instance
     */
    public PlayerModel getPlayerModel() {
        if (mPlayerModel == null) {
            mPlayerModel = PlayerModel.getInstance();
            mPlayerModel.init();
        }
        return mPlayerModel;
    }

    /**
     * Get whether is the lesson playing changed from last time
     *
     * @return boolean tag for lesson change
     */
    public boolean isLessonChanged() {
        return isLessonChanged;
    }

    /**-- PlayerFragment callback --**/
    @Override
    public void onLessonChange(int lesson) {
        isLessonChanged = true;
        mPlayerModel.setLessonIndex(lesson);
        setActionBarTitle(mPlayerModel.getTitle(PlayerActivity.this, mPlayerModel.getBookIndex(), mPlayerModel.getLessonIndex()));
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

    /**-- PlayerVocTooLessDialogFragment callback --**/
    @Override
    public void onPlayerAlertDone() {
        Logger.d(TAG, "onPlayerAlertDone");
        finish();
    }

    private void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    private void setPlayerStateResult() {
        Intent intent = new Intent(PlayerActivity.this, MainMenuActivity.class);
        intent.putExtra(MainMenuActivity.KEY_IS_PLAYING, mPlayerModel.isPlaying());
        setResult(RESULT_OK, intent);
    }

    private void checkContentAmount(int bookIndex, int lessonIndex) {
        if (mPlayerModel.getContentAmount(bookIndex, lessonIndex) <= 0) {
            onVocToLessAlert();
        }
    }

    private void onVocToLessAlert() {
        Logger.d(TAG, "onVocToLessAlert");
        PlayerVocTooLessDialogFragment dialogFragment = new PlayerVocTooLessDialogFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(PlayerActivity.VIEW_MAIN_RES_ID, dialogFragment, "PlayerVocTooLessDialogFragment");
        fragmentTransaction.addToBackStack("PlayerVocTooLessDialogFragment");
        fragmentTransaction.commit();
    }
}