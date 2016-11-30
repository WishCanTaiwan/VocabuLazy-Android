package wishcantw.vocabulazy.activities.player.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.analytics.Analytics;
import wishcantw.vocabulazy.analytics.firebase.FirebaseManager;
import wishcantw.vocabulazy.activities.player.activity.PlayerActivity;
import wishcantw.vocabulazy.activities.player.model.PlayerModel;
import wishcantw.vocabulazy.activities.player.view.PlayerAddVocToNoteDialogView;
import wishcantw.vocabulazy.widget.DialogFragmentNew;
import wishcantw.vocabulazy.widget.DialogViewNew;

import java.util.LinkedList;

/**
 * Created by SwallowChen on 9/6/16.
 */
public class PlayerAddVocToNoteDialogFragment extends DialogFragmentNew<Integer> implements DialogViewNew.OnYesOrNoClickListener, DialogViewNew.OnBackgroundClickListener {

    // callback interface
    public interface OnAddVocToNoteDialogFinishListener {
        void onNeedNewNote();
    }

    // layout resources
    private static final int LAYOUT_RES_ID = R.layout.view_player_add_voc_to_note_dialog;

    // record the id of selected vocabulary
    private int mSelectedVocId;

    // data model
    private PlayerModel mPlayerModel;

    // views
    private PlayerAddVocToNoteDialogView mPlayerAddVocToNoteDialogView;

    // listener
    private OnAddVocToNoteDialogFinishListener mOnAddVocToNoteDialogFinishListener;

    // the name list of notes
    private LinkedList<String> mNoteNameList;

    /** Life cycles **/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mPlayerAddVocToNoteDialogView = (PlayerAddVocToNoteDialogView) inflater.inflate(LAYOUT_RES_ID, container, false);
        mPlayerAddVocToNoteDialogView.setOnYesOrNoClickListener(this);
        mPlayerAddVocToNoteDialogView.setOnBackgroundClickListener(this);

        return mPlayerAddVocToNoteDialogView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // step 1: get player Model
        mPlayerModel = ((PlayerActivity) getActivity()).getPlayerModel();

        // step 2: get note list, remember to replace linkedlist by player model
        mNoteNameList = mPlayerModel.getNoteNameList();
        mPlayerAddVocToNoteDialogView.refreshNoteRadioGroup(mNoteNameList);
    }

    @Override
    public void onResume() {
        super.onResume();

        // send GA screen event
        FirebaseManager.getInstance().sendScreenEvent(Analytics.ScreenName.SELECT_NOTE);
    }

    /** Abstracts and Interfaces **/

    @Override
    protected String getGALabel() {
        return Analytics.ScreenName.SELECT_NOTE;
    }

    @Override
    public void onYesClick() {
        int selectedNoteIndex = mPlayerAddVocToNoteDialogView.getCurrentCheckedNoteIndex();
        if (selectedNoteIndex == mNoteNameList.size()) {
            getActivity().onBackPressed();
            if (mOnAddVocToNoteDialogFinishListener != null) {
                mOnAddVocToNoteDialogFinishListener.onNeedNewNote();
            }
        } else {
            mPlayerModel.addVocToNote(((PlayerActivity) getActivity()).getVocIdToBeAdded(), selectedNoteIndex);
            getActivity().onBackPressed();
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

    /** Public methods **/

    /**
     * Set {@link OnAddVocToNoteDialogFinishListener}.
     *
     * @param listener {@link OnAddVocToNoteDialogFinishListener}
     */
    public void setOnAddVocToNoteDialogFinishListener(OnAddVocToNoteDialogFinishListener listener) {
        mOnAddVocToNoteDialogFinishListener = listener;
    }

    /**
     * Set the id of selected vocabulary.
     *
     * @param vocId the id of selected vocabulary.
     */
    public void setSelectedVocId(int vocId) {
        mSelectedVocId = vocId;
    }
}
