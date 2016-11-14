package wishcantw.vocabulazy.mainmenu.note.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.analytics.Analytics;
import wishcantw.vocabulazy.mainmenu.activity.MainMenuActivity;
import wishcantw.vocabulazy.mainmenu.model.MainMenuModel;
import wishcantw.vocabulazy.mainmenu.note.view.NoteCreateDialogView;
import wishcantw.vocabulazy.widget.DialogFragmentNew;
import wishcantw.vocabulazy.widget.DialogViewNew;

public class NoteCreateDialogFragment extends DialogFragmentNew implements DialogViewNew.OnYesOrNoClickListener, DialogViewNew.OnBackgroundClickListener {

    @Override
    protected String getGALabel() {
        return Analytics.ScreenName.CREATE_NOTE;
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
        MainMenuModel mainMenuModel = ((MainMenuActivity) getActivity()).getModel();
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
