package com.wishcan.www.vocabulazy.main.player.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.wishcan.www.vocabulazy.VLApplication;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayerFragment extends Fragment implements PlayerModel.PlayerModelDataProcessListener, PlayerMainView.OnPlayerItemPreparedListener {

    private static final String TAG = PlayerFragment.class.getSimpleName();
    public static final String BOOK_INDEX_STR = "BOOK_INDEX_STR";
    public static final String LESSON_INDEX_STR = "LESSON_INDEX_STR";

    /**
     * KEYs for instance state bundle
     */
    public static final String KEY_BOOK_INDEX = "book-index";
    public static final String KEY_LESSON_INDEX = "lesson-index";
    public static final String KEY_ITEM_INDEX = "item-index";
    public static final String KEY_SENTENCE_INDEX = "sentence-index";

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

    private Tracker wTracker;

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

        VLApplication application = (VLApplication) getActivity().getApplication();
        wTracker = application.getDefaultTracker();

        requestAudioFocus();

        mPlayerModel = new PlayerModel((MainActivity) getActivity());
        mPlayerModel.setDataProcessListener(this);

        int restoredBookIndex = 1359;
        int restoredLessonIndex = 1359;
        int restoredItemIndex = 0;
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
        Log.d(TAG, "index match " + wIndicesMatch);
        updateIndices(argBookIndex, argLessonIndex, restoredItemIndex, restoredSentenceIndex);

        setLanguage(mBookIndex);

        ((MainActivity)getActivity()).switchActionBarStr(MainActivity.FRAGMENT_FLOW.GO, "Book " +mBookIndex+ " Lesson " +mLessonIndex);
        ((MainActivity)getActivity()).enableExpressWay(false);
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
                playerViewScrolling();
            }

            @Override
            public void onPlayerHorizontalScrollStop(int direction, boolean isViewTouchedDown) {
                int numOfLesson = mPlayerModel.getNumOfLessons(mBookIndex);
                mLessonIndex = (mLessonIndex + (direction == PlayerMainView.MOVE_TO_RIGHT ? -1 : 1) + numOfLesson) % numOfLesson;
                updateIndices(mBookIndex, mLessonIndex, 0, (mSentenceIndex < 0 ? -1 : 0));
                mPlayerModel.getVocabulariesIn(mBookIndex, mLessonIndex);
                mPlayerMainView.removeOldPlayer(direction == PlayerMainView.MOVE_TO_RIGHT ? PlayerMainView.RIGHT_VIEW_INDEX : PlayerMainView.LEFT_VIEW_INDEX);
//                if (isViewTouchedDown) {
//                    Log.d(TAG, "YOLOOOOOOOOOO");
//                    newListFocused(mVocabularies);
//                }
            }

            @Override
            public void onPlayerHorizontalScrolling() {
                playerViewScrolling();
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
                playerViewScrolling();
            }

            @Override
            public void onViewTouchDown() {

            }
        });

        mPlayerMainView.setOnPlayerItemPreparedListener(this);

        Log.d(TAG, "is player playing? " + mPlayerModel.isPlayerPlaying());
        mPlayerPanelView.setIconState(false, mPlayerModel.isPlayerPlaying(), false);
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
    public void onStart() {
        super.onStart();

        /**
         * when database is ready
         */
        setupOptions();
        initTTSEngine();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Setting screen name: " + TAG + " book " + mBookIndex + " unit " + mLessonIndex);
        wTracker.setScreenName(TAG + " book " + mBookIndex + " unit " + mLessonIndex);
        wTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void  onStop() {
        super.onStop();
        savePreferences();
        ((MainActivity) getActivity()).enableExpressWay(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /**
         * unregister when destoryed
         */
        wBroadcastManager.unregisterReceiver(wServiceBroadcastReceiver);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
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
        mPlayerModel.createPlayerDetailContent(mVocabularies.get(mItemIndex));

        Option option = mPlayerModel.getCurrentOption();
        setContent(mVocabularies, option);
        if (mVocabularies.size() > 0 && !wIndicesMatch) {
            updateIndices(mBookIndex, mLessonIndex, 0, (mSentenceIndex < 0 ? -1 : 0));
            startPlayingAt(0, -1, AudioService.PLAYING_SPELL);
//            mPlayerPanelView.setIconState(false, mPlayerModel.isPlayerPlaying(), false);
        }
    }

    /**
     * Implement PlayerMainView.OnPlayerItemPreparedListener
     * */
    @Override
    public void onInitialItemPrepared() {

    }

    /**
     * Implement PlayerMainView.OnPlayerItemPreparedListener
     * */
    @Override
    public void onFinalItemPrepared() {

    }

    private void savePreferences() {
        Log.d(TAG, "save indices book " + mBookIndex + " lesson " + mLessonIndex);
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
//        Log.d(TAG, "book " + bookIndex + ", lesson " + lessonIndex + ", item " + itemIndex + ", sentence " + sentenceIndex);
    }

    /**
     * messages sent to service
     */
    void requestAudioFocus() {
        Intent intent = new Intent(getActivity(), AudioService.class);
        intent.setAction(AudioService.ACTION_REQUEST_AUDIO_FOCUS);
        getActivity().startService(intent);
    }

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
//        mPlayerPanelView.setIconState(false, mPlayerModel.isPlayerPlaying(), false);
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

    void playerViewScrolling() {
        Intent intent = new Intent(getActivity(), AudioService.class);
        intent.setAction(AudioService.ACTION_PLAYERVIEW_SCROLLING);
        getActivity().startService(intent);
//        mPlayerPanelView.setIconState(false, mPlayerModel.isPlayerPlaying(), false);
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
                    if (mVocabularies != null) {
                        mPlayerModel.createPlayerDetailContent(mVocabularies.get(newItemIndex));
                    }
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

                case ServiceBroadcaster.ACTION_UPDATE_PLAYER_STATUS:
                    String status = intent.getStringExtra(ServiceBroadcaster.KEY_PLAYER_STATUS);
                    mPlayerModel.updatePlayerStatus(status);
                    mPlayerPanelView.setIconState(false, mPlayerModel.isPlayerPlaying(), false);
                    break;

                default:
                    Log.d(TAG, "unexpected condition in onReceive: " + intent.toString());
                    break;
            }
        }
    }
}
