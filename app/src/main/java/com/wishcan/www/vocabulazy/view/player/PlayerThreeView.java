package com.wishcan.www.vocabulazy.view.player;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import com.wishcan.www.vocabulazy.MainActivity;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.Vocabulary;
import com.wishcan.www.vocabulazy.view.infinitethreeview.InfiniteThreeView;

/**
 * Created by swallow on 2015/9/23.
 */
public class PlayerThreeView extends InfiniteThreeView {

    interface OnPlayerScrollListener {
        void onPlayerScrollStopped();

        void onPlayerScrollStarted();
    }

    interface OnNewPlayerViewRefreshedListener {
        void onNewPlayerViewRefreshed();
    }

    public interface OnFocusChangedListener {

        void onFocusChanged(int position);
    }

    public interface OnItemPreparedListener {
        void onInitialItemPrepared();

        void onFinalItemPrepared();
    }

    private static final String TAG = PlayerThreeView.class.getSimpleName();

    private Context mContext;

    private Database mDatabase;

    private RelativeLayout.LayoutParams mDefaultLayoutParams;

    /**
     * Listener will be set in PlayerScrollView.
     */
    private OnPlayerScrollListener mOnPlayerScrollListener;

    private OnNewPlayerViewRefreshedListener mOnNewPlayerViewAddedListener;

    private OnPageChangedListener mOnPageChangedListener;

    private OnFocusChangedListener mOnFocusChangedListener;

    private OnItemPreparedListener mOnItemPreparedListener;

    /**
     *
     * */
    private String[] mFrom = {
            "voc_spell",
            "voc_translation",
            "voc_spell_detail",
            "voc_translation_detail",
            "voc_kk_detail",
            "voc_sentence_detail",
            "voc_sentence_translation_detail"
    };

    /**
     *
     * */
    private int[] mTo = {
            PlayerScrollView.DEFAULT_PLAYER_LIST_ITEM0_RES_ID,
            PlayerScrollView.DEFAULT_PLAYER_LIST_ITEM1_RES_ID,
            PlayerScrollView.DEFAULT_PLAYER_DETAIL_ITEM0_RES_ID,
            PlayerScrollView.DEFAULT_PLAYER_DETAIL_ITEM1_RES_ID,
            PlayerScrollView.DEFAULT_PLAYER_DETAIL_ITEM2_RES_ID,
            PlayerScrollView.DEFAULT_PLAYER_DETAIL_ITEM3_RES_ID,
            PlayerScrollView.DEFAULT_PLAYER_DETAIL_ITEM4_RES_ID
    };


    public PlayerThreeView(Context context) {
        this(context, null);
    }

    public PlayerThreeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        mDatabase = ((MainActivity) context).getDatabase();

