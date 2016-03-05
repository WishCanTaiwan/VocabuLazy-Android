package com.wishcan.www.vocabulazy.main.player.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
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
import com.wishcan.www.vocabulazy.service.ServiceBroadcaster;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.Option;
import com.wishcan.www.vocabulazy.storage.Vocabulary;
import com.wishcan.www.vocabulazy.widget.FragmentWithActionBarTitle;

import java.util.ArrayList;
import java.util.ServiceConfigurationError;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayerFragment extends Fragment implements FragmentWithActionBarTitle{

    private static final String TAG = PlayerFragment.class.getSimpleName();
    public static final String BOOK_INDEX_STR = "BOOK_INDEX_STR";
    public static final String LESSON_INDEX_STR = "LESSON_INDEX_STR";

    /**
     * broadcast manager
     */
    private LocalBroadcastManager wBroadcastManager;

//    private Database mDatabase;
    private PlayerModel mPlayerModel;
    private int mBookIndex;
    private int mLessonIndex;
    private ArrayList<Vocabulary> mVocabularies;
    private PlayerMainView mPlayerMainView;
    private PlayerPanelView mPlayerPanelView;
    private PlayerOptionView mPlayerOptionView;
    private ViewGroup mPlayerOptionGrayBack;

    /**
     * receiver to get broadcasts from AudioService
     */
    private ServiceBroadcastReceiver wServiceBroadcastReceiver;

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

        Log.d(TAG, "BookIndex: " + mBookIndex + ", LessonIndex: " + mLessonIndex);

        /**
         * register the broadcast receiver
         */
        wBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        wServiceBroadcastReceiver = new ServiceBroadcastReceiver();
        wBroadcastManager.registerReceiver(wServiceBroadcastReceiver, new IntentFilter(ServiceBroadcaster.BROADCAST_INTENT));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final PlayerView playerView = new PlayerView(getActivity());
        mPlayerMainView = playerView.getPlayerMainView();
        mPlayerPanelView = playerView.getPlayerPanelView();
        mPlayerOptionGrayBack = playerView.getPlayerOptionGrayBack();
        mPlayerOptionView = playerView.getPlayerOptionView();

        /**Initialize and  Refresh PlayerView */
        mPlayerMainView.refreshPlayerDetail(mPlayerModel.createPlayerDetailContent(null));

        /** set Scroll Listener, update Player's detail content every time the scroll stopped */
        mPlayerMainView.setOnPlayerScrollStopListener(new PlayerMainView.OnPlayerScrollListener() {
            @Override
            public void onPlayerVerticalScrollStop(int currentPosition, boolean isViewTouchedDown) {
                mPlayerMainView.refreshPlayerDetail(
                        mPlayerModel.createPlayerDetailContent(
                                mVocabularies.get(currentPosition)));

                if (isViewTouchedDown)
                    newItemFocused(currentPosition);
            }

            @Override
            public void onPlayerVerticalScrolling() {
                playerviewScrolling();
            }

            @Override
            public void onPlayerHorizontalScrollStop(int direction, boolean isViewTouchedDown) {
                int numOfLesson = mPlayerModel.getNumOfLessons(mBookIndex);
                mLessonIndex = (mLessonIndex + (direction == PlayerMainView.MOVE_TO_RIGHT ? -1 : 1) + numOfLesson) % numOfLesson;
                mVocabularies = mPlayerModel.getVocabulariesIn(mBookIndex, mLessonIndex);
                mPlayerMainView.refreshPlayerDetail(mPlayerModel.createPlayerDetailContent(null));
                mPlayerMainView.addNewPlayer(mPlayerModel.createPlayerContent(mVocabularies));
                mPlayerMainView.removeOldPlayer(direction == PlayerMainView.MOVE_TO_RIGHT ? PlayerMainView.RIGHT_VIEW_INDEX : PlayerMainView.LEFT_VIEW_INDEX);

                if (isViewTouchedDown)
                    newListFocused(mVocabularies);
            }

            @Override
            public void onPlayerHorizontalScrolling() {
                playerviewScrolling();
            }

            @Override
            public void onDetailScrollStop(int index, boolean isViewTouchedDown) {
//                Log.d(TAG, "onDetailScrollStop: " + index);
                if (isViewTouchedDown)
                    newSentenceFocused(index);
            }

            @Override
            public void onDetailScrolling() {
                playerviewScrolling();
            }

            @Override
            public void onViewTouchDown() {

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

        mPlayerOptionView.setOptionsInTabContent(null);

        mPlayerOptionView.setOnOptionChangedListener(new PlayerOptionView.OnOptionChangedListener() {
            @Override
            public void onOptionChanged(View v, ArrayList<Option> optionLL, int currentMode) {
//                Log.d(TAG, "option changed: random => " + optionLL.get(currentMode).mIsRandom + ", sentence => " + optionLL.get(currentMode).mSentence + ".");
                mPlayerModel.setOptionAndMode(optionLL, currentMode);
                optionSettingChanged(optionLL.get(currentMode));
            }
        });

        mPlayerMainView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPlayerMainView.addNewPlayer(mPlayerModel.createPlayerContent(mVocabularies));
            }
        }, 600);

        return playerView;
    }

    @Override
    public String getActionBarTitle() {
        String titleStr = "Book ";
        if(mBookIndex != -1 && mLessonIndex != -1)
            titleStr += mBookIndex + " Lesson " + mLessonIndex;
        return titleStr;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");

        /**
         * start audioservice
         */
        ArrayList<Vocabulary> vocabularies = mPlayerModel.getVocabulariesIn(mBookIndex, mLessonIndex);
        Option option = mPlayerModel.getCurrentOption();
        setContent(vocabularies, option);
        if (vocabularies.size() > 0)
            startPlayingAt(0, -1, AudioService.PLAYING_SPELL);

        /**
         * when database is ready
         */
        mPlayerOptionView.setOptionsInTabContent(mPlayerModel.getDefaultOptions());

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        /**
         * unregister when destoryed
         */
        wBroadcastManager.unregisterReceiver(wServiceBroadcastReceiver);
    }

    /**
     * messages sent to service
     */
    void setContent(ArrayList<Vocabulary> vocabularies, Option option) {
        Intent intent = new Intent(getActivity(), AudioService.class);
        intent.setAction(AudioService.ACTION_SET_CONTENT);
        intent.putParcelableArrayListExtra(AudioService.KEY_PLAYER_CONTENT, vocabularies);
        intent.putExtra(AudioService.KEY_OPTION_SETTING, option);
        getActivity().startService(intent);
    }

    void startPlayingAt(int itemIndex, int sentenceIndex, String playingField) {
        Intent intent = new Intent(getActivity(), AudioService.class);
        intent.setAction(AudioService.ACTION_START_PLAYING);
        intent.putExtra(AudioService.KEY_START_ITEM_INDEX, itemIndex);
        intent.putExtra(AudioService.KEY_START_SENTENCE_INDEX, sentenceIndex);
        intent.putExtra(AudioService.KEY_PLAYING_FIELD, playingField);
        getActivity().startService(intent);
    }

    void optionPlayClicked() {
        Intent intent = new Intent(getActivity(), AudioService.class);
        intent.setAction(AudioService.ACTION_PLAY_BUTTON_CLICKED);
        getActivity().startService(intent);
    }

    void optionSettingChanged(Option option) {
//        Log.d(TAG, "option changed: random => " + option.mIsRandom + ", sentence => " + option.mSentence + ".");
        Intent intent = new Intent(getActivity(), AudioService.class);
        intent.setAction(AudioService.ACTION_OPTION_SETTING_CHANGED);
        intent.putExtra(AudioService.KEY_OPTION_SETTING, option);
        getActivity().startService(intent);
    }

    void stopPlaying(int newItemIndex) {
        Intent intent = new Intent(getActivity(), AudioService.class);
        intent.setAction(AudioService.ACTION_STOP_PLAYING);
        intent.putExtra(AudioService.KEY_STOP_AT_ITEM_INDEX, newItemIndex);
        getActivity().startService(intent);
    }

    void newItemFocused(int newItemIndex) {
        Log.d(TAG, "newItemFocused: " + newItemIndex);
        startPlayingAt(newItemIndex, 0, AudioService.PLAYING_SPELL);
    }

    void newListFocused(ArrayList<Vocabulary> vocabularies) {
        Log.d(TAG, "newListFocused");
        Intent intent = new Intent(getActivity(), AudioService.class);
        intent.setAction(AudioService.ACTION_NEW_LIST);
        intent.putParcelableArrayListExtra(AudioService.KEY_NEW_CONTENT, vocabularies);
        getActivity().startService(intent);
    }

    void newSentenceFocused(int index) {
        Log.d(TAG, "newSentenceFocused: " + index);
        Intent intent = new Intent(getActivity(), AudioService.class);
        intent.setAction(AudioService.ACTION_NEW_SENTENCE_FOCUSED);
        intent.putExtra(AudioService.KEY_NEW_SENTENCE_INDEX, index);
        getActivity().startService(intent);
    }

    void playerviewScrolling() {
        Intent intent = new Intent(getActivity(), AudioService.class);
        intent.setAction(AudioService.ACTION_PLAYERVIEW_SCROLLING);
        getActivity().startService(intent);
    }

    /**
     * messages received from service
     */
    protected class ServiceBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra(ServiceBroadcaster.KEY_ACTION);
            Log.d(TAG, action);

            switch (action) {

                case ServiceBroadcaster.ACTION_ITEM_COMPLETE:
                    int newItemIndex = intent.getIntExtra(ServiceBroadcaster.KEY_NEXT_ITEM_INDEX, -1);
                    mPlayerMainView.moveToPosition(newItemIndex);
                    mPlayerMainView.refreshPlayerDetail(mPlayerModel.createPlayerDetailContent(mVocabularies.get(newItemIndex)));
                    break;

                case ServiceBroadcaster.ACTION_LIST_COMPLETE:
                    mPlayerMainView.setCurrentItem(1);
                    break;

                case ServiceBroadcaster.ACTION_SHOW_DETAIL:
                    mPlayerMainView.showDetail();
                    break;

                case ServiceBroadcaster.ACTION_HIDE_DETAIL:
                    mPlayerMainView.hideDetail();
                    break;

                case ServiceBroadcaster.ACTION_PLAY_SENTENCE:
                    int sentenceIndex = intent.getIntExtra(ServiceBroadcaster.KEY_PLAY_SENTENCE_INDEX, -1);
                    mPlayerMainView.moveToDetailPage(sentenceIndex);
                    break;

                default:
                    Log.d(TAG, "unexpected condition in onReceive: " + intent.toString());
                    break;
            }
        }
    }
}
