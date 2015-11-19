package com.wishcan.www.vocabulazy;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;

import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.Lesson;
import com.wishcan.www.vocabulazy.view.notes.AddNoteDialogView;
import com.wishcan.www.vocabulazy.view.search.SearchDialogView;
import com.wishcan.www.vocabulazy.view.search.SearchListView;
import com.wishcan.www.vocabulazy.storage.Vocabulary;
import com.wishcan.www.vocabulazy.view.customview.DialogView;
import com.wishcan.www.vocabulazy.vocabulary.WordObject;

public class SearchActivity extends Activity {

    private static final String TAG = SearchActivity.class.getSimpleName();

    private static final int DEFAULT_LAYOUT_VIEW_RES_ID = R.layout.activity_search;

    private static final int DEFAULT_LAYOUT_PARENT_RES_ID = R.id.search_view_parent;

    private static final int DEFAULT_MENU_RES_ID = R.menu.menu_search;

    private static final int DEFAULT_SEARCH_ITEM_RES_ID = R.id.action_search;

    private static final int DEFAULT_SEARCH_DETAIL_VIEW = R.id.search_detail_view;

    private static final int DEFAULT_SEARCH_DETAIL_PARENT_VIEW = R.id.search_detail_parent_view;

    private static final int DEFAULT_SEARCH_DETAIL_ITEM0_RES_ID = R.id.search_voc_spell_detail;

    private static final int DEFAULT_SEARCH_DETAIL_ITEM1_RES_ID = R.id.search_voc_translation_detail;

    private static final int DEFAULT_SEARCH_DETAIL_ITEM2_RES_ID = R.id.search_voc_kk_detail;

    private static final int DEFAULT_SEARCH_DETAIL_ITEM3_RES_ID = R.id.search_voc_sentence_detail;

    private static final int DEFAULT_SEARCH_DETAIL_ITEM4_RES_ID = R.id.search_voc_sentence_translation_detail;

    private Vocabulary mVocabulary;

    private SearchView mSearchView;

    private MenuItem mSearchItem;

    private Menu mMenu;

    private ActionBar mActionBar;

    private SearchListView mSearchListView;

    private LinkedList<WordObject> mSearchResultList;

    private LinkedList<String> mNoteList;

    private AddNoteDialogView mNewNoteDialogView;

    private SearchDialogView mDialogView;

    private View mSearchDetailView;

    private View mSearchDetailParentView;

    private Database mDatabase;

    private ArrayList<Vocabulary> mSearchResultsList;

    private String mPreviousTitle;

    private Vocabulary mSelectedVocabulary;

    private int mSelectedVocabularyID;

    private int mSelectedNoteID;

    private String[] mFrom = {
            "voc_spell_detail",
            "voc_translation_detail",
            "voc_kk_detail",
            "voc_sentence_detail",
            "voc_sentence_translation_detail"
    };

