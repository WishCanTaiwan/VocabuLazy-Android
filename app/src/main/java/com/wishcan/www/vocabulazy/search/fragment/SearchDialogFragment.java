package com.wishcan.www.vocabulazy.search.fragment;

import android.os.Bundle;
import android.util.Log;

import com.wishcan.www.vocabulazy.VLApplication;
import com.wishcan.www.vocabulazy.search.SearchActivity;
import com.wishcan.www.vocabulazy.search.view.SearchDialogView;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.Lesson;
import com.wishcan.www.vocabulazy.widget.DialogFragment;
import com.wishcan.www.vocabulazy.widget.DialogView;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by swallow on 2016/1/20.
 */
public class SearchDialogFragment extends DialogFragment implements DialogFragment.OnDialogFinishListener<String> {

    public static final String TAG = SearchDialogFragment.class.getSimpleName();

    public static String M_TAG;

    private static final String DIALOG_BUNDLE_STR = "dialog_bundle_str";
    private SearchDialogView mSearchDialogView;
    private Database wDatabase;

    public static SearchDialogFragment newInstance(SearchDialogView.DIALOG_RES_ID_s resId) {
        SearchDialogFragment fragment = new SearchDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DIALOG_BUNDLE_STR, resId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public SearchDialogFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        VLApplication vlApplication = (VLApplication) getActivity().getApplication();
        wDatabase = vlApplication.getDatabase();

        if(getArguments() != null) {
            SearchDialogView.DIALOG_RES_ID_s resId = (SearchDialogView.DIALOG_RES_ID_s) getArguments().getSerializable(DIALOG_BUNDLE_STR);
            mSearchDialogView = new SearchDialogView(getContext(), null, resId);
            switch (resId) {
                case NEW:
                    mSearchDialogView.setOnYesOrNoClickedListener(new DialogView.OnYesOrNoClickListener() {
                        @Override
                        public void onYesClicked() {
                            OnDialogFinishListener fragment = (OnDialogFinishListener) getFragmentManager().findFragmentByTag(SearchDialogFragment.M_TAG);
                            fragment.onDialogFinish(mSearchDialogView.getDialogOutput());
                            ((SearchDialogFragment) fragment).refreshNoteList();
                            getActivity().onBackPressed();
                        }

                        @Override
                        public void onNoClicked() {
                            getActivity().onBackPressed();
                        }
                    });
                    break;
                case LIST:
                    M_TAG = getTag();
                    refreshNoteList();
                    mSearchDialogView.setOnDialogItemClickListener(new SearchDialogView.OnDialogItemClickListener() {
                        @Override
                        public void onListItemClick(int position) {
                            OnDialogFinishListener fragment = (OnDialogFinishListener) getFragmentManager().findFragmentByTag(SearchFragment.M_TAG);
                            fragment.onDialogFinish(position);
                            getActivity().onBackPressed();
                        }

                        @Override
                        public void onAddItemClick() {
                            SearchDialogFragment fragment = SearchDialogFragment.newInstance(SearchDialogView.DIALOG_RES_ID_s.NEW);
                            getFragmentManager()
                                    .beginTransaction()
                                    .add(SearchActivity.VIEW_CONTAINER_RES_ID, fragment, "SearchDialogFragment_New")
                                    .addToBackStack("SearchDialogFragment_List")
                                    .commit();
                        }
                    });
                    break;

            }
        }
    }

    @Override
    public void onDialogFinish(String obj) {
//        Log.d(TAG, mDatabase.toString());
        wDatabase.createNewNote(obj);
    }

    @Override
    protected DialogView getDialogView() {
        return mSearchDialogView;
    }

    @Override
    protected String getCallerTag() {
        return SearchFragment.M_TAG;
    }

    private void refreshNoteList() {
//        Log.d(TAG, mDatabase.toString());
//        Log.d(TAG, "refreshNotekList");
//        VLApplication vlApplication = (VLApplication) getActivity().getApplication();
//        wDatabase = vlApplication.getDatabase();
        ArrayList<Lesson> notes = (wDatabase == null) ? null : wDatabase.getLessonsByBook(-1);
        LinkedList<String> dataList = new LinkedList<>();
        if(notes != null)
            for(Lesson note : notes)
                dataList.add(note.getName());
        if(mSearchDialogView != null)
            mSearchDialogView.refreshNoteList(dataList);
    }
}
