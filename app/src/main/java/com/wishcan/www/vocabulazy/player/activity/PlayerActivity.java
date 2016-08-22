package com.wishcan.www.vocabulazy.player.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.VLApplication;
import com.wishcan.www.vocabulazy.player.fragment.PlayerFragment;
import com.wishcan.www.vocabulazy.player.model.PlayerModel;

public class PlayerActivity extends AppCompatActivity implements PlayerFragment.OnPlayerLessonChangeListener {
    public static final String TAG = "PlayerActivity";

    public static final String ARG_BOOK_INDEX = "arg-book-index";
    public static final String ARG_LESSON_INDEX = "arg-lesson-index";

    private PlayerModel model;
    private int bookIndex;
    private int lessonIndex;

    private Toolbar mToolbar;
    private PlayerFragment mPlayerFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Create");
        super.onCreate(savedInstanceState);

        bookIndex = getIntent().getIntExtra(ARG_BOOK_INDEX, 1359);
        lessonIndex = getIntent().getIntExtra(ARG_LESSON_INDEX, 1359);

        setContentView(R.layout.activity_player);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setActionBarTitle("Book " + bookIndex + " Lesson " + lessonIndex);

        if (model == null) {
            model = new PlayerModel((VLApplication) getApplication());
        }

    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        mPlayerFragment = (PlayerFragment) fragment;
        Log.d(TAG, "YOOO");
        mPlayerFragment.addOnPlayerLessonChangeListener(this);
        Log.d(TAG, "HEYHEY");
        mPlayerFragment.setBookAndLesson(bookIndex, lessonIndex);
    }

    @Override
    public void onLessonChange(int lesson) {
        setActionBarTitle("Book " + bookIndex + " Lesson " + lesson);
        Log.d(TAG, "change to [Book " + bookIndex + " Lesson " + lesson + "]");
        lessonIndex = lesson;
    }

    private void setActionBarTitle(String title) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
//        mToolbar.setTitle(title);
    }
}