    private int[] mTo = {
            DEFAULT_SEARCH_DETAIL_ITEM0_RES_ID,
            DEFAULT_SEARCH_DETAIL_ITEM1_RES_ID,
            DEFAULT_SEARCH_DETAIL_ITEM2_RES_ID,
            DEFAULT_SEARCH_DETAIL_ITEM3_RES_ID,
            DEFAULT_SEARCH_DETAIL_ITEM4_RES_ID
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(DEFAULT_LAYOUT_VIEW_RES_ID);

        Intent intent = getIntent();
        mPreviousTitle = intent.getStringExtra(MainActivity.PREVIOUS_TITLE);

        mActionBar = getActionBar();
        if(mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
        mSearchListView = (SearchListView) findViewById(R.id.search_list_view);

        mDatabase = intent.getExtras().getParcelable("database");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(DEFAULT_MENU_RES_ID, menu);
        mMenu = menu;

        mSearchItem = menu.findItem(DEFAULT_SEARCH_ITEM_RES_ID);
        mSearchView = (SearchView) mSearchItem.getActionView();

        mSearchView.onActionViewExpanded();          // Important, make ActionView expand initially
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.equals("")) {
                    mSearchResultsList = new ArrayList<>();
                } else {
                    mSearchResultsList = mDatabase.readSuggestVocabularyBySpell(newText);
                }

                refreshSearchResult(mSearchResultsList);
                return true;
            }
        });

        mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d("SearchActivity", "TextFocus");
                if (mNewNoteDialogView != null)
                    closeDialog(mNewNoteDialogView);
                if (mDialogView != null)
                    closeDialog(mDialogView);
                if (mSearchDetailParentView.getVisibility() == View.VISIBLE)
                    closeSearchDetail();
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
        Log.d(TAG, "onBackPressed");
        if (mNewNoteDialogView != null)
            closeDialog(mNewNoteDialogView);
        else if (mDialogView != null)
            closeDialog(mDialogView);
        else {
            Log.d(TAG, "onNavigateUp");
            Bundle bundle = new Bundle();
            bundle.putParcelable("database", mDatabase);
            Log.d(TAG, "" + mDatabase.getNoteContents(1).size());
            setResult(RESULT_OK, new Intent().putExtras(bundle));
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mSearchDetailParentView == null)
            mSearchDetailParentView = findViewById(DEFAULT_SEARCH_DETAIL_PARENT_VIEW);
        if(mSearchDetailView == null)
            mSearchDetailView = findViewById(DEFAULT_SEARCH_DETAIL_VIEW);
    }

    public void refreshSearchResult(ArrayList<Vocabulary> vocabularies){
        mSearchListView.refresh(vocabularies);
    }

    public void showListDialog(int index) {

        mSelectedVocabulary = mSearchResultsList.get(index);

        View view = this.getCurrentFocus();
        if (view != null) {
            // hide the software keyboard
            InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.clearFocus();
        }

        final ViewGroup viewGroup = ((ViewGroup)findViewById(DEFAULT_LAYOUT_PARENT_RES_ID));

        if(mDialogView != null)
            viewGroup.removeView(mDialogView);
        else {
            mDialogView = new SearchDialogView(this);
//            LinkedList<String> noteList = getNoteList();
            createNoteList();
            mDialogView.setAdapter(mNoteList);
            mDialogView.setOnListItemClickedListener(new SearchDialogView.OnListItemClickedListener() {
                @Override
                public void onListItemClicked(int listIndex) {
                    Log.d(TAG, "position: " + listIndex);
                    mSelectedNoteID = listIndex + 1;
                    mSelectedVocabularyID = mSelectedVocabulary.getID();
                    mDatabase.addVocToNote(mSelectedVocabularyID, mSelectedNoteID);
                    closeDialog(mDialogView);
                }

                @Override
                public void onAddItemClicked(int position) {
//                    Log.d(TAG, "position: " + position);
                    showNewNoteDialog();
                }
            });
        }

        viewGroup.addView(mDialogView);
        mDialogView.showDialog();

    }

    private void showNewNoteDialog(){

        final ViewGroup viewGroup = ((ViewGroup)findViewById(DEFAULT_LAYOUT_PARENT_RES_ID));
        if(mNewNoteDialogView != null)
            viewGroup.removeView(mNewNoteDialogView);
        else{
            mNewNoteDialogView = new AddNoteDialogView(this);
            mNewNoteDialogView.setOnYesOrNoClickedListener(new DialogView.OnYesOrNoClickedListener() {
                @Override
                public void onYesClicked() {
                    String newNoteName = (String) mNewNoteDialogView.getDialogOutput();

                    mDatabase.createNewNote(newNoteName);
                    createNoteList();
//                    mNoteList.addFirst(newNoteName);
                    mDialogView.setAdapter(mNoteList);

                    closeDialog(mNewNoteDialogView);
                }
                @Override
                public void onNoClicked() {
                    closeDialog(mNewNoteDialogView);
                }
            });
            viewGroup.addView(mNewNoteDialogView);
            mNewNoteDialogView.showDialog();
        }
    }

    public void closeDialog(DialogView dialog){
        if(dialog instanceof SearchDialogView){
            mDialogView = (SearchDialogView) dialog;
            mDialogView.closeDialog();
            mDialogView = null;
        }
        else{
            mNewNoteDialogView = (AddNoteDialogView) dialog;
            mNewNoteDialogView.closeDialog();
            mNewNoteDialogView = null;
        }
    }

    public void showSearchDetail(Vocabulary vocabulary){
        if(mSearchDetailParentView == null || mSearchDetailView == null || mDatabase == null)
            return;

        View view = this.getCurrentFocus();
        if (view != null) {
            // hide the software keyboard
            InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.clearFocus();
        }

        mSearchDetailParentView.setVisibility(View.VISIBLE);
        mSearchDetailParentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSearchDetail();
            }
        });
        mSearchDetailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSearchDetail();
            }
        });

        mSearchDetailView.requestFocus();

        ((TextView) mSearchDetailView.findViewById(mTo[0])).setText(vocabulary.getSpell());
        ((TextView) mSearchDetailView.findViewById(mTo[1])).setText(vocabulary.getTranslate());
        ((TextView) mSearchDetailView.findViewById(mTo[2])).setText(vocabulary.getKK());
        ((TextView) mSearchDetailView.findViewById(mTo[3])).setText(vocabulary.getEn_Sentence().get(0));
        ((TextView) mSearchDetailView.findViewById(mTo[4])).setText(vocabulary.getCn_Sentence().get(0));

        Animator alphaAnim = ObjectAnimator.ofFloat(mSearchDetailView, "Alpha", 0f, 1f);
        alphaAnim.setDuration(300);
        alphaAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        alphaAnim.start();
    }

    private void closeSearchDetail(){
        mSearchDetailParentView.setVisibility(View.GONE);
        mSearchDetailParentView.setOnClickListener(null);
    }

    public void createNoteList() {
        ArrayList<Lesson> notes = mDatabase.getLessonsByBook(-1);
        mNoteList = new LinkedList<>();
        for (int index = 0; index < notes.size(); index++) {
            mNoteList.add(notes.get(index).getName());
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart");
        super.onRestart();
    }
}
