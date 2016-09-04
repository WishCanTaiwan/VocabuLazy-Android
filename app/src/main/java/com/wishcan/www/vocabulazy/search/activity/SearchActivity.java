package com.wishcan.www.vocabulazy.search.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.search.fragment.SearchFragment;

/**
 * Created by SwallowChen on 8/31/16.
 */
public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    public static final int VIEW_MAIN_RES_ID = R.id.activity_search_container;
    private static final int VIEW_RES_ID = R.layout.activity_search;
    private static final int MENU_RES_ID = R.menu.menu_search;
    private static final int MENU_ITEM_SEARCH_RES_ID = R.id.menu_item_search;
    private static final int TOOLBAR_RES_ID = R.id.toolbar;

    private SearchView mSearchActionView;
    private Toolbar mToolbar;
    private SearchFragment mSearchFragment;

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
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof SearchFragment) {
            mSearchFragment = (SearchFragment) fragment;
        } else {

        }
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mSearchFragment.refreshSearchResult(newText);
        return true;
    }
}
