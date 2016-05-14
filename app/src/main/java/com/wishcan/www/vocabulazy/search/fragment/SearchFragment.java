package com.wishcan.www.vocabulazy.search.fragment;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.VLApplication;
import com.wishcan.www.vocabulazy.search.SearchActivity;
import com.wishcan.www.vocabulazy.search.model.SearchModel;
import com.wishcan.www.vocabulazy.search.view.SearchDialogView;
import com.wishcan.www.vocabulazy.search.view.SearchView;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Vocabulary;
import com.wishcan.www.vocabulazy.widget.DialogFragment;

import java.util.ArrayList;


public class SearchFragment extends Fragment implements DialogFragment.OnDialogFinishListener<Integer> {

    public static final String TAG = SearchFragment.class.getSimpleName();

    public static String M_TAG;

    private Database wDatabase;
    private SearchView mSearchView;
    private SearchModel mSearchModel;
    private ArrayList<Vocabulary> mSuggestedVocabularies;
    private int mSelectVocId;

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        M_TAG = getTag();

        VLApplication vlApplication = (VLApplication) getActivity().getApplication();
        wDatabase = vlApplication.getDatabase();
        mSearchModel = new SearchModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mSearchView = new SearchView(getContext());
        mSearchView.setOnItemClickListener(new SearchView.OnItemClickListener() {
            @Override
            public void onAddIconClick(int position) {
//                Log.d(TAG, "onAddIconClick");
                mSelectVocId = mSuggestedVocabularies.get(position).getId();
                SearchDialogFragment fragment = SearchDialogFragment.newInstance(SearchDialogView.DIALOG_RES_ID_s.LIST);
                getFragmentManager()
                        .beginTransaction()
                        .add(SearchActivity.VIEW_CONTAINER_RES_ID, fragment, "SearchDialogFragment_List")
                        .addToBackStack("SearchFragment")
                        .commit();
            }

            @Override
            public void onListItemClick(int position) {
//                Log.d(TAG, "onListItemClick");
                Vocabulary voc = mSuggestedVocabularies.get(position);
                mSelectVocId = voc.getId();
                mSearchView.refreshSearchDetail(
                        mSearchModel.createSearchResultDetailMap(voc));
                mSearchView.showSearchDetail();
            }
        });
        return mSearchView;
    }

    public void refreshSearchResult(ArrayList<Vocabulary> vocabularies) {
        mSuggestedVocabularies = vocabularies;
        mSearchView.refreshSearchResult(
                mSearchModel.createSearchResultMap(vocabularies));
    }

    @Override
    public void onDialogFinish(Integer obj) {
//        Log.d(TAG, mDatabase.toString());
        Log.d(TAG, " select  " +obj.intValue());
        wDatabase.addVocToNote(mSelectVocId, obj);
    }
}
