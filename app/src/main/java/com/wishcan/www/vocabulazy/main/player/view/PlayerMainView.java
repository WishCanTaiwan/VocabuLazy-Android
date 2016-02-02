package com.wishcan.www.vocabulazy.main.player.view;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.widget.LinkedListPagerAdapter;
import com.wishcan.www.vocabulazy.widget.Infinite3View;
import com.wishcan.www.vocabulazy.widget.PopScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by swallow on 2016/1/14.
 */
public class PlayerMainView extends Infinite3View {

    public interface OnPlayerScrollListener {
        void onPlayerVerticalScrollStop(int index);
        void onPlayerHorizontalScrollStop(int direction);
        void onPlayerHorizontalScrolling();
    }

    private Context mContext;
    private PlayerScrollView mPlayerScrollView;
    private LinkedList<HashMap> mPlayerDataList;
    private static HashMap<String, Object> mPlayerDetailDataMap;
    private static OnPlayerScrollListener mOnPlayerScrollListener;

    public PlayerMainView(Context context) {
        this(context, null);
    }

    public PlayerMainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mPlayerScrollView = null;
        setOnPageChangedListener(new OnPageChangedListener() {
            @Override
            public void onPageScrolled() {
                if (mOnPlayerScrollListener != null)
                    mOnPlayerScrollListener.onPlayerHorizontalScrolling();
            }

            @Override
            public void onPageChanged(int direction) {
                if (mOnPlayerScrollListener != null)
                    mOnPlayerScrollListener.onPlayerHorizontalScrollStop(direction);
            }
        });
    }

    public void addNewPlayer(LinkedList<HashMap> playerDataList){
        mPlayerDataList = playerDataList;
        mPlayerScrollView = new PlayerScrollView(mContext);
        mPlayerScrollView.setPopItemAdapter(
                mPlayerScrollView.getPopItemAdapter(mContext,
                        PlayerScrollView.PLAYER_ITEM_LAYOUT_RES_ID, mPlayerDataList,
                        PlayerScrollView.PLAYER_ITEM_CONTENT_FROM,
                        PlayerScrollView.PLAYER_ITEM_CONTENT_TO));
        refreshItem(CENTER_VIEW_INDEX, mPlayerScrollView);
    }

    public void removeOldPlayer(int position){
        if (position == CENTER_VIEW_INDEX)
            return;
        refreshItem(position, null);
    }

    public void refreshPlayerDetail(HashMap<String, Object> dataMap){
        mPlayerDetailDataMap = dataMap;
    }

    public void setOnPlayerScrollStopListener(OnPlayerScrollListener listener){
        mOnPlayerScrollListener = listener;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if(getCurrentItem() != null)
                    if(((PlayerScrollView) getCurrentItem()).isShowingDetails() == PopScrollView.STATE_ITEM_DETAIL_SHOW)
                        return false;
                break;
            default:
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    public static class PlayerScrollView extends PopScrollView {

        private static final int PLAYER_ITEM_LAYOUT_RES_ID = R.layout.view_player_item;
        private static final int PLAYER_ITEM_DETAIL_LAYOUT_RES_ID = R.layout.view_player_details;
        private static final int PLAYER_ITEM_DETAIL_SENTENCE_LAYOUT_RES_ID = R.layout.view_player_details_sentence;
        private static final int PLAYER_ITEM_DETAIL_PAGER_PARENT_LAYOUT_RES_ID = R.id.pager_parent;
        private static final int PLAYER_ITEM_DETAIL_PAGER_INDEX_PARENT_LAYOUT_RES_ID = R.id.pager_index_parent;
        
        public static final String[] PLAYER_ITEM_CONTENT_FROM = {
                "voc_spell", "voc_translation", "voc_translation2"
        };
        public static final int[] PLAYER_ITEM_CONTENT_TO = {
               R.id.player_voc_spell, R.id.player_voc_translation, R.id.player_voc_translation2
        };

        public static final String[] PLAYER_ITEM_DETAIL_CONTENT_FROM ={
                "voc_spell_detail",
                "voc_translation_detail",
                "voc_kk_detail",
                "voc_sentence_detail",
                "voc_sentence_translation_detail"
        };
        public static final int[] PLAYER_ITEM_DETAIL_LAYOUT_CONTENT_TO = {
                R.id.player_voc_spell_detail,
                R.id.player_voc_translation_detail,
                R.id.player_voc_kk_detail,
                R.id.player_voc_sentence_detail,
                R.id.player_voc_sentence_translation_detail
        };

        public enum PLAYER_ITEM_CONTENT_ID_s {
            VOC_SPELL_DETAIL(0),
            VOC_TRANSLATION_DETAIL(1),
            VOC_KK_DETAIL(2),
            VOC_SENTENCE_DETAIL(3),
            VOC_SENTENCE_TRANSLATION_DETAIL(4);

            private int value;
            PLAYER_ITEM_CONTENT_ID_s(int value){
                this.value = value;
            }

            public int getValue(){
                return this.value;
            }
        }

        private static PlayerDetailView mPlayerDetailView;

        public PlayerScrollView(Context context) {
            super(context);
            mPlayerDetailView = new PlayerDetailView(context);
            mPlayerDetailView.setAdapter(
                    mPlayerDetailView.getAdapter(
                            context, mPlayerDetailDataMap, PLAYER_ITEM_DETAIL_CONTENT_FROM, PLAYER_ITEM_DETAIL_LAYOUT_CONTENT_TO));
            setOnPopScrollStoppedListener(new OnPopScrollStoppedListener() {
                @Override
                public void onPopScrollStopped(int index) {
                    if (mOnPlayerScrollListener != null)
                        mOnPlayerScrollListener.onPlayerVerticalScrollStop(index);
                    mPlayerDetailView = new PlayerDetailView(getContext());
                    mPlayerDetailView.setAdapter(
                            mPlayerDetailView.getAdapter(
                                    getContext(), mPlayerDetailDataMap, PLAYER_ITEM_DETAIL_CONTENT_FROM, PLAYER_ITEM_DETAIL_LAYOUT_CONTENT_TO));
                }
            });
        }

        @Override
        protected View getItemDetailView() {
            return mPlayerDetailView;
        }

        private class PlayerDetailView extends LinearLayout {

            private Context context;
            private PlayerDetailView mContainer;
            private PagerIndexView pagerIndexView;
            private ViewPager viewPager;
            private LinkedList<ViewGroup> mItemPagesList;
            private PopItemDetailAdapter mAdapter;
            private int pageCount;

            public PlayerDetailView(Context context) {
                super(context);
                this.context = context;
                mContainer = this;
                setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                mContainer = this;
                pageCount = 0;
                setOrientation(VERTICAL);

            }

            public void setAdapter(PopItemDetailAdapter adapter){
                mAdapter = adapter;
                mAdapter.setChildViewToGroup();
            }

            public PopItemDetailAdapter getAdapter(Context context, HashMap<String, Object> dataMap, String[] from, int[] to){
                return new PopItemDetailAdapter(context, dataMap, from, to);
            }

            public void setCurrentPage(int index){
                if(viewPager != null)
                    viewPager.setCurrentItem(index);
            }

            private class PopItemDetailAdapter{
                private Context mContext;
                private HashMap<String, Object> mDataMap;
                private String[] mFrom;
                private int[] mTo;
                private LayoutInflater mInflater;

                PopItemDetailAdapter(Context context,
                                     HashMap<String, Object> dataMap,
                                     String[] from,
                                     int[] to){
                    mContext = context;
                    mDataMap = dataMap;
                    mFrom = from;
                    mTo = to;
                    mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                }

                public void setChildViewToGroup(){

                    ViewGroup itemView = (ViewGroup) mInflater.inflate(PLAYER_ITEM_DETAIL_LAYOUT_RES_ID, null);
                    ArrayList<String> enSentences, ceSentences;
                    viewPager = new ViewPager(mContext);

                    if(mDataMap == null)
                        return;

                    ((TextView) itemView
                            .findViewById(mTo[PLAYER_ITEM_CONTENT_ID_s.VOC_SPELL_DETAIL.getValue()]))
                            .setText((String) mDataMap.get(mFrom[PLAYER_ITEM_CONTENT_ID_s.VOC_SPELL_DETAIL.getValue()]));
                    ((TextView) itemView
                            .findViewById(mTo[PLAYER_ITEM_CONTENT_ID_s.VOC_TRANSLATION_DETAIL.getValue()]))
                            .setText((String) mDataMap.get(mFrom[PLAYER_ITEM_CONTENT_ID_s.VOC_TRANSLATION_DETAIL.getValue()]));
                    ((TextView) itemView
                            .findViewById(mTo[PLAYER_ITEM_CONTENT_ID_s.VOC_KK_DETAIL.getValue()]))
                            .setText((String) mDataMap.get(mFrom[PLAYER_ITEM_CONTENT_ID_s.VOC_KK_DETAIL.getValue()]));
                    enSentences = (ArrayList<String>) mDataMap.get(mFrom[PLAYER_ITEM_CONTENT_ID_s.VOC_SENTENCE_DETAIL.getValue()]);
                    ceSentences = (ArrayList<String>) mDataMap.get(mFrom[PLAYER_ITEM_CONTENT_ID_s.VOC_SENTENCE_TRANSLATION_DETAIL.getValue()]);

                    pageCount = enSentences.size();
                    pagerIndexView = new PagerIndexView(context, pageCount);

                    ((ViewGroup)itemView.findViewById(PLAYER_ITEM_DETAIL_PAGER_PARENT_LAYOUT_RES_ID)).addView(viewPager);
                    ((ViewGroup)itemView.findViewById(PLAYER_ITEM_DETAIL_PAGER_INDEX_PARENT_LAYOUT_RES_ID)).addView(pagerIndexView);
                    mContainer.addView(itemView);

                    createItemPages(pageCount, enSentences, ceSentences);
                }

                private void createItemPages(int pageCount, ArrayList<String> en_sentenceList,
                                             ArrayList<String> cn_sentenceList){
                    mItemPagesList = new LinkedList<>();
                    for(int i = 0; i < pageCount; i++) {
                        ViewGroup currentItemDetailsView =
                                (ViewGroup)((LayoutInflater) getContext()
                                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                                        .inflate(PLAYER_ITEM_DETAIL_SENTENCE_LAYOUT_RES_ID, null);

                        ((TextView) currentItemDetailsView
                                .findViewById(mTo[PLAYER_ITEM_CONTENT_ID_s.VOC_SENTENCE_DETAIL.getValue()]))
                                .setText(en_sentenceList.get(i));
                        ((TextView) currentItemDetailsView
                                .findViewById(mTo[PLAYER_ITEM_CONTENT_ID_s.VOC_SENTENCE_TRANSLATION_DETAIL.getValue()]))
                                .setText(cn_sentenceList.get(i));

                        mItemPagesList.add(currentItemDetailsView);
                    }
                    viewPager.setAdapter(new LinkedListPagerAdapter(mItemPagesList));
                    viewPager.addOnPageChangeListener(new OnPageChangeListener());

                }
            }

            private class OnPageChangeListener implements ViewPager.OnPageChangeListener {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
                @Override
                public void onPageSelected(int position) {
                    for(int i = 0; i < pageCount; i++) {
                        if(i == position)
                            ((GradientDrawable)((ImageView) pagerIndexView.getChildAt(i)).getDrawable())
                                    .setColor(ContextCompat.getColor(context, PagerIndexView.PAGER_INDEX_SELECTED_COLOR));
                        else
                            ((GradientDrawable)((ImageView) pagerIndexView.getChildAt(i)).getDrawable())
                                    .setColor(ContextCompat.getColor(context, PagerIndexView.PAGER_INDEX_COLOR));
                    }
                }
                @Override
                public void onPageScrollStateChanged(int state) {}
            }
        }

        private static class PagerIndexView extends LinearLayout{

            private static final int PAGER_INDEX_SELECTED_COLOR = R.color.player_pager_index_selected;
            private static final int PAGER_INDEX_COLOR = R.color.player_pager_index_color;

            private Context context;
            private int pagerCount;

            public PagerIndexView(Context context, int pagerCount) {
                this(context, null, pagerCount);
            }

            public PagerIndexView(Context context, AttributeSet attrs, int pagerCount) {
                super(context, attrs);
                this.context = context;
                this.pagerCount = pagerCount;
                setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                createPagerIndex(this.pagerCount);
            }

            private void createPagerIndex(int indexCount){
                for(int i = 0; i < indexCount; i++){
                    ImageView imageView = new ImageView(context);
                    imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.circle_pager_index));
                    if(i == 0)
                        ((GradientDrawable)imageView.getDrawable()).setColor(ContextCompat.getColor(context, PAGER_INDEX_SELECTED_COLOR));
                    else
                        ((GradientDrawable)imageView.getDrawable()).setColor(ContextCompat.getColor(context, PAGER_INDEX_COLOR));
                    imageView.setPadding(5, 5, 5, 5);
                    addView(imageView);
                }
            }
        }
    }


}
