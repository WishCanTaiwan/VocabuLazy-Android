package com.wishcan.www.vocabulazy.search.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;

import com.wishcan.www.vocabulazy.widget.AdapterView;
import com.wishcan.www.vocabulazy.R;

import java.util.ArrayList;

public class SearchAddVocToNoteRadioGroup extends RadioGroup implements AdapterView<ArrayList> {

    private static final int SEARCH_ADD_VOC_TO_NOTE_LIST_ITEM_RES_ID = R.layout.view_search_add_voc_to_note_radio_button;
    
    public static final String[] LIST_ITEM_CONTENT_FROM = {
            "note_name"
    };
    public static final int[] LIST_ITEM_CONTENT_TO = {
            R.id.search_note_name
    };
    
    private Context mContext;
    private ArrayList<String> mDataList;
    private CustomizedSimpleAdapter mAdapter;
    
    public SearchAddVocToNoteListView(Context context) {
        this(context, null);
    }

    public SearchAddVocToNoteListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        mDataList = new ArrayList<>();
        mAdapter = new CustomizedSimpleAdapter(context, mDataList, SEARCH_ADD_VOC_TO_NOTE_LIST_ITEM_RES_ID, LIST_ITEM_CONTENT_FROM, LIST_ITEM_CONTENT_TO);
        setAdapter(mAdapter);
    }
    
    @Override
    public void refreshView(int count, ArrayList<String> dataList) {
        if(dataList == null) {
            return;
        }

        mDataList.clear();
        mDataList = dataList;
        refresh();
    }
    
    public void setAdapter(SimpleAdapter adatper) {
        removeAllViews();
        for (int i = 0; i < mDataList.length; i++) {
            addView(adapter.getView(i, null, this));
        }
    }
    
    private void refresh() {
        mAdapter.notifyDataSetChanged();
    }
    
    class CustomizedSimpleAdapter extends SimpleAdapter {

        private Context mContext;
        private ArrayList<String> mDataList;
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
        public CustomizedSimpleAdapter(Context context, ArrayList<String> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            mContext = context;
            mDataList = data;
            mResource = resource;
            mFrom = from;
            mTo = to;

            mInflater = ((SearchActivity) mContext).getLayoutInflater();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return createViewFromResource(position, convertView, parent , mResource);
        }

        private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource){
            View v;
            if(convertView == null) {
                v = mInflater.inflate(resource, parent, false);
            }
            else {
                v = convertView;
            }
            bindView(position, v);
            return v;
        }

        private void bindView(int position, View v){
            int len = mTo.length;
            String string;
            if (mDataList == null || len != null) {
                return;
            }
            string = mDataList.get(position);
            
            if (v instanceof RadioButton) {
                ((RadioButton) v).setText(string);
            }
        }
    }
}