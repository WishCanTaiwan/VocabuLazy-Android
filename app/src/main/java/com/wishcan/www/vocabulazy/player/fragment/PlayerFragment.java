package com.wishcan.www.vocabulazy.player.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.application.GlobalVariable;
import com.wishcan.www.vocabulazy.ga.GAPlayerFragment;
import com.wishcan.www.vocabulazy.player.activity.PlayerActivity;
import com.wishcan.www.vocabulazy.player.model.PlayerModel;
import com.wishcan.www.vocabulazy.player.view.PlayerView;
import com.wishcan.www.vocabulazy.service.AudioPlayer;
import com.wishcan.www.vocabulazy.service.AudioService;
import com.wishcan.www.vocabulazy.storage.databaseObjects.OptionSettings;
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

    private int argBookIndex;
    private int argLessonIndex;
    private int mBookIndex;
    private int mLessonIndex;
    private int mItemIndex;
    private int mSentenceIndex;
    private boolean mIsSameAsLastEntrance;
    private ArrayList<Vocabulary> mVocabularies;
    private PlayerView mPlayerView;
    private Context mContext;
    private PlayerModel mPlayerModel;

    private OnPlayerLessonChangeListener mOnPlayerLessonChangeListener;

    /**
     * receiver to get broadcasts from AudioService
     */
    private ServiceBroadcastReceiver mServiceBroadcastReceiver;

    /**
     * The flag for solving changing play list after onStop()
     */
    private boolean mIsWaitingAddNewPlayer = false;

    public static PlayerFragment newInstance() {
        PlayerFragment fragment = new PlayerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public PlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create broadcast receiver instance
        mServiceBroadcastReceiver = new ServiceBroadcastReceiver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /** set Scroll Listener, update Player's detail content every time the scroll stopped */
        /** move listener setting into onCreate() */
        if (mPlayerView == null) {
            mPlayerView = (PlayerView) inflater.inflate(R.layout.view_player, container, false);
            mPlayerView.setPlayerEventListener(this);
            mPlayerView.setPlayerOptionTabContent(null);
        }

        return mPlayerView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // get model instance from activity
        PlayerActivity activity = (PlayerActivity) mContext;
        mPlayerModel = activity.getModel();

        // set listener listening to data loading progress
        mPlayerModel.setDataProcessListener(this);

        // check whether the user is entering the same lesson as last time did
        mIsSameAsLastEntrance = checkIndicesMatch();

        // get the vocabularies according to the given book and lesson
        mPlayerModel.getVocabulariesIn(mBookIndex, mLessonIndex);
    }

    @Override
    public void onResume() {
        super.onResume();

        // request audio focus when the fragment is on
        requestAudioFocus();

        // setup options view
        setupOptions();

        // register broadcast receiver
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mServiceBroadcastReceiver, new IntentFilter(GlobalVariable.PLAYER_BROADCAST_INTENT));

        // move the focus to current playing item and set icon state
        mPlayerView.moveToPosition(mItemIndex);
        mPlayerView.setIconState(false, mPlayerModel.isPlaying(), false);

        // TODO: ask swallow
        if (mIsWaitingAddNewPlayer) {
            mPlayerModel.createPlayerContent(mPlayerModel.getCurrentContent());
            mPlayerModel.createPlayerDetailContent(mPlayerModel.getCurrentContent().get(mItemIndex));
            mIsWaitingAddNewPlayer = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        // unregister broadcast receiver
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mServiceBroadcastReceiver);
    }

    /**
     * Add a {@link OnPlayerLessonChangeListener} to {@link PlayerFragment}.
     * @param listener An instance of {@link OnPlayerLessonChangeListener}
     */
    public void addOnPlayerLessonChangeListener(OnPlayerLessonChangeListener listener) {
        mOnPlayerLessonChangeListener = listener;
    }

    /**
     * When entering {@link PlayerFragment}, we need to tell the fragment which book and lesson is
     * selected for creating corresponding views. By calling this method, we are able to assign the
     * book index and lesson index to the fragment.
     * @param bookIndex The index of the selected book.
     * @param lessonIndex The index of the selected lesson.
     */
    public void setBookAndLesson(int bookIndex, int lessonIndex) {
        argBookIndex = bookIndex;
        argLessonIndex = lessonIndex;
    }

    /**----------------- Implement PlayerModel.PlayerModelDataProcessListener -------------------**/
    /**
     * @link PlayerModel
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
        mPlayerModel.createPlayerDetailContent(vocabularies.get((mIsSameAsLastEntrance ? mItemIndex : 0)));

        if (vocabularies.size() > 0 && !mIsSameAsLastEntrance) {
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

        // if the order of Infinite3View has not changed, the player should remain the same
        if (!isOrderChanged) {
            startPlayingAt(mItemIndex, mSentenceIndex, AudioPlayer.SPELL);
            return;
        }

        // if the order of Infinite3View has changed
        if (isViewTouchedDown) { /** Horizontal Scroll by user **/

            // fetch old and new indices
            int bookIndex = mPlayerModel.getBookIndex();
            int numOfLesson = mPlayerModel.getNumOfLessons(bookIndex);
            int oldLessonIndex = mPlayerModel.getLessonIndex();
            int newLessonIndex = (oldLessonIndex + (direction == Infinite3View.MOVE_TO_RIGHT ? -1 : 1) + numOfLesson) % numOfLesson;

            // update indices
            updateIndices(bookIndex, newLessonIndex, 0, (mSentenceIndex < 0 ? -1 : 0));

            // get new content from database
            mPlayerModel.getVocabulariesIn(bookIndex, newLessonIndex);

            // remove old playerview
            mPlayerView.removeOldPlayer(direction == Infinite3View.MOVE_TO_RIGHT ? Infinite3View.RIGHT_VIEW_INDEX : Infinite3View.LEFT_VIEW_INDEX);

            // notify that the lesson has been changed
            mOnPlayerLessonChangeListener.onLessonChange(newLessonIndex);

        } else { /** Horizontal Scroll by audio service */

            // since the event triggered by service, therefore new content will be fetched from service,
            // therefore we only need to get content from model.
            ArrayList<Vocabulary> vocabularies = mPlayerModel.getCurrentContent();

            // create player views based on contents
            mPlayerModel.createPlayerContent(vocabularies);

            // create detail view
            mPlayerModel.createPlayerDetailContent(vocabularies.get(0));

            // remove old playerview
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
    public void onPlayerOptionChanged(int optionID, int mode, View v) {
        super.onPlayerOptionChanged(optionID, mode, v);

        // TODO: (swallow) when switching option tab, this method should be called.

        /** Refresh option setting */
        mPlayerModel.updateOptionSettings(optionID, mode, v);

        // notify the service that option settings has changed
        optionChanged();
    }

    private boolean checkIndicesMatch() {
        GlobalVariable globalVariable = (GlobalVariable) ((Activity) mContext).getApplication();
        int restoredBookIndex = globalVariable.playerTextbookIndex;
        int restoredLessonIndex = globalVariable.playerLessonIndex;
        int restoredItemIndex = globalVariable.playerItemIndex;
        int restoredSentenceIndex = globalVariable.playerSentenceIndex;
        updateIndices(argBookIndex, argLessonIndex, restoredItemIndex, restoredSentenceIndex);
        return (argBookIndex == restoredBookIndex && argLessonIndex == restoredLessonIndex);
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

    private void optionChanged() {
        Intent intent = new Intent(getActivity(), AudioService.class);
        intent.setAction(AudioService.OPTION_SETTINGS_CHANGED);
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

    /**
     *
     */
    protected class ServiceBroadcastReceiver extends BroadcastReceiver {

        private ServiceBroadcastReceiver() {}

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra(GlobalVariable.PLAYER_BROADCAST_ACTION);
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
