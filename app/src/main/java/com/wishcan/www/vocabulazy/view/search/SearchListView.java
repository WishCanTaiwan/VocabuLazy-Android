package com.wishcan.www.vocabulazy.view.search;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.SearchActivity;
import com.wishcan.www.vocabulazy.storage.Vocabulary;
import com.wishcan.www.vocabulazy.vocabulary.WordObject;

/**
 * Created by swallow on 2015/8/10.
 */
public class SearchListView extends ListView {

    private CustomizedSimpleAdapter mAdapter;

    private Context mContext;
    private ArrayList<String> mVocStrList;
    private LinkedList<HashMap<String, Object>> mDataList;

    private ArrayList<Vocabulary> mSuggestedVocabularies;
    private ArrayList<String> mSuggestedVocabularySpells;
    private String[] mFrom = {"voc_name", "voc_category", "voc_translate"};

    private int[] mTo = {R.id.search_result_eng_text_view, R.id.search_result_category_text_view,
            R.id.search_result_cn_text_view};

//    private LinkedList<Vocabulary> mSuggestedVocabularies;
//
//    private LinkedList<String> mSuggestedVocabularySpells;

    private final Typeface engTf;

    private final Typeface cnTf;

    public SearchListView(Context context) {
        this(context, null);
    }

    public SearchListView(Context context, AttributeSet attrs) {

        super(context, attrs);
        mContext = context;
        mVocStrList = new ArrayList<>();
        mSuggestedVocabularies = new ArrayList<>();
        mSuggestedVocabularySpells = new ArrayList<>();

        engTf = Typeface.createFromAsset(getResources().getAssets(), "fonts/tt0142m_.ttf");
        cnTf = Typeface.createFromAsset(getResources().getAssets(), "fonts/DFHeiStd-W5.otf");

        mVocStrList = new ArrayList<>();
        mSuggestedVocabularies = new ArrayList<>();
        mSuggestedVocabularySpells = new ArrayList<>();
        mDataList = new LinkedList<>();
        mAdapter = new CustomizedSimpleAdapter(mContext, mDataList, R.layout.search_list_layout, mFrom, mTo);
        setAdapter(mAdapter);
    }

    public void refresh(ArrayList<Vocabulary> vocabularies) {
        mSuggestedVocabularies = vocabularies;
        createDataList();
        mAdapter = new CustomizedSimpleAdapter(mContext, mDataList, R.layout.search_list_layout, mFrom, mTo);
        setAdapter(mAdapter);
    }

    private void loadVocabularysSpells(ArrayList<Vocabulary> vocabularies) {
        if (mSuggestedVocabularySpells != null)
            mSuggestedVocabularySpells.clear();
        else
            mSuggestedVocabularySpells = new ArrayList<>();

        for (int index = 0; index < vocabularies.size(); index++) {
            Vocabulary vocabulary = vocabularies.get(index);
            mSuggestedVocabularySpells.add(vocabulary.getSpell());
        }
    }

    private void loadVocList(LinkedList<WordObject> wordObjLL){
        Iterator<WordObject> ii = wordObjLL.iterator();
        mVocStrList = new ArrayList<>();
//        while(ii.hasNext()){
//            WordObject wObj = ii.next();
//            mVocStrList.add(wObj.getSpellStr());
//        }
    }

    private void createDataList(){
        Iterator<Vocabulary> ii;
        if (mSuggestedVocabularies == null)
            return;

        ii = mSuggestedVocabularies.iterator();

        mDataList = new LinkedList<>();
        while(ii.hasNext()){
            Vocabulary vocabulary = ii.next();
            HashMap<String, Object> dataMap = new HashMap<>();
            dataMap.put(mFrom[0], vocabulary.getSpell());
            dataMap.put(mFrom[1], "(" + vocabulary.getCategory() + ")");
            dataMap.put(mFrom[2], vocabulary.getTranslate());
            mDataList.add(dataMap);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    class CustomizedSimpleAdapter extends SimpleAdapter{

        private Context mContext;
        private List<?> mDataList;
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
        public CustomizedSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            mContext = context;
            mDataList = data;
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
            if (mDataList == null)
                return;
            dataMap = (HashMap) mDataList.get(position);

            for (int i = 0; i < len; i++) {
                TextView textView = (TextView) v.findViewById(mTo[i]);
                textView.setText((String) dataMap.get(mFrom[i]));
                if (i == 0)
                    textView.setTypeface(engTf);
                else
                    textView.setTypeface(cnTf);
            }
            v.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((SearchActivity) mContext).showSearchDetail(mSuggestedVocabularies.get(index));
                }
            });

            View iconView = v.findViewById(R.id.search_result_adding_to_note_adding_icon);
            iconView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((SearchActivity) mContext).showListDialog(index);
                }
            });
        }
    }
}
