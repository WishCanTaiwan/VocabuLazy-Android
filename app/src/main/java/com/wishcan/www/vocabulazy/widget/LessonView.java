package com.wishcan.www.vocabulazy.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.wishcan.www.vocabulazy.R;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by swallow on 2016/1/13.
 */
public class LessonView extends SlideBackViewPager {

    public interface OnLessonClickListener {
        void onLessonClick(int lesson);
    }

    private Context mContext;
    LessonListView mLessonListView;

    public LessonView(Context context) {
        this(context, null);
    }

    public LessonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    public ViewGroup getMainPage() {
        mLessonListView = new LessonListView(getContext());
        return mLessonListView;
    }

    @Override
    public boolean enableSlideBack() {
        return true;
    }

    public void refreshView(int count, LinkedList<Integer> linkedList) {
        mLessonListView.refreshView(count, linkedList);
    }

    public void setOnLessonClickListener(OnLessonClickListener listener){
        mLessonListView.setOnLessonClickListener(listener);
    }

    private class LessonListView extends ListView implements AdapterView<Integer> {

        @Override
        public void refreshView(int count, LinkedList<Integer> linkedList) {
            mDataList.clear();
            Iterator<Integer> ii = linkedList.iterator();

            String mFromLessonString = "line_item_";
            while(ii.hasNext()){
                HashMap<String, Integer> hm = new HashMap<>();
                for(int i = 0; i < FROM.length && ii.hasNext(); i++)
                    hm.put(mFromLessonString+i ,ii.next());
                mDataList.add(hm);
            }
            refresh();
        }

        private final int LESSON_COUNT                          = 47;
        private final int LIST_ITEM_RES_ID                      = R.layout.lesson_list_view;
        private final int CIRCLE_GREEN_RIPPLE_DRAWABLE_RES_ID   = R.drawable.circle_lesson_green_ripple;
        private final int CIRCLE_YELLOW_RIPPLE_DRAWABLE_RES_ID  = R.drawable.circle_lesson_yellow_ripple;
        private final int CIRCLE_YELLOW_DRAWABLE_RES_ID         = R.drawable.circle_lesson_yellow_shadow;
        private final int CIRCLE_GREEN_DRAWABLE_RES_ID          = R.drawable.circle_lesson_green_shadow;
        private final int DEFAULT_BACKGROUND_COLOR              = R.color.default_color_white;
        private final int DIVIDER_COLOR                         = R.color.divider_color_gray;
        private final int DIVIDER_HEIGHT                        = R.dimen.divider_height;
        private final String[] FROM                             = {"line_item_0", "line_item_1", "line_item_2", "line_item_3", "line_item_4"};
        private final int[] TO                                  = {R.id.lesson_circle_0, R.id.lesson_circle_1, R.id.lesson_circle_2, R.id.lesson_circle_3, R.id.lesson_circle_4};


        private Context mContext;
        private LinkedList<HashMap<String, Integer>> mDataList;
        private int mResource;
        private CustomizedSimpleAdapter mAdapter;
        private OnLessonClickListener mListener;

        public LessonListView(Context context) {
            this(context, null);
        }

        public LessonListView(Context context, AttributeSet attrs) {
            super(context, attrs);

            mContext = context;
            mResource = LIST_ITEM_RES_ID;
            mDataList = new LinkedList<>();
            mAdapter = new CustomizedSimpleAdapter(mContext, mDataList, mResource, FROM, TO);
            setAdapter(mAdapter);

            setFooterDividersEnabled(true);
            setBackgroundColor(ContextCompat.getColor(mContext, DEFAULT_BACKGROUND_COLOR));
            setDivider(new ColorDrawable(ContextCompat.getColor(mContext, DIVIDER_COLOR)));
            setDividerHeight((int) getResources().getDimension(DIVIDER_HEIGHT));
        }

        public void setOnLessonClickListener(OnLessonClickListener listener){
            mListener = listener;
        }

        private void refresh(){
            mAdapter.notifyDataSetChanged();
        }

        private class CustomizedSimpleAdapter extends SimpleAdapter {

            private Context mContext;
            private LinkedList<HashMap<String, Integer>> mData;
            private int mResource;
            private String[] mFrom;
            private int[] mTo;

            private LayoutInflater mInflater;

            public CustomizedSimpleAdapter(Context context, LinkedList<HashMap<String, Integer>> data, int resource, String[] from, int[] to) {
                super(context, data, resource, from, to);
                mContext = context;
                mData = data;
                mResource = resource;
                mFrom = from;
                mTo = to;

                mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                HashMap<String, Integer> hm = mData.get(row);
                for(int i = 0; i < len; i++) {
                    final int lesson = row * len + i;
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
                                mListener.onLessonClick(lesson);
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
}
