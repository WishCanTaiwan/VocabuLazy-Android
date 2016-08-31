package com.wishcan.www.vocabulazy.search.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.search.fragment.SearchFragment;

/**
 * Created by SwallowChen on 8/31/16.
 */
public class SearchActivity extends AppCompatActivity {
    private static final int VIEW_RES_ID = R.layout.activity_search;
    private static final int TOOLBAR_RES_ID = R.id.toolbar;

    private Toolbar mToolbar;
    private SearchFragment mSearchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(VIEW_RES_ID);
        mToolbar = (Toolbar) findViewById(TOOLBAR_RES_ID);
        setSupportActionBar(mToolbar);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        mSearchFragment = (SearchFragment) fragment;
    }
}
