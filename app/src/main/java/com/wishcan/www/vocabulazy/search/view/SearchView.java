package com.wishcan.www.vocabulazy.search.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.search.SearchActivity;
import com.wishcan.www.vocabulazy.widget.AdapterView;
import com.wishcan.www.vocabulazy.widget.LinkedListPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class SearchView extends RelativeLayout {

    public static final String TAG = SearchView.class.getSimpleName();

	public interface OnItemClickListener {
		void onAddIconClick(int position);
		void onListItemClick(int position);
	}

	private static final int SEARCH_ITEM_DETAIL_LAYOUT_RES_ID = R.layout.view_search_details;
	private static final int SEARCH_ITEM_DETAIL_SENTENCE_LAYOUT_RES_ID = R.layout.view_search_details_sentence;
    private static final int SEARCH_ITEM_DETAIL_PAGER_PARENT_LAYOUT_RES_ID = R.id.pager_parent;
    private static final int SEARCH_ITEM_DETAIL_PAGER_INDEX_PARENT_LAYOUT_RES_ID = R.id.pager_index_parent;

    public enum LIST_ITEM_DETAIL_CONTENT_TO_FROM_s {
        VOC_SPELL_DETAIL(
                "voc_spell_detail", R.id.search_voc_spell_detail),
        VOC_TRANSLATION_DETAIL(
                "voc_translation_detail", R.id.search_voc_translation_detail),
        VOC_KK_DETAIL(
                "voc_kk_detail", R.id.search_voc_kk_detail),
        VOC_SENTENCE_DETAIL(
                "voc_sentence_detail", R.id.search_voc_sentence_detail),
        VOC_SENTENCE_TRANSLATION_DETAIL(
                "voc_sentence_translation_detail", R.id.search_voc_sentence_translation_detail);

        private String from;
        private int to;
        LIST_ITEM_DETAIL_CONTENT_TO_FROM_s(String from, int to){
            this.from = from;
            this.to = to;
        }

        public int getResTo() { return to; }

        public String getResFrom() { return from; }
    }

    private SearchListView mSearchListView;
    private SearchDetailView mSearchDetailView;
    private RelativeLayout mSearchDetailParentView;     // use for exit SearchDetailView
    private Context mContext;
    private static OnItemClickListener mOnItemClickListener;

    public SearchView(Context context) {
    	this(context, null);
    }

    public SearchView(Context context, AttributeSet attrs) {
    	super(context, attrs);
        mContext = context;
        setBackgroundColor(Color.WHITE);
    	mSearchListView = new SearchListView(context);
    	mSearchDetailView = new SearchDetailView(context);
        mSearchDetailParentView = new RelativeLayout(context);

    	mSearchListView.refreshView(0, null);

        LayoutParams detailViewLayoutParams = new LayoutParams(
                900, 600);
        detailViewLayoutParams.addRule(CENTER_IN_PARENT);
    	mSearchDetailView.setLayoutParams(detailViewLayoutParams);

        mSearchDetailParentView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mSearchDetailParentView.addView(mSearchDetailView);
        mSearchDetailParentView.setVisibility(View.GONE);
        mSearchDetailParentView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSearchDetail();
            }
        });

    	addView(mSearchListView);
    	addView(mSearchDetailParentView);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void refreshSearchResult(LinkedList<HashMap> dataList) {
    	if(dataList == null)
    		return;
    	mSearchListView.refreshView(dataList.size(), dataList);
    }

    public void refreshSearchDetail(HashMap<String, Object> dataMap) {
    	mSearchDetailView.setAdapter(
                mSearchDetailView.getAdapter(mContext, dataMap));
    }
    
    public void showSearchDetail() {
        mSearchDetailParentView.setVisibility(VISIBLE);
    }

    public void closeSearchDetail() {
        mSearchDetailParentView.setVisibility(GONE);

    }

	public static class SearchListView extends ListView implements AdapterView<HashMap> {

        public enum LIST_ITEM_CONTENT_ID_s {
            VOC_SPELL(0),
            VOC_TRANSLATION(1),
            VOC_CATEGORY(2);
            
            private int value;
            LIST_ITEM_CONTENT_ID_s(int value){
                this.value = value;
            }
            
            public int getValue() { return value; }
        }

        public static final String[] LIST_ITEM_CONTENT_FROM = {
                "voc_spell", "voc_translation", "voc_category"
        };
        public static final int[] LIST_ITEM_CONTENT_TO = {
                R.id.search_result_eng_text_view, R.id.search_result_cn_text_view, R.id.search_result_category_text_view
        };

		private static final int DIVIDER_COLOR = R.color.search_divider;
        private static final int DIVIDER_HEIGHT = R.dimen.search_divider_height;
        private static final int SEARCH_LIST_ITEM_RES_ID = R.layout.view_search_list;
        private static final int ICON_ADD_RES_ID = R.id.search_result_adding_to_note_adding_icon;

        private LinkedList<HashMap<String, ?>> mDataList;
        private CustomizedSimpleAdapter mAdapter;
        private final Typeface engTf;
		private final Typeface cnTf;

        public SearchListView(Context context) {
        	this(context, null);
        }

        public SearchListView(Context context, AttributeSet attrs) {
        	super(context, attrs);
        	setBackgroundColor(Color.WHITE);
            setDivider(new ColorDrawable(ContextCompat.getColor(context, DIVIDER_COLOR)));
            setDividerHeight((int) getResources().getDimension(DIVIDER_HEIGHT));

            engTf = Typeface.createFromAsset(getResources().getAssets(), "fonts/tt0142m_.ttf");
        	cnTf = Typeface.createFromAsset(getResources().getAssets(), "fonts/DFHeiStd-W5.otf");

            mDataList = new LinkedList<>();
            mAdapter = new CustomizedSimpleAdapter(context, mDataList, SEARCH_LIST_ITEM_RES_ID, LIST_ITEM_CONTENT_FROM, LIST_ITEM_CONTENT_TO);
            setAdapter(mAdapter);
        }

        @Override
        public void refreshView(int count, LinkedList<HashMap> dataList) {

            if(dataList == null)
                return;

            mDataList.clear();

             for(HashMap ii:dataList){
                 HashMap<String, Object> hm = new HashMap<>();
                 hm.put(LIST_ITEM_CONTENT_FROM[0], ii.get(LIST_ITEM_CONTENT_FROM[0]));
                 hm.put(LIST_ITEM_CONTENT_FROM[1], ii.get(LIST_ITEM_CONTENT_FROM[1]));
                 hm.put(LIST_ITEM_CONTENT_FROM[2], ii.get(LIST_ITEM_CONTENT_FROM[2]));
                 mDataList.add(hm);
             }
            refresh();
        }

        private void refresh() {
        	mAdapter.notifyDataSetChanged();
        }

        class CustomizedSimpleAdapter extends SimpleAdapter {

	        private Context mContext;
	        private LinkedList<HashMap<String, ?>> mDataList;
	        private int mResource;
	        private String[] mFrom;
	        private int[]   mTo;

	        private LayoutInflater mInflater;
	        /**
	         * Constructor
	         *
	         * @param context  The context where the View associated with this SimpleAdapter is running
	         * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
	         *                 Maps contain the data for each row, and should include all the entries specified in
	         *                 "from"
	         * @param resource Resource identifier of a view layout that defines the views for this list
	         *                 item. The layout file should include at least those named views defined in "to"
	         * @param from     A list of column names that will be added to the Map associated with each
	         *                 item.
	         * @param to       The views that should display column in the "from" parameter. These should all be
	         *                 TextViews. The first N views in this list are given the values of the first N columns
	         */
	        public CustomizedSimpleAdapter(Context context, LinkedList<HashMap<String, ?>> data, int resource, String[] from, int[] to) {
	            super(context, data, resource, from, to);
	            mContext = context;
	            mDataList = (LinkedList) data;
	            mResource = resource;
	            mFrom = from;
	            mTo = to;

	            mInflater = ((SearchActivity)mContext).getLayoutInflater();
	        }

	        @Override
	        public View getView(int position, View convertView, ViewGroup parent) {
	            return createViewFromResource(position, convertView, parent , mResource);
	        }

	        private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource){
	            View v;
	            if(convertView == null)
	                v = mInflater.inflate(resource, parent, false);
	            else
	                v = convertView;
	            bindView(position, v);
	            return v;
	        }

	        private void bindView(int position, View v){

	            final int index = position;

	            int len = mTo.length;
	            final HashMap<String, Object> dataMap;
	            if (mDataList == null) {
                    Log.d("SearchView", "Error");
                    return;
                }
	            dataMap = (HashMap) mDataList.get(position);

	            for (int i = 0; i < len; i++) {
	                TextView textView = (TextView) v.findViewById(mTo[i]);
                    textView.setText((String) dataMap.get(mFrom[i]));
//                    Log.d(TAG, "mFrom[" + i + "]: " + mFrom[i] + ", dataMap.get(mFrom[" + i + "]): " + dataMap.get(mFrom[i]));
                    if (i == 0)
	                    textView.setTypeface(engTf);
	                else
	                    textView.setTypeface(cnTf);
	            }
	            v.setOnClickListener(new OnClickListener() {
	                @Override
	                public void onClick(View v) {
                        if(mOnItemClickListener != null) {
                            mOnItemClickListener.onListItemClick(index);
                        }
	                    //((SearchActivity) mContext).showSearchDetail(mSuggestedVocabularies.get(index));
	                }
	            });

	            View iconView = v.findViewById(ICON_ADD_RES_ID);
	            iconView.setOnClickListener(new OnClickListener() {
	                @Override
	                public void onClick(View v) {
                        if(mOnItemClickListener != null) {
                            mOnItemClickListener.onAddIconClick(index);
                        }
	                    //((SearchActivity) mContext).showListDialog(index);
	                }
	            });
	        }
	    }
	}

	private class SearchDetailView extends LinearLayout {

        private Context context;
        private SearchDetailView mContainer;
        private PagerIndexView pagerIndexView;
        private ViewPager viewPager;
        private LinkedList<ViewGroup> mItemPagesList;
        private PopItemDetailAdapter mAdapter;
        private int pageCount;

        public SearchDetailView(Context context) {
            super(context);
            this.context = context;
            mContainer = this;
            setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            pageCount = 0;
            setOrientation(VERTICAL);

        }
        public void setAdapter(PopItemDetailAdapter adapter){
            mAdapter = adapter;
            mAdapter.setChildViewToGroup();
        }

        public PopItemDetailAdapter getAdapter(Context context, HashMap<String, Object> dataMap) {
            return new PopItemDetailAdapter(context, dataMap);
        }

        public void setCurrentPage(int index){
            if(viewPager != null)
                viewPager.setCurrentItem(index);
        }

        private class PopItemDetailAdapter{
            private Context mContext;
            private HashMap<String, Object> mDataMap;
            private LayoutInflater mInflater;

            PopItemDetailAdapter(Context context,
                                 HashMap<String, Object> dataMap){
                mContext = context;
                mDataMap = dataMap;
                mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }

            public void setChildViewToGroup(){
                ViewGroup itemView = (ViewGroup) mInflater.inflate(SEARCH_ITEM_DETAIL_LAYOUT_RES_ID, null);
                ArrayList<String> enSentences, ceSentences;
                viewPager = new ViewPager(mContext);

                if(mDataMap == null)
                    return;
                if(mContainer.getChildCount() != 0)
                    mContainer.removeAllViews();

                ((TextView) itemView
                       .findViewById(LIST_ITEM_DETAIL_CONTENT_TO_FROM_s.VOC_SPELL_DETAIL.getResTo()))
                       .setText((String) mDataMap.get(LIST_ITEM_DETAIL_CONTENT_TO_FROM_s.VOC_SPELL_DETAIL.getResFrom()));
                ((TextView) itemView
                       .findViewById(LIST_ITEM_DETAIL_CONTENT_TO_FROM_s.VOC_TRANSLATION_DETAIL.getResTo()))
                       .setText((String) mDataMap.get(LIST_ITEM_DETAIL_CONTENT_TO_FROM_s.VOC_TRANSLATION_DETAIL.getResFrom()));
                ((TextView) itemView
                        .findViewById(LIST_ITEM_DETAIL_CONTENT_TO_FROM_s.VOC_KK_DETAIL.getResTo()))
                        .setText((String) mDataMap.get(LIST_ITEM_DETAIL_CONTENT_TO_FROM_s.VOC_KK_DETAIL.getResFrom()));
                enSentences = (ArrayList<String>) mDataMap.get(LIST_ITEM_DETAIL_CONTENT_TO_FROM_s.VOC_SENTENCE_DETAIL.getResFrom());
                ceSentences = (ArrayList<String>) mDataMap.get(LIST_ITEM_DETAIL_CONTENT_TO_FROM_s.VOC_SENTENCE_TRANSLATION_DETAIL.getResFrom());

                pageCount = enSentences.size();
                pagerIndexView = new PagerIndexView(context, pageCount);

                ((ViewGroup)itemView.findViewById(SEARCH_ITEM_DETAIL_PAGER_PARENT_LAYOUT_RES_ID)).addView(viewPager);
                ((ViewGroup)itemView.findViewById(SEARCH_ITEM_DETAIL_PAGER_INDEX_PARENT_LAYOUT_RES_ID)).addView(pagerIndexView);
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
                                    .inflate(SEARCH_ITEM_DETAIL_SENTENCE_LAYOUT_RES_ID, null);

                    ((TextView) currentItemDetailsView
                            .findViewById(LIST_ITEM_DETAIL_CONTENT_TO_FROM_s.VOC_SENTENCE_DETAIL.getResTo()))
                            .setText(en_sentenceList.get(i));
                    ((TextView) currentItemDetailsView
                                .findViewById(LIST_ITEM_DETAIL_CONTENT_TO_FROM_s.VOC_SENTENCE_TRANSLATION_DETAIL.getResTo()))
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

        public static final int PAGER_INDEX_SELECTED_COLOR = R.color.player_pager_index_selected;
        public static final int PAGER_INDEX_COLOR = R.color.player_pager_index_color;

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
                    ((GradientDrawable)imageView.getDrawable()).setColor(ContextCompat.getColor(context, R.color.player_pager_index_selected));
                else
                    ((GradientDrawable)imageView.getDrawable()).setColor(ContextCompat.getColor(context, R.color.player_pager_index_color));
                imageView.setPadding(5, 5, 5, 5);
                addView(imageView);
            }
        }
    }

}