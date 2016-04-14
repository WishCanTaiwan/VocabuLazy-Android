package com.wishcan.www.vocabulazy.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wishcan.www.vocabulazy.R;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by swallow on 2015/8/5.
 */

abstract public class NoteView extends SlideBackViewPager{

    public static final String TAG = NoteView.class.getSimpleName();

    public interface OnListIconClickListener {
        void onListIconClick(int iconId, int position, View v);
    }
    public static final int ICON_PLAY = 0;
    public static final int ICON_ETC = 1;
    public static final int ICON_ETC_CLOSE = 2;
    public static final int ICON_DEL = 3;
    public static final int ICON_RENAME = 4;
    public static final int ICON_COMBINE = 5;
    public static final int ICON_NEW = 6;

    private RelativeLayout mContainer;
    private NoteListView mNoteListView;
    private NoteNewButton mNoteNewButton;

    public NoteView(Context context) {
        this(context, null);
    }

    public NoteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mNoteListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == SCROLL_STATE_FLING) {

                }
                else if(scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    mNoteNewButton.hideButton();
                }
                else if(scrollState == SCROLL_STATE_IDLE) {
                    mNoteNewButton.showButton();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    public void refreshView(int count, LinkedList<String> linkedList) {
        mNoteListView.refreshView(count, linkedList);
    }

    public void setOnListIconClickListener(OnListIconClickListener listener) {
        mNoteListView.setOnListIconClickListener(listener);
        mNoteNewButton.setOnListIconClickListener(listener);
    }

    public void setEnableEtcFunction(boolean bool) {
        mNoteListView.setEnableEtcFunction(bool);
    }

    public void setEnableNewFunction(boolean bool) {
        if(!bool)
            mNoteNewButton.setVisibility(GONE);
        else
            mNoteNewButton.setVisibility(VISIBLE);
    }

    @Override
    public ViewGroup getMainPage() {
        mContainer = new RelativeLayout(getContext());
        mNoteListView = new NoteListView(getContext());
        mNoteListView.setLayoutParams(new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mNoteNewButton = new NoteNewButton(getContext());
        mNoteNewButton.setVisibility(GONE);
        int layoutMargin = (int)getResources().getDisplayMetrics().density * 10;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.setMargins(0, 0, layoutMargin, layoutMargin);
        mNoteNewButton.setLayoutParams(layoutParams);

        mContainer.addView(mNoteListView);
        mContainer.addView(mNoteNewButton);
        return mContainer;
    }

    private class NoteListView extends ListView implements AdapterView<String>{

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

        private static final int DIVIDER_COLOR = R.color.widget_note_divider;
        private static final int DIVIDER_HEIGHT = R.dimen.widget_note_divider_height;
        private static final int LIST_ITEM_RES_ID = R.layout.widget_note_view_list_item;
        private static final int LIST_ITEM_ANIMATE_MOVE_OFFSET = R.dimen.widget_note_list_anim_offset;

        private final String[] FROM = {"NOTE_NAME"};
        private final int[] TO = {R.id.note_name};
        private LinkedList<HashMap<String, Object>> mDataList;
        private CustomizedSimpleAdapter mAdapter;
        private OnListIconClickListener mOnListIconClickListener;
        private boolean mEnableEtcFunction;

        private NoteListView mSelfNoteListView;

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
            mSelfNoteListView = this;
        }

        public void setOnListIconClickListener(OnListIconClickListener listener) {
            mOnListIconClickListener = listener;
        }

        public void setEnableEtcFunction(boolean bool){
            mEnableEtcFunction = bool;
        }

        private void onEtcCloseIconClick(int position){

            View parentView = mSelfNoteListView.getChildAt(position);
            View iconView;
            if(parentView != null){
                iconView = parentView.findViewById(R.id.action_note_etc_close);
                if(iconView != null) {
                    iconView.performClick();
                }
            }
        }

        private void refresh(){
            mAdapter.resetState();
            mAdapter.notifyDataSetChanged();
        }

        private class CustomizedSimpleAdapter extends ArrayAdapter {

            private Context mContext;
            private List<?> mData;
            private int mResource;
            private String[] mFrom;
            private int[] mTo;

            private LayoutInflater mInflater;
            private int mOpenIndex;

            public CustomizedSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
                super(context, resource);
                mContext = context;
                mData = data;
                mResource = resource;
                mFrom = from;
                mTo = to;

                mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                mOpenIndex = -1;
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
                final View mEtcParentView = v.findViewById(R.id.note_etc_parent);
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
                            Log.d(TAG, "onEtcClick");
                            mEtcParentView.setVisibility(VISIBLE);
                            float startX = mParentView.getX();
                            float endX;
                            if (startX >= 0)
                                endX = startX - animateMoveOffset;
                            else
                                endX = startX + animateMoveOffset;
                            handleListSlidingAnimation(mParentView, mNoteEtcFunctionView, startX, endX, null);
                            mOnListIconClickListener.onListIconClick(ICON_ETC, position, v);
                            if(mOpenIndex >= 0 && mOpenIndex != position) {
                                mSelfNoteListView.onEtcCloseIconClick(mOpenIndex);
                                mOpenIndex = position;
                            }
                            else if(mOpenIndex >= 0 && mOpenIndex == position)
                                mOpenIndex = -1;
                            else
                                mOpenIndex = position;
                        }
                    });


                    mNoteEtcCloseFunctionView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View iconView) {
                            Log.d(TAG, "onCloseClick");
                            float startX = mParentView.getX();
                            float endX;
                            if (startX >= 0)
                                endX = startX - animateMoveOffset;
                            else
                                endX = startX + animateMoveOffset;
                            Animator.AnimatorListener slidingAnimatorListener = new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    mEtcParentView.setVisibility(GONE);
                                }
                                @Override
                                public void onAnimationStart(Animator animation) {}
                                @Override
                                public void onAnimationCancel(Animator animation) {}
                                @Override
                                public void onAnimationRepeat(Animator animation) {}
                            };

                            handleListSlidingAnimation(mParentView, mNoteEtcFunctionView, startX, endX, slidingAnimatorListener);
                            mOnListIconClickListener.onListIconClick(ICON_ETC_CLOSE, position, v);
                            mOpenIndex = -1;
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

            public void resetState() {
                mOpenIndex = -1;
            }

            private void handleListSlidingAnimation(View v, View iconView, float startX, float endX, Animator.AnimatorListener listener) {
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
                if(listener != null){
                    set.addListener(listener);
                }
                set.start();
            }
        }
    }

    private class NoteNewButton extends RelativeLayout {

        private static final int BACKGROUND_RES_ID = R.drawable.circle_add_button_yellow;
        private Animator mShowingAnimator, mHidingAnimator;
        private OnListIconClickListener mListener;

        public NoteNewButton(Context context) {
            this(context, null);
        }

        public NoteNewButton(Context context, AttributeSet attrs) {
            super(context, attrs);
            setBackgroundResource(BACKGROUND_RES_ID);
            CrossView crossView = new CrossView(context);
            crossView.setCrossSize((int)getResources().getDisplayMetrics().density * 20);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(CENTER_IN_PARENT);
            crossView.setLayoutParams(layoutParams);
            addView(crossView);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                setElevation(30);

            mShowingAnimator = ObjectAnimator.ofFloat(this, "translationY", 960f, 0f);
            mShowingAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            mShowingAnimator.setDuration(200);
            mShowingAnimator.setStartDelay(150);

            mHidingAnimator = ObjectAnimator.ofFloat(this, "translationY", 0f, 960f);
            mHidingAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            mHidingAnimator.setDuration(200);

            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null)
                        mListener.onListIconClick(ICON_NEW, -1, v);
                }
            });
        }

        public void showButton() {
            mShowingAnimator.start();
        }

        public void hideButton() {
            mHidingAnimator.start();
        }

        public void setOnListIconClickListener(OnListIconClickListener listener) {
            mListener = listener;
        }
    }
}

