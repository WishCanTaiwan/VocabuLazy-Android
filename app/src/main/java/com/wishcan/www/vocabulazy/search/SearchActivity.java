package com.wishcan.www.vocabulazy.search;

import android.app.ActionBar;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import java.util.ArrayList;

import com.google.android.gms.analytics.Tracker;
import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.application.VLApplication;
import com.wishcan.www.vocabulazy.search.fragment.SearchFragment;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.databaseObjects.Vocabulary;

//import io.uxtesting.UXTesting;


public class SearchActivity extends FragmentActivity implements SearchView.OnQueryTextListener {

    public static final String TAG = SearchActivity.class.getSimpleName();

    public static final int VIEW_CONTAINER_RES_ID = R.id.activity_search_container;

    private static final int VIEW_ACTIVITY_RES_ID = R.layout.view_search_activity;
    private static final int DEFAULT_MENU_RES_ID = R.menu.menu_search;
    private static final int DEFAULT_SEARCH_ITEM_RES_ID = R.id.action_search;

    private SearchFragment mSearchFragment;
    private SearchView mSearchView;
    private ActionBar mActionBar;
    private Database wDatabase;

    private Tracker wTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        VLApplication application = (VLApplication) getApplication();
        wTracker = application.getDefaultTracker();
        wDatabase = Database.getInstance();

        setContentView(VIEW_ACTIVITY_RES_ID);
        if (savedInstanceState == null) {
            mSearchFragment = new SearchFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(VIEW_CONTAINER_RES_ID, mSearchFragment, "SearchFragment")
                    .commit();
        }
        mActionBar = getActionBar();
        if(mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Log.d(TAG, "Setting screen name: " + TAG);
//        wTracker.setScreenName(TAG);
//        wTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(DEFAULT_MENU_RES_ID, menu);

        MenuItem searchItem = menu.findItem(DEFAULT_SEARCH_ITEM_RES_ID);
        SearchView mSearchView = (SearchView) searchItem.getActionView();

        mSearchView.onActionViewExpanded();          // Important, make ActionView expand initially
        mSearchView.setOnQueryTextListener(this);

        mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
//                    Log.d(TAG, "searchView has focused");
                    if (!isFinishing()) {
                        mSearchFragment.clearSearchDetail();
                        mSearchFragment.clearDialogFragments();
                    }
                } else {
//                    Log.d(TAG, "searchView has not focus");
                }
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ArrayList<Vocabulary> searchResultsList;
        if (newText.equals("")) {
            searchResultsList = new ArrayList<>();
        } else {
            searchResultsList = wDatabase.readSuggestVocabularyBySpell(newText);
        }
        mSearchFragment.refreshSearchResult(searchResultsList);
        return true;
    }
}
