package com.wishcan.www.vocabulazy;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;

import com.wishcan.www.vocabulazy.storage.Database;
import com.wishcan.www.vocabulazy.storage.Lesson;
import com.wishcan.www.vocabulazy.view.adapter.LinkedListPagerAdapter;
import com.wishcan.www.vocabulazy.view.notes.AddNoteDialogView;
import com.wishcan.www.vocabulazy.view.search.SearchDialogView;
import com.wishcan.www.vocabulazy.view.search.SearchListView;
import com.wishcan.www.vocabulazy.storage.Vocabulary;
import com.wishcan.www.vocabulazy.view.customview.DialogView;
//import com.wishcan.www.vocabulazy.vocabulary.WordObject;

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

    private SearchView mSearchView;

    private MenuItem mSearchItem;

    private Menu mMenu;

    private ActionBar mActionBar;

    private SearchListView mSearchListView;

//    private LinkedList<WordObject> mSearchResultList;

    private LinkedList<String> mNoteList;

    private AddNoteDialogView mNewNoteDialogView;

    private SearchDialogView mDialogView;

    private View mSearchDetailView;

    private ViewGroup mSearchDetailParentView;

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
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        setContentView(DEFAULT_LAYOUT_VIEW_RES_ID);

        Intent intent = getIntent();
        mPreviousTitle = intent.getStringExtra(MainActivity.PREVIOUS_TITLE);

        mActionBar = getActionBar();
        if(mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }

        mSearchListView = (SearchListView) findViewById(R.id.search_list_view);

        mDatabase = new Database(this);

