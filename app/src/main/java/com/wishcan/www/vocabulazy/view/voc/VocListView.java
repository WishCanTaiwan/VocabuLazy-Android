package com.wishcan.www.vocabulazy.view.voc;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.Lesson;
import com.wishcan.www.vocabulazy.storage.Vocabulary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by swallow on 2015/11/22.
 */
public class VocListView extends ListView{

    private static final int LIST_ITEM_RES_ID = R.layout.voc_list_item_layout;

    private Database mDatabase;

    private ArrayAdapter mAdapter;

    private LinkedList<HashMap<String, Object>> mDataList;

    private ArrayList<Lesson> mBookList;

    private ArrayList<Vocabulary> mVocList;

    private String[] mFrom = {"VOC_SPELL", "VOC_SPELL_HAN", "VOC_TRANSLATION"};

    private int[] mTo = {R.id.voc_spell, R.id.voc_spell_han, R.id.voc_translation};

    public VocListView(Context context) {
        this(context, null);
    }

    public VocListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mDatabase = new Database(context);
        loadVoc();
        createDataList();
        mAdapter = new CustomizedSimpleAdapter(context, mDataList, LIST_ITEM_RES_ID, mFrom, mTo);
        setAdapter(mAdapter);

    }

    private void loadVoc() {
        ArrayList<Integer> contentIDList = mDatabase.getContentIDs(0, 0);
        mVocList = mDatabase.getVocabulariesByIDs(contentIDList);

    }

    private void createDataList(){
        if(mDataList == null)
            mDataList = new LinkedList<>();
        else {
            mDataList.clear();
        }

        Iterator<Vocabulary> ii = mVocList.iterator();
        while(ii.hasNext()){
            Vocabulary vocabulary = ii.next();
            HashMap<String, Object> hm = new HashMap<>();
            hm.put(mFrom[0], vocabulary.getSpell());
            hm.put(mFrom[1], vocabulary.getKK());
            hm.put(mFrom[2], vocabulary.getTranslationInOneString());
            mDataList.add(hm);
        }
    }

    private class CustomizedSimpleAdapter extends ArrayAdapter{

        private Context mContext;
        private List<?> mData;
        private int mResource;
        private String[] mFrom;
        private int[] mTo;

        private LayoutInflater mInflater;

        public CustomizedSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, resource);
            mContext = context;
            mData = data;
            mResource = resource;
            mFrom = from;
            mTo = to;

            mInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return createViewFromResource(position, convertView, parent, mResource);
        }

        private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource){

            View v;
            v = mInflater.inflate(resource, parent, false);

            bindView(position, v);
            return v;
        }

        private void bindView(final int position, final View v) {

            int len = mTo.length;
            View view;

            HashMap<String, Object> dataMap;
            dataMap = (HashMap<String, Object>) mData.get(position);

            for(int i = 0; i < len; i++){
                view = v.findViewById(mTo[i]);
                if(view instanceof TextView)
                    ((TextView) view).setText((String) dataMap.get(mFrom[i]));

            }
        }

        @Override
        public int getCount() {
            return mData.size();
        }
    }

}
