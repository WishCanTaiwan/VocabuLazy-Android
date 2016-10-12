package wishcantw.vocabulazy.mainmenu.note.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.analytics.Analytics;
import wishcantw.vocabulazy.mainmenu.activity.MainMenuActivity;
import wishcantw.vocabulazy.mainmenu.model.MainMenuModel;
import wishcantw.vocabulazy.mainmenu.note.view.NoteRenameDialogView;
import wishcantw.vocabulazy.widget.DialogFragmentNew;
import wishcantw.vocabulazy.widget.DialogViewNew;

/**
 * Created by allencheng07 on 2016/9/12.
 */
public class NoteRenameDialogFragment extends DialogFragmentNew implements DialogViewNew.OnYesOrNoClickListener, DialogViewNew.OnBackgroundClickListener {

    @Override
    protected String getGALabel() {
        return Analytics.ScreenName.RENAME_NOTE;
    }

    public interface OnRenameCompleteListener {
        void onRenameCompleted();
    }

    public static final String TAG = "NoteRenameDialogFrag";

    private Context mContext;
    private MainMenuModel mMainMenuModel;
    private NoteRenameDialogView mNoteRenameDialogView;
    private OnRenameCompleteListener mOnRenameCompleteListener;
    private String originalString;
    private int noteIndex;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mNoteRenameDialogView = (NoteRenameDialogView) inflater.inflate(R.layout.view_note_rename_dialog, container, false);
        mNoteRenameDialogView.setOriginalString(originalString);
        mNoteRenameDialogView.setOnYesOrNoClickListener(this);
        return mNoteRenameDialogView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainMenuActivity activity = (MainMenuActivity) getActivity();
        mMainMenuModel = activity.getModel();
    }

    @Override
    public void onYesClick() {
        String newString = mNoteRenameDialogView.getNewString();

        // update database
        mMainMenuModel.renameNote(noteIndex, newString);
        mOnRenameCompleteListener.onRenameCompleted();
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

    public void addOnRenameCompleteListener(OnRenameCompleteListener listener) {
        mOnRenameCompleteListener = listener;
    }

    public void setNoteIndex(int index) {
        noteIndex = index;
    }

    public void setOriginalString(String string) {
        originalString = string;
    }
}
