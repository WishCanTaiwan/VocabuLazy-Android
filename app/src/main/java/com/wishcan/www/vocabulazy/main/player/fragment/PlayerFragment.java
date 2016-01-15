package com.wishcan.www.vocabulazy.main.player.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.main.MainActivity;
import com.wishcan.www.vocabulazy.main.player.view.PlayerView;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.Vocabulary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayerFragment extends Fragment {

    private static final String BOOK_INDEX_STR = "BOOK_INDEX_STR";
    private static final String LESSON_INDEX_STR = "LESSON_INDEX_STR";

    private Database mDatabase;
    private int mBookIndex;
    private int mLessonIndex;
    private PlayerView mPlayerView;

    public static PlayerFragment newInstance(int bookIndex, int lessonIndex) {
        PlayerFragment fragment = new PlayerFragment();
        Bundle args = new Bundle();
        args.putInt(BOOK_INDEX_STR, bookIndex);
        args.putInt(LESSON_INDEX_STR, lessonIndex);
        fragment.setArguments(args);
        return fragment;
    }

    public PlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = ((MainActivity) getActivity()).getDatabase();
        mBookIndex = getArguments() == null ? 0 : getArguments().getInt(BOOK_INDEX_STR);
        mLessonIndex = getArguments() == null ? 0 : getArguments().getInt(LESSON_INDEX_STR);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mPlayerView = new PlayerView(getContext());
        ArrayList<Integer> contentIDs = mDatabase.getContentIDs(mBookIndex, mLessonIndex);
        ArrayList<Vocabulary> vocabularies = mDatabase.getVocabulariesByIDs(contentIDs);
        final LinkedList<HashMap> playlistContent = new LinkedList<>();
        for(Vocabulary voc : vocabularies) {
            HashMap<String, String> hm = new HashMap<>();
            hm.put(PlayerView.PlayerScrollView.PLAYER_ITEM_CONTENT_FROM[0], voc.getSpell());
            hm.put(PlayerView.PlayerScrollView.PLAYER_ITEM_CONTENT_FROM[1], voc.getTranslationInOneString());
            playlistContent.add(hm);
        }

        mPlayerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPlayerView.addNewPlayer(playlistContent);
            }
        }, 600);

        return mPlayerView;
    }

    private class AddNewPlayerAsyncTask extends AsyncTask<ArrayList<Integer>, Void, LinkedList<HashMap>> {

        private PlayerView view;

        private int mPlayerPosition;

        public AddNewPlayerAsyncTask(PlayerView view, int position) {
            this.view = view;
            mPlayerPosition = position;
        }

        @Override
        protected LinkedList<HashMap> doInBackground(ArrayList<Integer>... params) {
            return null;
        }

        @Override
        protected void onPostExecute(LinkedList<HashMap> playlistContent) {
            super.onPostExecute(playlistContent);

        }
    }

}
