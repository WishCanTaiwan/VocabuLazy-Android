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
import wishcantw.vocabulazy.activities.player.fragment.PlayerPrankDialogFragment;
import wishcantw.vocabulazy.activities.player.fragment.PlayerVocTooLessDialogFragment;
import wishcantw.vocabulazy.activities.player.model.PlayerModel;
import wishcantw.vocabulazy.activities.player.fragment.PlayerOptionDialogFragment;
import wishcantw.vocabulazy.database.Database;
import wishcantw.vocabulazy.utility.Logger;

public class PlayerActivity extends ParentActivity implements PlayerFragment.OnPlayerLessonChangeListener,
                                                                 PlayerFragment.OnPlayerPanelClickListener,
                                                                 PlayerAddVocToNoteDialogFragment.OnAddVocToNoteDialogFinishListener,
                                                                 PlayerNewNoteDialogFragment.OnNewNoteDialogFinishListener,
                                                                 PlayerVocTooLessDialogFragment.OnPlayerAlertDoneListener,
                                                                 PlayerOptionDialogFragment.OnPlayPrankListener {
    // layout ids
    private static final int VIEW_RES_ID = R.layout.activity_player;
    private static final int VIEW_MAIN_RES_ID = R.id.player_fragment_container;

    // tag for debugging
    public static final String TAG = "PlayerActivity";

    // args for bundle
    public static final String ARG_BOOK_INDEX = "arg-book-index";
    public static final String ARG_LESSON_INDEX = "arg-lesson-index";

    // tag for lesson change
    private boolean isLessonChanged = true;

    // TODO: 2016/11/30 The variable is used as quick solution, need re-evaluation on the structure of fragments.
    // voc id to be add
    private int vocIdToBeAdded;

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
            PlayerFragment playerFragment = (PlayerFragment) fragment;
            playerFragment.addOnPlayerLessonChangeListener(this);
            playerFragment.setOnPlayerPanelClickListener(this);

        } else if (fragment instanceof PlayerAddVocToNoteDialogFragment) {
            PlayerAddVocToNoteDialogFragment playerAddVocToNoteDialogFragment = (PlayerAddVocToNoteDialogFragment) fragment;
            playerAddVocToNoteDialogFragment.setOnAddVocToNoteDialogFinishListener(this);

        } else if (fragment instanceof PlayerNewNoteDialogFragment) {
            PlayerNewNoteDialogFragment playerNewNoteDialogFragment = (PlayerNewNoteDialogFragment) fragment;
            playerNewNoteDialogFragment.setOnNewNoteDialogFinishListener(this);

        } else if (fragment instanceof PlayerVocTooLessDialogFragment) {
            PlayerVocTooLessDialogFragment playerVocTooLessDialogFragment = (PlayerVocTooLessDialogFragment) fragment;
            playerVocTooLessDialogFragment.setOnExamAlertDoneListener(this);

        } else if (fragment instanceof PlayerOptionDialogFragment) {
            PlayerOptionDialogFragment playerOptionDialogFragment = (PlayerOptionDialogFragment) fragment;
            playerOptionDialogFragment.setOnPlayPrankListener(this);
        } else if (fragment instanceof PlayerPrankDialogFragment) {
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

    @Override
    protected void onPause() {
        super.onPause();
        mPlayerModel.storeData(PlayerActivity.this);
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

    /**-------------------------------- PlayerFragment callback ---------------------------------**/
    /**
     * Get the vocabulary id which is being added to note
     *
     * @return the id of the vocabulary
     */
    public int getVocIdToBeAdded() {
        return vocIdToBeAdded;
    }

    @Override
    public void onLessonChange(int lesson) {
        isLessonChanged = true;
        mPlayerModel.setLessonIndex(lesson);
        setActionBarTitle(mPlayerModel.getTitle(PlayerActivity.this, mPlayerModel.getBookIndex(), mPlayerModel.getLessonIndex()));
    }

    @Override
    public void onFavoriteClick(int vocId) {
        Logger.d(TAG, "onNewNote");
        vocIdToBeAdded = vocId;
        PlayerAddVocToNoteDialogFragment dialogFragment = new PlayerAddVocToNoteDialogFragment();
//        dialogFragment.setSelectedVocId(vocId);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(PlayerActivity.VIEW_MAIN_RES_ID, dialogFragment, "PlayerAddVocToNoteDialogFragment");
        fragmentTransaction.addToBackStack("PlayerAddVocToNoteDialogFragment");
        fragmentTransaction.commit();
    }

    @Override
    public void onOptionClick() {
        PlayerOptionDialogFragment playerOptionDialogFragment = new PlayerOptionDialogFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(PlayerActivity.VIEW_MAIN_RES_ID, playerOptionDialogFragment, "PlayerOptionDialogFragment");
        fragmentTransaction.addToBackStack("PlayerOptionDialogFragment");
        fragmentTransaction.commit();
    }

    /**-------------------------- PlayerAddVocToNoteDialogFragment callback ---------------------**/
    @Override
    public void onNeedNewNote() {
        Logger.d(TAG, "onNeedNewNote");
        PlayerNewNoteDialogFragment dialogFragment = new PlayerNewNoteDialogFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(PlayerActivity.VIEW_MAIN_RES_ID, dialogFragment, "PlayerNewNoteDialogFragment");
        fragmentTransaction.addToBackStack("PlayerNewNoteDialogFragment");
        fragmentTransaction.commit();
    }

    /**-------------------------- PlayerNewNoteDialogFragment callback --------------------------**/
    @Override
    public void onNewNoteDone(String string) {
        Logger.d(TAG, "onNewNote" + string);
        PlayerAddVocToNoteDialogFragment dialogFragment = new PlayerAddVocToNoteDialogFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(PlayerActivity.VIEW_MAIN_RES_ID, dialogFragment, "PlayerAddVocToNoteDialogFragment");
        fragmentTransaction.addToBackStack("PlayerAddVocToNoteDialogFragment");
        fragmentTransaction.commit();
    }

    /**------------------------ PlayerVocTooLessDialogFragment callback -------------------------**/
    @Override
    public void onPlayerAlertDone() {
        Logger.d(TAG, "onPlayerAlertDone");
        finish();
    }

    /**-------------------------- PlayerOptionDialogFragment callback ---------------------------**/
    @Override
    public void onPlayPrank(int count) {
        PlayerPrankDialogFragment dialogFragment = new PlayerPrankDialogFragment();
        dialogFragment.setPrankTypeByCount(count);
        if (dialogFragment.getPrankType() != PlayerPrankDialogFragment.PRANK_TYPE_NORMAL) {
            return;
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(PlayerActivity.VIEW_MAIN_RES_ID, dialogFragment, "PlayerPrankDialogFragment");
        fragmentTransaction.addToBackStack("PlayerPrankDialogFragment");
        fragmentTransaction.commit();
    }

    /**------------------------------------ private function ------------------------------------**/
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
