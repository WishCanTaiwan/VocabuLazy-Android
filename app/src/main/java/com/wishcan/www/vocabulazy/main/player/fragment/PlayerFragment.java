package com.wishcan.www.vocabulazy.main.player.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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
import com.wishcan.www.vocabulazy.storage.Option;
import com.wishcan.www.vocabulazy.storage.Vocabulary;
import com.wishcan.www.vocabulazy.widget.FragmentWithActionBarTitle;
import com.wishcan.www.vocabulazy.widget.PopScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayerFragment extends Fragment implements FragmentWithActionBarTitle, PlayerModel.PlayerModelDataProcessListener, PlayerMainView.OnPlayerItemPreparedListener {

    private static final String TAG = PlayerFragment.class.getSimpleName();
    public static final String BOOK_INDEX_STR = "BOOK_INDEX_STR";
    public static final String LESSON_INDEX_STR = "LESSON_INDEX_STR";

    /**
     * KEYs for instance state bundle
     */
    private static final String KEY_BOOK_INDEX = "book-index";
    private static final String KEY_LESSON_INDEX = "lesson-index";
    private static final String KEY_ITEM_INDEX = "item-index";
    private static final String KEY_SENTENCE_INDEX = "sentence-index";

    /**
     * broadcast manager
     */
    private LocalBroadcastManager wBroadcastManager;

//    private Database mDatabase;
    private PlayerModel mPlayerModel;
    private int mBookIndex;
    private int mLessonIndex;
    private int mItemIndex;
    private int mSentenceIndex;
    private boolean wIndicesMatch;
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
        Log.d(TAG, "newInstance");
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

        Log.d(TAG, "onCreate");

        mPlayerModel = new PlayerModel((MainActivity) getActivity());
        mPlayerModel.setDataProcessListener(this);

        int restoredBookIndex = 1359;
        int restoredLessonIndex = 1359;
        int restoredItemIndex = -1;
        int restoredSentenceIndex = -1;
        Bundle bundle = loadPreferences();
        if (bundle != null) {
            restoredBookIndex = bundle.getInt(KEY_BOOK_INDEX);
            restoredLessonIndex = bundle.getInt(KEY_LESSON_INDEX);
            restoredItemIndex = bundle.getInt(KEY_ITEM_INDEX);
            restoredSentenceIndex = bundle.getInt(KEY_SENTENCE_INDEX);
        }

        int argBookIndex = getArguments() == null ? 0 : getArguments().getInt(BOOK_INDEX_STR);
        int argLessonIndex = getArguments() == null ? 0 : getArguments().getInt(LESSON_INDEX_STR);
        wIndicesMatch = (argBookIndex == restoredBookIndex && argLessonIndex == restoredLessonIndex);
        updateIndices(argBookIndex, argLessonIndex, restoredItemIndex, restoredSentenceIndex);

        setLanguage(mBookIndex);

        /**
         * register the broadcast receiver
         */
        wBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        wServiceBroadcastReceiver = new ServiceBroadcastReceiver();
        wBroadcastManager.registerReceiver(wServiceBroadcastReceiver, new IntentFilter(ServiceBroadcaster.BROADCAST_INTENT));

        /**
         * start audioservice
         */
//        startAudio();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final PlayerView playerView = new PlayerView(getActivity());
        mPlayerMainView = playerView.getPlayerMainView();
        mPlayerPanelView = playerView.getPlayerPanelView();
        mPlayerOptionGrayBack = playerView.getPlayerOptionGrayBack();
        mPlayerOptionView = playerView.getPlayerOptionView();

        mPlayerModel.getVocabulariesIn(mBookIndex, mLessonIndex);

        /** set Scroll Listener, update Player's detail content every time the scroll stopped */
        mPlayerMainView.setOnPlayerScrollStopListener(new PlayerMainView.OnPlayerScrollListener() {
            @Override
            public void onPlayerVerticalScrollStop(int currentPosition, boolean isViewTouchedDown) {
                updateIndices(mBookIndex, mLessonIndex, currentPosition, (mSentenceIndex < 0 ? -1 : 0));
                if (isViewTouchedDown) {
                    newItemFocused(currentPosition);
                    mPlayerModel.createPlayerDetailContent(mVocabularies.get(currentPosition));
                }
            }

            @Override
            public void onPlayerVerticalScrolling() {
                playerviewScrolling();
            }

            @Override
            public void onPlayerHorizontalScrollStop(int direction, boolean isViewTouchedDown) {
                int numOfLesson = mPlayerModel.getNumOfLessons(mBookIndex);
                mLessonIndex = (mLessonIndex + (direction == PlayerMainView.MOVE_TO_RIGHT ? -1 : 1) + numOfLesson) % numOfLesson;
                updateIndices(mBookIndex, mLessonIndex, 0, (mSentenceIndex < 0 ? -1 : 0));
                mPlayerModel.getVocabulariesIn(mBookIndex, mLessonIndex);
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
                updateIndices(mBookIndex, mLessonIndex, mItemIndex, index);
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

        mPlayerMainView.setOnPlayerItemPreparedListener(this);

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
                mPlayerModel.setOptionAndMode(optionLL, currentMode);
                updateIndices(mBookIndex, mLessonIndex, mItemIndex, (optionLL.get(0).mSentence ? 0 : -1));
                optionSettingChanged(optionLL.get(currentMode));
            }
        });

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
         * when database is ready
         */
        setupOptions();
        initTTSEngine();
    }

    @Override
    public void  onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        // TODO: recording the book, lesson and item index for future recreating.
        savePreferences();
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSavedInstanceState");
        outState.putInt(KEY_BOOK_INDEX, mBookIndex);
        outState.putInt(KEY_LESSON_INDEX, mLessonIndex);
        outState.putInt(KEY_ITEM_INDEX, mItemIndex);
        outState.putInt(KEY_SENTENCE_INDEX, mSentenceIndex);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPlayerContentCreated(final LinkedList<HashMap> playerDataContent) {
        mPlayerMainView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPlayerMainView.addNewPlayer(playerDataContent, mItemIndex);
            }
        }, 600);
    }

    @Override
    public void onDetailPlayerContentCreated(HashMap<String, Object> playerDetailDataContent) {
        mPlayerMainView.refreshPlayerDetail(playerDetailDataContent);
    }

    @Override
    public void onVocabulariesGet(ArrayList<Vocabulary> vocabularies) {
        mVocabularies = vocabularies;
        mPlayerModel.createPlayerContent(mVocabularies);
        mPlayerModel.createPlayerDetailContent(mVocabularies.get(0));

        Option option = mPlayerModel.getCurrentOption();
        setContent(mVocabularies, option);
        if (mVocabularies.size() > 0 && !wIndicesMatch) {
            updateIndices(mBookIndex, mLessonIndex, 0, (mSentenceIndex < 0 ? -1 : 0));
            startPlayingAt(0, -1, AudioService.PLAYING_SPELL);
        }
    }

    @Override
    public void onInitialItemPrepared() {

    }

    @Override
    public void onFinalItemPrepared() {

    }

    private void savePreferences() {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_BOOK_INDEX, mBookIndex);
        bundle.putInt(KEY_LESSON_INDEX, mLessonIndex);
        bundle.putInt(KEY_ITEM_INDEX, mItemIndex);
        bundle.putInt(KEY_SENTENCE_INDEX, mSentenceIndex);
        mPlayerModel.savePlayerInfo(bundle);
    }

    private Bundle loadPreferences() {
        return mPlayerModel.loadPlayerInfo();
    }

    private void setupOptions() {
        ArrayList<Option> options = mPlayerModel.getDefaultOptions();
        mPlayerOptionView.setOptionsInTabContent(options);
        updateIndices(mBookIndex, mLessonIndex, mItemIndex, (options.get(0).mSentence ? 0 : -1));
    }

    private void updateIndices(int bookIndex, int lessonIndex, int itemIndex, int sentenceIndex) {
        mBookIndex = bookIndex;
        mLessonIndex = lessonIndex;
        mItemIndex = itemIndex;
        mSentenceIndex = sentenceIndex;
        Log.d(TAG, "book " + bookIndex + ", lesson " + lessonIndex + ", item " + itemIndex + ", sentence " + sentenceIndex);
    }

    /**
     * messages sent to service
     */
    void initTTSEngine() {
        Intent intent = new Intent(getActivity(), AudioService.class);
        intent.setAction(AudioService.ACTION_INIT_TTS_ENGINE);
        getActivity().startService(intent);
    }

    void setLanguage(int bookIndex) {
        Intent intent = new Intent(getActivity(), AudioService.class);
        intent.setAction(AudioService.ACTION_SET_LANGUAGE);
        intent.putExtra(AudioService.KEY_LANGUAGE, bookIndex);
        getActivity().startService(intent);
    }

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
//        Log.d(TAG, "newItemFocused: " + newItemIndex);
        startPlayingAt(newItemIndex, 0, AudioService.PLAYING_SPELL);
    }

    void newListFocused(ArrayList<Vocabulary> vocabularies) {
//        Log.d(TAG, "newListFocused");
        Intent intent = new Intent(getActivity(), AudioService.class);
        intent.setAction(AudioService.ACTION_NEW_LIST);
        intent.putParcelableArrayListExtra(AudioService.KEY_NEW_CONTENT, vocabularies);
        getActivity().startService(intent);
    }

    void newSentenceFocused(int index) {
//        Log.d(TAG, "newSentenceFocused: " + index);
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
//            Log.d(TAG, action + ", " + context.toString());

            switch (action) {

                case ServiceBroadcaster.ACTION_ITEM_COMPLETE:
                    int newItemIndex = intent.getIntExtra(ServiceBroadcaster.KEY_NEXT_ITEM_INDEX, -1);
                    updateIndices(mBookIndex, mLessonIndex, newItemIndex, (mSentenceIndex < 0 ? -1 : 0));
                    mPlayerMainView.moveToPosition(newItemIndex);
                    mPlayerModel.createPlayerDetailContent(mVocabularies.get(newItemIndex));
                    break;

                case ServiceBroadcaster.ACTION_LIST_COMPLETE:
                    mPlayerMainView.setCurrentItem(1);
                    updateIndices(mBookIndex, mLessonIndex++%10, 0, (mSentenceIndex < 0 ? -1 : 0));
                    break;

                case ServiceBroadcaster.ACTION_SHOW_DETAIL:
                    mPlayerMainView.showDetail();
                    break;

                case ServiceBroadcaster.ACTION_HIDE_DETAIL:
                    mPlayerMainView.hideDetail();
                    break;

                case ServiceBroadcaster.ACTION_PLAY_SENTENCE:
                    int sentenceIndex = intent.getIntExtra(ServiceBroadcaster.KEY_PLAY_SENTENCE_INDEX, -1);
                    updateIndices(mBookIndex, mLessonIndex, mItemIndex, sentenceIndex);
                    mPlayerMainView.moveToDetailPage(sentenceIndex);
                    break;

                default:
                    Log.d(TAG, "unexpected condition in onReceive: " + intent.toString());
                    break;
            }
        }
    }
}
