package wishcantw.vocabulazy.activities.search.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import wishcantw.vocabulazy.R;
import wishcantw.vocabulazy.activities.ParentActivity;
import wishcantw.vocabulazy.activities.search.fragment.SearchAddVocToNoteDialogFragment;
import wishcantw.vocabulazy.activities.search.fragment.SearchFragment;
import wishcantw.vocabulazy.activities.search.model.SearchModel;
import wishcantw.vocabulazy.activities.search.fragment.SearchNewNoteDialogFragment;
import wishcantw.vocabulazy.utility.Logger;

/**
 * Created by SwallowChen on 8/31/16.
 */
public class SearchActivity extends ParentActivity implements SearchView.OnQueryTextListener,
                                                                 SearchFragment.OnSearchItemEventListener,
        SearchAddVocToNoteDialogFragment.OnAddVocToNoteDialogFinishListener,
        SearchNewNoteDialogFragment.OnNewNoteDialogFinishListener {
    public static final String TAG = "SearchActivity";

    private static final int VIEW_MAIN_RES_ID = R.id.activity_search_container;
    private static final int VIEW_RES_ID = R.layout.activity_search;
    private static final int MENU_RES_ID = R.menu.menu_search;
    private static final int MENU_ITEM_SEARCH_RES_ID = R.id.menu_item_search;
    private static final int TOOLBAR_RES_ID = R.id.toolbar;

    private SearchFragment mSearchFragment;
    private SearchModel mSearchModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set up views
        setContentView(VIEW_RES_ID);
        setSupportActionBar((Toolbar) findViewById(TOOLBAR_RES_ID));

        // enabling HomeAsUp button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // data model
        if (mSearchModel == null) {
            mSearchModel = SearchModel.getInstance();
            mSearchModel.init();
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof SearchFragment) {
            mSearchFragment = (SearchFragment) fragment;
            mSearchFragment.setOnSearchItemEventListener(this);

        } else if (fragment instanceof SearchAddVocToNoteDialogFragment) {
            SearchAddVocToNoteDialogFragment mSearchAddVocToNoteDialogFragment = (SearchAddVocToNoteDialogFragment) fragment;
            mSearchAddVocToNoteDialogFragment.setOnAddVocToNoteDialogFinishListener(this);

        } else if (fragment instanceof SearchNewNoteDialogFragment) {
            SearchNewNoteDialogFragment mSearchNewNoteDialogFragment = (SearchNewNoteDialogFragment) fragment;
            mSearchNewNoteDialogFragment.setOnNewNoteDialogFinishListener(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // todo: we should show a storing dialog 
        // store the data
        mSearchModel.storeData(SearchActivity.this);
    }

//    @Override
//    public void onBackPressed() {
//        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
//            super.onBackPressed();
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /** Inflate the menu; this adds items to the action bar if it is present. */
        getMenuInflater().inflate(MENU_RES_ID, menu);
        SearchView mSearchActionView = (SearchView) menu.findItem(MENU_ITEM_SEARCH_RES_ID).getActionView();

        mSearchActionView.onActionViewExpanded();          // Important, make ActionView expand initially
        mSearchActionView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    public SearchModel getSearchModel() {
        if (mSearchModel == null) {
            mSearchModel = SearchModel.getInstance();
            mSearchModel.init();
        }
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
