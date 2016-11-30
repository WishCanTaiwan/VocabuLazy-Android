package wishcantw.vocabulazy.activities.search.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.activities.search.activity.SearchActivity;
import wishcantw.vocabulazy.widget.AdapterView;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * @since 2016/8/31
 */
public class SearchListView extends ListView implements AdapterView<HashMap> {
    private static final int SEARCH_LIST_ITEM_RES_ID = R.layout.view_search_list_item;

    public static final int IDX_VOC_SPELL = 0x0;
    public static final int IDX_VOC_TRANSLATION = 0x1;
    public static final int IDX_VOC_CATEGORY = 0x2;

    public static final String[] LIST_ITEM_CONTENT_FROM = {
            "voc_spell", "voc_translation", "voc_category"
    };
    public static final int[] LIST_ITEM_CONTENT_TO = {
            R.id.search_result_eng_text_view, R.id.search_result_cn_text_view, R.id.search_result_category_text_view
    };

    private LinkedList<HashMap<String, ?>> mDataList;
    private CustomizedSimpleAdapter mAdapter;

    public SearchListView(Context context) {
        this(context, null);
    }

    public SearchListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mDataList = new LinkedList<>();
        mAdapter = new CustomizedSimpleAdapter(context, mDataList, SEARCH_LIST_ITEM_RES_ID, LIST_ITEM_CONTENT_FROM, LIST_ITEM_CONTENT_TO);
        setAdapter(mAdapter);
    }

    @Override
    public void refreshView(int count, LinkedList<HashMap> dataList) {
        if(dataList == null) {
            return;
        }

        mDataList.clear();
        for(HashMap ii:dataList){
            HashMap<String, Object> hm = new HashMap<>();
            for (int i = 0; i < LIST_ITEM_CONTENT_FROM.length && i < LIST_ITEM_CONTENT_TO.length; i++) {
                hm.put(LIST_ITEM_CONTENT_FROM[i], ii.get(LIST_ITEM_CONTENT_FROM[i]));
            }
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
            final HashMap<String, Object> dataMap;
            if (mDataList == null) {
                return;
            }
            dataMap = (HashMap) mDataList.get(position);

            for (int i = 0; i < len; i++) {
                TextView textView = (TextView) v.findViewById(mTo[i]);
                textView.setText((String) dataMap.get(mFrom[i]));
            }
        }
    }
}
