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

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.wishcan.www.vocabulazy.VLApplication;
import com.wishcan.www.vocabulazy.ga.GAPlayerFragment;
import com.wishcan.www.vocabulazy.main.MainActivity;
import com.wishcan.www.vocabulazy.main.player.model.PlayerModel;
import com.wishcan.www.vocabulazy.main.player.view.PlayerMainView;
import com.wishcan.www.vocabulazy.main.player.view.PlayerOptionView;
import com.wishcan.www.vocabulazy.main.player.view.PlayerPanelView;
import com.wishcan.www.vocabulazy.main.player.view.PlayerView;
import com.wishcan.www.vocabulazy.service.AudioPlayer;
import com.wishcan.www.vocabulazy.service.AudioService;
import com.wishcan.www.vocabulazy.storage.databaseObjects.OptionSettings;
import com.wishcan.www.vocabulazy.storage.Preferences;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Vocabulary;
import com.wishcan.www.vocabulazy.widget.Infinite3View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


/**-------------------------------------- PlayerFragment ----------------------------------------**/
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayerFragment extends GAPlayerFragment {

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

    private PlayerModel mPlayerModel;
    private int mBookIndex;
    private int mLessonIndex;
    private int mItemIndex;
    private int mSentenceIndex;
    private boolean wIndicesMatch;
    private ArrayList<Vocabulary> mVocabularies;
    private PlayerView mPlayerView;
    private PlayerMainView mPlayerMainView;
    private PlayerPanelView mPlayerPanelView;
    private PlayerOptionView mPlayerOptionView;

    /**
     * receiver to get broadcasts from AudioService
     */
    private ServiceBroadcastReceiver wServiceBroadcastReceiver;

    /**
     * The tracker used to track the app's behaviors and send to Google Analytics.
     */
    private Tracker wTracker;

    /**
     * The flag for solving changing play list after onStop()
     * */
    private boolean mIsWaitingAddNewPlayer;

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
        Log.d(TAG, "onCreate");
        VLApplication application = (VLApplication) getActivity().getApplication();
        wTracker = application.getDefaultTracker();

        requestAudioFocus();

        mPlayerModel = new PlayerModel(application);
        mPlayerModel.setDataProcessListener(this);

        int restoredBookIndex = 1359;
        int restoredLessonIndex = 1359;
        int restoredItemIndex = 0;
        int restoredSentenceIndex = -1;
        int[] indices = loadPreferences();
        if (indices.length > 0) {
            restoredBookIndex = indices[0];
            restoredLessonIndex = indices[1];
            restoredItemIndex = indices[2];
            restoredSentenceIndex = indices[3];
        }
        int argBookIndex = getArguments() == null ? 0 : getArguments().getInt(BOOK_INDEX_STR);
        int argLessonIndex = getArguments() == null ? 0 : getArguments().getInt(LESSON_INDEX_STR);
        wIndicesMatch = (argBookIndex == restoredBookIndex && argLessonIndex == restoredLessonIndex);
        updateIndices(argBookIndex, argLessonIndex, restoredItemIndex, restoredSentenceIndex);

        ((MainActivity)getActivity()).switchActionBarStr(MainActivity.FRAGMENT_FLOW.GO, mPlayerModel.getLessonTitle(mBookIndex, mLessonIndex));
        ((MainActivity)getActivity()).enableExpressWay(false);

        /* register broadcast receiver */
        IntentFilter intentFilter = new IntentFilter(Preferences.VL_BROADCAST_INTENT);
        wServiceBroadcastReceiver = new ServiceBroadcastReceiver();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(wServiceBroadcastReceiver, intentFilter);
        mIsWaitingAddNewPlayer = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        mPlayerView = new PlayerView(getActivity());
        mPlayerMainView = mPlayerView.getPlayerMainView();
        mPlayerPanelView = mPlayerView.getPlayerPanelView();
        mPlayerOptionView = mPlayerView.getPlayerOptionView();

        mPlayerModel.getVocabulariesIn(mBookIndex, mLessonIndex);
        boolean isPlaying = mPlayerModel.isPlaying();

        /** set Scroll Listener, update Player's detail content every time the scroll stopped */
        /** move listener setting into onCreate() */
        mPlayerMainView.setOnPlayerScrollStopListener(this);
        mPlayerMainView.setOnPlayerItemPreparedListener(this);
        mPlayerPanelView.setIconState(false, isPlaying, false);
        mPlayerPanelView.setOnPanelItemClickListener(this);
        mPlayerView.setOnGrayBackClickListener(this);
        mPlayerOptionView.setOptionsInTabContent(null);
        mPlayerOptionView.setOnOptionChangedListener(this);

        return mPlayerView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        setupOptions();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        Log.d(TAG, "Setting screen name: " + TAG + " book " + mBookIndex + " unit " + mLessonIndex);
        wTracker.setScreenName(TAG + " book " + mBookIndex + " unit " + mLessonIndex);
        wTracker.send(new HitBuilders.ScreenViewBuilder().build());

        Log.d(TAG, "onResume at item " + mItemIndex);
        mPlayerMainView.moveToPosition(mItemIndex);
        ((MainActivity) getActivity()).enableExpressWay(false);
        if (mIsWaitingAddNewPlayer) {
            /**
             * TODO: Please add new player here
             * AudioService.TO_NEXT_LIST will destroy origin player and do nothing but raising the flag
             * if flag is raised, we should add new player during onResume(), fix the bug that after
             * turn off the screen and back to PlayerFragment causing PlayerList with no move
             */
            Log.d(TAG, "mIsWaitingAddNewPlayer");
            mPlayerModel.createPlayerContent(mPlayerModel.getCurrentContent());
            mPlayerModel.createPlayerDetailContent(mPlayerModel.getCurrentContent().get(mItemIndex));
            mIsWaitingAddNewPlayer = false;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        savePreferences();
        ((MainActivity) getActivity()).enableExpressWay(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        /* unregister broadcast receiver */
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(wServiceBroadcastReceiver);
    }

    private void savePreferences() {
        if (mPlayerModel == null) {
            sendException(getContext(), new NullPointerException(), true);
            return;
        }
        mPlayerModel.saveIndicesPreferences(new int[]{mBookIndex, mLessonIndex, mItemIndex, mSentenceIndex});
    }

    private int[] loadPreferences() {
        if (mPlayerModel == null) {
            sendException(getContext(), new NullPointerException(), true);
            return new int[]{1359, 1359, 0, -1};
        }
        return mPlayerModel.loadIndicesPreferences();
    }

    private void updateIndices(int bookIndex, int lessonIndex, int itemIndex, int sentenceIndex) {
        mBookIndex = bookIndex;
        mLessonIndex = lessonIndex;
        mItemIndex = itemIndex;
        mSentenceIndex = sentenceIndex;
        mPlayerModel.updateIndices(bookIndex, lessonIndex, itemIndex, sentenceIndex);
    }

    private void setupOptions() {
        ArrayList<OptionSettings> options = mPlayerModel.getOptionSettings();
        mPlayerOptionView.setOptionsInTabContent(options);
        updateIndices(mBookIndex, mLessonIndex, mItemIndex, (options.get(0).isSentence() ? 0 : -1));
    }

    /**------------------------------ message sent to service -----------------------------------**/
    /**
     * messages sent to service
     */
    private void requestAudioFocus() {
        Intent intent = new Intent(getActivity(), AudioService.class);
        intent.setAction(AudioService.GET_AUDIO_FOCUS);
        getActivity().startService(intent);
    }

    private void setContent(ArrayList<Vocabulary> vocabularies) {
        if (mPlayerModel == null) {
            sendException(getContext(), new NullPointerException(), true);
            return;
        }
        mPlayerModel.setCurrentContent(vocabularies);
        Intent intent = new Intent(getActivity(), AudioService.class);
        intent.setAction(AudioService.SET_CONTENT);
        getActivity().startService(intent);
    }

    private void startPlayingAt(int itemIndex, int sentenceIndex, String playingField) {
        Intent intent = new Intent(getActivity(), AudioService.class);
        intent.setAction(AudioService.START_PLAYING);
        intent.putExtra(AudioService.ITEM_INDEX, itemIndex);
        intent.putExtra(AudioService.SENTENCE_INDEX, sentenceIndex);
        intent.putExtra(AudioService.PLAYING_FIELD, playingField);
        getActivity().startService(intent);
    }

    private void optionPlayClicked() {
        Intent intent = new Intent(getActivity(), AudioService.class);
        intent.setAction(AudioService.PLAY_BUTTON_CLICKED);
        getActivity().startService(intent);
    }

    private void optionSettingChanged(ArrayList<OptionSettings> optionSettings, int optionMode) {
        if (mPlayerModel == null) {
            sendException(getContext(), new NullPointerException(), true);
            return;
        }
        mPlayerModel.setOptionSettingsAndMode(optionSettings, optionMode);
        Intent intent = new Intent(getActivity(), AudioService.class);
        intent.setAction(AudioService.OPTION_SETTINGS_CHANGED);
        getActivity().startService(intent);
    }

    private void newItemFocused(int newItemIndex) {
        Intent intent = new Intent(getActivity(), AudioService.class);
        intent.setAction(AudioService.NEW_ITEM_FOCUSED);
        intent.putExtra(AudioService.ITEM_INDEX, newItemIndex);
        intent.putExtra(AudioService.SENTENCE_INDEX, 0);
        intent.putExtra(AudioService.PLAYING_FIELD, AudioPlayer.SPELL);
        getActivity().startService(intent);
    }

    private void newSentenceFocused(int sentenceIndex) {
        Intent intent = new Intent(getActivity(), AudioService.class);
        intent.setAction(AudioService.NEW_SENTENCE_FOCUSED);
        intent.putExtra(AudioService.SENTENCE_INDEX, sentenceIndex);
        getActivity().startService(intent);
    }

    private void playerViewScrolling() {
        Intent intent = new Intent(getActivity(), AudioService.class);
        intent.setAction(AudioService.PLAYERVIEW_SCROLLING);
        getActivity().startService(intent);
    }

    private void startAudioTimer() {
        Intent intent = new Intent(getActivity(), AudioService.class);
        intent.setAction(AudioService.START_TIMER);
        getActivity().startService(intent);
    }

    /**----------------- Implement PlayerModel.PlayerModelDataProcessListener -------------------**/
    /**
     * Implement from PlayerModel
     * */
    @Override
    public void onPlayerContentCreated(final LinkedList<HashMap> playerDataContent) {
        super.onPlayerContentCreated(playerDataContent);
        mPlayerMainView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPlayerMainView.addNewPlayer(playerDataContent, mItemIndex);
            }
        }, 600);
    }

    @Override
    public void onDetailPlayerContentCreated(HashMap<String, Object> playerDetailDataContent) {
        super.onDetailPlayerContentCreated(playerDetailDataContent);
        mPlayerMainView.refreshPlayerDetail(playerDetailDataContent);
    }

    @Override
    public void onVocabulariesGet(ArrayList<Vocabulary> vocabularies) {
        super.onVocabulariesGet(vocabularies);
        mVocabularies = vocabularies;
        mPlayerModel.createPlayerContent(vocabularies);
        mPlayerModel.createPlayerDetailContent(vocabularies.get((wIndicesMatch ? mItemIndex : 0)));

        if (vocabularies.size() > 0 && !wIndicesMatch) {
            setContent(vocabularies);
            updateIndices(mBookIndex, mLessonIndex, 0, (mSentenceIndex < 0 ? -1 : 0));
            startPlayingAt(0, -1, AudioPlayer.SPELL);
            startAudioTimer();
        }
    }

    /**------------------ Implement PlayerMainView.OnPlayerItemPreparedListener -----------------**/
    @Override
    public void onInitialItemPrepared() {
        super.onInitialItemPrepared();
    }

    @Override
    public void onFinalItemPrepared() {
        super.onFinalItemPrepared();
    }

    /**----------------- Implement PlayerMainView.OnPlayerScrollListener ------------------------**/
    @Override
    public void onPlayerVerticalScrollStop(int currentPosition, boolean isViewTouchedDown) {
        super.onPlayerVerticalScrollStop(currentPosition, isViewTouchedDown);
        updateIndices(mBookIndex, mLessonIndex, currentPosition, (mSentenceIndex < 0 ? -1 : 0));
        if (isViewTouchedDown) {
            newItemFocused(currentPosition);
            mPlayerModel.createPlayerDetailContent(mVocabularies.get(currentPosition));
        }
    }

    @Override
    public void onPlayerVerticalScrolling() {
        super.onPlayerVerticalScrolling();
        playerViewScrolling();
    }

    @Override
    public void onPlayerHorizontalScrollStop(boolean isOrderChanged, int direction, boolean isViewTouchedDown) {
        super.onPlayerHorizontalScrollStop(isOrderChanged, direction, isViewTouchedDown);
        Log.d(TAG, "onPlayerHorizontalScrollStop");
        if (!isOrderChanged) {
            Log.d(TAG, "onPlayerHorizontalScrollStop !isOrderChanged");
            startPlayingAt(mItemIndex, mSentenceIndex, AudioPlayer.SPELL);
            return;
        }

        if (isViewTouchedDown) {
            Log.d(TAG, "onPlayerHorizontalScrollStop isViewTouchedDown");
            int bookIndex = mPlayerModel.getBookIndex();
            int numOfLesson = mPlayerModel.getNumOfLessons(bookIndex);
            int oldLessonIndex = mPlayerModel.getLessonIndex();
            int newLessonIndex = (oldLessonIndex + (direction == PlayerMainView.MOVE_TO_RIGHT ? -1 : 1) + numOfLesson) % numOfLesson;
            updateIndices(bookIndex, newLessonIndex, 0, (mSentenceIndex < 0 ? -1 : 0));
            mPlayerModel.getVocabulariesIn(bookIndex, newLessonIndex);
            mPlayerMainView.removeOldPlayer(direction == PlayerMainView.MOVE_TO_RIGHT ? PlayerMainView.RIGHT_VIEW_INDEX : PlayerMainView.LEFT_VIEW_INDEX);
        } else {
            Log.d(TAG, "onPlayerHorizontalScrollStop !isViewTouchedDown");
            ArrayList<Vocabulary> vocabularies = mPlayerModel.getCurrentContent();
            mPlayerModel.createPlayerContent(vocabularies);
            mPlayerModel.createPlayerDetailContent(vocabularies.get(0));
            mPlayerMainView.removeOldPlayer(direction == PlayerMainView.MOVE_TO_RIGHT ? PlayerMainView.RIGHT_VIEW_INDEX : PlayerMainView.LEFT_VIEW_INDEX);
        }
    }

    @Override
    public void onPlayerHorizontalScrolling() {
        super.onPlayerHorizontalScrolling();
        playerViewScrolling();
    }

    @Override
    public void onDetailScrollStop(int index, boolean isViewTouchedDown) {
        super.onDetailScrollStop(index, isViewTouchedDown);
        updateIndices(mBookIndex, mLessonIndex, mItemIndex, index);
        if (isViewTouchedDown) {
            newSentenceFocused(index);
        }
    }

    @Override
    public void onDetailScrolling() {
        super.onDetailScrolling();
        playerViewScrolling();
    }

    /**----------------- Implement PlayerPanelView.OnPanelItemClickListener ---------------------**/
    @Override
    public void onOptionFavoriteClick() {
        super.onOptionFavoriteClick();
    }

    @Override
    public void onOptionPlayClick() {
        super.onOptionPlayClick();
        optionPlayClicked();
    }

    @Override
    public void onOptionOptionClick() {
        super.onOptionOptionClick();
        if (mPlayerView == null) {
            return;
        }
        mPlayerView.showPlayerOptionView();
    }

    /**----------------- Implement PlayerOptionView.OnOptionChangedListener ---------------------**/
    @Override
    public void onOptionChanged(View v, ArrayList<OptionSettings> optionSettingsLL, int currentMode) {
        super.onOptionChanged(v, optionSettingsLL, currentMode);
        updateIndices(mBookIndex, mLessonIndex, mItemIndex, (optionSettingsLL.get(currentMode).isSentence() ? 0 : -1));
        optionSettingChanged(optionSettingsLL, currentMode);
    }

    /**-------------------- Implement PlayerView.OnGrayBackClickListener ------------------------**/
    @Override
    public void onGrayBackClick() {
        super.onGrayBackClick();
    }

    /**----------------------------- ServiceBroadcastReceiver -----------------------------------**/
    /**
     * messages received from service
     */
    public class ServiceBroadcastReceiver extends BroadcastReceiver {

        private ServiceBroadcastReceiver() {}

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra(Preferences.VL_BROADCAST_ACTION);

            switch (action) {

                case AudioService.ITEM_COMPLETE:
                    break;

                case AudioService.TO_ITEM:
                    int newItemIndex = intent.getIntExtra(AudioService.ITEM_INDEX, -1);
                    Log.d(TAG, "to item " + newItemIndex);
                    updateIndices(mBookIndex, mLessonIndex, newItemIndex, (mSentenceIndex < 0 ? -1 : 0));
                    Log.d(TAG, "moveToPosition Start");
                    mPlayerMainView.moveToPosition(newItemIndex);
                    if (mVocabularies != null) {
                        mPlayerModel.createPlayerDetailContent(mVocabularies.get(newItemIndex));
                    }
                    Log.d(TAG, "moveToPosition Done");
                    break;

                case AudioService.LIST_COMPLETE:
                    break;

                case AudioService.TO_NEXT_LIST:
                    /** To solved the bug that in onStop state addNewPlayer, but PlayerList didn't move. */
                    if (isResumed()) {
                        mPlayerMainView.setCurrentItem(Infinite3View.RIGHT_VIEW_INDEX);
                    } else {
                        mPlayerMainView.removeOldPlayer(Infinite3View.CENTER_VIEW_INDEX);
                        mIsWaitingAddNewPlayer = true;
                    }
                    mVocabularies = mPlayerModel.getCurrentContent();
                    mBookIndex = mPlayerModel.getBookIndex();
                    mLessonIndex = mPlayerModel.getLessonIndex();
                    break;

                case AudioService.SHOW_DETAIL:
                    mPlayerMainView.showDetail();
                    break;

                case AudioService.HIDE_DETAIL:
                    mPlayerMainView.hideDetail();
                    break;

                case AudioService.TO_SENTENCE:
                    int sentenceIndex = intent.getIntExtra(AudioService.SENTENCE_INDEX, -1);
                    updateIndices(mBookIndex, mLessonIndex, mItemIndex, sentenceIndex);
                    mPlayerMainView.moveToDetailPage(sentenceIndex);
                    break;

                case AudioService.PLAYER_STATE_CHANGED:
                    String state = intent.getStringExtra(AudioService.PLAYER_STATE);
                    mPlayerPanelView.setIconState(false, state.equals(AudioPlayer.PLAYING), false);
                    break;

                default:
                    Log.d(TAG, "unexpected condition in onReceive: " + intent.toString());
                    break;
            }
        }
    }
}
