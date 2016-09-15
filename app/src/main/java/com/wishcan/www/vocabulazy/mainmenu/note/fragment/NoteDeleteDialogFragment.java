package com.wishcan.www.vocabulazy.mainmenu.note.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.mainmenu.activity.MainMenuActivity;
import com.wishcan.www.vocabulazy.mainmenu.model.MainMenuModel;
import com.wishcan.www.vocabulazy.mainmenu.note.view.NoteDeleteDialogView;
import com.wishcan.www.vocabulazy.widget.DialogFragmentNew;
import com.wishcan.www.vocabulazy.widget.DialogView;
import com.wishcan.www.vocabulazy.widget.DialogViewNew;

/**
 * Created by allencheng07 on 2016/9/15.
 */
public class NoteDeleteDialogFragment extends DialogFragmentNew implements DialogViewNew.OnYesOrNoClickListener, DialogViewNew.OnBackgroundClickListener {

    public interface OnNoteDeleteListener {
        void onNoteDeleted();
    }

    private int noteIndex;
    private String noteTitle;

    private NoteDeleteDialogView mNoteDeleteDialogView;
    private OnNoteDeleteListener mOnNoteDeleteListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mNoteDeleteDialogView = (NoteDeleteDialogView) inflater.inflate(R.layout.view_note_delete, container, false);
        mNoteDeleteDialogView.setNoteTitle(noteTitle);
        mNoteDeleteDialogView.setOnYesOrNoClickListener(this);
        return mNoteDeleteDialogView;
    }

    @Override
    public void onYesClick() {
        MainMenuActivity activity = (MainMenuActivity) getActivity();
        MainMenuModel mainMenuModel =  activity.getModel();
        mainMenuModel.deleteNote(noteIndex);
        mOnNoteDeleteListener.onNoteDeleted();
        getActivity().onBackPressed();
    }

    @Override
    public void onNoClick() {
        getActivity().onBackPressed();
    }

    @Override
    public void onBackgroundClick() {
        getActivity().onBackPressed();
    }

    public void addOnNoteDeleteListener(OnNoteDeleteListener listener) {
        mOnNoteDeleteListener = listener;
    }

    public void setNoteIndex(int index) {
        noteIndex = index;
    }

    public void setNoteTitle(String title) {
        noteTitle = title;
    }
}
