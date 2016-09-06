package com.wishcan.www.vocabulazy.search.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.search.activity.SearchActivity;
import com.wishcan.www.vocabulazy.search.model.SearchModel;
import com.wishcan.www.vocabulazy.search.view.SearchAddVocToNoteDialogView;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.widget.DialogFragmentNew;
import com.wishcan.www.vocabulazy.widget.DialogViewNew;

import java.util.LinkedList;

/**
 * Created by SwallowChen on 9/4/16.
 */
public class SearchAddVocToNoteDialogFragment extends DialogFragmentNew implements DialogViewNew.OnYesOrNoClickListener, DialogViewNew.OnBackgroundClickListener {

    private static final int LAYOUT_RES_ID = R.layout.view_search_add_voc_to_note_dialog;

    private Context mContext;

    private SearchAddVocToNoteDialogView mDialogView;

    private LinkedList<String> mNoteNameList;

    /**
     * TODO: Beibei please help me complete steps below
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // get the context instance of the activity
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mDialogView == null) {
            mDialogView = (SearchAddVocToNoteDialogView) inflater.inflate(LAYOUT_RES_ID, container, false);
        }
        mDialogView.setOnYesOrNoClickListener(this);
        return mDialogView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // parse the context to SearchActivity
        SearchActivity activity = (SearchActivity) mContext;

        // step 1: get Search Model
        SearchModel searchModel = activity.getModel();

        // step 2: get note list, remember to replace linkedlist by search model
        mNoteNameList = searchModel.getNoteNameList();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDialogView != null) {
            mDialogView.refreshNoteRadioGroup(mNoteNameList);
        }
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
