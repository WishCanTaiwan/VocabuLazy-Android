package com.wishcan.www.vocabulazy.search.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
        void onSearchListClick(int vocId);
        void onSearchAddClick();
    }
    private static final int LAYOUT_RES_ID = R.layout.view_search;

    private Context mContext;
    private SearchView mSearchView;

    private SearchModel mSearchModel;
    private OnSearchItemEventListener mOnSearchItemEventListener;

    LinkedList<HashMap> mDataList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataList = new LinkedList<>();
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
    public void onAttach(Context context) {
        super.onAttach(context);

        // get the context of activity
        mContext = context;
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
        mDataList = mSearchModel.createSearchResultMap(searchStr);
        mSearchView.refreshSearchList(mDataList);
    }

    @Override
    public void onSearchItemClick(int vocId) {
        if (mOnSearchItemEventListener != null) {
            mOnSearchItemEventListener.onSearchListClick(vocId);
        }
    }

    @Override
    public void onSearchAddClick() {
        if (mOnSearchItemEventListener != null) {
            mOnSearchItemEventListener.onSearchAddClick();
        }
    }
}
