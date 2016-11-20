package wishcantw.vocabulazy.mainmenu.note.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.analytics.Analytics;
import wishcantw.vocabulazy.mainmenu.activity.MainMenuActivity;
import wishcantw.vocabulazy.mainmenu.model.MainMenuModel;
import wishcantw.vocabulazy.mainmenu.note.view.NoteDeleteDialogView;
import wishcantw.vocabulazy.widget.DialogFragmentNew;
import wishcantw.vocabulazy.widget.DialogViewNew;

public class NoteDeleteDialogFragment extends DialogFragmentNew implements DialogViewNew.OnYesOrNoClickListener, DialogViewNew.OnBackgroundClickListener {

    @Override
    protected String getGALabel() {
        return Analytics.ScreenName.DELETE_NOTE;
    }

    public interface OnNoteDeleteListener {
        void onNoteDeleted();
    }

    private int noteIndex;
    private String noteTitle;

    private NoteDeleteDialogView mNoteDeleteDialogView;
    private OnNoteDeleteListener mOnNoteDeleteListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mNoteDeleteDialogView = (NoteDeleteDialogView) inflater.inflate(R.layout.view_note_delete_dialog, container, false);
        mNoteDeleteDialogView.setNoteTitle(noteTitle);
        mNoteDeleteDialogView.setOnYesOrNoClickListener(this);
        return mNoteDeleteDialogView;
    }

    @Override
    public void onYesClick() {
        MainMenuModel mainMenuModel =  ((MainMenuActivity) getActivity()).getMainMenuModel();
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
