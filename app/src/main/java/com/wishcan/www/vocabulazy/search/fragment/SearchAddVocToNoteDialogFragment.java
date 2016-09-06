package com.wishcan.www.vocabulazy.search.fragment;

import android.content.Context;
import android.os.Bundle;
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
public class SearchAddVocToNoteDialogFragment extends DialogFragmentNew<Integer> implements DialogViewNew.OnYesOrNoClickListener, DialogViewNew.OnBackgroundClickListener {

    public interface OnAddVocToNoteDialogFinishListener {
        void onNeedNewNote();
    }

    private static final int LAYOUT_RES_ID = R.layout.view_search_add_voc_to_note_dialog;

    private Context mContext;

    private SearchAddVocToNoteDialogView mSearchAddVocToNoteDialogView;

    private OnAddVocToNoteDialogFinishListener mOnAddVocToNoteDialogFinishListener;
    private LinkedList<String> mNoteNameList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mSearchAddVocToNoteDialogView = (SearchAddVocToNoteDialogView) inflater.inflate(LAYOUT_RES_ID, container, false);
        Log.d("SearchDialogFragment", "onCreateView");
        mSearchAddVocToNoteDialogView.setOnYesOrNoClickListener(this);
        mSearchAddVocToNoteDialogView.setOnBackgroundClickListener(this);

        return mSearchAddVocToNoteDialogView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // get the context instance of the activity
        mContext = context;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // parse the context to SearchActivity
        SearchActivity activity = (SearchActivity) mContext;

        // step 1: get Search Model
        SearchModel searchModel = activity.getModel();

        // step 2: get note list, remember to replace linkedlist by search model
        mNoteNameList = searchModel.getNoteNameList();
        mSearchAddVocToNoteDialogView.refreshNoteRadioGroup(mNoteNameList);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void setOnAddVocToNoteDialogFinishListener(OnAddVocToNoteDialogFinishListener listener) {
        mOnAddVocToNoteDialogFinishListener = listener;
    }

    @Override
    public void onYesClick() {
        getActivity().onBackPressed();
        // TODO: ELSE part : (To beibei) please help me to complete add voc to note job
        Log.d("SearchAddVocDialog", "index = " + mSearchAddVocToNoteDialogView.getCurrentCheckedNoteIndex() + "mNoteNameList size = " + mNoteNameList.size());
        if (mSearchAddVocToNoteDialogView.getCurrentCheckedNoteIndex() == mNoteNameList.size()) {
            if (mOnAddVocToNoteDialogFinishListener != null) {
                mOnAddVocToNoteDialogFinishListener.onNeedNewNote();
            }
        } else {

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
