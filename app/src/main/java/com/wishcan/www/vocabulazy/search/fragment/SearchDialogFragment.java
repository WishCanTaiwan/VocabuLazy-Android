package com.wishcan.www.vocabulazy.search.fragment;

import android.os.Bundle;

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
public class SearchDialogFragment extends DialogFragment {

    private static final String DIALOG_BUNDLE_STR = "dialog_bundle_str";
    private SearchDialogView mSearchDialogView;

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
        final Database database = ((SearchActivity)getActivity()).getDatabase();
        if(getArguments() != null) {
            SearchDialogView.DIALOG_RES_ID_s resId = (SearchDialogView.DIALOG_RES_ID_s) getArguments().getSerializable(DIALOG_BUNDLE_STR);
            mSearchDialogView = new SearchDialogView(getContext(), null, resId);
            switch (resId) {
                case LIST:
                    ArrayList<Lesson> notes = (database == null) ? null : database.getLessonsByBook(-1);
                    LinkedList<String> dataList = new LinkedList<>();
                    if(notes != null)
                        for(Lesson note : notes)
                            dataList.add(note.getName());
                    mSearchDialogView.refreshNoteList(dataList);
                    break;
                case NEW:
                    break;
            }
        }
    }

    @Override
    protected DialogView getDialogView() {
        return mSearchDialogView;
    }

    @Override
    protected String getCallerTag() {
        return SearchFragment.M_TAG;
    }
}
