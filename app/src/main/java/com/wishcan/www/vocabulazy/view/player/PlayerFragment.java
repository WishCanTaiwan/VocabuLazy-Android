package com.wishcan.www.vocabulazy.view.player;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.LinkedList;

import com.wishcan.www.vocabulazy.MainActivity;
import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.player.AudioService;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.Option;
import com.wishcan.www.vocabulazy.view.infinitethreeview.InfiniteThreeView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlayerFragment.OptionOnClickListener} interface
 * to handle interaction events.
 * Use the {@link PlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayerFragment extends Fragment {

    public interface OptionOnClickListener {
        // TODO: Update argument type and name
        void onOptionClicked(View v);
    }

    public static final String ARG_NOTE_INDEX = "noteID";

    public static final String ARG_BOOK_INDEX = "bookIndex";

    public static final String ARG_LESSON_INDEX = "lessonIndex";

    private static final String TAG = PlayerFragment.class.getSimpleName();

    private static final int PLAYER_RES_ID = R.layout.fragment_player;

    private static final int PLAYER_VIEW_RES_ID = R.id.player_scroll_view;

    private static final int OPTION_PARENT_RES_ID = R.id.player_option_parent;

    private static final int OPTION_VIEW_RES_ID = R.id.player_option_view;

    private static final int PLAYER_OPTION_IMAGE_VIEW_RES_ID = R.id.action_player_option;

    private static final int PLAYER_PLAY_IMAGE_VIEW_RES_ID = R.id.action_player_play;

    private static final int PLAYER_FAVORITE_IMAGE_VIEW_RES_ID = R.id.action_player_favorite;

    private static final int ANIMATE_TRANSLATE_BOTTOM_UP = R.anim.translate_bottom_up;

    private static final int ANIMATE_TRANSLATE_UP_BOTTOM = R.anim.translate_up_bottom;

    private static final int PLAYER_STOP_DRAWABLE_RES_ID = R.drawable.icon_stop;

    private static final int PLAYER_PLAY_DRAWABLE_RES_ID = R.drawable.icon_play;

    private static final int THREE_VIEW_LEFT = 0;

    private static final int THREE_VIEW_CENTRAL = 1;

    private static final int THREE_VIEW_RIGHT = 2;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    private OptionOnClickListener mListener;

    private MainActivity mMainActivity;

    private FragmentManager mFragmentManager;

    private FragmentTransaction mFragmentTransaction;

    private View mTransparentGrayView;

    private PlayerOptionView mOptionView;

//    private AudioPlayer mAudioPlayer;

    private AudioService mAudioService;

    private Database mDatabase;

    private IntentFilter mAudioServiceBroadcastIntentFilter;

    private AudioServiceBroadcastReceiver mAudioServiceBoardcastReceiver;

    private PlayerThreeView mPlayerThreeView;

    private ArrayList<ArrayList<Integer>> mContentIDsOfThreeViews;

//    private LinkedList<Integer> mCurrentNoteContents;

    private int mNumOfLesson;

    private int mCurrentBookIndex;

    private int mCurrentLessonIndex;

    private int mCurrentBookID;

    private int mCurrentLessonID;

    private int mCurrentFocusedPosition;

    private ArrayList<Integer> mCurrentContentInPlayer;

    private String mPreviousTitle;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SwallowPlayerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlayerFragment newInstance(String previousTitle, int bookIndex, int lessonIndex, Database database) {
        PlayerFragment fragment = new PlayerFragment();
        Bundle args = new Bundle();
        args.putString(MainActivity.PREVIOUS_TITLE, previousTitle);
        args.putInt(ARG_BOOK_INDEX, bookIndex);
        args.putInt(ARG_LESSON_INDEX, lessonIndex);
//        args.putParcelable("database", database);
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

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        Log.d(TAG, "onSaveInstanceState");

        outState.putInt("mCurrentBookIndex", mCurrentBookIndex);
        outState.putInt("mCurrentLessonIndex", mCurrentLessonIndex);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreatedView");


        mMainActivity = (MainActivity) getActivity();

        mDatabase = mMainActivity.getDatabase();

        if (savedInstanceState != null) {
            mCurrentBookIndex = savedInstanceState.getInt("mCurrentBookIndex");
            mCurrentLessonIndex = savedInstanceState.getInt("mCurrentLessonIndex");
            mCurrentContentInPlayer = mDatabase.getCurrentContentInPlayer();
        } else {
            mCurrentBookIndex = -1;
            mCurrentLessonIndex = -1;
            mCurrentContentInPlayer = null;

            if (getArguments() != null) {
                mPreviousTitle = getArguments().getString(MainActivity.PREVIOUS_TITLE);
                mCurrentBookIndex = getArguments().getInt(ARG_BOOK_INDEX);
                mCurrentLessonIndex = getArguments().getInt(ARG_LESSON_INDEX);
            }
        }

        mAudioServiceBroadcastIntentFilter = new IntentFilter(AudioService.BROADCAST);
        mAudioServiceBoardcastReceiver = new AudioServiceBroadcastReceiver();
        LocalBroadcastManager.getInstance(mMainActivity).registerReceiver(mAudioServiceBoardcastReceiver, mAudioServiceBroadcastIntentFilter);

        mCurrentBookID = mDatabase.getBookID(mCurrentBookIndex);
        mCurrentLessonID = mDatabase.getLessonID(mCurrentBookIndex, mCurrentLessonIndex);
        mNumOfLesson = mDatabase.getNumOfLesson(mCurrentBookIndex);

        mContentIDsOfThreeViews = new ArrayList<>();

        // Inflate the layout for this fragment
        View v = inflater.inflate(PLAYER_RES_ID, container, false);
        mPlayerThreeView = (PlayerThreeView) v.findViewById(PLAYER_VIEW_RES_ID);

        playerThreeViewListenerRegistration();
        loadContentIDsOfThreeViews();

        mPlayerThreeView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPlayerThreeView.addNewPlayer(mContentIDsOfThreeViews.get(0));
            }
        }, 600);

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView playImageView = (ImageView) getView().findViewById(PLAYER_PLAY_IMAGE_VIEW_RES_ID);
        playImageView.setImageResource(PLAYER_PLAY_DRAWABLE_RES_ID);
    }

    private void playerThreeViewListenerRegistration() {
        mPlayerThreeView.setOnPlayerScrollStoppedListener(new PlayerThreeView.OnPlayerScrollListener() {
            @Override
            public void onPlayerScrollStopped() {

                Intent intent = new Intent(mMainActivity, AudioService.class);
                intent.setAction(AudioService.ACTION_START);
                intent.putExtra("position", mPlayerThreeView.getCurrentFocusedPosition());
                mMainActivity.startService(intent);

            }

            @Override
            public void onPlayerScrollStarted() {

                Intent intent = new Intent(mMainActivity, AudioService.class);
                intent.setAction(AudioService.ACTION_STOP);
                mMainActivity.startService(intent);

                Intent intent2 = new Intent(mMainActivity, AudioService.class);
                intent2.setAction(AudioService.ACTION_REMOVE_PENDING_TASK);
                mMainActivity.startService(intent2);

            }
        });

        mPlayerThreeView.setOnNewPlayerViewAddedListener(new PlayerThreeView.OnNewPlayerViewRefreshedListener() {
            @Override
            public void onNewPlayerViewRefreshed() {
                // BeiBei, this is where you can add your function after a new list is added
            }
        });

        mPlayerThreeView.setOnPageChangedListener(new InfiniteThreeView.OnPageChangedListener() {
            @Override
            public void onPageScrolled() {

                Intent intent = new Intent(mMainActivity, AudioService.class);
                intent.setAction(AudioService.ACTION_STOP);
                mMainActivity.startService(intent);
            }

            @Override
            public void onPageChanged(int direction) {

                Log.d(TAG, "onPageChanged");

                Intent intent = new Intent(mMainActivity, AudioService.class);
                intent.setAction(AudioService.ACTION_REMOVE_PENDING_TASK);
                mMainActivity.startService(intent);

                mCurrentLessonIndex = (mCurrentLessonIndex - direction + mNumOfLesson) % mNumOfLesson;

//                Log.d(TAG, "direction: " + direction);
//                Log.d(TAG, "mCurrentBookIndex: " + mCurrentBookIndex);
//                Log.d(TAG, "mCurrentLessonIndex: " + mCurrentLessonIndex);

                ArrayList<Integer> refreshedContentIDs = refreshContentIDsOfThreeViews(mCurrentLessonIndex, direction);
                mPlayerThreeView.addNewPlayer(refreshedContentIDs);
                mPlayerThreeView.cleanOldPlayer(direction == InfiniteThreeView.MOVE_TO_RIGHT ? InfiniteThreeView.RIGHT_VIEW_INDEX : InfiniteThreeView.LEFT_VIEW_INDEX);
                (mMainActivity).switchActionBarTitle(mDatabase.getLessonName(mCurrentBookIndex, mCurrentLessonIndex));

            }
        });

        mPlayerThreeView.setOnFocusChangedListener(new PlayerThreeView.OnFocusChangedListener() {
            @Override
            public void onFocusChanged(int position) {

                Intent intent = new Intent(mMainActivity, AudioService.class);
                intent.setAction(AudioService.ACTION_START);
                intent.putExtra("position", mPlayerThreeView.getCurrentFocusedPosition());
                mMainActivity.startService(intent);

//                mCurrentFocusedPosition = position;
//                mDatabase.setCurrentPlayingItem(mCurrentFocusedPosition);

            }
        });

        mPlayerThreeView.setOnItemPreparedListener(new PlayerThreeView.OnItemPreparedListener() {
            @Override
            public void onInitialItemPrepared() {
                if (mCurrentBookIndex != mDatabase.getCurrentPlayingBook() || mCurrentLessonIndex != mDatabase.getCurrentPlayingList())
                    setContentToPlayerAndStart();
            }

            @Override
            public void onFinalItemPrepared() {
                Log.d(TAG, "onFinalItemPrepared");
                if (mCurrentBookIndex == mDatabase.getCurrentPlayingBook() && mCurrentLessonIndex == mDatabase.getCurrentPlayingList()) {
                    mPlayerThreeView.moveToPosition(mDatabase.getCurrentPlayingItem());
                }
            }
        });
    }

    private void setContentToPlayerAndStart() {
        ArrayList<Integer> currentNoteContents = (ArrayList<Integer>) mContentIDsOfThreeViews.get(0).clone();

        mDatabase.setCurrentContentInPlayer(currentNoteContents);

        Bundle contentBundle = new Bundle();

        if (mCurrentContentInPlayer != null)
            contentBundle.putIntegerArrayList("mCurrentContentInPlayer", mCurrentContentInPlayer);
        else
            contentBundle.putIntegerArrayList("mCurrentContentInPlayer", currentNoteContents);

        Intent intent = new Intent(mMainActivity, AudioService.class);
        intent.setAction(AudioService.ACTION_SET_CONTENT);
        intent.putExtra("contentBundle", contentBundle);
        mDatabase.setCurrentPlayingBook(mCurrentBookIndex);
        mDatabase.setCurrentPlayingList(mCurrentLessonIndex);
        mMainActivity.startService(intent);

        Intent intent2 = new Intent(mMainActivity, AudioService.class);
        intent2.setAction(AudioService.ACTION_START);
        intent.putExtra("position", mPlayerThreeView.getCurrentFocusedPosition());
        mMainActivity.startService(intent2);
    }

    private void loadContentIDsOfThreeViews() {

        int centralIndex = mCurrentLessonIndex;

        int leftIndex = (centralIndex - 1 + mNumOfLesson) % mNumOfLesson;
        int rightIndex = (centralIndex + 1 + mNumOfLesson) % mNumOfLesson;
        ArrayList<Integer> centralNoteContentIDs = mDatabase.getContentIDs(mCurrentBookIndex, centralIndex);

        mContentIDsOfThreeViews.add(centralNoteContentIDs);
    }

    private ArrayList<Integer> refreshContentIDsOfThreeViews(int refreshedLessonIndex, int direction) {

        ArrayList<Integer> refreshedContentIDs = mDatabase.getContentIDs(mCurrentBookIndex, refreshedLessonIndex);

        LinkedList<ArrayList<Integer>> ll = new LinkedList<>();
        for (int index = 0; index < mContentIDsOfThreeViews.size(); index++) {
            ll.add(mContentIDsOfThreeViews.get(index));
        }

        if (direction == PlayerThreeView.MOVE_TO_RIGHT) {
            ll.removeLast();
            ll.addFirst(refreshedContentIDs);
        } else if (direction == PlayerThreeView.MOVE_TO_LEFT) {
            ll.removeFirst();
            ll.addLast(refreshedContentIDs);
        } else {
        }

        mContentIDsOfThreeViews.clear();
        for (int index = 0; index < ll.size(); index++) {
            mContentIDsOfThreeViews.add(ll.get(index));
        }

        return refreshedContentIDs;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onOptionClicked(View v) {
        int resId = v.getId();
        int index;
        switch (resId) {
            case OPTION_PARENT_RES_ID:     //gray screen
                exitOptionView(v);
                break;
            case OPTION_VIEW_RES_ID:       //option view, this is a TabView
                break;
            case PLAYER_FAVORITE_IMAGE_VIEW_RES_ID:
                break;
            case PLAYER_PLAY_IMAGE_VIEW_RES_ID:
                playpauseDochi();       // here to call play/pause function
                break;
            case PLAYER_OPTION_IMAGE_VIEW_RES_ID:
                showPlayerOptionView();
                break;
            default:
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OptionOnClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach");
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(mMainActivity != null) {
            ActionBar actionBar = mMainActivity.getActionBar();
            if (actionBar != null)
                actionBar.setDisplayHomeAsUpEnabled(true);

            mMainActivity.switchActionBarTitle(mDatabase.getLessonName(mCurrentBookIndex, mCurrentLessonIndex));
        }
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
        MainActivity parentActivity = mMainActivity;

        parentActivity.setActionBarTitleWhenStop(parentActivity.getActionBarTitleTextView());
        parentActivity.switchActionBarTitle(mPreviousTitle);

        LocalBroadcastManager.getInstance(mMainActivity).unregisterReceiver(mAudioServiceBoardcastReceiver);

    }

    private void playpauseDochi() {

        Intent intent = new Intent(mMainActivity, AudioService.class);
        intent.setAction(AudioService.ACTION_PLAYPAUSE);
        mMainActivity.startService(intent);
    }

    private void setPlayPauseImage(boolean isPlaying) {

        if (getView() == null)
            return;

        ImageView playImageView = (ImageView) getView().findViewById(PLAYER_PLAY_IMAGE_VIEW_RES_ID);
        if (isPlaying) playImageView.setImageResource(PLAYER_STOP_DRAWABLE_RES_ID);
        else playImageView.setImageResource(PLAYER_PLAY_DRAWABLE_RES_ID);
    }

    private void showPlayerOptionView() {
        View mTransparentGrayView = getView().findViewById(OPTION_PARENT_RES_ID);
        mOptionView = (PlayerOptionView) getView().findViewById(OPTION_VIEW_RES_ID);

        // this line is to set the default setting, which is stored in the database, to playerOptionView.
        mOptionView.setOptionsInTabContent(mDatabase.getOptions());

        if (mTransparentGrayView.getVisibility() == View.INVISIBLE)
            mTransparentGrayView.setVisibility(View.VISIBLE);

        mOptionView.setOnOptionChangedListener(new PlayerOptionView.OnOptionChangedListener() {
            @Override
            public void onOptionChanged(View v, ArrayList<Option> optionLL, int currentMode) {
                mDatabase.setCurrentOptions(optionLL);
                mDatabase.setCurrentOptionMode(currentMode);

                Intent intent = new Intent(mMainActivity, AudioService.class);
                intent.setAction(AudioService.ACTION_UPDATE_OPTION);
                mMainActivity.startService(intent);

            }
        });

        Animation comeInAnimation = AnimationUtils.loadAnimation(mMainActivity, ANIMATE_TRANSLATE_BOTTOM_UP);
        mOptionView.startAnimation(comeInAnimation);

        mTransparentGrayView.invalidate();
    }


    public void exitOptionView(final View view) {
        if (view.getVisibility() != View.VISIBLE)
            return;
        Animation goOutAnimation = AnimationUtils.loadAnimation(mMainActivity, ANIMATE_TRANSLATE_UP_BOTTOM);
        mOptionView.startAnimation(goOutAnimation);
        goOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.INVISIBLE);
                view.invalidate();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    public class AudioServiceBroadcastReceiver extends BroadcastReceiver {

        private AudioServiceBroadcastReceiver() {
            // prevents instantiation by other packages.
        }

        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getStringExtra(AudioService.BROADCAST_ACTION)) {

                case AudioService.BROADCAST_ACTION_ITEM_START:

                    int itemIndex = intent.getIntExtra("itemIndex", -1);
//                    Log.d("BROADCAST_。ACTION_S。TART", " "+itmIndex);
                    mPlayerThreeView.moveToPosition(itemIndex);
                    setPlayPauseImage(true);
                    break;

                case AudioService.BROADCAST_ACTION_SENTENCE_START:
                    /**
                     * TODO : "sentenceIndex" variable is made for notifying views to
                     * TODO : switch.
                     */
                    int sentenceIndex = intent.getIntExtra("sentenceIndex", -1);

                    mPlayerThreeView.showDetail();
                    break;

                case AudioService.BROADCAST_ACTION_SENTENCE_COMPLETE:
                    mPlayerThreeView.hideDetail();
                    break;

                case AudioService.BROADCAST_ACTION_ITEM_RESUME:
                    setPlayPauseImage(true);
                    break;

                case AudioService.BROADCAST_ACTION_ITEM_STOP:
                    setPlayPauseImage(false);
                    break;

                case AudioService.BROADCAST_ACTION_SET_PLAYPAUSE_ICON:
                    boolean isPlaying = intent.getBooleanExtra("isPlaying", false);
                    setPlayPauseImage(isPlaying);
                    break;

                case AudioService.BROADCAST_ACTION_ITEM_COMPLETE:
                    break;

                case AudioService.BROADCAST_ACTION_LIST_COMPLETE:
                    mPlayerThreeView.setCurrentItem(PlayerThreeView.RIGHT_VIEW_INDEX);
                    mPlayerThreeView.moveToPosition(0);
                    break;

                case AudioService.BROADCAST_ACTION_CURRENT_PLAYER_INFO:
                    break;

                default:
                    break;
            }
        }
    }

}

        