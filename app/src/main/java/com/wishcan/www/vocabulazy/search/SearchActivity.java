package com.wishcan.www.vocabulazy.search;

import android.app.ActionBar;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import java.util.ArrayList;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.wishcan.www.vocabulazy.R;
import com.wishcan.www.vocabulazy.VLApplication;
import com.wishcan.www.vocabulazy.search.fragment.SearchFragment;
import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.Vocabulary;

//import io.uxtesting.UXTesting;


public class SearchActivity extends FragmentActivity {

    public static final String TAG = SearchActivity.class.getSimpleName();

    public static final int VIEW_CONTAINER_RES_ID = R.id.activity_search_container;

    private static final int VIEW_ACTIVITY_RES_ID = R.layout.view_search_activity;
    private static final int DEFAULT_MENU_RES_ID = R.menu.menu_search;
    private static final int DEFAULT_SEARCH_ITEM_RES_ID = R.id.action_search;

    private SearchFragment mSearchFragment;
    private ActionBar mActionBar;
    private static Database mDatabase;

    private Tracker wTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.d(TAG, "onCreate");

        VLApplication application = (VLApplication) getApplication();
        wTracker = application.getDefaultTracker();

        setContentView(VIEW_ACTIVITY_RES_ID);
        if (savedInstanceState == null) {
            mSearchFragment = new SearchFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(VIEW_CONTAINER_RES_ID, mSearchFragment, "SearchFragment")
                    .commit();
        }
        mDatabase = new Database(this);
//        Log.d(TAG, "" + mDatabase);
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
//        Log.d(TAG, "onResume");
//        mDatabase = new Database(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Log.d(TAG, "onPause");
        mDatabase.writeToFile(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        Log.d(TAG, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
//        Log.d(TAG, "onStop");
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        UXTesting.onActivityResult(requestCode, resultCode, data);
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        UXTesting.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(DEFAULT_MENU_RES_ID, menu);

        MenuItem searchItem = menu.findItem(DEFAULT_SEARCH_ITEM_RES_ID);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.onActionViewExpanded();          // Important, make ActionView expand initially
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
                    searchResultsList = mDatabase.readSuggestVocabularyBySpell(newText);
                }
                mSearchFragment.refreshSearchResult(searchResultsList);
//                refreshSearchResult(mSearchResultsList);
                return true;
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
//                if (mNewNoteDialogView != null)
//                    closeDialog(mNewNoteDialogView);
//                if (mDialogView != null)
//                    closeDialog(mDialogView);
//                if (mSearchDetailParentView.getVisibility() == View.VISIBLE)
//                    closeSearchDetail();
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if (mNewNoteDialogView != null)
//            closeDialog(mNewNoteDialogView);
//        else if (mDialogView != null)
//            closeDialog(mDialogView);
//        else {
//            setResult(RESULT_OK, new Intent());
//            super.onBackPressed();
//        }
    }

    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return true;
    }

    public Database getDatabase() {
        return mDatabase;
    }
}
