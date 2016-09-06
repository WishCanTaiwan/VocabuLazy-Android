package com.wishcan.www.vocabulazy.search.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

    public interface OnSearchItemEventListener {
        void onSearchListClick();
        void onSearchAddClick();
    }
    private static final int LAYOUT_RES_ID = R.layout.view_search;

    private Context mContext;
    private SearchView mSearchView;

    private Database wDatabase;
    private SearchModel mSearchModel;
    private OnSearchItemEventListener mOnSearchItemEventListener;

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // get the context of activity
        mContext = context;
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSearchModel = ((SearchActivity) mContext).getModel();
    }

    public void setOnSearchItemEventListener(OnSearchItemEventListener listener) {
        mOnSearchItemEventListener = listener;
    }

    public void refreshSearchResult(String searchStr) {
        LinkedList<HashMap> dataList = mSearchModel.createSearchResultMap(searchStr);
        mSearchView.refreshSearchList(dataList);
    }

    @Override
    public void onSearchItemClick() {
        if (mOnSearchItemEventListener != null) {
            mOnSearchItemEventListener.onSearchListClick();
        }
    }

    @Override
    public void onSearchAddClick() {
        if (mOnSearchItemEventListener != null) {
            mOnSearchItemEventListener.onSearchAddClick();
        }
    }
}
