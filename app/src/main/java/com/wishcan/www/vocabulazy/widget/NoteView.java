package com.wishcan.www.vocabulazy.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.wishcan.www.vocabulazy.R;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by swallow on 2015/8/5.
 */

abstract public class NoteView extends SlideBackViewPager{

    public interface OnListIconClickListener {
        void onListIconClick(int iconId, int position, View v);
    }
    public static final int ICON_PLAY = 0;
    public static final int ICON_ETC = 1;
    public static final int ICON_ETC_CLOSE = 2;
    public static final int ICON_DEL = 3;
    public static final int ICON_RENAME = 4;
    public static final int ICON_COMBINE = 5;

    private NoteListView mNoteListView;

    public NoteView(Context context) {
        this(context, null);
    }

    public NoteView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void refreshView(int count, LinkedList<String> linkedList){
        mNoteListView.refreshView(count, linkedList);
    }

    public void setOnListIconClickListener(OnListIconClickListener listener) {
        mNoteListView.setOnListIconClickListener(listener);
    }

    @Override
    public ViewGroup getMainPage() {
        mNoteListView = new NoteListView(getContext());
        return mNoteListView;
    }



    public class NoteListView extends ListView implements AdapterView<String>{

        @Override
        public void refreshView(int count, LinkedList<String> linkedList) {

            if(linkedList == null)
                return;

            mDataList.clear();
            for(String ii:linkedList){
                HashMap<String, Object> hm = new HashMap<>();
                hm.put(FROM[0], ii);
                mDataList.add(hm);
            }
            refresh();
        }


        private static final int DIVIDER_COLOR = R.color.divider_color_gray;
        private static final int DIVIDER_HEIGHT = R.dimen.divider_height;
        private static final int LIST_ITEM_RES_ID = R.layout.widget_note_view_list_item;
        private static final int LIST_ITEM_ANIMATE_MOVE_OFFSET = R.dimen.note_list_item_move_offset;

        private final String[] FROM = {"NOTE_NAME"};
        private final int[] TO = {R.id.note_name};
        private LinkedList<HashMap<String, Object>> mDataList;
        private ArrayAdapter mAdapter;
        private OnListIconClickListener mOnListIconClickListener;
        private boolean mEnableEtcFunction;

        public NoteListView(Context context) {
            this(context, null);
        }

        public NoteListView(Context context, AttributeSet attrs) {
            super(context, attrs, 0);

            setBackgroundColor(Color.WHITE);
            setDivider(new ColorDrawable(ContextCompat.getColor(context, DIVIDER_COLOR)));
            setDividerHeight((int) getResources().getDimension(DIVIDER_HEIGHT));

            setEnableEtcFunction(true);

            mDataList = new LinkedList<>();
            mAdapter = new CustomizedSimpleAdapter(context, mDataList, LIST_ITEM_RES_ID, FROM, TO);
            setAdapter(mAdapter);

        }

        public void setOnListIconClickListener(OnListIconClickListener listener) {
            mOnListIconClickListener = listener;
        }

        private void refresh(){
            mAdapter.notifyDataSetChanged();
        }

        public void setEnableEtcFunction(boolean bool){
            mEnableEtcFunction = bool;
        }

        private class CustomizedSimpleAdapter extends ArrayAdapter {

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

                mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

                if(mData == null)
                    return;

                int len = mTo.length;
                View view;
                final View mNoteNameTextView = v.findViewById(mTo[0]);
                final View mParentView = v.findViewById(R.id.note_name_parent);
                final View mNoteEtcFunctionView = v.findViewById(R.id.action_note_etc);
                final View mNoteEtcParentView = v.findViewById(R.id.action_note_parent);
                final View mNoteEtcCloseFunctionView = v.findViewById(R.id.action_note_etc_close);
                final View mNoteRenameFunctionView = v.findViewById(R.id.action_note_rename);
                final View mNoteDeleteFunctionView = v.findViewById(R.id.action_note_delete);
                final View mNoteCombineFunctionView = v.findViewById(R.id.action_note_combine);

                final int animateMoveOffset = (int) getResources().getDimension(LIST_ITEM_ANIMATE_MOVE_OFFSET);

                HashMap<String, Object> dataMap = null;

                if(mOnListIconClickListener != null) {
                    v.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnListIconClickListener.onListIconClick(ICON_PLAY, position, v);
                        }
                    });
                }

                if(position < mData.size()) {
                    if (mData.get(position) instanceof HashMap)
                        dataMap = (HashMap<String, Object>) mData.get(position);
                    if (dataMap == null)
                        return;
                }
                else
                    return;

                for(int i = 0; i < len; i++){
                    view = v.findViewById(mTo[i]);
                    Object obj = dataMap.get(mFrom[i]);
                    if(view instanceof TextView && obj instanceof String)
                        ((TextView) view).setText((String) obj);

                }

                if(mEnableEtcFunction && mOnListIconClickListener != null) {
                    mNoteEtcParentView.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View iconView) {
                            float startX = mParentView.getX();
                            float endX;
                            if (startX >= 0)
                                endX = startX - animateMoveOffset;
                            else
                                endX = startX + animateMoveOffset;
                            handleListSlidingAnimation(mParentView, mNoteEtcFunctionView, startX, endX);
                            mOnListIconClickListener.onListIconClick(ICON_ETC, position, v);
                        }
                    });


                    mNoteEtcCloseFunctionView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View iconView) {
                            View mParentView = v.findViewById(R.id.note_name_parent);
                            float startX = mParentView.getX();
                            float endX;
                            if (startX >= 0)
                                endX = startX - animateMoveOffset;
                            else
                                endX = startX + animateMoveOffset;

                            handleListSlidingAnimation(mParentView, mNoteEtcFunctionView, startX, endX);
                            mOnListIconClickListener.onListIconClick(ICON_ETC_CLOSE, position, v);
                        }
                    });

                    mNoteDeleteFunctionView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnListIconClickListener.onListIconClick(ICON_DEL, position, mNoteNameTextView);
                        }
                    });

                    mNoteRenameFunctionView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnListIconClickListener.onListIconClick(ICON_RENAME, position, v);
                        }
                    });

                    mNoteCombineFunctionView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnListIconClickListener.onListIconClick(ICON_COMBINE, position, v);
                        }
                    });
                }
            }

            @Override
            public int getCount() {
                if(mData == null)
                    return 0;
                return mData.size();
            }

            private void handleListSlidingAnimation(View v, View iconView, float startX, float endX) {
                AnimatorSet set = new AnimatorSet();
                ValueAnimator mNoteEtcViewAnim;
                if (iconView.getRotation() == 0)
                    mNoteEtcViewAnim = ObjectAnimator.ofFloat(iconView, "Rotation", 0f, 180f);
                else
                    mNoteEtcViewAnim = ObjectAnimator.ofFloat(iconView, "Rotation", 180f, 0f);
                ValueAnimator mNoteNameParentViewAnim = ObjectAnimator.ofFloat(v, "x", startX, endX);
                set.play(mNoteNameParentViewAnim).with(mNoteEtcViewAnim);
                set.setDuration(300);
                set.setInterpolator(new AccelerateDecelerateInterpolator());
                set.start();
            }
        }
    }
}

