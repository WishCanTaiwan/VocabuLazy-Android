package com.wishcan.www.vocabulazy.search.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.search.view.SearchNewNoteDialogView;
import com.wishcan.www.vocabulazy.widget.DialogFragmentNew;
import com.wishcan.www.vocabulazy.widget.DialogViewNew;

/**
 * Created by SwallowChen on 9/6/16.
 */
public class SearchNewNoteDialogFragment extends DialogFragmentNew implements DialogViewNew.OnYesOrNoClickListener, DialogViewNew.OnBackgroundClickListener {

    public interface OnNewNoteDialogFinishListener {
        void onNewNoteDone(String string);
    }

    private static final int LAYOUT_RES_ID = R.layout.view_search_new_note_dialog;

    private SearchNewNoteDialogView mSearchNewNoteDialogView;

    private OnNewNoteDialogFinishListener mOnDialogFinishListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mSearchNewNoteDialogView = (SearchNewNoteDialogView) inflater.inflate(LAYOUT_RES_ID, container, false);
        mSearchNewNoteDialogView.setOnYesOrNoClickListener(this);
        return mSearchNewNoteDialogView;
    }

    public void setOnNewNoteDialogFinishListener(OnNewNoteDialogFinishListener listener) {
        mOnDialogFinishListener = listener;
    }

    @Override
    public void onYesClick() {
        getActivity().onBackPressed();
        // TODO: (To beibei) please help me to complete crate a new empty note with newNoteString
        String newNoteString = mSearchNewNoteDialogView.getNewNoteString();
        if (mOnDialogFinishListener != null) {
            mOnDialogFinishListener.onNewNoteDone(newNoteString);
        }
    }

    @Override
    public void onNoClick() {
        getActivity().onBackPressed();
    }

    @Override
    public void onBackgroundClick() {
        getActivity().onBackPressed();
    }
}
