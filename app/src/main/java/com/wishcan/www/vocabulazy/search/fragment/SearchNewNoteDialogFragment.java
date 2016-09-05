package com.wishcan.www.vocabulazy.search.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.search.view.SearchNewNoteDialogView;
import com.wishcan.www.vocabulazy.widget.DialogFragmentNew;

/**
 * Created by SwallowChen on 9/6/16.
 */
public class SearchNewNoteDialogFragment extends DialogFragmentNew {

    private static final int LAYOUT_RES_ID = R.layout.view_search_new_note_dialog;

    private SearchNewNoteDialogView mSearchNewNoteDialogView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mSearchNewNoteDialogView = (SearchNewNoteDialogView) inflater.inflate(LAYOUT_RES_ID, container, false);
        return mSearchNewNoteDialogView;
    }
}
