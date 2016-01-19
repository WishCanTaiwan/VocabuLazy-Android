package com.wishcan.www.vocabulazy.search.fragment;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.search.model.SearchModel;
import com.wishcan.www.vocabulazy.search.view.SearchView;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.Vocabulary;

import java.util.ArrayList;


public class SearchFragment extends Fragment {

    private Database mDatabase;
    private SearchView mSearchView;
    private SearchModel mSearchModel;
    private ArrayList<Vocabulary> mSuggestedVocabularies;

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
        mDatabase = new Database(getActivity());
        mSearchModel = new SearchModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mSearchView = new SearchView(getContext());
        mSearchView.setOnItemClickListener(new SearchView.OnItemClickListener() {
            @Override
            public void onAddIconClick(int position) {

            }

            @Override
            public void onListItemClick(int position) {
                mSearchView.refreshSearchDetail(
                        mSearchModel.createSearchResultDetailMap(mSuggestedVocabularies.get(position)));
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
}
