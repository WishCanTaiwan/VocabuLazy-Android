package com.wishcan.www.vocabulazy.view.notes;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.wishcan.www.vocabulazy.MainActivity;
import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.Lesson;

/**
 * Created by swallow on 2015/8/5.
 */
public class NotesListView extends ListView {

    public interface OnListIconClickedListener {
        void onListIconClicked(int iconId, int position, View v);
    }

    public static final int ICON_PLAY = 0;
    public static final int ICON_ETC = 1;
    public static final int ICON_ETC_CLOSE = 2;
    public static final int ICON_DEL = 3;
    public static final int ICON_RENAME = 4;
    public static final int ICON_COMBINE = 5;
    public static final int ICON_NEW_NOTE = 6;

    private static final int DEFAULT_ACTION_BAR_TITLE_RES_ID = R.string.list_title;

    private static final int DIVIDER_COLOR = R.color.divider_color_gray;

    private static final int DIVIDER_HEIGHT = R.dimen.divider_height;

    private static final int LIST_ITEM_RES_ID = R.layout.widget_note_view_list_item;

    private static final int LIST_ITEM_ANIMATE_MOVE_OFFSET = R.dimen.note_list_item_move_offset;

    private String[] from = {"NOTE_NAME"};

    private int[]   to = {R.id.note_name};

    private int mResource;

    private Context mContext;

    private ArrayList<Lesson> mNotes;

    private ArrayList<String> mNoteNames;

    private LinkedList<HashMap<String, Object>> mDataList;

    private String mActionBarTitle;

    private ArrayAdapter mAdapter;

    private Database mDatabase;

    private OnListIconClickedListener mOnListIconClickedListener;

    private boolean mEnableEtcFunction;

    public NotesListView(Context context) {
        this(context, null);
    }

    public NotesListView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);

        mContext = context;

        mActionBarTitle = mContext.getString(DEFAULT_ACTION_BAR_TITLE_RES_ID);

        mResource = LIST_ITEM_RES_ID;

        mDatabase = ((MainActivity) context).getDatabase();

        loadNotes();
        loadNoteNames();
        createDataList();

        setBackgroundColor(Color.WHITE);
        setDivider(new ColorDrawable(getResources().getColor(DIVIDER_COLOR)));
        setDividerHeight((int) getResources().getDimension(DIVIDER_HEIGHT));

        setEnableEtcFunction(true);

        mAdapter = new CustomizedSimpleAdapter(mContext, mDataList, mResource, from, to);
        setAdapter(mAdapter);


    }

    public void setOnListIconClickedListener(OnListIconClickedListener listener) {
        mOnListIconClickedListener = listener;
    }

    public void refreshDatabase() {
        mDatabase = ((MainActivity) mContext).getDatabase();
    }

    public void refresh(){

        loadNotes();
        loadNoteNames();
        createDataList();
        mAdapter.notifyDataSetChanged();
//        mAdapter = new CustomizedSimpleAdapter(mContext, mDataList, mResource, from, to);
//        setAdapter(mAdapter);
    }

    private void loadNotes() {
        mNotes = new ArrayList<>();
        mNotes = mDatabase.getLessonsByBook(-1);
    }

    private void loadNoteNames() {
        mNoteNames = new ArrayList<>();
        for (int index = 0; index < mNotes.size(); index++) {
            mNoteNames.add(mNotes.get(index).getName());
//            Log.d("loadNotesName", " " + mNotes.get(index).getName());
        }
    }

    private void createDataList(){
        if (mDataList == null)
            mDataList = new LinkedList<>();
        else {
            mDataList.clear();
        }

        Iterator<String> ii = mNoteNames.iterator();
        while(ii.hasNext()){
            HashMap<String, Object> hm = new HashMap<>();
            hm.put(from[0], ii.next());
            mDataList.add(hm);
        }
    }

    public void setEnableEtcFunction(boolean bool){
        mEnableEtcFunction = bool;
    }

    public ArrayList<Lesson> getNotes() {
        return mNotes;
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
            final View mNoteNameTextView = v.findViewById(mTo[0]);
            final View mParentView = v.findViewById(R.id.note_name_parent);
            final View mNoteEtcFunctionView = v.findViewById(R.id.action_note_etc);
            final View mNoteEtcParentView = v.findViewById(R.id.action_note_parent);
            final View mNoteEtcCloseFunctionView = v.findViewById(R.id.action_note_etc_close);
            final View mNoteRenameFunctionView = v.findViewById(R.id.action_note_rename);
            final View mNoteDeleteFunctionView = v.findViewById(R.id.action_note_delete);
            final View mNoteCombineFunctionView = v.findViewById(R.id.action_note_combine);

            final int animateMoveOffset = (int) getResources().getDimension(LIST_ITEM_ANIMATE_MOVE_OFFSET);

            HashMap<String, Object> dataMap;

            if(mOnListIconClickedListener != null) {
                v.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnListIconClickedListener.onListIconClicked(ICON_PLAY, position, v);
                    }
                });
            }

            //**mOnListIconClickedListener.onListIconClicked(ICON_NEW_NOTE, position, v);*/
            if(position < mData.size())
                dataMap = (HashMap<String, Object>) mData.get(position);
            else
                return;
            for(int i = 0; i < len; i++){
                view = v.findViewById(mTo[i]);
                if(view instanceof TextView)
                    ((TextView) view).setText((String) dataMap.get(mFrom[i]));

            }

            if(mEnableEtcFunction && mOnListIconClickedListener != null) {
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
                        mOnListIconClickedListener.onListIconClicked(ICON_ETC, position, v);
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
                        mOnListIconClickedListener.onListIconClicked(ICON_ETC_CLOSE, position, v);
                    }
                });

                mNoteDeleteFunctionView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnListIconClickedListener.onListIconClicked(ICON_DEL, position, mNoteNameTextView);
                    }
                });

                mNoteRenameFunctionView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnListIconClickedListener.onListIconClicked(ICON_RENAME, position, v);
                    }
                });

                mNoteCombineFunctionView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnListIconClickedListener.onListIconClicked(ICON_COMBINE, position, v);
                    }
                });
            }
        }

        @Override
        public int getCount() {
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
