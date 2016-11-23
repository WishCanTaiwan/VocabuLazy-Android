package wishcantw.vocabulazy.activities.search.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.analytics.Analytics;
import wishcantw.vocabulazy.analytics.firebase.FirebaseManager;
import wishcantw.vocabulazy.activities.search.activity.SearchActivity;
import wishcantw.vocabulazy.activities.search.model.SearchModel;
import wishcantw.vocabulazy.activities.search.view.SearchNewNoteDialogView;
import wishcantw.vocabulazy.widget.DialogFragmentNew;
import wishcantw.vocabulazy.widget.DialogViewNew;

/**
 * Created by SwallowChen on 9/6/16.
 */
public class SearchNewNoteDialogFragment extends DialogFragmentNew implements DialogViewNew.OnYesOrNoClickListener, DialogViewNew.OnBackgroundClickListener {

    // callback interface
    public interface OnNewNoteDialogFinishListener {
        void onNewNoteDone(String string);
    }

    // layout resource id
    private static final int LAYOUT_RES_ID = R.layout.view_search_new_note_dialog;

    // data model
    private SearchModel mSearchModel;

    // views
    private SearchNewNoteDialogView mSearchNewNoteDialogView;

    // listeners
    private OnNewNoteDialogFinishListener mOnDialogFinishListener;

    /** Life cycles **/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mSearchNewNoteDialogView = (SearchNewNoteDialogView) inflater.inflate(LAYOUT_RES_ID, container, false);
        mSearchNewNoteDialogView.setOnYesOrNoClickListener(this);
        return mSearchNewNoteDialogView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // get search model
        mSearchModel = ((SearchActivity) getActivity()).getSearchModel();
    }

    @Override
    public void onResume() {
        super.onResume();

        // send GA screen event
        FirebaseManager.getInstance().sendScreenEvent(Analytics.ScreenName.CREATE_NOTE);
    }

    /** Abstracts and Interfaces **/

    @Override
    protected String getGALabel() {
        return Analytics.ScreenName.CREATE_NOTE;
    }

    @Override
    public void onYesClick() {

        // remove current fragment
        getActivity().onBackPressed();

        // get name of new note
        String newNoteString = mSearchNewNoteDialogView.getNewNoteString();

        // access search model and add new note to database
        mSearchModel.addNewNote(newNoteString);

        // notify new note has been added
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

    /** Public methods **/

    /**
     * Set {@link OnNewNoteDialogFinishListener}.
     *
     * @param listener the instance of {@link OnNewNoteDialogFinishListener}.
     */
    public void setOnNewNoteDialogFinishListener(OnNewNoteDialogFinishListener listener) {
        mOnDialogFinishListener = listener;
    }
}
