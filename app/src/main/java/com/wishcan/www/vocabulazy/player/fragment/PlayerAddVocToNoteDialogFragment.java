package com.wishcan.www.vocabulazy.player.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.ga.tags.GAScreenName;
import com.wishcan.www.vocabulazy.player.activity.PlayerActivity;
import com.wishcan.www.vocabulazy.player.model.PlayerModel;
import com.wishcan.www.vocabulazy.player.view.PlayerAddVocToNoteDialogView;
import com.wishcan.www.vocabulazy.widget.DialogFragmentNew;
import com.wishcan.www.vocabulazy.widget.DialogViewNew;

import java.util.LinkedList;

/**
 * Created by SwallowChen on 9/6/16.
 */
public class PlayerAddVocToNoteDialogFragment extends DialogFragmentNew<Integer> implements DialogViewNew.OnYesOrNoClickListener, DialogViewNew.OnBackgroundClickListener {

    @Override
    protected String getGALabel() {
        return GAScreenName.SELECT_NOTE;
    }

    public interface OnAddVocToNoteDialogFinishListener {
        void onNeedNewNote();
    }

    private static final int LAYOUT_RES_ID = R.layout.view_player_add_voc_to_note_dialog;

    private int mSelectedVocId;

    private Context mContext;
    private PlayerModel mPlayerModel;

    private PlayerAddVocToNoteDialogView mPlayerAddVocToNoteDialogView;

    private OnAddVocToNoteDialogFinishListener mOnAddVocToNoteDialogFinishListener;
    private LinkedList<String> mNoteNameList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mPlayerAddVocToNoteDialogView = (PlayerAddVocToNoteDialogView) inflater.inflate(LAYOUT_RES_ID, container, false);
        Log.d("PlayerDialogFragment", "onCreateView");
        mPlayerAddVocToNoteDialogView.setOnYesOrNoClickListener(this);
        mPlayerAddVocToNoteDialogView.setOnBackgroundClickListener(this);

        return mPlayerAddVocToNoteDialogView;
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

        // parse the context to PlayerActivity
        PlayerActivity activity = (PlayerActivity) mContext;

        // step 1: get player Model
        mPlayerModel = activity.getModel();

        // step 2: get note list, remember to replace linkedlist by player model
        mNoteNameList = mPlayerModel.getNoteNameList();
        mPlayerAddVocToNoteDialogView.refreshNoteRadioGroup(mNoteNameList);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void setOnAddVocToNoteDialogFinishListener(OnAddVocToNoteDialogFinishListener listener) {
        mOnAddVocToNoteDialogFinishListener = listener;
    }

    public void setSelectedVocId(int vocId) {
        mSelectedVocId = vocId;
    }

    @Override
    public void onYesClick() {
        getActivity().onBackPressed();
        int selectedNoteIndex = mPlayerAddVocToNoteDialogView.getCurrentCheckedNoteIndex();
        if (selectedNoteIndex == mNoteNameList.size()) {
            if (mOnAddVocToNoteDialogFinishListener != null) {
                mOnAddVocToNoteDialogFinishListener.onNeedNewNote();
            }
        } else {
            mPlayerModel.addVocToNote(mSelectedVocId, selectedNoteIndex);
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
