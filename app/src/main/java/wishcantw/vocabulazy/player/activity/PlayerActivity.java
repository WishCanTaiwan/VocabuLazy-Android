package wishcantw.vocabulazy.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.mainmenu.activity.MainMenuActivity;
import wishcantw.vocabulazy.player.fragment.PlayerAddVocToNoteDialogFragment;
import wishcantw.vocabulazy.player.fragment.PlayerFragment;
import wishcantw.vocabulazy.player.fragment.PlayerNewNoteDialogFragment;
import wishcantw.vocabulazy.player.fragment.PlayerVocTooLessDialogFragment;
import wishcantw.vocabulazy.player.model.PlayerModel;
import wishcantw.vocabulazy.utility.Logger;

public class PlayerActivity extends AppCompatActivity implements PlayerFragment.OnPlayerLessonChangeListener,
                                                                 PlayerFragment.OnPlayerOptionFavoriteClickListener,
                                                                 PlayerAddVocToNoteDialogFragment.OnAddVocToNoteDialogFinishListener,
                                                                 PlayerNewNoteDialogFragment.OnNewNoteDialogFinishListener,
                                                                 PlayerVocTooLessDialogFragment.OnPlayerAlertDoneListener {
    private static final int VIEW_RES_ID = R.layout.activity_player;
    private static final int VIEW_MAIN_RES_ID = R.id.activity_player_container;

    public static final String TAG = "PlayerActivity";

    public static final String ARG_BOOK_INDEX = "arg-book-index";
    public static final String ARG_LESSON_INDEX = "arg-lesson-index";

    private boolean isLessonChanged = true;
    private PlayerModel mPlayerModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Logger.d(TAG, "Create");
        super.onCreate(savedInstanceState);

        // receive indices from intent
        int bookIndex = getIntent().getIntExtra(ARG_BOOK_INDEX, 1359);
        int lessonIndex = getIntent().getIntExtra(ARG_LESSON_INDEX, 1359);

        // create model for player
        mPlayerModel = PlayerModel.getInstance();
        mPlayerModel.init(getApplicationContext());

        isLessonChanged = (bookIndex != mPlayerModel.getBookIndex()) || (lessonIndex != mPlayerModel.getLessonIndex());
        mPlayerModel.setBookIndex(bookIndex);
        mPlayerModel.setLessonIndex(bookIndex);

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
//            mPlayerFragment.setBookAndLesson(mPlayerModel.getBookIndex(), mPlayerModel.getLessonIndex());

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

    public PlayerModel getPlayerModel() {
        if (mPlayerModel == null) {
            mPlayerModel = PlayerModel.getInstance();
            mPlayerModel.init(PlayerActivity.this);
        }
        return mPlayerModel;
    }

    public boolean isLessonChanged() {
        return isLessonChanged;
    }

    private void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
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
