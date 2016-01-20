package com.wishcan.www.vocabulazy.search.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wishcan.www.vocabulazy.R;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by swallow on 2016/1/20.
 */
public class SearchDialogNoteShowingView extends LinearLayout {

    private static final int VIEW_LIST_HEADER_RES_ID = R.layout.dialog_add_to_note_title;
    private static final int VIEW_RES_ID = R.layout.dialog_search_add_to_note;
    private static final int DIMEN_VIEW_WIDTH = R.dimen.search_result_add_to_note_dialog_width;
    private static final int DIMEN_VIEW_HEIGHT = R.dimen.search_result_add_to_note_dialog_height;

    private View mHeaderView;
    private SearchDialogListView mListView;
    private LayoutParams mLayoutParams;

    public SearchDialogNoteShowingView(Context context) {
        this(context, null);
    }

    public SearchDialogNoteShowingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        int width = (int) getResources().getDimension(DIMEN_VIEW_WIDTH);
        int height = (int) getResources().getDimension(DIMEN_VIEW_HEIGHT);
        mLayoutParams = new LayoutParams(width, height);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setOrientation(VERTICAL);

        mHeaderView = ((LayoutInflater)context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE)).inflate(VIEW_LIST_HEADER_RES_ID, null);
        mListView = new SearchDialogListView(context);

//        addView(((LayoutInflater)context.getSystemService
//                (Context.LAYOUT_INFLATER_SERVICE)).inflate(VIEW_LIST_HEADER_RES_ID, null));
        addView(mHeaderView);
        addView(mListView);
    }

    public void refreshDataList(LinkedList<String> linkedList) {
        mListView.refreshDataList(linkedList);
    }

    public LayoutParams getLayoutParams() {
        return mLayoutParams;
    }

    private class SearchDialogListView extends ListView {

        private static final int VIEW_LIST_ITEM_RES_ID = R.layout.search_result_add_to_note_list_item_view;
        private static final int VIEW_NOTE_TEXT_RES_ID = R.id.note_list_name;
        private static final int VIEW_ADD_NEW_NOTE_RES_ID = R.id.search_result_adding_to_note_adding_function;

        private MyAdapter mAdapter;
        private LinkedList<String> mLinkedList;

        public SearchDialogListView(Context context) {
            this(context, null);
        }

        public SearchDialogListView(Context context, AttributeSet attrs) {
            super(context, attrs);
            mLinkedList = new LinkedList<>();
            mAdapter = new MyAdapter(context, VIEW_LIST_ITEM_RES_ID, VIEW_NOTE_TEXT_RES_ID, VIEW_ADD_NEW_NOTE_RES_ID, mLinkedList);
            setAdapter(mAdapter);
        }

        public void refreshDataList(LinkedList<String> linkedList) {
            if(linkedList == null)
                return;

            mLinkedList.clear();
            for(String str : linkedList) {
                mLinkedList.add(str);
            }
            mAdapter.notifyDataSetChanged();
        }

        private class MyAdapter extends ArrayAdapter {

            private LinkedList<String> mLinkedList;
            private int mResource;
            private int mTextViewResource;
            private int mAddNoteViewResource;
            private LayoutInflater mInflater;

            public MyAdapter(Context context, int resource, int textViewResId, int addNoteViewResId, List<String> objects) {
                super(context, resource, textViewResId, objects);

                mLinkedList = (LinkedList<String>) objects;
                mResource = resource;
                mTextViewResource = textViewResId;
                mAddNoteViewResource = addNoteViewResId;
                mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }

            @Override
            public int getCount() {
                return mLinkedList.size() + 1;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v;
                if (mResource != 0)
                    v = mInflater.inflate(mResource, parent, false);
                else
                    return null;

                bindView(v, position);
                return v;
            }

            private void bindView(View v, int position) {
                if (position < 0)
                    return;
                else if (position == mLinkedList.size()) {
                    v.findViewById(mAddNoteViewResource).setVisibility(VISIBLE);
                    return;
                }

                TextView textView = (TextView) v.findViewById(mTextViewResource);
                textView.setText(mLinkedList.get(position));
            }
        }
    }


}
