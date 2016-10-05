package wishcantw.vocabulazy.search.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.ga.manager.GAManager;
import wishcantw.vocabulazy.ga.tags.GAScreenName;
import wishcantw.vocabulazy.search.activity.SearchActivity;
import wishcantw.vocabulazy.search.model.SearchModel;
import wishcantw.vocabulazy.search.view.SearchAddVocToNoteDialogView;
import wishcantw.vocabulazy.widget.DialogFragmentNew;
import wishcantw.vocabulazy.widget.DialogViewNew;

import java.util.LinkedList;

/**
 * Created by SwallowChen on 9/4/16.
 */
public class SearchAddVocToNoteDialogFragment extends DialogFragmentNew<Integer> implements DialogViewNew.OnYesOrNoClickListener, DialogViewNew.OnBackgroundClickListener {

    // callback interface
    public interface OnAddVocToNoteDialogFinishListener {
        void onNeedNewNote();
    }

    // layout resource id
    private static final int LAYOUT_RES_ID = R.layout.view_search_add_voc_to_note_dialog;

    // used to record the id of selected vocabulary
    private int selectedVocId;

    // the context of the application/activity
    private Context mContext;

    // data model
    private SearchModel mSearchModel;

    // views
    private SearchAddVocToNoteDialogView mSearchAddVocToNoteDialogView;

    // listeners
    private OnAddVocToNoteDialogFinishListener mOnAddVocToNoteDialogFinishListener;

    // the name of notes
    private LinkedList<String> mNoteNameList;

    /** Life cycles **/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // get the context instance of the activity
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mSearchAddVocToNoteDialogView = (SearchAddVocToNoteDialogView) inflater.inflate(LAYOUT_RES_ID, container, false);
        mSearchAddVocToNoteDialogView.setOnYesOrNoClickListener(this);
        mSearchAddVocToNoteDialogView.setOnBackgroundClickListener(this);
        return mSearchAddVocToNoteDialogView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // parse the context to SearchActivity
        SearchActivity activity = (SearchActivity) mContext;

        // step 1: get Search Model
        mSearchModel = activity.getModel();

        // step 2: get note list, remember to replace linkedlist by search model
        mNoteNameList = mSearchModel.getNoteNameList();
        mSearchAddVocToNoteDialogView.refreshNoteRadioGroup(mNoteNameList);
    }

    @Override
    public void onResume() {
        super.onResume();

        // send GA screen event
        GAManager.getInstance().sendScreenEvent(GAScreenName.SELECT_NOTE);
    }

    /** Abstracts and Interfaces **/

    @Override
    protected String getGALabel() {
        return GAScreenName.SELECT_NOTE;
    }

    @Override
    public void onYesClick() {

        // remove current fragment
        getActivity().onBackPressed();

        // get selected note index
        int selectedNoteIndex = mSearchAddVocToNoteDialogView.getCurrentCheckedNoteIndex();

        // if the selected index is the last one, then pop NewNoteDialog to create new note
        // otherwise add the vocabulary to the seleceted note
        if (mSearchAddVocToNoteDialogView.getCurrentCheckedNoteIndex() == mNoteNameList.size()) {
            if (mOnAddVocToNoteDialogFinishListener != null) {
                mOnAddVocToNoteDialogFinishListener.onNeedNewNote();
            }
        } else {
            mSearchModel.addVocToNote(selectedVocId, selectedNoteIndex);
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
     * Set the id of selected vocabulary.
     *
     * @param vocId the id the selected vocabulary.
     */
    public void setSelectedVocId(int vocId) {
        selectedVocId = vocId;
    }

    /**
     * Set {@link OnAddVocToNoteDialogFinishListener}.
     *
     * @param listener the instance of {@link OnAddVocToNoteDialogFinishListener}
     */
    public void setOnAddVocToNoteDialogFinishListener(OnAddVocToNoteDialogFinishListener listener) {
        mOnAddVocToNoteDialogFinishListener = listener;
    }
}
