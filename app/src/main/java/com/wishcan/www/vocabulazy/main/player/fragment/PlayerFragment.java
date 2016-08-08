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

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.VLApplication;
import com.wishcan.www.vocabulazy.ga.GAPlayerFragment;
import com.wishcan.www.vocabulazy.main.MainActivity;
import com.wishcan.www.vocabulazy.main.player.model.PlayerModel;
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

    public interface OnPlayerLessonChangeListener {
        void onLessonChange(int lesson);
    }

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
    private int argBookIndex;
    private int argLessonIndex;
    private int mBookIndex;
    private int mLessonIndex;
    private int mItemIndex;
    private int mSentenceIndex;
    private boolean wIndicesMatch;
    private ArrayList<Vocabulary> mVocabularies;
    private PlayerView mPlayerView;

    private OnPlayerLessonChangeListener mOnPlayerLessonChangeListener;

    /**
     * receiver to get broadcasts from AudioService
     */
    private ServiceBroadcastReceiver mServiceBroadcastReceiver;

    /**
     * The flag for solving changing play list after onStop()
     * */
    private boolean mIsWaitingAddNewPlayer;

    public static PlayerFragment newInstance() {
        PlayerFragment fragment = new PlayerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public PlayerFragment() {
        // Required empty public constructor
    }

    public void setBookAndLesson(int bookIndex, int lessonIndex) {
        argBookIndex = bookIndex;
        argLessonIndex = lessonIndex;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Create");

        requestAudioFocus();

        if (mPlayerModel == null) {
            mPlayerModel = new PlayerModel((VLApplication) getActivity().getApplication());
            mPlayerModel.setDataProcessListener(this);
            mPlayerModel.getVocabulariesIn(mBookIndex, mLessonIndex);
        }
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
        wIndicesMatch = (argBookIndex == restoredBookIndex && argLessonIndex == restoredLessonIndex);
        updateIndices(argBookIndex, argLessonIndex, restoredItemIndex, restoredSentenceIndex);

        /* register broadcast receiver */
        mServiceBroadcastReceiver = new ServiceBroadcastReceiver();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mServiceBroadcastReceiver, new IntentFilter(Preferences.VL_BROADCAST_INTENT));

        mIsWaitingAddNewPlayer = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "Create View");

        /** set Scroll Listener, update Player's detail content every time the scroll stopped */
        /** move listener setting into onCreate() */
        if (mPlayerView == null) {
            mPlayerView = (PlayerView) inflater.inflate(R.layout.view_player, container, false);
            mPlayerView.setPlayerEventListener(this);
            mPlayerView.setIconState(false, mPlayerModel.isPlaying(), false);
            mPlayerView.setPlayerOptionTabContent(null);
        }

        return mPlayerView;
    }


    @Override
    public void onResume() {
        Log.d(TAG, "Resume");
        super.onResume();
        setupOptions();
        mPlayerView.moveToPosition(mItemIndex);
        if (mIsWaitingAddNewPlayer) {
            mPlayerModel.createPlayerContent(mPlayerModel.getCurrentContent());
            mPlayerModel.createPlayerDetailContent(mPlayerModel.getCurrentContent().get(mItemIndex));
            mIsWaitingAddNewPlayer = false;
        }
    }

    @Override
    public void onPause() {
        Log.d(TAG, "Pause");
        super.onPause();
        savePreferences();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        /* unregister broadcast receiver */
        Log.d(TAG, "unregister receiver");
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mServiceBroadcastReceiver);
    }

    public void addOnPlayerLessonChangeListener(OnPlayerLessonChangeListener listener) {
        mOnPlayerLessonChangeListener = listener;
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
        mPlayerView.setPlayerOptionTabContent(options);
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
     * @Link PlayerModel
     * Implement from PlayerModel
     */
    @Override
    public void onPlayerContentCreated(final LinkedList<HashMap> playerDataContent) {
        super.onPlayerContentCreated(playerDataContent);
        mPlayerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPlayerView.addNewPlayer(playerDataContent, mItemIndex);
            }
        }, 600);
    }

    @Override
    public void onDetailPlayerContentCreated(HashMap<String, Object> playerDetailDataContent) {
        super.onDetailPlayerContentCreated(playerDetailDataContent);
        mPlayerView.refreshPlayerDetail(playerDetailDataContent);
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

    /**----------------- Implement PlayerView.PlayerEventListener ------------------------**/
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

        if (isViewTouchedDown) { /** Horizontal Scroll by user */
            Log.d(TAG, "onPlayerHorizontalScrollStop isViewTouchedDown");
            int bookIndex = mPlayerModel.getBookIndex();
            int numOfLesson = mPlayerModel.getNumOfLessons(bookIndex);
            int oldLessonIndex = mPlayerModel.getLessonIndex();
            int newLessonIndex = (oldLessonIndex + (direction == Infinite3View.MOVE_TO_RIGHT ? -1 : 1) + numOfLesson) % numOfLesson;
            updateIndices(bookIndex, newLessonIndex, 0, (mSentenceIndex < 0 ? -1 : 0));
            mPlayerModel.getVocabulariesIn(bookIndex, newLessonIndex);
            mPlayerView.removeOldPlayer(direction == Infinite3View.MOVE_TO_RIGHT ? Infinite3View.RIGHT_VIEW_INDEX : Infinite3View.LEFT_VIEW_INDEX);
            mOnPlayerLessonChangeListener.onLessonChange(newLessonIndex);
        } else { /** Horizontal Scroll by audio service */
            Log.d(TAG, "onPlayerHorizontalScrollStop !isViewTouchedDown");
            ArrayList<Vocabulary> vocabularies = mPlayerModel.getCurrentContent();
            mPlayerModel.createPlayerContent(vocabularies);
            mPlayerModel.createPlayerDetailContent(vocabularies.get(0));
            mPlayerView.removeOldPlayer(direction == Infinite3View.MOVE_TO_RIGHT ? Infinite3View.RIGHT_VIEW_INDEX : Infinite3View.LEFT_VIEW_INDEX);
        }
    }

    @Override
    public void onPlayerHorizontalScrolling() {
        super.onPlayerHorizontalScrolling();
        playerViewScrolling();
    }

    @Override
    public void onPlayerDetailScrollStop(int index, boolean isViewTouchedDown) {
        super.onPlayerDetailScrollStop(index, isViewTouchedDown);
        updateIndices(mBookIndex, mLessonIndex, mItemIndex, index);
        if (isViewTouchedDown) {
            newSentenceFocused(index);
        }
    }

    @Override
    public void onPlayerDetailScrolling() {
        super.onPlayerDetailScrolling();
        playerViewScrolling();
    }
    
    @Override
    public void onPlayerInitialItemPrepared() {
        super.onPlayerInitialItemPrepared();
    }

    @Override
    public void onPlayerFinalItemPrepared() {
        super.onPlayerFinalItemPrepared();
    }
    
    @Override
    public void onPlayerPanelFavoriteClick() {
        super.onPlayerPanelFavoriteClick();
    }

    @Override
    public void onPlayerPanelPlayClick() {
        super.onPlayerPanelPlayClick();
        optionPlayClicked();
    }

    @Override
    public void onPlayerPanelOptionClick() {
        super.onPlayerPanelOptionClick();
        if (mPlayerView == null) {
            return;
        }
        mPlayerView.showPlayerOptionView();
    }
    
    @Override
    public void onPlayerOptionChanged(View v, ArrayList<OptionSettings> optionSettingsLL, int currentMode) {
        super.onPlayerOptionChanged(v, optionSettingsLL, currentMode);
        updateIndices(mBookIndex, mLessonIndex, mItemIndex, (optionSettingsLL.get(currentMode).isSentence() ? 0 : -1));
        optionSettingChanged(optionSettingsLL, currentMode);
    }
    
    @Override
    public void onGrayBackClick() {
        super.onGrayBackClick();
    }
    
    /**----------------------------- ServiceBroadcastReceiver -----------------------------------**/
    /**
     * messages received from service
     */
    protected class ServiceBroadcastReceiver extends BroadcastReceiver {

        private ServiceBroadcastReceiver() {}

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra(Preferences.VL_BROADCAST_ACTION);
            Log.d(TAG, "broadcast received, " + action);
            switch (action) {

                case AudioService.ITEM_COMPLETE:
                    break;

                case AudioService.TO_ITEM:
                    int newItemIndex = intent.getIntExtra(AudioService.ITEM_INDEX, -1);
                    Log.d(TAG, "to item " + newItemIndex);
                    updateIndices(mBookIndex, mLessonIndex, newItemIndex, (mSentenceIndex < 0 ? -1 : 0));
                    Log.d(TAG, "moveToPosition Start");
                    mPlayerView.moveToPosition(newItemIndex);
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
                        mPlayerView.moveToPlayer(Infinite3View.RIGHT_VIEW_INDEX);
                    } else {
                        mPlayerView.removeOldPlayer(Infinite3View.CENTER_VIEW_INDEX);
                        mIsWaitingAddNewPlayer = true;
                    }
                    mVocabularies = mPlayerModel.getCurrentContent();
                    mBookIndex = mPlayerModel.getBookIndex();
                    mLessonIndex = mPlayerModel.getLessonIndex();
                    break;

                case AudioService.SHOW_DETAIL:
                    mPlayerView.showDetail();
                    break;

                case AudioService.HIDE_DETAIL:
                    mPlayerView.hideDetail();
                    break;

                case AudioService.TO_SENTENCE:
                    int sentenceIndex = intent.getIntExtra(AudioService.SENTENCE_INDEX, -1);
                    updateIndices(mBookIndex, mLessonIndex, mItemIndex, sentenceIndex);
                    mPlayerView.moveDetailPage(sentenceIndex);
                    break;

                case AudioService.PLAYER_STATE_CHANGED:
                    String state = intent.getStringExtra(AudioService.PLAYER_STATE);
                    mPlayerView.setIconState(false, state.equals(AudioPlayer.PLAYING), false);
                    break;

                default:
                    Log.d(TAG, "unexpected condition in onReceive: " + intent.toString());
                    break;
            }
        }
    }
}