        mDefaultLayoutParams = new RelativeLayout.LayoutParams(ViewPager.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public void addNewPlayer(ArrayList<Integer> playlistContentIDs) {
        this.addNewPlayer(playlistContentIDs, CENTER_VIEW_INDEX);
    }

//    public void addNewPlayer(LinkedList<Integer> playlistContentIDs, int position){
//
//        LinkedList<Vocabulary> vocabularies = mDatabase.getVocabulariesByIDs(playlistContentIDs);
//
//        LinkedList<HashMap> playlistContent = createPlaylistContent(vocabularies);
//        PlayerScrollView playerScrollView = new PlayerScrollView(getContext());
//
//        PlayerScrollView.PlayerAdapter playerAdapter =
//                playerScrollView.getAdapter(mContext, PlayerScrollView.DEFAULT_PLAYER_LIST_ITEM_VIEW_RES_ID, playlistContent, mFrom, mTo);
//        playerScrollView.setAdapter(playerAdapter);
//        playerScrollView.setOnPlayerScrollStoppedListener(new PlayerScrollView.OnPlayerScrollStoppedListener() {
//            @Override
//            public void onPlayerScrollStopped() {
//                mOnPlayerScrollListener.onPlayerScrollStopped();
//            }
//        });
//
//        playerScrollView.setOnFocusChangedListener(new PlayerScrollView.OnFocusChangedListener() {
//            @Override
//            public void onFocusChanged(int position) {
//                mOnFocusChangedListener.onFocusChanged(position);
//            }
//        });
//
//        refreshItem(position, playerScrollView);
////        mOnNewPlayerViewAddedListener.onNewPlayerViewRefreshed();;
//    }

    public void addNewPlayer(ArrayList<Integer> playlistContentIDs, int position) {
        AddNewPlayerAsyncTask addNewPlayerAsyncTask = new AddNewPlayerAsyncTask(this, position);
        addNewPlayerAsyncTask.execute(playlistContentIDs);
    }

    public void cleanOldPlayer(int position) {

        if (position == CENTER_VIEW_INDEX)
            return;
        refreshItem(position, null);
    }

    public void moveToPosition(int position) {
        if (getCurrentItem() != null)
            ((PlayerScrollView) getCurrentItem()).moveToPosition(position);
    }

    /**
     * temporary function for development
     */
    private LinkedList<String> createPlaylistContentSpells(LinkedList<Vocabulary> vocabularies) {
        LinkedList<String> playlistContetnSpells = new LinkedList<>();

        for (int index = 0; index < vocabularies.size(); index++) {
            playlistContetnSpells.add(vocabularies.get(index).getSpell());
        }

        return playlistContetnSpells;
    }

    private LinkedList<HashMap> createPlaylistContent(ArrayList<Vocabulary> vocabularies) {
        LinkedList<HashMap> playlistContent = new LinkedList<>();

//        Log.d(TAG, "createPlaylistContent: " + vocabularies);

        Iterator<Vocabulary> ii = vocabularies.iterator();
        while (ii.hasNext()) {
            Vocabulary vocabulary = ii.next();

//            Log.d(TAG, "createPlaylistContent: "+vocabulary);

            HashMap<String, String> hm = new HashMap<>();
            hm.put(mFrom[0], vocabulary.getSpell());
            hm.put(mFrom[1], vocabulary.getTranslate());
            hm.put(mFrom[2], vocabulary.getSpell());
            hm.put(mFrom[3], vocabulary.getTranslate());
            hm.put(mFrom[4], vocabulary.getKK());
            hm.put(mFrom[5], vocabulary.getEn_Sentence().get(0));
            hm.put(mFrom[6], vocabulary.getCn_Sentence().get(0));

//            Log.d(TAG, "createPlaylistContent: " + hm);

            playlistContent.add(hm);
        }

        return playlistContent;
    }

    public void showDetail() {
        if (getCurrentItem() != null)
            ((PlayerScrollView) getCurrentItem()).showItemDetails();
    }

    public void hideDetail() {
        if (getCurrentItem() != null)
            ((PlayerScrollView) getCurrentItem()).hideItemDetails();
    }

    public void setOnPlayerScrollStoppedListener(OnPlayerScrollListener listener) {
        mOnPlayerScrollListener = listener;
    }

    public void setOnNewPlayerViewAddedListener(OnNewPlayerViewRefreshedListener listener) {
        mOnNewPlayerViewAddedListener = listener;
    }

    public void setOnFocusChangedListener(OnFocusChangedListener listener) {
        mOnFocusChangedListener = listener;
    }

    public void setOnItemPreparedListener(OnItemPreparedListener listener) {
        mOnItemPreparedListener = listener;
    }

    public int getCurrentFocusedPosition() {
        if (getCurrentItem() != null)
            return ((PlayerScrollView) getCurrentItem()).getCurrentFocusedPosition();
        else
            return 0;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if(getCurrentItem() != null)
                    if(((PlayerScrollView) getCurrentItem()).isShowingDetails())
                        return false;

                mOnPlayerScrollListener.onPlayerScrollStarted();
                break;
            default:
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    private class AddNewPlayerAsyncTask extends AsyncTask<ArrayList<Integer>, Void, LinkedList<HashMap>> {

        private PlayerThreeView view;

        private int mPlayerPosition;

        public AddNewPlayerAsyncTask(PlayerThreeView view, int position) {
            this.view = view;
            mPlayerPosition = position;
        }

        @Override
        protected LinkedList<HashMap> doInBackground(ArrayList<Integer>... params) {

            Log.d(TAG, "doInBackground");

            ArrayList<Vocabulary> vocabularies = mDatabase.getVocabulariesByIDs(params[0]);

//            for (int index = 0; index < vocabularies.size(); index++) {
//                Log.d(TAG, ""+vocabularies.get(index).getSpell());
//            }

            LinkedList<HashMap> playlistContent = createPlaylistContent(vocabularies);

            return playlistContent;
        }

        @Override
        protected void onPostExecute(LinkedList<HashMap> playlistContent) {
            super.onPostExecute(playlistContent);

            Log.d(TAG, "onPostExecute: execute?");

            PlayerScrollView playerScrollView = new PlayerScrollView(getContext());
            PlayerScrollView.PlayerAdapter playerAdapter =
                    playerScrollView.getAdapter(mContext, PlayerScrollView.DEFAULT_PLAYER_LIST_ITEM_VIEW_RES_ID, playlistContent, mFrom, mTo);
            playerScrollView.setAdapter(playerAdapter);
            playerScrollView.setOnPlayerScrollStoppedListener(new PlayerScrollView.OnPlayerScrollStoppedListener() {
                @Override
                public void onPlayerScrollStopped() {
                    mOnPlayerScrollListener.onPlayerScrollStopped();
                }
            });

            playerScrollView.setOnFocusChangedListener(new PlayerScrollView.OnFocusChangedListener() {
                @Override
                public void onFocusChanged(int position) {
                    mOnFocusChangedListener.onFocusChanged(position);
                }
            });

            playerScrollView.setOnItemPreparedListener(new PlayerScrollView.OnItemPreparedListener() {
                @Override
                public void onInitialItemPrepared() {
                    if (mOnItemPreparedListener != null)
                        mOnItemPreparedListener.onInitialItemPrepared();
                }

                @Override
                public void onFinalItemPrepared() {
                    if (mOnItemPreparedListener != null)
                        mOnItemPreparedListener.onFinalItemPrepared();
                }
            });

            refreshItem(mPlayerPosition, playerScrollView);
        }
    }


}
