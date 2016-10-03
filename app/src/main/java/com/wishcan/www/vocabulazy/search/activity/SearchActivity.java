package com.wishcan.www.vocabulazy.search.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.search.fragment.SearchAddVocToNoteDialogFragment;
import com.wishcan.www.vocabulazy.search.fragment.SearchFragment;
import com.wishcan.www.vocabulazy.search.model.SearchModel;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.search.fragment.SearchNewNoteDialogFragment;
import com.wishcan.www.vocabulazy.utility.Logger;

/**
 * Created by SwallowChen on 8/31/16.
 */
public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,
                                                                 SearchFragment.OnSearchItemEventListener,
                                                                 SearchAddVocToNoteDialogFragment.OnAddVocToNoteDialogFinishListener,
                                                                 SearchNewNoteDialogFragment.OnNewNoteDialogFinishListener {
    public static final String TAG = "SearchActivity";

    private static final int VIEW_MAIN_RES_ID = R.id.activity_search_container;
    private static final int VIEW_RES_ID = R.layout.activity_search;
    private static final int MENU_RES_ID = R.menu.menu_search;
    private static final int MENU_ITEM_SEARCH_RES_ID = R.id.menu_item_search;
    private static final int TOOLBAR_RES_ID = R.id.toolbar;

    private SearchView mSearchActionView;
    private Toolbar mToolbar;
    private SearchFragment mSearchFragment;
    private SearchAddVocToNoteDialogFragment mSearchAddVocToNoteDialogFragment;
    private SearchNewNoteDialogFragment mSearchNewNoteDialogFragment;
    private SearchModel mSearchModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(VIEW_RES_ID);
        mToolbar = (Toolbar) findViewById(TOOLBAR_RES_ID);
        setSupportActionBar(mToolbar);

        // enabling HomeAsUp button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (mSearchModel == null) {
            mSearchModel = new SearchModel();
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof SearchFragment) {
            mSearchFragment = (SearchFragment) fragment;
            mSearchFragment.setOnSearchItemEventListener(this);
        } else if (fragment instanceof SearchAddVocToNoteDialogFragment) {
            mSearchAddVocToNoteDialogFragment = (SearchAddVocToNoteDialogFragment) fragment;
            mSearchAddVocToNoteDialogFragment.setOnAddVocToNoteDialogFinishListener(this);
        } else if (fragment instanceof SearchNewNoteDialogFragment) {
            mSearchNewNoteDialogFragment = (SearchNewNoteDialogFragment) fragment;
            mSearchNewNoteDialogFragment.setOnNewNoteDialogFinishListener(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Database.getInstance().writeToFile(getApplicationContext());
                return null;
            }
        }.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /** Inflate the menu; this adds items to the action bar if it is present. */
        getMenuInflater().inflate(MENU_RES_ID, menu);
        mSearchActionView = (SearchView) menu.findItem(MENU_ITEM_SEARCH_RES_ID).getActionView();

        mSearchActionView.onActionViewExpanded();          // Important, make ActionView expand initially
        mSearchActionView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    public SearchModel getModel() {
        return mSearchModel;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mSearchFragment.refreshSearchResult(newText);
        return true;
    }

    /**-- SearchFragment callback --**/
    @Override
    public void onSearchListClick(int vocId) {
        Logger.d(TAG, "onSearchListClick");
        SearchAddVocToNoteDialogFragment dialogFragment = new SearchAddVocToNoteDialogFragment();
        dialogFragment.setSelectedVocId(vocId);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(SearchActivity.VIEW_MAIN_RES_ID, dialogFragment, "SearchAddVocToNoteDialogFragment");
        fragmentTransaction.addToBackStack("SearchAddVocToNoteDialogFragment");
        fragmentTransaction.commit();
    }

    @Override
    public void onSearchAddClick() {
    }

    /**-- SearchAddVocToNoteDialogFragment callback --**/
    @Override
    public void onNeedNewNote() {
        Logger.d(TAG, "onNeedNewNote");
        SearchNewNoteDialogFragment dialogFragment = new SearchNewNoteDialogFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(SearchActivity.VIEW_MAIN_RES_ID, dialogFragment, "SearchNewNoteDialogFragment");
        fragmentTransaction.addToBackStack("SearchNewNoteDialogFragment");
        fragmentTransaction.commit();
    }

    /**-- SearchNewNoteDialogFragment callback --**/
    @Override
    public void onNewNoteDone(String string) {
        Logger.d(TAG, "onNewNote" + string);
        SearchAddVocToNoteDialogFragment dialogFragment = new SearchAddVocToNoteDialogFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(SearchActivity.VIEW_MAIN_RES_ID, dialogFragment, "SearchAddVocToNoteDialogFragment");
        fragmentTransaction.addToBackStack("SearchAddVocToNoteDialogFragment");
        fragmentTransaction.commit();
    }
}
