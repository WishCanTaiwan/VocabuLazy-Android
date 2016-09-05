package com.wishcan.www.vocabulazy.search.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.search.activity.SearchActivity;
import com.wishcan.www.vocabulazy.search.view.SearchAddVocToNoteDialogView;
import com.wishcan.www.vocabulazy.widget.DialogFragmentNew;
import com.wishcan.www.vocabulazy.widget.DialogViewNew;

import java.util.LinkedList;

/**
 * Created by SwallowChen on 9/4/16.
 */
public class SearchAddVocToNoteDialogFragment extends DialogFragmentNew implements DialogViewNew.OnYesOrNoClickListener, DialogViewNew.OnBackgroundClickListener {

    private static final int LAYOUT_RES_ID = R.layout.view_search_add_voc_to_note_dialog;

    private SearchAddVocToNoteDialogView mDialogView;

    private LinkedList<String> mNoteNameList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mDialogView == null) {
            mDialogView = (SearchAddVocToNoteDialogView) inflater.inflate(LAYOUT_RES_ID, container, false);
        }
        Log.d("SearchDialogFragment", "onCreateView");
        mDialogView.setOnYesOrNoClickListener(this);

        mDialogView.refreshNoteRadioGroup(mNoteNameList);
        return mDialogView;
    }

    /**
     * TODO: Beibei please help me complete steps below
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        SearchActivity activity = (SearchActivity) context;
        // step 1: get Search Model
        // step 2: get note list, remember to replace linkedlist by search model
        mNoteNameList = new LinkedList<>();
        mNoteNameList.add("note test 1");
        mNoteNameList.add("note test 2");
        mNoteNameList.add("note test 3");
        mNoteNameList.add("note test 4");
    }

    @Override
    public void onYesClick() {
        getActivity().onBackPressed();
    }

    @Override
    public void onNoClick() {
        getActivity().onBackPressed();
    }

    @Override
    public void onBackgroundClick() {
        // The backgroundClick is doing onBackPressed by default
    }
}
