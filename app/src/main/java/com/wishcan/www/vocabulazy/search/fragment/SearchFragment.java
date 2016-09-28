package com.wishcan.www.vocabulazy.search.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.ga.GABaseFragment;
import com.wishcan.www.vocabulazy.ga.tags.GAScreenName;
import com.wishcan.www.vocabulazy.search.activity.SearchActivity;
import com.wishcan.www.vocabulazy.search.model.SearchModel;
import com.wishcan.www.vocabulazy.search.view.SearchView;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * @since 2016/8/31
 */
public class SearchFragment extends GABaseFragment implements SearchView.SearchEventListener {

    // callback interface
    public interface OnSearchItemEventListener {
        void onSearchListClick(int vocId);
        void onSearchAddClick();
    }

    // layout resource id
    private static final int LAYOUT_RES_ID = R.layout.view_search;

    // the context of the application/activity
    private Context mContext;

    // views
    private SearchView mSearchView;

    // data model
    private SearchModel mSearchModel;

    // listener
    private OnSearchItemEventListener mOnSearchItemEventListener;

    // 
    private LinkedList<HashMap> mDataList;

    /** Life cycles **/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // get the context of activity
        mContext = context;
    }

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSearchModel = ((SearchActivity) mContext).getModel();
    }

    /** Abstracts and Interfaces **/

    @Override
    protected String getGALabel() {
        return GAScreenName.SEARCH;
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

    /** Public methods **/

    /**
     * Set {@link OnSearchItemEventListener}.
     *
     * @param listener the instance of {@link OnSearchItemEventListener}
     */
    public void setOnSearchItemEventListener(OnSearchItemEventListener listener) {
        mOnSearchItemEventListener = listener;
    }

    /**
     * Refresh the search result based on the query string.
     *
     * @param searchStr the query string.
     */
    public void refreshSearchResult(String searchStr) {
        mDataList = mSearchModel.createSearchResultMap(searchStr);
        mSearchView.refreshSearchList(mDataList);
    }
}
