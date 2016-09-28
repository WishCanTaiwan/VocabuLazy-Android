package com.wishcan.www.vocabulazy.mainmenu.note.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.ga.tags.GAScreenName;
import com.wishcan.www.vocabulazy.mainmenu.activity.MainMenuActivity;
import com.wishcan.www.vocabulazy.mainmenu.model.MainMenuModel;
import com.wishcan.www.vocabulazy.mainmenu.note.view.NoteCreateDialogView;
import com.wishcan.www.vocabulazy.widget.DialogFragmentNew;
import com.wishcan.www.vocabulazy.widget.DialogViewNew;

/**
 * Created by allencheng07 on 2016/9/15.
 */
public class NoteCreateDialogFragment extends DialogFragmentNew implements DialogViewNew.OnYesOrNoClickListener, DialogViewNew.OnBackgroundClickListener {

    @Override
    protected String getGALabel() {
        return GAScreenName.CREATE_NOTE;
    }

    public interface OnNoteCreateListener {
        void onNoteCreated();
    }

    private NoteCreateDialogView mNoteCreateDialogView;
    private OnNoteCreateListener mOnNoteCreateListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mNoteCreateDialogView = (NoteCreateDialogView) inflater.inflate(R.layout.view_note_create_dialog, container, false);
        mNoteCreateDialogView.setOnYesOrNoClickListener(this);
        return mNoteCreateDialogView;
    }

    @Override
    public void onYesClick() {
        String newTitle = mNoteCreateDialogView.getNewString();
        MainMenuActivity activity = (MainMenuActivity) getActivity();
        MainMenuModel mainMenuModel = activity.getModel();
        mainMenuModel.createNote(newTitle);
        mOnNoteCreateListener.onNoteCreated();
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

    public void addOnNoteCreateListener(OnNoteCreateListener listener) {
        mOnNoteCreateListener = listener;
    }
}
