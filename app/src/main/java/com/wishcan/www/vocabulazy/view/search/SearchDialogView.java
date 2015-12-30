package com.wishcan.www.vocabulazy.view.search;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.SearchActivity;
import com.wishcan.www.vocabulazy.view.customview.DialogView;
import com.wishcan.www.vocabulazy.view.customview.XXView;

/**
 * Created by swallow on 2015/9/27.
 */
public class SearchDialogView extends DialogView {

    public interface OnListItemClickedListener {
        void onListItemClicked(int position);

        void onAddItemClicked(int position);
    }

    private static final int DEFAULT_DIALOG_VIEW_RES_ID = R.layout.dialog_search_add_to_note;

    private static final int DEFAULT_NOTE_LIST_ITEM_RES_ID = R.layout.search_result_add_to_note_list_item_view;

    private static final int DEFAULT_NOTE_TEXT_VIEW_RES_ID = R.id.note_list_name;

    private static final int DEFAULT_ADD_NEW_NOTE_RES_ID = R.id.search_result_adding_to_note_adding_function;

    private static final int DEFAULT_LIST_VIEW_RES_ID = R.id.search_dialog_list_view;

    private static final int DEFAULT_DIALOG_CANCEL_VIEW_RES_ID = R.id.action_dialog_cancel;

    private static final int DEFAULT_NOTE_LIST_WIDTH = R.dimen.search_result_add_to_note_dialog_width;

    private static final int DEFAULT_NOTE_LIST_HEIGHT = R.dimen.search_result_add_to_note_dialog_height;

    private OnListItemClickedListener mOnListItemClickedListener;

    private ListView mListView;

    private XXView mXXView;

    private ArrayAdapter mAdapter;

    private LinkedList<String> mLinkedList;

    public SearchDialogView(Context context) {
        this(context, null);
    }

    public SearchDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        int Width = (int) getResources().getDimension(DEFAULT_NOTE_LIST_WIDTH);
        int Height = (int) getResources().getDimension(DEFAULT_NOTE_LIST_HEIGHT);

        ViewGroup viewGroup = (ViewGroup) ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(DEFAULT_DIALOG_VIEW_RES_ID, null);
        mListView = (ListView) viewGroup.findViewById(DEFAULT_LIST_VIEW_RES_ID);
//        mListView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == parent.getCount() - 1)
                    mOnListItemClickedListener.onAddItemClicked(position);
                else
                    mOnListItemClickedListener.onListItemClicked(position);
            }
        });


        mXXView = (XXView) viewGroup.findViewById(DEFAULT_DIALOG_CANCEL_VIEW_RES_ID);
        mXXView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SearchActivity) getContext()).onNavigateUp();
            }
        });

        setDialog(viewGroup, new ViewGroup.LayoutParams(Width, Height));
    }

    public void setAdapter(LinkedList<String> linkedList) {
        mLinkedList = linkedList;
        mAdapter = new MyAdapter(getContext(), DEFAULT_NOTE_LIST_ITEM_RES_ID, DEFAULT_NOTE_TEXT_VIEW_RES_ID, DEFAULT_ADD_NEW_NOTE_RES_ID, mLinkedList);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public Object getDialogOutput() {
        return null;
    }

    public void setOnListItemClickedListener(OnListItemClickedListener listener) {
        mOnListItemClickedListener = listener;
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
