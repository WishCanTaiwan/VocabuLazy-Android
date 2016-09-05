package com.wishcan.www.vocabulazy.player.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.player.fragment.PlayerFragment;
import com.wishcan.www.vocabulazy.player.model.PlayerModel;
import com.wishcan.www.vocabulazy.storage.Database;

public class PlayerActivity extends AppCompatActivity implements PlayerFragment.OnPlayerLessonChangeListener {
    public static final String TAG = "PlayerActivity";

    public static final String ARG_BOOK_INDEX = "arg-book-index";
    public static final String ARG_LESSON_INDEX = "arg-lesson-index";

    private int bookIndex;
    private int lessonIndex;

    private Database mDatabase;
    private PlayerModel mPlayerModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Create");
        super.onCreate(savedInstanceState);

        // receive indices from intent
        bookIndex = getIntent().getIntExtra(ARG_BOOK_INDEX, 1359);
        lessonIndex = getIntent().getIntExtra(ARG_LESSON_INDEX, 1359);

        // set content view
        setContentView(R.layout.activity_player);

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

        // while the fragment is attached, set up listener and pass the indices to fragment
        PlayerFragment playerFragment = (PlayerFragment) fragment;
        playerFragment.addOnPlayerLessonChangeListener(this);
        playerFragment.setBookAndLesson(bookIndex, lessonIndex);
    }

    @Override
    public void onLessonChange(int lesson) {
        setActionBarTitle(mDatabase.getLessonTitle(bookIndex, lesson));
        lessonIndex = lesson;
    }

    public PlayerModel getModel() {
        return mPlayerModel;
    }

    private void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}
