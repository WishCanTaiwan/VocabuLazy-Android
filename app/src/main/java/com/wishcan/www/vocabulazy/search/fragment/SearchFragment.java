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
import java.util.LinkedList;
import java.util.List;


public class SearchFragment extends Fragment implements SearchView.OnItemClickListener,
                                                        DialogFragment.OnDialogFinishListener<Integer> {

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
        mSearchView.setOnItemClickListener(this);
        return mSearchView;
    }

    public void refreshSearchResult(ArrayList<Vocabulary> vocabularies) {
        List<Fragment> fragmentLL;
        int numFragment;

        mSuggestedVocabularies = vocabularies;
        mSearchView.refreshSearchResult(
                mSearchModel.createSearchResultMap(vocabularies));
        mSearchView.closeSearchDetail();
        fragmentLL = getFragmentManager().getFragments();
        if (fragmentLL != null) {
            if ((numFragment = fragmentLL.size()) > 1) {
                for (int i = 1; i < numFragment; i++) {
                    getFragmentManager().beginTransaction().remove(fragmentLL.get(i)).commit();
                }
            }
        }
    }

    @Override
    public void onDialogFinish(Integer obj) {
        Log.d(TAG, " select  " +obj.intValue());
        wDatabase.addVocToNote(mSelectVocId, obj);
    }

    @Override
    public void onAddIconClick(int position) {
        mSelectVocId = mSuggestedVocabularies.get(position).getId();
        SearchDialogFragment fragment = SearchDialogFragment.newInstance(SearchDialogView.DIALOG_RES_ID_s.LIST);
        fragment.setOnDialogFinishListener(this);
        getFragmentManager()
                .beginTransaction()
                .add(SearchActivity.VIEW_CONTAINER_RES_ID, fragment, "SearchDialogFragment_List")
                .addToBackStack("SearchFragment")
                .commit();
    }

    @Override
    public void onListItemClick(int position) {
        Vocabulary voc = mSuggestedVocabularies.get(position);
        mSelectVocId = voc.getId();
        mSearchView.refreshSearchDetail(
                mSearchModel.createSearchResultDetailMap(voc));
        mSearchView.showSearchDetail();
    }
}