//        mDatabase = intent.getExtras().getParcelable("database");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "onResume");

        if(mSearchDetailParentView == null)
            mSearchDetailParentView = (ViewGroup) findViewById(DEFAULT_SEARCH_DETAIL_PARENT_VIEW);

        mDatabase = new Database(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(TAG, "onPause");

        mDatabase.writeToFile(this);
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
        if (mNewNoteDialogView != null)
            closeDialog(mNewNoteDialogView);
        else if (mDialogView != null)
            closeDialog(mDialogView);
        else {
            setResult(RESULT_OK, new Intent());
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return true;
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
        if(mSearchDetailParentView == null || mDatabase == null)
            return;
        if(mSearchDetailView == null) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(900, 600);
            mSearchDetailView = new ItemDetailLinearLayout(this, vocabulary);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            mSearchDetailView.setLayoutParams(layoutParams);

        }

        View view = this.getCurrentFocus();
        if (view != null) {
            // hide the software keyboard
            InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.clearFocus();
        }

        mSearchDetailParentView.addView(mSearchDetailView);
        mSearchDetailParentView.setVisibility(View.VISIBLE);
        mSearchDetailParentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSearchDetail();
            }
        });

        mSearchDetailView.requestFocus();

        Animator alphaAnim = ObjectAnimator.ofFloat(mSearchDetailParentView, "Alpha", 0f, 1f);
        alphaAnim.setDuration(300);
        alphaAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        alphaAnim.start();
    }

    private void closeSearchDetail(){
        mSearchDetailParentView.setVisibility(View.GONE);
        mSearchDetailParentView.setOnClickListener(null);
        if(mSearchDetailView != null) {
            mSearchDetailParentView.removeView(mSearchDetailView);
            mSearchDetailView = null;
        }
    }

    public void createNoteList() {
        ArrayList<Lesson> notes = mDatabase.getLessonsByBook(-1);
        mNoteList = new LinkedList<>();
        for (int index = 0; index < notes.size(); index++) {
            mNoteList.add(notes.get(index).getName());
        }
    }

    private class ItemDetailLinearLayout extends LinearLayout {

        private Context context;

        private PagerIndexView pagerIndexView;

        private ViewPager viewPager;

        private LinkedList<ViewGroup> mItemPagesList;

        private int pageCount;

        public ItemDetailLinearLayout(Context context, Vocabulary vocabulary) {
            super(context);
            this.context = context;
            setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            setOrientation(VERTICAL);
            ViewGroup itemView = (ViewGroup)((LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.view_search_details, null);
            viewPager = new ViewPager(context);

            ((TextView) itemView.findViewById(mTo[0])).setText(vocabulary.getSpell());
            ((TextView) itemView.findViewById(mTo[1])).setText(vocabulary.getTranslationInOneString());
            ((TextView) itemView.findViewById(mTo[2])).setText(vocabulary.getKK());
            Typeface kkTypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/kk.TTE");
            ((TextView) itemView.findViewById(mTo[2])).setTypeface(kkTypeFace);
            //((TextView) itemView.findViewById(mTo[3])).setText(vocabulary.getEn_Sentence().get(0));
            //((TextView) itemView.findViewById(mTo[4])).setText(vocabulary.getCn_Sentence().get(0));

            ArrayList<String> en_sentences = vocabulary.getEn_Sentence();
            ArrayList<String> cn_sentences = vocabulary.getCn_Sentence();
            pageCount = en_sentences.size();

            pagerIndexView = new PagerIndexView(context, pageCount);
            addView(itemView);
            ((ViewGroup)itemView.findViewById(R.id.pager_parent)).addView(viewPager);
            ((ViewGroup)itemView.findViewById(R.id.pager_index_parent)).addView(pagerIndexView);

            createItemPages(pageCount, en_sentences, cn_sentences);

        }

        private void createItemPages(int pageCount, ArrayList<String> en_sentenceList,
                                     ArrayList<String> cn_sentenceList){


            mItemPagesList = new LinkedList<>();
            for(int i = 0; i < pageCount; i++) {
                ViewGroup currentItemDetailsView =
                        (ViewGroup)((LayoutInflater) getContext()
                                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                                .inflate(R.layout.view_search_details_sentence, null);

                ((TextView) currentItemDetailsView.findViewById(R.id.search_voc_sentence_detail))
                        .setText(en_sentenceList.get(i));
                ((TextView) currentItemDetailsView.findViewById(R.id.search_voc_sentence_translation_detail))
                        .setText(cn_sentenceList.get(i));

                mItemPagesList.add(currentItemDetailsView);
            }
            viewPager.setAdapter(new LinkedListPagerAdapter(mItemPagesList));
            viewPager.addOnPageChangeListener(new OnPageChangeListener());

        }

        protected class OnPageChangeListener implements ViewPager.OnPageChangeListener {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for(int i = 0; i < pageCount; i++) {
                    if(i == position)
                        ((GradientDrawable)((ImageView) pagerIndexView.getChildAt(i)).getDrawable())
                                .setColor(ContextCompat.getColor(context, R.color.player_pager_index_selected));
                    else
                        ((GradientDrawable)((ImageView) pagerIndexView.getChildAt(i)).getDrawable())
                                .setColor(ContextCompat.getColor(context, R.color.player_pager_index_color));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        }
    }

    class PagerIndexView extends LinearLayout{

        private Context context;

        private int pagerCount;

        public PagerIndexView(Context context, int pagerCount) {
            this(context, null, pagerCount);
        }

        public PagerIndexView(Context context, AttributeSet attrs, int pagerCount) {
            super(context, attrs);
            this.context = context;
            this.pagerCount = pagerCount;
            setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            createPagerIndex(this.pagerCount);
        }

        private void createPagerIndex(int indexCount){
            for(int i = 0; i < indexCount; i++){
                ImageView imageView = new ImageView(context);
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.circle_pager_index));
                if(i == 0)
                    ((GradientDrawable)imageView.getDrawable()).setColor(ContextCompat.getColor(context, R.color.player_pager_index_selected));
                else
                    ((GradientDrawable)imageView.getDrawable()).setColor(ContextCompat.getColor(context, R.color.player_pager_index_color));
                imageView.setPadding(5, 5, 5, 5);
                addView(imageView);
            }
        }
    }
}
