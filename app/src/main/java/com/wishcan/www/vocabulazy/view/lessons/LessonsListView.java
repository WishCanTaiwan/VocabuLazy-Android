package com.wishcan.www.vocabulazy.view.lessons;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.wishcan.www.vocabulazy.R;

/**
 * Created by swallow on 2015/8/17.
 */
public class LessonsListView extends ListView {

    public interface OnLessonClickedListener {
        void onLessonClicked(int lessson);
    }

    private static final String TAG = LessonsListView.class.getSimpleName();

    private static final int LESSON_COUNT = 47;

    private static final int LIST_ITEM_RES_ID = R.layout.lesson_list_view;

    protected static final int CIRCLE_GREEN_RIPPLE_DRAWABLE_RES_ID = R.drawable.circle_lesson_green_ripple;

    protected static final int CIRCLE_YELLOW_RIPPLE_DRAWABLE_RES_ID = R.drawable.circle_lesson_yellow_ripple;

    protected static final int CIRCLE_YELLOW_DRAWABLE_RES_ID = R.drawable.circle_lesson_yellow_shadow;

    protected static final int CIRCLE_GREEN_DRAWABLE_RES_ID = R.drawable.circle_lesson_green_shadow;

    protected static final int DEFAULT_BACKGROUND_COLOR = R.color.default_color_white;

    protected static final int DIVIDER_COLOR = R.color.divider_color_gray;

    protected static final int DIVIDER_HEIGHT = R.dimen.divider_height;

    /**
     * Typically mContext is MainActivity
     * */
    private Context mContext;


    private LinkedList<Integer> mLessonList;
    /**
     * mDataList will get Lessons from Books
     * */
    private LinkedList<HashMap<String, Integer>> mDataList;

    /**
     * mResource will be the ViewGroup, lesson_list_view
     * */
    private int mResource;

    /**
     *
     * */
    private String[] mFrom = {"lesson_0", "lesson_1", "lesson_2", "lesson_3", "lesson_4"};

    /**
     *
     * */
    private int[] mTo = {R.id.lesson_circle_0, R.id.lesson_circle_1, R.id.lesson_circle_2, R.id.lesson_circle_3, R.id.lesson_circle_4};

    private CustomizedSimpleAdapter mAdapter;

    private OnLessonClickedListener mListener;

    public LessonsListView(Context context) {
        this(context, null);
    }

    public LessonsListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        mResource = LIST_ITEM_RES_ID;
        loadLessonList();
        createDataList();

        mAdapter = new CustomizedSimpleAdapter(mContext, mDataList, mResource, mFrom, mTo);
        setAdapter(mAdapter);

        setBackgroundColor(getResources().getColor(DEFAULT_BACKGROUND_COLOR));

        setFooterDividersEnabled(true);
        setDivider(new ColorDrawable(getResources().getColor(DIVIDER_COLOR)));
        setDividerHeight((int) getResources().getDimension(DIVIDER_HEIGHT));
    }

    public void setAdapterByIntLL(LinkedList<Integer> ll) {
        mLessonList = ll;
        createDataList();
        mAdapter = new CustomizedSimpleAdapter(mContext, mDataList, mResource, mFrom, mTo);
        setAdapter(mAdapter);
    }

    public void setOnLessonClickedListener(OnLessonClickedListener listener) {
        mListener = listener;
    }


    private void loadLessonList(){

        mLessonList = new LinkedList();
        for(int i = 0; i < LESSON_COUNT; i++)
            mLessonList.add(i);

    }

    private void createDataList(){

        mDataList = new LinkedList<>();
        Iterator<Integer> ii = mLessonList.iterator();
        String mFromLessonString = "lesson_";
        while(ii.hasNext()){
            HashMap<String, Integer> hm = new HashMap<>();
            for(int i = 0; i < mFrom.length && ii.hasNext(); i++)
                hm.put(mFromLessonString+i ,ii.next());
            mDataList.add(hm);
        }

    }


    private class CustomizedSimpleAdapter extends SimpleAdapter {

        private Context mContext;
        private List<?> mData;
        private int mResource;
        private String[] mFrom;
        private int[] mTo;

        private LayoutInflater mInflater;

        public CustomizedSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            mContext = context;
            mData = data;
            mResource = resource;
            mFrom = from;
            mTo = to;

            mInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int row, View convertView, ViewGroup parent) {
            return createViewFromResource(row, convertView, parent, mResource);
        }

        private View createViewFromResource(int row, View convertView, ViewGroup parent, int resource) {

            View v;
            v = mInflater.inflate(resource, parent, false);

            bindView(row, v);
            return v;
        }

        private void bindView(int row, final View v) {

            int len = mTo.length;
            HashMap<String, Integer> hm = mDataList.get(row);
            Log.d(TAG, "mDataListSize " + mDataList.size());
            for(int i = 0; i < len; i++) {
                final int lesson = row * len + i;
                Log.d(TAG, "Lesson " + lesson);
                ViewGroup lessonParentView = (ViewGroup) v.findViewById(mTo[i]);

                ImageView imageView = (ImageView) lessonParentView.getChildAt(0);
                TextView textView = (TextView) lessonParentView.getChildAt(1);
                if(i < hm.size()) {
                    textView.setText("" + hm.get(mFrom[i]));
                    if(row%2 == 0) {
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                            imageView.setImageResource(CIRCLE_GREEN_RIPPLE_DRAWABLE_RES_ID);
                        else
                            imageView.setImageResource(CIRCLE_GREEN_DRAWABLE_RES_ID);
                    }
                    else {
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                            imageView.setImageResource(CIRCLE_YELLOW_RIPPLE_DRAWABLE_RES_ID);
                        else
                            imageView.setImageResource(CIRCLE_YELLOW_DRAWABLE_RES_ID);
                    }
                    imageView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mListener.onLessonClicked(lesson);
                        }
                    });
                }
                else{
                    lessonParentView.setVisibility(GONE);
                }
            }

        }
    }

}
