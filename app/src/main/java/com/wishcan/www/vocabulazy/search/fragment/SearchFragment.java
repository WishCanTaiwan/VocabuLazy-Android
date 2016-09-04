package com.wishcan.www.vocabulazy.search.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.search.activity.SearchActivity;
import com.wishcan.www.vocabulazy.search.model.SearchModel;
import com.wishcan.www.vocabulazy.search.view.SearchView;
import com.wishcan.www.vocabulazy.storage.Database;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * @since 2016/8/31
 */
public class SearchFragment extends Fragment implements SearchView.SearchEventListener {
    private static final int LAYOUT_RES_ID = R.layout.view_search;
    private SearchView mSearchView;
    private Database wDatabase;
    private SearchModel mSearchModel;

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wDatabase = Database.getInstance();
        mSearchModel = new SearchModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mSearchView == null) {
            mSearchView = (SearchView) inflater.inflate(LAYOUT_RES_ID, container, false);
        }
        mSearchView.setSearchEventListener(this);
        return mSearchView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void refreshSearchResult(String searchStr) {
        LinkedList<HashMap> dataList = mSearchModel.createSearchResultMap(wDatabase.readSuggestVocabularyBySpell(searchStr));
        mSearchView.refreshSearchList(dataList);
    }

    @Override
    public void onSearchItemClick() {
        Log.d("SearchFragment", "onSearchItemClick");
        SearchAddVocToNoteDialogFragment dialogFragment = new SearchAddVocToNoteDialogFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(SearchActivity.VIEW_MAIN_RES_ID, dialogFragment, "SearchAddVocToNoteDialogFragment");
        fragmentTransaction.addToBackStack("SearchAddVocToNoteDialogFragment");
        fragmentTransaction.commit();
    }

    @Override
    public void onSearchAddClick() {

    }
}
