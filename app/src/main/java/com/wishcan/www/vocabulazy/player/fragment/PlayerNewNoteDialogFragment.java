package com.wishcan.www.vocabulazy.player.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.ga.tags.GAScreenName;
import com.wishcan.www.vocabulazy.player.activity.PlayerActivity;
import com.wishcan.www.vocabulazy.player.model.PlayerModel;
import com.wishcan.www.vocabulazy.player.view.PlayerNewNoteDialogView;
import com.wishcan.www.vocabulazy.widget.DialogFragmentNew;
import com.wishcan.www.vocabulazy.widget.DialogViewNew;

/**
 * Created by SwallowChen on 9/6/16.
 */
public class PlayerNewNoteDialogFragment extends DialogFragmentNew implements DialogViewNew.OnYesOrNoClickListener, DialogViewNew.OnBackgroundClickListener {

    @Override
    protected String getGALabel() {
        return GAScreenName.CREATE_NOTE;
    }

    public interface OnNewNoteDialogFinishListener {
        void onNewNoteDone(String string);
    }

    private static final int LAYOUT_RES_ID = R.layout.view_player_new_note_dialog;

    private Context mContext;

    private PlayerNewNoteDialogView mPlayerNewNoteDialogView;

    private OnNewNoteDialogFinishListener mOnDialogFinishListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // get the instance of activity
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mPlayerNewNoteDialogView = (PlayerNewNoteDialogView) inflater.inflate(LAYOUT_RES_ID, container, false);
        mPlayerNewNoteDialogView.setOnYesOrNoClickListener(this);
        return mPlayerNewNoteDialogView;
    }

    public void setOnNewNoteDialogFinishListener(OnNewNoteDialogFinishListener listener) {
        mOnDialogFinishListener = listener;
    }

    @Override
    public void onYesClick() {
        getActivity().onBackPressed();

        // get name of new note
        String newNoteString = mPlayerNewNoteDialogView.getNewNoteString();

        // access search model and add new note to database
        PlayerModel playerModel = ((PlayerActivity) mContext).getModel();
        playerModel.addNewNote(newNoteString);

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
