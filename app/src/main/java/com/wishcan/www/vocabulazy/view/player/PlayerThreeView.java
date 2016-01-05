package com.wishcan.www.vocabulazy.view.player;

import android.content.Context;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Parcelable;
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
import com.wishcan.www.vocabulazy.R;
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

    @Override
    protected void onDraw(Canvas canvas) {
//        Log.d(TAG, "onDraw");
        super.onDraw(canvas);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
//        Log.d(TAG, "onRestoreInstanceState");
        super.onRestoreInstanceState(state);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
//        Log.d(TAG, "onSavedInstanceState");
        return super.onSaveInstanceState();
    }

    public void addNewPlayer(ArrayList<Integer> playlistContentIDs) {
        this.addNewPlayer(playlistContentIDs, CENTER_VIEW_INDEX);
    }

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

    private LinkedList<HashMap> createPlaylistContent(ArrayList<Vocabulary> vocabularies) {
        LinkedList<HashMap> playlistContent = new LinkedList<>();

        Iterator<Vocabulary> ii = vocabularies.iterator();
        while (ii.hasNext()) {
            Vocabulary vocabulary = ii.next();

            HashMap<String, Object> hm = new HashMap<>();
            hm.put(mFrom[0], vocabulary.getSpell());
            hm.put(mFrom[1], vocabulary.getTranslationInOneString());
            hm.put(mFrom[2], vocabulary.getSpell());
            hm.put(mFrom[3], vocabulary.getTranslationInOneString());
            hm.put(mFrom[4], vocabulary.getKK());
            hm.put(mFrom[5], vocabulary.getEn_Sentence());      // En_Sentence is an ArrayList<String>
            hm.put(mFrom[6], vocabulary.getCn_Sentence());      // Cn_Sentence is an ArrayList<String>

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

    public void setDetailPage(int index){
        if (getCurrentItem () != null)
            if(((PlayerScrollView) getCurrentItem()).isShowingDetails())
                ((PlayerScrollView) getCurrentItem()).setDetailsItemPage(index);
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

//            Log.d(TAG, "doInBackground");

            ArrayList<Vocabulary> vocabularies = mDatabase.getVocabulariesByIDs(params[0]);

            LinkedList<HashMap> playlistContent = createPlaylistContent(vocabularies);

            return playlistContent;
        }

        @Override
        protected void onPostExecute(LinkedList<HashMap> playlistContent) {
            super.onPostExecute(playlistContent);

//            Log.d(TAG, "onPostExecute: execute?");

            PlayerScrollView playerScrollView = new PlayerScrollView(getContext());
            PlayerScrollView.PlayerAdapter playerAdapter =
                    playerScrollView.getAdapter(mContext, PlayerScrollView.DEFAULT_PLAYER_LIST_ITEM_VIEW_RES_ID, playlistContent, mFrom, mTo);
            playerScrollView.setId(R.id.playerscrollview_id);
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
