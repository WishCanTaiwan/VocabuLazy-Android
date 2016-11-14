package wishcantw.vocabulazy.player.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.analytics.Analytics;
import wishcantw.vocabulazy.analytics.firebase.FirebaseManager;
import wishcantw.vocabulazy.application.GlobalVariable;
import wishcantw.vocabulazy.database.AppPreference;
import wishcantw.vocabulazy.ga.GABaseFragment;
import wishcantw.vocabulazy.player.activity.PlayerActivity;
import wishcantw.vocabulazy.player.model.PlayerModel;
import wishcantw.vocabulazy.player.view.PlayerView;
import wishcantw.vocabulazy.service.AudioPlayer;
import wishcantw.vocabulazy.service.AudioPlayerUtils;
import wishcantw.vocabulazy.service.AudioService;
import wishcantw.vocabulazy.database.object.OptionSettings;
import wishcantw.vocabulazy.database.object.Vocabulary;
import wishcantw.vocabulazy.utility.Logger;
import wishcantw.vocabulazy.widget.Infinite3View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


/**-------------------------------------- PlayerFragment ----------------------------------------**/
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayerFragment extends GABaseFragment implements PlayerView.PlayerEventListener,
        PlayerModel.PlayerModelDataProcessListener {

    // callback interface
    public interface OnPlayerLessonChangeListener {
        void onLessonChange(int lesson);
    }

    // callback interface
    public interface OnPlayerOptionFavoriteClickListener {
        void onFavoriteClick(int vocId);
    }

    // TAG for debugging
    private static final String TAG = PlayerFragment.class.getSimpleName();

    // flag for solving changing play list after onStop()
    private boolean mIsWaitingAddNewPlayer = false;

    // data model
    private PlayerModel mPlayerModel;

    // views
    private PlayerView mPlayerView;

    //
    private ArrayList<Vocabulary> mVocabularies;

    // listeners
    private OnPlayerLessonChangeListener mOnPlayerLessonChangeListener;
    private OnPlayerOptionFavoriteClickListener mOnPlayerOptionFavoriteClickListener;

    // broadcast receiver
    private ServiceBroadcastReceiver mServiceBroadcastReceiver;

    // factory method to instantiate PlayerFragment
    public static PlayerFragment newInstance() {
        PlayerFragment fragment = new PlayerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    // required empty constructor
    public PlayerFragment() {
        // Required empty public constructor
    }

    /** Life cycle **/

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
        mPlayerModel = ((PlayerActivity) getActivity()).getPlayerModel();

        // set listener listening to data loading progress
        mPlayerModel.setDataProcessListener(this);

        // check whether the user is entering the same lesson as last time did
//        mIsSameAsLastEntrance = checkIndicesMatch();

        // get the vocabularies according to the given book and lesson
        mPlayerModel.getVocabulariesIn(mPlayerModel.getBookIndex(), mPlayerModel.getLessonIndex());
    }

    @Override
    public void onResume() {
        super.onResume();

        // send GA screen event
        FirebaseManager.getInstance().sendScreenEvent(Analytics.ScreenName.PLAYER);

        // request audio focus when the fragment is on
        requestAudioFocus();

        // setup options view
        setupOptions();

        // register broadcast receiver
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mServiceBroadcastReceiver, new IntentFilter(GlobalVariable.PLAYER_BROADCAST_INTENT));

        if (mPlayerView == null) {
            return;
        }

        if (mPlayerModel == null) {
            mPlayerModel = ((PlayerActivity) getActivity()).getPlayerModel();
        }

        int itemIndex = mPlayerModel.getItemIndex();

        // move the focus to current playing item and set icon state
        mPlayerView.moveToPosition(itemIndex);
        mPlayerView.setIconState(false, mPlayerModel.isPlaying(), false);

        if (mIsWaitingAddNewPlayer) {
            mPlayerModel.createPlayerContent(mPlayerModel.getPlayerContent());
            if (!mPlayerModel.getPlayerContent().isEmpty()
                    && mPlayerModel.getPlayerContent().size() > itemIndex
                    && itemIndex > -1) {
                mPlayerModel.createPlayerDetailContent(mPlayerModel.getPlayerContent().get(itemIndex));
            }
            mIsWaitingAddNewPlayer = false;
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        // unregister broadcast receiver
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mServiceBroadcastReceiver);
    }

    /** Abstracts and Interfaces **/

    @Override
    protected String getGALabel() {
        return Analytics.ScreenName.PLAYER;
    }

    /**----------------- Implement PlayerModel.PlayerModelDataProcessListener -------------------**/
    /**
     * @link PlayerModel
     * Implement from PlayerModel
     */
    @Override
    public void onPlayerContentCreated(final LinkedList<HashMap> playerDataContent) {
        if (mPlayerView == null) {
            return;
        }
        mPlayerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPlayerView.addNewPlayer(playerDataContent, mPlayerModel.getItemIndex());
            }
        }, 600);
    }

    @Override
    public void onDetailPlayerContentCreated(HashMap<String, Object> playerDetailDataContent) {
        if (mPlayerView == null) {
            return;
        }
        mPlayerView.refreshPlayerDetail(playerDetailDataContent);
    }

    @Override
    public void onVocabulariesGet(ArrayList<Vocabulary> vocabularies) {
        mVocabularies = vocabularies;

        if ( vocabularies == null || vocabularies.isEmpty()) {
            return;
        }

        if (mPlayerModel == null) {
            mPlayerModel = ((PlayerActivity) getActivity()).getPlayerModel();
        }

        boolean isLessonChanged = ((PlayerActivity) getActivity()).isLessonChanged();
        int itemIndex = isLessonChanged ? 0 : mPlayerModel.getItemIndex();
        mPlayerModel.createPlayerContent(vocabularies);

        if (vocabularies.size() > itemIndex) {
            mPlayerModel.createPlayerDetailContent(vocabularies.get(itemIndex));
        }

        if (vocabularies.size() > 0 && isLessonChanged) {
            Logger.d(TAG, "set content and start playing");
            setContent(vocabularies);
//            updateIndices(mBookIndex, mLessonIndex, 0, (mSentenceIndex < 0 ? -1 : 0));
            startPlayingAt(0, AudioPlayerUtils.PlayerField.SPELL);
        }
    }

    @Override
    public void onGrayBackClick() {

    }

    /**----------------- Implement PlayerView.PlayerEventListener ------------------------**/
    @Override
    public void onPlayerVerticalScrollStop(int currentPosition, boolean isViewTouchedDown) {
//        updateIndices(mBookIndex, mLessonIndex, currentPosition, (mSentenceIndex < 0 ? -1 : 0));
        if (isViewTouchedDown) {
            newItemFocused(currentPosition);
            if (mPlayerModel != null
                    && mVocabularies != null
                    && mVocabularies.size() > currentPosition
                    && currentPosition > -1) {
                mPlayerModel.createPlayerDetailContent(mVocabularies.get(currentPosition));
            }
        }
    }

    @Override
    public void onPlayerVerticalScrolling() {
        playerViewScrolling();
    }

    @Override
    public void onPlayerHorizontalScrollStop(boolean isOrderChanged, int direction, boolean isViewTouchedDown) {

        // if the order of Infinite3View has not changed, the player should remain the same
        if (!isOrderChanged) {
            startPlayingAt(mPlayerModel.getItemIndex(), AudioPlayerUtils.PlayerField.SPELL);
            return;
        }

        if (mPlayerView == null || mOnPlayerLessonChangeListener == null) {
            return;
        }

        if (mPlayerModel == null) {
            mPlayerModel = ((PlayerActivity) getActivity()).getPlayerModel();
        }

        // if the order of Infinite3View has changed
        if (isViewTouchedDown) { /** Horizontal Scroll by user **/

            // fetch old and new indices
            int bookIndex = mPlayerModel.getBookIndex();
            int numOfLesson = mPlayerModel.getNumOfLessons(bookIndex);
            int oldLessonIndex = mPlayerModel.getLessonIndex();
            int newLessonIndex = (oldLessonIndex + (direction == Infinite3View.MOVE_TO_RIGHT ? -1 : 1) + numOfLesson) % numOfLesson;

            // update indices
//            updateIndices(bookIndex, newLessonIndex, 0, (mSentenceIndex < 0 ? -1 : 0));

            // get new content from database
            mPlayerModel.getVocabulariesIn(bookIndex, newLessonIndex);

            // remove old playerview
            mPlayerView.removeOldPlayer(direction == Infinite3View.MOVE_TO_RIGHT ? Infinite3View.RIGHT_VIEW_INDEX : Infinite3View.LEFT_VIEW_INDEX);

            // notify that the lesson has been changed
            mOnPlayerLessonChangeListener.onLessonChange(newLessonIndex);

        } else { /** Horizontal Scroll by audio service */

            // since the event triggered by service, therefore new content will be fetched from service,
            // therefore we only need to get content from model.
            ArrayList<Vocabulary> vocabularies = mPlayerModel.getPlayerContent();

            if (vocabularies == null || vocabularies.isEmpty()) {
                return;
            }

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
        playerViewScrolling();
    }

    @Override
    public void onPlayerDetailScrollStop(int index, boolean isViewTouchedDown) {
        if (isViewTouchedDown) {
            newSentenceFocused(index);
        }
    }

    @Override
    public void onPlayerDetailScrolling() {
        playerViewScrolling();
    }

    @Override
    public void onPlayerInitialItemPrepared() {

    }

    @Override
    public void onPlayerFinalItemPrepared() {

    }

    @Override
    public void onPlayerPanelFavoriteClick() {

        if (mVocabularies == null || mVocabularies.isEmpty()) {
            return;
        }

        int itemIndex = mPlayerModel. getItemIndex();

        if (mVocabularies.size() > itemIndex && itemIndex > -1) {
            int vocId = mVocabularies.get(itemIndex).getId();
            if (mOnPlayerOptionFavoriteClickListener != null) {
                mOnPlayerOptionFavoriteClickListener.onFavoriteClick(vocId);
            }
        }
    }

    @Override
    public void onPlayerPanelPlayClick() {
        optionPlayClicked();
    }

    @Override
    public void onPlayerPanelOptionClick() {

        // TODO: (swallow) please use the parameter mode to set option tab
        int mode = AppPreference.getInstance().getPlayerOptionMode();

        if (mPlayerView == null) {
            return;
        }
        mPlayerView.showPlayerOptionView();
    }

    @Override
    public void onPlayerOptionChanged(int optionID, int mode, View v, int leftOrRight) {

        if (mPlayerModel != null) {
            /** Refresh option setting */
            mPlayerModel.updateOptionSettings(optionID, mode, v, leftOrRight);
        }
        // notify the service that option settings has changed
        optionChanged();
    }

    /** Public methods **/

    /**
     * Add a {@link OnPlayerLessonChangeListener} to {@link PlayerFragment}.
     * @param listener An instance of {@link OnPlayerLessonChangeListener}
     */
    public void addOnPlayerLessonChangeListener(@Nullable OnPlayerLessonChangeListener listener) {
        mOnPlayerLessonChangeListener = listener;
    }

    public void setOnPlayerOptionFavoriteClickListener(OnPlayerOptionFavoriteClickListener listener) {
        mOnPlayerOptionFavoriteClickListener = listener;
    }

//    /**
//     * When entering {@link PlayerFragment}, we need to tell the fragment which book and lesson is
//     * selected for creating corresponding views. By calling this method, we are able to assign the
//     * book index and lesson index to the fragment.
//     * @param bookIndex The index of the selected book.
//     * @param lessonIndex The index of the selected lesson.
//     */
//    public void setBookAndLesson(int bookIndex, int lessonIndex) {
//        argBookIndex = bookIndex;
//        argLessonIndex = lessonIndex;
//    }

    /** Private methods **/

//    private boolean checkIndicesMatch() {
//        GlobalVariable globalVariable = (GlobalVariable) getActivity().getApplication();
//        int restoredBookIndex = globalVariable.playerTextbookIndex;
//        int restoredLessonIndex = globalVariable.playerLessonIndex;
//        int restoredItemIndex = globalVariable.playerItemIndex;
//        int restoredSentenceIndex = globalVariable.playerSentenceIndex;
//        updateIndices(argBookIndex, argLessonIndex, restoredItemIndex, restoredSentenceIndex);
//        return (argBookIndex == restoredBookIndex && argLessonIndex == restoredLessonIndex);
//    }

//    private void updateIndices(int bookIndex, int lessonIndex, int itemIndex, int sentenceIndex) {
//        mBookIndex = bookIndex;
//        mLessonIndex = lessonIndex;
//        mItemIndex = itemIndex;
//        mSentenceIndex = sentenceIndex;
//        mPlayerModel.updateIndices(bookIndex, lessonIndex, itemIndex, sentenceIndex);
//    }

    private void setupOptions() {

        if (mPlayerView == null) {
            return;
        }

        if (mPlayerModel == null) {
            mPlayerModel = ((PlayerActivity) getActivity()).getPlayerModel();
        }

        ArrayList<OptionSettings> options = mPlayerModel.getOptionSettings();
        mPlayerView.setPlayerOptionTabContent(options);

        int sentenceIndex = -1;
        if (options != null && !options.isEmpty()) {
            sentenceIndex = options.get(0).isSentence() ? 0 : -1;
        }
//        updateIndices(mBookIndex, mLessonIndex, mItemIndex, sentenceIndex);
    }

    private void requestAudioFocus() {
        Intent intent = new Intent(getActivity(), AudioService.class);
        intent.setAction(AudioService.GET_AUDIO_FOCUS);
        getActivity().startService(intent);
    }

    private void setContent(ArrayList<Vocabulary> vocabularies) {
        if (mPlayerModel == null) {
            mPlayerModel = ((PlayerActivity) getActivity()).getPlayerModel();
        }
        mPlayerModel.setPlayerContent(vocabularies);
    }

    private void startPlayingAt(int itemIndex, AudioPlayerUtils.PlayerField playerField) {

        mPlayerModel.setItemIndex(itemIndex);
        mPlayerModel.setPlayerField(playerField);

        Intent intent = new Intent(getActivity(), AudioService.class);
        intent.setAction(AudioService.START_PLAYING);
        getActivity().startService(intent);
    }

    private void optionPlayClicked() {
        Intent intent = new Intent(getActivity(), AudioService.class);
        intent.setAction(AudioService.PLAY_BUTTON_CLICKED);
        getActivity().startService(intent);
    }

    private void newItemFocused(int newItemIndex) {

        AppPreference appPreference = AppPreference.getInstance();
        appPreference.setPlayerItemIndex(newItemIndex);
        appPreference.setPlayerField(AudioPlayerUtils.PlayerField.SPELL);

        Intent intent = new Intent(getActivity(), AudioService.class);
        intent.setAction(AudioService.NEW_ITEM_FOCUSED);
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

    /**
     * Broadcast receiver
     */
    protected class ServiceBroadcastReceiver extends BroadcastReceiver {

        private ServiceBroadcastReceiver() {}

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra(GlobalVariable.PLAYER_BROADCAST_ACTION);
            Logger.d(TAG, "broadcast received, " + action);
            switch (action) {

                case AudioService.ITEM_COMPLETE:
                    break;

                case AudioService.TO_ITEM:
                    if (mVocabularies == null || mVocabularies.isEmpty() || mPlayerModel == null || mPlayerView == null) {
                        break;
                    }
                    int newItemIndex = intent.getIntExtra(AudioService.ITEM_INDEX, -1);
                    if (newItemIndex == -1) {
                        break;
                    }
                    mPlayerView.moveToPosition(newItemIndex);
                    mPlayerModel.createPlayerDetailContent(mVocabularies.get(newItemIndex));
                    break;

                case AudioService.LIST_COMPLETE:
                    break;

                case AudioService.TO_NEXT_LIST:
                    if (mPlayerModel == null || mPlayerView == null) {
                        break;
                    }
                    /** To solved the bug that in onStop state addNewPlayer, but PlayerList didn't move. */
                    if (isResumed()) {
                        mPlayerView.moveToPlayer(Infinite3View.RIGHT_VIEW_INDEX);
                    } else {
                        mPlayerView.removeOldPlayer(Infinite3View.CENTER_VIEW_INDEX);
                        mIsWaitingAddNewPlayer = true;
                    }
                    mVocabularies = mPlayerModel.getPlayerContent();
                    break;

                case AudioService.SHOW_DETAIL:
                    break;

                case AudioService.HIDE_DETAIL:
                    break;

                case AudioService.TO_SENTENCE:
                    if (mPlayerView == null) {
                        break;
                    }
                    int sentenceIndex = intent.getIntExtra(AudioService.SENTENCE_INDEX, -1);
                    if (sentenceIndex == -1) {
                        break;
                    }
                    mPlayerView.moveDetailPage(sentenceIndex);
                    break;

                case AudioService.PLAYER_STATE_CHANGED:
                    if (mPlayerView == null) {
                        break;
                    }
                    mPlayerView.setIconState(false, AppPreference.getInstance().getPlayerState().equals(AudioPlayerUtils.PlayerState.PLAYING), false);
                    break;

                default:
                    Logger.d(TAG, "unexpected condition in onReceive: " + intent.toString());
                    break;
            }
        }
    }
}
