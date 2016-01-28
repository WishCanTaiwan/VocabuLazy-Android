package com.wishcan.www.vocabulazy.main.player.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.main.MainActivity;
import com.wishcan.www.vocabulazy.main.player.model.PlayerModel;
import com.wishcan.www.vocabulazy.main.player.view.PlayerMainView;
import com.wishcan.www.vocabulazy.main.player.view.PlayerOptionView;
import com.wishcan.www.vocabulazy.main.player.view.PlayerPanelView;
import com.wishcan.www.vocabulazy.main.player.view.PlayerView;
import com.wishcan.www.vocabulazy.service.AudioService;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.Option;
import com.wishcan.www.vocabulazy.storage.Vocabulary;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayerFragment extends Fragment {

    private static final String TAG = PlayerFragment.class.getSimpleName();
    private static final String BOOK_INDEX_STR = "BOOK_INDEX_STR";
    private static final String LESSON_INDEX_STR = "LESSON_INDEX_STR";

//    private Database mDatabase;
    private PlayerModel mPlayerModel;
    private int mBookIndex;
    private int mLessonIndex;
    private ArrayList<Vocabulary> mVocabularies;
    private PlayerMainView mPlayerMainView;
    private PlayerPanelView mPlayerPanelView;
    private PlayerOptionView mPlayerOptionView;
    private ViewGroup mPlayerOptionGrayBack;

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
        mPlayerModel = new PlayerModel((MainActivity) getActivity());
//        mDatabase = ((MainActivity) getActivity()).getDatabase();
        mBookIndex = getArguments() == null ? 0 : getArguments().getInt(BOOK_INDEX_STR);
        mLessonIndex = getArguments() == null ? 0 : getArguments().getInt(LESSON_INDEX_STR);
        mVocabularies = mPlayerModel.getVocabulariesIn(mBookIndex, mLessonIndex);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        final PlayerView playerView = new PlayerView(getActivity());
        mPlayerMainView = playerView.getPlayerMainView();
        mPlayerPanelView = playerView.getPlayerPanelView();
        mPlayerOptionGrayBack = playerView.getPlayerOptionGrayBack();
        mPlayerOptionView = playerView.getPlayerOptionView();

        /**Initialize and  Refresh PlayerView */
        mPlayerMainView.refreshPlayerDetail(mPlayerModel.createPlayerDetailContent(mVocabularies.get(0)));

        /** set Scroll Listener, update Player's detail content every time the scroll stopped */
        mPlayerMainView.setOnPlayerScrollStopListener(new PlayerMainView.OnPlayerScrollListener() {
            @Override
            public void onPlayerVerticalScrollStop(int currentPosition) {
                mPlayerMainView.refreshPlayerDetail(
                        mPlayerModel.createPlayerDetailContent(
                                mVocabularies.get(currentPosition)));
            }

            @Override
            public void onPlayerHorizontalScrollStop(int direction) {
                mLessonIndex += 1;
                mVocabularies = mPlayerModel.getVocabulariesIn(mBookIndex, mLessonIndex);
                mPlayerMainView.addNewPlayer(mPlayerModel.createPlayerContent(mVocabularies));
                mPlayerMainView.removeOldPlayer(direction == PlayerMainView.MOVE_TO_RIGHT ? PlayerMainView.RIGHT_VIEW_INDEX : PlayerMainView.LEFT_VIEW_INDEX);
            }

            @Override
            public void onPlayerHorizontalScrolling() {
            }
        });

        mPlayerPanelView.setOnPanelItemClickListener(new PlayerPanelView.OnPanelItemClickListener() {
            @Override
            public void onOptionFavoriteClick() {

            }

            @Override
            public void onOptionPlayClick() {
                optionPlayClicked();
            }

            @Override
            public void onOptionOptionClick() {
                playerView.showPlayerOptionView();
            }
        });

        mPlayerOptionGrayBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerView.exitOptionView();
            }
        });

        mPlayerOptionView.setOnOptionChangedListener(new PlayerOptionView.OnOptionChangedListener() {
            @Override
            public void onOptionChanged(View v, ArrayList<Option> optionLL, int currentMode) {
                mPlayerModel.setOptionAndMode(optionLL, currentMode);
                optionSettingChanged(optionLL.get(currentMode));
            }
        });
        mPlayerOptionView.setOptionsInTabContent(mPlayerModel.getDefaultOption());

        mPlayerMainView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPlayerMainView.addNewPlayer(mPlayerModel.createPlayerContent(mVocabularies));
            }
        }, 600);

        return playerView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");

        /**
         * start audioservice
         */
        setContent(mVocabularies);
        startPlayingAt(0);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    /**
     * testing method
     */
    void setContent(ArrayList<Vocabulary> vocabularies) {
        Intent intent = new Intent(getActivity(), AudioService.class);
        intent.setAction(AudioService.ACTION_SET_CONTENT);
        intent.putParcelableArrayListExtra(AudioService.KEY_PLAYER_CONTENT, vocabularies);
        getActivity().startService(intent);
    }

    void startPlayingAt(int index) {
        Intent intent = new Intent(getActivity(), AudioService.class);
        intent.setAction(AudioService.ACTION_START_PLAYING);
        intent.putExtra(AudioService.KEY_START_ITEM_INDEX, index);
        intent.putExtra(AudioService.KEY_PLAYING_FIELD, AudioService.PLAYING_SPELL);
        getActivity().startService(intent);
    }

    void optionPlayClicked() {
        Intent intent = new Intent(getActivity(), AudioService.class);
        intent.setAction(AudioService.ACTION_PLAY_BUTTON_CLICKED);
        getActivity().startService(intent);
    }

    void optionSettingChanged(Option option) {
        Intent intent = new Intent(getActivity(), AudioService.class);
        intent.setAction(AudioService.ACTION_PLAY_BUTTON_CLICKED);
        getActivity().startService(intent);
    }
}
