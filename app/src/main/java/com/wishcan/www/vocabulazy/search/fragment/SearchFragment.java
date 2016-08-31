package com.wishcan.www.vocabulazy.search.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.VLApplication;
import com.wishcan.www.vocabulazy.search.model.SearchModel;
import com.wishcan.www.vocabulazy.search.view.SearchView;
import com.wishcan.www.vocabulazy.storage.Database;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * @since 2016/8/31
 */
public class SearchFragment extends Fragment {
    private static final int LAYOUT_RES_ID = R.layout.view_search_new;
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
        VLApplication application = (VLApplication) getActivity().getApplication();
        wDatabase = application.getDatabase();
        mSearchModel = new SearchModel(application);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mSearchView == null) {
            mSearchView = (SearchView) inflater.inflate(LAYOUT_RES_ID, container, false);
        }
        return mSearchView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /** TODO: for testing flow only */
        LinkedList<HashMap> dataList = mSearchModel.createSearchResultMap(wDatabase.readSuggestVocabularyBySpell("a"));
        mSearchView.refreshSearchList(dataList);
    }
}
